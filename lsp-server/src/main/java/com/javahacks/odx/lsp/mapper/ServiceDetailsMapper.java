package com.javahacks.odx.lsp.mapper;

import com.javahacks.odx.index.DocumentIndex;
import com.javahacks.odx.lsp.dtos.DiagnosticElement;
import com.javahacks.odx.lsp.dtos.Service;
import com.javahacks.odx.model.DIAGLAYER;
import com.javahacks.odx.model.DIAGSERVICE;

import java.util.Collections;
import java.util.List;

public class ServiceDetailsMapper {
    private final DocumentIndex documentIndex;

    public ServiceDetailsMapper(final DocumentIndex documentIndex) {
        this.documentIndex = documentIndex;
    }

    public List<DiagnosticElement> map(final Service service) {
        final DIAGLAYER layer = documentIndex.findObjectInDocument(service.getLayerRef().getDocType(), service.getLayerRef().getDocRef(), service.getLayerRef().getIdRef(), DIAGLAYER.class).orElse(null);
        if (layer == null) {
            return Collections.emptyList();
        }
        return layer.query().diagCommByShortName(service.getName(), false)
                .map(DIAGSERVICE.class::cast)
                .map(diagService -> new ServiceMapper(layer, diagService).map())
                .orElse(Collections.emptyList());
    }
}
