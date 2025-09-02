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
            agent { 
                label 'slave1'  // Use actual node labels configured in Jenkins
            }
            steps {
                echo "Checking out code on node: ${env.NODE_NAME}"
                git branch: 'master', url: 'https://github.com/ady24s/mavenapp_jenkins.git'
                
                // Store workspace info for other stages
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
            agent { 
                label 'slave1'  // Same node as checkout to reuse workspace
            }
            steps {
                echo "Building on node: ${env.NODE_NAME}"
                
                // Re-checkout if workspace is clean (safety measure)
                script {
                    if (!fileExists('pom.xml')) {
                        git branch: 'master', url: 'https://github.com/ady24s/mavenapp_jenkins.git'
                    }
                }
                
                // Use sh instead of bat for Linux nodes, bat for Windows
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
            agent { 
                label 'slave2'  // Different node for testing
            }
            steps {
                echo "Testing on node: ${env.NODE_NAME}"
                
                // Checkout code on test node
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
                    // Archive test results regardless of outcome
                    publishTestResults testResultsPattern: 'target/surefire-reports/*.xml'
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
            agent { 
                label 'slave1'  // Back to build node
            }
            steps {
                echo "Packaging on node: ${env.NODE_NAME}"
                
                // Ensure we have the built code
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
                
                // Archive artifacts
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