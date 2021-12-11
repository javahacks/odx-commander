package com.javahacks.odx.lsp;

import com.javahacks.odx.lsp.dtos.*;
import org.eclipse.lsp4j.jsonrpc.services.JsonRequest;
import org.eclipse.lsp4j.jsonrpc.services.JsonSegment;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@JsonSegment("odx")
public interface OdxLspExtension {

    @JsonRequest(value = "getCategoriesByType")
    CompletableFuture<List<Document>> getCategoriesByType(Value type);

    @JsonRequest(value = "getCategoryDetails")
    CompletableFuture<List<DiagnosticElement>> getCategoryDetails(Link layerRef);

    @JsonRequest(value = "getLayersByType")
    CompletableFuture<List<Document>> getLayersByType(Value type);

    @JsonRequest(value = "getLayerDetails")
    CompletableFuture<LayerDetails> getLayerDetails(Link layerRef);

    @JsonRequest(value = "getServiceDetails")
    CompletableFuture<List<DiagnosticElement>> getServiceDetails(Service service);

    @JsonRequest(value = "getContent")
    CompletableFuture<String> getContent(Value sourceUri);

}
