package com.monochromeroad.grails.plugins.xwiki;

import org.xwiki.rendering.block.XDOM;
import org.xwiki.rendering.parser.Parser;
import org.xwiki.rendering.transformation.Transformation;
import org.xwiki.rendering.transformation.TransformationContext;
import org.xwiki.rendering.transformation.TransformationException;

import java.util.Collections;
import java.util.List;
import java.util.NavigableSet;
import java.util.TreeSet;

class XDOMTransformationManager {

    XWikiComponentRepository componentRepository;

    XWikiConfigurationProvider configurationProvider;

    XDOMTransformationManager(
            XWikiComponentRepository componentRepository, XWikiConfigurationProvider configurationProvider) {
        this.componentRepository = componentRepository;
        this.configurationProvider = configurationProvider;
    }

    XDOM transform(XDOM xdom, Parser parser, Transformation ...transformations) {
        TransformationContext context = new TransformationContext(xdom, parser.getSyntax());
        NavigableSet<Transformation> transformationList = getTransformationList(transformations);

        try {
            for(Transformation transformation : transformationList) {
                transformation.transform(xdom, context);
            }
        } catch (TransformationException e) {
            throw new IllegalStateException(e);
        }

        return xdom;
    }

    private NavigableSet<Transformation> getTransformationList(Transformation ...transformations) {
        List<String> defaultTransformations = configurationProvider.getDefaultTransformations();
        NavigableSet<Transformation> transformationList = new TreeSet<Transformation>();
        for(String defaultTransformation : defaultTransformations) {
            Transformation transform =
                    componentRepository.lookupComponent(Transformation.class, defaultTransformation);
            transformationList.add(transform);
        }
        Collections.addAll(transformationList, transformations);
        return transformationList;
    }

}
