grails.project.class.dir = "target/classes"
grails.project.test.class.dir = "target/test-classes"
grails.project.test.reports.dir = "target/test-reports"

grails.release.scm.enabled = false

hibernateVersion = '3.6.10.9'

private repositoryConfigLoader(name, path) {
    def centralCredentialLocation = System.getProperty(path)
    if (centralCredentialLocation) {
        def credentialFile = new File(centralCredentialLocation)
        if (credentialFile.canRead()) {
            def credential = new Properties()
            //noinspection GroovyMissingReturnStatement
            credentialFile.withReader {
                credential.load(it)
            }

            for (param in ["url", "username", "password", "type", "portal"]) {
                if (credential.get(param)) {
                    grails.project.repos."${name}"."${param}" = credential.get(param)
                }
            }
            println "Repository Configuration $name correctly loaded."
        } else {
            throw new IllegalStateException("Grails Central Credential File couldn't be read.")
        }
    }
}

for (repositoryName in ["grailsCentral", "snapshotRepository"]) {
    repositoryConfigLoader(repositoryName, "${repositoryName}.credential.properties")
    for (param in ["url", "type", "portal"]) {
        if (grails.project.repos."${repositoryName}"."${param}") {
            print "$repositoryName / $param : "
            print "$repositoryName / $param : "
            println grails.project.repos."${repositoryName}"."${param}"
        }
    }
}

grails.project.dependency.resolver = "maven" // or ivy
grails.project.dependency.resolution = {

    inherits("global") {
        excludes "xercesImpl", "xml-apis", "grails-core"
    }

    log "error" // log level of Ivy resolver, either 'error', 'warn', 'info', 'debug' or 'verbose'

    repositories {
        grailsPlugins()
        grailsHome()
        grailsCentral()
//        grailsRepo "http://grails.org/plugins"

        mavenCentral()
//        mavenRepo "http://nexus.xwiki.org/nexus/content/groups/public"
    }
    dependencies {
        def xwikiVersion = "5.4.1"
        compile("org.xwiki.commons:xwiki-commons-component-default:$xwikiVersion",
                "org.xwiki.rendering:xwiki-rendering-transformation-macro:$xwikiVersion"){
            excludes "xercesImpl", "slf4j-api"
        }

        def syntaxesConfig =  (grailsSettings.config.grails.xwiki.rendering.syntaxes ?: "").split(/\s*,\s*/).toList().findAll { it }
        syntaxesConfig << "xwiki21"
        syntaxesConfig << "xhtml"

        println "| XWiki syntaxes ${syntaxesConfig} loading."

        for (xwikiSyntax in syntaxesConfig) {
            compile("org.xwiki.rendering:xwiki-rendering-syntax-$xwikiSyntax:$xwikiVersion"){
                excludes "xercesImpl", "slf4j-api"
            }
        }

        def macrosConfig =  (grailsSettings.config.grails.xwiki.rendering.macros ?: "").split(/\s*,\s*/).toList().findAll { it }
        if (macrosConfig) {
            println "XWiki official macros ${macrosConfig} loading."
        }
        for (xwikiMacro in macrosConfig) {
            compile("org.xwiki.rendering:xwiki-rendering-macro-$xwikiMacro:$xwikiVersion"){
                excludes "xercesImpl", "slf4j-api"
            }
        }

        test("org.xwiki.rendering:xwiki-rendering-macro-comment:$xwikiVersion") {
            transitive = false
            export = false
        }
        test("org.xwiki.rendering:xwiki-rendering-syntax-mediawiki:$xwikiVersion"){
            excludes "xercesImpl", "slf4j-api"
            export = false
        }
        test("org.spockframework:spock-grails-support:0.7-groovy-2.0") {
            export = false
        }
    }

    plugins {
        build(":release:3.0.1", ":rest-client-builder:2.0.1") {
            export = false
        }

//        runtime ":hibernate:$hibernateVersion"

        test(":spock:0.7"/*, ":code-coverage:1.2.6"*/) {
            exclude "spock-grails-support"
            export = false
        }
    }
}
