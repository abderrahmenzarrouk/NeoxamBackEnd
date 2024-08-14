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

        stage("Build and Run Containers") {
            steps {
                script {
                    try {
                        sh "docker-compose up --build -d"
                        echo "Built and started Docker containers."
                    } catch (Exception e) {
                        echo "Failed to build and run Docker containers."
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
