package com.javahacks.odx.lsp.mapper;

import com.javahacks.odx.index.DocumentIndex;
import com.javahacks.odx.index.LayerProxy;
import com.javahacks.odx.lsp.dtos.Document;
import com.javahacks.odx.lsp.dtos.Link;
import com.javahacks.odx.model.DIAGLAYER;
import com.javahacks.odx.model.DOCTYPE;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Maps all information for a single {@link DIAGLAYER} that is shown in the client
 */
public class LayerMapper {
    private final DocumentIndex documentIndex;

    public LayerMapper(final DocumentIndex documentIndex) {
        this.documentIndex = documentIndex;
    }

    public List<Document> map(final String layerType) {
        return documentIndex.getIndexedCategories().stream()
                .filter(category -> DOCTYPE.CONTAINER.name().equals(category.getDocType()))
                .flatMap(category -> category.getLayers().stream())
                .filter(layer -> layer.getLayerType().equals(layerType))
                .map(this::mapLayer)
                .sorted(Comparator.comparing(Document::getName))
                .collect(Collectors.toList());
    }


    private Document mapLayer(final LayerProxy proxy) {
        final Document description = new Document();
        description.setOdxLink(new Link(proxy.getDocType(), proxy.getShortName(), proxy.getId()));
        description.setName(proxy.getShortName());
        description.setLocation(proxy.getLocation());
        description.setExpandable(proxy.isExpandable());
        return description;
    }
}
