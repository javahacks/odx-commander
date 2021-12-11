package com.javahacks.odx.index;

import com.javahacks.odx.model.DIAGLAYER;

/**
 * ODX elements that can be identified by id inside a single {@link DIAGLAYER} oder {@link com.javahacks.odx.model.ODXCATEGORY}.
 */
public interface IdentifiableElement {

    String getID();
}
