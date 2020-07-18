podTemplate(containers: [
    containerTemplate(name: 'kaniko', image: 'gcr.io/kaniko-project/executor:debug', ttyEnabled: true, command: 'cat')
  ]) {

    node(POD_LABEL) {
        stage("Checkout"){
            checkout(scm)
        }
        stage('kaniko job') {
            container('kaniko') {
                stage('Build the image') {
                sh"""#!/busybox/sh
                /kaniko/executor -f ./dockerfile -c `pwd` --destination=registry.default.svc.cluster.local/bjss-node:1.0.0 --verbosity=debug
                """
                }
            }
        }
    }
}

podTemplate(containers: [
    containerTemplate(name: 'bjss-node', image: 'registry.default.svc.cluster.local/bjss-node:1.0.0', args: "index.js")
  ]) {

    node(POD_LABEL){
        stage("Checkout"){
            checkout(scm)
        }

        stage("test"){
            sh(script: "test/test.sh")
        }

        if(! BRANCH_NAME.startWith("PR-"){
            stage("push"){
                withCredentials([usernamePassword(credentialsId: 'githubPAT', usernameVariable: "USER", passwordVariable: 'PAT')]) {
                    sh"""
                    env
                    git remote add github https://${PAT}@github.com/shin-imai/cicd-training.git
                    git push -u github ${BRANCH_NAME}
                    """
                }
            }

            stage("Create PR"){
                sh"""
                env
                """
            }
        }
    }

}
