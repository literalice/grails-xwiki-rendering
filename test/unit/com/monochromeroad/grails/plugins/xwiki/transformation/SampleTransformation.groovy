package com.monochromeroad.grails.plugins.xwiki.transformation

import org.xwiki.rendering.block.Block
import org.xwiki.rendering.block.WordBlock
import org.xwiki.rendering.block.match.ClassBlockMatcher
import org.xwiki.rendering.transformation.AbstractTransformation
import org.xwiki.rendering.transformation.TransformationContext
import org.xwiki.rendering.transformation.TransformationException

/**
 * @author Masatoshi Hayashi
 */
class SampleTransformation extends AbstractTransformation {

    String name

    SampleTransformation(String name) {
        this.name = name
    }

    @Override
    void transform(Block block, TransformationContext transformationContext) throws TransformationException {
        def comment = "It applies the transformation $name! "
        def wordBlocks = block.<WordBlock>getBlocks(new ClassBlockMatcher(WordBlock.class), Block.Axes.DESCENDANT_OR_SELF)
        if (wordBlocks.any{ it.word.contains("macro") }) {
            comment += "[.. may be applied a macro.] "
        }
        block.addChild(new WordBlock(comment))
    }

}
