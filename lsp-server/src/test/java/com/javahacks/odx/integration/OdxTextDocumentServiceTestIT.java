package com.javahacks.odx.integration;

import com.javahacks.odx.lsp.dtos.Value;
import com.javahacks.odx.model.*;
import org.eclipse.lsp4j.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.StringWriter;
import java.time.Duration;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

import static com.javahacks.odx.index.JaxbUtil.marshalModel;
import static com.javahacks.odx.model.TestHelper.createEcuVariant;
import static com.javahacks.odx.model.TestHelper.newDiagLayerContainer;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;

public class OdxTextDocumentServiceTestIT extends LspTestBase {

    @BeforeEach
    void initServer(){
        getLanguageServer().initialized(new InitializedParams());
    }


    @Test
    void documentOpenThrowsNoErrors() {
        final StringWriter writer = new StringWriter();
        marshalModel(createTestModel(), writer);

        final DidOpenTextDocumentParams params = createOpenParams(writer.toString());
        assertThatCode(() -> getLanguageServer().getTextDocumentService().didOpen(params)).doesNotThrowAnyException();
    }

    @Test
    void invalidChangePublishesErrorDiagnostics() {
        createAndOpenDocument();
        getLanguageServer().getTextDocumentService().didChange(createInvalidChangeParams());

        assertThat(languageClient.diagnosticsRequest).succeedsWithin(1, TimeUnit.SECONDS)
                .matches(p -> !p.getDiagnostics().isEmpty());
    }

    @Test
    void validChangePublishesEmptyDiagnostics() {
        createAndOpenDocument();

        final DidChangeTextDocumentParams params = createChangeParam();
        final TextDocumentContentChangeEvent changeEvent = new TextDocumentContentChangeEvent();
        changeEvent.setText("");
        changeEvent.setRange(new Range(new Position(0, 0), new Position(0, 0)));
        params.setContentChanges(Collections.singletonList(changeEvent));

        getLanguageServer().getTextDocumentService().didChange(params);

        assertThat(languageClient.diagnosticsRequest).succeedsWithin(5, TimeUnit.SECONDS)
                .matches(p -> p.getDiagnostics().isEmpty());
    }

    @Test
    void closingErroneousDocumentShowsWarning() throws Exception {
        createAndOpenDocument();

        getLanguageServer().getTextDocumentService().didChange(createInvalidChangeParams());
        getLanguageServer().getOdxExtension().getCategoriesByType(new Value(DOCTYPE.LAYER.value())).get(5, TimeUnit.SECONDS);//join thread

        final DidCloseTextDocumentParams params = new DidCloseTextDocumentParams();
        params.setTextDocument(new TextDocumentIdentifier(createDocumentUri()));
        getLanguageServer().getTextDocumentService().didClose(params);

        assertThat(languageClient.messageRequest).succeedsWithin(1, TimeUnit.SECONDS)
                .matches(p -> MessageType.Warning.equals(p.getType()));
    }

    @Test
    void documentNotInIndexIsNotValidated() throws Exception {
        final DidOpenTextDocumentParams params = new DidOpenTextDocumentParams();
        final TextDocumentItem textDocument = new TextDocumentItem();
        textDocument.setUri("file:/invalid/dev/test.odx");
        textDocument.setText("invalid");
        params.setTextDocument(textDocument);
        getLanguageServer().getTextDocumentService().didOpen(params);

        getLanguageServer().getOdxExtension().getCategoriesByType(new Value(DOCTYPE.LAYER.value())).get(5, TimeUnit.SECONDS);//join thread

        assertThat(languageClient.diagnosticsRequest).isNotCompleted();
    }

    @Test
    void openDocumentNotInIndexShowsWarning()  {
        final DidOpenTextDocumentParams params = new DidOpenTextDocumentParams();
        final TextDocumentItem textDocument = new TextDocumentItem();
        textDocument.setUri("file:/media/dev/invalid.odx");
        textDocument.setText("invalid");
        params.setTextDocument(textDocument);
        getLanguageServer().getTextDocumentService().didOpen(params);

        assertThat(languageClient.messageRequest).succeedsWithin(1, TimeUnit.SECONDS)
                .matches(p -> MessageType.Warning.equals(p.getType()));
    }

    @Test
    void foldingRangesAreFound() throws Exception {
        createAndOpenDocument();
        getLanguageServer().getOdxExtension().getCategoriesByType(new Value(DOCTYPE.LAYER.value())).get(5, TimeUnit.SECONDS);//join thread

        final FoldingRangeRequestParams requestParams= new FoldingRangeRequestParams();
        requestParams.setTextDocument(new TextDocumentIdentifier(createDocumentUri()));


        final CompletableFuture<List<FoldingRange>> future = getLanguageServer().getTextDocumentService().foldingRange(requestParams);
        assertThat(future).succeedsWithin(Duration.ofSeconds(1)).asList().hasSize(2);
    }


    private DidChangeTextDocumentParams createInvalidChangeParams() {
        final DidChangeTextDocumentParams params = createChangeParam();
        final TextDocumentContentChangeEvent changeEvent = new TextDocumentContentChangeEvent();
        changeEvent.setText("invalid");
        changeEvent.setRange(new Range(new Position(0, 0), new Position(0, 0)));
        params.setContentChanges(Collections.singletonList(changeEvent));
        return params;
    }

    private void createAndOpenDocument() {
        final StringWriter writer = new StringWriter();
        marshalModel(createTestModel(), writer);
        getLanguageServer().getTextDocumentService().didOpen(createOpenParams(writer.toString()));
    }

    private DidOpenTextDocumentParams createOpenParams(final String text) {
        final DidOpenTextDocumentParams params = new DidOpenTextDocumentParams();
        final TextDocumentItem textDocument = new TextDocumentItem();
        textDocument.setUri(createDocumentUri());
        textDocument.setText(text);
        params.setTextDocument(textDocument);
        return params;
    }


    private ODX createTestModel() {
        final DIAGLAYERCONTAINER container = newDiagLayerContainer();
        container.setDESC(new DESCRIPTION());
        final ECUVARIANT ecuVariant = createEcuVariant();
        ecuVariant.getDiagServices().add(new DIAGSERVICE());
        container.getEcuVariants().add(ecuVariant);
        final ODX odx = new ODX();
        odx.setDIAGLAYERCONTAINER(container);
        return odx;
    }

    private DidChangeTextDocumentParams createChangeParam() {
        final DidChangeTextDocumentParams params = new DidChangeTextDocumentParams();
        final VersionedTextDocumentIdentifier identifier = new VersionedTextDocumentIdentifier();
        identifier.setUri(createDocumentUri());
        params.setTextDocument(identifier);
        return params;
    }


    private String createDocumentUri() {
        return workspacePath.toFile().toURI().toString();
    }

}
