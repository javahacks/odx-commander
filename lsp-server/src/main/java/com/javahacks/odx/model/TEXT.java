//
// This file was generated by the Eclipse Implementation of JAXB, v2.3.3 
// See https://eclipse-ee4j.github.io/jaxb-ri 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2020.11.28 at 09:58:19 AM CET 
//


package com.javahacks.odx.model;

import javax.xml.bind.annotation.*;


/**
 * &lt;p&gt;Java class for TEXT complex type.
 * <p>
 * &lt;p&gt;The following schema fragment specifies the expected content contained within this class.
 * <p>
 * &lt;pre&gt;
 * &amp;lt;complexType name="TEXT"&amp;gt;
 * &amp;lt;simpleContent&amp;gt;
 * &amp;lt;extension base="&amp;lt;http://www.w3.org/2001/XMLSchema&amp;gt;string"&amp;gt;
 * &amp;lt;attribute name="TI" type="{http://www.w3.org/2001/XMLSchema}string" /&amp;gt;
 * &amp;lt;/extension&amp;gt;
 * &amp;lt;/simpleContent&amp;gt;
 * &amp;lt;/complexType&amp;gt;
 * &lt;/pre&gt;
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "TEXT")
@XmlSeeAlso({
        LONGNAME.class
})
public class TEXT {

    @XmlValue
    protected String value;
    @XmlAttribute(name = "TI")
    @XmlTransient
    protected String ti;

    /**
     * Gets the value of the value property.
     *
     * @return possible object is
     * {@link String }
     */
    public String getValue() {
        return value;
    }

    /**
     * Sets the value of the value property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setValue(final String value) {
        this.value = value;
    }

    /**
     * Gets the value of the ti property.
     *
     * @return possible object is
     * {@link String }
     */
    public String getTI() {
        return ti;
    }

    /**
     * Sets the value of the ti property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setTI(final String value) {
        this.ti = value;
    }

}
