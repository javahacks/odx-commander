//
// This file was generated by the Eclipse Implementation of JAXB, v2.3.3 
// See https://eclipse-ee4j.github.io/jaxb-ri 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2020.11.28 at 09:58:19 AM CET 
//


package com.javahacks.odx.model;

import javax.xml.bind.annotation.*;


/**
 * &lt;p&gt;Java class for PHYSICAL-TYPE complex type.
 * <p>
 * &lt;p&gt;The following schema fragment specifies the expected content contained within this class.
 * <p>
 * &lt;pre&gt;
 * &amp;lt;complexType name="PHYSICAL-TYPE"&amp;gt;
 * &amp;lt;complexContent&amp;gt;
 * &amp;lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&amp;gt;
 * &amp;lt;sequence&amp;gt;
 * &amp;lt;element name="PRECISION" type="{http://www.w3.org/2001/XMLSchema}unsignedInt" minOccurs="0"/&amp;gt;
 * &amp;lt;/sequence&amp;gt;
 * &amp;lt;attribute name="BASE-DATA-TYPE" use="required" type="{}PHYSICAL-DATA-TYPE" /&amp;gt;
 * &amp;lt;attribute name="DISPLAY-RADIX" type="{}RADIX" /&amp;gt;
 * &amp;lt;/restriction&amp;gt;
 * &amp;lt;/complexContent&amp;gt;
 * &amp;lt;/complexType&amp;gt;
 * &lt;/pre&gt;
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "PHYSICAL-TYPE")
public class PHYSICALTYPE {

    @XmlElement(name = "PRECISION")
    @XmlSchemaType(name = "unsignedInt")
    protected Long precision;
    @XmlAttribute(name = "BASE-DATA-TYPE", required = true)
    protected PHYSICALDATATYPE basedatatype;
    @XmlAttribute(name = "DISPLAY-RADIX")
    protected RADIX displayradix;

    /**
     * Gets the value of the precision property.
     *
     * @return possible object is
     * {@link Long }
     */
    public Long getPRECISION() {
        return precision;
    }

    /**
     * Sets the value of the precision property.
     *
     * @param value allowed object is
     *              {@link Long }
     */
    public void setPRECISION(Long value) {
        this.precision = value;
    }

    /**
     * Gets the value of the basedatatype property.
     *
     * @return possible object is
     * {@link PHYSICALDATATYPE }
     */
    public PHYSICALDATATYPE getBASEDATATYPE() {
        return basedatatype;
    }

    /**
     * Sets the value of the basedatatype property.
     *
     * @param value allowed object is
     *              {@link PHYSICALDATATYPE }
     */
    public void setBASEDATATYPE(PHYSICALDATATYPE value) {
        this.basedatatype = value;
    }

    /**
     * Gets the value of the displayradix property.
     *
     * @return possible object is
     * {@link RADIX }
     */
    public RADIX getDISPLAYRADIX() {
        return displayradix;
    }

    /**
     * Sets the value of the displayradix property.
     *
     * @param value allowed object is
     *              {@link RADIX }
     */
    public void setDISPLAYRADIX(RADIX value) {
        this.displayradix = value;
    }

}
