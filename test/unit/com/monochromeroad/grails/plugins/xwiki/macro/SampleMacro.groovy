package com.monochromeroad.grails.plugins.xwiki.macro

import org.xwiki.rendering.block.Block
import org.xwiki.rendering.block.WordBlock
import org.xwiki.rendering.transformation.MacroTransformationContext

/**
 * @author Masatoshi Hayashi
 */
class SampleMacro extends DefaultXWikiMacro<SampleMacroParameter> {

    SampleMacro() {
        super("sample", SampleMacroParameter)
    }

    @Override
    boolean supportsInlineMode() { true }

    @Override
    List<Block> execute(SampleMacroParameter p, String content, MacroTransformationContext macroTransformationContext) {
        [new WordBlock("data:${p.data}[inserted by a macro: ${macroName}]")]
    }
}

class SampleMacroParameter {
    String data
}

