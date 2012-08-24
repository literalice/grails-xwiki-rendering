package com.monochromeroad.grails.plugins.xwiki.macro;

import org.xwiki.properties.BeanManager;
import org.xwiki.rendering.macro.AbstractMacro;

/**
 * XWiki macro takes some parameters (e.g. {{code for="java" /}}) to use in Grails.
 *
 * <p>You need to create a constructor that takes no arguments.</p>
 *
 * @author Masatoshi Hayashi
 * @param <P> Macro Parameters Class
 */
public abstract class DefaultXWikiMacro<P> extends AbstractMacro<P> {

    private final String macroName;

    public DefaultXWikiMacro(String name, Class<P> parametersBeanClass) {
        super(name, "XWiki Macro: " + name, parametersBeanClass);
        this.macroName = name;
    }

    public String getMacroName() {
        return macroName;
    }

    public void setBeanManager(BeanManager beanManager) {
        this.beanManager = beanManager;
    }
}
