package com.javahacks.odx.lsp.dtos;

import com.google.gson.annotations.SerializedName;

import javax.xml.bind.annotation.XmlAttribute;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents arbitrary elements contained in a document {@link com.javahacks.odx.model.DIAGLAYER} or {@link com.javahacks.odx.model.ODXCATEGORY}.
 * <p>
 * Each diagnostic object has a location within the its document, a name, unique type and optional list of child elements.
 */

public class DiagnosticElement {
    @SerializedName("children")
    private List<DiagnosticElement> children = new ArrayList<>();
    @SerializedName("label")
    @XmlAttribute(required=false)
    private String label;
    @SerializedName("name")
    private String name;
    @SerializedName("type")
    private String type;
    @SerializedName("location")
    private StartTagLocation location;
    @SerializedName("revealable")
    private boolean revealable;

    public List<DiagnosticElement> getChildren() {
        return children;
    }

    public void setChildren(List<DiagnosticElement> children) {
        this.children = children;
    }

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

    public boolean isRevealable() {
        return revealable;
    }

    public void setRevealable(boolean revealable) {
        this.revealable = revealable;
    }

    public StartTagLocation getLocation() {
        return location;
    }

    public void setLocation(StartTagLocation location) {
        this.location = location;
    }
}
