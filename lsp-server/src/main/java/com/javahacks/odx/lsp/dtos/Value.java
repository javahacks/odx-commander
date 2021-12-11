package com.javahacks.odx.lsp.dtos;

import com.google.gson.annotations.SerializedName;


public class Value {

    @SerializedName("value")
    private String value;

    public Value (String value){
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
