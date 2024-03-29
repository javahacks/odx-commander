//
// This file was generated by the Eclipse Implementation of JAXB, v2.3.3 
// See https://eclipse-ee4j.github.io/jaxb-ri 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2020.11.28 at 09:58:19 AM CET 
//


package com.javahacks.odx.model;

import com.javahacks.odx.index.AbstractLinkTarget;
import com.javahacks.odx.index.Layer;
import com.javahacks.odx.index.LayerAware;

import javax.xml.bind.annotation.*;


/**
 * &lt;p&gt;Java class for PARAM complex type.
 * <p>
 * &lt;p&gt;The following schema fragment specifies the expected content contained within this class.
 * <p>
 * &lt;pre&gt;
 * &amp;lt;complexType name="PARAM"&amp;gt;
 * &amp;lt;complexContent&amp;gt;
 * &amp;lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&amp;gt;
 * &amp;lt;sequence&amp;gt;
 * &amp;lt;group ref="{}ELEMENT-ID"/&amp;gt;
 * &amp;lt;element name="SDGS" type="{}SDGS" minOccurs="0"/&amp;gt;
 * &amp;lt;/sequence&amp;gt;
 * &amp;lt;attribute name="OID" type="{http://www.w3.org/2001/XMLSchema}string" /&amp;gt;
 * &amp;lt;attribute name="SEMANTIC" type="{http://www.w3.org/2001/XMLSchema}string" /&amp;gt;
 * &amp;lt;/restriction&amp;gt;
 * &amp;lt;/complexContent&amp;gt;
 * &amp;lt;/complexType&amp;gt;
 * &lt;/pre&gt;
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "PARAM")
@XmlSeeAlso({
        POSITIONABLEPARAM.class
})
public abstract class PARAM extends AbstractLinkTarget implements LayerAware {

    @XmlElement(name = "SHORT-NAME", required = true)
    protected String shortname;
    @XmlElement(name = "LONG-NAME")
    protected LONGNAME longname;
    @XmlElement(name = "DESC")
    protected DESCRIPTION desc;
    @XmlElement(name = "SDGS")
    protected SDGS sdgs;
    @XmlAttribute(name = "OID")
    @XmlTransient
    protected String oid;
    @XmlAttribute(name = "SEMANTIC")
    protected String semantic;

    @XmlID
    @XmlSchemaType(name = "ID")
    protected String id;

    @XmlTransient
    private Layer layer;

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
     * Gets the value of the semantic property.
     *
     * @return possible object is
     * {@link String }
     */
    public String getSEMANTIC() {
        return semantic;
    }

    /**
     * Sets the value of the semantic property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setSEMANTIC(final String value) {
        this.semantic = value;
    }

    @Override
    public Layer getLayer() {
        return layer;
    }

    public void setLayer(final Layer layer) {
        this.layer = layer;
    }

    public String getID() {
        return id;
    }

    public void setID(final String id) {
        this.id = id;
    }
}
