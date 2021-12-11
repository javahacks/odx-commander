package com.javahacks.odx.index;

import com.javahacks.odx.model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

class LayerQueryTest {

    private final DocumentIndex index = Mockito.mock(DocumentIndex.class);
    private final BASEVARIANT baseVariant = TestHelper.createBaseVariant();
    private final ECUVARIANT ecuVariant = TestHelper.createEcuVariant();
    private final ECUSHAREDDATA sharedData = TestHelper.createSharedData();

    @BeforeEach
    void setup() {
        when(index.resolveLink(Mockito.any(), Mockito.any())).thenReturn(Optional.of(baseVariant));
        TestHelper.toLayer(ecuVariant).getImports().add(sharedData);
        ecuVariant.setIndex(index);
    }

    @Test
    void serviceByNameIsFound() {
        final DIAGSERVICE service = TestHelper.newService("sn1");
        TestHelper.toLayer(ecuVariant).getValueInheritedElements().add(service);

        final LayerQuery query = new LayerQuery(ecuVariant);
        assertThat(query.diagCommByShortName(service.getSHORTNAME(), false)).isNotNull();
    }


    @Test
    void linkedServiceIsFound() {
        final DIAGSERVICE service = TestHelper.newService("serviceName");
        sharedData.getDiagServices().add(service);

        final ODXLINK odxLink = TestHelper.createOdxLInk(DOCTYPE.LAYER, service.getSHORTNAME(), service.getID());
        ecuVariant.getDiagServicesSnRefs().add(odxLink);
        when(index.resolveLink(odxLink, DIAGSERVICE.class)).thenReturn(Optional.of(service));

        final LayerQuery query = new LayerQuery(ecuVariant);
        assertThat(query.diagCommByShortName(service.getSHORTNAME(), false)).isNotNull();
    }


    @Test
    void serviceByNameIsNotFound() {
        final DIAGSERVICE service = TestHelper.newService("sn1");
        TestHelper.toLayer(ecuVariant).getValueInheritedElements().add(service);

        final LayerQuery query = new LayerQuery(ecuVariant);
        assertThat(query.diagCommByShortName("xxx", false)).isEmpty();
    }

    @Test
    void importAndParentServicesAreFound() {

        final DIAGSERVICE ev = TestHelper.newService("sn1");
        final DIAGSERVICE bv = TestHelper.newService("sn2");
        final DIAGSERVICE es = TestHelper.newService("sn3");

        TestHelper.toLayer(baseVariant).getValueInheritedElements().add(bv);
        TestHelper.toLayer(ecuVariant).getValueInheritedElements().add(ev);
        TestHelper.toLayer(sharedData).getValueInheritedElements().add(es);

        ecuVariant.getParentRefs().add(TestHelper.fillLinkTarget(baseVariant, new BASEVARIANTREF()));
        ecuVariant.getImportRefs().add(TestHelper.fillLinkTarget(sharedData, new ECUSHAREDDATAREF()));


        final LayerQuery query = new LayerQuery(ecuVariant);
        assertThat(query.visibleDiagComms()).containsExactlyInAnyOrder(ev, bv);

    }

    @Test
    void serviceInEvOverwritesBvAndEs() {
        final DIAGSERVICE ev = TestHelper.newService("sn1");
        final DIAGSERVICE bv = TestHelper.newService("sn1");
        final DIAGSERVICE es = TestHelper.newService("sn1");

        TestHelper.toLayer(baseVariant).getValueInheritedElements().add(bv);
        TestHelper.toLayer(ecuVariant).getValueInheritedElements().add(ev);
        TestHelper.toLayer(sharedData).getValueInheritedElements().add(es);

        ecuVariant.getParentRefs().add(TestHelper.fillLinkTarget(baseVariant, new BASEVARIANTREF()));
        ecuVariant.getImportRefs().add(TestHelper.fillLinkTarget(sharedData, new ECUSHAREDDATAREF()));

        final LayerQuery query = new LayerQuery(ecuVariant);
        assertThat(query.visibleDiagComms()).containsExactlyInAnyOrder(ev);

    }

    @Test
    void serviceInEsOverwritesBv() {
        final DIAGSERVICE ev = TestHelper.newService("sn1");
        final DIAGSERVICE bv = TestHelper.newService("sn2");
        final DIAGSERVICE es = TestHelper.newService("sn2");

        TestHelper.toLayer(baseVariant).getValueInheritedElements().add(bv);
        TestHelper.toLayer(ecuVariant).getValueInheritedElements().add(ev);
        TestHelper.toLayer(sharedData).getValueInheritedElements().add(es);

        ecuVariant.getParentRefs().add(TestHelper.fillLinkTarget(baseVariant, new BASEVARIANTREF()));
        ecuVariant.getImportRefs().add(TestHelper.fillLinkTarget(sharedData, new ECUSHAREDDATAREF()));

        final LayerQuery query = new LayerQuery(ecuVariant);
        assertThat(query.visibleDiagComms()).containsExactlyInAnyOrder(ev, bv);

    }

    @Test
    void importAndParentDopsAreFound() {

        final DATAOBJECTPROP dopev = TestHelper.newDop("sn1");
        final DATAOBJECTPROP dopbv = TestHelper.newDop("sn2");
        final DATAOBJECTPROP dopsd = TestHelper.newDop("sn3");

        TestHelper.toLayer(baseVariant).getValueInheritedElements().add(dopbv);
        TestHelper.toLayer(ecuVariant).getValueInheritedElements().add(dopev);
        TestHelper.toLayer(sharedData).getValueInheritedElements().add(dopsd);

        ecuVariant.getParentRefs().add(TestHelper.fillLinkTarget(baseVariant, new BASEVARIANTREF()));
        ecuVariant.getImportRefs().add(TestHelper.fillLinkTarget(sharedData, new ECUSHAREDDATAREF()));


        final LayerQuery query = new LayerQuery(ecuVariant);
        assertThat(query.visibleDops()).containsExactlyInAnyOrder(dopev, dopbv);

    }


    @Test
    void dopInEvOverwritesBvAndEs() {
        final DATAOBJECTPROP dopev = TestHelper.newDop("sn1");
        final DATAOBJECTPROP dopbv = TestHelper.newDop("sn1");
        final DATAOBJECTPROP dopsd = TestHelper.newDop("sn1");

        TestHelper.toLayer(baseVariant).getValueInheritedElements().add(dopbv);
        TestHelper.toLayer(ecuVariant).getValueInheritedElements().add(dopev);
        TestHelper.toLayer(sharedData).getValueInheritedElements().add(dopsd);

        ecuVariant.getParentRefs().add(TestHelper.fillLinkTarget(baseVariant, new BASEVARIANTREF()));
        ecuVariant.getImportRefs().add(TestHelper.fillLinkTarget(sharedData, new ECUSHAREDDATAREF()));


        final LayerQuery query = new LayerQuery(ecuVariant);
        assertThat(query.visibleDops()).containsExactly(dopev);

    }

    @Test
    void dopInEsNotOverwritesBv() {
        final DATAOBJECTPROP dopev = TestHelper.newDop("sn1");
        final DATAOBJECTPROP dopbv = TestHelper.newDop("sn2");
        final DATAOBJECTPROP dopsd = TestHelper.newDop("sn2");

        TestHelper.toLayer(baseVariant).getValueInheritedElements().add(dopbv);
        TestHelper.toLayer(ecuVariant).getValueInheritedElements().add(dopev);
        TestHelper.toLayer(sharedData).getValueInheritedElements().add(dopsd);

        ecuVariant.getParentRefs().add(TestHelper.fillLinkTarget(baseVariant, new BASEVARIANTREF()));
        ecuVariant.getImportRefs().add(TestHelper.fillLinkTarget(sharedData, new ECUSHAREDDATAREF()));

        final LayerQuery query = new LayerQuery(ecuVariant);
        assertThat(query.visibleDops()).containsExactlyInAnyOrder(dopev, dopbv);

    }

    @Test
    void dopByNameIsFound() {
        final DATAOBJECTPROP dop = TestHelper.newDop("sn1");
        TestHelper.toLayer(ecuVariant).getValueInheritedElements().add(dop);

        final LayerQuery query = new LayerQuery(ecuVariant);
        assertThat(query.dopByShortName(dop.getSHORTNAME())).isNotNull();

    }

    @Test
    void dopByNameIsNotFound() {
        final DATAOBJECTPROP dop = TestHelper.newDop("sn1");
        TestHelper.toLayer(ecuVariant).getValueInheritedElements().add(dop);

        final LayerQuery query = new LayerQuery(ecuVariant);
        assertThat(query.dopByShortName("sn2")).isEmpty();

    }


    @Test
    void parentServiceIsNotInherited() {
        final DIAGSERVICE ev = TestHelper.newService("sn1");
        final DIAGSERVICE bv = TestHelper.newService("sn2");

        TestHelper.toLayer(baseVariant).getValueInheritedElements().add(bv);
        TestHelper.toLayer(ecuVariant).getValueInheritedElements().add(ev);

        final BASEVARIANTREF linkTarget = TestHelper.fillLinkTarget(baseVariant, new BASEVARIANTREF());
        final NOTINHERITEDDIAGCOMM notInherited = new NOTINHERITEDDIAGCOMM();
        notInherited.setDIAGCOMMSNREF(TestHelper.snRef("sn2"));
        linkTarget.getNotinheriteddiagcomms().add(notInherited);
        ecuVariant.getParentRefs().add(linkTarget);

        final LayerQuery query = new LayerQuery(ecuVariant);
        assertThat(query.visibleDiagComms()).containsExactlyInAnyOrder(ev);
    }

    @Test
    void parentServiceIsNotInheritedButCouldBeFoundByShortName() {
        final DIAGSERVICE ev = TestHelper.newService("sn1");
        final DIAGSERVICE bv = TestHelper.newService("sn2");

        TestHelper.toLayer(baseVariant).getValueInheritedElements().add(bv);
        TestHelper.toLayer(ecuVariant).getValueInheritedElements().add(ev);

        final BASEVARIANTREF linkTarget = TestHelper.fillLinkTarget(baseVariant, new BASEVARIANTREF());
        final NOTINHERITEDDIAGCOMM notInherited = new NOTINHERITEDDIAGCOMM();
        notInherited.setDIAGCOMMSNREF(TestHelper.snRef("sn2"));
        linkTarget.getNotinheriteddiagcomms().add(notInherited);
        ecuVariant.getParentRefs().add(linkTarget);

        final LayerQuery query = new LayerQuery(ecuVariant);
        assertThat(query.diagCommByShortName(notInherited.getDIAGCOMMSNREF().getSHORTNAME(), true)).isPresent();
        assertThat(query.diagCommByShortName(notInherited.getDIAGCOMMSNREF().getSHORTNAME(), false)).isNotPresent();
    }

    @Test
    void parentDopIsNotInherited() {
        final DATAOBJECTPROP ev = TestHelper.newDop("sn1");
        final DATAOBJECTPROP bv = TestHelper.newDop("sn2");

        TestHelper.toLayer(baseVariant).getValueInheritedElements().add(bv);
        TestHelper.toLayer(ecuVariant).getValueInheritedElements().add(ev);

        final BASEVARIANTREF linkTarget = TestHelper.fillLinkTarget(baseVariant, new BASEVARIANTREF());
        final NOTINHERITEDDOP notInherited = new NOTINHERITEDDOP();
        notInherited.setDOPBASESNREF(TestHelper.snRef("sn2"));
        linkTarget.getNotinheriteddops().add(notInherited);
        ecuVariant.getParentRefs().add(linkTarget);

        final LayerQuery query = new LayerQuery(ecuVariant);
        assertThat(query.visibleDops()).containsExactlyInAnyOrder(ev);
    }


    @Test
    void tableIsFound() {
        final TABLE table = new TABLE();
        table.setSHORTNAME(TestHelper.randomId());
        TestHelper.toLayer(ecuVariant).getValueInheritedElements().add(table);

        final LayerQuery query = new LayerQuery(ecuVariant);
        assertThat(query.visibleTables()).containsExactly(table);
    }

    @Test
    void parentTableIsOverridden() {
        final TABLE tableBv = new TABLE();
        tableBv.setSHORTNAME(TestHelper.SHORT_NAME);
        final TABLE tableEv = new TABLE();
        tableEv.setSHORTNAME(TestHelper.SHORT_NAME);

        TestHelper.toLayer(baseVariant).getValueInheritedElements().add(tableBv);
        TestHelper.toLayer(ecuVariant).getValueInheritedElements().add(tableEv);

        ecuVariant.getParentRefs().add(TestHelper.fillLinkTarget(baseVariant, new BASEVARIANTREF()));
        ecuVariant.getImportRefs().add(TestHelper.fillLinkTarget(sharedData, new ECUSHAREDDATAREF()));

        final LayerQuery query = new LayerQuery(ecuVariant);
        assertThat(query.visibleTables()).containsExactly(tableEv);
    }

}