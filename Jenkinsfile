pipeline {
  agent any
  environment {
    // Replace with the correct Nexus docker repository endpoint
    NEXUS_REGISTRY = "localhost:5000"             // or "localhost:8082" depending on Nexus Docker repo port
    IMAGE_NAME = "${NEXUS_REGISTRY}/diwali-sales"
    SONAR_SERVER = "SonarQube"                    // Jenkins SonarQube server name (configured in Jenkins)
    SONAR_TOKEN_CREDENTIAL = "sonar-token"        // Jenkins credential id (secret text)
    NEXUS_CRED = "nexus-credentials"              // Jenkins username/password credentials id
    KUBECONFIG_CRED = "kubeconfig"                // Jenkins secret file credential id (if needed)
  }
  stages {
    stage('Checkout') {
      steps {
        checkout scm
      }
    }

    stage('Unit Tests') {
      steps {
        sh 'mvn -B test'
      }
    }

    stage('SonarQube Analysis') {
      steps {
        withCredentials([string(credentialsId: env.SONAR_TOKEN_CREDENTIAL, variable: 'SONAR_TOKEN')]) {
          // Use Maven Sonar plugin
          sh "mvn -B sonar:sonar -Dsonar.host.url=http://localhost:9000 -Dsonar.login=${SONAR_TOKEN}"
        }
      }
    }

    stage('Build Docker Image') {
      steps {
        script {
          sh "docker build -t ${IMAGE_NAME}:latest ."
        }
      }
    }

    stage('Image Security Scan (Trivy)') {
      steps {
        // fail the build if Trivy returns exit code for high vulnerabilities
        sh '''
          if ! trivy image --exit-code 1 --severity CRITICAL,HIGH ${IMAGE_NAME}:latest ; then
            echo "Trivy found HIGH/CRITICAL vulnerabilities"
            exit 1
          fi
        '''
      }
    }

    stage('Push to Nexus') {
      steps {
        withCredentials([usernamePassword(credentialsId: env.NEXUS_CRED, usernameVariable: 'NEXUS_USER', passwordVariable: 'NEXUS_PASS')]) {
          sh '''
            echo "$NEXUS_PASS" | docker login ${NEXUS_REGISTRY} -u "$NEXUS_USER" --password-stdin
            docker push ${IMAGE_NAME}:latest
          '''
        }
      }
    }

    stage('Deploy to Kubernetes (Minikube)') {
      steps {
        // two possible methods:
        // 1) Use kubectl installed on Jenkins and use kubeconfig via file credential
        // 2) Use kubernetes plugin/agent. Here we show kubeconfig file method
        withCredentials([file(credentialsId: env.KUBECONFIG_CRED, variable: 'KUBECONFIG_FILE')]) {
          sh '''
            export KUBECONFIG=${KUBECONFIG_FILE}
            # Update image in k8s manifest and apply
            kubectl set image deployment/diwali-sales diwali-sales=${IMAGE_NAME}:latest --namespace default || true
            kubectl apply -f k8s/deployment.yaml
            kubectl apply -f k8s/service.yaml
          '''
        }
      }
    }
  }

  post {
    always {
      cleanWs()
    }
    failure {
      mail to: 'you@example.com', subject: "Pipeline failed: ${env.JOB_NAME}", body: "Check Jenkins"
    }
  }
}
