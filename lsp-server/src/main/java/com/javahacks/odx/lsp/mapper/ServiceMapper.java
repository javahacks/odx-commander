package com.javahacks.odx.lsp.mapper;

import com.javahacks.odx.lsp.dtos.DiagnosticElement;
import com.javahacks.odx.model.*;

import java.util.ArrayList;
import java.util.List;

import static com.javahacks.odx.lsp.mapper.Types.getDocumentType;

/**
 * Maps all relevant information for a single {@link DIAGSERVICE} that is shown in the client
 */
public class ServiceMapper {
    private final DIAGLAYER diaglayer;
    private final DIAGSERVICE diagservice;

    public ServiceMapper(final DIAGLAYER diaglayer, final DIAGSERVICE diagservice) {
        this.diaglayer = diaglayer;
        this.diagservice = diagservice;
    }

    public List<DiagnosticElement> map() {
        final ParamMapper paramMapper = new ParamMapper(diaglayer);
        final List<DiagnosticElement> details = new ArrayList<>();

        final DiagnosticElement request = createRequest(paramMapper, diagservice);
        if (request != null) {
            details.add(request);
        }
        details.addAll(createPositiveResponses(paramMapper, diagservice));
        details.addAll(createNegativeResponses(paramMapper, diagservice));

        return details;
    }


    private DiagnosticElement createRequest(final ParamMapper paramMapper, final DIAGSERVICE diagservice) {
        final ODXLINK odxlink = diagservice.getREQUESTREF();
        if (odxlink == null) {
            return null;
        }
        final DiagnosticElement result = new DiagnosticElement();
        result.setName("Request");
        diaglayer.getIndex().resolveLink(odxlink, REQUEST.class).ifPresent(request -> {
            result.getChildren().addAll(paramMapper.mapParams(request.getParams()));
            result.setType(getDocumentType(request.getLayer()));
            result.setLocation(request.getLocation());
        });
        return result;
    }

    private List<DiagnosticElement> createPositiveResponses(final ParamMapper paramMapper, final DIAGSERVICE diagservice) {
        final List<DiagnosticElement> responses = new ArrayList<>();
        for (final ODXLINK odxlink : diagservice.getPosresponserefs()) {
            diaglayer.getIndex().resolveLink(odxlink, RESPONSE.class).ifPresent(response -> {
                responses.add(mapResponse(paramMapper, response, true));
            });
        }
        return responses;
    }


    private List<DiagnosticElement> createNegativeResponses(final ParamMapper paramMapper, final DIAGSERVICE diagservice) {
        final List<DiagnosticElement> responses = new ArrayList<>();

        for (final ODXLINK odxlink : diagservice.getNegresponserefs()) {
            diaglayer.getIndex().resolveLink(odxlink, RESPONSE.class).ifPresent(response -> {
                responses.add(mapResponse(paramMapper, response, false));
            });
        }

        if (responses.isEmpty()) {
            for (final RESPONSE response : diagservice.getLayer().query().visibleGlobalNegResponses()) {
                responses.add(mapResponse(paramMapper, response, false));
            }
        }

        return responses;
    }

    private DiagnosticElement mapResponse(final ParamMapper paramMapper, final RESPONSE response, final boolean positive) {
        final DiagnosticElement result = new DiagnosticElement();
        result.setName(positive ? "Positive Response" : "Negative Response");
        result.setLocation(response.getLocation());
        result.setType(getDocumentType(response.getLayer()));
        result.setChildren(paramMapper.mapParams(response.getParams()));
        return result;
    }
}
