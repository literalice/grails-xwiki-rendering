package com.monochromeroad.grails.plugins.xwiki;

import org.xwiki.rendering.parser.Parser;
import org.xwiki.rendering.syntax.Syntax;

class XWikiParserFactory {

    XWikiComponentRepository componentRepository;

    XWikiParserFactory(XWikiComponentRepository componentRepository) {
        this.componentRepository = componentRepository;
    }

    Parser getParser(Syntax syntax) {
        return componentRepository.
                lookupComponent(Parser.class, syntax.toIdString());
    }
}
