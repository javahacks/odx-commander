//
// This file was generated by the Eclipse Implementation of JAXB, v2.3.3 
// See https://eclipse-ee4j.github.io/jaxb-ri 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2020.11.28 at 09:58:19 AM CET 
//


package com.javahacks.odx.model;

import javax.xml.bind.annotation.*;
import javax.xml.bind.annotation.adapters.HexBinaryAdapter;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;


/**
 * &lt;p&gt;Java class for DIAG-COMM-DATA-CONNECTOR complex type.
 * <p>
 * &lt;p&gt;The following schema fragment specifies the expected content contained within this class.
 * <p>
 * &lt;pre&gt;
 * &amp;lt;complexType name="DIAG-COMM-DATA-CONNECTOR"&amp;gt;
 * &amp;lt;complexContent&amp;gt;
 * &amp;lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&amp;gt;
 * &amp;lt;sequence&amp;gt;
 * &amp;lt;element name="UNCOMPRESSED-SIZE" type="{http://www.w3.org/2001/XMLSchema}unsignedInt"/&amp;gt;
 * &amp;lt;element name="SOURCE-START-ADDRESS" type="{http://www.w3.org/2001/XMLSchema}hexBinary"/&amp;gt;
 * &amp;lt;element name="READ-DIAG-COMM-CONNECTOR" type="{}READ-DIAG-COMM-CONNECTOR" minOccurs="0"/&amp;gt;
 * &amp;lt;element name="WRITE-DIAG-COMM-CONNECTOR" type="{}WRITE-DIAG-COMM-CONNECTOR" minOccurs="0"/&amp;gt;
 * &amp;lt;/sequence&amp;gt;
 * &amp;lt;/restriction&amp;gt;
 * &amp;lt;/complexContent&amp;gt;
 * &amp;lt;/complexType&amp;gt;
 * &lt;/pre&gt;
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "DIAG-COMM-DATA-CONNECTOR")
public class DIAGCOMMDATACONNECTOR {

    @XmlElement(name = "UNCOMPRESSED-SIZE")
    @XmlSchemaType(name = "unsignedInt")
    protected long uncompressedsize;
    @XmlElement(name = "SOURCE-START-ADDRESS", required = true, type = String.class)
    @XmlJavaTypeAdapter(HexBinaryAdapter.class)
    @XmlSchemaType(name = "hexBinary")
    protected byte[] sourcestartaddress;
    @XmlElement(name = "READ-DIAG-COMM-CONNECTOR")
    protected READDIAGCOMMCONNECTOR readdiagcommconnector;
    @XmlElement(name = "WRITE-DIAG-COMM-CONNECTOR")
    protected WRITEDIAGCOMMCONNECTOR writediagcommconnector;

    /**
     * Gets the value of the uncompressedsize property.
     */
    public long getUNCOMPRESSEDSIZE() {
        return uncompressedsize;
    }

    /**
     * Sets the value of the uncompressedsize property.
     */
    public void setUNCOMPRESSEDSIZE(final long value) {
        this.uncompressedsize = value;
    }

    /**
     * Gets the value of the sourcestartaddress property.
     *
     * @return possible object is
     * {@link String }
     */
    public byte[] getSOURCESTARTADDRESS() {
        return sourcestartaddress;
    }

    /**
     * Sets the value of the sourcestartaddress property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setSOURCESTARTADDRESS(final byte[] value) {
        this.sourcestartaddress = value;
    }

    /**
     * Gets the value of the readdiagcommconnector property.
     *
     * @return possible object is
     * {@link READDIAGCOMMCONNECTOR }
     */
    public READDIAGCOMMCONNECTOR getREADDIAGCOMMCONNECTOR() {
        return readdiagcommconnector;
    }

    /**
     * Sets the value of the readdiagcommconnector property.
     *
     * @param value allowed object is
     *              {@link READDIAGCOMMCONNECTOR }
     */
    public void setREADDIAGCOMMCONNECTOR(final READDIAGCOMMCONNECTOR value) {
        this.readdiagcommconnector = value;
    }

    /**
     * Gets the value of the writediagcommconnector property.
     *
     * @return possible object is
     * {@link WRITEDIAGCOMMCONNECTOR }
     */
    public WRITEDIAGCOMMCONNECTOR getWRITEDIAGCOMMCONNECTOR() {
        return writediagcommconnector;
    }

    /**
     * Sets the value of the writediagcommconnector property.
     *
     * @param value allowed object is
     *              {@link WRITEDIAGCOMMCONNECTOR }
     */
    public void setWRITEDIAGCOMMCONNECTOR(final WRITEDIAGCOMMCONNECTOR value) {
        this.writediagcommconnector = value;
    }

}
