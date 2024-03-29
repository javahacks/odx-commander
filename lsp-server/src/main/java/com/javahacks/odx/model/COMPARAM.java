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
 * &lt;p&gt;Java class for COMPARAM complex type.
 * <p>
 * &lt;p&gt;The following schema fragment specifies the expected content contained within this class.
 * <p>
 * &lt;pre&gt;
 * &amp;lt;complexType name="COMPARAM"&amp;gt;
 * &amp;lt;complexContent&amp;gt;
 * &amp;lt;extension base="{}BASE-COMPARAM"&amp;gt;
 * &amp;lt;sequence&amp;gt;
 * &amp;lt;element name="PHYSICAL-DEFAULT-VALUE" type="{http://www.w3.org/2001/XMLSchema}string"/&amp;gt;
 * &amp;lt;element name="DATA-OBJECT-PROP-REF" type="{}ODXLINK"/&amp;gt;
 * &amp;lt;/sequence&amp;gt;
 * &amp;lt;/extension&amp;gt;
 * &amp;lt;/complexContent&amp;gt;
 * &amp;lt;/complexType&amp;gt;
 * &lt;/pre&gt;
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "COMPARAM")
public class COMPARAM
        extends BASECOMPARAM {

    @XmlElement(name = "PHYSICAL-DEFAULT-VALUE", required = true)
    protected String physicaldefaultvalue;
    @XmlElement(name = "DATA-OBJECT-PROP-REF", required = true)
    protected ODXLINK dataobjectpropref;

    /**
     * Gets the value of the physicaldefaultvalue property.
     *
     * @return possible object is
     * {@link String }
     */
    public String getPHYSICALDEFAULTVALUE() {
        return physicaldefaultvalue;
    }

    /**
     * Sets the value of the physicaldefaultvalue property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setPHYSICALDEFAULTVALUE(final String value) {
        this.physicaldefaultvalue = value;
    }

    /**
     * Gets the value of the dataobjectpropref property.
     *
     * @return possible object is
     * {@link ODXLINK }
     */
    public ODXLINK getDATAOBJECTPROPREF() {
        return dataobjectpropref;
    }

    /**
     * Sets the value of the dataobjectpropref property.
     *
     * @param value allowed object is
     *              {@link ODXLINK }
     */
    public void setDATAOBJECTPROPREF(final ODXLINK value) {
        this.dataobjectpropref = value;
    }

}
