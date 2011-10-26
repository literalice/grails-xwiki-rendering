package com.monochromeroad.grails.plugins.xwiki

import grails.plugin.spock.GroovyPagesSpec

/**
 * The specification for the tag library.
 */
class XWikiRenderingTagLibSpec extends GroovyPagesSpec {

    def setup() {
        String.metaClass.getReader = {
            return new StringReader(delegate)
        }
    }

    def "Rendering XWiki text"() {
        when:
        template = '<xwiki:render inputSyntax="mediawiki/1.0" outputSyntax="plain/1.0">\'\'\'bold\'\'\'</xwiki:render>'
        then:
        output == "bold"
    }

    def "Rendering XWiki text using default input syntax xwiki and default output syntax xhtml"() {
        when:
        template = '<xwiki:render>**bold**</xwiki:render>'
        then:
        output == "<p><strong>bold</strong></p>"
    }
}
