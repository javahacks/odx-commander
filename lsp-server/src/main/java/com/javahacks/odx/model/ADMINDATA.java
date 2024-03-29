//
// This file was generated by the Eclipse Implementation of JAXB, v2.3.3 
// See https://eclipse-ee4j.github.io/jaxb-ri 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2020.11.28 at 09:58:19 AM CET 
//


package com.javahacks.odx.model;

import com.javahacks.odx.index.CommentFoldable;
import com.javahacks.odx.index.HiddenModelElement;
import com.javahacks.odx.index.LocationAware;
import org.eclipse.persistence.oxm.annotations.XmlPath;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.CollapsedStringAdapter;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.util.ArrayList;
import java.util.List;


/**
 * &lt;p&gt;Java class for ADMIN-DATA complex type.
 * <p>
 * &lt;p&gt;The following schema fragment specifies the expected content contained within this class.
 * <p>
 * &lt;pre&gt;
 * &amp;lt;complexType name="ADMIN-DATA"&amp;gt;
 * &amp;lt;complexContent&amp;gt;
 * &amp;lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&amp;gt;
 * &amp;lt;sequence&amp;gt;
 * &amp;lt;element name="LANGUAGE" minOccurs="0"&amp;gt;
 * &amp;lt;simpleType&amp;gt;
 * &amp;lt;restriction base="{http://www.w3.org/2001/XMLSchema}language"&amp;gt;
 * &amp;lt;pattern value="[a-z]{2}(-[A-Z]{2})?"/&amp;gt;
 * &amp;lt;/restriction&amp;gt;
 * &amp;lt;/simpleType&amp;gt;
 * &amp;lt;/element&amp;gt;
 * &amp;lt;element name="COMPANY-DOC-INFOS" type="{}COMPANY-DOC-INFOS" minOccurs="0"/&amp;gt;
 * &amp;lt;element name="DOC-REVISIONS" type="{}DOC-REVISIONS" minOccurs="0"/&amp;gt;
 * &amp;lt;/sequence&amp;gt;
 * &amp;lt;/restriction&amp;gt;
 * &amp;lt;/complexContent&amp;gt;
 * &amp;lt;/complexType&amp;gt;
 * &lt;/pre&gt;
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ADMIN-DATA")
public class ADMINDATA extends LocationAware.AbstractLocationAware implements HiddenModelElement, CommentFoldable {

    @XmlElement(name = "LANGUAGE")
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    protected String language;
    @XmlPath("COMPANY-DOC-INFOS/COMPANY-DOC-INFO")
    protected List<COMPANYDOCINFO> companydocinfos = new ArrayList<>();
    @XmlElement(name = "DOC-REVISIONS")
    protected DOCREVISIONS docrevisions;

    /**
     * Gets the value of the language property.
     *
     * @return possible object is
     * {@link String }
     */
    public String getLANGUAGE() {
        return language;
    }

    /**
     * Sets the value of the language property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setLANGUAGE(final String value) {
        this.language = value;
    }

    /**
     * Gets the value of the docrevisions property.
     *
     * @return possible object is
     * {@link DOCREVISIONS }
     */
    public DOCREVISIONS getDOCREVISIONS() {
        return docrevisions;
    }

    /**
     * Sets the value of the docrevisions property.
     *
     * @param value allowed object is
     *              {@link DOCREVISIONS }
     */
    public void setDOCREVISIONS(final DOCREVISIONS value) {
        this.docrevisions = value;
    }

    public List<COMPANYDOCINFO> getCompanydocinfos() {
        return companydocinfos;
    }
}
