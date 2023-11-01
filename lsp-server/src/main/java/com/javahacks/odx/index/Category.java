package com.javahacks.odx.index;

import com.javahacks.odx.model.ODX;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import java.util.ArrayList;
import java.util.List;

/**
 * Used to maintain additional information about a single {@link com.javahacks.odx.model.ODXCATEGORY} instance
 */
@XmlAccessorType(XmlAccessType.NONE)
public abstract class Category extends IndexableDocument {
    private final List<Layer> layers = new ArrayList<>();
    private final List<LocationAware> locationAwares = new ArrayList<>();
    private ODX parent;

    void addLayer(final Layer layer) {
        layers.add(layer);
    }

    void addLocationAware(final LocationAware locationAware) {
        locationAwares.add(locationAware);
    }

    public List<Layer> getLayers() {
        return layers;
    }

    public List<LocationAware> getLocationAwares() {
        return locationAwares;
    }

    @Override
    Object getElementById(final String id) {
        return super.getElementById(id);        
    }

    public ODX getParent() {
        return parent;
    }

    public void setParent(final ODX parent) {
        this.parent = parent;
    }
}
