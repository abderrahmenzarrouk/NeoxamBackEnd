pipeline {
    agent {
        label "jinkins-agent" // Adjust this to your Jenkins agent label
    }
    tools {
        jdk "Java17"
        maven "Maven3"
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
                        sh "docker run -d --name mysql -e MYSQL_ROOT_PASSWORD=root -e MYSQL_DATABASE=neoxame -p 3306:3306 mysql:latest"

                        // Check MySQL container status
                        def mysqlStatus = sh(script: "docker ps --filter 'name=mysql' --format '{{.Names}}'", returnStdout: true).trim()
                        echo "MySQL container status: ${mysqlStatus}"

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
                        sh "docker build -t backend -f Dockerfile ."
                        sh "docker run -d --name backend --link mysql:mysql -e SPRING_DATASOURCE_URL=jdbc:mysql://mysql:3306/neoxame -e SPRING_DATASOURCE_USERNAME=root -e SPRING_DATASOURCE_PASSWORD=root backend"
                        echo "Backend container built and started successfully."

                        // Check Docker logs
                        sh "docker logs backend"

                        // Ensure MySQL container is still running
                        def mysqlStatus = sh(script: "docker ps --filter 'name=mysql' --format '{{.Names}}'", returnStdout: true).trim()
                        echo "MySQL container status: ${mysqlStatus}"

                        if (mysqlStatus != "mysql") {
                            error "MySQL container is not running, backend container setup may fail."
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
    }
}
