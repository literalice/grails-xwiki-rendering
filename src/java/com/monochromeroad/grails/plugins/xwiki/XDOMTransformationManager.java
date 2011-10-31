package com.monochromeroad.grails.plugins.xwiki;

import org.xwiki.rendering.block.XDOM;
import org.xwiki.rendering.parser.Parser;
import org.xwiki.rendering.transformation.Transformation;
import org.xwiki.rendering.transformation.TransformationContext;
import org.xwiki.rendering.transformation.TransformationException;

import java.util.Collections;
import java.util.NavigableSet;
import java.util.TreeSet;

class XDOMTransformationManager {

    private NavigableSet<Transformation> defaultTransformations = new TreeSet<Transformation>();

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

    void addDefaultTransformation(Transformation transformation) {
        defaultTransformations.add(transformation);
    }

    private NavigableSet<Transformation> getTransformationList(Transformation ...transformations) {
        NavigableSet<Transformation> transformationList =
                new TreeSet<Transformation>(defaultTransformations);
        Collections.addAll(transformationList, transformations);
        return transformationList;
    }

}
