package com.monochromeroad.grails.plugins.xwiki;

import org.xwiki.component.manager.ComponentManager;
import org.xwiki.rendering.block.XDOM;
import org.xwiki.rendering.parser.Parser;
import org.xwiki.rendering.transformation.Transformation;
import org.xwiki.rendering.transformation.TransformationContext;
import org.xwiki.rendering.transformation.TransformationException;

/**
 * Default Transform Class for XWiki macro support.
 *
 * @author Masatoshi Hayashi
 */
public class DefaultXDOMTransformation implements XDOMTransformation {

    private final XWikiComponentRepository componentRepository;

    public DefaultXDOMTransformation(ComponentManager componentManager) {
        this.componentRepository = new XWikiComponentRepository(componentManager);
    }

    /**
     * Applies XWiki macros to the XDOM
     *
     * @param xdom the XDOM object
     * @param parser XDOM parser
     */
    public void transform(XDOM xdom, Parser parser, Object ...parameters) {
        try {
            Transformation transform = componentRepository.lookupComponent(Transformation.class, "macro");
            TransformationContext context = new TransformationContext(xdom, parser.getSyntax());
            transform.transform(xdom, context);
        } catch (TransformationException e) {
            throw new IllegalStateException(e);
        }

    }
}
