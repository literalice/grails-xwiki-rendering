grails.project.class.dir = "target/classes"
grails.project.test.class.dir = "target/test-classes"
grails.project.test.reports.dir = "target/test-reports"

grails.release.scm.enabled = false

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

grails.project.dependency.resolution = {

    inherits("global")

    log "error" // log level of Ivy resolver, either 'error', 'warn', 'info', 'debug' or 'verbose'

    repositories {
        grailsPlugins()
        grailsHome()
        grailsCentral()
        grailsRepo "http://grails.org/plugins"

        mavenCentral()
        mavenRepo "http://nexus.xwiki.org/nexus/content/groups/public"
    }
    dependencies {
        def xwikiVersion = "4.1.3"
        compile("org.xwiki.commons:xwiki-commons-component-default:$xwikiVersion",
                "org.xwiki.rendering:xwiki-rendering-transformation-macro:$xwikiVersion"){
            excludes "xercesImpl", "slf4j-api"
        }
        compile "javax.inject:javax.inject:1"

        def syntaxesConfig =  (grailsSettings.config.grails.xwiki.rendering.syntaxes ?: "").split(/\s*,\s*/).toList().findAll { it }
        syntaxesConfig << "xwiki21"
        syntaxesConfig << "xhtml"

        println "XWiki syntaxes ${syntaxesConfig} loading."

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
        test("org.spockframework:spock-grails-support:0.7-groovy-2.0") {
            export = false
        }
    }

    plugins {
        build(":release:2.0.4", ":rest-client-builder:1.0.2") {
            export = false
        }

        test(":spock:0.7") {
            exclude "spock-grails-support"
            export = false
        }
    }
}

