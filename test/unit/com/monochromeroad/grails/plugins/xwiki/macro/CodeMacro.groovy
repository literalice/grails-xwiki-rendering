package com.monochromeroad.grails.plugins.xwiki.macro

import org.xwiki.rendering.block.Block
import org.xwiki.rendering.block.VerbatimBlock
import org.xwiki.rendering.transformation.MacroTransformationContext

/**
 * HTML Code Macro.<br />Usage:
 * <pre>
 * {{code mode="java"}}
 * class TestClass{
 *
 * }
 * {{/code}}
 * </pre>
 */
public class CodeMacro extends DefaultXWikiMacro<CodeMacroParameter> {

    public CodeMacro() {
        super("code", CodeMacroParameter)
    }

    @Override
    boolean supportsInlineMode() {
        return true
    }

    @Override
    List<Block> execute(CodeMacroParameter parameters, String content, MacroTransformationContext context) {
        Map<String, String> params = new HashMap<String, String>();
        params.put("class", parameters.mode);
        VerbatimBlock result = new VerbatimBlock(content, params, false);

        return Collections.<Block>singletonList(result);
    }

}

public class CodeMacroParameter {
    String mode
}

