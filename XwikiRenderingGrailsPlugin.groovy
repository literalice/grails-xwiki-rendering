import com.monochromeroad.grails.plugins.xwiki.DefaultXWikiRendering
import com.monochromeroad.grails.plugins.xwiki.XWikiComponentManager
import com.monochromeroad.grails.plugins.xwiki.XWikiRenderer
import com.monochromeroad.grails.plugins.xwiki.XWikiConfigurationProvider
import com.monochromeroad.grails.plugins.xwiki.XWikiStreamRenderer
import com.monochromeroad.grails.plugins.xwiki.XWikiSyntaxFactory
import com.monochromeroad.grails.plugins.xwiki.artefact.GrailsXwikiMacroClass
import com.monochromeroad.grails.plugins.xwiki.artefact.XwikiMacroArtefactHandler

class XwikiRenderingGrailsPlugin {
    // the plugin version
    def version = "1.0-RC3"

    // the version or versions of Grails the plugin is designed for
    def grailsVersion = "* > 1.2.2"
    // the other plugins this plugin depends on
    def dependsOn = [:]
    // resources that are excluded from plugin packaging

    def loadAfter = ['logging'] // retained to ensure correct loading under Grails < 2.0

    def pluginExcludes = [
        "src/docs/**/*"
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
        defaultXWikiRendering(DefaultXWikiRendering)
        defaultXWikiComponentManager(defaultXWikiRendering: "getXWikiComponentManager")
        defaultXWikiConfigurationProvider(defaultXWikiRendering: "getXWikiConfigurationProvider")
        defaultXWikiSyntaxFactory(defaultXWikiRendering: "getXWikiSyntaxFactory")

        xwikiRenderer(defaultXWikiRendering: "getXWikiRenderer")
        xwikiStreamRenderer(defaultXWikiRendering: "getXWikiStreamRenderer")
    }

    def doWithApplicationContext = { acx ->
        XWikiComponentManager defaultXWikiComponentManager =
            acx.getBean("defaultXWikiComponentManager") as XWikiComponentManager

        XWikiSyntaxFactory syntaxFactory = acx.getBean("defaultXWikiSyntaxFactory") as XWikiSyntaxFactory
        XWikiConfigurationProvider configurationProvider = getXWikiConfiguration(acx, application)
        XWikiRenderer xwikiRenderer = acx.getBean("xwikiRenderer") as XWikiRenderer
        XWikiStreamRenderer xwikiStreamRenderer = acx.getBean("xwikiStreamRenderer") as XWikiStreamRenderer

        DefaultXWikiRendering defaultXWikiRendering = acx.getBean("defaultXWikiRendering") as DefaultXWikiRendering

        defaultXWikiRendering.initialize(
                application.classLoader,
                defaultXWikiComponentManager,
                syntaxFactory,
                configurationProvider,
                xwikiRenderer, xwikiStreamRenderer)

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
            defaultXWikiComponentManager.registerMacro(eventSource)
            log.info("Grails XWiki Macro [${eventSource.name}] reloaded successfully")
        }
    }

    private static XWikiConfigurationProvider getXWikiConfiguration(acx, application) {
        XWikiConfigurationProvider configuration =
            acx.getBean("defaultXWikiConfigurationProvider") as XWikiConfigurationProvider

        configuration.setMacrosEnabled(isMacroEnabled(application.config))

        return configuration
    }

    private static boolean isMacroEnabled(config) {
        def enabled = config.grails.xwiki.rendering.macros.enabled
        if (enabled instanceof Boolean) {
            return enabled
        } else {
            true
        }
    }

}
