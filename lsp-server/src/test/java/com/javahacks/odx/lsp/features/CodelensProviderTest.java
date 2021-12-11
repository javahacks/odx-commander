package com.javahacks.odx.lsp.features;

import com.javahacks.odx.lsp.VirtualDocument;
import com.javahacks.odx.lsp.mapper.Types;
import com.javahacks.odx.model.DIAGLAYERCONTAINER;
import com.javahacks.odx.model.ECUVARIANT;
import com.javahacks.odx.model.TestHelper;
import com.javahacks.odx.utils.OdxUtils;
import org.eclipse.lsp4j.CodeLens;
import org.junit.jupiter.api.Test;

import java.util.List;

import static com.javahacks.odx.utils.OdxUtils.locationToRange;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class CodelensProviderTest {

    private final CodelensProvider codelensProvider = new CodelensProvider();
    private final VirtualDocument virtualDocument = mock(VirtualDocument.class);


    @Test
    void missingModelYieldsEmptyList() {
        final List<CodeLens> result = codelensProvider.createCodeLens(virtualDocument);
        assertThat(result).isEmpty();
    }

    @Test
    void codeLensForVariantIsProvided() {
        final DIAGLAYERCONTAINER diaglayercontainer = new DIAGLAYERCONTAINER();
        diaglayercontainer.getLocationAwares().add(createVariant());
        when(virtualDocument.getVirtualModel()).thenReturn(diaglayercontainer);
        final List<CodeLens> result = codelensProvider.createCodeLens(virtualDocument);
        assertThat(result).hasSize(1);
    }

    @Test
    void containerIsFiltered() {
        final DIAGLAYERCONTAINER diaglayercontainer = new DIAGLAYERCONTAINER();
        diaglayercontainer.getLocationAwares().add(createVariant());
        diaglayercontainer.getLocationAwares().add(diaglayercontainer);
        when(virtualDocument.getVirtualModel()).thenReturn(diaglayercontainer);

        final List<CodeLens> result = codelensProvider.createCodeLens(virtualDocument);
        assertThat(result).hasSize(1);
    }

    @Test
    void multipleCodeLensesAreProvided() {
        final DIAGLAYERCONTAINER diaglayercontainer = new DIAGLAYERCONTAINER();
        diaglayercontainer.getLocationAwares().add(createVariant());
        diaglayercontainer.getLocationAwares().add(createVariant());
        when(virtualDocument.getVirtualModel()).thenReturn(diaglayercontainer);
        final List<CodeLens> result = codelensProvider.createCodeLens(virtualDocument);
        assertThat(result).hasSize(2);
    }

    @Test
    void codeLensIsProperlyFilled() {
        final DIAGLAYERCONTAINER diaglayercontainer = new DIAGLAYERCONTAINER();
        final ECUVARIANT variant = createVariant();
        diaglayercontainer.getLocationAwares().add(variant);
        when(virtualDocument.getVirtualModel()).thenReturn(diaglayercontainer);

        final CodeLens codeLens = codelensProvider.createCodeLens(virtualDocument).get(0);
        assertThat(codeLens.getCommand().getCommand()).isEqualTo(CodelensProvider.REVEAL_DOCUMENT_COMMAND);
        assertThat(codeLens.getCommand().getTitle()).isEqualTo(CodelensProvider.REVEAL_TITLE);
        assertThat(codeLens.getCommand().getArguments()).contains(variant.getSHORTNAME(), Types.getDocumentType(variant));
        assertThat(codeLens.getRange()).isEqualTo(locationToRange(variant.getLocation()));
    }

    private ECUVARIANT createVariant() {
        final ECUVARIANT ecuVariant = TestHelper.createEcuVariant();
        ecuVariant.setLocation(TestHelper.createStartEndTagLocation(0, 0, 1, 10));
        return ecuVariant;
    }
}