package com.javahacks.odx.index;

import com.javahacks.odx.model.*;
import org.junit.jupiter.api.Test;

import java.io.StringWriter;

import static com.javahacks.odx.index.LocationAwareXMLStreamWriter.IDENT_STRING;
import static com.javahacks.odx.index.LocationAwareXMLStreamWriter.NOTICE;
import static org.assertj.core.api.Assertions.assertThat;

class JaxbUtilTest {

    @Test
    void headerIsWritten() {
        final StringWriter writer = new StringWriter();
        JaxbUtil.marshalModelFormattedAndSimplified(prepareOdxModel(), writer);
        assertThat(writer.toString()).contains("<?xml version=\"1.0\" ?>");
        assertThat(writer.toString()).contains(NOTICE);
    }

    @Test
    void containerLocationIsWritten() {
        final ODX odx = prepareOdxModel();

        final StringWriter writer = new StringWriter();
        JaxbUtil.marshalModelFormattedAndSimplified(odx, writer);
        final XmlElementLocation location = odx.getDIAGLAYERCONTAINER().getLocation();

        assertThat(location.getStartLine()).isEqualTo(3);
        assertThat(location.getStartColumn()).isEqualTo(2);
        assertThat(location.getEndLine()).isEqualTo(6);
        assertThat(location.getEndColumn()).isEqualTo(25);
    }

    @Test
    void identIsWritten() {
        final StringWriter writer = new StringWriter();
        JaxbUtil.marshalModelFormattedAndSimplified(prepareOdxModel(), writer);
        assertThat(writer.toString()).contains(IDENT_STRING + "<DIAG-LAYER-CONTAINER");
        assertThat(writer.toString()).contains(IDENT_STRING + IDENT_STRING + "<SHORT-NAME>");
    }

    @Test
    void emptyElementsAreWritten() {
        final StringWriter writer = new StringWriter();
        JaxbUtil.marshalModelFormattedAndSimplified(prepareOdxModel(), writer);

        assertThat(writer.toString()).contains("<LONG-NAME/>");
    }

    @Test
    void hiddenElementsAreRemoved() {
        final StringWriter writer = new StringWriter();
        JaxbUtil.marshalModelFormattedAndSimplified(prepareOdxModel(), writer);
        assertThat(writer.toString()).doesNotContain("<ADMIN-DATA>");
        assertThat(writer.toString()).doesNotContain("<COMPANY-DATAS>");
    }


    @Test
    void hiddenElementLocationsAreReset() {
        final ODX model = prepareOdxModel();
        JaxbUtil.marshalModelFormattedAndSimplified(model, new StringWriter());

        final DIAGLAYERCONTAINER diaglayercontainer = model.getDIAGLAYERCONTAINER();
        final COMPANYDATA companydata = diaglayercontainer.getCOMPANYDATAS().getCOMPANYDATA().get(0);
        final XmlElementLocation layerLocation = diaglayercontainer.getLocation();
        final XmlElementLocation memberLocation = companydata.getTeamMembers().get(0).getLocation();

        assertThat(layerLocation.getEndLine()).isGreaterThan(-1);
        assertThat(layerLocation.getStartTagEndLine()).isGreaterThan(-1);
        assertThat(memberLocation.getEndLine()).isEqualTo(-1);
        assertThat(memberLocation.getStartTagEndLine()).isEqualTo(-1);
    }

    private ODX prepareOdxModel() {
        final ODX odx = new ODX();
        final DIAGLAYERCONTAINER layerContainer = TestHelper.newDiagLayerContainer();
        layerContainer.setLocation(new XmlElementLocation());
        odx.setDIAGLAYERCONTAINER(layerContainer);

        final ADMINDATA admindata = new ADMINDATA();
        admindata.setLANGUAGE("language");
        admindata.setLocation(new XmlElementLocation());
        layerContainer.setADMINDATA(admindata);

        final COMPANYDATAS companydatas = new COMPANYDATAS();
        companydatas.setLocation(new XmlElementLocation());
        final COMPANYDATA companydata = new COMPANYDATA();
        companydata.setLocation(new XmlElementLocation());
        companydata.setSHORTNAME("company data sn");

        final TEAMMEMBER teammember = new TEAMMEMBER();
        teammember.setLocation(new XmlElementLocation());
        companydata.getTeamMembers().add(teammember);

        layerContainer.setLONGNAME(new LONGNAME());
        companydatas.getCOMPANYDATA().add(companydata);
        layerContainer.setCOMPANYDATAS(companydatas);

        return odx;
    }


}