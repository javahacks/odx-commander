package com.javahacks.odx.lsp.mapper;

import com.javahacks.odx.index.DocumentIndex;
import com.javahacks.odx.index.LayerProxy;
import com.javahacks.odx.lsp.dtos.DiagnosticElement;
import com.javahacks.odx.model.*;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static com.javahacks.odx.model.TestHelper.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class VehicleInformationMapperTest {


    public static final String DEFAULT_VALUE = "DEFAULT_VALUE";



    @Test
    void allVehicleInformationIsMapped() {
        final VehicleInformationMapper mapper = new VehicleInformationMapper(createSpecWithIndex());
        final List<DiagnosticElement> dtos = mapper.map();
        assertThat(dtos).hasSize(1);
        assertThat(dtos.get(0).getName()).isEqualTo(SHORT_NAME);
        assertThat(dtos.get(0).getLocation()).isEqualTo(LOCATION);
        assertThat(dtos.get(0).getType()).isEqualTo("vehicle_info");
    }

    @Test
    void logicalLinkIsMapped() {
        final VehicleInformationMapper mapper = new VehicleInformationMapper(createSpecWithIndex());
        final DiagnosticElement element = mapper.map().get(0).getChildren().get(0);
        assertThat(element.getType()).isEqualTo("logical_link");
        assertThat(element.getName()).isEqualTo(SHORT_NAME);
        assertThat(element.getLocation()).isEqualTo(LOCATION);
    }

    @Test
    void baseVariantIsMapped() {
        final VehicleInformationMapper mapper = new VehicleInformationMapper(createSpecWithIndex());
        final DiagnosticElement element = mapper.map().get(0).getChildren().get(0).getChildren().get(0);

        assertThat(element.getName()).isEqualTo(SHORT_NAME);
        assertThat(element.getLocation()).isEqualTo(LOCATION);
        assertThat(element.getType()).isEqualTo("base_variant");
        assertThat(element.isRevealable()).isTrue();
    }

    @Test
    void functionalGroupIsMapped() {
        final VEHICLEINFOSPEC vehicleinfospec = createSpecWithIndex();
        final LOGICALLINK logicallink = vehicleinfospec.getVehicleinformations().get(0).getLogicallinks().get(0);
        logicallink.setBASEVARIANTREF(null);
        final VehicleInformationMapper mapper = new VehicleInformationMapper(vehicleinfospec);
        final DiagnosticElement element = mapper.map().get(0).getChildren().get(0).getChildren().get(0);

        assertThat(element.getName()).isEqualTo(SHORT_NAME);
        assertThat(element.getLocation()).isEqualTo(LOCATION);
        assertThat(element.getType()).isEqualTo("functional_group");
    }

    @Test
    void comparamValueIsOverridden() {

        final VEHICLEINFOSPEC spec = attachLinkedComParam(createSpecWithIndex(), "new_value");

        final VehicleInformationMapper mapper = new VehicleInformationMapper(spec);
        final DiagnosticElement element = mapper.map().get(0).getChildren().get(0).getChildren().get(0);
        assertThat(element.getType()).isEqualTo(Types.LIST);
        assertThat(element.getChildren().get(0).getName()).matches(".*" + SHORT_NAME + ".*" + "new_value");

    }

    @Test
    void comparamValueIsNotOverridden() {
        final VEHICLEINFOSPEC spec = attachLinkedComParam(createSpecWithIndex(), null);

        final VehicleInformationMapper mapper = new VehicleInformationMapper(spec);
        final DiagnosticElement element = mapper.map().get(0).getChildren().get(0).getChildren().get(0);
        assertThat(element.getType()).isEqualTo(Types.LIST);
        assertThat(element.getChildren().get(0).getName()).matches(SHORT_NAME + ".*" + DEFAULT_VALUE);
    }

    @Test
    void complexComparamValueIsOverridden() {
        final VEHICLEINFOSPEC spec = attachComplexComParam(createSpecWithIndex(), "new_value");

        final VehicleInformationMapper mapper = new VehicleInformationMapper(spec);
        final DiagnosticElement element = mapper.map().get(0).getChildren().get(0).getChildren().get(0);
        assertThat(element.getType()).isEqualTo(Types.LIST);
        assertThat(element.getChildren().get(0).getName()).matches(".*" + SHORT_NAME + ".*" + "new_value");
    }

    @Test
    void complexComparamValueIsNotOverridden() {
        final VEHICLEINFOSPEC spec = attachComplexComParam(createSpecWithIndex(), null);

        final VehicleInformationMapper mapper = new VehicleInformationMapper(spec);
        final DiagnosticElement element = mapper.map().get(0).getChildren().get(0).getChildren().get(0);
        assertThat(element.getType()).isEqualTo(Types.LIST);
        assertThat(element.getChildren().get(0).getName()).matches(SHORT_NAME + ".*" + DEFAULT_VALUE);
    }

    private VEHICLEINFOSPEC attachLinkedComParam(final VEHICLEINFOSPEC spec, final String simpleValue) {
        final LOGICALLINK logicallink = spec.getVehicleinformations().get(0).getLogicallinks().get(0);
        final LINKCOMPARAMREF link = new LINKCOMPARAMREF();
        logicallink.getLinkcomparamrefs().add(link);

        if (simpleValue != null) {
            final SIMPLEVALUE simplevalue = new SIMPLEVALUE();
            simplevalue.setValue(simpleValue);
            link.setSIMPLEVALUE(simplevalue);
        }

        when(spec.getIndex().resolveLink(link, BASECOMPARAM.class)).thenReturn(Optional.of(createComParam()));

        return spec;
    }

    private VEHICLEINFOSPEC attachComplexComParam(final VEHICLEINFOSPEC spec, final String simpleValue) {
        final LOGICALLINK logicallink = spec.getVehicleinformations().get(0).getLogicallinks().get(0);
        final LINKCOMPARAMREF link = new LINKCOMPARAMREF();
        logicallink.getLinkcomparamrefs().add(link);

        if (simpleValue != null) {
            final SIMPLEVALUE simplevalue = new SIMPLEVALUE();
            simplevalue.setValue(simpleValue);
            link.setSIMPLEVALUE(simplevalue);
            final COMPLEXVALUE complexvalue = new COMPLEXVALUE();
            complexvalue.getSIMPLEVALUEOrCOMPLEXVALUE().add(simplevalue);
            link.setCOMPLEXVALUE(complexvalue);
        }

        final COMPLEXCOMPARAM complexcomparam = new COMPLEXCOMPARAM();
        complexcomparam.getCOMPARAMOrCOMPLEXCOMPARAM().add(createComParam());
        when(spec.getIndex().resolveLink(link, BASECOMPARAM.class)).thenReturn(Optional.of(complexcomparam));
        return spec;
    }

    private COMPARAM createComParam() {
        final COMPARAM comparam = new COMPARAM();
        comparam.setSHORTNAME(SHORT_NAME);
        comparam.setPHYSICALDEFAULTVALUE(DEFAULT_VALUE);
        return comparam;
    }

    private VEHICLEINFOSPEC createSpecWithIndex() {
        final VEHICLEINFOSPEC vehicleinfospec = createTestSpec();

        final DocumentIndex index = mock(DocumentIndex.class);
        final LOGICALLINK logicallink = vehicleinfospec.getVehicleinformations().get(0).getLogicallinks().get(0);
        when(index.findLayerProxyForOdxLink(logicallink.getBASEVARIANTREF()))
                .thenReturn(Optional.of(createProxy("base_variant")));
        when(index.findLayerProxyForOdxLink(logicallink.getFUNCTIONALGROUPREF()))
                .thenReturn(Optional.of(createProxy("functional_group")));

        vehicleinfospec.setIndex(index);

        return vehicleinfospec;
    }

    private LayerProxy createProxy(final String layerType) {
        final LayerProxy layerProxy = new LayerProxy();
        layerProxy.setLocation(LOCATION);
        layerProxy.setShortName(SHORT_NAME);
        layerProxy.setLayerType(layerType);
        return layerProxy;
    }
}