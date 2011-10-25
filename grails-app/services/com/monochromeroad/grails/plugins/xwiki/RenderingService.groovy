package com.monochromeroad.grails.plugins.xwiki

/**
 * For being compatible with 0.1
 */
class RenderingService {

    boolean transactional = false

    def xwikiRenderer

    def xwikiConfigurationProvider

    String renderAsXHTML(String syntax, String input) {
        return xwikiRenderer.render(
                input, syntax, xwikiConfigurationProvider.defaultOutputSyntax)
    }
}
