package com.monochromeroad.grails.plugins.xwiki;

import org.xwiki.rendering.block.XDOM;
import org.xwiki.rendering.parser.Parser;

/**
 * XDOM Transform Class
 *
 * @author Masatoshi Hayashi
 */
public interface XDOMTransformer {

    /**
     * Transforms the XDOM Object
     *
     * @param xdom the XDOM object
     * @param parser XDOM parser
     */
    public void transform(XDOM xdom, Parser parser, Object ...parameters);

}
