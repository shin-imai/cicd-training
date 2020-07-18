/*
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
                ls
                #/kaniko/executor -f ./dockerfile -c `pwd` --destination=registry.default.svc.cluster.local/bjss-node:1.0.0 --verbosity=debug
                """
                }
            }
        }
    }
}
*/

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

        if(BRANCH_NAME.startsWith("PR-")){
            withCredentials([usernamePassword(credentialsId: 'githubPAT', usernameVariable: "USER", passwordVariable: 'PAT')]) {
                stage("push"){
                    sh"""
                    git checkout -b ${CHANGE_BRANCH}
                    git remote add github https://${PAT}@github.com/shin-imai/cicd-training.git
                    git branch -a
                    git fetch github 
                    git reset --hard HEAD^1
                    git push -u github ${CHANGE_BRANCH}
                    """
                }

                stage("Create PR"){
                    sh"""
                    curl -XPOST -d@- -H "Content-Type: application/json" -H "Authorization: Token ${PAT}" 'https://api.github.com/repos/shin-imai/cicd-training/pulls' <<EOF
{
  "title": "Amazing new feature",
  "body": "Please pull these awesome changes in!",
  "head": "${CHANGE_BRANCH}",
  "base": "${CHANGE_TARGET}"
}

EOF
                    """
                }
            }
        }
    }

}
