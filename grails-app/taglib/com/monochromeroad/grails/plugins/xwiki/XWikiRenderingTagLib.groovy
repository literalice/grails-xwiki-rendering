package com.monochromeroad.grails.plugins.xwiki

import org.xwiki.rendering.syntax.Syntax

class XWikiRenderingTagLib {

    static namespace = "xwiki"

    def xwikiRenderer

    def defaultInputSyntax = Syntax.XWIKI_2_1.toIdString()

    def defaultOutputSyntax = Syntax.XHTML_1_0.toIdString()

    /**
     * Renders Wiki Text
     *
     * @attrs inputSyntax WikiText Syntax ("xwiki/2.1", "mediawiki/1.0",...)
     * @attrs outputSyntax Output Format ("xhtml/1.0", "plain/1.0", ...)
     */
    def render = { attrs, body ->
        def inputSyntax = (attrs.inputSyntax) ?: defaultInputSyntax
        def outputSyntax = (attrs.outputSyntax) ?: defaultOutputSyntax

        xwikiRenderer.render(body().reader, out, inputSyntax, outputSyntax)
    }

}
