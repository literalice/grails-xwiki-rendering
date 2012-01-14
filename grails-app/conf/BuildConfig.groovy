grails.project.class.dir = "target/classes"
grails.project.test.class.dir = "target/test-classes"
grails.project.test.reports.dir = "target/test-reports"
//grails.project.war.file = "target/${appName}-${appVersion}.war"

grails.release.scm.enabled = false

grails.project.dependency.resolution = {

    inherits("global")

    log "error" // log level of Ivy resolver, either 'error', 'warn', 'info', 'debug' or 'verbose'

    repositories {
        grailsPlugins()
        grailsHome()
        grailsCentral()

        mavenCentral()
        mavenRepo name: "XWikiPublic", root: "http://nexus.xwiki.org/nexus/content/groups/public"
    }
    dependencies {
        // specify dependencies here under either 'build', 'compile', 'runtime', 'test' or 'provided' scopes eg.
        def xwikiVersion="3.2"
        compile("org.xwiki.commons:xwiki-commons-component-default:$xwikiVersion",
                "org.xwiki.rendering:xwiki-rendering-syntax-xwiki2:$xwikiVersion",
                "org.xwiki.rendering:xwiki-rendering-syntax-xhtml:$xwikiVersion",
                "org.xwiki.rendering:xwiki-rendering-transformation-macro:$xwikiVersion"){
            excludes "xml-apis", "xercesImpl", "slf4j-api"
        }
        runtime("org.xwiki.rendering:xwiki-rendering-macro-comment:$xwikiVersion") {
            transitive = false
            export = false
        }
    }

    plugins {
        build(":tomcat:$grailsVersion",
              ":release:1.0.1",
              ":svn:1.0.2") {
            export = false
        }

        test(":spock:0.6-SNAPSHOT") {
            export = false
        }
    }
}
