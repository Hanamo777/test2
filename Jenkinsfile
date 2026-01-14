pipeline {
    agent any
    
    environment {
        DOCKER_IMAGE = 'bless'
        DOCKER_TAG = "${env.BUILD_NUMBER}"
    }
    
    triggers {
        githubPush()
    }
    
    stages {
        stage('Checkout') {
            steps {
                echo "*** [Checkout Stage] 시작 ***"
                checkout scm
                sh """
                    echo "--- 진행 ---"       
                    
                """
                echo "*** [Checkout Stage] 완료 ***"
            }
        }
        
        stage('Build') {
            steps {
                echo "*** [Build Stage] 시작 ***"
                sh """
                    # Jenkins 컨테이너를 제외한 모든 실행 중인 컨테이너 중지
                    docker ps -q | grep -v \$(docker ps -q --filter name=jenkins-server) | xargs -r docker stop
                    
                    # Jenkins 컨테이너를 제외한 모든 컨테이너 삭제
                    docker ps -aq | grep -v \$(docker ps -q --filter name=jenkins-server) | xargs -r docker rm
                    
                    # 사용하지 않는 모든 리소스 삭제
                    docker system prune -af
                    
                    # 새로운 이미지 빌드
                    docker compose -f docker-compose-dev.yml build
                """
                echo "*** [Build Stage] 완료 ***"
            }
        }
        
        stage('Deploy') {
            steps {
                echo "*** [Deploy Stage] 시작 ***"
                sh """
                    docker compose -f docker-compose-dev.yml up -d
                """
                echo "*** [Deploy Stage] 완료 ***"
            }
        }
    }
    
    post {
        always {
            echo "*** [Post Actions] 시작 ***"
            sh """
                echo "--- 컨테이너 상태 ---"
                docker ps -a
            """
            cleanWs()
            echo "*** [Post Actions] 완료: 작업 공간 정리됨 ***"
        }
    }
} 