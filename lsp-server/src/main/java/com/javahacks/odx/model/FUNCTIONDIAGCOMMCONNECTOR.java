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
 * &lt;p&gt;Java class for FUNCTION-DIAG-COMM-CONNECTOR complex type.
 * <p>
 * &lt;p&gt;The following schema fragment specifies the expected content contained within this class.
 * <p>
 * &lt;pre&gt;
 * &amp;lt;complexType name="FUNCTION-DIAG-COMM-CONNECTOR"&amp;gt;
 * &amp;lt;complexContent&amp;gt;
 * &amp;lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&amp;gt;
 * &amp;lt;sequence&amp;gt;
 * &amp;lt;element name="LOGICAL-LINK-REF" type="{}ODXLINK" minOccurs="0"/&amp;gt;
 * &amp;lt;element name="DIAG-COMM-REF" type="{}ODXLINK"/&amp;gt;
 * &amp;lt;/sequence&amp;gt;
 * &amp;lt;/restriction&amp;gt;
 * &amp;lt;/complexContent&amp;gt;
 * &amp;lt;/complexType&amp;gt;
 * &lt;/pre&gt;
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "FUNCTION-DIAG-COMM-CONNECTOR")
public class FUNCTIONDIAGCOMMCONNECTOR {

    @XmlElement(name = "LOGICAL-LINK-REF")
    protected ODXLINK logicallinkref;
    @XmlElement(name = "DIAG-COMM-REF", required = true)
    protected ODXLINK diagcommref;

    /**
     * Gets the value of the logicallinkref property.
     *
     * @return possible object is
     * {@link ODXLINK }
     */
    public ODXLINK getLOGICALLINKREF() {
        return logicallinkref;
    }

    /**
     * Sets the value of the logicallinkref property.
     *
     * @param value allowed object is
     *              {@link ODXLINK }
     */
    public void setLOGICALLINKREF(final ODXLINK value) {
        this.logicallinkref = value;
    }

    /**
     * Gets the value of the diagcommref property.
     *
     * @return possible object is
     * {@link ODXLINK }
     */
    public ODXLINK getDIAGCOMMREF() {
        return diagcommref;
    }

    /**
     * Sets the value of the diagcommref property.
     *
     * @param value allowed object is
     *              {@link ODXLINK }
     */
    public void setDIAGCOMMREF(final ODXLINK value) {
        this.diagcommref = value;
    }

}
