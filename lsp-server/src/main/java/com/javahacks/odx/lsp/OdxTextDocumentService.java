package com.javahacks.odx.lsp;

import com.javahacks.odx.index.DocumentIndex;
import com.javahacks.odx.lsp.features.*;
import org.eclipse.lsp4j.*;
import org.eclipse.lsp4j.services.TextDocumentService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.inject.Provider;
import java.net.URI;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;

import static com.javahacks.odx.lsp.features.MessageHelper.showWarning;
import static com.javahacks.odx.utils.OdxUtils.parseUri;

/**
 * {@link TextDocumentService} implementation used to handle ODX documents.
 */
public class OdxTextDocumentService implements TextDocumentService {
    private static final Logger LOGGER = LoggerFactory.getLogger(OdxTextDocumentService.class);
    private final DocumentLinkProvider documentLinkProvider = new DocumentLinkProvider();
    private final HoverProvider hoverProvider = new HoverProvider();
    private final CodelensProvider codelensProvider = new CodelensProvider();
    private final FoldingProvider foldingProvider = new FoldingProvider();
    private final ExecutorService executorService;
    private final Provider<OdxLanguageClient> clientProvider;
    private final DocumentIndex documentIndex;

    @Inject
    public OdxTextDocumentService(final DocumentIndex documentIndex, final ExecutorService executorService, final Provider<OdxLanguageClient> languageClientProvider) {
        this.documentIndex = documentIndex;
        this.executorService = executorService;
        this.clientProvider = languageClientProvider;
    }

    @Override
    public void didOpen(final DidOpenTextDocumentParams params) {
        LOGGER.trace("Text document {} opened", params.getTextDocument().getUri());
        final boolean readonly = params.getTextDocument().getUri().startsWith("odx:");
        final URI uri = parseUri(params.getTextDocument().getUri());
        final VirtualDocument document = new VirtualDocument(uri, params.getTextDocument().getText(), readonly);
        documentIndex.addVirtualDocument(document);

        executorService.submit(() -> {
            if (!documentIndex.partOfIndex(uri)) {
                showWarning(clientProvider.get(), "Document not part of active ODX index. Run indexing command first!");
            } else {
                document.setVirtualModel(documentIndex.getCategoryByUri(uri).orElse(null));
                validateAndPublish(document);
            }
        });

    }

    @Override
    public void didChange(final DidChangeTextDocumentParams params) {
        LOGGER.trace("Text document {} changed", params.getTextDocument().getUri());
        final VirtualDocument document = getDocument(params.getTextDocument().getUri());
        document.applyTextChanges(params.getContentChanges());
        validateAndPublish(document);
    }

    @Override
    public void didClose(final DidCloseTextDocumentParams params) {
        final URI uri = parseUri(params.getTextDocument().getUri());
        final VirtualDocument document = documentIndex.removeVirtualDocument(uri);
        executorService.submit(() -> {
            if (document.isErroneous()) {
                showWarning(clientProvider.get(), "You closed the erroneous document " + uri.getPath() + ". This might have invalidated your ODX data!");
            }
        });
        LOGGER.trace("Text document {} closed", params.getTextDocument().getUri());
    }

    @Override
    public void didSave(final DidSaveTextDocumentParams params) {
        LOGGER.trace("Text document {} saved", params.getTextDocument().getUri());
    }

    @Override
    public CompletableFuture<List<DocumentLink>> documentLink(final DocumentLinkParams params) {
        final VirtualDocument document = getDocument(params.getTextDocument().getUri());
        return CompletableFuture.supplyAsync(() -> documentLinkProvider.getDocumentLinks(document), executorService);
    }

    @Override
    public CompletableFuture<Hover> hover(final HoverParams params) {
        final VirtualDocument document = getDocument(params.getTextDocument().getUri());

        return CompletableFuture.supplyAsync(() -> hoverProvider.createHover(document, params.getPosition()).orElse(null), executorService);
    }

    @Override
    public CompletableFuture<List<FoldingRange>> foldingRange(final FoldingRangeRequestParams params) {
        final VirtualDocument document = getDocument(params.getTextDocument().getUri());

        return CompletableFuture.supplyAsync(() -> foldingProvider.createFoldingRanges(document), executorService);

    }


    @Override
    public CompletableFuture<List<? extends CodeLens>> codeLens(final CodeLensParams params) {
        final VirtualDocument document = getDocument(params.getTextDocument().getUri());
        return CompletableFuture.supplyAsync(() -> codelensProvider.createCodeLens(document), executorService);
    }

    private void validateAndPublish(final VirtualDocument document) {
        if (document.isReadOnly() || !documentIndex.partOfIndex(document.getUri())) {
            return;
        }
        final DiagnosticsProvider diagnosticsProvider = new DiagnosticsProvider(documentIndex, document);
        executorService.submit(() ->
                clientProvider.get().publishDiagnostics(diagnosticsProvider.updateAndValidate())
        );
    }

    private VirtualDocument getDocument(final String uri) {
        return documentIndex.getVirtualDocument(parseUri(uri)).orElseThrow(() ->
                new IllegalStateException("No document found for URI '" + uri + "'")
        );
    }
}
