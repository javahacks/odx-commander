//
// This file was generated by the Eclipse Implementation of JAXB, v2.3.3 
// See https://eclipse-ee4j.github.io/jaxb-ri 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2020.11.28 at 09:58:19 AM CET 
//


package com.javahacks.odx.model;

import com.javahacks.odx.index.AbstractLinkTarget;
import org.eclipse.persistence.oxm.annotations.XmlPath;

import javax.xml.bind.annotation.*;
import javax.xml.bind.annotation.adapters.CollapsedStringAdapter;
import javax.xml.bind.annotation.adapters.HexBinaryAdapter;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.util.ArrayList;
import java.util.List;


/**
 * &lt;p&gt;Java class for DATABLOCK complex type.
 * <p>
 * &lt;p&gt;The following schema fragment specifies the expected content contained within this class.
 * <p>
 * &lt;pre&gt;
 * &amp;lt;complexType name="DATABLOCK"&amp;gt;
 * &amp;lt;complexContent&amp;gt;
 * &amp;lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&amp;gt;
 * &amp;lt;sequence&amp;gt;
 * &amp;lt;group ref="{}ELEMENT-ID"/&amp;gt;
 * &amp;lt;element name="LOGICAL-BLOCK-INDEX" type="{http://www.w3.org/2001/XMLSchema}hexBinary" minOccurs="0"/&amp;gt;
 * &amp;lt;element name="FLASHDATA-REF" type="{}ODXLINK" minOccurs="0"/&amp;gt;
 * &amp;lt;element name="FILTERS" type="{}FILTERS" minOccurs="0"/&amp;gt;
 * &amp;lt;element name="SEGMENTS" type="{}SEGMENTS" minOccurs="0"/&amp;gt;
 * &amp;lt;element name="TARGET-ADDR-OFFSET" type="{}TARGET-ADDR-OFFSET" minOccurs="0"/&amp;gt;
 * &amp;lt;element name="OWN-IDENTS" type="{}OWN-IDENTS" minOccurs="0"/&amp;gt;
 * &amp;lt;element name="SECURITYS" type="{}SECURITYS" minOccurs="0"/&amp;gt;
 * &amp;lt;element name="SDGS" type="{}SDGS" minOccurs="0"/&amp;gt;
 * &amp;lt;element name="AUDIENCE" type="{}AUDIENCE" minOccurs="0"/&amp;gt;
 * &amp;lt;/sequence&amp;gt;
 * &amp;lt;attribute name="ID" use="required" type="{http://www.w3.org/2001/XMLSchema}ID" /&amp;gt;
 * &amp;lt;attribute name="OID" type="{http://www.w3.org/2001/XMLSchema}string" /&amp;gt;
 * &amp;lt;attribute name="TYPE" use="required" type="{http://www.w3.org/2001/XMLSchema}string" /&amp;gt;
 * &amp;lt;/restriction&amp;gt;
 * &amp;lt;/complexContent&amp;gt;
 * &amp;lt;/complexType&amp;gt;
 * &lt;/pre&gt;
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "DATABLOCK")
public class DATABLOCK extends AbstractLinkTarget {

    @XmlElement(name = "SHORT-NAME", required = true)
    protected String shortname;
    @XmlElement(name = "LONG-NAME")
    protected LONGNAME longname;
    @XmlElement(name = "DESC")
    protected DESCRIPTION desc;
    @XmlElement(name = "LOGICAL-BLOCK-INDEX", type = String.class)
    @XmlJavaTypeAdapter(HexBinaryAdapter.class)
    @XmlSchemaType(name = "hexBinary")
    protected byte[] logicalblockindex;
    @XmlElement(name = "FLASHDATA-REF")
    protected ODXLINK flashdataref;
    @XmlElement(name = "FILTERS")
    protected FILTERS filters;
    @XmlPath("SEGMENTS/SEGMENT")
    protected List<SEGMENT> segments = new ArrayList<>();
    @XmlElement(name = "TARGET-ADDR-OFFSET")
    protected TARGETADDROFFSET targetaddroffset;
    @XmlElement(name = "OWN-IDENTS")
    protected OWNIDENTS ownidents;
    @XmlPath("SECURITYS/SECURITY")
    protected List<SECURITY> securitys = new ArrayList<>();
    @XmlElement(name = "SDGS")
    protected SDGS sdgs;
    @XmlElement(name = "AUDIENCE")
    protected AUDIENCE audience;
    @XmlAttribute(name = "ID", required = true)
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    @XmlID
    @XmlSchemaType(name = "ID")
    protected String id;
    @XmlAttribute(name = "OID")
    @XmlTransient
    protected String oid;
    @XmlAttribute(name = "TYPE", required = true)
    protected String type;

    /**
     * Gets the value of the shortname property.
     *
     * @return possible object is
     * {@link String }
     */
    public String getSHORTNAME() {
        return shortname;
    }

    /**
     * Sets the value of the shortname property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setSHORTNAME(final String value) {
        this.shortname = value;
    }

    /**
     * Gets the value of the longname property.
     *
     * @return possible object is
     * {@link LONGNAME }
     */
    public LONGNAME getLONGNAME() {
        return longname;
    }

    /**
     * Sets the value of the longname property.
     *
     * @param value allowed object is
     *              {@link LONGNAME }
     */
    public void setLONGNAME(final LONGNAME value) {
        this.longname = value;
    }

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
    public void setDESC(final DESCRIPTION value) {
        this.desc = value;
    }

    /**
     * Gets the value of the logicalblockindex property.
     *
     * @return possible object is
     * {@link String }
     */
    public byte[] getLOGICALBLOCKINDEX() {
        return logicalblockindex;
    }

    /**
     * Sets the value of the logicalblockindex property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setLOGICALBLOCKINDEX(final byte[] value) {
        this.logicalblockindex = value;
    }

    /**
     * Gets the value of the flashdataref property.
     *
     * @return possible object is
     * {@link ODXLINK }
     */
    public ODXLINK getFLASHDATAREF() {
        return flashdataref;
    }

    /**
     * Sets the value of the flashdataref property.
     *
     * @param value allowed object is
     *              {@link ODXLINK }
     */
    public void setFLASHDATAREF(final ODXLINK value) {
        this.flashdataref = value;
    }

    /**
     * Gets the value of the filters property.
     *
     * @return possible object is
     * {@link FILTERS }
     */
    public FILTERS getFILTERS() {
        return filters;
    }

    /**
     * Sets the value of the filters property.
     *
     * @param value allowed object is
     *              {@link FILTERS }
     */
    public void setFILTERS(final FILTERS value) {
        this.filters = value;
    }

    public List<SEGMENT> getSEGMENTS() {
        return segments;
    }

    public void setSEGMENTS(final List<SEGMENT> value) {
        this.segments = value;
    }

    /**
     * Gets the value of the targetaddroffset property.
     *
     * @return possible object is
     * {@link TARGETADDROFFSET }
     */
    public TARGETADDROFFSET getTARGETADDROFFSET() {
        return targetaddroffset;
    }

    /**
     * Sets the value of the targetaddroffset property.
     *
     * @param value allowed object is
     *              {@link TARGETADDROFFSET }
     */
    public void setTARGETADDROFFSET(final TARGETADDROFFSET value) {
        this.targetaddroffset = value;
    }

    /**
     * Gets the value of the ownidents property.
     *
     * @return possible object is
     * {@link OWNIDENTS }
     */
    public OWNIDENTS getOWNIDENTS() {
        return ownidents;
    }

    /**
     * Sets the value of the ownidents property.
     *
     * @param value allowed object is
     *              {@link OWNIDENTS }
     */
    public void setOWNIDENTS(final OWNIDENTS value) {
        this.ownidents = value;
    }

    public List<SECURITY> getSECURITYS() {
        return securitys;
    }

    public void setSECURITYS(final List<SECURITY> value) {
        this.securitys = value;
    }

    /**
     * Gets the value of the sdgs property.
     *
     * @return possible object is
     * {@link SDGS }
     */
    public SDGS getSDGS() {
        return sdgs;
    }

    /**
     * Sets the value of the sdgs property.
     *
     * @param value allowed object is
     *              {@link SDGS }
     */
    public void setSDGS(final SDGS value) {
        this.sdgs = value;
    }

    /**
     * Gets the value of the audience property.
     *
     * @return possible object is
     * {@link AUDIENCE }
     */
    public AUDIENCE getAUDIENCE() {
        return audience;
    }

    /**
     * Sets the value of the audience property.
     *
     * @param value allowed object is
     *              {@link AUDIENCE }
     */
    public void setAUDIENCE(final AUDIENCE value) {
        this.audience = value;
    }

    /**
     * Gets the value of the id property.
     *
     * @return possible object is
     * {@link String }
     */
    public String getID() {
        return id;
    }

    /**
     * Sets the value of the id property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setID(final String value) {
        this.id = value;
    }

    /**
     * Gets the value of the oid property.
     *
     * @return possible object is
     * {@link String }
     */
    public String getOID() {
        return oid;
    }

    /**
     * Sets the value of the oid property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setOID(final String value) {
        this.oid = value;
    }

    /**
     * Gets the value of the type property.
     *
     * @return possible object is
     * {@link String }
     */
    public String getTYPE() {
        return type;
    }

    /**
     * Sets the value of the type property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setTYPE(final String value) {
        this.type = value;
    }

}
