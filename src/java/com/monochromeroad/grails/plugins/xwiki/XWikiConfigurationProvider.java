package com.monochromeroad.grails.plugins.xwiki;

import org.xwiki.rendering.syntax.Syntax;

public class XWikiConfigurationProvider {

    private Boolean macrosEnabled = true;

    public Syntax getDefaultInputSyntax() {
        return Syntax.XWIKI_2_1;
    }

    public Syntax getDefaultOutputSyntax() {
        return Syntax.XHTML_1_0;
    }

    public Boolean isMacrosEnabled() {
        return macrosEnabled;
    }

    public void setMacrosEnabled(Boolean macrosEnabled) {
        this.macrosEnabled = macrosEnabled;
    }

}

