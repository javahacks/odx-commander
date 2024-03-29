//
// This file was generated by the Eclipse Implementation of JAXB, v2.3.3 
// See https://eclipse-ee4j.github.io/jaxb-ri 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2020.11.28 at 09:58:19 AM CET 
//


package com.javahacks.odx.model;

import com.javahacks.odx.index.AbstractLinkTarget;
import com.javahacks.odx.index.RegionFoldable;

import javax.xml.bind.annotation.*;
import javax.xml.bind.annotation.adapters.CollapsedStringAdapter;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.util.ArrayList;
import java.util.List;


/**
 * &lt;p&gt;Java class for TABLE complex type.
 * <p>
 * &lt;p&gt;The following schema fragment specifies the expected content contained within this class.
 * <p>
 * &lt;pre&gt;
 * &amp;lt;complexType name="TABLE"&amp;gt;
 * &amp;lt;complexContent&amp;gt;
 * &amp;lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&amp;gt;
 * &amp;lt;sequence&amp;gt;
 * &amp;lt;group ref="{}ELEMENT-ID"/&amp;gt;
 * &amp;lt;element name="KEY-LABEL" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&amp;gt;
 * &amp;lt;element name="STRUCT-LABEL" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&amp;gt;
 * &amp;lt;element name="ADMIN-DATA" type="{}ADMIN-DATA" minOccurs="0"/&amp;gt;
 * &amp;lt;element name="KEY-DOP-REF" type="{}ODXLINK" minOccurs="0"/&amp;gt;
 * &amp;lt;group ref="{}ROW-WRAPPER" maxOccurs="unbounded"/&amp;gt;
 * &amp;lt;element name="TABLE-DIAG-COMM-CONNECTORS" type="{}TABLE-DIAG-COMM-CONNECTORS" minOccurs="0"/&amp;gt;
 * &amp;lt;element name="SDGS" type="{}SDGS" minOccurs="0"/&amp;gt;
 * &amp;lt;/sequence&amp;gt;
 * &amp;lt;attribute name="ID" use="required" type="{http://www.w3.org/2001/XMLSchema}ID" /&amp;gt;
 * &amp;lt;attribute name="OID" type="{http://www.w3.org/2001/XMLSchema}string" /&amp;gt;
 * &amp;lt;attribute name="SEMANTIC" type="{http://www.w3.org/2001/XMLSchema}string" /&amp;gt;
 * &amp;lt;/restriction&amp;gt;
 * &amp;lt;/complexContent&amp;gt;
 * &amp;lt;/complexType&amp;gt;
 * &lt;/pre&gt;
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "TABLE")
public class TABLE extends AbstractLinkTarget implements RegionFoldable {

    @XmlElement(name = "SHORT-NAME", required = true)
    protected String shortname;
    @XmlElement(name = "LONG-NAME")
    protected LONGNAME longname;
    @XmlElement(name = "DESC")
    protected DESCRIPTION desc;
    @XmlElement(name = "KEY-LABEL")
    protected String keylabel;
    @XmlElement(name = "STRUCT-LABEL")
    protected String structlabel;
    @XmlElement(name = "ADMIN-DATA")
    protected ADMINDATA admindata;
    @XmlElement(name = "KEY-DOP-REF")
    protected ODXLINK keydopref;
    @XmlElements({
            @XmlElement(name = "TABLE-ROW-REF", type = ODXLINK.class),
            @XmlElement(name = "TABLE-ROW", type = TABLEROW.class)
    })
    protected List<Object> rowwrapper;
    @XmlElement(name = "TABLE-DIAG-COMM-CONNECTORS")
    protected TABLEDIAGCOMMCONNECTORS tablediagcommconnectors;
    @XmlElement(name = "SDGS")
    protected SDGS sdgs;
    @XmlAttribute(name = "ID", required = true)
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    @XmlID
    @XmlSchemaType(name = "ID")
    protected String id;
    @XmlAttribute(name = "OID")
    @XmlTransient
    protected String oid;
    @XmlAttribute(name = "SEMANTIC")
    protected String semantic;

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
     * Gets the value of the keylabel property.
     *
     * @return possible object is
     * {@link String }
     */
    public String getKEYLABEL() {
        return keylabel;
    }

    /**
     * Sets the value of the keylabel property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setKEYLABEL(final String value) {
        this.keylabel = value;
    }

    /**
     * Gets the value of the structlabel property.
     *
     * @return possible object is
     * {@link String }
     */
    public String getSTRUCTLABEL() {
        return structlabel;
    }

    /**
     * Sets the value of the structlabel property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setSTRUCTLABEL(final String value) {
        this.structlabel = value;
    }

    /**
     * Gets the value of the admindata property.
     *
     * @return possible object is
     * {@link ADMINDATA }
     */
    public ADMINDATA getADMINDATA() {
        return admindata;
    }

    /**
     * Sets the value of the admindata property.
     *
     * @param value allowed object is
     *              {@link ADMINDATA }
     */
    public void setADMINDATA(final ADMINDATA value) {
        this.admindata = value;
    }

    /**
     * Gets the value of the keydopref property.
     *
     * @return possible object is
     * {@link ODXLINK }
     */
    public ODXLINK getKEYDOPREF() {
        return keydopref;
    }

    /**
     * Sets the value of the keydopref property.
     *
     * @param value allowed object is
     *              {@link ODXLINK }
     */
    public void setKEYDOPREF(final ODXLINK value) {
        this.keydopref = value;
    }

    /**
     * Gets the value of the rowwrapper property.
     * <p>
     * &lt;p&gt;
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a &lt;CODE&gt;set&lt;/CODE&gt; method for the rowwrapper property.
     * <p>
     * &lt;p&gt;
     * For example, to add a new item, do as follows:
     * &lt;pre&gt;
     * getROWWRAPPER().add(newItem);
     * &lt;/pre&gt;
     * <p>
     * <p>
     * &lt;p&gt;
     * Objects of the following type(s) are allowed in the list
     * {@link ODXLINK }
     * {@link TABLEROW }
     */
    public List<Object> getROWWRAPPER() {
        if (rowwrapper == null) {
            rowwrapper = new ArrayList<>();
        }
        return this.rowwrapper;
    }

    /**
     * Gets the value of the tablediagcommconnectors property.
     *
     * @return possible object is
     * {@link TABLEDIAGCOMMCONNECTORS }
     */
    public TABLEDIAGCOMMCONNECTORS getTABLEDIAGCOMMCONNECTORS() {
        return tablediagcommconnectors;
    }

    /**
     * Sets the value of the tablediagcommconnectors property.
     *
     * @param value allowed object is
     *              {@link TABLEDIAGCOMMCONNECTORS }
     */
    public void setTABLEDIAGCOMMCONNECTORS(final TABLEDIAGCOMMCONNECTORS value) {
        this.tablediagcommconnectors = value;
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

}
