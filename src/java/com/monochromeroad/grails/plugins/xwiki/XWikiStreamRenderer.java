package com.monochromeroad.grails.plugins.xwiki;

import groovy.lang.Closure;
import org.xwiki.rendering.block.XDOM;
import org.xwiki.rendering.parser.ParseException;
import org.xwiki.rendering.parser.StreamParser;
import org.xwiki.rendering.renderer.Renderer;
import org.xwiki.rendering.syntax.Syntax;
import org.xwiki.rendering.transformation.Transformation;

import java.io.Reader;
import java.util.*;

/**
 * XWiki Rendering System -- streaming based
 *
 * <br /><br />
 *
 * Default syntax: xwiki/2.1 output: xhtml/1.0
 *
 * @author Masatoshi Hayashi
 */
public class XWikiStreamRenderer extends XWikiRenderingSystem {

    public XWikiStreamRenderer(XWikiComponentManager componentManager, XWikiConfigurationProvider configuration) {
        super(componentManager, configuration);
    }

    /**
     * For the Grails Default XWiki Rendering System.
     * It need to be initialized after construction.
     */
    XWikiStreamRenderer() {}

    /**
     * XWiki rendering
     *
     * @param source source text reader
     * @param inputSyntax inputSyntax
     * @param outputSyntax outputSyntax
     * @param callback a callback function processed on parsing a text
     */
    public void render(Reader source, Syntax inputSyntax, Syntax outputSyntax, Closure callback) {
        if (configurationProvider.isMacrosEnabled()) {
            renderOnXDOM(source, inputSyntax, outputSyntax, Collections.<Transformation>emptyList(), callback);
        } else {
            renderOnStream(source, inputSyntax, outputSyntax, callback);
        }
    }

    /**
     * XWiki XHTML rendering
     *
     * @param source source text reader
     * @param inputSyntax inputSyntax
     * @param callback a callback function processed on parsing a text
     */
    public void render(Reader source, Syntax inputSyntax, Closure callback) {
        Syntax outputSyntax = configurationProvider.getDefaultOutputSyntax();
        render(source, inputSyntax, outputSyntax, callback);
    }

    /**
     * XWiki XHTML rendering using the default syntax
     *
     * @param source source text reader
     * @param callback a callback function processed on parsing a text
     */
    public void render(Reader source, Closure callback) {
        Syntax inputSyntax = configurationProvider.getDefaultInputSyntax();
        Syntax outputSyntax = configurationProvider.getDefaultOutputSyntax();
        render(source, inputSyntax, outputSyntax, callback);
    }

    /**
     * XWiki rendering
     *
     * @param source source text reader
     * @param inputSyntax inputSyntax
     * @param outputSyntax outputSyntax
     * @param transformations transform parameters
     * @param callback a callback function processed on parsing a text
     */
    public void render(Reader source, Syntax inputSyntax, Syntax outputSyntax, List<Transformation> transformations, Closure callback) {
        if (configurationProvider.isMacrosEnabled() || !transformations.isEmpty()) {
            renderOnXDOM(source, inputSyntax, outputSyntax, transformations, callback);
        } else {
            renderOnStream(source, inputSyntax, outputSyntax, callback);
        }
    }

    /**
     * XWiki XHTML rendering
     *
     * @param source source text reader
     * @param inputSyntax inputSyntax
     * @param transformations transform parameters
     * @param callback a callback function processed on parsing a text
     */
    public void render(Reader source, Syntax inputSyntax, List<Transformation> transformations, Closure callback) {
        Syntax outputSyntax = configurationProvider.getDefaultOutputSyntax();
        render(source, inputSyntax, outputSyntax, transformations, callback);
    }

    /**
     * XWiki XHTML rendering using the default syntax
     *
     * @param source source text reader
     * @param transformations transform parameters
     * @param callback a callback function processed on parsing a text
     */
    public void render(Reader source, List<Transformation> transformations, Closure callback) {
        Syntax inputSyntax = configurationProvider.getDefaultInputSyntax();
        Syntax outputSyntax = configurationProvider.getDefaultOutputSyntax();
        render(source, inputSyntax, outputSyntax, transformations, callback);
    }

    private void renderOnXDOM(Reader source, Syntax inputSyntax, Syntax outputSyntax, List<Transformation> transformations, Closure callback) {
        XDOM xdom = buildXDOM(source, inputSyntax);
        transform(xdom, inputSyntax, transformations);
        applyRenderer(xdom, outputSyntax, new XWikiCallbackPrinter(callback));
    }

    private void renderOnStream(Reader source, Syntax inputSyntax, Syntax outputSyntax, Closure callback) {
        StreamParser streamParser = componentManager.getInstance(StreamParser.class, inputSyntax.toIdString());
        XWikiCallbackPrinter printer = new XWikiCallbackPrinter(callback);
        Renderer renderer = createRenderer(outputSyntax, printer);
        try {
            streamParser.parse(source, renderer);
        } catch (ParseException e) {
            throw new IllegalStateException(e);
        }
    }
}

