package com.monochromeroad.grails.plugins.xwiki.macro;

import org.xwiki.rendering.block.Block;
import org.xwiki.rendering.macro.MacroExecutionException;
import org.xwiki.rendering.transformation.MacroTransformationContext;

import java.util.List;

public abstract class DefaultXWikiNoParameterMacro extends DefaultXWikiMacro<Object> {
    public DefaultXWikiNoParameterMacro(String name) {
        super(name, Object.class);
    }

    public final List<Block> execute(
            Object parameters, String content, MacroTransformationContext context) throws MacroExecutionException {
        return execute(content, context);
    }

    protected abstract List<Block> execute(
            String content, MacroTransformationContext context) throws MacroExecutionException;
}
