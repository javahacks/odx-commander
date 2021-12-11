package com.javahacks.odx.lsp.dtos;

import com.google.gson.annotations.SerializedName;
import com.javahacks.odx.model.DIAGSERVICE;

/**
 * Represents a single {@link DIAGSERVICE} element.
 */
public class Service {
    @SerializedName("label")
    private String label;
    @SerializedName("name")
    private String name;
    @SerializedName("type")
    private String type;
    @SerializedName("layerRef")
    private Link layerRef;
    @SerializedName("location")
    private StartTagLocation location;

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Link getLayerRef() {
        return layerRef;
    }

    public void setLayerRef(Link layerRef) {
        this.layerRef = layerRef;
    }

    public StartTagLocation getLocation() {
        return location;
    }

    public void setLocation(StartTagLocation location) {
        this.location = location;
    }
}
