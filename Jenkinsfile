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
                }
                echo "Cleaned up existing MySQL containers."
            }
        }

        stage("Setup MySQL Container") {
            steps {
                script {
                    echo "Starting MySQL container"
                    try {
                        sh "docker-compose -f mysql-docker-compose.yml up --build -d mysql"

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

        stage("Build and Run Containers") {
            steps {
                script {
                    echo "Starting to build and run backend container"
                    try {
                        sh "docker-compose up --build -d backend"
                        echo "Docker Compose command executed"

                        // Check Docker Compose logs
                        sh "docker-compose logs"

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

        stage("Build application") {
            steps {
                script {
                    try {
                        sh "docker-compose exec backend mvn clean package"
                        echo "Application built successfully."
                    } catch (Exception e) {
                        echo "Failed to build the application."
                        throw e
                    }
                }
            }
        }

        stage("List MySQL Tables") {
            steps {
                script {
                    try {
                        sh "docker-compose exec mysql mysql -u root -proot -e 'SHOW TABLES FROM neoxame;'"
                        echo "Listed MySQL tables."
                    } catch (Exception e) {
                        echo "Failed to list MySQL tables."
                        throw e
                    }
                }
            }
        }

        stage("Test application") {
            when {
                branch 'main' // Run tests only on the main branch
            }
            steps {
                script {
                    try {
                        sh "docker-compose exec backend mvn test -Dspring.profiles.active=docker"
                        echo "Application tests completed."
                    } catch (Exception e) {
                        echo "Failed to run application tests."
                        throw e
                    }
                }
            }
        }
    }
}
