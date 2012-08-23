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

    public XWikiSyntaxFactory getXWikiSyntaxFactory() {
        return new XWikiSyntaxFactory();
    }

    public void initialize(ClassLoader classLoader,
                            XWikiComponentManager componentManager,
                            XWikiSyntaxFactory syntaxFactory,
                            XWikiRenderer renderer,
                            XWikiConfigurationProvider configurationProvider) {
        componentManager.initialize(classLoader);
        syntaxFactory.initialize(componentManager);
        renderer.initialize(componentManager, configurationProvider);
    }

}
