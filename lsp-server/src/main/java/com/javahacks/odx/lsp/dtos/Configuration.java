package com.javahacks.odx.lsp.dtos;

import com.google.gson.JsonObject;


public class Configuration {
    private String activeIndexLocation;
    private String maxHeapSpace;

    public static Configuration fromJsonObject(JsonObject object) {
        Configuration configuration = new Configuration();
        configuration.setActiveIndexLocation(object.get("activeIndexLocation").getAsString());
        configuration.setMaxHeapSpace(object.get("maxHeapSpace").getAsString());
        return configuration;
    }


    public String getActiveIndexLocation() {
        return activeIndexLocation;
    }

    public void setActiveIndexLocation(String activeIndexLocation) {
        this.activeIndexLocation = activeIndexLocation;
    }

    public String getMaxHeapSpace() {
        return maxHeapSpace;
    }

    public void setMaxHeapSpace(String maxHeapSpace) {
        this.maxHeapSpace = maxHeapSpace;
    }
}
