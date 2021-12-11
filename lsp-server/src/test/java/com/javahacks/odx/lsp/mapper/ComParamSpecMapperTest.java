package com.javahacks.odx.lsp.mapper;

import com.javahacks.odx.index.DocumentIndex;
import com.javahacks.odx.lsp.dtos.DiagnosticElement;
import com.javahacks.odx.model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.List;
import java.util.Optional;

import static com.javahacks.odx.model.TestHelper.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class ComParamSpecMapperTest {
    private static final String DEFAULT_VALUE = "DEFAULT_VALUE";
    private final DocumentIndex documentIndex = mock(DocumentIndex.class);
    private final COMPARAMSPEC spec = createSpec(documentIndex);
    private final ComParamSpecMapper subsetMapper = new ComParamSpecMapper(spec);

    @BeforeEach
    void setup() {
        spec.setIndex(documentIndex);
    }

    @Test
    void comParamIsMapped() {
        final COMPARAM comparam = createParam(SHORT_NAME);
        spec.getComparams().add(comparam);

        final List<DiagnosticElement> elements = subsetMapper.map();

        assertThat(elements).isNotEmpty();
        assertThat(elements.get(0).getType()).isEqualTo(Types.BYTE_FIELD);
        assertThat(elements.get(0).getName()).matches(comparam.getSHORTNAME() + ".*" + DEFAULT_VALUE + " m/s");

    }

    @Test
    void comParamsAreSortedAndFiltered() {
        spec.getComparams().add(createParam("xx"));
        spec.getComparams().add(createParam("aa"));
        final List<DiagnosticElement> elements = subsetMapper.map();

        assertThat(elements.get(0).getName()).startsWith("aa");
        assertThat(elements.get(1).getName()).startsWith("xx");
    }

    @Test
    void protocolStacksAreMapped() {
        spec.getProtocolStacks().add(createStack());
        spec.getProtocolStacks().add(createStack());

        final List<DiagnosticElement> elements = subsetMapper.map();
        assertThat(elements).hasSize(2);
        final DiagnosticElement subsetElement = elements.get(0).getChildren().get(0);
        assertThat(subsetElement.getLocation()).isNotNull();
        assertThat(subsetElement.getName()).isNotEmpty();

    }

    private PROTSTACK createStack() {
        final PROTSTACK protstack = new PROTSTACK();
        protstack.setSHORTNAME(TestHelper.randomId());
        protstack.setID(TestHelper.randomId());

        final ODXLINK setLink = createOdxLInk(DOCTYPE.CONTAINER, SHORT_NAME, randomId());
        protstack.getComparamsubsetrefs().add(setLink);

        when(documentIndex.findCategoryProxyForOdxLink(setLink)).thenReturn(Optional.of(createCategoryProxy(DOCTYPE.CONTAINER)));

        return protstack;
    }


    private COMPARAM createParam(final String shortName) {
        final COMPARAM comparam = new COMPARAM();
        comparam.setSHORTNAME(shortName);
        comparam.setPHYSICALDEFAULTVALUE(DEFAULT_VALUE);

        final DATAOBJECTPROP dop = createDop();
        spec.getDataobjectprops().add(dop);
        comparam.setDATAOBJECTPROPREF(createOdxLInk(DOCTYPE.LAYER, spec.getSHORTNAME(), dop.getID()));

        final UNIT unit = new UNIT();
        unit.setDISPLAYNAME("m/s");
        unit.setSHORTNAME(SHORT_NAME);
        dop.setUNITREF(createOdxLInk(DOCTYPE.LAYER, spec.getSHORTNAME(), unit.getSHORTNAME()));

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

    private COMPARAMSPEC createSpec(final DocumentIndex documentIndex) {
        final COMPARAMSPEC spec = new COMPARAMSPEC();
        spec.setSHORTNAME(randomId());
        spec.setID(randomId());
        spec.setIndex(documentIndex);
        return spec;
    }
}