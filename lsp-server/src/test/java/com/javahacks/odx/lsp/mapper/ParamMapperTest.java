package com.javahacks.odx.lsp.mapper;

import com.javahacks.odx.index.AbstractLinkTarget;
import com.javahacks.odx.index.DocumentIndex;
import com.javahacks.odx.index.LayerQuery;
import com.javahacks.odx.lsp.dtos.DiagnosticElement;
import com.javahacks.odx.model.*;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static com.javahacks.odx.model.TestHelper.*;
import static java.util.Collections.singletonList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class ParamMapperTest {

    private final DIAGLAYER diaglayer = TestHelper.createEcuVariant();
    private final ParamMapper paramMapper = new ParamMapper(diaglayer);

    @Test
    void invalidDopReferenceIsMapped() {
        final VALUE param = new VALUE();
        param.setSHORTNAME(SHORT_NAME);

        final DiagnosticElement element = paramMapper.mapParams(singletonList(param)).get(0);

        assertThat(element.getName()).isEqualTo(Types.INVALID);
        assertThat(element.getType()).isEqualTo(Types.INVALID);
    }

    @Test
    void valueIsMapped() {
        final VALUE param = new VALUE();
        param.setSHORTNAME(SHORT_NAME);
        param.setDOPREF(attachDopToLayer(newDop("my-dop")));

        final DiagnosticElement element = paramMapper.mapParams(singletonList(param)).get(0);

        assertThat(element.getName()).startsWith("VALUE");
        assertThat(element.getChildren()).hasSize(1);
    }

    @Test
    void reservedIsMapped() {
        final RESERVED param = new RESERVED();
        param.setSHORTNAME(SHORT_NAME);

        final DiagnosticElement element = paramMapper.mapParams(singletonList(param)).get(0);
        assertThat(element.getName()).isEqualTo("RESERVED");
        assertThat(element.getType()).isEqualTo("RESERVED");
    }

    @Test
    void dynamicIsMapped() {
        final DYNAMIC param = new DYNAMIC();
        param.setSHORTNAME(SHORT_NAME);

        final DiagnosticElement element = paramMapper.mapParams(singletonList(param)).get(0);
        assertThat(element.getName()).isEqualTo("DYNAMIC");
    }

    @Test
    void mrpIsMapped() {
        final MATCHINGREQUESTPARAM param = new MATCHINGREQUESTPARAM();
        param.setSHORTNAME(SHORT_NAME);

        final DiagnosticElement element = paramMapper.mapParams(singletonList(param)).get(0);
        assertThat(element.getName()).isEqualTo("MATCHING REQUEST");
    }

    @Test
    void tableKeyIsMapped() {
        final TABLEKEY param = new TABLEKEY();
        param.setSHORTNAME(SHORT_NAME);

        final DiagnosticElement element = paramMapper.mapParams(singletonList(param)).get(0);
        assertThat(element.getName()).isEqualTo("TABLE KEY");
    }

    @Test
    void tableStructIsMapped() {
        final TABLESTRUCT param = new TABLESTRUCT();
        param.setSHORTNAME(SHORT_NAME);
        final DiagnosticElement element = paramMapper.mapParams(singletonList(param)).get(0);
        assertThat(element.getName()).isEqualTo("TABLE STRUCT");
    }

    @Test
    void tableStructChildrenAreMapped() {
        final List<PARAM> params=new ArrayList<>();
        final TABLESTRUCT param = new TABLESTRUCT();
        param.setSHORTNAME(SHORT_NAME);
        params.add(param);

        final SNREF snRef = new SNREF();
        snRef.setSHORTNAME(SHORT_NAME);
        param.setTABLEKEYSNREF(snRef);

        final TABLEKEY tk = new TABLEKEY();
        tk.setSHORTNAME(SHORT_NAME);
        params.add(tk);

        final SNREF snref = new SNREF();
        snref.setSHORTNAME(TestHelper.TABLE_ROW_ID);

        tk.getRest().add(TestHelper.createJaxbLink(snRef, "TABLE-SNREF"));

        final DIAGLAYER layerWithTable = Mockito.spy(diaglayer);
        final LayerQuery layerQuery = mock(LayerQuery.class);
        when(layerQuery.visibleTables()).thenReturn(TestHelper.getTestTable());
        when(layerWithTable.query()).thenReturn(layerQuery);

        final ParamMapper paramMapper = new ParamMapper(layerWithTable);
        final DiagnosticElement element = paramMapper.mapParams(params).get(0);
        assertThat(element.getChildren().size() == 1);
        assertThat(element.getChildren().get(0).getType()).isEqualTo(Types.TABLE_ROW);
    }

    @Test
    void tableEntryIsMapped() {
        final TABLEENTRY param = new TABLEENTRY();
        param.setSHORTNAME(SHORT_NAME);

        final DiagnosticElement element = paramMapper.mapParams(singletonList(param)).get(0);
        assertThat(element.getName()).isEqualTo("TABLE ENTRY");
    }

    @Test
    void codedConstWithIntIsMapped() {
        final CODEDCONST param = new CODEDCONST();
        param.setSHORTNAME(SHORT_NAME);
        final DIAGCODEDTYPE diagcodedtype = new STANDARDLENGTHTYPE();
        diagcodedtype.setBASEDATATYPE(DATATYPE.A_INT_32);
        param.setCODEDVALUE("15");
        param.setDIAGCODEDTYPE(diagcodedtype);

        final DiagnosticElement element = paramMapper.mapParams(singletonList(param)).get(0);
        assertThat(element.getName()).isEqualTo("CODED CONST (0F)");
        assertThat(element.getType()).isEqualTo(Types.NUMBER);
    }

    @Test
    void codedConstWithTextIsMapped() {
        final CODEDCONST param = new CODEDCONST();
        param.setSHORTNAME(SHORT_NAME);
        final DIAGCODEDTYPE diagcodedtype = new STANDARDLENGTHTYPE();
        diagcodedtype.setBASEDATATYPE(DATATYPE.A_ASCIISTRING);
        param.setCODEDVALUE("hello");
        param.setDIAGCODEDTYPE(diagcodedtype);

        final DiagnosticElement element = paramMapper.mapParams(singletonList(param)).get(0);
        assertThat(element.getName()).isEqualTo("CODED CONST (hello)");
    }

    @Test
    void physicalConstIsMapped() {
        final PHYSCONST param = new PHYSCONST();
        param.setSHORTNAME(SHORT_NAME);
        param.setPHYSCONSTANTVALUE("value");
        param.setDOPREF(attachDopToLayer(newDop("my-dop")));

        final DiagnosticElement element = paramMapper.mapParams(singletonList(param)).get(0);
        assertThat(element.getName()).isEqualTo("PHYSICAL CONST (value)");
        assertThat(element.getChildren()).hasSize(1);

    }

    @Test
    void systemIsMapped() {
        final SYSTEM param = new SYSTEM();
        param.setSHORTNAME(SHORT_NAME);
        param.setDOPREF(attachDopToLayer(newDop("my-dop")));

        final DiagnosticElement element = paramMapper.mapParams(singletonList(param)).get(0);
        assertThat(element.getName()).isEqualTo("SYSTEM");
        assertThat(element.getChildren()).hasSize(1);

    }

    @Test
    void lengthKeyIsMapped() {
        final LENGTHKEY param = new LENGTHKEY();
        param.setSHORTNAME(SHORT_NAME);
        param.setDOPREF(attachDopToLayer(newDop("my-dop")));

        final DiagnosticElement element = paramMapper.mapParams(singletonList(param)).get(0);
        assertThat(element.getName()).isEqualTo("LENGTH KEY");
        assertThat(element.getChildren()).hasSize(1);

    }

    @Test
    void nrcConstIsMapped() {
        final NRCCONST param = new NRCCONST();
        param.setSHORTNAME(SHORT_NAME);

        final DIAGCODEDTYPE diagcodedtype = new STANDARDLENGTHTYPE();
        diagcodedtype.setBASEDATATYPE(DATATYPE.A_BYTEFIELD);
        param.setDIAGCODEDTYPE(diagcodedtype);

        final DiagnosticElement element = paramMapper.mapParams(singletonList(param)).get(0);
        assertThat(element.getName()).isEqualTo("NRC CONST");
        assertThat(element.getType()).isEqualTo(Types.BYTE_FIELD);

    }


    @Test
    void valueShortNameIsMapped() {
        final VALUE param = new VALUE();
        param.setSHORTNAME(SHORT_NAME);
        param.setDOPREF(attachDopToLayer(newDop("my-dop")));
        final DiagnosticElement element = paramMapper.mapParams(singletonList(param)).get(0);

        assertThat(element.getName()).isEqualTo("VALUE (short_name)");
    }

    @Test
    void emptyValueShortNameIsNotMapped() {
        final ODXLINK dopRef = attachDopToLayer(newDop("my-dop"));
        final VALUE p1 = new VALUE();
        p1.setSHORTNAME("");
        p1.setDOPREF(dopRef);
        final VALUE p2 = new VALUE();
        p2.setDOPREF(dopRef);
        final List<DiagnosticElement> elements = paramMapper.mapParams(Arrays.asList(p1,p2));

        assertThat(elements.get(0).getName()).isEqualTo("VALUE");
        assertThat(elements.get(1).getName()).isEqualTo("VALUE");
    }



    private ODXLINK attachDopToLayer(final DATAOBJECTPROP dop) {
        dop.setLayer(diaglayer);
        diaglayer.getDIAGDATADICTIONARYSPEC().getDataobjectprops().add(dop);
        final ODXLINK odxLInk = createOdxLInk(DOCTYPE.LAYER, diaglayer.getSHORTNAME(), dop.getID());
        odxLInk.setDocument(diaglayer);

        final DocumentIndex index = mock(DocumentIndex.class);
        diaglayer.setIndex(index);
        when(index.resolveLink(odxLInk, AbstractLinkTarget.class)).thenReturn(Optional.of(dop));
        return odxLInk;
    }
}