package com.monochromeroad.grails.plugins.xwiki;

import org.xwiki.component.manager.ComponentManager;
import org.xwiki.rendering.syntax.SyntaxFactory;
import org.xwiki.rendering.transformation.Transformation;

import java.util.List;

public class XWikiRendererConfigurator {

    public static void initialize(XWikiRenderer renderer,
            XWikiConfigurationProvider configurationProvider, ComponentManager componentManager) {
        XWikiComponentRepository componentRepository = new XWikiComponentRepository(componentManager);

        renderer.setConfigurationProvider(configurationProvider);

        renderer.setParserFactory(new XWikiParserFactory(componentRepository));

        renderer.setSyntaxFactory(getSyntaxFactory(componentRepository));

        XDOMTransformationManager transformationManager =
                createTransformationManager(componentRepository, configurationProvider);
        renderer.setTransformationManager(transformationManager);

        renderer.setWriter(new XDOMWriter(componentRepository));
    }

    private static XDOMTransformationManager createTransformationManager(
            XWikiComponentRepository componentRepository, XWikiConfigurationProvider configurationProvider) {
        XDOMTransformationManager manager = new XDOMTransformationManager();
        List<String> defaultTransformations = configurationProvider.getDefaultTransformations();
        for (String transformation : defaultTransformations) {
            manager.addDefaultTransformation(
                    componentRepository.lookupComponent(Transformation.class, transformation));
        }
        return manager;
    }

    private static SyntaxFactory getSyntaxFactory(XWikiComponentRepository componentRepository) {
        return componentRepository.
                lookupComponent(SyntaxFactory.class);
    }
}
