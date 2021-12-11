package com.javahacks.odx.index;

import javax.xml.stream.Location;
import javax.xml.stream.XMLStreamException;

public class LocationAwareParsingException extends RuntimeException {
    private final Location lastLocation;
    private final Location currentLocation;

    public LocationAwareParsingException(final Exception ex, final Location lastLocation, final Location currentLocation) {
        super(ex);
        this.lastLocation = lastLocation;
        this.currentLocation = currentLocation;
    }

    public int getStartLine() {
        return currentLocation.getLineNumber();
    }

    public int getStartColumn() {
        return currentLocation.getColumnNumber();
    }

    public int getEndLine() {
        return lastLocation.getLineNumber();
    }

    public int getEndColumn() {
        return lastLocation.getColumnNumber();
    }

    public String getDetailedMessage() {
        Throwable parent = this;
        while (parent.getCause() != null) {
            parent = parent.getCause();
            if (parent instanceof XMLStreamException) {
                return parent.getMessage();
            }
        }
        return parent.getMessage();
    }
}
