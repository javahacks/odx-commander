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
 * &lt;p&gt;Java class for SNREF-TO-TABLEROW complex type.
 * <p>
 * &lt;p&gt;The following schema fragment specifies the expected content contained within this class.
 * <p>
 * &lt;pre&gt;
 * &amp;lt;complexType name="SNREF-TO-TABLEROW"&amp;gt;
 * &amp;lt;complexContent&amp;gt;
 * &amp;lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&amp;gt;
 * &amp;lt;sequence&amp;gt;
 * &amp;lt;element name="TABLE-SNREF" type="{}SNREF"/&amp;gt;
 * &amp;lt;element name="TABLE-ROW-SNREF" type="{}SNREF"/&amp;gt;
 * &amp;lt;/sequence&amp;gt;
 * &amp;lt;/restriction&amp;gt;
 * &amp;lt;/complexContent&amp;gt;
 * &amp;lt;/complexType&amp;gt;
 * &lt;/pre&gt;
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "SNREF-TO-TABLEROW")
public class SNREFTOTABLEROW {

    @XmlElement(name = "TABLE-SNREF", required = true)
    protected SNREF tablesnref;
    @XmlElement(name = "TABLE-ROW-SNREF", required = true)
    protected SNREF tablerowsnref;

    /**
     * Gets the value of the tablesnref property.
     *
     * @return possible object is
     * {@link SNREF }
     */
    public SNREF getTABLESNREF() {
        return tablesnref;
    }

    /**
     * Sets the value of the tablesnref property.
     *
     * @param value allowed object is
     *              {@link SNREF }
     */
    public void setTABLESNREF(SNREF value) {
        this.tablesnref = value;
    }

    /**
     * Gets the value of the tablerowsnref property.
     *
     * @return possible object is
     * {@link SNREF }
     */
    public SNREF getTABLEROWSNREF() {
        return tablerowsnref;
    }

    /**
     * Sets the value of the tablerowsnref property.
     *
     * @param value allowed object is
     *              {@link SNREF }
     */
    public void setTABLEROWSNREF(SNREF value) {
        this.tablerowsnref = value;
    }

}
