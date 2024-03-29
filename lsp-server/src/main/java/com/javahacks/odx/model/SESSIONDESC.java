//
// This file was generated by the Eclipse Implementation of JAXB, v2.3.3 
// See https://eclipse-ee4j.github.io/jaxb-ri 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2020.11.28 at 09:58:19 AM CET 
//


package com.javahacks.odx.model;

import com.javahacks.odx.index.LocationAware.AbstractLocationAware;

import javax.xml.bind.annotation.*;


/**
 * &lt;p&gt;Java class for SESSION-DESC complex type.
 * <p>
 * &lt;p&gt;The following schema fragment specifies the expected content contained within this class.
 * <p>
 * &lt;pre&gt;
 * &amp;lt;complexType name="SESSION-DESC"&amp;gt;
 * &amp;lt;complexContent&amp;gt;
 * &amp;lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&amp;gt;
 * &amp;lt;sequence&amp;gt;
 * &amp;lt;group ref="{}ELEMENT-ID"/&amp;gt;
 * &amp;lt;element name="PARTNUMBER" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&amp;gt;
 * &amp;lt;element name="PRIORITY" type="{http://www.w3.org/2001/XMLSchema}unsignedInt" minOccurs="0"/&amp;gt;
 * &amp;lt;element name="SESSION-SNREF" type="{}SNREF"/&amp;gt;
 * &amp;lt;element name="DIAG-COMM-SNREF" type="{}SNREF" minOccurs="0"/&amp;gt;
 * &amp;lt;element name="FLASH-CLASS-REFS" type="{}FLASH-CLASS-REFS" minOccurs="0"/&amp;gt;
 * &amp;lt;element name="SDGS" type="{}SDGS" minOccurs="0"/&amp;gt;
 * &amp;lt;element name="AUDIENCE" type="{}AUDIENCE" minOccurs="0"/&amp;gt;
 * &amp;lt;element name="OWN-IDENT" type="{}OWN-IDENT" minOccurs="0"/&amp;gt;
 * &amp;lt;/sequence&amp;gt;
 * &amp;lt;attribute name="OID" type="{http://www.w3.org/2001/XMLSchema}string" /&amp;gt;
 * &amp;lt;attribute name="DIRECTION" use="required" type="{}DIRECTION" /&amp;gt;
 * &amp;lt;/restriction&amp;gt;
 * &amp;lt;/complexContent&amp;gt;
 * &amp;lt;/complexType&amp;gt;
 * &lt;/pre&gt;
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "SESSION-DESC")
public class SESSIONDESC extends AbstractLocationAware {

    @XmlElement(name = "SHORT-NAME", required = true)
    protected String shortname;
    @XmlElement(name = "LONG-NAME")
    protected LONGNAME longname;
    @XmlElement(name = "DESC")
    protected DESCRIPTION desc;
    @XmlElement(name = "PARTNUMBER")
    protected String partnumber;
    @XmlElement(name = "PRIORITY")
    @XmlSchemaType(name = "unsignedInt")
    protected Long priority;
    @XmlElement(name = "SESSION-SNREF", required = true)
    protected SNREF sessionsnref;
    @XmlElement(name = "DIAG-COMM-SNREF")
    protected SNREF diagcommsnref;
    @XmlElement(name = "FLASH-CLASS-REFS")
    protected FLASHCLASSREFS flashclassrefs;
    @XmlElement(name = "SDGS")
    protected SDGS sdgs;
    @XmlElement(name = "AUDIENCE")
    protected AUDIENCE audience;
    @XmlElement(name = "OWN-IDENT")
    protected OWNIDENT ownident;
    @XmlAttribute(name = "OID")
    @XmlTransient
    protected String oid;
    @XmlAttribute(name = "DIRECTION", required = true)
    protected DIRECTION direction;

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
     * Gets the value of the partnumber property.
     *
     * @return possible object is
     * {@link String }
     */
    public String getPARTNUMBER() {
        return partnumber;
    }

    /**
     * Sets the value of the partnumber property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setPARTNUMBER(final String value) {
        this.partnumber = value;
    }

    /**
     * Gets the value of the priority property.
     *
     * @return possible object is
     * {@link Long }
     */
    public Long getPRIORITY() {
        return priority;
    }

    /**
     * Sets the value of the priority property.
     *
     * @param value allowed object is
     *              {@link Long }
     */
    public void setPRIORITY(final Long value) {
        this.priority = value;
    }

    /**
     * Gets the value of the sessionsnref property.
     *
     * @return possible object is
     * {@link SNREF }
     */
    public SNREF getSESSIONSNREF() {
        return sessionsnref;
    }

    /**
     * Sets the value of the sessionsnref property.
     *
     * @param value allowed object is
     *              {@link SNREF }
     */
    public void setSESSIONSNREF(final SNREF value) {
        this.sessionsnref = value;
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
    public void setDIAGCOMMSNREF(final SNREF value) {
        this.diagcommsnref = value;
    }

    /**
     * Gets the value of the flashclassrefs property.
     *
     * @return possible object is
     * {@link FLASHCLASSREFS }
     */
    public FLASHCLASSREFS getFLASHCLASSREFS() {
        return flashclassrefs;
    }

    /**
     * Sets the value of the flashclassrefs property.
     *
     * @param value allowed object is
     *              {@link FLASHCLASSREFS }
     */
    public void setFLASHCLASSREFS(final FLASHCLASSREFS value) {
        this.flashclassrefs = value;
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
     * Gets the value of the ownident property.
     *
     * @return possible object is
     * {@link OWNIDENT }
     */
    public OWNIDENT getOWNIDENT() {
        return ownident;
    }

    /**
     * Sets the value of the ownident property.
     *
     * @param value allowed object is
     *              {@link OWNIDENT }
     */
    public void setOWNIDENT(final OWNIDENT value) {
        this.ownident = value;
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
     * Gets the value of the direction property.
     *
     * @return possible object is
     * {@link DIRECTION }
     */
    public DIRECTION getDIRECTION() {
        return direction;
    }

    /**
     * Sets the value of the direction property.
     *
     * @param value allowed object is
     *              {@link DIRECTION }
     */
    public void setDIRECTION(final DIRECTION value) {
        this.direction = value;
    }

}
