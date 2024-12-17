pipeline {
    agent any  // Используем любой доступный агент

    environment {
        DOCKER_IMAGE = 'dostavista-image'
        CONTAINER_NAME = 'my-app-container'
    }

    stages {
        stage('Check Docker Version') {
            steps {
                sh 'echo $PATH'
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
                    // Остановим и удалим существующий контейнер, если он есть
                    sh '''
                        if [ $(docker ps -a -q -f name=${CONTAINER_NAME}) ]; then
                            docker stop ${CONTAINER_NAME} || true
                            docker rm ${CONTAINER_NAME} || true
                        fi
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
