package com.javahacks.odx.lsp.mapper;

import com.javahacks.odx.index.XmlElementLocation;
import com.javahacks.odx.lsp.dtos.DiagnosticElement;
import com.javahacks.odx.model.COMPARAMSPEC;
import com.javahacks.odx.model.ODXLINK;
import com.javahacks.odx.model.PROTSTACK;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Mapper for {@link COMPARAMSPEC} related information
 */
public class ComParamSpecMapper {
    private final COMPARAMSPEC spec;
    private final ComParamMapper comParamMapper;

    public ComParamSpecMapper(final COMPARAMSPEC spec) {
        this.spec = spec;
        this.comParamMapper = new ComParamMapper(spec.getIndex());
    }

    public List<DiagnosticElement> map() {
        final List<DiagnosticElement> elements = new ArrayList<>();

        if (!spec.getProtocolStacks().isEmpty()) {
            elements.addAll(mapProtocolStacks());
        }
        if (!spec.getComparams().isEmpty()) {
            elements.addAll(mapComParams());
        }
        return elements;
    }

    private List<DiagnosticElement> mapComParams() {
        return spec.getComparams().stream()
                .map(comParamMapper::mapComParam)
                .sorted(Comparator.comparing(DiagnosticElement::getName))
                .collect(Collectors.toList());
    }

    private List<DiagnosticElement> mapProtocolStacks() {
        return spec.getProtocolStacks().stream()
                .map(this::mapStack)
                .collect(Collectors.toList());
    }

    private DiagnosticElement mapStack(final PROTSTACK protstack) {
        final DiagnosticElement stackElement = new DiagnosticElement();
        stackElement.setName(protstack.getSHORTNAME());
        stackElement.setLocation(protstack.getLocation());
        stackElement.setType(Types.STACK);
        stackElement.getChildren().addAll(mapSubSetRefs(protstack));
        return stackElement;
    }

    private List<DiagnosticElement> mapSubSetRefs(final PROTSTACK protstack) {
        final List<DiagnosticElement> subsets = new ArrayList<>();

        for (final ODXLINK odxlink : protstack.getComparamsubsetrefs()) {
            spec.getIndex().findCategoryProxyForOdxLink(odxlink).ifPresent(layerProxy -> {
                final DiagnosticElement subsetElement = new DiagnosticElement();
                subsetElement.setName(layerProxy.getShortName());
                subsetElement.setLocation(new XmlElementLocation(layerProxy.getIndexPath()));
                subsets.add(subsetElement);
            });
        }

        return subsets;
    }


}
