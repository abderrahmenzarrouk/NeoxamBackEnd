pipeline {
    agent {
        label "jinkins-agent" // Adjust this to your Jenkins agent label
    }
    tools {
        jdk "Java17"
        maven "Maven3"
    }
    environment {
        APP_NAME = "neoxambackend"
        RELEASE = "1.0.0"
        DOCKER_USER = "zarroukabderrahmen"
        DOCKER_PASS = 'dockerhub'
        IMAGE_NAME = "${DOCKER_USER}" + "/" + "${APP_NAME}"
        IMAGE_TAG = "${RELEASE}-${BUILD_NUMBER}"


    }
    stages {
        stage("Cleanup Workspace") {
            steps {
                cleanWs()
                echo "Workspace cleaned up."
            }
        }

        stage("Checkout from SCM") {
            steps {
                git branch: 'main', credentialsId: 'github', url: 'https://github.com/abderrahmenzarrouk/NeoxamBackEnd'
                echo "Checked out code from SCM."
            }
        }

        stage("Clean Up Existing Containers") {
            steps {
                script {
                    sh "docker ps -q --filter 'name=mysql' | xargs -r docker rm -f"
                    sh "docker ps -q --filter 'name=backend' | xargs -r docker rm -f"
                }
                echo "Cleaned up existing containers."
            }
        }

        stage("Setup MySQL Container") {
            steps {
                script {
                    echo "Starting MySQL container"
                    try {
                        // Check if a container named 'mysql' exists (running or stopped)
                        def mysqlExists = sh(script: "docker ps -a -q -f name=mysql", returnStdout: true).trim()

                        if (mysqlExists) {
                            echo "MySQL container exists. Removing it."
                            sh "docker rm -f mysql" // Force remove any existing 'mysql' container
                        }

                        // Start a new MySQL container
                        sh "docker run -d --name mysql -e MYSQL_ROOT_PASSWORD=root -e MYSQL_DATABASE=neoxame -p 3306:3306 mysql:latest"
                        echo "MySQL container started."

                        // Check MySQL container status
                        def mysqlStatus = sh(script: "docker ps --filter 'name=mysql' --format '{{.Names}}'", returnStdout: true).trim()

                        if (mysqlStatus == "mysql") {
                            echo "MySQL container is running."

                            // Get the MySQL container IP address
                            def mysqlIp = sh(script: "docker inspect -f '{{range .NetworkSettings.Networks}}{{.IPAddress}}{{end}}' mysql", returnStdout: true).trim()
                            echo "MySQL container IP address: ${mysqlIp}"
                        } else {
                            error "MySQL container is not running."
                        }
                    } catch (Exception e) {
                        echo "Failed to set up MySQL container."
                        throw e
                    }
                }
            }
        }



        stage("Build and Run Backend Container") {
            steps {
                script {
                    echo "Starting to build and run backend container"
                    try {
                        // Check if a container named 'backend' exists (running or stopped)
                        def backendExists = sh(script: "docker ps -a -q -f name=backend", returnStdout: true).trim()

                        if (backendExists) {
                            echo "Backend container exists. Removing it."
                            sh "docker rm -f backend" // Force remove any existing 'backend' container
                        }

                        // Build the backend container
                        sh "docker build -t backend -f Dockerfile ."

                        // Run the backend container
                        sh "docker run -d --name backend --link mysql:mysql -e SPRING_DATASOURCE_URL=jdbc:mysql://mysql:3306/neoxame -e SPRING_DATASOURCE_USERNAME=root -e SPRING_DATASOURCE_PASSWORD=root backend"
                        echo "Backend container built and started successfully."

                        // Check Docker logs
                        sh "docker logs backend"

                        // Ensure MySQL container is still running
                        def mysqlStatus = sh(script: "docker ps --filter 'name=mysql' --format '{{.Names}}'", returnStdout: true).trim()
                        echo "MySQL container status: ${mysqlStatus}"

                        if (mysqlStatus != "mysql") {
                            error "MySQL container is not running; backend container setup may fail."
                        }
                    } catch (Exception e) {
                        echo "Failed to build and run backend container."
                        throw e
                    }
                }
            }
        }


        stage("List MySQL Tables") {
            steps {
                script {
                    try {
                        sh "docker exec mysql mysql -u root -proot -e 'SHOW TABLES FROM neoxame;'"
                        echo "Listed MySQL tables."
                    } catch (Exception e) {
                        echo "Failed to list MySQL tables."
                        throw e
                    }
                }
            }
        }

        stage("Test application") {
            steps {
               sh "mvn test"
            }
        }

        stage("Sonarqube Analysis") {
            steps {
                script {
                    withSonarQubeEnv(credentialsId: 'jenkins-sonarqube-token') {
                        sh "mvn sonar:sonar"
                    }
                }
            }

        }

        stage("Build & Push Docker Image") {
            steps {
                script {
                    docker.withRegistry('',DOCKER_PASS) {
                        docker_image = docker.build "${IMAGE_NAME}"
                    }

                    docker.withRegistry('',DOCKER_PASS) {
                        docker_image.push("${IMAGE_TAG}")
                        docker_image.push('latest')
                    }
                }
            }

        }
    }
}
