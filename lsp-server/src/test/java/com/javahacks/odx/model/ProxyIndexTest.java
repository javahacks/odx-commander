package com.javahacks.odx.model;

import com.javahacks.odx.index.*;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.net.URI;
import java.nio.charset.StandardCharsets;

import static org.assertj.core.api.Assertions.assertThat;

class ProxyIndexTest {

    public static final URI FILE_URI = URI.create("d:/temp/test.text");


    @Test
    void indexVersionIsMarshalled() throws Exception {
        final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        final ProxyIndex testIndex = createTestIndex();
        testIndex.setIndexVersion(12);
        JaxbUtil.marshalProxyIndex(testIndex, outputStream);
        final ProxyIndex restoredIndex = JaxbUtil.unmarshalProxyIndex(new ByteArrayInputStream(outputStream.toByteArray()));
        assertThat(restoredIndex.getIndexVersion()).isEqualTo(12);
    }

    @Test
    void categoriesAreMarshalled() throws Exception {
        final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        final ProxyIndex testIndex = createTestIndex();
        JaxbUtil.marshalProxyIndex(testIndex, outputStream);
        final ProxyIndex restoredIndex = JaxbUtil.unmarshalProxyIndex(new ByteArrayInputStream(outputStream.toByteArray()));
        assertThat(restoredIndex.getCategories()).isNotEmpty();
        assertThat(restoredIndex.getCategories().get(0).getIndexPath()).isEqualTo(FILE_URI);
    }

    @Test
    void layersAreMarshalled() throws Exception {
        final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        final ProxyIndex testIndex = createTestIndex();
        JaxbUtil.marshalProxyIndex(testIndex, outputStream);
        final ProxyIndex restoredIndex = JaxbUtil.unmarshalProxyIndex(new ByteArrayInputStream(outputStream.toByteArray()));
        assertThat(restoredIndex.getCategories().get(0).getLayers()).isNotEmpty();
    }

    @Test
    void positionIsMarshalled() throws Exception {
        final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        final ProxyIndex testIndex = createTestIndex();
        JaxbUtil.marshalProxyIndex(testIndex, outputStream);
        final ProxyIndex restoredIndex = JaxbUtil.unmarshalProxyIndex(new ByteArrayInputStream(outputStream.toByteArray()));
        assertThat(restoredIndex.getCategories().get(0).getLayers().get(0).getLocation()).isNotNull();
    }

    @Test
    void moreGeneralLocationIsNotMarshalled() throws Exception {
        final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        final ProxyIndex testIndex = createTestIndex();

        JaxbUtil.marshalProxyIndex(testIndex, outputStream);
        final String xmlContent = new String(outputStream.toByteArray(), StandardCharsets.UTF_8);
        assertThat(xmlContent).doesNotContain("endLine");
    }


    private ProxyIndex createTestIndex() {
        final ProxyIndex index = new ProxyIndex();
        final CategoryProxy category = new CategoryProxy();
        category.setShortName("Category");
        category.setDocType("Layer");
        category.setShortName("LayerSN");
        category.setLastModification(12);
        category.setIndexPath(FILE_URI);
        index.getCategories().add(category);

        final LayerProxy layer = new LayerProxy();
        layer.setLayerType("layer");
        layer.setShortName("layerSN");
        final XmlElementLocation location = new XmlElementLocation(FILE_URI, 1, 1);
        location.setEndLine(100);
        location.setEndColumn(100);
        layer.setLocation(location);
        category.getLayers().add(layer);
        return index;
    }

}