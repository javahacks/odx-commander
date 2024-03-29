//
// This file was generated by the Eclipse Implementation of JAXB, v2.3.3 
// See https://eclipse-ee4j.github.io/jaxb-ri 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2020.11.28 at 09:58:19 AM CET 
//


package com.javahacks.odx.model;

import javax.xml.bind.annotation.*;


/**
 * &lt;p&gt;Java class for COMM-RELATION complex type.
 * <p>
 * &lt;p&gt;The following schema fragment specifies the expected content contained within this class.
 * <p>
 * &lt;pre&gt;
 * &amp;lt;complexType name="COMM-RELATION"&amp;gt;
 * &amp;lt;complexContent&amp;gt;
 * &amp;lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&amp;gt;
 * &amp;lt;sequence&amp;gt;
 * &amp;lt;element name="DESC" type="{}DESCRIPTION" minOccurs="0"/&amp;gt;
 * &amp;lt;element name="RELATION-TYPE" type="{http://www.w3.org/2001/XMLSchema}string"/&amp;gt;
 * &amp;lt;choice&amp;gt;
 * &amp;lt;element name="DIAG-COMM-REF" type="{}ODXLINK"/&amp;gt;
 * &amp;lt;element name="DIAG-COMM-SNREF" type="{}SNREF"/&amp;gt;
 * &amp;lt;/choice&amp;gt;
 * &amp;lt;choice&amp;gt;
 * &amp;lt;choice minOccurs="0"&amp;gt;
 * &amp;lt;element name="IN-PARAM-IF-SNREF" type="{}SNREF"/&amp;gt;
 * &amp;lt;element name="IN-PARAM-IF-SNPATHREF" type="{}SNPATHREF"/&amp;gt;
 * &amp;lt;/choice&amp;gt;
 * &amp;lt;choice minOccurs="0"&amp;gt;
 * &amp;lt;element name="OUT-PARAM-IF-SNREF" type="{}SNREF"/&amp;gt;
 * &amp;lt;element name="OUT-PARAM-IF-SNPATHREF" type="{}SNPATHREF"/&amp;gt;
 * &amp;lt;/choice&amp;gt;
 * &amp;lt;/choice&amp;gt;
 * &amp;lt;/sequence&amp;gt;
 * &amp;lt;attribute name="VALUE-TYPE" type="{}COMM-RELATION-VALUE-TYPE" /&amp;gt;
 * &amp;lt;/restriction&amp;gt;
 * &amp;lt;/complexContent&amp;gt;
 * &amp;lt;/complexType&amp;gt;
 * &lt;/pre&gt;
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "COMM-RELATION")
public class COMMRELATION {

    @XmlElement(name = "DESC")
    protected DESCRIPTION desc;
    @XmlElement(name = "RELATION-TYPE", required = true)
    protected String relationtype;
    @XmlElement(name = "DIAG-COMM-REF")
    protected ODXLINK diagcommref;
    @XmlElement(name = "DIAG-COMM-SNREF")
    protected SNREF diagcommsnref;
    @XmlElement(name = "IN-PARAM-IF-SNREF")
    protected SNREF inparamifsnref;
    @XmlElement(name = "IN-PARAM-IF-SNPATHREF")
    protected SNPATHREF inparamifsnpathref;
    @XmlElement(name = "OUT-PARAM-IF-SNREF")
    protected SNREF outparamifsnref;
    @XmlElement(name = "OUT-PARAM-IF-SNPATHREF")
    protected SNPATHREF outparamifsnpathref;
    @XmlAttribute(name = "VALUE-TYPE")
    protected COMMRELATIONVALUETYPE valuetype;

    /**
     * Gets the value of the desc property.
     *
     * @return possible object is
     * {@link DESCRIPTION }
     */
    public DESCRIPTION getDESC() {
        return desc;
    }

    /**
     * Sets the value of the desc property.
     *
     * @param value allowed object is
     *              {@link DESCRIPTION }
     */
    public void setDESC(DESCRIPTION value) {
        this.desc = value;
    }

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
    public void setRELATIONTYPE(String value) {
        this.relationtype = value;
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
    public void setDIAGCOMMREF(ODXLINK value) {
        this.diagcommref = value;
    }

    /**
     * Gets the value of the diagcommsnref property.
     *
     * @return possible object is
     * {@link SNREF }
     */
    public SNREF getDIAGCOMMSNREF() {
        return diagcommsnref;
    }

    /**
     * Sets the value of the diagcommsnref property.
     *
     * @param value allowed object is
     *              {@link SNREF }
     */
    public void setDIAGCOMMSNREF(SNREF value) {
        this.diagcommsnref = value;
    }

    /**
     * Gets the value of the inparamifsnref property.
     *
     * @return possible object is
     * {@link SNREF }
     */
    public SNREF getINPARAMIFSNREF() {
        return inparamifsnref;
    }

    /**
     * Sets the value of the inparamifsnref property.
     *
     * @param value allowed object is
     *              {@link SNREF }
     */
    public void setINPARAMIFSNREF(SNREF value) {
        this.inparamifsnref = value;
    }

    /**
     * Gets the value of the inparamifsnpathref property.
     *
     * @return possible object is
     * {@link SNPATHREF }
     */
    public SNPATHREF getINPARAMIFSNPATHREF() {
        return inparamifsnpathref;
    }

    /**
     * Sets the value of the inparamifsnpathref property.
     *
     * @param value allowed object is
     *              {@link SNPATHREF }
     */
    public void setINPARAMIFSNPATHREF(SNPATHREF value) {
        this.inparamifsnpathref = value;
    }

    /**
     * Gets the value of the outparamifsnref property.
     *
     * @return possible object is
     * {@link SNREF }
     */
    public SNREF getOUTPARAMIFSNREF() {
        return outparamifsnref;
    }

    /**
     * Sets the value of the outparamifsnref property.
     *
     * @param value allowed object is
     *              {@link SNREF }
     */
    public void setOUTPARAMIFSNREF(SNREF value) {
        this.outparamifsnref = value;
    }

    /**
     * Gets the value of the outparamifsnpathref property.
     *
     * @return possible object is
     * {@link SNPATHREF }
     */
    public SNPATHREF getOUTPARAMIFSNPATHREF() {
        return outparamifsnpathref;
    }

    /**
     * Sets the value of the outparamifsnpathref property.
     *
     * @param value allowed object is
     *              {@link SNPATHREF }
     */
    public void setOUTPARAMIFSNPATHREF(SNPATHREF value) {
        this.outparamifsnpathref = value;
    }

    /**
     * Gets the value of the valuetype property.
     *
     * @return possible object is
     * {@link COMMRELATIONVALUETYPE }
     */
    public COMMRELATIONVALUETYPE getVALUETYPE() {
        return valuetype;
    }

    /**
     * Sets the value of the valuetype property.
     *
     * @param value allowed object is
     *              {@link COMMRELATIONVALUETYPE }
     */
    public void setVALUETYPE(COMMRELATIONVALUETYPE value) {
        this.valuetype = value;
    }

}
