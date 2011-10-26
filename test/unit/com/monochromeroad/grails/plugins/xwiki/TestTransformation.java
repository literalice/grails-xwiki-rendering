/**
 * 11/10/20
 *
 * Copyright (c) 2011 Monochromeroad
 */
package com.monochromeroad.grails.plugins.xwiki;

import org.xwiki.rendering.block.RawBlock;
import org.xwiki.rendering.block.XDOM;
import org.xwiki.rendering.parser.Parser;
import org.xwiki.rendering.syntax.Syntax;

public class TestTransformation implements XDOMTransformation {

    public void transform(XDOM xdom, Parser parser, Object... parameters) {
        if (parameters.length > 2) {
            xdom.addChild(new RawBlock((String) parameters[2], Syntax.XHTML_1_0));
        }
    }

}
