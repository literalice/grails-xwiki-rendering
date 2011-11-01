package com.monochromeroad.grails.plugins.xwiki.macro

import org.xwiki.rendering.block.Block
import org.xwiki.rendering.transformation.MacroTransformationContext
import org.xwiki.rendering.macro.MacroExecutionException
import org.apache.commons.lang3.StringEscapeUtils
import org.xwiki.rendering.block.RawBlock
import org.xwiki.rendering.block.ParagraphBlock
import org.xwiki.rendering.syntax.Syntax;

/**
 * HTML Ruby Macro.<br />Usage:
 * <pre>
 * {{rb read="Ruby Text"}}Target Text{{/rb}}
 * </pre>
 */
public class RbMacro implements GrailsMacro {

    static macroName = "rb"

    static inlineSupport = true

    static parametersBeanClass = RbMacroParameter

    public List<Block> execute(Object parameters, String content,
                            MacroTransformationContext context) throws MacroExecutionException {
        if (!content) {
            return Collections.emptyList();
        }

        String rubyText = "<ruby>" + StringEscapeUtils.escapeHtml4(content) +
                                "<rp>(</rp><rt>" + StringEscapeUtils.escapeHtml4(parameters.getRead()) + "</rt><rp>)</rp></ruby>";
        RawBlock rubyBlock = new RawBlock(rubyText, Syntax.XHTML_1_0);
        if (context.isInline()) {
            return Collections.<Block>singletonList(rubyBlock);
        } else {
            return Collections.<Block>singletonList(
                        new ParagraphBlock(Collections.<Block>singletonList(rubyBlock)));
        }
    }
}

public class RbMacroParameter {
    String read
}

