package com.javahacks.odx.index;


import com.javahacks.odx.model.DIAGLAYER;

/**
 * Must be implemented by all elements that are contained id a {@link DIAGLAYER} and need a reference to it.
 */
public interface LayerAware {

    /**
     * The {@link DIAGLAYER} which contains the implementing element
     */
    Layer getLayer();

    void setLayer(final Layer layer);
}
