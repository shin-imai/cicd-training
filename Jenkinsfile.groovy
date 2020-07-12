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
