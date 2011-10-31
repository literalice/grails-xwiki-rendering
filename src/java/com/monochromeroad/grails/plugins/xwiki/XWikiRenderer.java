package com.monochromeroad.grails.plugins.xwiki;

import org.xwiki.rendering.block.XDOM;
import org.xwiki.rendering.parser.ParseException;
import org.xwiki.rendering.parser.Parser;
import org.xwiki.rendering.syntax.Syntax;
import org.xwiki.rendering.syntax.SyntaxFactory;
import org.xwiki.rendering.transformation.Transformation;

import java.io.*;

/**
 * Wiki Text Renderer using XWiki Rendering Engine.
 *
 * @author Masatoshi Hayashi
 */
public class XWikiRenderer {

    private XWikiConfigurationProvider configurationProvider;

    private SyntaxFactory syntaxFactory;

    private XWikiParserFactory parserFactory;

    private XDOMBuilder xdomBuilder = new XDOMBuilder();

    private XDOMTransformationManager xdomTransformationManager;

    private XDOMWriter xdomWriter;

    /**
     * Renders a source text using XWiki Rendering Engine.
     *
     * @param source source text reader
     * @param writer output writer
     * @param inputSyntax inputSyntax
     * @param outputSyntax outputSyntax
     * @param transformations transform parameters
     */
    public void render(Reader source, Writer writer,
                         CharSequence inputSyntax, CharSequence outputSyntax, Transformation ...transformations) {
        Syntax inputSyntaxObj = getSyntax(inputSyntax);
        Syntax outputSyntaxObj = getSyntax(outputSyntax);

        Parser parser = parserFactory.getParser(inputSyntaxObj);
        XDOM xdom = xdomBuilder.build(source, parser);
        xdomTransformationManager.transform(xdom, parser, transformations);
        xdomWriter.write(xdom, outputSyntaxObj, writer);
    }

    /**
     * Renders a source text using XWiki rendering engine and default syntax.
     *
     * @param source source text reader
     * @param writer output writer
     * @param transformations transform parameters
     */
    public void render(Reader source, Writer writer, Transformation ...transformations) {
        String inputSyntax = configurationProvider.getDefaultInputSyntax();
        String outputSyntax = configurationProvider.getDefaultOutputSyntax();
        render(source, writer, inputSyntax, outputSyntax, transformations);
    }

    /**
     * Renders a source text using XWiki rendering engine and default syntax.
     *
     * @param source source text reader
     * @param writer output writer
     * @param inputSyntax inputSyntax
     * @param outputSyntax outputSyntax
     * @param transformations transform parameters
     */
    public void render(
            CharSequence source, Writer writer,
            CharSequence inputSyntax, CharSequence outputSyntax, Transformation ...transformations) {
        render(new StringReader(source.toString()), writer, inputSyntax, outputSyntax, transformations);
    }

    /**
     * Renders a source text using XWiki rendering engine and default syntax.
     *
     * @param source source text reader
     * @param writer output writer
     * @param transformations transform parameters
     */
    public void render(CharSequence source, Writer writer, Transformation...transformations) {
        render(new StringReader(source.toString()), writer, transformations);
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
    public String render(Reader source, String inputSyntax, String outputSyntax, Transformation ...transformations) {
        Writer writer = new StringWriter();
        render(source, writer, inputSyntax, outputSyntax, transformations);
        return writer.toString();
    }

    /**
     * Returns rendered text from XWiki Rendering using default input syntax and default output syntax.
     *
     * @param source a source text
     * @param transformations transform parameters
     * @return a rendered result
     */
    public String render(Reader source, Transformation ...transformations) {
        String inputSyntax = configurationProvider.getDefaultInputSyntax();
        String outputSyntax = configurationProvider.getDefaultOutputSyntax();
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
    public String render(CharSequence source,
                          String inputSyntax, String outputSyntax, Transformation ...transformations) {
        return render(new StringReader(source.toString()), inputSyntax, outputSyntax, transformations);
    }

    /**
     * Returns rendered text from XWiki Rendering using default input syntax and default output syntax.
     *
     * @param source a source text
     * @param transformations transform parameters
     * @return a rendered result
     */
    public String render(CharSequence source, Transformation ...transformations) {
        String inputSyntax = configurationProvider.getDefaultInputSyntax();
        String outputSyntax = configurationProvider.getDefaultOutputSyntax();
        return render(source, inputSyntax, outputSyntax, transformations);
    }

    void setTransformationManager(XDOMTransformationManager transformationManager) {
        this.xdomTransformationManager = transformationManager;
    }

    void setWriter(XDOMWriter writer) {
        this.xdomWriter = writer;
    }

    void setSyntaxFactory(SyntaxFactory syntaxFactory) {
        this.syntaxFactory = syntaxFactory;
    }

    void setParserFactory(XWikiParserFactory parserFactory) {
        this.parserFactory = parserFactory;
    }

    void setConfigurationProvider(XWikiConfigurationProvider configurationProvider) {
        this.configurationProvider = configurationProvider;
    }

    private Syntax getSyntax(Object id) {
        try {
            return syntaxFactory.createSyntaxFromIdString(id.toString());
        } catch (ParseException e) {
            throw new IllegalArgumentException("Invalid Syntax ID: " + id, e);
        }
    }

}
