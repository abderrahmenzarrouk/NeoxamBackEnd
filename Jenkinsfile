pipeline{
    agent{
        label "jinkins-agent"
    }
    tools{
        jdk "Java17"
        maven "Maven3"
    }
    stages{
        stage("Cleanup Workspace"){
            steps{
                cleanWs()
            }
        }
        
        stage("Checkout from SCM"){
            steps{
                git branch: 'main', credentialsId: 'github', url: 'https://github.com/abderrahmenzarrouk/NeoxamBackEnd'
            }
        }
    
        stage("Build application"){
            steps{
                sh "mvn clean package"
            }
        }

        stage("Test application"){
            steps{
                sh "mvn test"
            }
        }
    }

}