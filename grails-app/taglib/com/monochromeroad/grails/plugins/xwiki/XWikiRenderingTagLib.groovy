package com.monochromeroad.grails.plugins.xwiki

class XWikiRenderingTagLib {

    static namespace = "xwiki"

    def xwikiRenderer

    def xwikiConfigurationProvider

    /**
     * Renders Wiki Text
     *
     * @attrs inputSyntax WikiText Syntax ("xwiki/2.1", "mediawiki/1.0",...)
     * @attrs outputSyntax Output Format ("xhtml/1.0", "plain/1.0", ...)
     */
    def render = { attrs, body ->
        def inputSyntax = (attrs.inputSyntax) ?: xwikiConfigurationProvider.defaultInputSyntax
        def outputSyntax = (attrs.outputSyntax) ?: xwikiConfigurationProvider.defaultOutputSyntax

        xwikiRenderer.render(body().reader, out, inputSyntax, outputSyntax)
    }

}
