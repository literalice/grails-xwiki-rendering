package com.monochromeroad.grails.plugins.xwiki.macro

import org.xwiki.rendering.block.Block
import org.xwiki.rendering.transformation.MacroTransformationContext
import org.xwiki.rendering.block.RawBlock
import org.xwiki.rendering.syntax.Syntax

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
        return Collections.<Block>singletonList(
                new RawBlock(new Date().format("yyyy/MM/dd"), Syntax.XHTML_1_0));
    }
}
