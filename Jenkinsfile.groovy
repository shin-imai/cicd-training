podTemplate(containers: [
    containerTemplate(name: 'kaniko', image: 'gcr.io/kaniko-project/executor:debug', ttyEnabled: true, command: 'cat')
  ]) {

    node(POD_LABEL) {
        stage('Get a Golang project') {
            git url: "https://github.com/shin-imai/cicd-training.git"
            container('kaniko') {
                stage('Build a Go project') {
                sh"""#!/busybox/sh
                /kaniko/executor -f ./dockerfile -c `pwd` --insecure --destination=registry.default.svc.cluster.local/bjss-node:1.0.0
                """
                }
            }
        }

    }
}
