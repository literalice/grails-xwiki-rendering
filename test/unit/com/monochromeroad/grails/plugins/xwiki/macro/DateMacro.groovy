package com.monochromeroad.grails.plugins.xwiki.macro

import org.xwiki.rendering.block.Block
import org.xwiki.rendering.transformation.MacroTransformationContext
import org.xwiki.rendering.block.RawBlock
import org.xwiki.rendering.syntax.Syntax

/**
 * <p>Formatted Date Macro.</p>
 *
 * Usage:
 * <pre>
 * {{date /}}
 * </pre>
 * or
 * <pre>
 * {{date}}yyyy-MM-dd{{/date}}
 * </pre>
 */
class DateMacro extends DefaultXWikiNoParameterMacro {

    public DateMacro() {
        super("date")
    }

    @Override
    boolean supportsInlineMode() {
        return true
    }

    @Override
    List<Block> execute(String content, MacroTransformationContext context) {
        String formattedDate = new Date().format((content) ?: "yyyy/MM/dd")

        return Collections.<Block>singletonList(
                new RawBlock("<span class=\"dateMacro\">${formattedDate}</span>", Syntax.XHTML_1_0))
    }
}
