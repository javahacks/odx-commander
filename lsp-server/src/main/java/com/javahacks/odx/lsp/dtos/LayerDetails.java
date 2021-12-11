package com.javahacks.odx.lsp.dtos;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;


public class LayerDetails {
    @SerializedName("services")
    private List<Service> services = new ArrayList<>();
    @SerializedName("variantPatterns")
    private List<DiagnosticElement> variantPatterns = new ArrayList<>();
    @SerializedName("dependencies")
    private List<DiagnosticElement> dependencies = new ArrayList<>();

    public List<Service> getServices() {
        return services;
    }

    public void setServices(List<Service> services) {
        this.services = services;
    }

    public List<DiagnosticElement> getVariantPatterns() {
        return variantPatterns;
    }

    public void setVariantPatterns(List<DiagnosticElement> variantPatterns) {
        this.variantPatterns = variantPatterns;
    }

    public List<DiagnosticElement> getDependencies() {
        return dependencies;
    }

    public void setDependencies(List<DiagnosticElement> dependencies) {
        this.dependencies = dependencies;
    }
}
