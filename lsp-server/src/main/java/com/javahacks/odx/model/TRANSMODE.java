//
// This file was generated by the Eclipse Implementation of JAXB, v2.3.3 
// See https://eclipse-ee4j.github.io/jaxb-ri 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2020.11.28 at 09:58:19 AM CET 
//


package com.javahacks.odx.model;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;


/**
 * &lt;p&gt;Java class for TRANS-MODE.
 * <p>
 * &lt;p&gt;The following schema fragment specifies the expected content contained within this class.
 * &lt;pre&gt;
 * &amp;lt;simpleType name="TRANS-MODE"&amp;gt;
 * &amp;lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&amp;gt;
 * &amp;lt;enumeration value="SEND-ONLY"/&amp;gt;
 * &amp;lt;enumeration value="RECEIVE-ONLY"/&amp;gt;
 * &amp;lt;enumeration value="SEND-AND-RECEIVE"/&amp;gt;
 * &amp;lt;enumeration value="SEND-OR-RECEIVE"/&amp;gt;
 * &amp;lt;/restriction&amp;gt;
 * &amp;lt;/simpleType&amp;gt;
 * &lt;/pre&gt;
 */
@XmlType(name = "TRANS-MODE")
@XmlEnum
public enum TRANSMODE {

    @XmlEnumValue("SEND-ONLY")
    SEND_ONLY("SEND-ONLY"),
    @XmlEnumValue("RECEIVE-ONLY")
    RECEIVE_ONLY("RECEIVE-ONLY"),
    @XmlEnumValue("SEND-AND-RECEIVE")
    SEND_AND_RECEIVE("SEND-AND-RECEIVE"),
    @XmlEnumValue("SEND-OR-RECEIVE")
    SEND_OR_RECEIVE("SEND-OR-RECEIVE");
    private final String value;

    TRANSMODE(String v) {
        value = v;
    }

    public static TRANSMODE fromValue(String v) {
        for (TRANSMODE c : TRANSMODE.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

    public String value() {
        return value;
    }

}
