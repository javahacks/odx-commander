package com.javahacks.odx.lsp.mapper;

import com.javahacks.odx.index.IndexableDocument;
import com.javahacks.odx.model.*;

import static java.util.Objects.isNull;

/**
 * Represents the type for a {@link com.javahacks.odx.lsp.dtos.DiagnosticElement}
 * <p>
 * For each type an appropriate icon with same name must be available under
 * resources/types in the client application.
 */
public class Types {
    static final String UNDEFINED = "UNDEFINED";
    static final String UNKNOWN = "UNKNOWN";
    static final String INVALID = "INVALID";

    static final String LIST = "LIST";
    static final String KEY = "KEY";
    static final String STACK = "STACK";
    static final String TABLE = "TABLE";
    static final String TABLE_ROW = "TABLE_ROW";
    static final String TABLE_ENTRY = "TABLE_ENTRY";
    static final String STRING = "STRING";
    static final String BYTE_FIELD = "BYTE_FIELD";
    static final String NUMBER = "NUMBER";
    static final String MATCH = "MATCH";
    static final String COMPLEX = "COMPLEX";
    static final String DTC = "DTC";
    static final String LOGICAL_LINK = "LOGICAL_LINK";
    static final String SHARED_DATA = "SHARED_DATA";
    static final String SERVICE = "SERVICE";
    static final String RESERVED = "RESERVED";
    static String NOT_INHERITED = "NOT_INHERITED";

    static String mapPhysicalType(final PHYSICALTYPE type) {
        if (isNull(type) || isNull(type.getBASEDATATYPE())) {
            return Types.UNKNOWN;
        }

        if (PHYSICALDATATYPE.A_UNICODE_2_STRING.equals(type.getBASEDATATYPE())) {
            return Types.STRING;
        }

        if (PHYSICALDATATYPE.A_BYTEFIELD.equals(type.getBASEDATATYPE())) {
            return Types.BYTE_FIELD;
        }

        return Types.NUMBER;

    }

    static boolean isIntegerType(final DIAGCODEDTYPE type) {
        return DATATYPE.A_INT_32.equals(type.getBASEDATATYPE()) ||
                DATATYPE.A_UINT_32.equals(type.getBASEDATATYPE());
    }

    static String mapCodedType(final DIAGCODEDTYPE type) {
        if (type == null) {
            return UNDEFINED;
        }

        if (type.getBASEDATATYPE() == DATATYPE.A_ASCIISTRING
                || type.getBASEDATATYPE() == DATATYPE.A_UNICODE_2_STRING
                || type.getBASEDATATYPE() == DATATYPE.A_UTF_8_STRING) {
            return STRING;
        }

        if (type.getBASEDATATYPE() == DATATYPE.A_BYTEFIELD) {
            return BYTE_FIELD;
        }

        return NUMBER;
    }

    public static String getDocumentType(final IndexableDocument document) {
        if (document instanceof ECUVARIANT) {
            return "ecu_variant";
        }
        if (document instanceof ECUSHAREDDATA) {
            return "shared_data";
        }
        if (document instanceof BASEVARIANT) {
            return "base_variant";
        }
        if (document instanceof FUNCTIONALGROUP) {
            return "functional_group";
        }
        if (document instanceof PROTOCOL) {
            return "protocol";
        }

        return document.getDocType().toLowerCase();
    }

}
