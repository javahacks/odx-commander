package com.javahacks.odx.lsp.mapper;

import com.javahacks.odx.index.DocumentIndex;
import com.javahacks.odx.lsp.dtos.DiagnosticElement;
import com.javahacks.odx.model.*;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static com.javahacks.odx.lsp.mapper.ComParamMapper.OVERRIDDEN_PREFIX;
import static com.javahacks.odx.model.TestHelper.SHORT_NAME;
import static com.javahacks.odx.model.TestHelper.randomId;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class ComParamMapperTest {
    private static final String DEFAULT_VALUE = "default_value";
    private static final String NEW_VALUE = "new_value";
    private final DocumentIndex documentIndex = mock(DocumentIndex.class);
    private final ComParamMapper comParamMapper = new ComParamMapper(documentIndex);

    @Test
    void defaultValueIsMapped() {
        final DiagnosticElement element = comParamMapper.mapComParam(createComParam());
        Assertions.assertThat(element.getName()).isEqualTo(SHORT_NAME + " = " + DEFAULT_VALUE);
    }


    @Test
    void dopAndUnitIsMapped() {
        DATAOBJECTPROP dataobjectprop = new DATAOBJECTPROP();
        PHYSICALTYPE physicaltype = new PHYSICALTYPE();
        physicaltype.setBASEDATATYPE(PHYSICALDATATYPE.A_UNICODE_2_STRING);
        dataobjectprop.setPHYSICALTYPE(physicaltype);

        UNIT unit = new UNIT();
        unit.setDISPLAYNAME("test_unit");
        dataobjectprop.setUNITREF(TestHelper.createOdxLInk(DOCTYPE.LAYER, randomId(), TestHelper.randomId()));

        when(documentIndex.resolveLink(any(), any())).thenReturn(Optional.of(dataobjectprop), Optional.of(unit));


        final DiagnosticElement element = comParamMapper.mapComParam(createComParam());
        Assertions.assertThat(element.getType()).isEqualTo(Types.STRING);
        Assertions.assertThat(element.getName()).isEqualTo(SHORT_NAME + " = " + DEFAULT_VALUE + " test_unit");
    }

    @Test
    void defaultValueIsOverridden() {
        final DiagnosticElement element = comParamMapper.mapComParam(createComParam(), NEW_VALUE);
        Assertions.assertThat(element.getName()).isEqualTo(OVERRIDDEN_PREFIX + SHORT_NAME + " = " + NEW_VALUE);
    }


    @Test
    void complexValueIsMapped() {
        final List<DiagnosticElement> element = comParamMapper.mapComplexComParam(createComplexComParam());
        Assertions.assertThat(element).hasSize(1);
        Assertions.assertThat(element.get(0).getName()).isEqualTo(SHORT_NAME + " = " + DEFAULT_VALUE);
    }

    @Test
    void complexValueIsOverridden() {
        final List<DiagnosticElement> element = comParamMapper.mapComplexComParam(createComplexComParam(), createComplexValue(NEW_VALUE));
        Assertions.assertThat(element).hasSize(1);
        Assertions.assertThat(element.get(0).getName()).isEqualTo(OVERRIDDEN_PREFIX + SHORT_NAME + " = " + NEW_VALUE);
    }


    private COMPLEXCOMPARAM createComplexComParam() {
        COMPLEXCOMPARAM param = new COMPLEXCOMPARAM();
        param.getCOMPARAMOrCOMPLEXCOMPARAM().add(createComParam());
        param.setComplexphysicaldefaultvalues(createComplexValue(DEFAULT_VALUE));
        return param;
    }

    private COMPLEXVALUE createComplexValue(String value) {
        COMPLEXVALUE defaultValue = new COMPLEXVALUE();
        SIMPLEVALUE simpleValue = new SIMPLEVALUE();
        simpleValue.setValue(value);
        defaultValue.getSIMPLEVALUEOrCOMPLEXVALUE().add(simpleValue);
        return defaultValue;
    }

    private COMPARAM createComParam() {
        COMPARAM comparam = new COMPARAM();
        comparam.setSHORTNAME(SHORT_NAME);
        comparam.setPHYSICALDEFAULTVALUE(DEFAULT_VALUE);
        return comparam;
    }
}