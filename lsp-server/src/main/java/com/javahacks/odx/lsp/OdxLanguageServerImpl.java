package com.javahacks.odx.lsp;

import org.eclipse.lsp4j.*;
import org.eclipse.lsp4j.services.TextDocumentService;
import org.eclipse.lsp4j.services.WorkspaceService;

import javax.inject.Inject;
import java.util.concurrent.CompletableFuture;

/**
 * Language server implementation for diagnostic data
 */
public class OdxLanguageServerImpl implements OdxLanguageServer {
    private final TextDocumentService textService;
    private final OdxWorkspaceService workspaceService;
    private final OdxLspExtension lspExtension;


    @Inject
    public OdxLanguageServerImpl(final TextDocumentService textDocumentService, final OdxWorkspaceService workspaceService, final OdxLspExtension lspExtension) {
        this.textService = textDocumentService;
        this.workspaceService = workspaceService;
        this.lspExtension = lspExtension;
    }

    public CompletableFuture<InitializeResult> initialize(final InitializeParams params) {
        final InitializeResult result = new InitializeResult(new ServerCapabilities());
        result.getCapabilities().setTextDocumentSync(TextDocumentSyncKind.Incremental);
        result.getCapabilities().setWorkspace(new WorkspaceServerCapabilities());
        result.getCapabilities().setDocumentLinkProvider(new DocumentLinkOptions());
        result.getCapabilities().setFoldingRangeProvider(true);
        result.getCapabilities().setHoverProvider(true);
        result.getCapabilities().setCodeLensProvider(new CodeLensOptions(false));
        return CompletableFuture.completedFuture(result);
    }

    @Override
    public void initialized(final InitializedParams params) {
        workspaceService.onClientInitialized();
    }

    @Override
    public CompletableFuture<Object> shutdown() {
        return CompletableFuture.supplyAsync(workspaceService::shutDown);
    }

    public TextDocumentService getTextDocumentService() {
        return this.textService;
    }

    public WorkspaceService getWorkspaceService() {
        return this.workspaceService;
    }

    @Override
    public OdxLspExtension getOdxExtension() {
        return this.lspExtension;
    }

    @Override
    public void exit() {
        Runtime.getRuntime().exit(1);
    }
}