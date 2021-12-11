package com.javahacks.odx.index;

import com.javahacks.odx.model.DIAGLAYER;
import com.javahacks.odx.model.DIAGSERVICE;
import com.javahacks.odx.model.HIERARCHYELEMENT;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import java.util.ArrayList;
import java.util.List;

/**
 * {@link Layer} represent {@link DIAGLAYER} elements in the ODX inheritance.
 */
@XmlAccessorType(XmlAccessType.NONE)
public abstract class Layer extends IndexableDocument implements CategoryAware {
    private final List<Layer> parents = new ArrayList<>();
    private final List<Layer> imports = new ArrayList<>();
    private final List<Object> snRefElements = new ArrayList<>();
    private Category container;
    private LayerQuery query;

    public LayerQuery query() {
        if (query == null) {
            query = new LayerQuery(this);
        }
        return query;
    }


    public List<Layer> getImports() {
        return imports;
    }

    public List<Layer> getParents() {
        return parents;
    }

    @Override
    Object getElementById(final String id) {
        // search in container itself
        final Object defaultElement = super.getElementById(id);
        if (defaultElement != null) {
            return defaultElement;
        }

        // search in imported containers
        final Object importElement = getElementByIdInLayers(id, imports);
        if (importElement != null) {
            return importElement;
        }
        // finally search in parent containers
        return getElementByIdInLayers(id, parents);
    }

    public List<Object> getValueInheritedElements() {
        return snRefElements;
    }

    @Override
    public Category getCategory() {
        return container;
    }

    @Override
    public void setCategory(final Category category) {
        this.container = category;
    }

    public boolean isExpandable() {
        //right now we can only guess whether service is present or not
        return snRefElements.stream().anyMatch(DIAGSERVICE.class::isInstance) ||
                (this instanceof HIERARCHYELEMENT && !((HIERARCHYELEMENT) this).getParentRefs().isEmpty());
    }
}
