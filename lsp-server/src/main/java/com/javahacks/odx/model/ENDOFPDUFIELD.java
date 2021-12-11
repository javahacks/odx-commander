//
// This file was generated by the Eclipse Implementation of JAXB, v2.3.3 
// See https://eclipse-ee4j.github.io/jaxb-ri 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2020.11.28 at 09:58:19 AM CET 
//


package com.javahacks.odx.model;

import javax.xml.bind.annotation.*;


/**
 * &lt;p&gt;Java class for END-OF-PDU-FIELD complex type.
 * <p>
 * &lt;p&gt;The following schema fragment specifies the expected content contained within this class.
 * <p>
 * &lt;pre&gt;
 * &amp;lt;complexType name="END-OF-PDU-FIELD"&amp;gt;
 * &amp;lt;complexContent&amp;gt;
 * &amp;lt;extension base="{}FIELD"&amp;gt;
 * &amp;lt;sequence&amp;gt;
 * &amp;lt;element name="MAX-NUMBER-OF-ITEMS" type="{http://www.w3.org/2001/XMLSchema}unsignedInt" minOccurs="0"/&amp;gt;
 * &amp;lt;element name="MIN-NUMBER-OF-ITEMS" type="{http://www.w3.org/2001/XMLSchema}unsignedInt" minOccurs="0"/&amp;gt;
 * &amp;lt;/sequence&amp;gt;
 * &amp;lt;/extension&amp;gt;
 * &amp;lt;/complexContent&amp;gt;
 * &amp;lt;/complexType&amp;gt;
 * &lt;/pre&gt;
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "END-OF-PDU-FIELD")
public class ENDOFPDUFIELD
        extends FIELD {

    @XmlElement(name = "MAX-NUMBER-OF-ITEMS")
    @XmlSchemaType(name = "unsignedInt")
    protected Long maxnumberofitems;
    @XmlElement(name = "MIN-NUMBER-OF-ITEMS")
    @XmlSchemaType(name = "unsignedInt")
    protected Long minnumberofitems;

    /**
     * Gets the value of the maxnumberofitems property.
     *
     * @return possible object is
     * {@link Long }
     */
    public Long getMAXNUMBEROFITEMS() {
        return maxnumberofitems;
    }

    /**
     * Sets the value of the maxnumberofitems property.
     *
     * @param value allowed object is
     *              {@link Long }
     */
    public void setMAXNUMBEROFITEMS(Long value) {
        this.maxnumberofitems = value;
    }

    /**
     * Gets the value of the minnumberofitems property.
     *
     * @return possible object is
     * {@link Long }
     */
    public Long getMINNUMBEROFITEMS() {
        return minnumberofitems;
    }

    /**
     * Sets the value of the minnumberofitems property.
     *
     * @param value allowed object is
     *              {@link Long }
     */
    public void setMINNUMBEROFITEMS(Long value) {
        this.minnumberofitems = value;
    }

}