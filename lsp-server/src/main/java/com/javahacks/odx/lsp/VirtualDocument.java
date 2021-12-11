package com.javahacks.odx.lsp;

import com.javahacks.odx.index.Category;
import org.eclipse.lsp4j.Position;
import org.eclipse.lsp4j.TextDocumentContentChangeEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URI;
import java.util.List;

/**
 * For each open file a {@link VirtualDocument} is created that is the source of further LSP requests
 * (e.g. CodeLens resolution)
 */
public class VirtualDocument {
    private static final Logger LOGGER = LoggerFactory.getLogger(VirtualDocument.class);
    private final StringBuilder content;
    private final URI uri;
    private final boolean readOnly;
    private boolean erroneous;
    private Category virtualModel;

    public VirtualDocument(final URI documentUri, final String content, final boolean readOnly) {
        this.content = new StringBuilder(content);
        this.uri = documentUri;
        this.readOnly = readOnly;
    }

    public void applyTextChanges(final List<TextDocumentContentChangeEvent> changes) {
        for (final TextDocumentContentChangeEvent changeEvent : changes) {
            final Position start = changeEvent.getRange().getStart();
            final Position end = changeEvent.getRange().getEnd();
            final int startIndex = findIndexByPosition(content, start, 0);
            final int endIndex = findIndexByPosition(content, end, 0);
            content.replace(startIndex, endIndex, changeEvent.getText());
        }
    }


    public String getTextContent() {
        return content.toString();
    }

    public URI getUri() {
        return uri;
    }

    public boolean isReadOnly() {
        return readOnly;
    }

    public Category getVirtualModel() {
        return virtualModel;
    }

    public void setVirtualModel(final Category virtualModel) {
        this.virtualModel = virtualModel;
    }


    /**
     * Returns the error state of this document.
     *
     * @return Returns <code>true</code> when document has errors
     */
    public boolean isErroneous() {
        return erroneous;
    }

    /**
     * Set erroneous state of this document.
     *
     * @param erroneous The new erroneous state
     */
    public void setErroneous(final boolean erroneous) {
        this.erroneous = erroneous;
    }


    private int findIndexByPosition(final StringBuilder stringBuilder, final Position position, int startIndex) {
        int line = 0;
        while (line < position.getLine()) {
            startIndex = getNextLineIndex(stringBuilder, startIndex);
            line++;
        }
        return startIndex + position.getCharacter();
    }

    private int getNextLineIndex(final StringBuilder stringBuilder, final int startIndex) {
        final int lastIndex = stringBuilder.length() - 1;

        for (int i = startIndex; i <= lastIndex; i++) {
            if (stringBuilder.charAt(i) == '\n') {
                return i + 1;
            }
            if (stringBuilder.charAt(i) == '\r') {
                if (i < lastIndex && stringBuilder.charAt(i + 1) == '\n') {
                    return i + 2;
                }
                return i + 1;
            }
        }

        throw new IllegalStateException("No next line in buffer");
    }

}
