pipeline {
    agent {
        label "jenkins-agent"
    }
    tools {
        jdk "Java17"
        maven "Maven3"
    }
    environment {
        MYSQL_URL = 'jdbc:mysql://localhost:3306/neoxame' // Corrected URL
        MYSQL_USER = 'root'
        MYSQL_PASSWORD = 'root'
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
                    // Start a MySQL container and keep it running in the background
                    docker.image('mysql:latest').withRun('-e MYSQL_ROOT_PASSWORD=${MYSQL_PASSWORD} -e MYSQL_DATABASE=neoxame -p 3306:3306 --name mysql') { c ->
                        // Wait for MySQL to be ready
                        waitUntil {
                            script {
                                def ready = sh(script: "docker exec ${c.id} mysqladmin ping -h localhost --silent", returnStatus: true)
                                return (ready == 0)
                            }
                        }
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
            when {
                branch 'main' // Run tests only on the main branch
            }
            steps {
                sh "mvn test"
            }
        }
    }
}
