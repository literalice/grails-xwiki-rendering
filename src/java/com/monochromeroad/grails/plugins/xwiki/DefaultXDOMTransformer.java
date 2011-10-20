package com.monochromeroad.grails.plugins.xwiki;

import org.xwiki.component.manager.ComponentManager;
import org.xwiki.rendering.block.XDOM;
import org.xwiki.rendering.parser.Parser;
import org.xwiki.rendering.transformation.TransformationContext;
import org.xwiki.rendering.transformation.TransformationException;
import org.xwiki.rendering.transformation.TransformationManager;

/**
 * Default Transform Class for XWiki macro support.
 *
 * @author Masatoshi Hayashi
 */
public class DefaultXDOMTransformer implements XDOMTransformer {

    private final XWikiComponentRepository componentRepository;

    public DefaultXDOMTransformer(ComponentManager componentManager) {
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
            TransformationManager txManager =
                    componentRepository.lookupComponent(TransformationManager.class);
            txManager.performTransformations(xdom, new TransformationContext(xdom, parser.getSyntax()));
        } catch (TransformationException e) {
            throw new IllegalStateException(e);
        }

    }
}
