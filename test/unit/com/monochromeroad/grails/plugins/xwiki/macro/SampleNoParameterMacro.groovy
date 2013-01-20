package com.monochromeroad.grails.plugins.xwiki.macro

import org.xwiki.rendering.block.Block
import org.xwiki.rendering.block.WordBlock
import org.xwiki.rendering.macro.MacroExecutionException
import org.xwiki.rendering.transformation.MacroTransformationContext

/**
 * Created with IntelliJ IDEA.
 * User: masatoshi
 * Date: 13/01/20
 * Time: 1:47
 * To change this template use File | Settings | File Templates.
 */
class SampleNoParameterMacro extends DefaultXWikiNoParameterMacro {

    SampleNoParameterMacro() {
        super("sample2")
    }

    @Override
    protected List<Block> execute(String content, MacroTransformationContext context) throws MacroExecutionException {
        [new WordBlock("$content[inserted by a macro: ${macroName}]")]
    }

    @Override
    boolean supportsInlineMode() { true }
}
