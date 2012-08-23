package com.monochromeroad.grails.plugins.xwiki;

import org.xwiki.rendering.parser.ParseException;
import org.xwiki.rendering.syntax.Syntax;
import org.xwiki.rendering.syntax.SyntaxFactory;

public class XWikiSyntaxFactory {

    private SyntaxFactory syntaxFactory;

    public XWikiSyntaxFactory() {}

    public XWikiSyntaxFactory(XWikiComponentManager componentManager) {
        initialize(componentManager);
    }

    public Syntax getSyntax(String syntaxId) {
        try {
            return syntaxFactory.createSyntaxFromIdString(syntaxId);
        } catch (ParseException e) {
            throw new IllegalArgumentException(e);
        }
    }

    public void initialize(XWikiComponentManager componentManager) {
        this.syntaxFactory = componentManager.getInstance(SyntaxFactory.class);
    }
}
