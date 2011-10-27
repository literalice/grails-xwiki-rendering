package com.monochromeroad.grails.plugins.xwiki

import org.codehaus.groovy.grails.web.util.StreamCharBuffer

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

        def bodyReader = body()
        def bodyContent
        if (bodyReader instanceof StreamCharBuffer) {
            bodyContent = bodyReader.reader
        } else {
            bodyContent = bodyReader.toString()
        }
        xwikiRenderer.render(bodyContent, out, inputSyntax, outputSyntax)
    }

}
