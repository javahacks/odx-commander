package com.javahacks.odx.lsp;

import com.google.common.util.concurrent.MoreExecutors;
import com.javahacks.odx.index.*;
import com.javahacks.odx.lsp.dtos.DiagnosticElement;
import com.javahacks.odx.lsp.dtos.Document;
import com.javahacks.odx.lsp.dtos.Value;
import com.javahacks.odx.lsp.dtos.Link;
import com.javahacks.odx.model.*;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import java.net.URI;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

import static com.javahacks.odx.lsp.mapper.Types.getDocumentType;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class OdxLspExtensionImplTest {
    private final DocumentIndex documentIndex = mock(DocumentIndex.class);
    private final OdxLspExtension lspExtension = new OdxLspExtensionImpl(documentIndex,  MoreExecutors.newDirectExecutorService());

    @Test
    void documentIsFoundAndModified() {
        final URI test = URI.create("z:/test.odx");
        when(documentIndex.getCategoryByUri(test)).thenReturn(Optional.of(TestHelper.newDiagLayerContainer()));

        final CompletableFuture<String> content = lspExtension.getContent(new Value(test.toString()));
        assertThat(content).isCompletedWithValueMatching(s -> s.contains(LocationAwareXMLStreamWriter.NOTICE));
    }


    @Test
    void layersAreFoundByType() {
        final CategoryProxy categoryProxy = TestHelper.createCategoryProxy(DOCTYPE.CONTAINER);

        categoryProxy.getLayers().add(TestHelper.createLayerProxy(getDocumentType(new ECUVARIANT())));
        categoryProxy.getLayers().add(TestHelper.createLayerProxy(getDocumentType(new BASEVARIANT())));
        categoryProxy.getLayers().add(TestHelper.createLayerProxy(getDocumentType(new PROTOCOL())));
        categoryProxy.getLayers().add(TestHelper.createLayerProxy(getDocumentType(new FUNCTIONALGROUP())));
        categoryProxy.getLayers().add(TestHelper.createLayerProxy(getDocumentType(new ECUSHAREDDATA())));

        when(documentIndex.getIndexedCategories()).thenReturn(Collections.singletonList(categoryProxy));

        for (final LayerProxy layerProxy : categoryProxy.getLayers()) {
            final CompletableFuture<List<Document>> layers = lspExtension.getLayersByType(new Value(layerProxy.getLayerType()));
            Assertions.assertThat(layers).isCompletedWithValueMatching(result -> result.size() == 1);
        }
    }


    @Test
    void categoryWithProperTypeIsFound() {
        final CategoryProxy containerProxy = TestHelper.createCategoryProxy(DOCTYPE.CONTAINER);
        final CategoryProxy flashProxy = TestHelper.createCategoryProxy(DOCTYPE.FLASH);
        containerProxy.getLayers().add(TestHelper.createLayerProxy(getDocumentType(new ECUVARIANT())));

        when(documentIndex.getIndexedCategories()).thenReturn(Arrays.asList(containerProxy, flashProxy));

        final CompletableFuture<List<Document>> categories = lspExtension.getCategoriesByType(new Value(DOCTYPE.CONTAINER.name()));
        final CompletableFuture<List<Document>> flashDocs = lspExtension.getCategoriesByType(new Value(DOCTYPE.FLASH.name()));

        Assertions.assertThat(categories).isCompletedWithValueMatching(result -> result.size() == 1);
        Assertions.assertThat(flashDocs).isCompletedWithValueMatching(result -> result.size() == 1);

    }


    @Test
    void vehicleInfoSpecDetailsAreResolved() {

        final VEHICLEINFOSPEC spec = TestHelper.createTestSpec();
        spec.setIndex(documentIndex);
        when(documentIndex.findObjectInDocument(DOCTYPE.VEHICLE_INFO_SPEC.name(), spec.getSHORTNAME(), spec.getID(), Category.class)).thenReturn(Optional.of(spec));

        final Link link = new Link();
        link.setDocType(DOCTYPE.VEHICLE_INFO_SPEC.name());
        link.setDocRef(spec.getSHORTNAME());
        link.setIdRef(spec.getID());

        final CompletableFuture<List<DiagnosticElement>> categoryDetails = lspExtension.getCategoryDetails(link);

        Assertions.assertThat(categoryDetails).isCompletedWithValueMatching(result -> result.size() == 1);


    }

    @Test
    void comparamSubsetDetailsAreResolved() {

        final VEHICLEINFOSPEC spec = TestHelper.createTestSpec();
        spec.setIndex(documentIndex);
        when(documentIndex.findObjectInDocument(DOCTYPE.VEHICLE_INFO_SPEC.name(), spec.getSHORTNAME(), spec.getID(), Category.class)).thenReturn(Optional.of(spec));

        final Link link = new Link();
        link.setDocType(DOCTYPE.VEHICLE_INFO_SPEC.name());
        link.setDocRef(spec.getSHORTNAME());
        link.setIdRef(spec.getID());

        final CompletableFuture<List<DiagnosticElement>> categoryDetails = lspExtension.getCategoryDetails(link);

        Assertions.assertThat(categoryDetails).isCompletedWithValueMatching(result -> result.size() == 1);

    }

}