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
            }
        }

        stage("Checkout from SCM") {
            steps {
                git branch: 'main', credentialsId: 'github', url: 'https://github.com/abderrahmenzarrouk/NeoxamBackEnd'
            }
        }

        stage("Clean Up Existing Containers") {
            steps {
                script {
                    sh "docker ps -q --filter 'name=mysql' | xargs -r docker rm -f"
                }
            }
        }

        stage("Build and Run Containers") {
            steps {
                script {
                    sh "docker-compose up --build -d"

                    // Wait for MySQL to be ready
                    waitUntil {
                        script {
                            def result = sh(script: "docker-compose exec mysql mysqladmin ping -h localhost --silent", returnStatus: true)
                            if (result != 0) {
                                // Print container logs if MySQL is not ready
                                sh "docker-compose logs mysql"
                            }
                            return result == 0
                        }
                    }
                }
            }
        }

        stage("Build application") {
            steps {
                sh "docker-compose exec app mvn clean package"
            }
        }

        stage("Test application") {
            when {
                branch 'main' // Run tests only on the main branch
            }
            steps {
                sh "docker-compose exec app mvn test"
            }
        }
    }
    post {
        always {
            // Clean up Docker containers
            sh "docker-compose down"
        }
    }
}