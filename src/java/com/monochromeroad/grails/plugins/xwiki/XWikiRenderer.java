package com.monochromeroad.grails.plugins.xwiki;

import org.xwiki.component.manager.ComponentManager;
import org.xwiki.rendering.block.XDOM;
import org.xwiki.rendering.parser.ParseException;
import org.xwiki.rendering.parser.Parser;
import org.xwiki.rendering.syntax.Syntax;
import org.xwiki.rendering.syntax.SyntaxFactory;

import java.io.*;
import java.util.LinkedList;
import java.util.List;

/**
 * Wiki Text Renderer using XWiki Rendering Engine.
 *
 * @author Masatoshi Hayashi
 */
public class XWikiRenderer {

    private XWikiComponentRepository componentRepository;

    private XDOMBuilder xdomBuilder;

    private List<XDOMTransformer> transformers = new LinkedList<XDOMTransformer> ();

    private XDOMWriter xdomWriter;

    private String defaultInputSyntax = Syntax.XWIKI_2_1.toIdString();

    private String defaultOutputSyntax = Syntax.XHTML_1_0.toIdString();

    public XWikiRenderer(ComponentManager componentManager) {
        this.componentRepository = new XWikiComponentRepository(componentManager);
        this.xdomBuilder = new XDOMBuilder();
        this.transformers.add(new DefaultXDOMTransformer(componentManager));
        this.xdomWriter = new XDOMWriter(componentManager);
    }

    /**
     * Renders a source text using XWiki Rendering Engine.
     *
     * @param source source text reader
     * @param writer output writer
     * @param inputSyntax input syntax
     * @param outputSyntax output syntax
     */
    public void render(Reader source, Writer writer, CharSequence inputSyntax, CharSequence outputSyntax) {
        SyntaxFactory syntaxFactory = getSyntaxFactory();
        Syntax inputSyntaxObj = getSyntax(syntaxFactory, inputSyntax);
        Syntax outputSyntaxObj = getSyntax(syntaxFactory, outputSyntax);

        Parser parser = getParser(inputSyntaxObj);
        XDOM xdom = xdomBuilder.build(source, parser);
        for (XDOMTransformer transformer:transformers) {
            transformer.transform(xdom, parser);
        }
        xdomWriter.write(xdom, outputSyntaxObj, writer);
    }

    /**
     * Renders a source text using XWiki rendering engine and default input syntax.
     *
     * @param source source text reader
     * @param writer output writer
     * @param outputSyntax output syntax
     */
    public void render(Reader source, Writer writer, CharSequence outputSyntax) {
        render(source, writer, defaultInputSyntax, outputSyntax);
    }

    /**
     * Renders a source text using XWiki rendering engine and default input syntax and output syntax.
     *
     * @param source source text reader
     * @param writer output writer
     */
    public void render(Reader source, Writer writer) {
        render(source, writer, defaultInputSyntax, defaultOutputSyntax);
    }

    /**
     * @see #render(java.io.Reader, java.io.Writer, CharSequence, CharSequence)
     */
    public void render(CharSequence source, Writer writer, CharSequence inputSyntax, CharSequence outputSyntax) {
        render(new StringReader(source.toString()), writer, inputSyntax, outputSyntax);
    }

    /**
     * @see #render(java.io.Reader, java.io.Writer, CharSequence)
     */
    public void render(CharSequence source, Writer writer, CharSequence outputSyntax) {
        render(source, writer, defaultInputSyntax, outputSyntax);
    }

    /**
     * @see #render(java.io.Reader, java.io.Writer)
     */
    public void render(CharSequence source, Writer writer) {
        render(source, writer, defaultOutputSyntax);
    }

    /**
     * Returns rendered text from XWiki Rendering.
     *
     * @param source a source text
     * @param inputSyntax input syntax
     * @param outputSyntax output syntax
     * @return a rendered result
     */
    public String render(Reader source, CharSequence inputSyntax, CharSequence outputSyntax) {
        Writer writer = new StringWriter();
        render(source, writer, inputSyntax, outputSyntax);
        return writer.toString();
    }

    /**
     * Returns rendered text from XWiki Rendering using default input syntax.
     *
     * @param source a source text
     * @param outputSyntax output syntax
     * @return a rendered result
     */
    public String render(Reader source, CharSequence outputSyntax) {
        return render(source, defaultInputSyntax, outputSyntax);
    }

    /**
     * Returns rendered text from XWiki Rendering using default input syntax and default output syntax.
     *
     * @param source a source text
     * @return a rendered result
     */
    public String render(Reader source) {
        return render(source, defaultOutputSyntax);
    }

    /**
     * @see #render(java.io.Reader, CharSequence, CharSequence)
     */
    public String render(CharSequence source, CharSequence inputSyntax, CharSequence outputSyntax) {
        return render(new StringReader(source.toString()), inputSyntax, outputSyntax);
    }

    /**
     * @see #render(java.io.Reader, CharSequence)
     */
    public String render(CharSequence source, CharSequence outputSyntax) {
        return render(source, defaultInputSyntax, outputSyntax);
    }

    /**
     * @see #render(java.io.Reader)
     */
    public String render(CharSequence source) {
        return render(source, defaultOutputSyntax);
    }

    /**
     * Adds a transformer
     *
     * @param transformer transformer
     */
    public void addTransformer(XDOMTransformer transformer) {
        this.transformers.add(transformer);
    }

    /**
     * Adds a transformer
     *
     * @param index the index of the transformer
     * @param transformer transformer
     */
    public void addTransformer(int index, XDOMTransformer transformer) {
        this.transformers.add(index, transformer);
    }

    private Parser getParser(Syntax syntax) {
        return componentRepository.
                lookupComponent(Parser.class, syntax.toIdString());
    }

    private Syntax getSyntax(SyntaxFactory factory, CharSequence id) {
        try {
            return factory.createSyntaxFromIdString(id.toString());
        } catch (ParseException e) {
            throw new IllegalArgumentException("Invalid Syntax ID: " + id, e);
        }
    }

    private SyntaxFactory getSyntaxFactory() {
        return componentRepository.
                lookupComponent(SyntaxFactory.class);
    }
}
