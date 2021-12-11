package com.javahacks.odx.lsp.dtos;

import com.google.gson.annotations.SerializedName;


public class Link {
    @SerializedName("docType")
    private String docType;
    @SerializedName("docRef")
    private String docRef;
    @SerializedName("idRef")
    private String idRef;

    public Link() {

    }

    public Link(String docType, String docRef, String idRef) {
        this.docType = docType;
        this.docRef = docRef;
        this.idRef = idRef;
    }

    public String getDocType() {
        return docType;
    }

    public void setDocType(String docType) {
        this.docType = docType;
    }

    public String getDocRef() {
        return docRef;
    }

    public void setDocRef(String docRef) {
        this.docRef = docRef;
    }

    public String getIdRef() {
        return idRef;
    }

    public void setIdRef(String idRef) {
        this.idRef = idRef;
    }
}
