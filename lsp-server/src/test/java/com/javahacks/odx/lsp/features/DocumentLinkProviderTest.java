package com.javahacks.odx.lsp.features;

import com.javahacks.odx.index.AbstractLinkTarget;
import com.javahacks.odx.index.Category;
import com.javahacks.odx.index.DocumentIndex;
import com.javahacks.odx.index.XmlElementLocation;
import com.javahacks.odx.lsp.VirtualDocument;
import com.javahacks.odx.model.*;
import org.assertj.core.api.Assertions;
import org.eclipse.lsp4j.DocumentLink;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.net.URI;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class DocumentLinkProviderTest {

    private final VirtualDocument virtualDocument = mock(VirtualDocument.class);
    private final DocumentLinkProvider linkProvider = new DocumentLinkProvider();
    private final DocumentIndex index = mock(DocumentIndex.class);


    @Test
    void name() {
        final Category testModel = createTestModel();
        when(virtualDocument.getVirtualModel()).thenReturn(testModel);
        final List<DocumentLink> links = linkProvider.getDocumentLinks(virtualDocument);
        assertThat(links).hasSize(1);
    }

    private Category createTestModel() {
        final DIAGLAYERCONTAINER container = new DIAGLAYERCONTAINER();
        final BASEVARIANT baseVariant = TestHelper.createBaseVariant();
        baseVariant.setLocation(createTestLocation());
        final ECUVARIANT ecuVariant = TestHelper.createEcuVariant();

        final ODXLINK odxlink = TestHelper.createOdxLInk(DOCTYPE.LAYER, baseVariant.getSHORTNAME(), baseVariant.getID());
        odxlink.setDocument(ecuVariant);
        odxlink.setLocation(createTestLocation());
        ecuVariant.setIndex(index);
        container.getLocationAwares().add(odxlink);

        when(index.resolveLink(odxlink, AbstractLinkTarget.class)).thenReturn(Optional.of(baseVariant));

        container.getBaseVariants().add(baseVariant);
        container.getEcuVariants().add(ecuVariant);

        return container;
    }

    private XmlElementLocation createTestLocation() {
        final XmlElementLocation xmlElementLocation = new XmlElementLocation();
        xmlElementLocation.setFileUri(URI.create("/media/dev"));
        return xmlElementLocation;
    }
}