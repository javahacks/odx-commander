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
 * &lt;p&gt;Java class for SIZEDEF-PHYS-SEGMENT complex type.
 * <p>
 * &lt;p&gt;The following schema fragment specifies the expected content contained within this class.
 * <p>
 * &lt;pre&gt;
 * &amp;lt;complexType name="SIZEDEF-PHYS-SEGMENT"&amp;gt;
 * &amp;lt;complexContent&amp;gt;
 * &amp;lt;extension base="{}PHYS-SEGMENT"&amp;gt;
 * &amp;lt;sequence&amp;gt;
 * &amp;lt;element name="SIZE"&amp;gt;
 * &amp;lt;simpleType&amp;gt;
 * &amp;lt;restriction base="{http://www.w3.org/2001/XMLSchema}unsignedInt"&amp;gt;
 * &amp;lt;minExclusive value="0"/&amp;gt;
 * &amp;lt;/restriction&amp;gt;
 * &amp;lt;/simpleType&amp;gt;
 * &amp;lt;/element&amp;gt;
 * &amp;lt;/sequence&amp;gt;
 * &amp;lt;/extension&amp;gt;
 * &amp;lt;/complexContent&amp;gt;
 * &amp;lt;/complexType&amp;gt;
 * &lt;/pre&gt;
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "SIZEDEF-PHYS-SEGMENT")
public class SIZEDEFPHYSSEGMENT
        extends PHYSSEGMENT {

    @XmlElement(name = "SIZE")
    protected long size;

    /**
     * Gets the value of the size property.
     */
    public long getSIZE() {
        return size;
    }

    /**
     * Sets the value of the size property.
     */
    public void setSIZE(long value) {
        this.size = value;
    }

}
