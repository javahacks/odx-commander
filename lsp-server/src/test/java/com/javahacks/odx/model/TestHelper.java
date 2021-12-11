package com.javahacks.odx.model;

import com.javahacks.odx.index.*;
import com.javahacks.odx.lsp.mapper.Types;
import org.mockito.Mockito;

import javax.xml.bind.JAXBElement;
import javax.xml.namespace.QName;
import java.io.IOException;
import java.io.Writer;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static com.javahacks.odx.lsp.mapper.Types.getDocumentType;
import static org.mockito.Mockito.when;

public class TestHelper {
    public static final XmlElementLocation LOCATION = new XmlElementLocation(URI.create("/temp/test.odx"), 0, 1);
    public static final String SHORT_NAME = "short_name";
    public static final String ID = "id";
    public static final String TABLE_ROW_ID = "table.row.id";

    public static BASEVARIANT createBaseVariant() {
        final BASEVARIANT basevariant = new BASEVARIANT();
        prepareLayer(basevariant);
        return basevariant;
    }

    public static ECUVARIANT createEcuVariant() {
        final ECUVARIANT result = new ECUVARIANT();
        prepareLayer(result);
        return result;
    }

    public static ECUSHAREDDATA createSharedData() {
        final ECUSHAREDDATA result = new ECUSHAREDDATA();
        prepareLayer(result);
        return result;
    }

    public static List<TABLE> getTestTable() {
        final TABLE table = new TABLE();
        table.setSHORTNAME(SHORT_NAME);
        table.setID(ID);
        final TABLEROW myTestRow = new TABLEROW();
        myTestRow.setSHORTNAME(SHORT_NAME);
        myTestRow.setID(TABLE_ROW_ID);
        table.getROWWRAPPER().add(myTestRow);
        return Collections.singletonList(table);
    }

    private static void prepareLayer(final DIAGLAYER result) {
        result.setSHORTNAME(randomId());
        result.setID(randomId());
        result.setDIAGDATADICTIONARYSPEC(new DIAGDATADICTIONARYSPEC());
    }

    public static <T extends ODXLINK> T fillLinkTarget(final DIAGLAYER baseVariant, final T link) {
        link.setDOCREF(baseVariant.getSHORTNAME());
        link.setIDREF(baseVariant.getID());
        link.setDOCTYPE(DOCTYPE.valueOf(baseVariant.getDocType()));
        return link;
    }

    public static DATAOBJECTPROP newDop(final String shortName) {
        final DATAOBJECTPROP dop = new DATAOBJECTPROP();
        dop.setSHORTNAME(shortName);
        dop.setID(randomId());
        return dop;
    }

    public static DIAGSERVICE newService(final String shortName) {
        final DIAGSERVICE diagservice = new DIAGSERVICE();
        diagservice.setSHORTNAME(shortName);
        diagservice.setID(randomId());
        return diagservice;
    }

    public static String randomId() {
        return UUID.randomUUID().toString();
    }

    public static Layer toLayer(final DIAGLAYER diaglayer) {
        return diaglayer;
    }


    public static SNREF snRef(final String shortname) {
        final SNREF snref = new SNREF();
        snref.setSHORTNAME(shortname);
        return snref;
    }

    public static ODXLINK createOdxLInk(final DOCTYPE doctype, final String shortName, final String idRef) {
        final ODXLINK odxlink = new ODXLINK();
        odxlink.setDOCTYPE(doctype);
        odxlink.setDOCREF(shortName);
        odxlink.setIDREF(idRef);
        return odxlink;
    }

    public static DIAGLAYERCONTAINER newDiagLayerContainer() {
        final DIAGLAYERCONTAINER diaglayercontainer = new DIAGLAYERCONTAINER();
        diaglayercontainer.setID(randomId());
        diaglayercontainer.setSHORTNAME(randomId());
        diaglayercontainer.setParent(new ODX());
        return diaglayercontainer;
    }

    public static LayerProxy createLayerProxy(final DIAGLAYER layer) {
        final LayerProxy proxy = new LayerProxy();
        proxy.setShortName(layer.getSHORTNAME());
        proxy.setId(layer.getID());
        proxy.setDocType(DOCTYPE.LAYER.name());
        proxy.setLayerType(getDocumentType(layer));
        return proxy;
    }

    public static void saveOdxFile(final DIAGLAYERCONTAINER diaglayercontainer, final Path odxFile) throws IOException {
        try (final Writer writer = Files.newBufferedWriter(odxFile)) {
            final ODX odx = new ODX();
            odx.setDIAGLAYERCONTAINER(diaglayercontainer);
            JaxbUtil.marshalModel(odx, writer);
        }
    }


    public static LayerProxy createLayerProxy(final String layerType) {
        final LayerProxy layerProxy = new LayerProxy();
        layerProxy.setDocType(DOCTYPE.LAYER.name());
        layerProxy.setLayerType(layerType);
        layerProxy.setId(randomId());
        layerProxy.setShortName(randomId());
        layerProxy.setLocation(LOCATION);
        return layerProxy;
    }

    public static CategoryProxy createCategoryProxy(final DOCTYPE doctype) {
        final CategoryProxy categoryProxy = new CategoryProxy();
        categoryProxy.setDocType(doctype.name());
        categoryProxy.setId(UUID.randomUUID().toString());
        categoryProxy.setShortName(UUID.randomUUID().toString());
        return categoryProxy;
    }

    public static VEHICLEINFOSPEC createTestSpec() {
        final LOGICALLINK logicallink = new MEMBERLOGICALLINK();
        logicallink.setSHORTNAME(SHORT_NAME);
        logicallink.setLocation(LOCATION);
        logicallink.setBASEVARIANTREF(createOdxLInk(DOCTYPE.LAYER, SHORT_NAME, randomId()));
        logicallink.setFUNCTIONALGROUPREF(createOdxLInk(DOCTYPE.LAYER, SHORT_NAME, randomId()));

        final VEHICLEINFORMATION vi = new VEHICLEINFORMATION();
        vi.setLocation(LOCATION);
        vi.setSHORTNAME(SHORT_NAME);
        vi.getLogicallinks().add(logicallink);

        final VEHICLEINFOSPEC result = new VEHICLEINFOSPEC();
        result.getVehicleinformations().add(vi);
        return result;
    }


    public static REQUEST createRequest() {
        final REQUEST request = new REQUEST();
        request.setID(randomId());
        return request;
    }

    public static JAXBElement<Object> createJaxbLink(final Object link, final String localPart) {
        final JAXBElement<Object> ele = Mockito.mock(JAXBElement.class);
        when(ele.getValue()).thenReturn(link);
        when(ele.getName()).thenReturn(new QName(localPart));
        return ele;
    }



    public static XmlElementLocation createStartEndTagLocation(final int startLine, final int startColumn, final int endLine, final int endColumn ) {
        final XmlElementLocation location = new XmlElementLocation();
        location.setStartLine(startLine);
        location.setEndLine(endLine);
        location.setStartColumn(startColumn);
        location.setEndColumn(endColumn);
        location.setFileUri(TestHelper.LOCATION.getFileUri());
        return location;
    }
}


