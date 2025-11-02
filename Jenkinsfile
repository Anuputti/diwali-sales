pipeline {
    agent any

    environment {
        IMAGE_NAME = "springboot-app"
        IMAGE_TAG = "v1"
        REGISTRY = "localhost:5000"   // Change to your Docker Hub if needed
        // For DockerHub: REGISTRY = "docker.io/yourusername"
        // Then full image would be "${REGISTRY}/${IMAGE_NAME}:${IMAGE_TAG}"
    }

    stages {
        stage('Checkout') {
            steps {
                echo 'Cloning repository...'
                git branch: 'main', url: 'https://github.com/Anuputti/<your-repo>.git'
            }
        }

        stage('Build') {
            steps {
                echo 'Building the project...'
                sh 'mvn clean package -DskipTests'
            }
        }

        stage('Build Docker Image') {
            steps {
                echo 'Building Docker image...'
                sh 'docker build -t ${IMAGE_NAME}:${IMAGE_TAG} .'
            }
        }

        stage('Tag & Push Image') {
            steps {
                echo 'Tagging and pushing image...'
                // For local registry:
                sh '''
                    docker tag ${IMAGE_NAME}:${IMAGE_TAG} ${REGISTRY}/${IMAGE_NAME}:${IMAGE_TAG}
                    docker push ${REGISTRY}/${IMAGE_NAME}:${IMAGE_TAG}
                '''
                // For Docker Hub:
                // withCredentials([usernamePassword(credentialsId: 'dockerhub-creds', usernameVariable: 'USER', passwordVariable: 'PASS')]) {
                //     sh '''
                //         echo "$PASS" | docker login -u "$USER" --password-stdin
                //         docker tag ${IMAGE_NAME}:${IMAGE_TAG} ${REGISTRY}/${IMAGE_NAME}:${IMAGE_TAG}
                //         docker push ${REGISTRY}/${IMAGE_NAME}:${IMAGE_TAG}
                //     '''
                // }
            }
        }

        stage('Clean Up') {
            steps {
                echo 'Cleaning up...'
                sh 'docker rmi ${IMAGE_NAME}:${IMAGE_TAG} || true'
            }
        }
    }

    post {
        success {
            echo "🎉 Build and Push successful!"
        }
        failure {
            echo "❌ Build failed!"
        }
    }
}

