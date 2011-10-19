package com.monochromeroad.grails.plugins.xwiki;

import org.xwiki.rendering.block.XDOM;
import org.xwiki.rendering.parser.ParseException;
import org.xwiki.rendering.parser.Parser;

import java.io.Reader;

/**
 * XWiki XDOM Builder
 *
 * @author Masatoshi Hayashi
 */
public class XDOMBuilder {

    /**
     * Builds XDOM Tree from a source text reader.
     *
     * @param source source text reader
     * @param parser wiki text parser
     * @return the built XDOM
     */
    public XDOM build(Reader source, Parser parser) {
        try {
            return parser.parse(source);
        } catch (ParseException e) {
            throw new IllegalStateException(e);
        }
    }

}
