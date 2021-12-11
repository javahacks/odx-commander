package com.javahacks.odx.index;

import com.javahacks.odx.model.PARAM;

import java.util.List;

/**
 * Interface for all named elements that provide a list of {@link PARAM} objects
 */
public interface ElementWithParams extends LocationAware {

    String getSHORTNAME();

    List<PARAM> getParams();
}
