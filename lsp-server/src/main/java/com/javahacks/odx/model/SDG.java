//
// This file was generated by the Eclipse Implementation of JAXB, v2.3.3 
// See https://eclipse-ee4j.github.io/jaxb-ri 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2020.11.28 at 09:58:19 AM CET 
//


package com.javahacks.odx.model;

import javax.xml.bind.annotation.*;
import java.util.ArrayList;
import java.util.List;


/**
 * &lt;p&gt;Java class for SDG complex type.
 * <p>
 * &lt;p&gt;The following schema fragment specifies the expected content contained within this class.
 * <p>
 * &lt;pre&gt;
 * &amp;lt;complexType name="SDG"&amp;gt;
 * &amp;lt;complexContent&amp;gt;
 * &amp;lt;extension base="{}SPECIAL-DATA"&amp;gt;
 * &amp;lt;sequence&amp;gt;
 * &amp;lt;choice&amp;gt;
 * &amp;lt;element name="SDG-CAPTION-REF" type="{}ODXLINK" minOccurs="0"/&amp;gt;
 * &amp;lt;element name="SDG-CAPTION" type="{}SDG-CAPTION" minOccurs="0"/&amp;gt;
 * &amp;lt;/choice&amp;gt;
 * &amp;lt;choice maxOccurs="unbounded" minOccurs="0"&amp;gt;
 * &amp;lt;element name="SDG" type="{}SDG"/&amp;gt;
 * &amp;lt;element name="SD" type="{}SD"/&amp;gt;
 * &amp;lt;/choice&amp;gt;
 * &amp;lt;/sequence&amp;gt;
 * &amp;lt;attribute name="SI" type="{http://www.w3.org/2001/XMLSchema}string" /&amp;gt;
 * &amp;lt;/extension&amp;gt;
 * &amp;lt;/complexContent&amp;gt;
 * &amp;lt;/complexType&amp;gt;
 * &lt;/pre&gt;
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "SDG")
public class SDG
        extends SPECIALDATA {

    @XmlElement(name = "SDG-CAPTION-REF")
    protected ODXLINK sdgcaptionref;
    @XmlElement(name = "SDG-CAPTION")
    protected SDGCAPTION sdgcaption;
    @XmlElements({
            @XmlElement(name = "SDG", type = SDG.class),
            @XmlElement(name = "SD", type = SD.class)
    })
    protected List<Object> sdgOrSD;
    @XmlAttribute(name = "SI")
    protected String si;

    /**
     * Gets the value of the sdgcaptionref property.
     *
     * @return possible object is
     * {@link ODXLINK }
     */
    public ODXLINK getSDGCAPTIONREF() {
        return sdgcaptionref;
    }

    /**
     * Sets the value of the sdgcaptionref property.
     *
     * @param value allowed object is
     *              {@link ODXLINK }
     */
    public void setSDGCAPTIONREF(ODXLINK value) {
        this.sdgcaptionref = value;
    }

    /**
     * Gets the value of the sdgcaption property.
     *
     * @return possible object is
     * {@link SDGCAPTION }
     */
    public SDGCAPTION getSDGCAPTION() {
        return sdgcaption;
    }

    /**
     * Sets the value of the sdgcaption property.
     *
     * @param value allowed object is
     *              {@link SDGCAPTION }
     */
    public void setSDGCAPTION(SDGCAPTION value) {
        this.sdgcaption = value;
    }

    /**
     * Gets the value of the sdgOrSD property.
     * <p>
     * &lt;p&gt;
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a &lt;CODE&gt;set&lt;/CODE&gt; method for the sdgOrSD property.
     * <p>
     * &lt;p&gt;
     * For example, to add a new item, do as follows:
     * &lt;pre&gt;
     * getSDGOrSD().add(newItem);
     * &lt;/pre&gt;
     * <p>
     * <p>
     * &lt;p&gt;
     * Objects of the following type(s) are allowed in the list
     * {@link SDG }
     * {@link SD }
     */
    public List<Object> getSDGOrSD() {
        if (sdgOrSD == null) {
            sdgOrSD = new ArrayList<Object>();
        }
        return this.sdgOrSD;
    }

    /**
     * Gets the value of the si property.
     *
     * @return possible object is
     * {@link String }
     */
    public String getSI() {
        return si;
    }

    /**
     * Sets the value of the si property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setSI(String value) {
        this.si = value;
    }

}
