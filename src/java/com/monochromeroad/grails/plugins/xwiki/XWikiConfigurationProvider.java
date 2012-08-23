package com.monochromeroad.grails.plugins.xwiki;

import org.xwiki.rendering.syntax.Syntax;

import java.util.ArrayList;
import java.util.List;

public class XWikiConfigurationProvider {

    // TODO import from a configuration file
    // private Map<String, String> config = new HashMap<String, String>();

    public Syntax getDefaultInputSyntax() {
        return Syntax.XWIKI_2_1;
    }

    public Syntax getDefaultOutputSyntax() {
        return Syntax.XHTML_1_0;
    }

    public List<String> getDefaultTransformations() {
        List<String> transformations = new ArrayList<String>();
        transformations.add("macro");
        return transformations;
    }
}
