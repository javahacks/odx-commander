package com.javahacks.odx.index;

import com.javahacks.odx.index.LocationAware.AbstractLocationAware;
import com.javahacks.odx.model.ODXLINK;
import com.javahacks.odx.model.SNREF;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlTransient;
import java.util.Optional;

/**
 * Base class for all links contained in a document.
 */
@XmlAccessorType(XmlAccessType.NONE)
public abstract class AbstractLink extends AbstractLocationAware {

    private IndexableDocument document;

    /**
     * Sets a reference to the {@link IndexableDocument} in which this link is contained
     */
    public void setDocument(final IndexableDocument document) {
        this.document = document;
    }

    /**
     * Returns a reference to the {@link IndexableDocument} in which this link is contained
     */
    public IndexableDocument getDocument() {
        return document;
    }



}
