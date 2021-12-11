package com.javahacks.odx.lsp.mapper;

import com.google.common.base.Strings;
import com.javahacks.odx.lsp.dtos.DiagnosticElement;
import com.javahacks.odx.model.RESERVED;
import com.javahacks.odx.model.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static com.javahacks.odx.lsp.mapper.Types.*;
import static com.javahacks.odx.utils.OdxUtils.convertTextToHexStringOrDefault;

/**
 * Maps all information for a single {@link PARAM} that is shown in the client
 */
public class ParamMapper {
    private static final Logger LOGGER = LoggerFactory.getLogger(ParamMapper.class);
    private final DIAGLAYER diaglayer;
    private final DopMapper dopMapper;

    public ParamMapper(final DIAGLAYER diaglayer) {
        this.diaglayer = diaglayer;
        this.dopMapper = new DopMapper(this, diaglayer);
    }

    public List<DiagnosticElement> mapParams(final List<PARAM> params) {
        return params.stream().map(param -> mapParam(param, params)).filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    private DiagnosticElement mapParam(final PARAM param, final List<PARAM> paramsContext) {
        if (param instanceof VALUE) {
            return mapValue((VALUE) param);
        }

        if (param instanceof RESERVED) {
            return mapSimpleParam(param, Types.RESERVED);
        }

        if (param instanceof DYNAMIC) {
            return mapSimpleParam(param, UNKNOWN);
        }
        if (param instanceof MATCHINGREQUESTPARAM) {
            return mapSimpleParam(param, MATCH);
        }

        if (param instanceof TABLEKEY) {
            return mapSimpleParam(param, KEY);
        }

        if (param instanceof TABLESTRUCT) {
            return mapTableStruct((TABLESTRUCT) param, paramsContext);
        }

        if (param instanceof TABLEENTRY) {
            return mapSimpleParam(param, TABLE_ENTRY);
        }

        if (param instanceof CODEDCONST) {
            return mapCodedConstParam((CODEDCONST) param);
        }

        if (param instanceof PHYSCONST) {
            return mapPhysicalConst((PHYSCONST) param);
        }

        if (param instanceof NRCCONST) {
            return mapNrcConst((NRCCONST) param);
        }

        if (param instanceof SYSTEM) {
            return mapSystem((SYSTEM) param);
        }

        if (param instanceof LENGTHKEY) {
            return mapLengthKey((LENGTHKEY) param);
        }

        return null;
    }

    private DiagnosticElement mapValue(final VALUE param) {
        final DiagnosticElement result = new DiagnosticElement();
        result.setLocation(param.getLocation());
        result.setName(mapName(param) + getShortNameSuffix(param));
        dopMapper.lookupAndMapDop(result, param.getDOPSNREF(), param.getDOPREF());
        return result;
    }

    private DiagnosticElement mapLengthKey(final LENGTHKEY param) {
        final DiagnosticElement result = new DiagnosticElement();
        result.setLocation(param.getLocation());
        result.setName(mapName(param));
        dopMapper.lookupAndMapDop(result, param.getDOPSNREF(), param.getDOPREF());
        return result;
    }

    private DiagnosticElement mapSystem(final SYSTEM param) {
        final DiagnosticElement result = new DiagnosticElement();
        result.setLocation(param.getLocation());
        result.setName(mapName(param));
        dopMapper.lookupAndMapDop(result, param.getDOPSNREF(), param.getDOPREF());
        return result;
    }

    private DiagnosticElement mapSimpleParam(final PARAM param, final String type) {
        final DiagnosticElement result = new DiagnosticElement();
        result.setLocation(param.getLocation());
        result.setName(mapName(param));
        result.setType(type);
        return result;
    }

    private DiagnosticElement mapTableStruct(final TABLESTRUCT tableStruct, final List<PARAM> paramsContext) {
        final DiagnosticElement result = mapSimpleParam(tableStruct, TABLE);
        if (tableStruct.getTABLEKEYREF() != null) {
            diaglayer.getIndex().resolveLink(tableStruct.getTABLEKEYREF(), TABLEKEY.class).ifPresent(tableKey ->
                    result.getChildren().addAll(dopMapper.mapTableRowsForTableKey(tableKey))
            );

        } else if (tableStruct.getTABLEKEYSNREF() != null) {
            paramsContext.stream()
                    .filter(param -> param.getSHORTNAME().equals(tableStruct.getTABLEKEYSNREF().getSHORTNAME()) && param instanceof TABLEKEY)
                    .map(param -> (TABLEKEY) param).findFirst().ifPresent(tableKey ->
                            result.getChildren().addAll(dopMapper.mapTableRowsForTableKey(tableKey))
                    );
        }
        return result;
    }

    private DiagnosticElement mapNrcConst(final NRCCONST param) {
        final DiagnosticElement result = new DiagnosticElement();
        result.setLocation(param.getLocation());
        result.setName(mapName(param));
        result.setType(mapCodedType(param.getDIAGCODEDTYPE()));
        return result;
    }

    private DiagnosticElement mapPhysicalConst(final PHYSCONST param) {
        final DiagnosticElement result = new DiagnosticElement();
        result.setLocation(param.getLocation());
        result.setName(mapName(param) + " (" + param.getPHYSCONSTANTVALUE() + ")");
        dopMapper.lookupAndMapDop(result, param.getDOPSNREF(), param.getDOPREF());
        return result;
    }

    private DiagnosticElement mapCodedConstParam(final CODEDCONST param) {
        final DiagnosticElement result = new DiagnosticElement();
        result.setLocation(param.getLocation());
        result.setName(mapName(param) + " (" + getCodedConstValue(param) + ")");
        result.setType(mapCodedType(param.getDIAGCODEDTYPE()));
        return result;
    }

    private String getCodedConstValue(final CODEDCONST codedconst) {
        if (isIntegerType(codedconst.getDIAGCODEDTYPE()) && codedconst.getCODEDVALUE() != null) {
            return convertTextToHexStringOrDefault(codedconst.getCODEDVALUE(), codedconst.getCODEDVALUE());
        }
        return codedconst.getCODEDVALUE() != null ? codedconst.getCODEDVALUE() : UNDEFINED;
    }


    private String getShortNameSuffix(final VALUE param) {
        if (Strings.isNullOrEmpty(param.getSHORTNAME())) {
            return "";
        }
        return " (" + param.getSHORTNAME() + ")";
    }


    public static String mapName(final PARAM param) {
        if (param instanceof TABLESTRUCT) {
            return "TABLE STRUCT";
        }
        if (param instanceof TABLEENTRY) {
            return "TABLE ENTRY";
        }
        if (param instanceof TABLEKEY) {
            return "TABLE KEY";
        }
        if (param instanceof CODEDCONST) {
            return "CODED CONST";
        }
        if (param instanceof PHYSCONST) {
            return "PHYSICAL CONST";
        }
        if (param instanceof NRCCONST) {
            return "NRC CONST";
        }
        if (param instanceof LENGTHKEY) {
            return "LENGTH KEY";
        }
        if (param instanceof MATCHINGREQUESTPARAM) {
            return "MATCHING REQUEST";
        }

        return param.getClass().getSimpleName().toUpperCase();
    }

}
