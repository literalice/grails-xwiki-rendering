import org.xwiki.component.embed.EmbeddableComponentManager
import com.monochromeroad.grails.plugins.xwiki.XWikiRenderer
import com.monochromeroad.grails.plugins.xwiki.XWikiConfigurationProvider
import com.monochromeroad.grails.plugins.xwiki.XWikiRendererConfigurator
import com.monochromeroad.grails.plugins.xwiki.artefact.GrailsXwikiMacroClass
import com.monochromeroad.grails.plugins.xwiki.artefact.XwikiMacroArtefactHandler
import org.xwiki.properties.BeanManager
import com.monochromeroad.grails.plugins.xwiki.macro.GenericGrailsMacro
import com.monochromeroad.grails.plugins.xwiki.macro.GrailsMacro

class XwikiRenderingGrailsPlugin {
    // the plugin version
    def version = "0.3"
    // the version or versions of Grails the plugin is designed for
    def grailsVersion = "* > 1.3.0"
    // the other plugins this plugin depends on
    def dependsOn = [:]
    // resources that are excluded from plugin packaging
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

    def documentation = "http://grails.org/plugin/xwiki-rendering"

    def license = "LGPL"
    
    def scm = [url: "https://github.com/literalice/grails-xwiki-rendering"]

    def artefacts = [ XwikiMacroArtefactHandler ]

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

        BeanManager beanManager = componentManager.lookup(BeanManager) as BeanManager

        log.info("Starting to register Grails XWiki Macros...")
        application.xwikiMacroClasses.each { GrailsXwikiMacroClass macroClass->
            log.info("XWiki Macro [${macroClass.naturalName}] is being registered...")

            def macro = createMacro(macroClass, beanManager);
            def macroDescriptor = macro.createDescriptor()
            componentManager.registerComponent(macroDescriptor, macro);
        }
        log.info("Grails XWiki Macros registered successfully")

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

    GenericGrailsMacro createMacro(
            GrailsXwikiMacroClass macroClass, BeanManager beanManager) {
        String macroName = macroClass.getPropertyValue("macroName") as String
        if (!macroName) {
            throw new IllegalStateException(
                    "Macro name (static macroName) not defined.: $macroClass.fullName")
        }

        def grailsMacro = macroClass.clazz.newInstance() as GrailsMacro
        Class parametersBeanClass =
            (macroClass.getPropertyValue("parametersBeanClass") ?: Object.class) as Class
        Boolean inlineSupport =
            macroClass.getPropertyValue("inlineSupport") as Boolean

        return GenericGrailsMacro.getInstance(beanManager,
                    macroName:macroName,
                    macroImpl:grailsMacro,
                    parametersBeanClass: parametersBeanClass,
                    inlineSupport: inlineSupport);
    }
}
