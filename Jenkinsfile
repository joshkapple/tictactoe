pipeline {
    agent { docker { image 'hseeberger/scala-sbt:11.0.8_1.3.13_2.13.3' } }
    stages {
        stage('build') {
            steps {
                sh 'sbt --version'
                sh 'sbt package'
            }
        }
    }
}
