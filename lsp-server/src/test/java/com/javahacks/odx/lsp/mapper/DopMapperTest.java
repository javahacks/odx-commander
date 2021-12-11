package com.javahacks.odx.lsp.mapper;

import com.javahacks.odx.index.AbstractLinkTarget;
import com.javahacks.odx.index.Category;
import com.javahacks.odx.index.DocumentIndex;
import com.javahacks.odx.lsp.dtos.DiagnosticElement;
import com.javahacks.odx.model.*;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.List;
import java.util.Optional;

import static com.javahacks.odx.model.TestHelper.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class DopMapperTest {
    private final Category container = new DIAGLAYERCONTAINER();
    private final DIAGLAYER diaglayer = TestHelper.createEcuVariant();

    private final ParamMapper paramMapper = new ParamMapper(diaglayer);
    private final DopMapper dopMapper = new DopMapper(paramMapper, diaglayer);
    private final DocumentIndex documentIndex = mock(DocumentIndex.class);


    @BeforeEach
    void setup() {
        diaglayer.setIndex(documentIndex);
        container.getLayers().add(diaglayer);
    }


    @Test
    void muxIsMapped() {
        final DiagnosticElement parent = new DiagnosticElement();
        final MUX dop = new MUX();

        final DiagnosticElement mappedDop = executeLookup(parent, dop);
        Assertions.assertThat(mappedDop.getName()).isEqualTo("MUX");
        Assertions.assertThat(parent.getType()).isEqualTo(Types.COMPLEX);
    }

    @Test
    void envdataIsMapped() {
        final DiagnosticElement parent = new DiagnosticElement();
        final ENVDATA dop = new ENVDATA();

        final DiagnosticElement mappedDop = executeLookup(parent, dop);
        Assertions.assertThat(mappedDop.getName()).isEqualTo("ENVDATA");
        Assertions.assertThat(parent.getType()).isEqualTo(Types.COMPLEX);
    }

    @Test
    void envdataDescIsMapped() {
        final DiagnosticElement parent = new DiagnosticElement();
        final ENVDATADESC dop = new ENVDATADESC();

        final DiagnosticElement mappedDop = executeLookup(parent, dop);
        Assertions.assertThat(mappedDop.getName()).isEqualTo("ENVDATADESC");
        Assertions.assertThat(parent.getType()).isEqualTo(Types.COMPLEX);
    }

    @Test
    void lengthFieldIsMapped() {
        final DiagnosticElement parent = new DiagnosticElement();
        final DYNAMICLENGTHFIELD dop = new DYNAMICLENGTHFIELD();

        final DiagnosticElement mappedDop = executeLookup(parent, dop);
        Assertions.assertThat(mappedDop.getName()).isEqualTo("DYNAMIC LENGTH FIELD");
        Assertions.assertThat(parent.getType()).isEqualTo(Types.COMPLEX);
    }

    @Test
    void endMarkerFieldIsMapped() {
        final DiagnosticElement parent = new DiagnosticElement();
        final DYNAMICENDMARKERFIELD dop = new DYNAMICENDMARKERFIELD();

        final DiagnosticElement mappedDop = executeLookup(parent, dop);
        Assertions.assertThat(mappedDop.getName()).isEqualTo("DYNAMIC ENDMARKER FIELD");
        Assertions.assertThat(parent.getType()).isEqualTo(Types.COMPLEX);
    }

    @Test
    void endOdPduFieldIsMapped() {
        final DiagnosticElement parent = new DiagnosticElement();
        final ENDOFPDUFIELD dop = new ENDOFPDUFIELD();
        dop.setMAXNUMBEROFITEMS(10l);
        dop.setMINNUMBEROFITEMS(1l);

        final DiagnosticElement mappedDop = executeLookup(parent, dop);
        Assertions.assertThat(mappedDop.getName()).isEqualTo("END OF PDU FIELD (MIN: 1 MAX: 10)");
        Assertions.assertThat(parent.getType()).isEqualTo(Types.COMPLEX);
    }

    @Test
    void staticFieldIsMapped() {
        final DiagnosticElement parent = new DiagnosticElement();
        final STATICFIELD dop = new STATICFIELD();
        dop.setFIXEDNUMBEROFITEMS(10l);
        dop.setITEMBYTESIZE(10l);

        final DiagnosticElement mappedDop = executeLookup(parent, dop);
        Assertions.assertThat(mappedDop.getName()).isEqualTo("STATIC FIELD (NUMBER-OF-ITEMS: 10 BYTE-SIZE: 10)");
        Assertions.assertThat(parent.getType()).isEqualTo(Types.COMPLEX);
    }

    @Test
    void structureIsMapped() {
        final DiagnosticElement parent = new DiagnosticElement();
        final STRUCTURE dop = new STRUCTURE();

        final DiagnosticElement mappedDop = executeLookup(parent, dop);
        Assertions.assertThat(mappedDop.getName()).isEqualTo("STRUCTURE");
        Assertions.assertThat(parent.getType()).isEqualTo(Types.COMPLEX);
    }

    @Test
    void shortNameIsMappedForSimpleDop() {
        final DiagnosticElement parent = new DiagnosticElement();
        final DATAOBJECTPROP dop = new DATAOBJECTPROP();
        dop.setSHORTNAME(SHORT_NAME);

        final DiagnosticElement mappedDop = executeLookup(parent, dop);
        Assertions.assertThat(mappedDop.getName()).isEqualTo(SHORT_NAME + " [UNIT]");
        Assertions.assertThat(parent.getType()).isEqualTo(Types.UNKNOWN);
    }

    @Test
    void compuCodeIsMappedForSimpleDop() {
        final DATAOBJECTPROP dop = new DATAOBJECTPROP();
        dop.setSHORTNAME(SHORT_NAME);

        final COMPUMETHOD compumethod = new COMPUMETHOD();
        compumethod.setCATEGORY(COMPUCATEGORY.LINEAR);
        dop.setCOMPUMETHOD(compumethod);

        final PHYSICALTYPE physicaltype = new PHYSICALTYPE();
        physicaltype.setBASEDATATYPE(PHYSICALDATATYPE.A_BYTEFIELD);

        dop.setPHYSICALTYPE(physicaltype);

        final DiagnosticElement parent = new DiagnosticElement();
        final DiagnosticElement mappedDop = executeLookup(parent, dop);
        Assertions.assertThat(mappedDop.getName()).isEqualTo(COMPUCATEGORY.LINEAR.name() + " [UNIT]");
        Assertions.assertThat(parent.getType()).isEqualTo(Types.BYTE_FIELD);

    }

    @Test
    void tableKeyIsMappedWithSnRef() {
        final TABLE table = createTestTable();

        final TABLEKEY key = new TABLEKEY();
        key.setSHORTNAME(SHORT_NAME);

        final SNREF snRef = new SNREF();
        snRef.setSHORTNAME(table.getSHORTNAME());
        key.getRest().add(createJaxbLink(snRef, "TABLE-SNREF"));

        final List<DiagnosticElement> elements = dopMapper.mapTableRowsForTableKey(key);
        Assertions.assertThat(elements).isNotEmpty();
    }


    @Test
    void tableKeyIsMappedWithOdxLink() {
        final TABLE table = createTestTable();
        final TABLEKEY key = new TABLEKEY();
        key.setSHORTNAME(SHORT_NAME);

        final ODXLINK snRef = new ODXLINK();
        snRef.setIDREF(table.getID());

        key.getRest().add(createJaxbLink(snRef, "TABLE-REF"));

        when(documentIndex.resolveLink(snRef, TABLE.class)).thenReturn(Optional.of(table));

        final List<DiagnosticElement> elements = dopMapper.mapTableRowsForTableKey(key);
        Assertions.assertThat(elements).isNotEmpty();
    }

    @Test
    void tableKeyIsMappedWithRowOdxLink() {
        final TABLE table = createTestTable();
        final TABLEKEY key = new TABLEKEY();
        key.setSHORTNAME(SHORT_NAME);

        final ODXLINK link = (ODXLINK) table.getROWWRAPPER().get(1);
        key.getRest().add(createJaxbLink(link, "TABLE-ROW-REF"));
        when(documentIndex.resolveLink(link, TABLE.class)).thenReturn(Optional.of(new TABLE()));

        final List<DiagnosticElement> elements = dopMapper.mapTableRowsForTableKey(key);
        Assertions.assertThat(elements).isNotEmpty();
    }

    @Test
    void tableKeyIsMappedWithRowSNRef() {
        final TABLE table = createTestTable();
        final TABLEKEY key = new TABLEKEY();
        key.setSHORTNAME(SHORT_NAME);

        final SNREF snRef = new SNREF();
        snRef.setSHORTNAME(SHORT_NAME);
        key.getRest().add(createJaxbLink(snRef, "TABLE-ROW-SNREF"));

        final List<DiagnosticElement> elements = dopMapper.mapTableRowsForTableKey(key);
        Assertions.assertThat(elements).isNotEmpty();
    }


    @Test
    void dtcDopIsMapped() {
        final DiagnosticElement parent = new DiagnosticElement();
        final DTCDOP dop = new DTCDOP();
        dop.setSHORTNAME(SHORT_NAME);

        final DiagnosticElement mappedDop = executeLookup(parent, dop);
        Assertions.assertThat(mappedDop.getName()).isEqualTo("DTC-DOP (0)");
        Assertions.assertThat(parent.getType()).isEqualTo(Types.DTC);
    }

    private DiagnosticElement executeLookup(final DiagnosticElement parent, final DOPBASE dop) {
        dopMapper.lookupAndMapDop(parent, null, attachDopToLayer(dop));
        if (parent.getChildren().isEmpty()) {
            return null;
        }
        return parent.getChildren().get(parent.getChildren().size() - 1);
    }

    private ODXLINK attachDopToLayer(final DOPBASE dop) {
        dop.setLayer(diaglayer);

        final ODXLINK odxLInk = TestHelper.createOdxLInk(DOCTYPE.LAYER, diaglayer.getSHORTNAME(), dop.getID());
        odxLInk.setDocument(diaglayer);

        final DocumentIndex index = mock(DocumentIndex.class);
        diaglayer.setIndex(index);
        when(index.resolveLink(odxLInk, AbstractLinkTarget.class)).thenReturn(Optional.of(dop));

        if (dop instanceof DATAOBJECTPROP) {
            attachUnit((DATAOBJECTPROP) dop);
            diaglayer.getDIAGDATADICTIONARYSPEC().getDataobjectprops().add((DATAOBJECTPROP) dop);
        }
        if (dop instanceof DTCDOP) {
            diaglayer.getDIAGDATADICTIONARYSPEC().getDtcdops().add((DTCDOP) dop);
        }

        return odxLInk;
    }

    private void attachUnit(final DATAOBJECTPROP dop) {
        final UNIT unit = new UNIT();
        unit.setDISPLAYNAME("UNIT");
        unit.setSHORTNAME(SHORT_NAME);
        unit.setID("unit-id");

        final ODXLINK unitLink = TestHelper.createOdxLInk(DOCTYPE.LAYER, diaglayer.getSHORTNAME(), unit.getID());
        dop.setUNITREF(unitLink);
        when(diaglayer.getIndex().resolveLink(unitLink, UNIT.class)).thenReturn(Optional.of(unit));
    }


    private TABLE createTestTable() {
        final TABLE table = new TABLE();
        table.setSHORTNAME(SHORT_NAME);
        final TABLEROW row = new TABLEROW();
        row.setID(TABLE_ROW_ID);
        row.setSHORTNAME(SHORT_NAME);

        final ODXLINK rowLink = TestHelper.createOdxLInk(DOCTYPE.LAYER, row.getSHORTNAME(), row.getID());

        table.getROWWRAPPER().add(row);
        table.getROWWRAPPER().add(rowLink);
        diaglayer.getDIAGDATADICTIONARYSPEC().getTables().add(table);
        diaglayer.getValueInheritedElements().add(table);

        when(documentIndex.resolveLink(rowLink, TABLEROW.class)).thenReturn(Optional.of(row));

        return table;
    }

}