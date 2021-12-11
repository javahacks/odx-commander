package com.javahacks.odx.index;

import com.javahacks.odx.model.DOCTYPE;
import com.javahacks.odx.model.ODXLINK;
import org.junit.jupiter.api.Test;

import static com.javahacks.odx.model.TestHelper.createOdxLInk;
import static org.assertj.core.api.Assertions.assertThat;

class DocumentIndexTest {
    private static final String CATEGORY_ID = "category_id";
    private static final String CATEGORY_NAME = "category_name";
    private static final String LAYER_ID = "layer_id";
    private static final String LAYER_NAME = "layer_name";

    @Test
    void existingLayerProxyIsFound() {
        final DocumentIndex index = new DocumentIndex();
        index.resetIndex(createSimpleLayerIndex(layerProxy()));

        final ODXLINK odxlink = createOdxLInk(DOCTYPE.LAYER, LAYER_NAME, LAYER_ID);
        assertThat(index.findLayerProxyForOdxLink(odxlink)).isNotEmpty();
    }

    @Test
    void nonExistingLayerProxyIsNotFound() {
        final DocumentIndex index = new DocumentIndex();
        index.resetIndex(createSimpleLayerIndex(layerProxy()));

        final ODXLINK odxlink = createOdxLInk(DOCTYPE.LAYER, LAYER_NAME, "invalid");
        assertThat(index.findLayerProxyForOdxLink(odxlink)).isEmpty();
    }

    @Test
    void existingCategoryProxyIsFound() {
        final DocumentIndex index = new DocumentIndex();
        index.resetIndex(createSimpleLayerIndex(layerProxy()));

        final ODXLINK odxlink = createOdxLInk(DOCTYPE.CONTAINER, CATEGORY_NAME, CATEGORY_ID);
        assertThat(index.findCategoryProxyForOdxLink(odxlink)).isNotEmpty();
    }

    @Test
    void nonExistingCatgegoryProxyIsNotFound() {
        final DocumentIndex index = new DocumentIndex();
        index.resetIndex(createSimpleLayerIndex(layerProxy()));

        final ODXLINK odxlink = createOdxLInk(DOCTYPE.CONTAINER, CATEGORY_NAME, "invalid");
        assertThat(index.findCategoryProxyForOdxLink(odxlink)).isEmpty();
    }


    private LayerProxy layerProxy() {
        final LayerProxy layerProxy = new LayerProxy();
        layerProxy.setDocType(DOCTYPE.LAYER.name());
        layerProxy.setLocation(new XmlElementLocation());
        layerProxy.setShortName(LAYER_NAME);
        layerProxy.setId(LAYER_ID);
        return layerProxy;
    }

    private ProxyIndex createSimpleLayerIndex(final LayerProxy layerProxy) {
        final CategoryProxy categoryProxy = new CategoryProxy();
        categoryProxy.setDocType(DOCTYPE.CONTAINER.name());
        categoryProxy.setId(CATEGORY_ID);
        categoryProxy.setShortName(CATEGORY_NAME);
        categoryProxy.getLayers().add(layerProxy);
        final ProxyIndex proxyIndex = new ProxyIndex();
        proxyIndex.getCategories().add(categoryProxy);
        return proxyIndex;
    }
}