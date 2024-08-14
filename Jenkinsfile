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

                }
            }
        }

        stage("Build application") {
            steps {
                // Ensure environment variables are set
                sh "docker-compose exec backend mvn clean package"
                echo "Built and started Docker containers."
            }

        }

        stage("List MySQL Tables") {
            steps {
                script {
                    sh "docker-compose exec mysql mysql -u root -proot -e 'SHOW TABLES FROM neoxame;'"
                }
            }
        }

        stage("Test application") {
            when {
                branch 'main' // Run tests only on the main branch
            }
            steps {
                // Ensure the tests run with the correct environment variables
                sh "docker-compose exec backend mvn test -Dspring.profiles.active=docker"
            }
        }
    }
}
