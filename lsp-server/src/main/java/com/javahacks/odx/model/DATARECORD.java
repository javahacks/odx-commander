//
// This file was generated by the Eclipse Implementation of JAXB, v2.3.3 
// See https://eclipse-ee4j.github.io/jaxb-ri 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2020.11.28 at 09:58:19 AM CET 
//


package com.javahacks.odx.model;

import javax.xml.bind.annotation.*;


/**
 * &lt;p&gt;Java class for DATA-RECORD complex type.
 * <p>
 * &lt;p&gt;The following schema fragment specifies the expected content contained within this class.
 * <p>
 * &lt;pre&gt;
 * &amp;lt;complexType name="DATA-RECORD"&amp;gt;
 * &amp;lt;complexContent&amp;gt;
 * &amp;lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&amp;gt;
 * &amp;lt;sequence&amp;gt;
 * &amp;lt;group ref="{}ELEMENT-ID"/&amp;gt;
 * &amp;lt;element name="RULE" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&amp;gt;
 * &amp;lt;element name="KEY" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&amp;gt;
 * &amp;lt;element name="DATA-ID" type="{}IDENT-VALUE" minOccurs="0"/&amp;gt;
 * &amp;lt;element name="SDGS" type="{}SDGS" minOccurs="0"/&amp;gt;
 * &amp;lt;choice&amp;gt;
 * &amp;lt;element name="DATAFILE" type="{}DATAFILE" minOccurs="0"/&amp;gt;
 * &amp;lt;element name="DATA" type="{}DATA" minOccurs="0"/&amp;gt;
 * &amp;lt;/choice&amp;gt;
 * &amp;lt;element name="AUDIENCE" type="{}AUDIENCE" minOccurs="0"/&amp;gt;
 * &amp;lt;/sequence&amp;gt;
 * &amp;lt;attribute name="DATAFORMAT" use="required" type="{}DATAFORMAT-SELECTION" /&amp;gt;
 * &amp;lt;/restriction&amp;gt;
 * &amp;lt;/complexContent&amp;gt;
 * &amp;lt;/complexType&amp;gt;
 * &lt;/pre&gt;
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "DATA-RECORD")
public class DATARECORD {

    @XmlElement(name = "SHORT-NAME", required = true)
    protected String shortname;
    @XmlElement(name = "LONG-NAME")
    protected LONGNAME longname;
    @XmlElement(name = "DESC")
    protected DESCRIPTION desc;
    @XmlElement(name = "RULE")
    protected String rule;
    @XmlElement(name = "KEY")
    protected String key;
    @XmlElement(name = "DATA-ID")
    protected IDENTVALUE dataid;
    @XmlElement(name = "SDGS")
    protected SDGS sdgs;
    @XmlElement(name = "DATAFILE")
    protected DATAFILE datafile;
    @XmlElement(name = "DATA")
    protected DATA data;
    @XmlElement(name = "AUDIENCE")
    protected AUDIENCE audience;
    @XmlAttribute(name = "DATAFORMAT", required = true)
    protected DATAFORMATSELECTION dataformat;

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
    public void setSHORTNAME(String value) {
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
    public void setLONGNAME(LONGNAME value) {
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
    public void setDESC(DESCRIPTION value) {
        this.desc = value;
    }

    /**
     * Gets the value of the rule property.
     *
     * @return possible object is
     * {@link String }
     */
    public String getRULE() {
        return rule;
    }

    /**
     * Sets the value of the rule property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setRULE(String value) {
        this.rule = value;
    }

    /**
     * Gets the value of the key property.
     *
     * @return possible object is
     * {@link String }
     */
    public String getKEY() {
        return key;
    }

    /**
     * Sets the value of the key property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setKEY(String value) {
        this.key = value;
    }

    /**
     * Gets the value of the dataid property.
     *
     * @return possible object is
     * {@link IDENTVALUE }
     */
    public IDENTVALUE getDATAID() {
        return dataid;
    }

    /**
     * Sets the value of the dataid property.
     *
     * @param value allowed object is
     *              {@link IDENTVALUE }
     */
    public void setDATAID(IDENTVALUE value) {
        this.dataid = value;
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
    public void setSDGS(SDGS value) {
        this.sdgs = value;
    }

    /**
     * Gets the value of the datafile property.
     *
     * @return possible object is
     * {@link DATAFILE }
     */
    public DATAFILE getDATAFILE() {
        return datafile;
    }

    /**
     * Sets the value of the datafile property.
     *
     * @param value allowed object is
     *              {@link DATAFILE }
     */
    public void setDATAFILE(DATAFILE value) {
        this.datafile = value;
    }

    /**
     * Gets the value of the data property.
     *
     * @return possible object is
     * {@link DATA }
     */
    public DATA getDATA() {
        return data;
    }

    /**
     * Sets the value of the data property.
     *
     * @param value allowed object is
     *              {@link DATA }
     */
    public void setDATA(DATA value) {
        this.data = value;
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
    public void setAUDIENCE(AUDIENCE value) {
        this.audience = value;
    }

    /**
     * Gets the value of the dataformat property.
     *
     * @return possible object is
     * {@link DATAFORMATSELECTION }
     */
    public DATAFORMATSELECTION getDATAFORMAT() {
        return dataformat;
    }

    /**
     * Sets the value of the dataformat property.
     *
     * @param value allowed object is
     *              {@link DATAFORMATSELECTION }
     */
    public void setDATAFORMAT(DATAFORMATSELECTION value) {
        this.dataformat = value;
    }

}