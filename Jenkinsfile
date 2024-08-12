pipeline {
    agent {
        label "jinkins-agent"
    }
    tools {
        jdk "Java17"
        maven "Maven3"
    }
    environment {
        MYSQL_URL = 'jdbc:mysql://localhost:3306/neoxame'
        MYSQL_USER = 'root'
        MYSQL_PASSWORD = 'password'
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

        stage("Setup MySQL") {
            steps {
                script {
                    // Start a MySQL container
                    docker.image('mysql:latest').withRun('-e MYSQL_ROOT_PASSWORD=${MYSQL_PASSWORD} -e MYSQL_DATABASE=neoxame -p 3306:3306') {
                        // Wait for MySQL to start up
                        sleep(time: 30, unit: 'SECONDS')
                    }
                }
            }
        }

        stage("Build application") {
            steps {
                sh "mvn clean package"
            }
        }

        stage("Test application") {
            steps {
                sh "mvn test"
            }
        }
    }
}
