#!groovy
pipeline {
    agent {
        label "docker-build"
    }
    
    options {
        timestamps()
        skipStagesAfterUnstable()
    }

    stages {
        stage('Build Docker image') {
            steps {
                withDockerRegistry([url: 'https://index.docker.io/v1/', credentialsId: 'docker.com-bot']) {
                    sh '''#!/usr/bin/env bash
                        echo "Starting build for Eclipse Codewind ..."
                        mvn -version
                        ./script/build.sh
                    '''
                }
            }
        }  
        
        
        stage('Publish Docker image') {

            // This when clause disables PR build uploads; you may comment this out if you want your build uploaded.
            when {
                beforeAgent true
                not {
                    changeRequest()
                }
            }

            steps {
                withDockerRegistry([url: 'https://index.docker.io/v1/', credentialsId: 'docker.com-bot']) {
                    sh '''#!/usr/bin/env bash
                        echo "Publishing docker images for Eclipse Codewind ..."
                        export REGISTRY="eclipse"
                        
                        if [ "$GIT_BRANCH"=="master" ]; then
                            TAG="latest"
                        else
                            TAG=$GIT_BRANCH
                        fi        

                        declare -a DOCKER_IMAGE_ARRAY=("codewind-initialize-amd64" 
                                                       "codewind-performance-amd64" 
                                                       "codewind-pfe-amd64")

                        for i in "${DOCKER_IMAGE_ARRAY[@]}"
                        do
                            echo "Publishing $i:$TAG"
                            ./scripts/publish.sh $i $REGISTRY $TAG
                        done
                    '''
                }
             }
        } 
    }
}
