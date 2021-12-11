package com.javahacks.odx.lsp.features;

import com.javahacks.odx.index.ElementWithParams;
import com.javahacks.odx.lsp.VirtualDocument;
import com.javahacks.odx.model.*;
import org.eclipse.lsp4j.Hover;
import org.eclipse.lsp4j.MarkupContent;
import org.eclipse.lsp4j.Position;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Optional;

import static com.javahacks.odx.lsp.features.HoverProvider.SHORT_NAME_MAX_LENGTH;
import static com.javahacks.odx.model.TestHelper.createStartEndTagLocation;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class HoverProviderTest {

    private  final  HoverProvider hoverProvider = new HoverProvider();

    @Test
    void paramHoverIsCreated() {
        final Optional<Hover> hover = hoverProvider.createHover(createDocument(), new Position(11, 0));
        final MarkupContent content = hover.get().getContents().getRight();

        assertThat(content.getKind()).isEqualTo("markdown");
        assertThat(content.getValue()).contains("["+TestHelper.SHORT_NAME+"]");
    }

    @Test
    void shortNameIsLimited() {
        final Optional<Hover> hover = hoverProvider.createHover(createDocument(), new Position(13, 0));
        final MarkupContent content = hover.get().getContents().getRight();

        System.out.println(content.getValue());
        assertThat(content.getValue()).contains("["+createMaxLengthString(SHORT_NAME_MAX_LENGTH)+" ...]");
    }

    @Test
    void odxLinkCreatesEmptyHover() {
        final Optional<Hover> hover = hoverProvider.createHover(createDocument(), new Position(12, 0));
        final MarkupContent content = hover.get().getContents().getRight();

        assertThat(content.getValue()).isEmpty();
    }


    @Test
    void noHoverIsCreatedOutsideLocation() {
        final Optional<Hover> hover = hoverProvider.createHover(createDocument(), new Position(1, 0));

        assertThat(hover).isEmpty();
    }

    private VirtualDocument createDocument() {
        final VirtualDocument virtualDocument= mock(VirtualDocument.class);
        final DIAGLAYERCONTAINER diaglayercontainer = new DIAGLAYERCONTAINER();
        diaglayercontainer.getLocationAwares().add(createTestModel());
        diaglayercontainer.getLocationAwares().add(createDummyLink());
        when(virtualDocument.getVirtualModel()).thenReturn(diaglayercontainer);
        return virtualDocument;
    }

    private ODXLINK createDummyLink() {
        final ODXLINK odxlink =new ODXLINK();
        odxlink.setLocation(createStartEndTagLocation(12,0,12,20));
        return odxlink;
    }

    private ElementWithParams createTestModel(){
        final ElementWithParams request = new REQUEST();
        request.setLocation(createStartEndTagLocation(10,0,20,20));

        final CODEDCONST codedconst = new CODEDCONST();
        codedconst.setSHORTNAME(TestHelper.SHORT_NAME);
        codedconst.setLocation(createStartEndTagLocation(11,0,11,20));
        request.getParams().add(codedconst);

        final VALUE value = new VALUE();

        value.setSHORTNAME(createMaxLengthString(SHORT_NAME_MAX_LENGTH+1));
        value.setLocation(createStartEndTagLocation(13,0,13,20));
        request.getParams().add(value);

        return request;
    }

    private String createMaxLengthString(final int leght) {
        final char[] chars = new char[leght];
        Arrays.fill(chars,'a');
        return new String(chars);
    }

}