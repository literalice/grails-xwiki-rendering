package com.monochromeroad.grails.plugins.xwiki;

import org.xwiki.rendering.block.XDOM;
import org.xwiki.rendering.parser.ParseException;
import org.xwiki.rendering.parser.Parser;
import org.xwiki.rendering.renderer.PrintRendererFactory;
import org.xwiki.rendering.renderer.Renderer;
import org.xwiki.rendering.renderer.printer.WikiPrinter;
import org.xwiki.rendering.syntax.Syntax;
import org.xwiki.rendering.transformation.Transformation;
import org.xwiki.rendering.transformation.TransformationContext;
import org.xwiki.rendering.transformation.TransformationException;

import java.io.Reader;
import java.util.Arrays;

/**
 * Base functions for XWiki rendering system.
 *
 * @author Masatoshi Hayashi
 */
public abstract class XWikiRenderingSystem {

    protected XWikiConfigurationProvider configurationProvider;

    protected XWikiComponentManager componentManager;

    protected XWikiRenderingSystem(XWikiComponentManager componentManager, XWikiConfigurationProvider configuration) {
        initialize(componentManager, configuration);
    }

    protected XWikiRenderingSystem() { }

    final void initialize(XWikiComponentManager componentManager, XWikiConfigurationProvider configurationProvider) {
        this.componentManager = componentManager;
        this.configurationProvider = configurationProvider;
    }

    protected final XDOM buildXDOM(Reader source, Syntax input) {
        Parser parser = componentManager.getInstance(Parser.class, input.toIdString());
        try {
            return parser.parse(source);
        } catch (ParseException e) {
            throw new IllegalStateException(e);
        }
    }

    protected final void transform(XDOM xdom, Syntax syntax, Transformation ...transformations) {
        transform(xdom, syntax, Arrays.asList(transformations));
    }

    protected final void transform(XDOM xdom, Syntax syntax, Iterable<Transformation> transformations) {
        TransformationContext txContext = new TransformationContext(xdom, syntax);

        try {
            if (configurationProvider.isMacrosEnabled()) {
                getTransformationForMacro().transform(xdom, txContext);
            }
            for (Transformation transformation : transformations) {
                transformation.transform(xdom, txContext);
            }
        } catch (TransformationException e) {
            throw new IllegalStateException(e);
        }
    }

    protected final Transformation getTransformationForMacro() {
        return componentManager.getInstance(Transformation.class, "macro");
    }

    protected final void applyRenderer(XDOM xdom, Syntax syntax, WikiPrinter printer) {
        Renderer renderer = createRenderer(syntax, printer);
        xdom.traverse(renderer);
    }

    protected final Renderer createRenderer(Syntax output, WikiPrinter printer) {
        PrintRendererFactory rendererFactory = componentManager.getInstance(PrintRendererFactory.class, output.toIdString());
        return rendererFactory.createRenderer(printer);
    }

}
