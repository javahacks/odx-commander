package com.javahacks.odx.lsp.mapper;

import com.google.common.base.Strings;
import com.javahacks.odx.index.DocumentIndex;
import com.javahacks.odx.lsp.dtos.DiagnosticElement;
import com.javahacks.odx.model.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static com.javahacks.odx.lsp.mapper.Types.mapPhysicalType;

/**
 * Mapper for either {@link COMPLEXCOMPARAM} or {@link COMPARAM} instances
 */
public class ComParamMapper {
    public static final String OVERRIDDEN_PREFIX = "\u26A0  ";
    private final DocumentIndex index;

    ComParamMapper(final DocumentIndex index) {
        this.index = index;
    }


    /**
     * Maps a single {@link COMPARAM}. The physical default value is not overridden.
     */
    DiagnosticElement mapComParam(final COMPARAM comparam) {
        return mapComParam(comparam, null);
    }

    /**
     * Maps a single {@link COMPARAM}. The physical default value is overridden with new value if not <code>null</code>.
     */
    DiagnosticElement mapComParam(final COMPARAM comparam, final String newValue) {
        return mapComParam(comparam, comparam.getPHYSICALDEFAULTVALUE(), newValue);
    }

    private DiagnosticElement mapComParam(final COMPARAM comparam, final String defaultValue, final String newValue) {
        final DiagnosticElement element = new DiagnosticElement();
        element.setLocation(comparam.getLocation());
        final String value = newValue != null ? newValue : defaultValue;
        element.setName(overriddenPrefix(defaultValue, newValue) + comparam.getSHORTNAME() + assignmentSuffix(value));

        index.resolveLink(comparam.getDATAOBJECTPROPREF(), DATAOBJECTPROP.class).ifPresent(dop -> {
            element.setType(mapPhysicalType(dop.getPHYSICALTYPE()));
            if (dop.getUNITREF() != null) {
                attachUnit(dop, element);
            }
        });

        return element;
    }


    List<DiagnosticElement> mapComplexComParam(final COMPLEXCOMPARAM complexParam) {
        return mapComplexComParam(complexParam, null);
    }

    /**
     * Maps a single {@link COMPLEXCOMPARAM}. The physical default values are overridden with values of given {@link COMPLEXVALUE}.
     *
     * @return
     */
    List<DiagnosticElement> mapComplexComParam(final COMPLEXCOMPARAM complexParam, final COMPLEXVALUE newValue) {
        final List<SIMPLEVALUE> defaultValues = flattenComplexValue(complexParam.getComplexphysicaldefaultvalues());
        final List<SIMPLEVALUE> newValues = flattenComplexValue(newValue);

        final List<COMPARAM> comParams = flattenComplexComParam(complexParam);

        final List<DiagnosticElement> result = new ArrayList<>();


        for (int i = 0; i < comParams.size(); i++) {
            final COMPARAM comparam = comParams.get(i);

            final String defaultValue = defaultValues.size() > i ? defaultValues.get(i).getValue() : comparam.getPHYSICALDEFAULTVALUE();
            final String value = newValues.size() > i ? newValues.get(i).getValue() : defaultValue;

            result.add(mapComParam(comparam, defaultValue, value));
        }

        return result;

    }

    private void attachUnit(final DATAOBJECTPROP dop, final DiagnosticElement element) {
        index.resolveLink(dop.getUNITREF(), UNIT.class).ifPresent(unit -> {
            if (!Strings.isNullOrEmpty(unit.getDISPLAYNAME())) {
                element.setName(element.getName() + " " + unit.getDISPLAYNAME());
            }
        });
    }


    private String assignmentSuffix(final String value) {
        if (Strings.isNullOrEmpty(value)) {
            return "";
        }

        return " = " + value.substring(0, Math.min(value.length(), 30));
    }

    private String overriddenPrefix(final String defaultValue, final String newValue) {
        if (newValue != null && !newValue.equals(defaultValue)) {
            return OVERRIDDEN_PREFIX;
        }

        return "";
    }

    /**
     * Flattens a complex {@link COMPLEXCOMPARAM} structure to a list of simple {@link COMPARAM} instances
     */
    private List<COMPARAM> flattenComplexComParam(final COMPLEXCOMPARAM complexParam) {
        final List<COMPARAM> result = new ArrayList<>();

        for (final BASECOMPARAM baseComParam : complexParam.getCOMPARAMOrCOMPLEXCOMPARAM()) {

            if (baseComParam instanceof COMPARAM) {
                result.add((COMPARAM) baseComParam);
            }

            if (baseComParam instanceof COMPLEXCOMPARAM) {
                result.addAll(flattenComplexComParam((COMPLEXCOMPARAM) baseComParam));
            }
        }

        return result;
    }

    /**
     * Flattens a {@link COMPLEXVALUE} structure to a list of simple {@link SIMPLEVALUE} instances
     */
    private List<SIMPLEVALUE> flattenComplexValue(final COMPLEXVALUE complexValue) {
        if (complexValue == null) {
            return Collections.emptyList();
        }

        final List<SIMPLEVALUE> result = new ArrayList<>();

        for (final Object object : complexValue.getSIMPLEVALUEOrCOMPLEXVALUE()) {
            if (object instanceof SIMPLEVALUE) {
                result.add((SIMPLEVALUE) object);
            }
            if (object instanceof COMPLEXVALUE) {
                result.addAll(flattenComplexValue((COMPLEXVALUE) object));
            }
        }

        return result;
    }
}
