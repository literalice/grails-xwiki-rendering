package com.monochromeroad.grails.plugins.xwiki;

import org.xwiki.rendering.block.XDOM;
import org.xwiki.rendering.renderer.printer.DefaultWikiPrinter;
import org.xwiki.rendering.syntax.Syntax;
import org.xwiki.rendering.transformation.Transformation;

import java.io.*;
import java.util.Collections;

/**
 * XWiki Rendering System -- XDOM based
 *
 * <p>
 * It needs to create a XDOM that represents the whole document structure in a memory.
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
public class XWikiRenderer extends XWikiRenderingSystem {

    public XWikiRenderer(XWikiComponentManager componentManager, XWikiConfigurationProvider configuration) {
        super(componentManager, configuration);
    }

    /**
     * For the Grails Default XWiki Rendering System.
     * It need to be initialized after construction.
     */
    XWikiRenderer() {}

    /**
     * XWiki rendering
     *
     * @param source source text reader
     * @param inputSyntax inputSyntax
     * @param outputSyntax outputSyntax
     * @return a rendered result
     */
    public String render(Reader source, Syntax inputSyntax, Syntax outputSyntax) {
        return render(source, inputSyntax, outputSyntax, Collections.<Transformation>emptyList(), Collections.<Transformation>emptyList());
    }

    /**
     * XWiki rendering
     *
     * @param source source text reader
     * @param inputSyntax inputSyntax
     * @param outputSyntax outputSyntax
     * @param transformations transforms applied after macros
     * @return a rendered result
     */
    public String render(Reader source, Syntax inputSyntax, Syntax outputSyntax, Iterable<Transformation> transformations) {
        return render(source, inputSyntax, outputSyntax, Collections.<Transformation>emptyList(), transformations);
    }

    /**
     * XWiki rendering
     *
     * @param source source text reader
     * @param inputSyntax inputSyntax
     * @param outputSyntax outputSyntax
     * @param preTransformations transforms applied before macros
     * @param postTransformations transforms applied after macros
     * @return a rendered result
     */
    public String render(Reader source, Syntax inputSyntax, Syntax outputSyntax, Iterable<Transformation> preTransformations, Iterable<Transformation> postTransformations) {
        XDOM xdom = buildXDOM(source, inputSyntax);
        transform(xdom, inputSyntax, preTransformations, postTransformations);
        return convertToString(xdom, outputSyntax);
    }

    /**
     * XWiki XHTML rendering
     *
     * @param source a source text
     * @param inputSyntax input syntax
     * @return a rendered result
     */
    public String render(Reader source, Syntax inputSyntax) {
        Syntax outputSyntax = configurationProvider.getDefaultOutputSyntax();
        return render(source, inputSyntax, outputSyntax);
    }

    /**
     * XWiki XHTML rendering
     *
     * @param source a source text
     * @param inputSyntax input syntax
     * @param transformations transforms applied after macros
     * @return a rendered result
     */
    public String render(Reader source, Syntax inputSyntax, Iterable<Transformation> transformations) {
        Syntax outputSyntax = configurationProvider.getDefaultOutputSyntax();
        return render(source, inputSyntax, outputSyntax, transformations);
    }

    /**
     * XWiki XHTML rendering
     *
     * @param source a source text
     * @param inputSyntax input syntax
     * @param preTransformations transforms applied before macros
     * @param postTransformations transforms applied after macros
     * @return a rendered result
     */
    public String render(Reader source, Syntax inputSyntax, Iterable<Transformation> preTransformations, Iterable<Transformation> postTransformations) {
        Syntax outputSyntax = configurationProvider.getDefaultOutputSyntax();
        return render(source, inputSyntax, outputSyntax, preTransformations, postTransformations);
    }

    /**
     * XWiki XHTML rendering using default syntax.
     *
     * @param source a source text
     * @return a rendered result
     */
    public String render(Reader source) {
        Syntax inputSyntax = configurationProvider.getDefaultInputSyntax();
        Syntax outputSyntax = configurationProvider.getDefaultOutputSyntax();
        return render(source, inputSyntax, outputSyntax);
    }

    /**
     * XWiki XHTML rendering using default syntax.
     *
     * @param source a source text
     * @param transformations transforms applied after macros
     * @return a rendered result
     */
    public String render(Reader source, Iterable<Transformation> transformations) {
        Syntax inputSyntax = configurationProvider.getDefaultInputSyntax();
        Syntax outputSyntax = configurationProvider.getDefaultOutputSyntax();
        return render(source, inputSyntax, outputSyntax, transformations);
    }

    /**
     * XWiki XHTML rendering using default syntax.
     *
     * @param source a source text
     * @param preTransformations transforms applied before macros
     * @param postTransformations transforms applied after macros
     * @return a rendered result
     */
    public String render(Reader source, Iterable<Transformation> preTransformations, Iterable<Transformation> postTransformations) {
        Syntax inputSyntax = configurationProvider.getDefaultInputSyntax();
        Syntax outputSyntax = configurationProvider.getDefaultOutputSyntax();
        return render(source, inputSyntax, outputSyntax, preTransformations, postTransformations);
    }

    /**
     * XWiki rendering
     *
     * @param source a source text
     * @param inputSyntax inputSyntax
     * @param outputSyntax outputSyntax
     * @return a rendered result
     */
    public String render(String source, Syntax inputSyntax, Syntax outputSyntax) {
        return render(new StringReader(source), inputSyntax, outputSyntax);
    }

    /**
     * XWiki rendering
     *
     * @param source a source text
     * @param inputSyntax inputSyntax
     * @param outputSyntax outputSyntax
     * @param transformations transforms applied after macros
     * @return a rendered result
     */
    public String render(String source, Syntax inputSyntax, Syntax outputSyntax, Iterable<Transformation> transformations) {
        return render(new StringReader(source), inputSyntax, outputSyntax, transformations);
    }

    /**
     * XWiki rendering
     *
     * @param source a source text
     * @param inputSyntax inputSyntax
     * @param outputSyntax outputSyntax
     * @param preTransformations transforms applied before macros
     * @param postTransformations transforms applied after macros
     * @return a rendered result
     */
    public String render(String source, Syntax inputSyntax, Syntax outputSyntax, Iterable<Transformation> preTransformations, Iterable<Transformation> postTransformations) {
        return render(new StringReader(source), inputSyntax, outputSyntax, preTransformations, postTransformations);
    }

    /**
     * XWiki XHTML rendering
     *
     * @param source a source text
     * @param inputSyntax inputSyntax
     * @return a rendered result
     */
    public String render(String source, Syntax inputSyntax) {
        return render(new StringReader(source), inputSyntax);
    }

    /**
     * XWiki XHTML rendering
     *
     * @param source a source text
     * @param inputSyntax inputSyntax
     * @param transformations transforms applied after macros
     * @return a rendered result
     */
    public String render(String source, Syntax inputSyntax, Iterable<Transformation> transformations) {
        return render(new StringReader(source), inputSyntax, transformations);
    }

    /**
     * XWiki XHTML rendering
     *
     * @param source a source text
     * @param inputSyntax inputSyntax
     * @param preTransformations transforms applied before macros
     * @param postTransformations transforms applied after macros
     * @return a rendered result
     */
    public String render(String source, Syntax inputSyntax, Iterable<Transformation> preTransformations, Iterable<Transformation> postTransformations) {
        return render(new StringReader(source), inputSyntax, preTransformations, postTransformations);
    }

    /**
     * XWiki XHTML rendering using default syntax
     *
     * @param source a source text
     * @return a rendered result
     */
    public String render(String source) {
        return render(new StringReader(source));
    }

    /**
     * XWiki XHTML rendering using default syntax
     *
     * @param source a source text
     * @param transformations transforms applied after macros
     * @return a rendered result
     */
    public String render(String source, Iterable<Transformation> transformations) {
        return render(new StringReader(source), transformations);
    }

    /**
     * XWiki XHTML rendering using default syntax
     *
     * @param source a source text
     * @param preTransformations transforms applied before macros
     * @param postTransformations transforms applied after macros
     * @return a rendered result
     */
    public String render(String source, Iterable<Transformation> preTransformations, Iterable<Transformation> postTransformations) {
        return render(new StringReader(source), preTransformations, postTransformations);
    }

    private String convertToString(XDOM xdom, Syntax syntax) {
        DefaultWikiPrinter printer = new DefaultWikiPrinter();
        applyRenderer(xdom, syntax, printer);
        return printer.toString();
    }
}

