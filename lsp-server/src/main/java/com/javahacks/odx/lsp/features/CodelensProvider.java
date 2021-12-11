package com.javahacks.odx.lsp.features;

import com.javahacks.odx.index.IndexableDocument;
import com.javahacks.odx.index.LocationAware;
import com.javahacks.odx.lsp.VirtualDocument;
import com.javahacks.odx.model.DIAGLAYERCONTAINER;
import org.eclipse.lsp4j.CodeLens;
import org.eclipse.lsp4j.Command;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static com.javahacks.odx.lsp.mapper.Types.getDocumentType;
import static com.javahacks.odx.utils.OdxUtils.locationToRange;
import static java.util.Arrays.asList;

/**
 * This class is responsible for providing all {@link CodeLens} instances for a single document.
 */
public class CodelensProvider {
    public static final String REVEAL_DOCUMENT_COMMAND = "odx.revealDocument";
    public static final String REVEAL_TITLE = "Show in Navigator";

    public List<CodeLens> createCodeLens(final VirtualDocument document) {
        if (document.getVirtualModel() == null) {
            return Collections.emptyList();
        }

        return document.getVirtualModel().getLocationAwares().stream()
                .filter(this::isValidDocument)
                .map(IndexableDocument.class::cast)
                .map(this::createCodeLens)
                .collect(Collectors.toList());
    }

    private boolean isValidDocument(final LocationAware locationAware) {
        return locationAware instanceof IndexableDocument && !(locationAware instanceof DIAGLAYERCONTAINER);
    }

    private CodeLens createCodeLens(final IndexableDocument document) {
        final CodeLens codeLens = new CodeLens();
        codeLens.setRange(locationToRange(document.getLocation()));
        codeLens.setCommand(createRevealCommand(document));
        return codeLens;
    }

    private Command createRevealCommand(final IndexableDocument document) {
        final Command command = new Command();
        command.setCommand(REVEAL_DOCUMENT_COMMAND);
        command.setTitle(REVEAL_TITLE);
        command.setArguments(asList(getDocumentType(document), document.getSHORTNAME()));
        return command;
    }

}
