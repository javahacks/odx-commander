package com.javahacks.odx.lsp;

import org.eclipse.lsp4j.jsonrpc.services.JsonDelegate;
import org.eclipse.lsp4j.services.LanguageServer;

/**
 * Advanced {@link LanguageServer} interface that provides additional ODX related services.
 */
public interface OdxLanguageServer extends LanguageServer {

    @JsonDelegate
    OdxLspExtension getOdxExtension();
}
