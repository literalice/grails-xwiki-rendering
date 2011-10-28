package com.monochromeroad.grails.plugins.xwiki;

import org.xwiki.rendering.block.XDOM;
import org.xwiki.rendering.renderer.BlockRenderer;
import org.xwiki.rendering.renderer.printer.WikiPrinter;
import org.xwiki.rendering.syntax.Syntax;

import java.io.IOException;
import java.io.Writer;

/**
 * XDOM Writer Class
 *
 * @author Masatoshi Hayashi
 */
class XDOMWriter {

    private XWikiComponentRepository componentRepository;

    XDOMWriter(XWikiComponentRepository componentRepository) {
        this.componentRepository = componentRepository;
    }

    /**
     * Writes the XDOM Object using the specified writer.
     *
     * @param xdom the written xdom
     * @param outputSyntax the output format
     * @param writer the writer used to write the xdom
     */
    void write(XDOM xdom, Syntax outputSyntax, Writer writer) {
        BlockRenderer renderer = componentRepository.lookupComponent(
                BlockRenderer.class, outputSyntax.toIdString());

        WikiPrinter printer = new RedirectPrinter(writer);
        renderer.render(xdom, printer);
    }

    private static class RedirectPrinter implements WikiPrinter {

        private Writer writer;

        RedirectPrinter(Writer writer) {
            this.writer = writer;
        }

        public void print(String s) {
            try {
                writer.append(s);
            } catch (IOException e) {
                throw new IllegalStateException(e);
            }
        }

        public void println(String s) {
            try {
                writer.append(s);
                writer.append("\n");
            } catch (IOException e) {
                throw new IllegalStateException(e);
            }
        }
    }
}
