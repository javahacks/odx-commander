package com.javahacks.odx.lsp.dtos;

import com.google.gson.annotations.SerializedName;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import java.net.URI;

/**
 * Holds information for the XML start tag location
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class StartTagLocation {
    @SerializedName("fileUri")
    @XmlElement(name = "fileUri")
    private URI fileUri;
    @SerializedName("startLine")
    @XmlElement(name = "startLine")
    private int startLine;
    @SerializedName("startColumn")
    @XmlElement(name = "startColumn")
    private int startColumn;

    public StartTagLocation() {
        //
    }

    public StartTagLocation(final URI uri) {
        this.fileUri = uri;
    }

    public StartTagLocation(final URI uri, final int startLine, final int startColumn) {
        this.fileUri = uri;
        this.startLine = startLine;
        this.startColumn = startColumn;
    }

    public URI getFileUri() {
        return fileUri;
    }

    public void setFileUri(final URI fileUri) {
        this.fileUri = fileUri;
    }

    public int getStartLine() {
        return startLine;
    }

    public void setStartLine(final int startLine) {
        this.startLine = startLine;
    }

    public int getStartColumn() {
        return startColumn;
    }

    public void setStartColumn(final int startColumn) {
        this.startColumn = startColumn;
    }
}
