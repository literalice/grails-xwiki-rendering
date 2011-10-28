import org.xwiki.component.embed.EmbeddableComponentManager
import com.monochromeroad.grails.plugins.xwiki.XWikiRenderer
import com.monochromeroad.grails.plugins.xwiki.XWikiConfigurationProvider

class XwikiRenderingGrailsPlugin {
    // the plugin version
    def version = "0.3"
    // the version or versions of Grails the plugin is designed for
    def grailsVersion = "* > 1.3.0"
    // the other plugins this plugin depends on
    def dependsOn = [:]
    // resources that are excluded from plugin packaging
    def pluginExcludes = [
            "grails-app/views/**/*",
            "grails-app/i18n/**/*",
            "grails-app/controllers/**/*",
            "web-app/images/**/*",
    ]

    def author = "Masatoshi Hayashi"
    def authorEmail = "literalice@monochromeroad.com"
    def title = "XWiki Rendering Plugin"
    def description = "The wiki rendering engine using XWiki Rendering Framework."

    // URL to the plugin's documentation
    def documentation = "http://grails.org/plugin/xwiki-rendering"

    def license = "LGPL"
    
    def scm = [url: "https://github.com/literalice/grails-xwiki-rendering"]

    def doWithWebDescriptor = { xml ->
        // TODO Implement additions to web.xml (optional), this event occurs before 
    }

    def doWithSpring = {
        componentManager(EmbeddableComponentManager)
        xwikiConfigurationProvider(XWikiConfigurationProvider)
        xwikiRenderer(XWikiRenderer, componentManager, xwikiConfigurationProvider)
    }

    def doWithDynamicMethods = { ctx ->
        // TODO Implement registering dynamic methods to classes (optional)
    }

    def doWithApplicationContext = { applicationContext ->
        def componentManager = applicationContext.getBean("componentManager")
        def grailsApplication = applicationContext.getBean("grailsApplication")
        componentManager.initialize(grailsApplication.classLoader)
    }

    def onChange = { event ->
        // TODO Implement code that is executed when any artefact that this plugin is
        // watching is modified and reloaded. The event contains: event.source,
        // event.application, event.manager, event.ctx, and event.plugin.
    }

    def onConfigChange = { event ->
        // TODO Implement code that is executed when the project configuration changes.
        // The event is the same as for 'onChange'.
    }
}
