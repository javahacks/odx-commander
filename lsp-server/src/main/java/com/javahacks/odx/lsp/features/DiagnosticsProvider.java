package com.javahacks.odx.lsp.features;

import com.google.common.base.Strings;
import com.javahacks.odx.index.*;
import com.javahacks.odx.lsp.VirtualDocument;
import com.javahacks.odx.model.ODXLINK;
import org.eclipse.lsp4j.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static com.javahacks.odx.utils.OdxUtils.locationToRange;

/**
 * Provided LSP diagnostics support
 */
public class DiagnosticsProvider {
    private final List<Diagnostic> diagnostics = new ArrayList<>();
    private final DocumentIndex documentIndex;
    private final VirtualDocument document;

    public DiagnosticsProvider(final DocumentIndex documentIndex, final VirtualDocument document) {
        this.documentIndex = documentIndex;
        this.document = document;
    }

    public PublishDiagnosticsParams updateAndValidate() {
        try {
            documentIndex.updateVirtualDocument(document);
        } catch (final LocationAwareParsingException e) {
            diagnostics.add(mapParsingException(e));
        }

        diagnostics.addAll(validateOdxLinks());
        document.setErroneous(hasErrorDiagnostics());

        return createDiagnosticsParams();
    }


    private PublishDiagnosticsParams createDiagnosticsParams() {
        final PublishDiagnosticsParams diagnosticsParams = new PublishDiagnosticsParams();
        diagnosticsParams.setUri(document.getUri().toString());
        diagnosticsParams.getDiagnostics().addAll(diagnostics);
        return diagnosticsParams;
    }

    private List<Diagnostic> validateOdxLinks() {
        if (document.getVirtualModel() == null) {
            return Collections.emptyList();
        }

        return document.getVirtualModel().getLocationAwares().stream()
                .filter(ODXLINK.class::isInstance)
                .map(ODXLINK.class::cast)
                .filter(this::linkTargetInValid)
                .map(this::mapMissingTarget)
                .collect(Collectors.toList());

    }

    private Diagnostic mapMissingTarget(final AbstractLink abstractLink) {
        final Diagnostic diagnostic = new Diagnostic();
        diagnostic.setSeverity(DiagnosticSeverity.Warning);
        diagnostic.setMessage("Link target missing");
        diagnostic.setRange(locationToRange(abstractLink.getLocation()));
        return diagnostic;
    }

    private boolean linkTargetInValid(final ODXLINK link) {
        if (!diagnostics.isEmpty() && isTargetInSameDocument(link)) {
            //if the document is erroneous and target in same document invalidate target markers are confusing
            return false;
        }
        return !documentIndex.resolveLink(link, AbstractLinkTarget.class).isPresent();
    }

    private boolean isTargetInSameDocument(final ODXLINK link) {
        if (Strings.isNullOrEmpty(link.getDOCREF()) || link.getDOCTYPE() == null) {
            return true;
        }
        final IndexableDocument indexableDocument = documentIndex.resolveProxyDocument(link.getDOCTYPE().name(), link.getDOCREF());
        return (indexableDocument == null || document.getUri().equals(indexableDocument.getLocation().getFileUri()));
    }

    private Diagnostic mapParsingException(final LocationAwareParsingException exception) {
        final Diagnostic diagnostic = new Diagnostic();
        diagnostic.setSeverity(DiagnosticSeverity.Error);
        diagnostic.setMessage(exception.getDetailedMessage());

        final Position start = new Position(exception.getStartLine(), exception.getStartColumn());
        final Position end = new Position(exception.getEndLine(), exception.getEndColumn());
        diagnostic.setRange(new Range(start, end));
        return diagnostic;
    }

    private boolean hasErrorDiagnostics() {
        return diagnostics.stream().anyMatch(diagnostic -> DiagnosticSeverity.Error.equals(diagnostic.getSeverity()));
    }

}
