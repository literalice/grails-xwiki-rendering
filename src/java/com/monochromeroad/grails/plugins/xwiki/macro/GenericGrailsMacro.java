package com.monochromeroad.grails.plugins.xwiki.macro;

import org.apache.commons.lang.StringUtils;
import org.xwiki.component.descriptor.ComponentDescriptor;
import org.xwiki.component.descriptor.DefaultComponentDescriptor;
import org.xwiki.component.phase.InitializationException;
import org.xwiki.properties.BeanManager;
import org.xwiki.rendering.block.Block;
import org.xwiki.rendering.macro.AbstractMacro;
import org.xwiki.rendering.macro.Macro;
import org.xwiki.rendering.macro.MacroExecutionException;
import org.xwiki.rendering.transformation.MacroTransformationContext;

import java.util.List;
import java.util.Map;

/**
 * XWiki Macro Runner in a Grails System
 */
public class GenericGrailsMacro extends AbstractMacro<Object> {

    public static GenericGrailsMacro getInstance(Map<String, Object> config, BeanManager beanManager) {
        String macroName = (String)config.get("macroName");
        String macroFullName = "Grails Macro " + StringUtils.capitalize(macroName);
        GrailsMacro macroImpl = (GrailsMacro)config.get("macroImpl");
        Boolean inlineSupport = (Boolean)config.get("inlineSupport");
        Class parametersBeanClass = (Class)config.get("parametersBeanClass");
        GenericGrailsMacro macro =
                new GenericGrailsMacro(macroName, macroFullName,
                        macroImpl, beanManager, parametersBeanClass, inlineSupport);
        try {
            macro.initialize();
        } catch (InitializationException e) {
            throw new IllegalStateException(e);
        }

        return macro;
    }

    private GrailsMacro macroImpl;

    private String macroName;

    private Boolean inlineSupport = false;

    private GenericGrailsMacro(String macroName, String macroFullName , GrailsMacro macroImpl,
                               BeanManager beanManager, Class parametersBeanClass, Boolean inlineSupport) {
        super(macroFullName, macroImpl.toString(), parametersBeanClass);
        this.macroName = macroName;
        this.macroImpl = macroImpl;
        this.beanManager = beanManager;
        this.inlineSupport = inlineSupport;
    }

    public boolean supportsInlineMode() {
        return inlineSupport;
    }

    public List<Block> execute(Object parameters, String content, MacroTransformationContext context) throws MacroExecutionException {
        return macroImpl.execute(parameters, content, context);
    }

    public String getMacroName() {
        return macroName;
    }

    public ComponentDescriptor createDescriptor() {
        DefaultComponentDescriptor<Macro> macroDescriptor =
                        new DefaultComponentDescriptor<Macro>();
        macroDescriptor.setImplementation(GenericGrailsMacro.class);
        macroDescriptor.setRoleType(Macro.class);
        macroDescriptor.setRoleHint(this.macroName);
        return macroDescriptor;
    }
}
