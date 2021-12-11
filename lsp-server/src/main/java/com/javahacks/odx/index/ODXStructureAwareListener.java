package com.javahacks.odx.index;

import com.javahacks.odx.model.*;

import javax.xml.bind.Unmarshaller;
import javax.xml.stream.Location;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import javax.xml.stream.util.StreamReaderDelegate;
import java.net.URI;
import java.util.Arrays;
import java.util.List;

/**
 * Used to extract additional structural ODX information not provided by domain model
 */
class ODXStructureAwareListener extends Unmarshaller.Listener {
    private static final List<String> VALID_SN_TAGS = Arrays.asList("DOP-SNREF", "DIAG-COMM-SNREF");
    private final ReaderDelegate readerDelegate;
    private final URI filePath;

    private Category activeCategory;
    private Layer activeLayer;

    ODXStructureAwareListener(final ReaderDelegate readerDelegate, final URI fileUri) {
        this.readerDelegate = readerDelegate;
        this.filePath = fileUri;
    }

    @Override
    public void beforeUnmarshal(final Object target, final Object parent) {
        if (target instanceof DIAGLAYER) {
            activeLayer = (DIAGLAYER) target;
        }

        if (target instanceof Category) {
            activeCategory = (Category) target;
        }

        if (target instanceof LocationAware) {
            final XmlElementLocation position = createLocation(filePath, readerDelegate.getLastLocation(), readerDelegate.getCurrentLocation());
            ((LocationAware) target).setLocation(position);
            if (target instanceof AbstractLink) {
                ((AbstractLink) target).setDocument(activeLayer != null ? activeLayer : activeCategory);
            }
            if (activeCategory != null) {
                activeCategory.addLocationAware((LocationAware) target);
            }
        }
    }

    @Override
    public void afterUnmarshal(final Object target, final Object parent) {
        if (target instanceof CategoryAware && activeCategory != null) {
            ((CategoryAware) target).setCategory(activeCategory);
        }

        if (target instanceof LayerAware && activeLayer != null) {
            ((LayerAware) target).setLayer(activeLayer);
        }

        if (target instanceof SNREF && VALID_SN_TAGS.contains(readerDelegate.getTagName()) && activeLayer != null) {
            ((SNREF) target).setTagName(readerDelegate.getTagName());
        }

        if (target instanceof IdentifiableElement) {
            if (activeLayer != null) {
                activeLayer.index(((IdentifiableElement) target).getID(), target);
            }
            if (activeCategory != null) {
                activeCategory.index(((IdentifiableElement) target).getID(), target);
            }
        }

        if (parent instanceof Category && target instanceof Layer) {
            ((Category) parent).addLayer((Layer) target);
            activeLayer = null;
        }


        if (activeLayer != null) {
            indexValueInheritedElement(target);
        }

        if (parent instanceof ODX && activeCategory != null) {
            activeCategory.setParent((ODX) parent);
        }

        if (target instanceof LocationAware) {
            final Location currentLocation = readerDelegate.getCurrentLocation();
            ((LocationAware) target).getLocation().setEndLine(currentLocation.getLineNumber() - 1);
            ((LocationAware) target).getLocation().setEndColumn(currentLocation.getColumnNumber());
        }
    }

    private void indexValueInheritedElement(final Object target) {
        if (target instanceof TABLE
                || target instanceof UNITGROUP
                || target instanceof GLOBALNEGRESPONSE
                || target instanceof DOPBASE
                || target instanceof DIAGCOMM) {

            activeLayer.getValueInheritedElements().add(target);
        }
    }

    private XmlElementLocation createLocation(final URI fileUri, final Location start, final Location end) {
        //include < and > brackets
        final XmlElementLocation location = new XmlElementLocation(fileUri, start.getLineNumber() - 1, start.getColumnNumber() - 2);
        location.setStartTagEndLine(end.getLineNumber() - 1);
        location.setStartTagEndColumn(end.getColumnNumber());
        return location;
    }

}

class ReaderDelegate extends StreamReaderDelegate {

    private String tagName;
    private javax.xml.stream.Location lastLocation;
    private javax.xml.stream.Location currentLocation;

    ReaderDelegate(final XMLStreamReader xmlStreamReader) {
        super(xmlStreamReader);
    }

    @Override
    public int next() throws XMLStreamException {
        final int next = super.next();
        this.lastLocation = this.currentLocation;
        this.currentLocation = getLocation();
        return next;
    }

    @Override
    public String getLocalName() {
        tagName = super.getLocalName();
        return tagName;
    }

    public javax.xml.stream.Location getLastLocation() {
        if (lastLocation != null) {
            return lastLocation;
        }
        return getLocation();
    }

    public javax.xml.stream.Location getCurrentLocation() {
        if (currentLocation != null) {
            return currentLocation;
        }
        return getLocation();
    }

    public String getTagName() {
        return tagName;
    }


}

