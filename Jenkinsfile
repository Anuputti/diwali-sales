pipeline {
    agent any

    environment {
        IMAGE_NAME = "springboot-app"
        IMAGE_TAG = "v1"
        REGISTRY = "localhost:5000"
    }

    stages {
        stage('Checkout') {
            steps {
                git branch: 'main', url: 'https://github.com/Anuputti/diwali-sales.git'
            }
        }

        stage('Build') {
            steps {
                sh 'mvn clean package -DskipTests'
            }
        }

        stage('Docker Build & Push') {
            steps {
                sh '''
                docker build -t localhost:5000/diwali-sales:latest .
                docker push localhost:5000/diwali-sales:latest
                '''
            }
        }
    }

    post {
        success { echo "🎉 Build and Push successful!" }
        failure { echo "❌ Build failed!" }
    }
}
