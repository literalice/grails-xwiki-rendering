package com.monochromeroad.grails.plugins.xwiki;

/**
 * For the Grails Default XWiki Rendering System.
 * It need to be initialized after construction.
 *
 * @author Masatoshi Hayashi
 */
public class DefaultXWikiRendering {

    public XWikiComponentManager getXWikiComponentManager() {
        return new XWikiComponentManager();
    }

    public XWikiConfigurationProvider getXWikiConfigurationProvider() {
        return new XWikiConfigurationProvider();
    }

    public XWikiRenderer getXWikiRenderer() {
        return new XWikiRenderer();
    }

    public XWikiStreamRenderer getXWikiStreamRenderer() {
        return new XWikiStreamRenderer();
    }

    public XWikiSyntaxFactory getXWikiSyntaxFactory() {
        return new XWikiSyntaxFactory();
    }

    public void initialize(ClassLoader classLoader,
                            XWikiComponentManager componentManager,
                            XWikiSyntaxFactory syntaxFactory,
                            XWikiConfigurationProvider configurationProvider,
                            XWikiRenderingSystem ...renderers) {
        componentManager.initialize(classLoader);
        syntaxFactory.initialize(componentManager);
        for (XWikiRenderingSystem renderer : renderers) {
            renderer.initialize(componentManager, configurationProvider);
        }
    }

}
