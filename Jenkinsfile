pipeline {
  agent any

  stages {
      stage('Build Artifact') {
            steps {
              sh "mvn clean package -DskipTests=true"
              archive 'target/*.jar' //testing webhook
            }
        }   
      stage('Unit Tests') {
            steps {
              sh "mvn test"
            }
            post {
              always{
                junit 'target/surefire-reports/*.xml'
                jacoco execPattern: 'target/jacoco.exec'
              }
              
            }
        }   
      stage ('Docker Build and Push'){
        steps{
          withDockerRegistry([credentialsId: 'dockerhub', url: '']) {
            sh 'printenv'
            sh 'docker build -t chmadhus/numeric-app:""$GIT_COMMIT"" .'
            sh 'docker push chmadhus/numeric-app:""$GIT_COMMIT""'
          }
        }
      }
      stage ('K8s Deployment - DEV') {
        steps {
          withKubeConfig([credentialsId: 'kubeconfig']) {
            sh "sed -i 's#replace#chmadhus/numeric-app:${GIT_COMMIT}#g' k8s_deployment_service.yaml"
            sh "kubectl apply -f k8s_deployment_service.yaml"
          }
        }
      }
    }
}