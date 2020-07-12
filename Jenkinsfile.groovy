podTemplate(containers: [
    containerTemplate(name: 'kaniko', image: 'gcr.io/kaniko-project/executor:debug', ttyEnabled: true, command: 'cat')
  ]) {

    node(POD_LABEL) {
        stage('Get a Golang project') {
            container('kaniko') {
                stage('Build a Go project') {
                sh"""#!/busybox/sh
                /kaniko/executor -f ./dockerfile -c `pwd` —destination=10.10.110.170:5000/bjss-node:1.0.0
                """
                }
            }
        }

    }
}
