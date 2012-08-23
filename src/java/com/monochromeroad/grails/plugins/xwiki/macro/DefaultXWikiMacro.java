package com.monochromeroad.grails.plugins.xwiki.macro;

import org.xwiki.properties.BeanManager;
import org.xwiki.rendering.macro.AbstractMacro;

/**
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
