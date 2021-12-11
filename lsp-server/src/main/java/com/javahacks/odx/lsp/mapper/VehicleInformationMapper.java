package com.javahacks.odx.lsp.mapper;

import com.javahacks.odx.index.LayerProxy;
import com.javahacks.odx.lsp.dtos.DiagnosticElement;
import com.javahacks.odx.model.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import static java.util.Collections.emptyList;

/**
 * Maps all relevant {@link VEHICLEINFOSPEC} information that is shown in the client application.
 */
public class VehicleInformationMapper {
    private static final String TYPE_LOGICAL_LINK = "logical_link";
    private static final String TYPE_VEHICLE_INFO = "vehicle_info";
    private final ComParamMapper comParamMapper;
    private final VEHICLEINFOSPEC vehicleinfospec;

    public VehicleInformationMapper(final VEHICLEINFOSPEC vehicleinfospec) {
        this.vehicleinfospec = vehicleinfospec;
        this.comParamMapper = new ComParamMapper(vehicleinfospec.getIndex());
    }

    public List<DiagnosticElement> map() {
        final List<DiagnosticElement> result = new ArrayList<>();
        for (final VEHICLEINFORMATION info : vehicleinfospec.getVehicleinformations()) {
            final DiagnosticElement viDescription = new DiagnosticElement();
            viDescription.setName(info.getSHORTNAME());
            viDescription.setLocation(info.getLocation());
            viDescription.setType(TYPE_VEHICLE_INFO);
            result.add(viDescription);

            final List<DiagnosticElement> logicalLinks = info.getLogicallinks().stream()
                    .map(this::mapLogicalLink)
                    .sorted(Comparator.comparing(DiagnosticElement::getName))
                    .collect(Collectors.toList());

            viDescription.setChildren(logicalLinks);
        }

        return result;
    }

    private DiagnosticElement mapLogicalLink(final LOGICALLINK logicallink) {
        final DiagnosticElement llInfo = new DiagnosticElement();
        llInfo.setName(logicallink.getSHORTNAME());
        llInfo.setLocation(logicallink.getLocation());
        llInfo.setType(TYPE_LOGICAL_LINK);

        final DiagnosticElement cpInfo = mapLinkedComParams(logicallink);
        if (!cpInfo.getChildren().isEmpty()) {
            llInfo.getChildren().add(cpInfo);
        }

        final DiagnosticElement baseVariantDescription = createReferencedLayerDescription(logicallink);
        if (baseVariantDescription != null) {
            llInfo.getChildren().add(baseVariantDescription);
        }

        return llInfo;
    }

    private DiagnosticElement mapLinkedComParams(final LOGICALLINK logicallink) {
        final DiagnosticElement result = new DiagnosticElement();
        result.setName("Linked Comparams");
        result.setLocation(!logicallink.getLinkcomparamrefs().isEmpty() ? logicallink.getLinkcomparamrefs().get(0).getLocation() : null);
        result.setType(Types.LIST);

        for (final LINKCOMPARAMREF link : logicallink.getLinkcomparamrefs()) {
            vehicleinfospec.getIndex().resolveLink(link, BASECOMPARAM.class).ifPresent(param -> {
                result.getChildren().addAll(mapLink(link, param));
            });
        }
        return result;
    }

    private List<DiagnosticElement> mapLink(final LINKCOMPARAMREF link, final BASECOMPARAM param) {
        if (param instanceof COMPLEXCOMPARAM) {
            if (link.getCOMPLEXVALUE() != null) {
                return comParamMapper.mapComplexComParam((COMPLEXCOMPARAM) param, link.getCOMPLEXVALUE());
            }
            return comParamMapper.mapComplexComParam((COMPLEXCOMPARAM) param);
        }
        if (param instanceof COMPARAM) {
            if (link.getSIMPLEVALUE() != null) {
                return Collections.singletonList(comParamMapper.mapComParam((COMPARAM) param, link.getSIMPLEVALUE().getValue()));
            }
            return Collections.singletonList(comParamMapper.mapComParam((COMPARAM) param));
        }
        return emptyList();
    }


    private DiagnosticElement createReferencedLayerDescription(final LOGICALLINK logicallink) {
        if (logicallink.getBASEVARIANTREF() != null) {
            return vehicleinfospec.getIndex().findLayerProxyForOdxLink(logicallink.getBASEVARIANTREF())
                    .map(this::createLayerDto)
                    .orElse(null);
        }

        if (logicallink.getFUNCTIONALGROUPREF() != null) {
            return vehicleinfospec.getIndex().findLayerProxyForOdxLink(logicallink.getFUNCTIONALGROUPREF())
                    .map(this::createLayerDto)
                    .orElse(null);
        }

        return null;
    }

    private DiagnosticElement createLayerDto(final LayerProxy bvProxy) {
        final DiagnosticElement element = new DiagnosticElement();
        element.setName(bvProxy.getShortName());
        element.setLocation(bvProxy.getLocation());
        element.setType(bvProxy.getLayerType());
        element.setRevealable(true);
        return element;
    }

}
