package com.monochromeroad.grails.plugins.xwiki.macro

import org.xwiki.rendering.block.Block
import org.xwiki.rendering.transformation.MacroTransformationContext
import org.xwiki.rendering.block.RawBlock
import org.xwiki.rendering.syntax.Syntax

class DateMacro implements GrailsMacro {

    static macroName = "date"

    static inlineSupport = true

    List<Block> execute(Object parameters, String content, MacroTransformationContext context) {
        return Collections.<Block>singletonList(
                new RawBlock(new Date().format("yyyy/MM/dd(E)"), Syntax.XHTML_1_0));
    }
}
