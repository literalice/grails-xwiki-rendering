package com.monochromeroad.grails.plugins.xwiki.macro;

import org.xwiki.rendering.block.Block;
import org.xwiki.rendering.macro.MacroExecutionException;
import org.xwiki.rendering.transformation.MacroTransformationContext;

import java.util.List;

public interface GrailsMacro {
    public List<Block> execute(
            Object parameters, String content, MacroTransformationContext context)
                throws MacroExecutionException;
}

