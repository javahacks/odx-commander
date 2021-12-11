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
 * &lt;p&gt;Java class for INTERNAL-CONSTR complex type.
 * <p>
 * &lt;p&gt;The following schema fragment specifies the expected content contained within this class.
 * <p>
 * &lt;pre&gt;
 * &amp;lt;complexType name="INTERNAL-CONSTR"&amp;gt;
 * &amp;lt;complexContent&amp;gt;
 * &amp;lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&amp;gt;
 * &amp;lt;sequence&amp;gt;
 * &amp;lt;element name="LOWER-LIMIT" type="{}LIMIT" minOccurs="0"/&amp;gt;
 * &amp;lt;element name="UPPER-LIMIT" type="{}LIMIT" minOccurs="0"/&amp;gt;
 * &amp;lt;element name="SCALE-CONSTRS" type="{}SCALE-CONSTRS" minOccurs="0"/&amp;gt;
 * &amp;lt;/sequence&amp;gt;
 * &amp;lt;/restriction&amp;gt;
 * &amp;lt;/complexContent&amp;gt;
 * &amp;lt;/complexType&amp;gt;
 * &lt;/pre&gt;
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "INTERNAL-CONSTR")
public class INTERNALCONSTR {

    @XmlElement(name = "LOWER-LIMIT")
    protected LIMIT lowerlimit;
    @XmlElement(name = "UPPER-LIMIT")
    protected LIMIT upperlimit;
    @XmlElement(name = "SCALE-CONSTRS")
    protected SCALECONSTRS scaleconstrs;

    /**
     * Gets the value of the lowerlimit property.
     *
     * @return possible object is
     * {@link LIMIT }
     */
    public LIMIT getLOWERLIMIT() {
        return lowerlimit;
    }

    /**
     * Sets the value of the lowerlimit property.
     *
     * @param value allowed object is
     *              {@link LIMIT }
     */
    public void setLOWERLIMIT(LIMIT value) {
        this.lowerlimit = value;
    }

    /**
     * Gets the value of the upperlimit property.
     *
     * @return possible object is
     * {@link LIMIT }
     */
    public LIMIT getUPPERLIMIT() {
        return upperlimit;
    }

    /**
     * Sets the value of the upperlimit property.
     *
     * @param value allowed object is
     *              {@link LIMIT }
     */
    public void setUPPERLIMIT(LIMIT value) {
        this.upperlimit = value;
    }

    /**
     * Gets the value of the scaleconstrs property.
     *
     * @return possible object is
     * {@link SCALECONSTRS }
     */
    public SCALECONSTRS getSCALECONSTRS() {
        return scaleconstrs;
    }

    /**
     * Sets the value of the scaleconstrs property.
     *
     * @param value allowed object is
     *              {@link SCALECONSTRS }
     */
    public void setSCALECONSTRS(SCALECONSTRS value) {
        this.scaleconstrs = value;
    }

}