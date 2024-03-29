//
// This file was generated by the Eclipse Implementation of JAXB, v2.3.3 
// See https://eclipse-ee4j.github.io/jaxb-ri 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2020.11.28 at 09:58:19 AM CET 
//


package com.javahacks.odx.model;

import org.eclipse.persistence.oxm.annotations.XmlPath;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import java.util.ArrayList;
import java.util.List;


/**
 * &lt;p&gt;Java class for VEHICLE-INFO-SPEC complex type.
 * <p>
 * &lt;p&gt;The following schema fragment specifies the expected content contained within this class.
 * <p>
 * &lt;pre&gt;
 * &amp;lt;complexType name="VEHICLE-INFO-SPEC"&amp;gt;
 * &amp;lt;complexContent&amp;gt;
 * &amp;lt;extension base="{}ODX-CATEGORY"&amp;gt;
 * &amp;lt;sequence&amp;gt;
 * &amp;lt;element name="INFO-COMPONENTS" type="{}INFO-COMPONENTS" minOccurs="0"/&amp;gt;
 * &amp;lt;element name="VEHICLE-INFORMATIONS" type="{}VEHICLE-INFORMATIONS"/&amp;gt;
 * &amp;lt;/sequence&amp;gt;
 * &amp;lt;/extension&amp;gt;
 * &amp;lt;/complexContent&amp;gt;
 * &amp;lt;/complexType&amp;gt;
 * &lt;/pre&gt;
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "VEHICLE-INFO-SPEC")
public class VEHICLEINFOSPEC extends ODXCATEGORY {

    @XmlPath("VEHICLE-INFORMATIONS/VEHICLE-INFORMATION")
    private final List<VEHICLEINFORMATION> vehicleinformations = new ArrayList<>();
    @XmlElement(name = "INFO-COMPONENTS")
    protected INFOCOMPONENTS infocomponents;

    /**
     * Gets the value of the infocomponents property.
     *
     * @return possible object is
     * {@link INFOCOMPONENTS }
     */
    public INFOCOMPONENTS getINFOCOMPONENTS() {
        return infocomponents;
    }

    /**
     * Sets the value of the infocomponents property.
     *
     * @param value allowed object is
     *              {@link INFOCOMPONENTS }
     */
    public void setINFOCOMPONENTS(final INFOCOMPONENTS value) {
        this.infocomponents = value;
    }


    public List<VEHICLEINFORMATION> getVehicleinformations() {
        return vehicleinformations;
    }

    @Override
    public boolean isExpandable() {
        return !vehicleinformations.isEmpty();
    }
}
