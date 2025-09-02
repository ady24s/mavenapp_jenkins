pipeline {
    agent none  // Master won't run stages

    tools {
        maven 'Maven 3.9.9'
        jdk 'jdk21'
    }

    environment {
        MAVEN_OPTS = '-Dmaven.test.failure.ignore=true'
    }

    stages {
        stage('Checkout') {
            agent { label 'slave' }  // Run on your slave node
            steps {
                echo "Checking out code on node: ${env.NODE_NAME}"
                git branch: 'master', url: 'https://github.com/ady24s/mavenapp_jenkins.git'
                
                script {
                    env.WORKSPACE_PATH = pwd()
                }
            }
            post {
                success {
                    echo "Checkout completed successfully on ${env.NODE_NAME}"
                }
            }
        }

        stage('Build') {
            agent { label 'slave' }
            steps {
                echo "Building on node: ${env.NODE_NAME}"
                sh 'mvn clean compile'
            }
            post {
                success {
                    echo "Build completed successfully on ${env.NODE_NAME}"
                }
                failure {
                    echo "Build failed on ${env.NODE_NAME}"
                }
            }
        }

        stage('Test') {
            agent { label 'slave' }
            steps {
                echo "Testing on node: ${env.NODE_NAME}"
                sh 'mvn test'
            }
            post {
                always {
                    // Use junit instead of publishTestResults
                    junit 'target/surefire-reports/*.xml'
                }
                success {
                    echo "Tests passed on ${env.NODE_NAME}"
                }
                failure {
                    echo "Tests failed on ${env.NODE_NAME}"
                }
            }
        }

        stage('Package & Archive') {
            agent { label 'slave' }
            steps {
                echo "Packaging on node: ${env.NODE_NAME}"
                sh 'mvn package -DskipTests'
                archiveArtifacts artifacts: 'target/*.jar', fingerprint: true, allowEmptyArchive: true
            }
            post {
                success {
                    echo "Packaging completed successfully on ${env.NODE_NAME}"
                }
            }
        }
    }

    post {
        always {
            echo "Pipeline execution completed"
        }
        success {
            echo "Pipeline succeeded! Artifacts available for download."
        }
        failure {
            echo "Pipeline failed. Check logs for details."
        }
        cleanup {
            echo "Cleaning up workspace on all nodes"
        }
    }
}
