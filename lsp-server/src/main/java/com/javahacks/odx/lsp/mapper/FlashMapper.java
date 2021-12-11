package com.javahacks.odx.lsp.mapper;

import com.javahacks.odx.index.LocationAware;
import com.javahacks.odx.lsp.dtos.DiagnosticElement;
import com.javahacks.odx.model.*;

import javax.xml.bind.DatatypeConverter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Function;

import static java.util.Comparator.comparing;
import static java.util.stream.Collectors.toList;

/**
 * Maps all information for a single {@link FLASH} category that is shown in the client
 */
public class FlashMapper {
    private final FLASH flash;

    public FlashMapper(final FLASH flash) {
        this.flash = flash;
    }

    public List<DiagnosticElement> map() {
        final List<DiagnosticElement> elements = new ArrayList<>();
        elements.addAll(mapEcuMems());
        elements.addAll(mapEcuMemConnectors());
        return elements;
    }

    private List<DiagnosticElement> mapEcuMemConnectors() {
        final ChildrenMapper<ECUMEMCONNECTOR> ecuMemConnectorChildren = appendingChildrenMapper(
                childrenMapper(ECUMEMCONNECTOR::getSESSIONDESCS, map(Types.SERVICE, SESSIONDESC::getSHORTNAME)));
        return mapList(flash.getEcumemconnectors(), map(Types.LOGICAL_LINK, ECUMEMCONNECTOR::getSHORTNAME, ecuMemConnectorChildren));
    }

    private List<DiagnosticElement> mapEcuMems() {
        final ChildrenMapper<DATABLOCK> childrenMapper = appendingChildrenMapper(
                childrenMapper(DATABLOCK::getSEGMENTS, map(Types.STACK, this::segmentName)),
                childrenMapper(DATABLOCK::getSECURITYS, map(Types.KEY, this::securityName)));
        return mapList(flash.getECUMEMS(), map(Types.COMPLEX, ECUMEM::getSHORTNAME, appendingChildrenMapper(
                childrenMapper((ECUMEM ecumem) -> ecumem.getMEM().getSESSIONS(), map(Types.SERVICE, SESSION::getSHORTNAME)),
                childrenMapper((ECUMEM ecumem) -> ecumem.getMEM().getDATABLOCKS(), map(Types.BYTE_FIELD, DATABLOCK::getSHORTNAME, childrenMapper)),
                childrenMapper((ECUMEM ecumem) -> ecumem.getMEM().getFLASHDATAS(), map(Types.SHARED_DATA, FLASHDATA::getSHORTNAME)))));
    }

    private String segmentName(final SEGMENT segment) {
        String name = segment.getSHORTNAME();
        if (segment.getSOURCESTARTADDRESS() != null) {
            name += " start: " + DatatypeConverter.printHexBinary(segment.getSOURCESTARTADDRESS());
        }
        if (segment.getSOURCEENDADDRESS() != null) {
            name += " end: " + DatatypeConverter.printHexBinary(segment.getSOURCEENDADDRESS().getValue());
        }
        if (segment.getUNCOMPRESSEDSIZE() != null) {
            name += " size: " + segment.getUNCOMPRESSEDSIZE().getValue();
        }
        return name;
    }

    private String securityName(final SECURITY security) {
        if (security.getFWCHECKSUM() != null) {
            return "Checksum: " + security.getFWCHECKSUM().getValue();
        }
        if (security.getFWSIGNATURE() != null) {
            return "Signature: " + security.getFWSIGNATURE().getValue();
        }
        if (security.getSECURITYMETHOD() != null) {
            return "Method: " + security.getSECURITYMETHOD().getValue();
        }
        return "Security";
    }

    private <T extends LocationAware> Function<T, DiagnosticElement> map(final String type, final Function<T, String> nameMapper) {
        return map(type, nameMapper, object -> Collections.emptyList());
    }

    private <T extends LocationAware> Function<T, DiagnosticElement> map(final String type, final Function<T, String> nameMapper, final ChildrenMapper<T> childMapper) {
        return object -> {
            final DiagnosticElement element = new DiagnosticElement();
            element.setName(nameMapper.apply(object));
            element.setLocation(object.getLocation());
            element.setType(type);
            element.setChildren(childMapper.apply(object));
            return element;
        };
    }

    private <T extends LocationAware, C extends LocationAware> ChildrenMapper<T> childrenMapper(final Function<T, List<C>> childrenExtractor,
                                                                                                final Function<C, DiagnosticElement> childMapper) {
        return object -> {
            final List<C> children = childrenExtractor.apply(object);
            return mapList(children, childMapper);
        };
    }

    @SafeVarargs
    private final <T extends LocationAware> ChildrenMapper<T> appendingChildrenMapper(final ChildrenMapper<T>... mappers) {
        return object -> {
            final List<DiagnosticElement> children = new ArrayList<>();
            for (final ChildrenMapper<T> mapper : mappers) {
                children.addAll(mapper.apply(object));
            }
            return children;
        };
    }

    private <T> List<DiagnosticElement> mapList(final List<T> elements, final Function<T, DiagnosticElement> mapper) {
        if (elements == null) {
            return Collections.emptyList();
        }
        return elements.stream()
                .map(mapper)
                .filter(elem -> elem.getName() != null)
                .sorted(comparing(DiagnosticElement::getName))
                .collect(toList());
    }

    private interface ChildrenMapper<T extends LocationAware> extends Function<T, List<DiagnosticElement>> {

    }
}
