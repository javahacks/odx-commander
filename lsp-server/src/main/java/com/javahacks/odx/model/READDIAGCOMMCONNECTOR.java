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
 * &lt;p&gt;Java class for READ-DIAG-COMM-CONNECTOR complex type.
 * <p>
 * &lt;p&gt;The following schema fragment specifies the expected content contained within this class.
 * <p>
 * &lt;pre&gt;
 * &amp;lt;complexType name="READ-DIAG-COMM-CONNECTOR"&amp;gt;
 * &amp;lt;complexContent&amp;gt;
 * &amp;lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&amp;gt;
 * &amp;lt;sequence&amp;gt;
 * &amp;lt;element name="READ-PARAM-VALUES" type="{}READ-PARAM-VALUES" minOccurs="0"/&amp;gt;
 * &amp;lt;choice&amp;gt;
 * &amp;lt;element name="READ-DIAG-COMM-REF" type="{}ODXLINK"/&amp;gt;
 * &amp;lt;element name="READ-DIAG-COMM-SNREF" type="{}SNREF"/&amp;gt;
 * &amp;lt;/choice&amp;gt;
 * &amp;lt;choice&amp;gt;
 * &amp;lt;element name="READ-DATA-SNREF" type="{}SNREF"/&amp;gt;
 * &amp;lt;element name="READ-DATA-SNPATHREF" type="{}SNPATHREF"/&amp;gt;
 * &amp;lt;/choice&amp;gt;
 * &amp;lt;/sequence&amp;gt;
 * &amp;lt;/restriction&amp;gt;
 * &amp;lt;/complexContent&amp;gt;
 * &amp;lt;/complexType&amp;gt;
 * &lt;/pre&gt;
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "READ-DIAG-COMM-CONNECTOR")
public class READDIAGCOMMCONNECTOR {

    @XmlElement(name = "READ-PARAM-VALUES")
    protected READPARAMVALUES readparamvalues;
    @XmlElement(name = "READ-DIAG-COMM-REF")
    protected ODXLINK readdiagcommref;
    @XmlElement(name = "READ-DIAG-COMM-SNREF")
    protected SNREF readdiagcommsnref;
    @XmlElement(name = "READ-DATA-SNREF")
    protected SNREF readdatasnref;
    @XmlElement(name = "READ-DATA-SNPATHREF")
    protected SNPATHREF readdatasnpathref;

    /**
     * Gets the value of the readparamvalues property.
     *
     * @return possible object is
     * {@link READPARAMVALUES }
     */
    public READPARAMVALUES getREADPARAMVALUES() {
        return readparamvalues;
    }

    /**
     * Sets the value of the readparamvalues property.
     *
     * @param value allowed object is
     *              {@link READPARAMVALUES }
     */
    public void setREADPARAMVALUES(READPARAMVALUES value) {
        this.readparamvalues = value;
    }

    /**
     * Gets the value of the readdiagcommref property.
     *
     * @return possible object is
     * {@link ODXLINK }
     */
    public ODXLINK getREADDIAGCOMMREF() {
        return readdiagcommref;
    }

    /**
     * Sets the value of the readdiagcommref property.
     *
     * @param value allowed object is
     *              {@link ODXLINK }
     */
    public void setREADDIAGCOMMREF(ODXLINK value) {
        this.readdiagcommref = value;
    }

    /**
     * Gets the value of the readdiagcommsnref property.
     *
     * @return possible object is
     * {@link SNREF }
     */
    public SNREF getREADDIAGCOMMSNREF() {
        return readdiagcommsnref;
    }

    /**
     * Sets the value of the readdiagcommsnref property.
     *
     * @param value allowed object is
     *              {@link SNREF }
     */
    public void setREADDIAGCOMMSNREF(SNREF value) {
        this.readdiagcommsnref = value;
    }

    /**
     * Gets the value of the readdatasnref property.
     *
     * @return possible object is
     * {@link SNREF }
     */
    public SNREF getREADDATASNREF() {
        return readdatasnref;
    }

    /**
     * Sets the value of the readdatasnref property.
     *
     * @param value allowed object is
     *              {@link SNREF }
     */
    public void setREADDATASNREF(SNREF value) {
        this.readdatasnref = value;
    }

    /**
     * Gets the value of the readdatasnpathref property.
     *
     * @return possible object is
     * {@link SNPATHREF }
     */
    public SNPATHREF getREADDATASNPATHREF() {
        return readdatasnpathref;
    }

    /**
     * Sets the value of the readdatasnpathref property.
     *
     * @param value allowed object is
     *              {@link SNPATHREF }
     */
    public void setREADDATASNPATHREF(SNPATHREF value) {
        this.readdatasnpathref = value;
    }

}
