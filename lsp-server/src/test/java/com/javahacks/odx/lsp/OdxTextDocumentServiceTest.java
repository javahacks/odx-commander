package com.javahacks.odx.lsp;

import com.google.common.util.concurrent.MoreExecutors;
import com.javahacks.odx.index.DocumentIndex;
import com.javahacks.odx.index.JaxbUtil;
import com.javahacks.odx.index.ProxyIndex;
import com.javahacks.odx.model.ODX;
import org.eclipse.lsp4j.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.io.StringWriter;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

import static java.net.URI.create;
import static org.assertj.core.api.Assertions.assertThat;

class OdxTextDocumentServiceTest {
    private static final String TEST_URI = "file:/dev/media/test.odx";
    private final DocumentIndex documentIndex = new DocumentIndex();
    private final OdxLanguageClient languageClient = Mockito.mock(OdxLanguageClient.class);

    private final OdxTextDocumentService textDocumentService = new OdxTextDocumentService(documentIndex, MoreExecutors.newDirectExecutorService(), () -> languageClient);


    @BeforeEach
    void setUp() {
        final ProxyIndex proxyIndex = new ProxyIndex();
        proxyIndex.setOdxSourcePath(Paths.get("/dev/media/"));
        documentIndex.resetIndex(proxyIndex);
    }

    @Test
    void openCreatesVirtualDocument() {
        final TextDocumentItem textDocument = openTextDocument();

        final Optional<VirtualDocument> virtualDocument = documentIndex.getVirtualDocument(create(textDocument.getUri()));
        assertThat(virtualDocument).isPresent();
        assertThat(virtualDocument.get().getTextContent()).isEqualTo(textDocument.getText());
    }


    @Test
    void changeUpdatesVirtualDocument() throws InterruptedException {
        openTextDocument();

        final DidChangeTextDocumentParams changeParams = new DidChangeTextDocumentParams();
        changeParams.setTextDocument(createIdentifier());
        final TextDocumentContentChangeEvent changeEvent = new TextDocumentContentChangeEvent();
        changeEvent.setText("invalid");
        changeEvent.setRange(new Range(new Position(), new Position()));
        changeParams.setContentChanges(Collections.singletonList(changeEvent));

        textDocumentService.didChange(changeParams);

        final Optional<VirtualDocument> virtualDocument = documentIndex.getVirtualDocument(create(TEST_URI));

        assertThat(virtualDocument.get().isErroneous()).isTrue();

    }


    private VersionedTextDocumentIdentifier createIdentifier() {
        final VersionedTextDocumentIdentifier identifier = new VersionedTextDocumentIdentifier();
        identifier.setUri(TEST_URI);
        return identifier;
    }


    private TextDocumentItem openTextDocument() {
        final TextDocumentItem textDocument = createTextDocumentItem();
        final DidOpenTextDocumentParams openParams = new DidOpenTextDocumentParams();
        openParams.setTextDocument(textDocument);

        textDocumentService.didOpen(openParams);

        return textDocument;
    }


    private TextDocumentItem createTextDocumentItem() {
        final TextDocumentItem textDocument = new TextDocumentItem();
        textDocument.setUri(TEST_URI);
        final StringWriter writer = new StringWriter();
        JaxbUtil.marshalModel(new ODX(), writer);
        textDocument.setText(writer.toString());
        return textDocument;
    }

}