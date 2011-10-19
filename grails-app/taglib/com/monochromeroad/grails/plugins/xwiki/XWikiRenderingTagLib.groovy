package com.monochromeroad.grails.plugins.xwiki

import org.xwiki.rendering.syntax.Syntax

class XWikiRenderingTagLib {

    static namespace = "xwiki"

    def xwikiRenderer

    def defaultInputSyntax = Syntax.XWIKI_2_1.toIdString()

    def defaultOutputSyntax = Syntax.XHTML_1_0.toIdString()

    /**
     * Rendering Wiki Text
     *
     * @attrs syntax WikiText Syntax ("xwiki/2.1", "mediawiki/1.0",...)
     * @attrs format Output Format ("xhtml/1.0", "plain/1.0", ...)
     */
    def renderer = { attrs, body ->
        def inputSyntax = (attrs.syntax) ?: defaultInputSyntax
        def outputSyntax = (attrs.format) ?: defaultOutputSyntax

        xwikiRenderer.render(body().reader, out, inputSyntax, outputSyntax)
    }

}
