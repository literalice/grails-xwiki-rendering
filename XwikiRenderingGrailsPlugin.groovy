import com.monochromeroad.grails.plugins.xwiki.macro.DefaultXWikiMacro
import org.xwiki.component.descriptor.DefaultComponentDescriptor
import org.xwiki.component.embed.EmbeddableComponentManager
import com.monochromeroad.grails.plugins.xwiki.XWikiRenderer
import com.monochromeroad.grails.plugins.xwiki.XWikiConfigurationProvider
import com.monochromeroad.grails.plugins.xwiki.XWikiRendererConfigurator
import com.monochromeroad.grails.plugins.xwiki.artefact.GrailsXwikiMacroClass
import com.monochromeroad.grails.plugins.xwiki.artefact.XwikiMacroArtefactHandler
import org.xwiki.properties.BeanManager
import org.xwiki.rendering.macro.Macro

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
        xwikiComponentManager(EmbeddableComponentManager)
        xwikiConfigurationProvider(XWikiConfigurationProvider)
        xwikiRenderer(XWikiRenderer)
    }

    def doWithDynamicMethods = { ctx ->
    }

    def doWithApplicationContext = { applicationContext ->
        XWikiConfigurationProvider xwikiConfigurationProvider =
            applicationContext.getBean("xwikiConfigurationProvider") as XWikiConfigurationProvider

        EmbeddableComponentManager componentManager =
            applicationContext.getBean("xwikiComponentManager") as EmbeddableComponentManager
        componentManager.initialize(application.classLoader)

        XWikiRenderer xwikiRenderer =
            applicationContext.getBean("xwikiRenderer") as XWikiRenderer
        XWikiRendererConfigurator.initialize(
                xwikiRenderer, xwikiConfigurationProvider, componentManager)

        log.info("Starting to register Grails XWiki Macros...")
        application.xwikiMacroClasses.each { GrailsXwikiMacroClass macroClass->
            log.info("XWiki Macro [${macroClass.naturalName}] is being registered...")
            reloadMacro(componentManager, macroClass.clazz.newInstance() as DefaultXWikiMacro)
        }
        log.info("Grails XWiki Macros registered successfully")
    }

    def onChange = { event ->
        def eventSource = event.source
        if (eventSource instanceof Class && application.isXwikiMacroClass(eventSource) && event.ctx) {
            log.info("Reloading Grails XWiki Macros..")
            def componentManager = event.ctx.getBean("xwikiComponentManager") as EmbeddableComponentManager
            DefaultXWikiMacro macroImpl = eventSource.newInstance()
            reloadMacro(componentManager, macroImpl)
            log.info("Grails XWiki Macro [${macroImpl.macroName}] reloaded successfully")
        }
    }

    private void reloadMacro(EmbeddableComponentManager componentManager, DefaultXWikiMacro macroImpl) {
        def beanManager = componentManager.getInstance(BeanManager) as BeanManager
        macroImpl.beanManager = beanManager
        macroImpl.initialize()

        def macroDescriptor = new DefaultComponentDescriptor<Macro>()
        macroDescriptor.setImplementation(macroImpl.class)
        macroDescriptor.setRoleType(Macro)
        macroDescriptor.setRoleHint(macroImpl.macroName)

        componentManager.unregisterComponent(Macro, macroImpl.macroName)
        componentManager.registerComponent(macroDescriptor, macroImpl)
    }

}
