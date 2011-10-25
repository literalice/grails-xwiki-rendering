package com.monochromeroad.grails.plugins.xwiki;

import org.xwiki.rendering.syntax.Syntax;

import java.util.HashMap;
import java.util.Map;

public class XWikiConfigurationProvider {

    private Map<String, String> config = new HashMap<String, String>();

    public String getDefaultInputSyntax() {
        return Syntax.XWIKI_2_1.toIdString();
    }

    public String getDefaultOutputSyntax() {
        return Syntax.XHTML_1_0.toIdString();
    }
}
