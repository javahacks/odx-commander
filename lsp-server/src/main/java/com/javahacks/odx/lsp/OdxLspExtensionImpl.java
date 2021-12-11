package com.javahacks.odx.lsp;

import com.javahacks.odx.index.Category;
import com.javahacks.odx.index.DocumentIndex;
import com.javahacks.odx.index.JaxbUtil;
import com.javahacks.odx.lsp.dtos.*;
import com.javahacks.odx.lsp.mapper.*;
import com.javahacks.odx.model.COMPARAMSPEC;
import com.javahacks.odx.model.COMPARAMSUBSET;
import com.javahacks.odx.model.FLASH;
import com.javahacks.odx.model.VEHICLEINFOSPEC;

import javax.inject.Inject;
import java.io.StringWriter;
import java.net.URI;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;

import static com.javahacks.odx.index.JaxbUtil.marshalModelFormattedAndSimplified;
import static com.javahacks.odx.utils.OdxUtils.parseUri;


/**
 * Implementation of additional ODX related LSP services
 */
public class OdxLspExtensionImpl implements OdxLspExtension {
    private final ServiceDetailsMapper serviceDetailsMapper;
    private final LayerMapper layerMapper;
    private final CategoryMapper categoryMapper;

    private final DocumentIndex documentIndex;
    private final ExecutorService executorService;

    @Inject
    public OdxLspExtensionImpl(final DocumentIndex documentIndex,final ExecutorService executorService) {
        this.documentIndex = documentIndex;
        this.executorService = executorService;
        this.layerMapper = new LayerMapper(documentIndex);
        this.categoryMapper = new CategoryMapper(documentIndex);
        this.serviceDetailsMapper = new ServiceDetailsMapper(documentIndex);
    }

    @Override
    public CompletableFuture<String> getContent(final Value sourceUri) {
        return CompletableFuture.supplyAsync(() -> {
            //remove readonly prefix in order to load document
            final URI uri = parseUri(sourceUri.getValue());
            final Category category = documentIndex.getCategoryByUri(uri).get();
            final StringWriter writer = new StringWriter();
            marshalModelFormattedAndSimplified(category.getParent(), writer);
            return writer.toString();

        }, executorService);
    }

    @Override
    public CompletableFuture<List<Document>> getLayersByType(final Value type) {
        return CompletableFuture
                .supplyAsync(() -> layerMapper.map(type.getValue()), executorService);
    }

    @Override
    public CompletableFuture<List<Document>> getCategoriesByType(final Value type) {
        return CompletableFuture
                .supplyAsync(() -> categoryMapper.map(type.getValue()), executorService);
    }

    @Override
    public CompletableFuture<List<DiagnosticElement>> getCategoryDetails(final Link odxLink) {
        return CompletableFuture
                .supplyAsync(() -> mapCategoryDetails(odxLink), executorService);
    }

    @Override
    public CompletableFuture<LayerDetails> getLayerDetails(final Link layerRef) {
        return CompletableFuture.
                supplyAsync(() -> new LayerDetailsMapper(documentIndex, layerRef).map(), executorService);
    }

    @Override
    public CompletableFuture<List<DiagnosticElement>> getServiceDetails(final Service service) {
        return CompletableFuture.supplyAsync(() -> serviceDetailsMapper.map(service), executorService);
    }

    private List<DiagnosticElement> mapCategoryDetails(final Link odxLink) {
        final Category category = documentIndex.findObjectInDocument(odxLink.getDocType(), odxLink.getDocRef(), odxLink.getIdRef(), Category.class).orElse(null);
        if (category instanceof VEHICLEINFOSPEC) {
            return new VehicleInformationMapper((VEHICLEINFOSPEC) category).map();
        }

        if (category instanceof COMPARAMSUBSET) {
            return new ComParamSubsetMapper((COMPARAMSUBSET) category).map();
        }

        if (category instanceof COMPARAMSPEC) {
            return new ComParamSpecMapper((COMPARAMSPEC) category).map();
        }

        if (category instanceof FLASH) {
            return new FlashMapper((FLASH) category).map();
        }

        return Collections.emptyList();
    }

}
