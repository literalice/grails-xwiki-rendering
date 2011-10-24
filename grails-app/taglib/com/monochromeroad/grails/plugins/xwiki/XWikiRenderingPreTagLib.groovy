package com.monochromeroad.grails.plugins.xwiki

import org.xwiki.rendering.syntax.Syntax

/**
 * For being compatible with 0.1
 */
class XWikiRenderingPreTagLib {

    def xwikiRenderer

    def defaultInputSyntax = Syntax.XWIKI_2_0.toIdString()

    def defaultOutputSyntax = Syntax.XHTML_1_0.toIdString()

    /**
     * Renders Wiki Text as XHTML 1.0
     *
     * @attrs syntax WikiText Syntax ("xwiki/2.1", "mediawiki/1.0",...)
     */
    def xwikiRender = { attrs, body ->
        def inputSyntax = (attrs.syntax) ?: defaultInputSyntax
        xwikiRenderer.render(
                body().reader, out, inputSyntax, defaultOutputSyntax)
    }

}
