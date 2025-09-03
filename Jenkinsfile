pipeline {
    agent none  // Master won't run any stage

    tools {
        maven 'Maven 3.9.9'
        jdk 'jdk21'
    }

    environment {
        MAVEN_OPTS = '-Dmaven.test.failure.ignore=true'
    }

    stages {
        stage('Checkout') {
            agent any
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
            agent any
            steps {
                echo "Building on node: ${env.NODE_NAME}"
                
                script {
                    if (!fileExists('pom.xml')) {
                        git branch: 'master', url: 'https://github.com/ady24s/mavenapp_jenkins.git'
                    }
                }
                
                script {
                    if (isUnix()) {
                        sh 'mvn clean compile'
                    } else {
                        bat 'mvn clean compile'
                    }
                }
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
            agent any
            steps {
                echo "Testing on node: ${env.NODE_NAME}"
                
                git branch: 'master', url: 'https://github.com/ady24s/mavenapp_jenkins.git'
                
                script {
                    if (isUnix()) {
                        sh 'mvn test'
                    } else {
                        bat 'mvn test'
                    }
                }
            }
            post {
                always {
                    // Archive JUnit test results
                    junit '**/target/surefire-reports/*.xml'
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
            agent any
            steps {
                echo "Packaging on node: ${env.NODE_NAME}"
                
                script {
                    if (!fileExists('target/classes')) {
                        git branch: 'master', url: 'https://github.com/ady24s/mavenapp_jenkins.git'
                        if (isUnix()) {
                            sh 'mvn clean compile'
                        } else {
                            bat 'mvn clean compile'
                        }
                    }
                }
                
                script {
                    if (isUnix()) {
                        sh 'mvn package -DskipTests'
                    } else {
                        bat 'mvn package -DskipTests'
                    }
                }
                
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
//////helooo