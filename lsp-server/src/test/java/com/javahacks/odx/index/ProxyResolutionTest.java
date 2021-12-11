package com.javahacks.odx.index;

import com.javahacks.odx.model.*;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Optional;

import static com.javahacks.odx.model.TestHelper.createOdxLInk;
import static com.javahacks.odx.model.TestHelper.fillLinkTarget;

public class ProxyResolutionTest {

    private static final BASEVARIANT baseVariant = TestHelper.createBaseVariant();
    private static final ECUVARIANT ecuVariant = TestHelper.createEcuVariant();
    private static final ECUSHAREDDATA sharedData = TestHelper.createSharedData();
    private static final DIAGLAYERCONTAINER diaglayercontainer = TestHelper.newDiagLayerContainer();
    @TempDir
    static Path tempDir;
    private final DocumentIndex documentIndex = new DocumentIndex();

    @BeforeAll
    static void setupModel() throws IOException {
        ecuVariant.getParentRefs().add(fillLinkTarget(baseVariant, new BASEVARIANTREF()));
        baseVariant.getImportRefs().add(fillLinkTarget(sharedData, new ECUSHAREDDATAREF()));

        diaglayercontainer.getBaseVariants().add(baseVariant);
        diaglayercontainer.getEcuVariants().add(ecuVariant);
        diaglayercontainer.getEcusharedDatas().add(sharedData);

        final DIAGSERVICE evService = new DIAGSERVICE();
        evService.setID("ev-service-id");
        ecuVariant.getDiagServices().add(evService);

        final DIAGSERVICE esService = new DIAGSERVICE();
        esService.setID("es-service-id");
        sharedData.getDiagServices().add(esService);

        TestHelper.saveOdxFile(diaglayercontainer, tempDir.resolve("sample.xml"));
    }


    @Test
    void categoryProxyIsResolvedAndFound() {
        final Path odxFile = tempDir.resolve("sample.xml");
        documentIndex.resetIndex(createProxyIndex());

        final Optional<Category> category = documentIndex.getCategoryByUri(odxFile.toUri());
        Assertions.assertThat(category).isNotEmpty();
    }

    @Test
    void categoryProxyIsNotFound() {
        final Path odxFile = tempDir.resolve("invalid.xml");
        documentIndex.resetIndex(createProxyIndex());

        final Optional<Category> category = documentIndex.getCategoryByUri(odxFile.toUri());
        Assertions.assertThat(category).isEmpty();
    }


    @Test
    void layerProxyIsResolvedAndFound() {
        documentIndex.resetIndex(createProxyIndex());
        final Optional<LayerProxy> layerProxy = documentIndex.findLayerProxyForOdxLink(createOdxLInk(DOCTYPE.LAYER, ecuVariant.getSHORTNAME(), ecuVariant.getID()));
        Assertions.assertThat(layerProxy).isNotEmpty();
    }

    @Test
    void layerProxyIsNotAndFound() {
        documentIndex.resetIndex(createProxyIndex());
        final Optional<LayerProxy> layerProxy = documentIndex.findLayerProxyForOdxLink(createOdxLInk(DOCTYPE.LAYER, ecuVariant.getSHORTNAME(), "invalid-id"));
        Assertions.assertThat(layerProxy).isEmpty();
    }


    @Test
    void linkInLayerIsFound() {
        documentIndex.resetIndex(createProxyIndex());

        final Optional<DIAGSERVICE> service = documentIndex.findObjectInDocument(DOCTYPE.LAYER.name(), ecuVariant.getSHORTNAME(), "ev-service-id", DIAGSERVICE.class);
        Assertions.assertThat(service).isNotEmpty();
    }

    @Test
    void invalidLinkInLayerIsNotFound() {
        documentIndex.resetIndex(createProxyIndex());

        final Optional<DIAGSERVICE> service = documentIndex.findObjectInDocument(DOCTYPE.LAYER.name(), ecuVariant.getSHORTNAME(), "invalid-service-id", DIAGSERVICE.class);
        Assertions.assertThat(service).isEmpty();
    }

    @Test
    void linkInInvalidLayerIsNotFound() {
        documentIndex.resetIndex(createProxyIndex());

        final Optional<DIAGSERVICE> service = documentIndex.findObjectInDocument(DOCTYPE.LAYER.name(), "invalid-sn", "ev-service-id", DIAGSERVICE.class);
        Assertions.assertThat(service).isEmpty();
    }

    @Test
    void linkInLayerIsResolved() {
        documentIndex.resetIndex(createProxyIndex());

        final Optional<DIAGSERVICE> service = documentIndex.resolveLink(createOdxLInk(DOCTYPE.LAYER, ecuVariant.getSHORTNAME(), "ev-service-id"), DIAGSERVICE.class);
        Assertions.assertThat(service).isNotEmpty();
    }

    @Test
    void invalidLinkInLayerIsNotResolved() {
        documentIndex.resetIndex(createProxyIndex());

        final Optional<DIAGSERVICE> service = documentIndex.resolveLink(createOdxLInk(DOCTYPE.LAYER, ecuVariant.getSHORTNAME(), "invalid-service-id"), DIAGSERVICE.class);
        Assertions.assertThat(service).isEmpty();
    }

    @Test
    void linkInContainerIsResolved() {
        documentIndex.resetIndex(createProxyIndex());

        final Optional<DIAGSERVICE> service = documentIndex.resolveLink(createOdxLInk(DOCTYPE.CONTAINER, diaglayercontainer.getSHORTNAME(), "ev-service-id"), DIAGSERVICE.class);
        Assertions.assertThat(service).isNotEmpty();
    }

    @Test
    void invalidLinkInContainerIsNotResolved() {
        documentIndex.resetIndex(createProxyIndex());

        final Optional<DIAGSERVICE> service = documentIndex.resolveLink(createOdxLInk(DOCTYPE.CONTAINER, diaglayercontainer.getSHORTNAME(), "invalid-service-id"), DIAGSERVICE.class);
        Assertions.assertThat(service).isEmpty();
    }

    @Test
    void simpleLinkInLayerIsResolved() {
        documentIndex.resetIndex(createProxyIndex());

        final ODXLINK serviceLink = createOdxLInk(null, null, "ev-service-id");

        final Optional<DIAGLAYER> layer = documentIndex.resolveLink(createOdxLInk(DOCTYPE.LAYER, ecuVariant.getSHORTNAME(), ecuVariant.getID()), DIAGLAYER.class);
        serviceLink.setDocument(layer.get());

        final Optional<DIAGSERVICE> service = documentIndex.resolveLink(serviceLink, DIAGSERVICE.class);
        Assertions.assertThat(service).isNotEmpty();
    }


    @Test
    void simpleLinkInEsIsResolved() {
        documentIndex.resetIndex(createProxyIndex());

        final ODXLINK serviceLink = createOdxLInk(null, null, "es-service-id");

        final Optional<DIAGLAYER> layer = documentIndex.resolveLink(createOdxLInk(DOCTYPE.LAYER, ecuVariant.getSHORTNAME(), ecuVariant.getID()), DIAGLAYER.class);
        serviceLink.setDocument(layer.get());

        final Optional<DIAGSERVICE> service = documentIndex.resolveLink(serviceLink, DIAGSERVICE.class);
        Assertions.assertThat(service).isNotEmpty();
    }


    private ProxyIndex createProxyIndex() {
        final Path odxFile = tempDir.resolve("sample.xml");

        final CategoryProxy categoryProxy = new CategoryProxy();
        categoryProxy.setShortName(diaglayercontainer.getSHORTNAME());
        categoryProxy.setDocType(DOCTYPE.CONTAINER.name());
        categoryProxy.setId(diaglayercontainer.getID());
        categoryProxy.setIndexPath(odxFile.toUri());

        categoryProxy.getLayers().add(TestHelper.createLayerProxy(ecuVariant));
        categoryProxy.getLayers().add(TestHelper.createLayerProxy(baseVariant));
        categoryProxy.getLayers().add(TestHelper.createLayerProxy(sharedData));

        final ProxyIndex index = new ProxyIndex();
        index.getCategories().add(categoryProxy);
        return index;
    }


}
