package com.monochromeroad.grails.plugins.xwiki

/**
 * For being compatible with 0.1
 */
class XWikiRenderingPreTagLib {

    def xwikiRenderer

    def xwikiConfigurationProvider

    /**
     * Renders Wiki Text as XHTML 1.0
     *
     * @attrs syntax WikiText Syntax ("xwiki/2.1", "mediawiki/1.0",...)
     */
    def xwikiRender = { attrs, body ->
        def inputSyntax = (attrs.syntax) ?: xwikiConfigurationProvider.defaultInputSyntax
        xwikiRenderer.render(
                body().reader, out, inputSyntax, xwikiConfigurationProvider.defaultOutputSyntax)
    }

}
