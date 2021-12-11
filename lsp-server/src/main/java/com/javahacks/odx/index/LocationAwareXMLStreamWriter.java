package com.javahacks.odx.index;

import com.javahacks.odx.model.ODX;

import javax.xml.bind.Marshaller;
import javax.xml.namespace.NamespaceContext;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.io.Writer;
import java.util.Iterator;
import java.util.Stack;

/**
 * Generates simplified XML output and updates element locations according to the
 * new location in modified and formatted document.
 */
public class LocationAwareXMLStreamWriter extends Marshaller.Listener implements XMLStreamWriter {
    public static final String NOTICE = "NOTICE: THE ORIGINAL DOCUMENT WAS FORMATTED AND SIMPLIFIED BY ODX COMMANDER";
    static final String IDENT_STRING = "  ";

    private final Stack<String> tagStack = new Stack<>();
    private final Writer writer;
    private final Marshaller marshaller;

    private int lineNumber = 0;
    private int lineOffset = 0;
    private MarshallingState currentState;
    private LocationAware activeLocationAware;
    private LocationAware openLocationAware;
    private HiddenModelElement hiddenElement;

    public LocationAwareXMLStreamWriter(final Marshaller marshaller, final Writer writer) {
        this.marshaller = marshaller;
        this.writer = writer;
    }

    public void formatModel(final ODX model) throws Exception {
        marshaller.setProperty(Marshaller.JAXB_FRAGMENT, true);
        marshaller.setListener(this);
        writeHeader(writer);
        marshaller.marshal(model, this);
    }

    private void writeHeader(final Writer writer) throws IOException {
        writer.write("<?xml version=\"1.0\" ?>");
        writeLine();
        writer.write("<!-- " + NOTICE + " -->");
        writeLine();
    }

    @Override
    public void beforeMarshal(final Object source) {
        activeLocationAware = null;
        if (source instanceof HiddenModelElement && hiddenElement == null) {
            hiddenElement = (HiddenModelElement) source;
        }
        if (source instanceof LocationAware) {
            activeLocationAware = (LocationAware) source;
            resetEndLines();
        }
    }


    @Override
    public void afterMarshal(final Object source) {
        activeLocationAware = null;
        if (source == hiddenElement) {
            hiddenElement = null;
            currentState = MarshallingState.HIDDEN_ELEMENT_CLOSED;
        }
        if (source instanceof LocationAware) {
            activeLocationAware = (LocationAware) source;
        }
    }

    @Override
    public void writeStartElement(final String localName) {
        writeStartElement(null, localName, null);
    }

    @Override
    public void writeStartElement(final String namespaceURI, final String localName) {
        writeStartElement(null, localName, null);
    }

    @Override
    public void writeStartElement(final String prefix, final String localName, final String namespaceURI) {
        if (MarshallingState.START_TAG_OPEN == currentState || MarshallingState.ATTRIBUTE_WRITTEN == currentState) {
            writeEndTag();
            writeLine();
        }
        if (hiddenElement != null) {
            return;
        }

        lineOffset = 0;
        for (int i = 0; i < tagStack.size(); i++) {
            writeToDocument(IDENT_STRING);
        }
        tagStack.push(localName);


        updateStartLocation();
        writeToDocument("<" + localName);
        currentState = MarshallingState.START_TAG_OPEN;
    }


    @Override
    public void writeAttribute(final String localName, final String value) {
        writeAttribute(null, localName, value);
    }

    @Override
    public void writeAttribute(final String prefix, final String namespaceURI, final String localName, final String value) {
        writeAttribute(null, localName, value);
    }

    @Override
    public void writeAttribute(final String namespaceURI, final String localName, final String value) {
        if (hiddenElement != null) {
            return;
        }
        writeToDocument(" " + localName + "=\"" + value + "\"");
        currentState = MarshallingState.ATTRIBUTE_WRITTEN;
    }


    @Override
    public void writeCharacters(final String text) {
        if (hiddenElement != null) {
            return;
        }
        writeEndTag();
        writeToDocument(text.trim().replaceAll("[\r\n]", ""));
        currentState = MarshallingState.TEXT_WRITTEN;
    }

    private void writeEndTag() {
        writeToDocument(">");
        updateStartTagEndLocation();
    }

    @Override
    public void writeEndElement() {
        if (hiddenElement != null) {
            return;
        }
        if (currentState == MarshallingState.HIDDEN_ELEMENT_CLOSED) {
            currentState = null;
            return;
        }

        if (currentState == MarshallingState.START_TAG_OPEN || currentState == MarshallingState.ATTRIBUTE_WRITTEN) {
            writeToDocument("/");
            writeEndTag();
            tagStack.pop();
        } else {
            if (currentState != MarshallingState.TEXT_WRITTEN) {
                lineOffset = 0;
                for (int i = 0; i < tagStack.size() - 1; i++) {
                    writeToDocument(IDENT_STRING);
                }
            }
            writeToDocument("</" + tagStack.pop() + ">");
        }

        updateEndLocation();

        if (!tagStack.isEmpty()) {
            writeLine();
        }

        currentState = MarshallingState.TAG_COSED;
    }


    @Override
    public void writeEmptyElement(final String namespaceURI, final String localName) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void writeEmptyElement(final String prefix, final String localName, final String namespaceURI) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void writeEmptyElement(final String localName) {
        throw new UnsupportedOperationException();
    }


    @Override
    public void writeEndDocument() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void close() {
        try {
            writer.close();
        } catch (final IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    @Override
    public void flush() {
        try {
            writer.flush();
        } catch (final IOException e) {
            throw new UncheckedIOException(e);
        }
    }


    @Override
    public void writeNamespace(final String prefix, final String namespaceURI) throws XMLStreamException {
        currentState = MarshallingState.ATTRIBUTE_WRITTEN;
    }

    @Override
    public void writeDefaultNamespace(final String namespaceURI) throws XMLStreamException {
        throw new UnsupportedOperationException();
    }

    @Override
    public void writeComment(final String data) throws XMLStreamException {
        throw new UnsupportedOperationException();
    }

    @Override
    public void writeProcessingInstruction(final String target) throws XMLStreamException {
        throw new UnsupportedOperationException();
    }

    @Override
    public void writeProcessingInstruction(final String target, final String data) throws XMLStreamException {
        throw new UnsupportedOperationException();
    }

    @Override
    public void writeCData(final String data) throws XMLStreamException {
        throw new UnsupportedOperationException();
    }

    @Override
    public void writeDTD(final String dtd) throws XMLStreamException {
        throw new UnsupportedOperationException();
    }

    @Override
    public void writeEntityRef(final String name) throws XMLStreamException {
        throw new UnsupportedOperationException();
    }

    @Override
    public void writeStartDocument() throws XMLStreamException {
        throw new UnsupportedOperationException();
    }

    @Override
    public void writeStartDocument(final String version) throws XMLStreamException {
        throw new UnsupportedOperationException();
    }

    @Override
    public void writeStartDocument(final String encoding, final String version) throws XMLStreamException {
        throw new UnsupportedOperationException();
    }

    @Override
    public void writeCharacters(final char[] text, final int start, final int len) {
        throw new UnsupportedOperationException();
    }

    @Override
    public String getPrefix(final String uri) {
        return null;
    }

    @Override
    public void setPrefix(final String prefix, final String uri) {

    }

    @Override
    public void setDefaultNamespace(final String uri) {

    }

    @Override
    public NamespaceContext getNamespaceContext() {
        return new NamespaceContext() {
            @Override
            public String getNamespaceURI(final String prefix) {
                return null;
            }

            @Override
            public String getPrefix(final String namespaceURI) {
                return null;
            }

            @Override
            public Iterator getPrefixes(final String namespaceURI) {
                return null;
            }
        };
    }

    @Override
    public void setNamespaceContext(final NamespaceContext context) {

    }

    @Override
    public Object getProperty(final String name) throws IllegalArgumentException {
        return null;
    }

    private void writeLine() {
        writeToDocument(System.lineSeparator());
        lineNumber++;
    }

    private void writeToDocument(final String s) {
        try {
            writer.write(s);
            lineOffset += s.length();
        } catch (final IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    private void updateStartLocation() {
        if (activeLocationAware != null) {
            openLocationAware = activeLocationAware;

            activeLocationAware.getLocation().setStartLine(lineNumber);
            activeLocationAware.getLocation().setStartColumn(lineOffset);
            activeLocationAware = null;
        }
    }

    private void updateStartTagEndLocation() {
        if (openLocationAware != null) {
            openLocationAware.getLocation().setStartTagEndLine(lineNumber);
            openLocationAware.getLocation().setStartTagEndColumn(lineOffset);
            openLocationAware = null;
        }
    }

    private void updateEndLocation() {
        if (activeLocationAware != null) {
            activeLocationAware.getLocation().setEndLine(lineNumber);
            activeLocationAware.getLocation().setEndColumn(lineOffset);
            activeLocationAware = null;
        }
    }


    private void resetEndLines() {
        activeLocationAware.getLocation().setStartTagEndLine(-1);
        activeLocationAware.getLocation().setEndLine(-1);
    }

    private enum MarshallingState {
        START_TAG_OPEN,
        ATTRIBUTE_WRITTEN,
        TEXT_WRITTEN,
        TAG_COSED,
        HIDDEN_ELEMENT_CLOSED

    }

}
