package com.monochromeroad.grails.plugins.xwiki

import spock.lang.Specification
import spock.lang.Shared
import org.xwiki.component.manager.ComponentManager
import org.xwiki.component.embed.EmbeddableComponentManager

class RenderingServiceSpec extends Specification {

    @Shared
    ComponentManager componentManager

    def setupSpec() {
        componentManager = new EmbeddableComponentManager()
        componentManager.initialize(getClass().classLoader)
    }

    XWikiRenderer renderer

    RenderingService renderingService

    String testXWiki21Text = """
=level1=
text :**bold**
"""

    String expectedHTML = '<h1 id="Hlevel1"><span>level1</span></h1><p>text :<strong>bold</strong></p>'

    def setup() {
        renderer = new XWikiRenderer(componentManager)
        renderingService = new RenderingService(xwikiRenderer: renderer)
    }

    def "Rendering a text as XHTML using XWiki 2_0 syntax"() {
        when:
        String parsed = renderingService.renderAsXHTML("xwiki/2.0", testXWiki21Text)
        then:
        parsed == expectedHTML
    }
}
