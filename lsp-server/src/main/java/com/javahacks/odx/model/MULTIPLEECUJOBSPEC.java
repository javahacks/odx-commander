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
 * &lt;p&gt;Java class for MULTIPLE-ECU-JOB-SPEC complex type.
 * <p>
 * &lt;p&gt;The following schema fragment specifies the expected content contained within this class.
 * <p>
 * &lt;pre&gt;
 * &amp;lt;complexType name="MULTIPLE-ECU-JOB-SPEC"&amp;gt;
 * &amp;lt;complexContent&amp;gt;
 * &amp;lt;extension base="{}ODX-CATEGORY"&amp;gt;
 * &amp;lt;sequence&amp;gt;
 * &amp;lt;element name="MULTIPLE-ECU-JOBS" type="{}MULTIPLE-ECU-JOBS" minOccurs="0"/&amp;gt;
 * &amp;lt;element name="DIAG-DATA-DICTIONARY-SPEC" type="{}DIAG-DATA-DICTIONARY-SPEC" minOccurs="0"/&amp;gt;
 * &amp;lt;element name="FUNCT-CLASSS" type="{}FUNCT-CLASSS" minOccurs="0"/&amp;gt;
 * &amp;lt;element name="ADDITIONAL-AUDIENCES" type="{}ADDITIONAL-AUDIENCES" minOccurs="0"/&amp;gt;
 * &amp;lt;element name="IMPORT-REFS" type="{}IMPORT-REFS" minOccurs="0"/&amp;gt;
 * &amp;lt;/sequence&amp;gt;
 * &amp;lt;/extension&amp;gt;
 * &amp;lt;/complexContent&amp;gt;
 * &amp;lt;/complexType&amp;gt;
 * &lt;/pre&gt;
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "MULTIPLE-ECU-JOB-SPEC")
public class MULTIPLEECUJOBSPEC
        extends ODXCATEGORY {

    @XmlElement(name = "MULTIPLE-ECU-JOBS")
    protected MULTIPLEECUJOBS multipleecujobs;
    @XmlElement(name = "DIAG-DATA-DICTIONARY-SPEC")
    protected DIAGDATADICTIONARYSPEC diagdatadictionaryspec;
    @XmlElement(name = "FUNCT-CLASSS")
    protected FUNCTCLASSS functclasss;
    @XmlElement(name = "ADDITIONAL-AUDIENCES")
    protected ADDITIONALAUDIENCES additionalaudiences;
    @XmlElement(name = "IMPORT-REFS")
    protected IMPORTREFS importrefs;

    /**
     * Gets the value of the multipleecujobs property.
     *
     * @return possible object is
     * {@link MULTIPLEECUJOBS }
     */
    public MULTIPLEECUJOBS getMULTIPLEECUJOBS() {
        return multipleecujobs;
    }

    /**
     * Sets the value of the multipleecujobs property.
     *
     * @param value allowed object is
     *              {@link MULTIPLEECUJOBS }
     */
    public void setMULTIPLEECUJOBS(MULTIPLEECUJOBS value) {
        this.multipleecujobs = value;
    }

    /**
     * Gets the value of the diagdatadictionaryspec property.
     *
     * @return possible object is
     * {@link DIAGDATADICTIONARYSPEC }
     */
    public DIAGDATADICTIONARYSPEC getDIAGDATADICTIONARYSPEC() {
        return diagdatadictionaryspec;
    }

    /**
     * Sets the value of the diagdatadictionaryspec property.
     *
     * @param value allowed object is
     *              {@link DIAGDATADICTIONARYSPEC }
     */
    public void setDIAGDATADICTIONARYSPEC(DIAGDATADICTIONARYSPEC value) {
        this.diagdatadictionaryspec = value;
    }

    /**
     * Gets the value of the functclasss property.
     *
     * @return possible object is
     * {@link FUNCTCLASSS }
     */
    public FUNCTCLASSS getFUNCTCLASSS() {
        return functclasss;
    }

    /**
     * Sets the value of the functclasss property.
     *
     * @param value allowed object is
     *              {@link FUNCTCLASSS }
     */
    public void setFUNCTCLASSS(FUNCTCLASSS value) {
        this.functclasss = value;
    }

    /**
     * Gets the value of the additionalaudiences property.
     *
     * @return possible object is
     * {@link ADDITIONALAUDIENCES }
     */
    public ADDITIONALAUDIENCES getADDITIONALAUDIENCES() {
        return additionalaudiences;
    }

    /**
     * Sets the value of the additionalaudiences property.
     *
     * @param value allowed object is
     *              {@link ADDITIONALAUDIENCES }
     */
    public void setADDITIONALAUDIENCES(ADDITIONALAUDIENCES value) {
        this.additionalaudiences = value;
    }

    /**
     * Gets the value of the importrefs property.
     *
     * @return possible object is
     * {@link IMPORTREFS }
     */
    public IMPORTREFS getIMPORTREFS() {
        return importrefs;
    }

    /**
     * Sets the value of the importrefs property.
     *
     * @param value allowed object is
     *              {@link IMPORTREFS }
     */
    public void setIMPORTREFS(IMPORTREFS value) {
        this.importrefs = value;
    }

}