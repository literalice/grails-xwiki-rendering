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

    private XWikiConfigurationProvider configurationProvider;

    private XDOMBuilder xdomBuilder;

    private List<XDOMTransformation> transformations = new LinkedList<XDOMTransformation> ();

    private XDOMWriter xdomWriter;

    public XWikiRenderer(ComponentManager componentManager, XWikiConfigurationProvider xwikiConfigurationProvider) {
        this.componentRepository = new XWikiComponentRepository(componentManager);
        this.configurationProvider = xwikiConfigurationProvider;
        this.xdomBuilder = new XDOMBuilder();
        this.transformations.add(new DefaultXDOMTransformation(componentManager));
        this.xdomWriter = new XDOMWriter(componentManager);
    }

    /**
     * Renders a source text using XWiki Rendering Engine.
     *
     * @param source source text reader
     * @param writer output writer
     * @param parameters [input syntax, output syntax, ...transform parameters]
     */
    public void render(Reader source, Writer writer, Object ...parameters) {
        if (parameters.length < 2) {
            throw new IllegalArgumentException("the input/output sytaxes must be passed.");
        }

        SyntaxFactory syntaxFactory = getSyntaxFactory();
        Syntax inputSyntaxObj = getSyntax(syntaxFactory, parameters[0]);
        Syntax outputSyntaxObj = getSyntax(syntaxFactory, parameters[1]);

        Parser parser = getParser(inputSyntaxObj);
        XDOM xdom = xdomBuilder.build(source, parser);
        for (XDOMTransformation transformation : transformations) {
            transformation.transform(xdom, parser, parameters);
        }
        xdomWriter.write(xdom, outputSyntaxObj, writer);
    }

    /**
     * Renders a source text using XWiki rendering engine and default syntax.
     *
     * @param source source text reader
     * @param writer output writer
     */
    public void render(Reader source, Writer writer) {
        render(source, writer,
                configurationProvider.getDefaultInputSyntax(),
                configurationProvider.getDefaultOutputSyntax());
    }

    /**
     * @see #render(java.io.Reader, java.io.Writer, Object[]))
     */
    public void render(CharSequence source, Writer writer, Object...parameters) {
        render(new StringReader(source.toString()), writer, parameters);
    }

    /**
     * @see #render(java.io.Reader, java.io.Writer)
     */
    public void render(CharSequence source, Writer writer) {
        render(source, writer,
                configurationProvider.getDefaultInputSyntax(),
                configurationProvider.getDefaultOutputSyntax());
    }

    /**
     * Returns rendered text from XWiki Rendering.
     *
     * @param source a source text
     * @param parameters [input syntax, output syntax, and transform parameters...]
     * @return a rendered result
     */
    public String render(Reader source, Object ...parameters) {
        Writer writer = new StringWriter();
        render(source, writer, parameters);
        return writer.toString();
    }

    /**
     * Returns rendered text from XWiki Rendering using default input syntax and default output syntax.
     *
     * @param source a source text
     * @return a rendered result
     */
    public String render(Reader source) {
        return render(source,
                configurationProvider.getDefaultInputSyntax(),
                configurationProvider.getDefaultOutputSyntax());
    }

    /**
     * @see #render(java.io.Reader, Object[])
     */
    public String render(CharSequence source, Object ...parameters) {
        return render(new StringReader(source.toString()), parameters);
    }

    /**
     * @see #render(java.io.Reader)
     */
    public String render(CharSequence source) {
        return render(source,
                configurationProvider.getDefaultInputSyntax(),
                configurationProvider.getDefaultOutputSyntax());
    }

    /**
     * Adds a transformation
     *
     * @param transformation transformation
     */
    public void addTransformer(XDOMTransformation transformation) {
        this.transformations.add(transformation);
    }

    /**
     * Adds a transformation
     *
     * @param index the index of the transformation
     * @param transformation transformation
     */
    public void addTransformation(int index, XDOMTransformation transformation) {
        this.transformations.add(index, transformation);
    }

    private Parser getParser(Syntax syntax) {
        return componentRepository.
                lookupComponent(Parser.class, syntax.toIdString());
    }

    private Syntax getSyntax(SyntaxFactory factory, Object id) {
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
