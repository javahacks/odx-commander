package com.javahacks.odx.index;

import com.javahacks.odx.lsp.dtos.StartTagLocation;

import javax.xml.bind.annotation.*;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

/**
 * An index used for lazy loading {@link com.javahacks.odx.model.ODXCATEGORY} files.
 */
@XmlRootElement(name = "odx-index")
@XmlAccessorType(XmlAccessType.FIELD)
public class ProxyIndex {
    public static final int INDEX_VERSION = 7;

    @XmlElement(name = "indexVersion")
    private int indexVersion;
    @XmlElement(name = "categories")
    private List<CategoryProxy> categories = new ArrayList<>();
    @XmlElement(name = "packed")
    private boolean packed;

    @XmlTransient
    private Path odxSourcePath;
    @XmlTransient
    private Path indexPath;

    public ProxyIndex() {
        //JAXB
    }

    public ProxyIndex(final boolean packed) {
        this.indexVersion = INDEX_VERSION;
        this.packed = packed;
    }

    public List<CategoryProxy> getCategories() {
        return categories;
    }

    public void setCategories(final List<CategoryProxy> categories) {
        this.categories = categories;
    }

    public int getIndexVersion() {
        return indexVersion;
    }

    public void setIndexVersion(final int indexVersion) {
        this.indexVersion = indexVersion;
    }

    public boolean isPacked() {
        return packed;
    }

    public void setPacked(final boolean packed) {
        this.packed = packed;
    }

    public Path getOdxSourcePath() {
        return odxSourcePath;
    }

    public void setOdxSourcePath(final Path odxSourcePath) {
        this.odxSourcePath = odxSourcePath;
    }

    public Path getIndexPath() {
        return indexPath;
    }

    public void setIndexPath(final Path indexPath) {
        this.indexPath = indexPath;
    }
}

@XmlAccessorType(XmlAccessType.FIELD)
abstract class AbstractDocumentProxy extends ProxyRef {
    @XmlElement(name = "expandable")
    private boolean expandable;
    @XmlElement(name = "location")
    private StartTagLocation location;

    public boolean isExpandable() {
        return expandable;
    }

    public void setExpandable(final boolean expandable) {
        this.expandable = expandable;
    }

    public StartTagLocation getLocation() {
        return location;
    }

    public void setLocation(final StartTagLocation location) {
        this.location = location;
    }

}



