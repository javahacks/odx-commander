package com.javahacks.odx.lsp;

import org.eclipse.lsp4j.jsonrpc.services.JsonNotification;
import org.eclipse.lsp4j.jsonrpc.services.JsonSegment;
import org.eclipse.lsp4j.services.LanguageClient;

/**
 * Language client stub that provides additional ODX related services
 */
@JsonSegment("odx")
public interface OdxLanguageClient extends LanguageClient {

    /**
     * Send from server to client to trigger a data invalidation on the client side.
     */
    @JsonNotification(value = "indexChanged")
    void indexChanged();
}
