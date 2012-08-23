import com.monochromeroad.grails.plugins.xwiki.XWikiComponentManager
import com.monochromeroad.grails.plugins.xwiki.XWikiRenderer
import com.monochromeroad.grails.plugins.xwiki.XWikiConfigurationProvider
import com.monochromeroad.grails.plugins.xwiki.XWikiSyntaxFactory
import com.monochromeroad.grails.plugins.xwiki.artefact.GrailsXwikiMacroClass
import com.monochromeroad.grails.plugins.xwiki.artefact.XwikiMacroArtefactHandler

class XwikiRenderingGrailsPlugin {
    // the plugin version
    def version = "0.7"

    // the version or versions of Grails the plugin is designed for
    def grailsVersion = "* > 1.2.2"
    // the other plugins this plugin depends on
    def dependsOn = [:]
    // resources that are excluded from plugin packaging

    def loadAfter = ['logging'] // retained to ensure correct loading under Grails < 2.0

    def pluginExcludes = [
            "grails-app/xwiki/**/*",
            "src/docs/**/*",
    ]

    def author = "Masatoshi Hayashi"
    def authorEmail = "literalice@monochromeroad.com"
    def title = "XWiki Rendering Plugin"
    def description = "The wiki rendering engine using XWiki Rendering Framework."

    def documentation = "http://literalice.github.com/grails-xwiki-rendering/"

    def license = "LGPL2.1"
    
    def scm = [url: "https://github.com/literalice/grails-xwiki-rendering"]

    def artefacts = [ XwikiMacroArtefactHandler ]

    def watchedResources = "file:./grails-app/xwiki/**/*"

    def doWithWebDescriptor = { xml ->
    }

    def doWithSpring = {
        defaultXWikiComponentManager(XWikiComponentManager)
        defaultXWikiConfigurationProvider(XWikiConfigurationProvider)
        defaultXWikiSyntaxFactory(XWikiSyntaxFactory)

        xwikiRenderer(XWikiRenderer)
    }

    def doWithApplicationContext = { applicationContext ->
        XWikiComponentManager defaultXWikiComponentManager =
            applicationContext.getBean("defaultXWikiComponentManager") as XWikiComponentManager
        defaultXWikiComponentManager.initialize(application.classLoader)

        XWikiSyntaxFactory syntaxFactory =
            applicationContext.getBean("defaultXWikiSyntaxFactory") as XWikiSyntaxFactory
        syntaxFactory.initialize(defaultXWikiComponentManager)

        XWikiRenderer xwikiRenderer =
            applicationContext.getBean("xwikiRenderer") as XWikiRenderer

        XWikiConfigurationProvider rendererConfiguration =
            applicationContext.getBean("defaultXWikiConfigurationProvider") as XWikiConfigurationProvider

        xwikiRenderer.initialize(defaultXWikiComponentManager, rendererConfiguration)

        log.info("Starting to register Grails XWiki Macros...")
        application.xwikiMacroClasses.each { GrailsXwikiMacroClass macroClass->
            log.info("XWiki Macro [${macroClass.naturalName}] is being registered...")
            defaultXWikiComponentManager.registerMacro(macroClass.clazz)
        }
        log.info("Grails XWiki Macros registered successfully")
    }

    def onChange = { event ->
        def eventSource = event.source
        if (eventSource instanceof Class && application.isXwikiMacroClass(eventSource) && event.ctx) {
            log.info("Reloading Grails XWiki Macros..")
            def defaultXWikiComponentManager = event.ctx.getBean("defaultXWikiComponentManager") as XWikiComponentManager
            defaultXWikiComponentManager.registerMacro(eventSource.class)
            log.info("Grails XWiki Macro [${eventSource.class.name}] reloaded successfully")
        }
    }

}
