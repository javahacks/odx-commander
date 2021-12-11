package com.javahacks.odx.index;


/**
 * Must be implemented by all elements that should keep track of their enclosing {@link com.javahacks.odx.model.ODXCATEGORY}.
 */
public interface CategoryAware {

    Category getCategory();

    void setCategory(final Category category);
}
