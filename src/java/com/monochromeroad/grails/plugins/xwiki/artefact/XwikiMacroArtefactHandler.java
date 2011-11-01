package com.monochromeroad.grails.plugins.xwiki.artefact;

import com.monochromeroad.grails.plugins.xwiki.macro.GrailsMacro;
import org.codehaus.groovy.grails.commons.ArtefactHandlerAdapter;

public class XwikiMacroArtefactHandler extends ArtefactHandlerAdapter {

    public static final String TYPE = "XwikiMacro";

    public XwikiMacroArtefactHandler() {
        super(TYPE, GrailsXwikiMacroClass.class, DefaultGrailsXwikiMacroClass.class, null);
    }

    public boolean isArtefactClass(Class clazz) {
        return clazz != null && GrailsMacro.class.isAssignableFrom(clazz);
    }

}
