package com.javahacks.odx.index;

import com.javahacks.odx.model.DOCTYPE;
import com.javahacks.odx.model.ODXLINK;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "link")
public class ProxyRef {
    @XmlElement(name = "docType")
    private String docType;
    @XmlElement(name = "shortName")
    private String shortName;
    @XmlElement(name = "id")
    private String id;

    public String getShortName() {
        return shortName;
    }

    public void setShortName(final String shortName) {
        this.shortName = shortName;
    }

    public String getDocType() {
        return docType;
    }

    public void setDocType(final String docType) {
        this.docType = docType;
    }

    public String getId() {
        return id;
    }

    public void setId(final String id) {
        this.id = id;
    }

    public boolean matchesDocument(final String docType, final String docRef) {
        return docType.equals(getDocType()) && docRef.equals(getShortName());
    }

    public boolean matchesLink(final ODXLINK odxlink) {
        return matchesDocument(odxlink.getDOCTYPE().name(), odxlink.getDOCREF())
                && odxlink.getIDREF().equals(getId());
    }

    public static ProxyRef create(final String docType, final String shortName, final String id) {
        final ProxyRef proxyRef = new ProxyRef();
        proxyRef.setDocType(docType);
        proxyRef.setShortName(shortName);
        proxyRef.setId(id);
        return proxyRef;
    }
    public ODXLINK toOdxLink(){
        final ODXLINK odxlink=new ODXLINK();
        odxlink.setIDREF(getId());
        odxlink.setDOCREF(getShortName());
        odxlink.setDOCTYPE(DOCTYPE.fromValue(getDocType()));
        return odxlink;
    }
}

