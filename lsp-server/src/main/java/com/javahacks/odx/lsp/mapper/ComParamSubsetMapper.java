package com.javahacks.odx.lsp.mapper;

import com.google.common.collect.Streams;
import com.javahacks.odx.lsp.dtos.DiagnosticElement;
import com.javahacks.odx.model.COMPARAMSUBSET;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;


/**
 * Mapper for {@link COMPARAMSUBSET} related information
 */
public class ComParamSubsetMapper {
    private final COMPARAMSUBSET subset;
    private final ComParamMapper comParamMapper;

    public ComParamSubsetMapper(final COMPARAMSUBSET subset) {
        this.subset = subset;
        this.comParamMapper = new ComParamMapper(subset.getIndex());
    }

    public List<DiagnosticElement> map() {
        final Stream<DiagnosticElement> complexParams = subset.getComplexcomparams().stream()
                .map(comParamMapper::mapComplexComParam)
                .flatMap(List::stream);

        final Stream<DiagnosticElement> comParams = subset.getComparams().stream()
                .map(comParamMapper::mapComParam);

        return Streams.concat(complexParams, comParams)
                .sorted(Comparator.comparing(DiagnosticElement::getName))
                .collect(Collectors.toList());
    }


}
