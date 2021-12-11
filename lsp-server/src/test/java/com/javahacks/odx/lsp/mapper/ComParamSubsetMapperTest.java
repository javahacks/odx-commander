package com.javahacks.odx.lsp.mapper;

import com.javahacks.odx.index.DocumentIndex;
import com.javahacks.odx.lsp.dtos.DiagnosticElement;
import com.javahacks.odx.model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.List;
import java.util.Optional;

import static com.javahacks.odx.model.TestHelper.randomId;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

class ComParamSubsetMapperTest {
    private static final String DEFAULT_VALUE = "DEFAULT_VALUE";
    private final DocumentIndex documentIndex = mock(DocumentIndex.class);
    private final COMPARAMSUBSET subSet = createComparamsubset(documentIndex);


    private final ComParamSubsetMapper subsetMapper = new ComParamSubsetMapper(subSet);

    @BeforeEach
    void setup() {
        subSet.setIndex(documentIndex);
    }

    @Test
    void comparamFieldsAreMapped() {
        final COMPARAM comparam = createParam();
        subSet.getComparams().add(comparam);

        final List<DiagnosticElement> elements = subsetMapper.map();

        assertThat(elements).isNotEmpty();
        assertThat(elements.get(0).getType()).isEqualTo(Types.BYTE_FIELD);
        assertThat(elements.get(0).getName()).matches(comparam.getSHORTNAME() + ".*" + DEFAULT_VALUE + " m/s");

    }


    @Test
    void complexComparamFieldsAreMapped() {
        final COMPLEXCOMPARAM comparam = createComplexComparam();
        subSet.getComplexcomparams().add(comparam);

        final List<DiagnosticElement> elements = subsetMapper.map();

        assertThat(elements).isNotEmpty();
        assertThat(elements).hasSize(comparam.getCOMPARAMOrCOMPLEXCOMPARAM().size());

    }


    @Test
    void complexComParamsAreSorted() {
        subSet.getComplexcomparams().add(createComplexComparam());

        final List<DiagnosticElement> elements = subsetMapper.map();

        assertThat(elements.get(0).getName()).contains("s1");
        assertThat(elements.get(1).getName()).contains("s2");
    }

    private COMPLEXCOMPARAM createComplexComparam() {
        final COMPLEXCOMPARAM result = new COMPLEXCOMPARAM();
        result.getCOMPARAMOrCOMPLEXCOMPARAM().add(createParam());
        result.getCOMPARAMOrCOMPLEXCOMPARAM().add(createParam());

        final COMPLEXVALUE complexvalue = new COMPLEXVALUE();
        result.setComplexphysicaldefaultvalues(complexvalue);
        final COMPLEXVALUE defaultValue = new COMPLEXVALUE();
        complexvalue.getSIMPLEVALUEOrCOMPLEXVALUE().add(defaultValue);
        final SIMPLEVALUE s1 = new SIMPLEVALUE();
        s1.setValue("s1");
        final SIMPLEVALUE s2 = new SIMPLEVALUE();
        s2.setValue("s2");
        defaultValue.getSIMPLEVALUEOrCOMPLEXVALUE().add(s2);
        defaultValue.getSIMPLEVALUEOrCOMPLEXVALUE().add(s1);

        return result;
    }

    private COMPARAM createParam() {
        final COMPARAM comparam = new COMPARAM();
        comparam.setSHORTNAME(TestHelper.SHORT_NAME);
        comparam.setPHYSICALDEFAULTVALUE(DEFAULT_VALUE);

        final DATAOBJECTPROP dop = createDop();
        subSet.getDataobjectprops().add(dop);
        comparam.setDATAOBJECTPROPREF(TestHelper.createOdxLInk(DOCTYPE.LAYER, subSet.getSHORTNAME(), dop.getID()));

        final UNIT unit = new UNIT();
        unit.setDISPLAYNAME("m/s");
        unit.setSHORTNAME(TestHelper.SHORT_NAME);
        dop.setUNITREF(TestHelper.createOdxLInk(DOCTYPE.LAYER, subSet.getSHORTNAME(), unit.getSHORTNAME()));

        Mockito.when(documentIndex.resolveLink(comparam.getDATAOBJECTPROPREF(), DATAOBJECTPROP.class)).thenReturn(Optional.of(dop));
        Mockito.when(documentIndex.resolveLink(dop.getUNITREF(), UNIT.class)).thenReturn(Optional.of(unit));

        return comparam;
    }

    private DATAOBJECTPROP createDop() {
        final DATAOBJECTPROP dop = new DATAOBJECTPROP();
        final PHYSICALTYPE physicaltype = new PHYSICALTYPE();
        physicaltype.setBASEDATATYPE(PHYSICALDATATYPE.A_BYTEFIELD);
        dop.setPHYSICALTYPE(physicaltype);
        return dop;
    }

    private COMPARAMSUBSET createComparamsubset(final DocumentIndex documentIndex) {
        final COMPARAMSUBSET subSet = new COMPARAMSUBSET();
        subSet.setSHORTNAME(randomId());
        subSet.setID(randomId());
        subSet.setIndex(documentIndex);
        return subSet;

    }
}