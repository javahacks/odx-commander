package com.javahacks.odx.lsp.mapper;

import com.google.common.base.Strings;
import com.javahacks.odx.index.DocumentIndex;
import com.javahacks.odx.index.Layer;
import com.javahacks.odx.index.LayerProxy;
import com.javahacks.odx.index.XmlElementLocation;
import com.javahacks.odx.lsp.dtos.DiagnosticElement;
import com.javahacks.odx.lsp.dtos.LayerDetails;
import com.javahacks.odx.lsp.dtos.Link;
import com.javahacks.odx.lsp.dtos.Service;
import com.javahacks.odx.model.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.google.common.base.Strings.nullToEmpty;
import static com.javahacks.odx.lsp.mapper.Types.NOT_INHERITED;
import static com.javahacks.odx.lsp.mapper.Types.getDocumentType;
import static java.util.stream.Collectors.toCollection;

/**
 * Maps all relevant information for a single {@link DIAGLAYER} that is shown in the client
 */
public class LayerDetailsMapper {

    private final DocumentIndex documentIndex;
    private final Link layerRef;

    public LayerDetailsMapper(final DocumentIndex documentIndex, final Link layerRef) {
        this.documentIndex = documentIndex;
        this.layerRef = layerRef;
    }

    public LayerDetails map() {
        final DIAGLAYER diaglayer = documentIndex.findObjectInDocument(layerRef.getDocType(), layerRef.getDocRef(), layerRef.getIdRef(), DIAGLAYER.class).get();
        final LayerDetails layerDetails = new LayerDetails();
        layerDetails.setServices(createServiceObjects(diaglayer));
        layerDetails.setVariantPatterns(resolveVariantPatterns(diaglayer));
        layerDetails.setDependencies(resolveDependencies(diaglayer));
        return layerDetails;
    }

    private List<Service> createServiceObjects(final DIAGLAYER diaglayer) {
        return diaglayer.query().visibleDiagComms().stream()
                .filter(DIAGSERVICE.class::isInstance)
                .map(DIAGSERVICE.class::cast)
                .sorted(this::sortBySemanticThenName)
                .map(this::mapService)
                .collect(Collectors.toList());
    }

    private Service mapService(final DIAGSERVICE diagservice) {
        final Service service = new Service();
        service.setLayerRef(layerRef);
        service.setLabel(getSemantic(diagservice) + diagservice.getSHORTNAME());
        service.setName(nullToEmpty(diagservice.getSHORTNAME()));
        service.setLocation(diagservice.getLocation());
        service.setType(getDocumentType(diagservice.getLayer()));
        return service;
    }

    private String getSemantic(final DIAGSERVICE diagservice) {
        return Strings.isNullOrEmpty(diagservice.getSEMANTIC()) ? "" : diagservice.getSEMANTIC().toUpperCase() + " - ";
    }

    private List<DiagnosticElement> resolveVariantPatterns(final DIAGLAYER diaglayer) {
        final List<DiagnosticElement> children = new ArrayList<>();

        if (diaglayer instanceof BASEVARIANT) {
            ((BASEVARIANT) diaglayer).getMatchingBasevariantParameters().forEach(parameter ->
                    children.add(mapMatchingParameter(diaglayer, parameter.getEXPECTEDVALUE(), parameter.getLocation(), parameter.getDIAGCOMMSNREF()))
            );
        }
        if (diaglayer instanceof ECUVARIANT) {
            ((ECUVARIANT) diaglayer).getMatchingParameters().forEach(parameter ->
                    children.add(mapMatchingParameter(diaglayer, parameter.getEXPECTEDVALUE(), parameter.getLocation(), parameter.getDIAGCOMMSNREF()))
            );
        }
        return children;
    }

    private DiagnosticElement mapMatchingParameter(final DIAGLAYER diaglayer, final String value, final XmlElementLocation location, final SNREF snref) {
        final DiagnosticElement expectedValue = new DiagnosticElement();
        expectedValue.setName(Strings.isNullOrEmpty(value) ? "EMPTY" : value.toUpperCase());
        expectedValue.setType("match");
        expectedValue.setLocation(location);

        if (snref != null && snref.getSHORTNAME() != null) {
            diaglayer.query().diagCommByShortName(snref.getSHORTNAME(), true).ifPresent(diagComm -> {
                final DiagnosticElement serviceReference = new DiagnosticElement();
                serviceReference.setName(diagComm.getSHORTNAME());
                serviceReference.setLocation(diagComm.getLocation());
                serviceReference.setType(getDocumentType(diagComm.getLayer()));
                expectedValue.getChildren().add(serviceReference);
            });
        }

        return expectedValue;
    }

    int sortBySemanticThenName(final DIAGSERVICE s1, final DIAGSERVICE s2) {
        if (nullToEmpty(s1.getSEMANTIC()).equals(nullToEmpty(s2.getSEMANTIC()))) {
            //same semantic then sort by name
            return nullToEmpty(s1.getSHORTNAME()).compareTo(nullToEmpty(s2.getSHORTNAME()));
        }

        return nullToEmpty(s1.getSEMANTIC()).compareTo(nullToEmpty(s2.getSEMANTIC()));
    }

    private List<DiagnosticElement> resolveDependencies(final DIAGLAYER diaglayer) {
        final List<DiagnosticElement> children = new ArrayList<>();
        if (diaglayer instanceof HIERARCHYELEMENT) {
            mapParents((HIERARCHYELEMENT) diaglayer, children);
        }
        diaglayer.getImports().stream().map(l -> mapLayer(l, "IMPORT")).collect(toCollection(() -> children));
        documentIndex.findLayerProxyForOdxLink(toOdxLink(diaglayer)).map(this::createChildGroup).ifPresent(children::addAll);
        return children;
    }

    private void mapParents(final HIERARCHYELEMENT hierarchyelement, final List<DiagnosticElement> children) {
        for (final PARENTREF parentref : hierarchyelement.getParentRefs()) {
            mapProxy(parentref, "PARENT - ").ifPresent(parentElement -> {
                children.add(parentElement);
                parentElement.getChildren().addAll(mapNotInherited(parentref));
            });
        }
    }

    private List<DiagnosticElement> mapNotInherited(final PARENTREF parentref) {
        final List<DiagnosticElement> result = new ArrayList<>();
        parentref.getNotinheriteddiagcomms().stream().map(NOTINHERITEDDIAGCOMM::getDIAGCOMMSNREF).map(sn -> mapSnRef(sn, "DIAG-COMM - ")).collect(toCollection(() -> result));
        parentref.getNotinheriteddops().stream().map(NOTINHERITEDDOP::getDOPBASESNREF).map(sn -> mapSnRef(sn, "DOP - ")).collect(toCollection(() -> result));
        parentref.getNotinheritedglobalnegresponses().stream().map(NOTINHERITEDGLOBALNEGRESPONSE::getGLOBALNEGRESPONSESNREF).map(sn -> mapSnRef(sn, "GLOBAL NRE - ")).collect(toCollection(() -> result));
        parentref.getNotinheritedtables().stream().map(NOTINHERITEDTABLE::getTABLESNREF).map(sn -> mapSnRef(sn, "TABLE - ")).collect(toCollection(() -> result));
        parentref.getNotinheritedvariables().stream().map(NOTINHERITEDVARIABLE::getDIAGVARIABLESNREF).map(sn -> mapSnRef(sn, "VARIABLE - ")).collect(toCollection(() -> result));
        return result;
    }

    private List<DiagnosticElement> createChildGroup(final LayerProxy layerProxy) {
        return layerProxy.getChildren().stream()
                .map(p -> mapProxy(p.toOdxLink(), "CHILD - "))
                .map(Optional::get).collect(Collectors.toList());
    }

    private Optional<DiagnosticElement> mapProxy(final ODXLINK childLink, final String prefix) {
        return documentIndex.findLayerProxyForOdxLink(childLink).map(childProxy -> {
            final DiagnosticElement element = new DiagnosticElement();
            element.setLabel(prefix + childProxy.getShortName());
            element.setType(childProxy.getLayerType());
            element.setName(childProxy.getShortName());
            element.setLocation(childProxy.getLocation());
            element.setRevealable(true);
            return element;
        });
    }


    private DiagnosticElement mapLayer(final Layer layer, final String prefix) {
        final DiagnosticElement element = new DiagnosticElement();
        element.setLabel(prefix + " - " + layer.getSHORTNAME());
        element.setLocation(layer.getLocation());
        element.setType(getDocumentType(layer));
        element.setName(layer.getSHORTNAME());
        element.setRevealable(true);
        return element;
    }

    private DiagnosticElement mapSnRef(final SNREF snref, final String prefix) {
        final DiagnosticElement element = new DiagnosticElement();
        element.setName(prefix + snref.getSHORTNAME());
        element.setLocation(snref.getLocation());
        element.setType(NOT_INHERITED);
        return element;
    }

    private ODXLINK toOdxLink(final DIAGLAYER diaglayer) {
        final ODXLINK odxlink = new ODXLINK();
        odxlink.setDOCTYPE(DOCTYPE.LAYER);
        odxlink.setDOCREF(diaglayer.getSHORTNAME());
        odxlink.setIDREF(diaglayer.getID());
        return odxlink;
    }

}
