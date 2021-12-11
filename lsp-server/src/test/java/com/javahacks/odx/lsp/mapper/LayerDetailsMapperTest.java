package com.javahacks.odx.lsp.mapper;

import com.javahacks.odx.index.DocumentIndex;
import com.javahacks.odx.index.LayerProxy;
import com.javahacks.odx.index.ProxyRef;
import com.javahacks.odx.lsp.dtos.DiagnosticElement;
import com.javahacks.odx.lsp.dtos.Link;
import com.javahacks.odx.model.*;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.List;
import java.util.Optional;

import static com.javahacks.odx.model.TestHelper.*;
import static java.util.Arrays.asList;
import static java.util.Collections.sort;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

class LayerDetailsMapperTest {

    private final DocumentIndex index = Mockito.mock(DocumentIndex.class);
    private final LayerDetailsMapper mapper = new LayerDetailsMapper(index, new Link());

    @Test
    void importDetailsAreMapped() {
        final ECUVARIANT variant = createModel(true, false, false);
        when(index.findObjectInDocument(any(), any(), any(), any())).thenReturn(Optional.of(variant));

        final DiagnosticElement element = mapper.map().getDependencies().get(0);

        assertThat(element.getLabel()).isEqualTo("IMPORT - " + variant.getImports().get(0).getSHORTNAME());
        assertThat(element.getName()).isEqualTo(variant.getImports().get(0).getSHORTNAME());

    }

    @Test
    void parentDetailsAreMapped() {
        final ECUVARIANT variant = createModel(false, true, false);
        when(index.findObjectInDocument(any(), any(), any(), any())).thenReturn(Optional.of(variant));

        final DiagnosticElement element = mapper.map().getDependencies().get(0);
        assertThat(element.getLabel()).isEqualTo("PARENT - " + variant.getParents().get(0).getSHORTNAME());
        assertThat(element.getName()).isEqualTo(variant.getParents().get(0).getSHORTNAME());
        assertThat(element.getType()).isEqualTo("base_variant");
        assertThat(element.isRevealable()).isTrue();
    }


    @Test
    void notInheritedDetailsAreMapped() {
        final ECUVARIANT variant = createModel(false, true, false);
        when(index.findObjectInDocument(any(), any(), any(), any())).thenReturn(Optional.of(variant));

        final DiagnosticElement element = mapper.map().getDependencies().get(0);
        assertThat(element.getChildren().get(0).getName()).isEqualTo("DIAG-COMM - "+SHORT_NAME);
        assertThat(element.getChildren().get(0).getType()).isEqualTo(Types.NOT_INHERITED);
        assertThat(element.getChildren().get(1).getName()).isEqualTo("DOP - "+SHORT_NAME);
        assertThat(element.getChildren().get(1).getType()).isEqualTo(Types.NOT_INHERITED);
        assertThat(element.getChildren().get(2).getName()).isEqualTo("GLOBAL NRE - "+SHORT_NAME);
        assertThat(element.getChildren().get(2).getType()).isEqualTo(Types.NOT_INHERITED);
        assertThat(element.getChildren().get(3).getName()).isEqualTo("TABLE - "+SHORT_NAME);
        assertThat(element.getChildren().get(3).getType()).isEqualTo(Types.NOT_INHERITED);
        assertThat(element.getChildren().get(4).getName()).isEqualTo("VARIABLE - "+SHORT_NAME);
        assertThat(element.getChildren().get(4).getType()).isEqualTo(Types.NOT_INHERITED);

    }

    @Test
    void childDetailsAreMapped() {
        final ECUVARIANT variant = createModel(false, false, true);
        when(index.findObjectInDocument(any(), any(), any(), any())).thenReturn(Optional.of(variant));

        final DiagnosticElement element = mapper.map().getDependencies().get(0);

        assertThat(element.getLabel()).isEqualTo("CHILD - " + SHORT_NAME);
        assertThat(element.getName()).isEqualTo(SHORT_NAME);
        assertThat(element.getType()).isEqualTo("test_layer");
        assertThat(element.isRevealable()).isTrue();
    }

    @Test
    void servicesAreSortedBySemantic() {
        final DIAGSERVICE s1 = createService("sn", "c");
        final DIAGSERVICE s2 = createService("sn", "b");
        final DIAGSERVICE s3 = createService("sn", "a");

        final List<DIAGSERVICE> list = asList(s1, s2, s3);
        sort(list, mapper::sortBySemanticThenName);
        assertThat(list).containsExactly(s3, s2, s1);
    }


    @Test
    void servicesAreSortedByName() {
        final DIAGSERVICE s1 = createService("c", "s");
        final DIAGSERVICE s2 = createService("b", "s");
        final DIAGSERVICE s3 = createService("a", "s");

        final List<DIAGSERVICE> list = asList(s1, s2, s3);
        sort(list, mapper::sortBySemanticThenName);
        assertThat(list).containsExactly(s3, s2, s1);
    }

    @Test
    void servicesAreSortedByNameAndSemantic() {
        final DIAGSERVICE s1 = createService("a", "b");
        final DIAGSERVICE s2 = createService("b", "a");
        final DIAGSERVICE s3 = createService("c", "a");

        final List<DIAGSERVICE> list = asList(s1, s2, s3);
        sort(list, mapper::sortBySemanticThenName);
        assertThat(list).containsExactly(s2, s3, s1);

    }

    private DIAGSERVICE createService(final String name, final String semantic) {
        final DIAGSERVICE diagservice = new DIAGSERVICE();
        diagservice.setSHORTNAME(name);
        diagservice.setSEMANTIC(semantic);
        return diagservice;
    }


    private ECUVARIANT createModel(final boolean addImport, final boolean addParent, final boolean addChild) {
        final ECUVARIANT variant = TestHelper.createEcuVariant();
        variant.setIndex(index);
        if (addImport) {
            variant.getImports().add(TestHelper.createEcuVariant());
        }
        if (addParent) {
            prepareParent(variant);
        }
        if (addChild) {
            addChildProxy();
        }
        return variant;
    }

    private void prepareParent(final ECUVARIANT variant) {
        final BASEVARIANT baseVariant = TestHelper.createBaseVariant();
        final BASEVARIANTREF variantRef = new BASEVARIANTREF();
        variantRef.setIDREF(baseVariant.getID());
        variantRef.setDOCTYPE(DOCTYPE.LAYER);
        variantRef.setDOCREF(baseVariant.getSHORTNAME());

        final NOTINHERITEDDIAGCOMM niDiagComm=new NOTINHERITEDDIAGCOMM();
        niDiagComm.setDIAGCOMMSNREF(snRef(SHORT_NAME));
        variantRef.getNotinheriteddiagcomms().add(niDiagComm);
        final NOTINHERITEDDOP niDop = new NOTINHERITEDDOP();
        niDop.setDOPBASESNREF(snRef(SHORT_NAME));
        variantRef.getNotinheriteddops().add(niDop);
        final NOTINHERITEDGLOBALNEGRESPONSE niResponse = new NOTINHERITEDGLOBALNEGRESPONSE();
        niResponse.setGLOBALNEGRESPONSESNREF(snRef(SHORT_NAME));
        variantRef.getNotinheritedglobalnegresponses().add(niResponse);
        final NOTINHERITEDTABLE niTable = new NOTINHERITEDTABLE();
        niTable.setTABLESNREF(snRef(SHORT_NAME));
        variantRef.getNotinheritedtables().add(niTable);
        final NOTINHERITEDVARIABLE niVariable = new NOTINHERITEDVARIABLE();
        niVariable.setDIAGVARIABLESNREF(snRef(SHORT_NAME));
        variantRef.getNotinheritedvariables().add(niVariable);



        variant.getParentRefs().add(variantRef);
        variant.getParents().add(baseVariant);
        when(index.findLayerProxyForOdxLink(variantRef)).thenReturn(Optional.of(createLayerProxy(baseVariant)));
    }

    private void addChildProxy() {
        final LayerProxy layerProxy = new LayerProxy();
        final ProxyRef proxyRef = new ProxyRef();
        proxyRef.setDocType(DOCTYPE.LAYER.name());
        proxyRef.setId(randomId());
        proxyRef.setShortName(randomId());
        layerProxy.getChildren().add(proxyRef);
        when(index.findLayerProxyForOdxLink(any())).thenReturn(Optional.of(layerProxy));

        final LayerProxy childProxy = new LayerProxy();
        childProxy.setId("child_proxy");
        childProxy.setLayerType("test_layer");
        childProxy.setShortName(SHORT_NAME);
        when(index.findLayerProxyForOdxLink(proxyRef.toOdxLink())).thenReturn(Optional.of(childProxy));
    }
}