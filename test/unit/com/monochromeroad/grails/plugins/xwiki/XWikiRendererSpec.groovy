package com.monochromeroad.grails.plugins.xwiki

import com.monochromeroad.grails.plugins.xwiki.macro.DateMacro
import com.monochromeroad.grails.plugins.xwiki.macro.RbMacro
import org.xwiki.rendering.syntax.Syntax
import spock.lang.Specification
import spock.lang.Shared
import org.apache.log4j.BasicConfigurator

/**
 * XWiki Renderer Spec
 */
class XWikiRendererSpec extends Specification {

    @Shared
    XWikiRenderer renderer

    def setupSpec() {
        BasicConfigurator.configure();

        XWikiConfigurationProvider configurationProvider = new XWikiConfigurationProvider();
        XWikiComponentManager componentManager = new XWikiComponentManager(getClass().classLoader);
        componentManager.registerMacro(DateMacro)
        componentManager.registerMacro(RbMacro)

        renderer = new XWikiRenderer(componentManager, configurationProvider)
    }

    String testXWiki21Text = """
=level1=
text :**bold**
"""

    String expectedHTML = '<h1 id="Hlevel1"><span>level1</span></h1><p>text :<strong>bold</strong></p>'

    String expectedText = "level1\n\ntext :bold"

    def "Converts wiki text using XWiki syntax"() {
        when:
        def result = renderer.render(new StringReader(testXWiki21Text), Syntax.XWIKI_2_1, Syntax.XHTML_1_0)
        then:
        result == expectedHTML
    }

    def "Converts XWiki 2_1 text to another format using XWiki syntax"() {
        when:
        def result = renderer.render(new StringReader(testXWiki21Text), Syntax.XWIKI_2_1, Syntax.PLAIN_1_0)
        then:
        result == expectedText
    }

    def "Embedded Macro Support"() {
        String text = """
= level1 =
== level2 ==
{{comment}}
this line is comment.
{{/comment}}
"""

        when:
        def result = renderer.render(text, Syntax.XWIKI_2_1, Syntax.XHTML_1_0)
        then:"The text written in Comment Macro is not rendered"
        result == '<h1 id="Hlevel1"><span>level1</span></h1><h2 id="Hlevel2"><span>level2</span></h2>'
    }

    def "User Macro Support"() {
        String text = """
{{date/}}

{{rb read="reading"}}TEXT{{/rb}}

"""

        when:
        def result = renderer.render(text, Syntax.XWIKI_2_1, Syntax.XHTML_1_0)
        then:
        result == new Date().format("yyyy/MM/dd") + '<p><ruby>TEXT<rp>(</rp><rt>reading</rt><rp>)</rp></ruby></p>'
    }

    def "Extra API: using default input syntax and default output syntax"() {
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
                testXWiki21Text, Syntax.XWIKI_2_1, Syntax.XHTML_1_0, transformation1, transformation2)
        println result
        then:
        result == expectedHTML + "Pre TransformationPost Transformation"
    }
}
