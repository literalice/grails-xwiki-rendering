package com.monochromeroad.grails.plugins.xwiki

import spock.lang.Specification
import spock.lang.Shared
import org.xwiki.component.embed.EmbeddableComponentManager
import org.apache.log4j.BasicConfigurator

/**
 * XWiki Renderer Spec
 */
class XWikiRendererSpec extends Specification {

    @Shared
    XWikiComponentRepository componentRepository

    def setupSpec() {
        BasicConfigurator.configure();

        def componentManager = new EmbeddableComponentManager()
        componentManager.initialize(getClass().classLoader)
        componentRepository = new XWikiComponentRepository(componentManager);
    }

    XWikiRenderer renderer

    String testXWiki21Text = """
=level1=
text :**bold**
"""

    String testMediaWikiText = """
=level1=
text :'''bold'''
"""

    String expectedHTML = '<h1 id="Hlevel1"><span>level1</span></h1><p>text :<strong>bold</strong></p>'

    String expectedText = "level1\n\ntext :bold"

    def setup() {
        renderer = new XWikiRenderer(componentRepository, new XWikiConfigurationProvider())
    }

    def "Converts wiki text using XWiki syntax"() {
        Writer writer = new StringWriter();
        
        when:
        renderer.render(new StringReader(testXWiki21Text), writer, "xwiki/2.0", "xhtml/1.0")
        then:
        writer.toString() == expectedHTML
    }

    def "Converts XWiki 2_1 text to another format using XWiki syntax"() {
        Writer writer = new StringWriter();

        when:
        renderer.render(new StringReader(testXWiki21Text), writer, "xwiki/2.0", "plain/1.0")
        then:
        writer.toString() == expectedText
    }

    def "Converts another syntax wiki text to another format using XWiki syntax"() {
        Writer writer = new StringWriter();

        when:
        renderer.render(new StringReader(testMediaWikiText), writer, "mediawiki/1.0", "xhtml/1.0")
        then:
        writer.toString() == expectedHTML
    }

    def "Macro Support"() {
        String text = """
= level1 =
== level2 ==
{{comment}}
this line is comment.
{{/comment}}
"""
        Writer writer = new StringWriter();

        when:
        renderer.render(text, writer, "xwiki/2.0", "xhtml/1.0")
        then:"The text written in Comment Macro is not rendered"
        writer.toString() == '<h1 id="Hlevel1"><span>level1</span></h1><h2 id="Hlevel2"><span>level2</span></h2>'
    }
    
    def "Extra API: using default input syntax and default output syntax"() {
        when:
        String result = renderer.render(testXWiki21Text)
        then:
        result == expectedHTML
    }

    def "Extra API: returns the result text"() {
        when:
        String result = renderer.render(testXWiki21Text, "xwiki/2.0", "xhtml/1.0")
        then:
        result == expectedHTML
    }

    def "Extra API: returns the result text using default input syntax and default output syntax"() {
        when:
        String result = renderer.render(testXWiki21Text)
        then:
        result == expectedHTML
    }

    def "Adds some custom transformer"() {
        def transformation1 = new TestTransformation("Pre Transformation", 1);
        def transformation2 = new TestTransformation("Post Transformation", 1000);

        when:
        String result = renderer.render(
                testXWiki21Text, "xwiki/2.0", "xhtml/1.0", transformation1, transformation2)
        println result
        then:
        result == expectedHTML + "Pre TransformationPost Transformation"
    }
}
