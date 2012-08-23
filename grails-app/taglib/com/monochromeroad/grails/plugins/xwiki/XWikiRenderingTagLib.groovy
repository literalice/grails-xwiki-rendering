package com.monochromeroad.grails.plugins.xwiki

import org.codehaus.groovy.grails.web.util.StreamCharBuffer

class XWikiRenderingTagLib {

    static namespace = "xwiki"

    def xwikiRenderer

    def defaultXWikiConfigurationProvider

    def defaultXWikiSyntaxFactory


    /**
     * Renders Wiki Text
     *
     * @attrs inputSyntax WikiText Syntax ("xwiki/2.1", "mediawiki/1.0",...)
     * @attrs outputSyntax Output Format ("xhtml/1.0", "plain/1.0", ...)
     */
    def render = { attrs, body ->
        def inputSyntax = (attrs.inputSyntax) ? defaultXWikiSyntaxFactory.getSyntax(attrs.inputSyntax) : defaultXWikiConfigurationProvider.defaultInputSyntax
        def outputSyntax = (attrs.outputSyntax) ? defaultXWikiSyntaxFactory.getSyntax(attrs.outputSyntax) : defaultXWikiConfigurationProvider.defaultOutputSyntax

        def bodyReader = body()
        def bodyContent
        if (bodyReader instanceof StreamCharBuffer) {
            bodyContent = bodyReader.reader
        } else {
            bodyContent = bodyReader.toString()
        }
        out << xwikiRenderer.render(bodyContent, inputSyntax, outputSyntax)
    }

}
