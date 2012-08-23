package com.monochromeroad.grails.plugins.xwiki;

import org.xwiki.rendering.block.XDOM;
import org.xwiki.rendering.parser.ParseException;
import org.xwiki.rendering.parser.Parser;
import org.xwiki.rendering.renderer.PrintRendererFactory;
import org.xwiki.rendering.renderer.Renderer;
import org.xwiki.rendering.renderer.printer.DefaultWikiPrinter;
import org.xwiki.rendering.renderer.printer.WikiPrinter;
import org.xwiki.rendering.syntax.Syntax;
import org.xwiki.rendering.transformation.Transformation;
import org.xwiki.rendering.transformation.TransformationContext;
import org.xwiki.rendering.transformation.TransformationException;

import java.io.*;

/**
 * Wiki Text Renderer using XWiki Rendering Engine.
 *
 * @author Masatoshi Hayashi
 */
public class XWikiRenderer {

    private XWikiConfigurationProvider configurationProvider;

    private XWikiComponentManager componentManager;

    /**
     * For the Grails Default XWiki Rendering System.
     *
     * It need to be initialized after construction.
     */
    XWikiRenderer() {}

    public XWikiRenderer(XWikiComponentManager componentManager, XWikiConfigurationProvider configuration) {
        initialize(componentManager, configuration);
    }

    /**
     * Returns rendered text from XWiki Rendering.
     *
     * @param source source text reader
     * @param inputSyntax inputSyntax
     * @param outputSyntax outputSyntax
     * @param transformations transform parameters
     * @return a rendered result
     */
    public String render(Reader source, Syntax inputSyntax, Syntax outputSyntax, Transformation ...transformations) {
        XDOM xdom = buildXDOM(source, inputSyntax);
        transform(xdom, inputSyntax, transformations);
        return convertToString(xdom, outputSyntax);
    }

    /**
     * Returns rendered text from XWiki Rendering using default input syntax and default output syntax.
     *
     * @param source a source text
     * @param transformations transform parameters
     * @return a rendered result
     */
    public String render(Reader source, Transformation ...transformations) {
        Syntax inputSyntax = configurationProvider.getDefaultInputSyntax();
        Syntax outputSyntax = configurationProvider.getDefaultOutputSyntax();
        return render(source, inputSyntax, outputSyntax, transformations);
    }

    /**
     * Returns rendered text from XWiki Rendering.
     *
     * @param source a source text
     * @param inputSyntax inputSyntax
     * @param outputSyntax outputSyntax
     * @param transformations transform parameters
     * @return a rendered result
     */
    public String render(String source, Syntax inputSyntax, Syntax outputSyntax, Transformation ...transformations) {
        return render(new StringReader(source), inputSyntax, outputSyntax, transformations);
    }

    private XDOM buildXDOM(Reader source, Syntax input) {
        Parser parser = componentManager.getInstance(Parser.class, input.toIdString());
        try {
            return parser.parse(source);
        } catch (ParseException e) {
            throw new IllegalStateException(e);
        }
    }

    private void transform(XDOM xdom, Syntax syntax, Transformation ...transformations) {
        TransformationContext txContext = new TransformationContext(xdom, syntax);
        try {
            for (Transformation transformation : transformations) {
                    transformation.transform(xdom, txContext);
            }
        } catch (TransformationException e) {
            throw new IllegalStateException(e);
        }
    }

    private void applyRenderer(XDOM xdom, Syntax syntax, WikiPrinter printer) {
        Renderer renderer = createRenderer(syntax, printer);
        xdom.traverse(renderer);
    }

    private String convertToString(XDOM xdom, Syntax syntax) {
        DefaultWikiPrinter printer = new DefaultWikiPrinter();
        applyRenderer(xdom, syntax, printer);
        return printer.toString();
    }

    private Renderer createRenderer(Syntax output, WikiPrinter printer) {
        PrintRendererFactory rendererFactory = componentManager.getInstance(PrintRendererFactory.class, output.toIdString());
        return rendererFactory.createRenderer(printer);
    }

    /**
     * Returns rendered text from XWiki Rendering using default input syntax and default output syntax.
     *
     * @param source a source text
     * @param transformations transform parameters
     * @return a rendered result
     */
    public String render(String source, Transformation ...transformations) {
        Syntax inputSyntax = configurationProvider.getDefaultInputSyntax();
        Syntax outputSyntax = configurationProvider.getDefaultOutputSyntax();
        return render(source, inputSyntax, outputSyntax, transformations);
    }

    public void initialize(XWikiComponentManager componentManager, XWikiConfigurationProvider configurationProvider) {
        this.componentManager = componentManager;
        this.configurationProvider = configurationProvider;
    }

}
