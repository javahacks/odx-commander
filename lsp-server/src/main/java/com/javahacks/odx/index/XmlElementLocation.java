package com.javahacks.odx.index;

import com.javahacks.odx.lsp.dtos.StartTagLocation;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import java.net.URI;

/**
 * The server needs additional location information for XML elements.
 */
@XmlAccessorType(XmlAccessType.NONE)
public class XmlElementLocation extends StartTagLocation {

    private transient int endLine;
    private transient int endColumn;
    private transient int startTagEndLine;
    private transient int startTagEndColumn;

    public XmlElementLocation(final URI indexPath) {
        super(indexPath);
    }

    public XmlElementLocation(final URI fileUri, final int startLine, final int startColumn) {
        super(fileUri, startLine, startColumn);
    }

    public XmlElementLocation() {
        super();
    }


    public int getEndLine() {
        return endLine;
    }

    public void setEndLine(final int endLine) {
        this.endLine = endLine;
    }

    public int getEndColumn() {
        return endColumn;
    }

    public void setEndColumn(final int endColumn) {
        this.endColumn = endColumn;
    }

    public int getStartTagEndLine() {
        return startTagEndLine;
    }

    public void setStartTagEndLine(final int startTagEndLine) {
        this.startTagEndLine = startTagEndLine;
    }

    public int getStartTagEndColumn() {
        return startTagEndColumn;
    }

    public void setStartTagEndColumn(final int startTagEndColumn) {
        this.startTagEndColumn = startTagEndColumn;
    }
}
