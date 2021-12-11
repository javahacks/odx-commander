package com.javahacks.odx.lsp.features;

import com.javahacks.odx.index.XmlElementLocation;
import com.javahacks.odx.lsp.VirtualDocument;
import com.javahacks.odx.model.DESCRIPTION;
import com.javahacks.odx.model.DIAGLAYERCONTAINER;
import com.javahacks.odx.model.REQUEST;
import com.javahacks.odx.model.VEHICLETYPE;
import org.assertj.core.api.Assertions;
import org.eclipse.lsp4j.FoldingRange;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class FoldingProviderTest {
    private final VirtualDocument document = mock(VirtualDocument.class);
    private final FoldingProvider provider = new FoldingProvider();

    @Test
    void commentRangesAreCreated(){
        final DIAGLAYERCONTAINER diaglayercontainer = createContainer();
        final DESCRIPTION description = new DESCRIPTION();
        description.setLocation(createLocation());
        diaglayercontainer.getLocationAwares().add(description);

        final List<FoldingRange> ranges = provider.createFoldingRanges(document);
        assertThat(ranges).hasSize(1);
        assertThat(ranges.get(0).getKind()).isEqualTo(FoldingProvider.COMMENT_KIND);
    }

    @Test
    void regionRangesAreCreated(){
        final DIAGLAYERCONTAINER diaglayercontainer = createContainer();
        final REQUEST request = new REQUEST();
        request.setLocation(createLocation());
        diaglayercontainer.getLocationAwares().add(request);

        final List<FoldingRange> ranges = provider.createFoldingRanges(document);
        assertThat(ranges).hasSize(1);
        assertThat(ranges.get(0).getKind()).isEqualTo(FoldingProvider.REGION_KIND);

    }

    @Test
    void emptyRegionsAreHandled(){
        final DIAGLAYERCONTAINER diaglayercontainer = createContainer();
        final DIAGLAYERCONTAINER container = new DIAGLAYERCONTAINER();
        container.setLocation(createLocation());
        diaglayercontainer.getLocationAwares().add(container);

        final List<FoldingRange> ranges = provider.createFoldingRanges(document);
        assertThat(ranges).isEmpty();
    }


    private DIAGLAYERCONTAINER createContainer() {
        final DIAGLAYERCONTAINER diaglayercontainer = new DIAGLAYERCONTAINER();
        when(document.getVirtualModel()).thenReturn(diaglayercontainer);
        return diaglayercontainer;
    }

    private XmlElementLocation createLocation() {
        final XmlElementLocation location = new XmlElementLocation();
        location.setStartLine(1);
        location.setStartColumn(1);
        location.setEndLine(10);
        location.setEndColumn(10);
        return location;
    }


}