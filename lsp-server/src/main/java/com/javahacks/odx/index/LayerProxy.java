package com.javahacks.odx.index;

import javax.xml.bind.annotation.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Keeps stripped index information for layers.
 */
@XmlRootElement(name = "layer")
@XmlAccessorType(XmlAccessType.FIELD)
public class LayerProxy extends AbstractDocumentProxy {

    @XmlElement(name = "layerType")
    private String layerType;
    @XmlElement(name = "children/link")
    private final List<ProxyRef> children = new ArrayList<>();
    @XmlElement(name = "parents/link")
    private final List<ProxyRef> parents = new ArrayList<>();

    public String getLayerType() {
        return layerType;
    }
    public void setLayerType(final String layerType) {
        this.layerType = layerType;
    }

    public List<ProxyRef> getChildren() {
        return children;
    }

    public List<ProxyRef> getParents() {
        return parents;
    }
}
