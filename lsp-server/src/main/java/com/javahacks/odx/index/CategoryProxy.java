package com.javahacks.odx.index;

import org.eclipse.persistence.oxm.annotations.XmlPath;

import javax.xml.bind.annotation.*;
import java.lang.ref.SoftReference;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

/**
 * Keeps stripped index information for ODX categories.
 */
@XmlRootElement(name = "odx-category")
@XmlAccessorType(XmlAccessType.FIELD)
public class CategoryProxy extends AbstractDocumentProxy {
    @XmlPath("layers/layer")
    private final List<LayerProxy> layers = new ArrayList<>();
    @XmlElement(name = "indexPath")
    private URI indexPath;
    @XmlElement(name = "lastModification")
    private long lastModification;
    @XmlTransient
    private SoftReference<Category> proxyTarget;


    public CategoryProxy() {
        //default constructor
    }

    public CategoryProxy(final URI indexPath, final long lastModification) {
        setIndexPath(indexPath);
        setLastModification(lastModification);
    }


    public SoftReference<Category> getProxyTarget() {
        return proxyTarget;
    }

    public void setProxyTarget(final SoftReference<Category> proxyTarget) {
        this.proxyTarget = proxyTarget;
    }

    public URI getIndexPath() {
        return indexPath;
    }

    public void setIndexPath(final URI indexPath) {
        this.indexPath = indexPath;
    }

    public long getLastModification() {
        return lastModification;
    }

    public void setLastModification(final long lastModification) {
        this.lastModification = lastModification;
    }

    public List<LayerProxy> getLayers() {
        return layers;
    }

}
