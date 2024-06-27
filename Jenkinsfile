pipeline {
  agent any

  stages {
    stage('Build') {
      steps {
        sh 'export MAVEN_OPTS="-Xmx512M -XX:MaxPermSize=512m" && mvn -B clean package'
      }
    }

    stage('Test') {
      steps {
        sh 'mvn test'
      }
    }
  }
}