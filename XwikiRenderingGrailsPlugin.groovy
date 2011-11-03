import org.xwiki.component.embed.EmbeddableComponentManager
import com.monochromeroad.grails.plugins.xwiki.XWikiRenderer
import com.monochromeroad.grails.plugins.xwiki.XWikiConfigurationProvider
import com.monochromeroad.grails.plugins.xwiki.XWikiRendererConfigurator
import com.monochromeroad.grails.plugins.xwiki.artefact.GrailsXwikiMacroClass
import com.monochromeroad.grails.plugins.xwiki.artefact.XwikiMacroArtefactHandler
import org.xwiki.properties.BeanManager
import com.monochromeroad.grails.plugins.xwiki.macro.GenericGrailsMacro
import com.monochromeroad.grails.plugins.xwiki.macro.GrailsMacro
import org.xwiki.rendering.macro.Macro

class XwikiRenderingGrailsPlugin {
    // the plugin version
    def version = "0.3"
    // the version or versions of Grails the plugin is designed for
    def grailsVersion = "* > 1.2.2"
    // the other plugins this plugin depends on
    def dependsOn = [:]
    // resources that are excluded from plugin packaging

    def loadAfter = ['logging'] // retained to ensure correct loading under Grails < 2.0

    def pluginExcludes = [
            "grails-app/xwiki/**/*",
            "grails-app/views/**/*",
            "grails-app/i18n/**/*",
            "grails-app/controllers/**/*",
            "web-app/images/**/*",
    ]

    def author = "Masatoshi Hayashi"
    def authorEmail = "literalice@monochromeroad.com"
    def title = "XWiki Rendering Plugin"
    def description = "The wiki rendering engine using XWiki Rendering Framework."

    def documentation = "http://literalice.github.com/grails-xwiki-rendering/"

    def license = "APACHE"
    
    def scm = [url: "https://github.com/literalice/grails-xwiki-rendering"]

    def artefacts = [ XwikiMacroArtefactHandler ]

    def watchedResources = "file:./grails-app/xwiki/**/*"

    def doWithWebDescriptor = { xml ->
        // TODO Implement additions to web.xml (optional), this event occurs before 
    }

    def doWithSpring = {
        xwikiComponentManager(EmbeddableComponentManager)
        xwikiConfigurationProvider(XWikiConfigurationProvider)
        xwikiRenderer(XWikiRenderer)
    }

    def doWithDynamicMethods = { ctx ->
        // TODO Implement registering dynamic methods to classes (optional)
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
            reloadMacro(componentManager, macroClass.clazz.newInstance() as GrailsMacro)
        }
        log.info("Grails XWiki Macros registered successfully")
    }

    def onChange = { event ->
        log.info("Reloading Grails XWiki Macros..")
        if (application.isXwikiMacroClass(event.source) && event.ctx) {
            def componentManager =
                    event.ctx.getBean("xwikiComponentManager") as EmbeddableComponentManager
            GrailsMacro macroImpl = event.source.newInstance()
            reloadMacro(componentManager, macroImpl)
            log.info("Grails XWiki Macro [${macroImpl.macroName}] reloaded successfully")
        }
    }

    def onConfigChange = { event ->
        // TODO Implement code that is executed when the project configuration changes.
        // The event is the same as for 'onChange'.
    }

    private void reloadMacro(EmbeddableComponentManager componentManager, GrailsMacro macroImpl) {
        BeanManager beanManager = componentManager.lookup(BeanManager) as BeanManager
        GenericGrailsMacro macro = createMacro(macroImpl, beanManager);
        componentManager.unregisterComponent(Macro, macro.macroName);
        componentManager.registerComponent(macro.createDescriptor(), macro);
    }

    private GenericGrailsMacro createMacro(GrailsMacro macroImpl, BeanManager beanManager) {
        String macroName
        if (macroImpl.hasProperty("macroName")) {
            macroName = macroImpl.macroName
        } else {
            throw new IllegalStateException(
                    "Macro name (static macroName) not defined.: ${macroImpl.class.name}")
        }

        Class parametersBeanClass = Object.class
        if (macroImpl.hasProperty("parametersBeanClass")) {
            parametersBeanClass = macroImpl.parametersBeanClass as Class
        }

        Boolean inlineSupport = false
        if (macroImpl.hasProperty("inlineSupport")) {
            inlineSupport = macroImpl.inlineSupport as Boolean
        }

        return GenericGrailsMacro.getInstance(beanManager,
                macroName:macroName,
                macroImpl:macroImpl,
                parametersBeanClass: parametersBeanClass,
                inlineSupport: inlineSupport);
    }
}
