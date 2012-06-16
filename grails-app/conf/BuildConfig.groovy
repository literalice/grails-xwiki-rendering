grails.project.class.dir = "target/classes"
grails.project.test.class.dir = "target/test-classes"
grails.project.test.reports.dir = "target/test-reports"

grails.release.scm.enabled = false

def grailsCentralCredentialLocation = System.getProperty("grailsCentral.credential.properties")
if (grailsCentralCredentialLocation) {
    def credentialFile = new File(grailsCentralCredentialLocation)
    if (credentialFile.canRead()) {
        def grailsCentralCredential = new Properties()
        //noinspection GroovyMissingReturnStatement
        credentialFile.withReader {
            grailsCentralCredential.load(it)
        }

        grails.project.repos.grailsCentral.username = grailsCentralCredential.get("grails.project.repos.grailsCentral.username")
        grails.project.repos.grailsCentral.password = grailsCentralCredential.get("grails.project.repos.grailsCentral.password")
        println "Grails Central Credential File correctly loaded."
    } else {
        throw new IllegalStateException("Grails Central Credential File couldn't be read.")
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
        def xwikiVersion="4.1-rc-1"
        compile("org.xwiki.commons:xwiki-commons-component-default:$xwikiVersion",
                "org.xwiki.rendering:xwiki-rendering-transformation-macro:$xwikiVersion"){
            excludes "xercesImpl", "slf4j-api"
        }

        def syntaxesConfig =  (grailsSettings.config.grails.xwiki.rendering.syntaxes ?: "").split(/\s*,\s*/).toList().findAll { it }
        syntaxesConfig << "xwiki21"
        syntaxesConfig << "xhtml"

        println "XWiki Syntaxes ${syntaxesConfig} loading."

        for (xwikiSyntax in syntaxesConfig) {
            compile("org.xwiki.rendering:xwiki-rendering-syntax-$xwikiSyntax:$xwikiVersion"){
                excludes "xercesImpl", "slf4j-api"
            }
        }

        test("org.xwiki.rendering:xwiki-rendering-macro-comment:$xwikiVersion") {
            transitive = false
            export = false
        }
    }

    plugins {
        build(":release:2.0.3") {
            export = false
        }

        test(":spock:0.6") {
            export = false
        }
    }
}

