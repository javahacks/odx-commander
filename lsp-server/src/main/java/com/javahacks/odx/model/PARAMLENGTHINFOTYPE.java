//
// This file was generated by the Eclipse Implementation of JAXB, v2.3.3 
// See https://eclipse-ee4j.github.io/jaxb-ri 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2020.11.28 at 09:58:19 AM CET 
//


package com.javahacks.odx.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * &lt;p&gt;Java class for PARAM-LENGTH-INFO-TYPE complex type.
 * <p>
 * &lt;p&gt;The following schema fragment specifies the expected content contained within this class.
 * <p>
 * &lt;pre&gt;
 * &amp;lt;complexType name="PARAM-LENGTH-INFO-TYPE"&amp;gt;
 * &amp;lt;complexContent&amp;gt;
 * &amp;lt;extension base="{}DIAG-CODED-TYPE"&amp;gt;
 * &amp;lt;sequence&amp;gt;
 * &amp;lt;element name="LENGTH-KEY-REF" type="{}ODXLINK"/&amp;gt;
 * &amp;lt;/sequence&amp;gt;
 * &amp;lt;/extension&amp;gt;
 * &amp;lt;/complexContent&amp;gt;
 * &amp;lt;/complexType&amp;gt;
 * &lt;/pre&gt;
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "PARAM-LENGTH-INFO-TYPE")
public class PARAMLENGTHINFOTYPE
        extends DIAGCODEDTYPE {

    @XmlElement(name = "LENGTH-KEY-REF", required = true)
    protected ODXLINK lengthkeyref;

    /**
     * Gets the value of the lengthkeyref property.
     *
     * @return possible object is
     * {@link ODXLINK }
     */
    public ODXLINK getLENGTHKEYREF() {
        return lengthkeyref;
    }

    /**
     * Sets the value of the lengthkeyref property.
     *
     * @param value allowed object is
     *              {@link ODXLINK }
     */
    public void setLENGTHKEYREF(ODXLINK value) {
        this.lengthkeyref = value;
    }

}
