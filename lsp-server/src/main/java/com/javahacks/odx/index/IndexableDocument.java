package com.javahacks.odx.index;

import com.javahacks.odx.model.*;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlTransient;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@XmlAccessorType(value = XmlAccessType.FIELD)
public abstract class IndexableDocument extends AbstractLinkTarget {
    @XmlTransient
    private final Map<String, Object> idToObjectMap = new HashMap<>();
    @XmlTransient
    protected DocumentIndex index;

    public abstract String getSHORTNAME();

    void index(final String id, final Object object) {
        idToObjectMap.put(id, object);
    }

    Object getElementById(final String id) {
        return idToObjectMap.get(id);
    }

    Object getElementByIdInLayers(final String id, final List<Layer> layers) {
        for (final Layer layer : layers) {
            return layer.getElementById(id);
        }
        return null;
    }

    public final String getDocType() {
        if (this instanceof DIAGLAYER) {
            return DOCTYPE.LAYER.name();
        }
        if (this instanceof DIAGLAYERCONTAINER) {
            return DOCTYPE.CONTAINER.name();
        }
        if (this instanceof COMPARAMSPEC) {
            return DOCTYPE.COMPARAM_SPEC.name();
        }
        if (this instanceof COMPARAMSUBSET) {
            return DOCTYPE.COMPARAM_SUBSET.name();
        }
        if (this instanceof VEHICLEINFOSPEC) {
            return DOCTYPE.VEHICLE_INFO_SPEC.name();
        }
        if (this instanceof FLASH) {
            return DOCTYPE.FLASH.name();
        }
        if (this instanceof ECUCONFIG) {
            return DOCTYPE.ECU_CONFIG.name();
        }
        if (this instanceof MULTIPLEECUJOBSPEC) {
            return DOCTYPE.MULTIPLE_ECU_JOB_SPEC.name();
        }
        if (this instanceof FUNCTIONDICTIONARY) {
            return DOCTYPE.FUNCTION_DICTIONARY_SPEC.name();
        }
        throw new IllegalArgumentException("Doctype for class unknown: " + getClass());
    }

    public DocumentIndex getIndex() {
        return index;
    }

    public void setIndex(final DocumentIndex index) {
        this.index = index;
    }

    public boolean isExpandable() {
        return false;
    }
}
