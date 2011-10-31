/**
 * 11/10/20
 *
 * Copyright (c) 2011 Monochromeroad
 */
package com.monochromeroad.grails.plugins.xwiki;

import org.xwiki.rendering.block.Block;
import org.xwiki.rendering.block.RawBlock;
import org.xwiki.rendering.syntax.Syntax;
import org.xwiki.rendering.transformation.AbstractTransformation;
import org.xwiki.rendering.transformation.TransformationContext;
import org.xwiki.rendering.transformation.TransformationException;

public class TestTransformation extends AbstractTransformation {

    private String parameter;

    private int priority;

    public TestTransformation(String parameter, int priority) {
        this.parameter = parameter;
        this.priority = priority;
    }

    public void transform(
            Block block, TransformationContext transformationContext) throws TransformationException {
        block.addChild(new RawBlock(parameter, Syntax.XHTML_1_0));
    }

    public int getPriority() {
        return this.priority;
    }
}
