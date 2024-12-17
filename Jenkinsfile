pipeline {
    agent any  // Используем любой доступный агент

    agent {
            docker { image 'docker:latest' }
        }


    environment {
        DOCKER_IMAGE = 'dostavista-image'
        CONTAINER_NAME = 'my-app-container'
    }

    stages {

        stage('Check Docker Version') {
            steps {
                sh 'docker --version'
            }
        }
        // Шаг для извлечения кода из репозитория
        stage('Checkout') {
            steps {
                git branch: 'main', url: 'https://github.com/Irvanev/DostavistaAPI.git'
            }
        }

        // Шаг для сборки проекта с помощью Gradle
        stage('Build') {
            steps {
                script {
                    // Убедитесь, что Gradle и Java доступны на агенте
                    sh './gradlew clean build -x test'
                }
            }
        }

        // Шаг для создания Docker-образа
        stage('Build Docker Image') {
            steps {
                script {
                    sh 'docker build -t ${DOCKER_IMAGE} .'
                }
            }
        }

        // Шаг для запуска Docker контейнера
        stage('Run Docker Container') {
            steps {
                script {
                    // Остановим и удалим контейнер, если он уже существует
                    sh '''
                        docker ps -q -f name=${CONTAINER_NAME} | xargs -r docker stop
                        docker ps -q -f name=${CONTAINER_NAME} | xargs -r docker rm
                        docker run -d --name ${CONTAINER_NAME} -p 8080:8080 ${DOCKER_IMAGE}
                    '''
                }
            }
        }
    }

    post {
        success {
            echo 'Deployment was successful!'
        }
        failure {
            echo 'Deployment failed!'
        }
    }
}