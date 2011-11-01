package com.monochromeroad.grails.plugins.xwiki.artefact;

import org.codehaus.groovy.grails.commons.AbstractInjectableGrailsClass;

public class DefaultGrailsXwikiMacroClass
            extends AbstractInjectableGrailsClass
                        implements GrailsXwikiMacroClass {

    public DefaultGrailsXwikiMacroClass(Class<?> clazz) {
        super(clazz, XwikiMacroArtefactHandler.TYPE);
    }
}
