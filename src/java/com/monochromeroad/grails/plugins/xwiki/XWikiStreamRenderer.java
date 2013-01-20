package com.monochromeroad.grails.plugins.xwiki;

import groovy.lang.Closure;
import org.xwiki.rendering.block.XDOM;
import org.xwiki.rendering.parser.ParseException;
import org.xwiki.rendering.parser.StreamParser;
import org.xwiki.rendering.renderer.Renderer;
import org.xwiki.rendering.syntax.Syntax;
import org.xwiki.rendering.transformation.Transformation;

import java.io.Reader;
import java.util.Collections;

/**
 * XWiki Rendering System -- streaming based
 *
 * <p>
 * If you use some macros or transformations, it needs to create a XDOM that represents the whole document structure in a memory.
 * </p>
 *
 * <dl>
 *     <dt>Default Syntax</dt>
 *     <dd>xwiki/2.1</dd>
 *     <dt>Output</dt>
 *     <dd>xhtml/1.0</dd>
 * </dl>
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
     * @param preTransformations transform parameters before applying macros
     * @param postTransformations transform parameters after applying macros
     * @param callback a callback function processed on parsing a text
     */
    public void render(Reader source, Syntax inputSyntax, Syntax outputSyntax, Iterable<Transformation> preTransformations, Iterable<Transformation> postTransformations, Closure callback) {
        if (configurationProvider.isMacrosEnabled() || preTransformations.iterator().hasNext() || postTransformations.iterator().hasNext()) {
            renderOnXDOM(source, inputSyntax, outputSyntax, preTransformations, postTransformations, callback);
        } else {
            renderOnStream(source, inputSyntax, outputSyntax, callback);
        }
    }

    /**
     * XWiki rendering
     *
     * @param source source text reader
     * @param inputSyntax inputSyntax
     * @param outputSyntax outputSyntax
     * @param callback a callback function processed on parsing a text
     */
    public void render(Reader source, Syntax inputSyntax, Syntax outputSyntax, Closure callback) {
        render(source, inputSyntax, outputSyntax, Collections.<Transformation>emptyList(), Collections.<Transformation>emptyList(), callback);
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
    public void render(Reader source, Syntax inputSyntax, Syntax outputSyntax, Iterable<Transformation> transformations, Closure callback) {
        render(source, inputSyntax, outputSyntax, Collections.<Transformation>emptyList(), transformations, callback);
    }

    /**
     * XWiki XHTML rendering
     *
     * @param source source text reader
     * @param inputSyntax inputSyntax
     * @param transformations transform parameters
     * @param callback a callback function processed on parsing a text
     */
    public void render(Reader source, Syntax inputSyntax, Iterable<Transformation> transformations, Closure callback) {
        Syntax outputSyntax = configurationProvider.getDefaultOutputSyntax();
        render(source, inputSyntax, outputSyntax, transformations, callback);
    }

    /**
     * XWiki XHTML rendering
     *
     * @param source source text reader
     * @param inputSyntax inputSyntax
     * @param preTransformations transform parameters
     * @param postTransformations transform parameters
     * @param callback a callback function processed on parsing a text
     */
    public void render(Reader source, Syntax inputSyntax, Iterable<Transformation> preTransformations, Iterable<Transformation> postTransformations, Closure callback) {
        Syntax outputSyntax = configurationProvider.getDefaultOutputSyntax();
        render(source, inputSyntax, outputSyntax, preTransformations, postTransformations, callback);
    }

    /**
     * XWiki XHTML rendering using the default syntax
     *
     * @param source source text reader
     * @param transformations transform parameters
     * @param callback a callback function processed on parsing a text
     */
    public void render(Reader source, Iterable<Transformation> transformations, Closure callback) {
        Syntax inputSyntax = configurationProvider.getDefaultInputSyntax();
        Syntax outputSyntax = configurationProvider.getDefaultOutputSyntax();
        render(source, inputSyntax, outputSyntax, transformations, callback);
    }

    /**
     * XWiki XHTML rendering using the default syntax
     *
     * @param source source text reader
     * @param preTransformations transform parameters
     * @param postTransformations transform parameters
     * @param callback a callback function processed on parsing a text
     */
    public void render(Reader source, Iterable<Transformation> preTransformations, Iterable<Transformation> postTransformations, Closure callback) {
        Syntax inputSyntax = configurationProvider.getDefaultInputSyntax();
        Syntax outputSyntax = configurationProvider.getDefaultOutputSyntax();
        render(source, inputSyntax, outputSyntax, preTransformations, postTransformations, callback);
    }

    private void renderOnXDOM(Reader source, Syntax inputSyntax, Syntax outputSyntax, Iterable<Transformation> preTransformations, Iterable<Transformation> postTransformations, Closure callback) {
        XDOM xdom = buildXDOM(source, inputSyntax);
        transform(xdom, inputSyntax, preTransformations, postTransformations);
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

