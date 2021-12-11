package com.javahacks.odx.index;

import com.javahacks.odx.index.LocationAware.AbstractLocationAware;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;

/**
 * Base class for all ODX elements that are target of an {@link AbstractLink}
 */
@XmlAccessorType(XmlAccessType.NONE)
public abstract class AbstractLinkTarget extends AbstractLocationAware implements IdentifiableElement {
}

