pipeline {
    agent any

    stages {
        stage('Build') {
            steps {
                checkout scmGit(branches: [[name: '*/feature/sprint_1_dev']], extensions: [], userRemoteConfigs: [[credentialsId: 'f6dc9638-c900-4266-af16-0828771670ba', url: 'https://ankittechsopi@bitbucket.org/techsopi-timesheet/timesheet.git']])
                sh 'mvn clean install'
            }
        }

        stage('Containerized the application') {
            steps {
                script {
                    // Stop and remove existing containers
                    sh 'docker rm -f $(docker ps -qa) || true'

                    // Run the new container
                    sh "docker-compose down"

                    // Start MySQL service inside the container
                    sh "cat my.cnf && docker-compose up -d"

                    // Change to the application directory and run the JAR file
                    //sh '''
                    //docker exec timesheetapplication${BUILD_ID} sh -c "cd /pro/client/target && nohup java -jar timesheet-0.0.1-SNAPSHOT.jar --spring.config.location=/pro/client/src/main/resources/application.properties > timesheetLogs.txt 2>&1 &"
                    //'''
                }
            }
        }
    }
}