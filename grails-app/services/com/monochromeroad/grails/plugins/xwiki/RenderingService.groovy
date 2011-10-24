package com.monochromeroad.grails.plugins.xwiki

import org.xwiki.rendering.syntax.Syntax

/**
 * For being compatible with 0.1
 */
class RenderingService {

    boolean transactional = false

    def xwikiRenderer

    def defaultOutputSyntax = Syntax.XHTML_1_0.toIdString()

    String renderAsXHTML(String syntax, String input) {
        return xwikiRenderer.render(input, syntax, defaultOutputSyntax)
    }
}
