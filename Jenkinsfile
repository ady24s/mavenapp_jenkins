pipeline {
    agent none  // Master won't run any stage

    tools {
        maven 'Maven 3.9.9'
        jdk 'jdk21'
    }

    stages {
        stage('Checkout') {
            agent { label 'build-node' } // slave1
            steps {
                git branch: 'master', url: 'https://github.com/ady24s/mavenapp_jenkins.git'
            }
        }

        stage('Build') {
            agent { label 'build-node' } // slave1
            steps {
                bat 'mvn clean compile'
            }
        }

        stage('Test') {
            agent { label 'test-node' } // slave2
            steps {
                bat 'mvn test'
            }
        }

        stage('Package & Archive') {
            agent { label 'build-node' } // slave1
            steps {
                bat 'mvn package'
                archiveArtifacts artifacts: 'target/devopsmavenapp-1.0-SNAPSHOT.jar', fingerprint: true
            }
        }
    }
}
