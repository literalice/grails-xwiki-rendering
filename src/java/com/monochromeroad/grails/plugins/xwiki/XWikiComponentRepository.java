package com.monochromeroad.grails.plugins.xwiki;

import org.xwiki.component.manager.ComponentLookupException;
import org.xwiki.component.manager.ComponentManager;

public class XWikiComponentRepository {

    private final ComponentManager componentManager;

    public XWikiComponentRepository(ComponentManager componentManager) {
        this.componentManager = componentManager;
    }

    public <T> T lookupComponent(Class<T> componentClass) {
        try {
            return componentManager.getInstance(componentClass);
        } catch (ComponentLookupException e) {
            throw new IllegalStateException(e);
        }
    }

    public <T> T lookupComponent(Class<T> componentClass, String parameter) {
        try {
            return componentManager.getInstance(componentClass, parameter);
        } catch (ComponentLookupException e) {
            throw new IllegalStateException(e);
        }
    }
}
