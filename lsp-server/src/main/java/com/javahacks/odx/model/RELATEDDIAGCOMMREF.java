//
// This file was generated by the Eclipse Implementation of JAXB, v2.3.3 
// See https://eclipse-ee4j.github.io/jaxb-ri 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2020.11.28 at 09:58:19 AM CET 
//


package com.javahacks.odx.model;

import javax.xml.bind.annotation.*;


/**
 * &lt;p&gt;Java class for RELATED-DIAG-COMM-REF complex type.
 * <p>
 * &lt;p&gt;The following schema fragment specifies the expected content contained within this class.
 * <p>
 * &lt;pre&gt;
 * &amp;lt;complexType name="RELATED-DIAG-COMM-REF"&amp;gt;
 * &amp;lt;complexContent&amp;gt;
 * &amp;lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&amp;gt;
 * &amp;lt;sequence&amp;gt;
 * &amp;lt;element name="RELATION-TYPE" type="{http://www.w3.org/2001/XMLSchema}string"/&amp;gt;
 * &amp;lt;/sequence&amp;gt;
 * &amp;lt;attGroup ref="{}ODXLINK-ATTR"/&amp;gt;
 * &amp;lt;/restriction&amp;gt;
 * &amp;lt;/complexContent&amp;gt;
 * &amp;lt;/complexType&amp;gt;
 * &lt;/pre&gt;
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "RELATED-DIAG-COMM-REF")
public class RELATEDDIAGCOMMREF extends ODXLINK {

    @XmlElement(name = "RELATION-TYPE", required = true)
    protected String relationtype;

    @XmlAttribute(name = "REVISION")
    protected String revision;

    /**
     * Gets the value of the relationtype property.
     *
     * @return possible object is
     * {@link String }
     */
    public String getRELATIONTYPE() {
        return relationtype;
    }

    /**
     * Sets the value of the relationtype property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setRELATIONTYPE(final String value) {
        this.relationtype = value;
    }


    /**
     * Gets the value of the revision property.
     *
     * @return possible object is
     * {@link String }
     */
    public String getREVISION() {
        return revision;
    }

    /**
     * Sets the value of the revision property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setREVISION(final String value) {
        this.revision = value;
    }

}
