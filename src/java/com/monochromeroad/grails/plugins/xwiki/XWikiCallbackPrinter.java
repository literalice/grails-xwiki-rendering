package com.monochromeroad.grails.plugins.xwiki;

import groovy.lang.Closure;
import org.xwiki.rendering.renderer.printer.WikiPrinter;

/**
 * @author Masatoshi Hayashi
 */
public class XWikiCallbackPrinter implements WikiPrinter {

    private Closure callback;

    public XWikiCallbackPrinter(Closure callback) {
        this.callback = callback;
    }

    public void print(String s) {
        callback.call(s);
    }

    public void println(String s) {
        callback.call(s + "\n");
    }
}

