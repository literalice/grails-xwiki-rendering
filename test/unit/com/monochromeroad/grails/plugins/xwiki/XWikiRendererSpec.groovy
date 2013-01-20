package com.monochromeroad.grails.plugins.xwiki

import com.monochromeroad.grails.plugins.xwiki.macro.SampleMacro
import com.monochromeroad.grails.plugins.xwiki.macro.SampleNoParameterMacro
import com.monochromeroad.grails.plugins.xwiki.transformation.SampleTransformation
import org.xwiki.rendering.syntax.Syntax
import org.xwiki.rendering.transformation.Transformation
import spock.lang.Specification
import spock.lang.Shared
import org.apache.log4j.BasicConfigurator

/**
 * XWiki Renderer Spec
 */
class XWikiRendererSpec extends Specification {

    @Shared
    XWikiRenderer renderer

    @Shared
    XWikiStreamRenderer streamRenderer

    @Shared
    XWikiConfigurationProvider configurationProvider = new XWikiConfigurationProvider()

    def setupSpec() {
        BasicConfigurator.configure()

        XWikiComponentManager componentManager = new XWikiComponentManager(getClass().classLoader)
        componentManager.registerMacro(SampleMacro)
        componentManager.registerMacro(SampleNoParameterMacro)

        renderer = new XWikiRenderer(componentManager, configurationProvider)
        streamRenderer = new XWikiStreamRenderer(componentManager, configurationProvider)
    }

    def expected = "**test** {{sample data='test' /}} {{sample2}}body{{/sample2}}"

    def "Renders a wiki text"() {
        when:
        configurationProvider.macrosEnabled = true
        def result = assertRendererApi(expected)

        then:
        result == "<p><strong>test</strong> data:test[inserted by a macro: sample] body[inserted by a macro: sample2]</p>"
    }

    def "Renders a wiki text without macros"() {
        when:
        configurationProvider.macrosEnabled = false
        and:
        def result = assertRendererApi(expected)

        then:
        result == "<p><strong>test</strong>&nbsp;&nbsp;</p>"
    }

    def "Renders a wiki text with a transformation after applying a macro"() {
        when:
        configurationProvider.macrosEnabled = true
        def result = assertRendererApi(expected, [new SampleTransformation("T1"), new SampleTransformation("T2")])

        then:
        result.trim() == "<p><strong>test</strong> data:test[inserted by a macro: sample] body[inserted by a macro: sample2]</p>It applies the transformation T1! [.. may be applied a macro.] It applies the transformation T2! [.. may be applied a macro.]"
    }

    def "Renders a wiki text with a transformation after/before applying a macro"() {
        when:
        configurationProvider.macrosEnabled = true
        def result = assertRendererApi(expected, [new SampleTransformation("T1")], [new SampleTransformation("T2")])

        then:
        result.trim() == "<p><strong>test</strong> data:test[inserted by a macro: sample] body[inserted by a macro: sample2]</p>It applies the transformation T1! It applies the transformation T2! [.. may be applied a macro.]"
    }

    def "Renders a wiki text with another syntax"() {
        when:
        def result = assertRendererApi("''test'' '''test'''", Syntax.MEDIAWIKI_1_0)
        then:
        result == '<p><em>test</em> <strong>test</strong></p>'
    }

    def "Renders a wiki text with another output syntax"() {
        when:
        configurationProvider.macrosEnabled = true
        def result = assertRendererApi(expected, Syntax.XWIKI_2_1, Syntax.PLAIN_1_0)

        then:
        result.trim() == "test data:test[inserted by a macro: sample] body[inserted by a macro: sample2]"
    }

    private String assertRendererApi(String src, Syntax inputSyntax) {
        def result = renderer.render(src, inputSyntax)
        assert result == renderer.render(src, inputSyntax, configurationProvider.defaultOutputSyntax)
        assert result == renderer.render(new StringReader(src), inputSyntax)
        assert result == renderer.render(new StringReader(src), inputSyntax, configurationProvider.defaultOutputSyntax)

        def streamResult = ""
        streamRenderer.render(new StringReader(src), inputSyntax) { streamResult += it }
        assert streamResult == result

        streamResult = ""
        streamRenderer.render(new StringReader(src), inputSyntax, configurationProvider.defaultOutputSyntax) { streamResult += it }
        assert streamResult == result

        result
    }

    private String assertRendererApi(String src, Syntax inputSyntax, Syntax outputSyntax) {
        def result = renderer.render(src, inputSyntax, outputSyntax)
        assert result == renderer.render(new StringReader(src), inputSyntax, outputSyntax)

        def streamResult = ""
        streamRenderer.render(new StringReader(src), inputSyntax, outputSyntax) { streamResult += it }
        assert streamResult == result

        result
    }

    private String assertRendererApi(String src) {
        def result = renderer.render(src)

        assert result == renderer.render(src, configurationProvider.defaultInputSyntax)
        assert result == renderer.render(src, configurationProvider.defaultInputSyntax, configurationProvider.defaultOutputSyntax)
        assert result == renderer.render(new StringReader(src))
        assert result == renderer.render(new StringReader(src), configurationProvider.defaultInputSyntax)
        assert result == renderer.render(new StringReader(src), configurationProvider.defaultInputSyntax, configurationProvider.defaultOutputSyntax)

        def streamResult = ""
        streamRenderer.render(new StringReader(src)) { streamResult += it }
        assert streamResult == result

        streamResult = ""
        streamRenderer.render(new StringReader(src), configurationProvider.defaultInputSyntax) { streamResult += it }
        assert streamResult == result

        streamResult = ""
        streamRenderer.render(new StringReader(src), configurationProvider.defaultInputSyntax, configurationProvider.defaultOutputSyntax) { streamResult += it }
        assert streamResult == result

        result
    }

    private String assertRendererApi(String src, List<Transformation> postTransformation) {
        def result = renderer.render(src, postTransformation)
        assert result == renderer.render(src, configurationProvider.defaultInputSyntax, postTransformation)
        assert result == renderer.render(src, configurationProvider.defaultInputSyntax, configurationProvider.defaultOutputSyntax, postTransformation)
        assert result == renderer.render(new StringReader(src), postTransformation)
        assert result == renderer.render(new StringReader(src), configurationProvider.defaultInputSyntax, postTransformation)
        assert result == renderer.render(new StringReader(src), configurationProvider.defaultInputSyntax, configurationProvider.defaultOutputSyntax, postTransformation)

        def streamResult = ""
        streamRenderer.render(new StringReader(src), postTransformation) { streamResult += it }
        assert streamResult == result

        streamResult = ""
        streamRenderer.render(new StringReader(src), configurationProvider.defaultInputSyntax, postTransformation) { streamResult += it }
        assert streamResult == result

        streamResult = ""
        streamRenderer.render(new StringReader(src), configurationProvider.defaultInputSyntax, configurationProvider.defaultOutputSyntax, postTransformation) { streamResult += it }
        assert streamResult == result

        result
    }

    private String assertRendererApi(String src, List<Transformation> preTransformation, List<Transformation> postTransformation) {
        def result = renderer.render(src, preTransformation, postTransformation)
        assert result == renderer.render(src, configurationProvider.defaultInputSyntax, preTransformation, postTransformation)
        assert result == renderer.render(src, configurationProvider.defaultInputSyntax, configurationProvider.defaultOutputSyntax, preTransformation, postTransformation)
        assert result == renderer.render(new StringReader(src), preTransformation, postTransformation)
        assert result == renderer.render(new StringReader(src), configurationProvider.defaultInputSyntax, preTransformation, postTransformation)
        assert result == renderer.render(new StringReader(src), configurationProvider.defaultInputSyntax, configurationProvider.defaultOutputSyntax, preTransformation, postTransformation)

        def streamResult = ""
        streamRenderer.render(new StringReader(src), preTransformation, postTransformation) { streamResult += it }
        assert streamResult == result

        streamResult = ""
        streamRenderer.render(new StringReader(src), configurationProvider.defaultInputSyntax, preTransformation, postTransformation) { streamResult += it }
        assert streamResult == result

        streamResult = ""
        streamRenderer.render(new StringReader(src), configurationProvider.defaultInputSyntax, configurationProvider.defaultOutputSyntax, preTransformation, postTransformation) { streamResult += it }
        assert streamResult == result

        result
    }
}
