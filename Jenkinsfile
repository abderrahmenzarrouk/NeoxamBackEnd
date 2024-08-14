pipeline {
    agent {
        label "jinkins-agent"
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

        stage('Setup MySQL') {
            steps {
                script {
                    // Remove existing MySQL container if it exists
                    sh "docker ps -a --filter name=mysql --format '{{.Names}}' | grep -w mysql && docker stop mysql || true"
                    sh "docker ps -a --filter name=mysql --format '{{.Names}}' | grep -w mysql && docker rm mysql || true"

                    // Run new MySQL container
                    sh "docker run -d --name mysql -e MYSQL_ROOT_PASSWORD=rootpassword -e MYSQL_DATABASE=neoxame -p 3306:3306 mysql:latest"

                    // Wait for MySQL to be ready
                    waitUntil {
                        script {
                            def result = sh(script: "docker exec mysql mysqladmin ping -h localhost --silent", returnStatus: true)
                            return result == 0
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