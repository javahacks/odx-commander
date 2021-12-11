package com.javahacks.odx.lsp.mapper;

import com.javahacks.odx.index.XmlElementLocation;
import com.javahacks.odx.lsp.assertions.Assertions;
import com.javahacks.odx.lsp.dtos.DiagnosticElement;
import com.javahacks.odx.model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static java.util.Collections.singletonList;
import static org.assertj.core.api.Assertions.assertThat;

class FlashMapperTest {

    private final ObjectFactory factory = new ObjectFactory();
    private FLASH flash;

    @BeforeEach
    void setUp() {
        flash = factory.createFLASH();
    }

    @Test
    void mapEmptyFlash() {
        assertThat(runMapper()).isEmpty();
    }

    @Test
    void mapEcuMemConnector() {
        final ECUMEMCONNECTOR memConnector = createEcumemconnector();
        flash.getEcumemconnectors().add(memConnector);
        final List<DiagnosticElement> mappedElements = runMapper();
        assertThat(mappedElements).hasSize(1);
        Assertions.assertThat(mappedElements.get(0))
                .hasType(Types.LOGICAL_LINK).hasName("memconnector").hasLocation(memConnector.getLocation());
    }

    @Test
    void mapSessionDesc_withBrokenSessionSnref() {
        final ECUMEMCONNECTOR memConnector = createEcumemconnector();
        final SESSIONDESC sessionDesc = createSessionDesc("referencedSession");
        memConnector.setSESSIONDESCS(singletonList(sessionDesc));

        flash.getEcumemconnectors().add(memConnector);
        final List<DiagnosticElement> mappedElements = runMapper();
        assertThat(mappedElements.get(0).getChildren()).hasSize(1);
        Assertions.assertThat(mappedElements.get(0).getChildren().get(0))
                .hasType(Types.SERVICE).hasName("sessiondesc").hasLocation(sessionDesc.getLocation()).hasNoChildren();
    }

    @Test
    void mapSession() {
        final SESSION session = createSession("ses");
        addEcuMem().getMEM().getSESSIONS().add(session);
        final List<DiagnosticElement> mappedElements = runMapper();
        assertThat(mappedElements.get(0).getChildren()).hasSize(1);
        Assertions.assertThat(mappedElements.get(0).getChildren().get(0))
                .hasType(Types.SERVICE).hasName(session.getSHORTNAME()).hasLocation(session.getLocation()).hasNoChildren();
    }

    @Test
    void mapFlashdata() {
        final FLASHDATA flashdata = createFlashdata("flashdata", "datafileValue");
        addEcuMem().getMEM().getFLASHDATAS().add(flashdata);
        final List<DiagnosticElement> mappedElements = runMapper();
        assertThat(mappedElements.get(0).getChildren()).hasSize(1);
        Assertions.assertThat(mappedElements.get(0).getChildren().get(0))
                .hasType(Types.SHARED_DATA).hasName(flashdata.getSHORTNAME()).hasLocation(flashdata.getLocation()).hasNoChildren();
    }

    @Test
    void mapDatablock() {
        final DATABLOCK datablock = createDatablock("datablock");
        addEcuMem().getMEM().getDATABLOCKS().add(datablock);
        final List<DiagnosticElement> mappedElements = runMapper();
        assertThat(mappedElements.get(0).getChildren()).hasSize(1);
        Assertions.assertThat(mappedElements.get(0).getChildren().get(0))
                .hasType(Types.BYTE_FIELD).hasName(datablock.getSHORTNAME()).hasLocation(datablock.getLocation()).hasNoChildren();
    }

    @Test
    void mapDatablockWithSegment_allSegmentDataAvailable() {
        final SEGMENT segment = createSegment("segment", new byte[]{1, 2, 3}, new byte[]{4, 5, 6}, 100L);
        assertSegmentName(segment, segment.getSHORTNAME() + " start: 010203 end: 040506 size: 100");
    }

    @Test
    void mapDatablockWithSegment_emptySegment() {
        final SEGMENT segment = createSegment("segment", null, null, null);
        assertSegmentName(segment, segment.getSHORTNAME());
    }

    private void assertSegmentName(final SEGMENT segment, final String expectedSegmentName) {
        final DATABLOCK datablock = createDatablock("datablock");
        addEcuMem().getMEM().getDATABLOCKS().add(datablock);
        datablock.getSEGMENTS().add(segment);
        final List<DiagnosticElement> mappedElements = runMapper();
        assertThat(mappedElements.get(0).getChildren().get(0).getChildren()).hasSize(1);
        Assertions.assertThat(mappedElements.get(0).getChildren().get(0).getChildren().get(0))
                .hasType(Types.STACK).hasName(expectedSegmentName).hasLocation(segment.getLocation()).hasNoChildren();
    }

    @Test
    void mapSecurityWithChecksum() {
        assertSecurityName(createSecurity("checksum", null), "Checksum: checksum");
    }

    @Test
    void mapSecurityWithSignature() {
        assertSecurityName(createSecurity(null, "signature"), "Signature: signature");
    }

    @Test
    void mapSecurityWithoutChecksumOrSignature() {
        assertSecurityName(createSecurity(null, null), "Security");
    }

    private void assertSecurityName(final SECURITY security, final String expectedSecurityName) {
        final DATABLOCK datablock = createDatablock("datablock");
        addEcuMem().getMEM().getDATABLOCKS().add(datablock);
        datablock.getSECURITYS().add(security);
        final List<DiagnosticElement> mappedElements = runMapper();
        assertThat(mappedElements.get(0).getChildren().get(0).getChildren()).hasSize(1);
        Assertions.assertThat(mappedElements.get(0).getChildren().get(0).getChildren().get(0))
                .hasType(Types.KEY).hasName(expectedSecurityName).hasLocation(security.getLocation()).hasNoChildren();
    }

    private SECURITY createSecurity(final String fwChecksum, final String fwSignature) {
        final SECURITY security = factory.createSECURITY();
        security.setLocation(createLocation());
        if (fwChecksum != null) {
            security.setFWCHECKSUM(factory.createFWCHECKSUM());
            security.getFWCHECKSUM().setValue(fwChecksum);
        }
        if (fwSignature != null) {
            security.setFWSIGNATURE(factory.createFWSIGNATURE());
            security.getFWSIGNATURE().setValue(fwSignature);
        }
        return security;
    }

    private SEGMENT createSegment(final String shortname, final byte[] startAddress, final byte[] endAddress, final Long uncompressedSize) {
        final SEGMENT segment = factory.createSEGMENT();
        segment.setSHORTNAME(shortname);
        segment.setLocation(createLocation());
        if (endAddress != null) {
            segment.setSOURCEENDADDRESS(factory.createSOURCEENDADDRESS());
            segment.getSOURCEENDADDRESS().setValue(endAddress);
        }
        segment.setSOURCESTARTADDRESS(startAddress);
        if (uncompressedSize != null) {
            segment.setUNCOMPRESSEDSIZE(factory.createUNCOMPRESSEDSIZE());
            segment.getUNCOMPRESSEDSIZE().setValue(uncompressedSize);
        }
        return segment;
    }

    private DATABLOCK createDatablock(final String shortname) {
        final DATABLOCK datablock = factory.createDATABLOCK();
        datablock.setSHORTNAME(shortname);
        datablock.setLocation(createLocation());
        return datablock;
    }

    private FLASHDATA createFlashdata(final String shortname, final String datafileValue) {
        final EXTERNFLASHDATA flashdata = factory.createEXTERNFLASHDATA();
        flashdata.setSHORTNAME(shortname);
        flashdata.setLocation(createLocation());
        final DATAFILE datafile = factory.createDATAFILE();
        datafile.setLocation(createLocation());
        datafile.setValue(datafileValue);
        flashdata.setDATAFILE(datafile);
        return flashdata;
    }

    private ECUMEM addEcuMem() {
        final ECUMEM ecumem = factory.createECUMEM();
        ecumem.setSHORTNAME("ecumem");
        ecumem.setMEM(factory.createMEM());
        flash.getECUMEMS().add(ecumem);
        return ecumem;
    }

    private SESSION createSession(final String shortName) {
        final SESSION session = factory.createSESSION();
        session.setSHORTNAME(shortName);
        session.setLocation(createLocation());
        return session;
    }

    private SESSIONDESC createSessionDesc(final String referencedSession) {
        final SESSIONDESC sessionDesc = factory.createSESSIONDESC();
        sessionDesc.setSHORTNAME("sessiondesc");
        sessionDesc.setLocation(createLocation());
        if (referencedSession != null) {
            sessionDesc.setSESSIONSNREF(factory.createSNREF());
            sessionDesc.getSESSIONSNREF().setSHORTNAME(referencedSession);
        }
        return sessionDesc;
    }

    private ECUMEMCONNECTOR createEcumemconnector() {
        final ECUMEMCONNECTOR memConnector = factory.createECUMEMCONNECTOR();
        memConnector.setSHORTNAME("memconnector");
        memConnector.setLocation(createLocation());
        return memConnector;
    }

    private XmlElementLocation createLocation() {
        return new XmlElementLocation();
    }

    private List<DiagnosticElement> runMapper() {
        return new FlashMapper(flash).map();
    }
}