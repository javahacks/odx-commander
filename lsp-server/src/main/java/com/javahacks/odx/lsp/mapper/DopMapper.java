package com.javahacks.odx.lsp.mapper;

import com.javahacks.odx.index.AbstractLinkTarget;
import com.javahacks.odx.lsp.dtos.DiagnosticElement;
import com.javahacks.odx.model.TABLE;
import com.javahacks.odx.model.*;

import javax.xml.bind.JAXBElement;
import java.util.*;
import java.util.stream.Collectors;

import static com.javahacks.odx.lsp.mapper.Types.*;
import static com.javahacks.odx.utils.OdxUtils.resolveODXLink;
import static com.javahacks.odx.utils.OdxUtils.resolveSnRef;

/**
 * Maps all information for a single {@link DOPBASE} that is shown in the client
 */
public class DopMapper {

    private final ParamMapper paramMapper;
    private final DIAGLAYER diaglayer;

    DopMapper(final ParamMapper paramMapper, final DIAGLAYER diaglayer) {
        this.paramMapper = paramMapper;
        this.diaglayer = diaglayer;
    }

    /**
     * Searches for the dop by either given {@link SNREF} or {@link ODXLINK} and
     * maps it onto a structure of {@link DiagnosticElement}
     */
    void lookupAndMapDop(final DiagnosticElement parent, final SNREF snref, final ODXLINK dopRef) {
        final DOPBASE dop = lookupDopRef(snref, dopRef);
        if (dop != null) {
            final DiagnosticElement dopParam = mapDop(dop);
            parent.setType(dopParam.getType());
            dopParam.setType(getDocumentType(dop.getLayer()));
            parent.getChildren().add(dopParam);
        } else {
            parent.setName(INVALID);
            parent.setType(INVALID);
        }
    }

    private DOPBASE lookupDopRef(final SNREF snref, final ODXLINK odxlink) {
        final AbstractLinkTarget target = resolveTarget(snref, odxlink);
        if(target instanceof  DOPBASE){
            return (DOPBASE) target;
        }
        return null;
    }

    private AbstractLinkTarget resolveTarget(final SNREF snref, final ODXLINK odxlink) {
        if (snref != null ) {
            return resolveSnRef(diaglayer,snref).orElse(null);
        }
        if (odxlink != null) {
            return resolveODXLink(odxlink,AbstractLinkTarget.class).orElse(null);
        }
        return null;
    }

    private DiagnosticElement mapDop(final DOPBASE dop) {
        if (dop instanceof COMPLEXDOP) {
            return mapComplexDop((COMPLEXDOP) dop);
        }

        if (dop instanceof DTCDOP) {
            return mapDtcDop((DTCDOP) dop);
        }

        return mapSimpleDop((DATAOBJECTPROP) dop);
    }

    private DiagnosticElement mapSimpleDop(final DATAOBJECTPROP dop) {
        final DiagnosticElement result = new DiagnosticElement();
        result.setLocation(dop.getLocation());
        if (dop.getCOMPUMETHOD() != null && dop.getCOMPUMETHOD().getCATEGORY() != null) {
            result.setName(dop.getCOMPUMETHOD().getCATEGORY().name());
        } else {
            result.setName(dop.getSHORTNAME());
        }

        if (dop.getUNITREF() != null) {
            diaglayer.getIndex().resolveLink(dop.getUNITREF(), UNIT.class).ifPresent(unit -> {
                result.setName(result.getName() + " [" + unit.getDISPLAYNAME() + "]");
            });
        }

        result.setType(Types.mapPhysicalType(dop.getPHYSICALTYPE()));
        return result;
    }

    private DiagnosticElement mapDtcDop(final DTCDOP dop) {
        final DiagnosticElement result = new DiagnosticElement();
        result.setLocation(dop.getLocation());
        result.setType(Types.DTC);
        final int dtcNumber = dop.getLinkeddtcdops().size()
                + (dop.getDTCS() != null ? dop.getDTCS().getDTCPROXY().size() : 0);
        result.setName("DTC-DOP (" + dtcNumber + ")");
        return result;
    }

    private DiagnosticElement mapComplexDop(final COMPLEXDOP dop) {
        if (dop instanceof STRUCTURE) {
            return mapStructure(dop);
        }
        if (dop instanceof ENDOFPDUFIELD) {
            return mapEndOfPduField(dop);
        }
        if (dop instanceof DYNAMICLENGTHFIELD) {
            return mapDynamicLengthField(dop);
        }
        if (dop instanceof DYNAMICENDMARKERFIELD) {
            return mapDynamicEndMarkerField(dop);
        }
        if (dop instanceof STATICFIELD) {
            return mapStaticField(dop);
        }
        if (dop instanceof ENVDATA) {
            return mapEnvData(dop);
        }
        if (dop instanceof ENVDATADESC) {
            return mapEnvDataDesc(dop);
        }
        if (dop instanceof MUX) {
            return mapMux(dop);
        }

        return mapComplexDopBase(dop);
    }

    private DiagnosticElement mapDynamicEndMarkerField(final COMPLEXDOP dop) {
        final DiagnosticElement result = mapComplexDopBase(dop);
        final DYNAMICENDMARKERFIELD field = (DYNAMICENDMARKERFIELD) dop;
        result.setName("DYNAMIC ENDMARKER FIELD");
        final DOPBASE structure = lookupDopRef(field.getBASICSTRUCTURESNREF(), field.getBASICSTRUCTUREREF());
        if (structure instanceof STRUCTURE) {
            result.getChildren().addAll(paramMapper.mapParams(((STRUCTURE) structure).getParams()));
        }
        return result;
    }

    private DiagnosticElement mapDynamicLengthField(final COMPLEXDOP dop) {
        final DiagnosticElement result = mapComplexDopBase(dop);
        result.setName("DYNAMIC LENGTH FIELD");
        return result;
    }

    private DiagnosticElement mapEndOfPduField(final COMPLEXDOP dop) {
        final DiagnosticElement result = mapComplexDopBase(dop);

        final ENDOFPDUFIELD endofpdufield = (ENDOFPDUFIELD) dop;
        result.setName("END OF PDU FIELD (MIN: " + selfOrUndefined(endofpdufield.getMINNUMBEROFITEMS()) + " MAX: "
                + selfOrUndefined(endofpdufield.getMAXNUMBEROFITEMS()) + ")");
        final DOPBASE structure = lookupDopRef(endofpdufield.getBASICSTRUCTURESNREF(),
                endofpdufield.getBASICSTRUCTUREREF());
        if (structure instanceof STRUCTURE) {
            result.getChildren().addAll(paramMapper.mapParams(((STRUCTURE) structure).getParams()));
        }

        return result;
    }

    private DiagnosticElement mapStaticField(final COMPLEXDOP dop) {
        final DiagnosticElement result = mapComplexDopBase(dop);
        final STATICFIELD staticfield = (STATICFIELD) dop;
        result.setName("STATIC FIELD (NUMBER-OF-ITEMS: " + staticfield.getFIXEDNUMBEROFITEMS() + " BYTE-SIZE: "
                + staticfield.getITEMBYTESIZE() + ")");
        return result;
    }

    private DiagnosticElement mapEnvData(final COMPLEXDOP dop) {
        final DiagnosticElement result = mapComplexDopBase(dop);
        result.setName("ENVDATA");
        return result;
    }

    private DiagnosticElement mapEnvDataDesc(final COMPLEXDOP dop) {
        final DiagnosticElement result = mapComplexDopBase(dop);
        result.setName("ENVDATADESC");
        return result;
    }

    private DiagnosticElement mapMux(final COMPLEXDOP dop) {
        final DiagnosticElement result = mapComplexDopBase(dop);
        result.setName("MUX");
        return result;
    }

    private DiagnosticElement mapStructure(final COMPLEXDOP dop) {
        final DiagnosticElement result = mapComplexDopBase(dop);
        final STRUCTURE structure = (STRUCTURE) dop;
        result.setName("STRUCTURE");
        result.getChildren().addAll(paramMapper.mapParams(structure.getParams()));
        return result;
    }

    private DiagnosticElement mapComplexDopBase(final COMPLEXDOP dop) {
        final DiagnosticElement result = new DiagnosticElement();
        result.setLocation(dop.getLocation());
        result.setType(COMPLEX);
        return result;
    }


    private Object selfOrUndefined(final Object o) {
        return !Objects.isNull(o) ? o : Types.UNDEFINED;
    }

    List<DiagnosticElement> mapTableRowsForTableKey(final TABLEKEY tablekey) {
        final List<TABLEROW> rows = new ArrayList<TABLEROW>();

        for (final JAXBElement jaxbElement : tablekey.getRest()) {

            if ("TABLE-REF".equals(jaxbElement.getName().getLocalPart()) && jaxbElement.getValue() instanceof ODXLINK) {
                diaglayer.getIndex().resolveLink((ODXLINK) jaxbElement.getValue(), com.javahacks.odx.model.TABLE.class).ifPresent(table -> {
                    rows.addAll(directRows(table));
                    rows.addAll(referencedRows(table));
                });
            }

            if ("TABLE-SNREF".equals(jaxbElement.getName().getLocalPart()) && jaxbElement.getValue() instanceof SNREF) {
                resolveTableByShortName((SNREF) jaxbElement.getValue()).ifPresent(table -> {
                    rows.addAll(directRows(table));
                    rows.addAll(referencedRows(table));
                });
            }

            if ("TABLE-ROW-REF".equals(jaxbElement.getName().getLocalPart()) && jaxbElement.getValue() instanceof ODXLINK) {
                diaglayer.getIndex().resolveLink((ODXLINK) jaxbElement.getValue(), TABLEROW.class).ifPresent(row -> {
                    rows.add(row);
                });
            }

            if ("TABLE-ROW-SNREF".equals(jaxbElement.getName().getLocalPart()) && jaxbElement.getValue() instanceof SNREF) {
                final SNREF snref = (SNREF) jaxbElement.getValue();
                if (snref.getSHORTNAME() != null) {
                    rows.addAll(resolveAllRowsByShorName(snref.getSHORTNAME()));
                }
            }
        }

        return rows.stream().map(this::mapRow)
                .collect(Collectors.toList());

    }

    private List<TABLEROW> resolveAllRowsByShorName(final String shortName) {
        return diaglayer.query().visibleTables().stream()
                .map(this::directRows)
                .flatMap(Collection::stream)
                .filter(row -> shortName.equals(row.getSHORTNAME()))
                .collect(Collectors.toList());
    }

    private Optional<com.javahacks.odx.model.TABLE> resolveTableByShortName(final SNREF snref) {
        return diaglayer.query().visibleTables().stream()
                .filter(table -> table.getSHORTNAME() != null)
                .filter(table -> table.getSHORTNAME().equals(snref.getSHORTNAME()))
                .findFirst();
    }

    private DiagnosticElement mapRow(final TABLEROW row) {
        final DiagnosticElement diagElement = new DiagnosticElement();
        diagElement.setLocation(row.getLocation());
        diagElement.setName(row.getSHORTNAME());
        diagElement.setType(TABLE_ROW);

        final DOPBASE structure = lookupDopRef(row.getSTRUCTURESNREF(), row.getSTRUCTUREREF());
        if (structure instanceof STRUCTURE) {
            diagElement.getChildren().addAll(paramMapper.mapParams(((STRUCTURE) structure).getParams()));
        }
        return diagElement;
    }

    /**
     * Collects all table rows referenced by {@link ODXLINK}
     */
    private Collection<TABLEROW> referencedRows(final TABLE table) {
        return table.getROWWRAPPER().stream()
                .filter(ODXLINK.class::isInstance)
                .map(ODXLINK.class::cast)
                .map(it -> diaglayer.getIndex().resolveLink(it, TABLEROW.class))
                .filter(Optional::isPresent)
                .map(o -> o.get())
                .collect(Collectors.toList());
    }

    /**
     * Collects all table rows directly referenced in table
     */
    private Collection<TABLEROW> directRows(final TABLE table) {
        return table.getROWWRAPPER().stream()
                .filter(TABLEROW.class::isInstance)
                .map(TABLEROW.class::cast)
                .collect(Collectors.toList());
    }
}
