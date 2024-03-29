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
 * &lt;p&gt;Java class for DATA-OBJECT-PROP complex type.
 * <p>
 * &lt;p&gt;The following schema fragment specifies the expected content contained within this class.
 * <p>
 * &lt;pre&gt;
 * &amp;lt;complexType name="DATA-OBJECT-PROP"&amp;gt;
 * &amp;lt;complexContent&amp;gt;
 * &amp;lt;extension base="{}DOP-BASE"&amp;gt;
 * &amp;lt;sequence&amp;gt;
 * &amp;lt;element name="COMPU-METHOD" type="{}COMPU-METHOD"/&amp;gt;
 * &amp;lt;element name="DIAG-CODED-TYPE" type="{}DIAG-CODED-TYPE"/&amp;gt;
 * &amp;lt;element name="PHYSICAL-TYPE" type="{}PHYSICAL-TYPE"/&amp;gt;
 * &amp;lt;element name="INTERNAL-CONSTR" type="{}INTERNAL-CONSTR" minOccurs="0"/&amp;gt;
 * &amp;lt;element name="UNIT-REF" type="{}ODXLINK" minOccurs="0"/&amp;gt;
 * &amp;lt;element name="PHYS-CONSTR" type="{}INTERNAL-CONSTR" minOccurs="0"/&amp;gt;
 * &amp;lt;/sequence&amp;gt;
 * &amp;lt;/extension&amp;gt;
 * &amp;lt;/complexContent&amp;gt;
 * &amp;lt;/complexType&amp;gt;
 * &lt;/pre&gt;
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "DATA-OBJECT-PROP")
public class DATAOBJECTPROP
        extends DOPBASE {

    @XmlElement(name = "COMPU-METHOD", required = true)
    protected COMPUMETHOD compumethod;
    @XmlElement(name = "DIAG-CODED-TYPE", required = true)
    protected DIAGCODEDTYPE diagcodedtype;
    @XmlElement(name = "PHYSICAL-TYPE", required = true)
    protected PHYSICALTYPE physicaltype;
    @XmlElement(name = "INTERNAL-CONSTR")
    protected INTERNALCONSTR internalconstr;
    @XmlElement(name = "UNIT-REF")
    protected ODXLINK unitref;
    @XmlElement(name = "PHYS-CONSTR")
    protected INTERNALCONSTR physconstr;

    /**
     * Gets the value of the compumethod property.
     *
     * @return possible object is
     * {@link COMPUMETHOD }
     */
    public COMPUMETHOD getCOMPUMETHOD() {
        return compumethod;
    }

    /**
     * Sets the value of the compumethod property.
     *
     * @param value allowed object is
     *              {@link COMPUMETHOD }
     */
    public void setCOMPUMETHOD(COMPUMETHOD value) {
        this.compumethod = value;
    }

    /**
     * Gets the value of the diagcodedtype property.
     *
     * @return possible object is
     * {@link DIAGCODEDTYPE }
     */
    public DIAGCODEDTYPE getDIAGCODEDTYPE() {
        return diagcodedtype;
    }

    /**
     * Sets the value of the diagcodedtype property.
     *
     * @param value allowed object is
     *              {@link DIAGCODEDTYPE }
     */
    public void setDIAGCODEDTYPE(DIAGCODEDTYPE value) {
        this.diagcodedtype = value;
    }

    /**
     * Gets the value of the physicaltype property.
     *
     * @return possible object is
     * {@link PHYSICALTYPE }
     */
    public PHYSICALTYPE getPHYSICALTYPE() {
        return physicaltype;
    }

    /**
     * Sets the value of the physicaltype property.
     *
     * @param value allowed object is
     *              {@link PHYSICALTYPE }
     */
    public void setPHYSICALTYPE(PHYSICALTYPE value) {
        this.physicaltype = value;
    }

    /**
     * Gets the value of the internalconstr property.
     *
     * @return possible object is
     * {@link INTERNALCONSTR }
     */
    public INTERNALCONSTR getINTERNALCONSTR() {
        return internalconstr;
    }

    /**
     * Sets the value of the internalconstr property.
     *
     * @param value allowed object is
     *              {@link INTERNALCONSTR }
     */
    public void setINTERNALCONSTR(INTERNALCONSTR value) {
        this.internalconstr = value;
    }

    /**
     * Gets the value of the unitref property.
     *
     * @return possible object is
     * {@link ODXLINK }
     */
    public ODXLINK getUNITREF() {
        return unitref;
    }

    /**
     * Sets the value of the unitref property.
     *
     * @param value allowed object is
     *              {@link ODXLINK }
     */
    public void setUNITREF(ODXLINK value) {
        this.unitref = value;
    }

    /**
     * Gets the value of the physconstr property.
     *
     * @return possible object is
     * {@link INTERNALCONSTR }
     */
    public INTERNALCONSTR getPHYSCONSTR() {
        return physconstr;
    }

    /**
     * Sets the value of the physconstr property.
     *
     * @param value allowed object is
     *              {@link INTERNALCONSTR }
     */
    public void setPHYSCONSTR(INTERNALCONSTR value) {
        this.physconstr = value;
    }

}
