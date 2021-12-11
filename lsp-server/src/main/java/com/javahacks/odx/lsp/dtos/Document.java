package com.javahacks.odx.lsp.dtos;

import com.google.gson.annotations.SerializedName;

public class Document {
    @SerializedName("location")
    private StartTagLocation location;
    @SerializedName("odxLink")
    private Link odxLink;
    @SerializedName("name")
    private String name;
    @SerializedName("expandable")
    private boolean expandable;

    public StartTagLocation getLocation() {
        return location;
    }

    public void setLocation(StartTagLocation location) {
        this.location = location;
    }

    public Link getOdxLink() {
        return odxLink;
    }

    public void setOdxLink(Link odxLink) {
        this.odxLink = odxLink;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isExpandable() {
        return expandable;
    }

    public void setExpandable(boolean expandable) {
        this.expandable = expandable;
    }
}
