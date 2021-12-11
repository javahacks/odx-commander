package com.javahacks.odx.index;

import com.google.common.io.ByteStreams;
import com.javahacks.odx.model.ODX;
import org.eclipse.persistence.jaxb.JAXBContextFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.xml.bind.*;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import java.io.*;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.Collections;
import java.util.Optional;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

import static javax.xml.bind.Marshaller.JAXB_FORMATTED_OUTPUT;


public class JaxbUtil {

    private static final Logger LOGGER = LoggerFactory.getLogger(JaxbUtil.class.getName());
    private static final XMLInputFactory xif = XMLInputFactory.newInstance();
    private static final JAXBContext ODX_CONTEXT;
    private static final JAXBContext INDEX_CONTEXT;

    static {
        try {
            ODX_CONTEXT = JAXBContextFactory.createContext(new Class[]{ODX.class}, Collections.emptyMap());
            INDEX_CONTEXT = JAXBContextFactory.createContext(new Class[]{ProxyIndex.class}, Collections.emptyMap());
        } catch (final Exception e) {
            throw new IllegalStateException("Could not initialize JAXB context", e);
        }
    }

    public static void saveProxyIndex(final ProxyIndex index, final Path indexPath) throws IOException, JAXBException {
        try (final OutputStream os = Files.newOutputStream(indexPath, StandardOpenOption.TRUNCATE_EXISTING, StandardOpenOption.CREATE);
             final GZIPOutputStream zipOutputStream = new GZIPOutputStream(os)) {
            marshalProxyIndex(index, zipOutputStream);
        }
    }

    public static void marshalProxyIndex(final ProxyIndex index, final OutputStream out) throws JAXBException {
        final Marshaller marshaller = INDEX_CONTEXT.createMarshaller();
        marshaller.setProperty(JAXB_FORMATTED_OUTPUT, true);
        marshaller.setProperty(Marshaller.JAXB_ENCODING, StandardCharsets.UTF_8.name());
        marshaller.marshal(index, out);
    }

    public static ProxyIndex loadProxyIndex(final Path indexPath) throws IOException, JAXBException {
        try (final InputStream is = Files.newInputStream(indexPath, StandardOpenOption.READ);
             final GZIPInputStream zipInputStream = new GZIPInputStream(is)) {
            return unmarshalProxyIndex(zipInputStream);
        }
    }

    public static ProxyIndex unmarshalProxyIndex(final InputStream in) throws JAXBException {
        return (ProxyIndex) INDEX_CONTEXT.createUnmarshaller().unmarshal(in);
    }

    /**
     * Saves given {@link ODX} model in normal mode
     */
    public static void marshalModel(final ODX model, final Writer writer) {
        try {
            final Marshaller marshaller = ODX_CONTEXT.createMarshaller();
            marshaller.setProperty(JAXB_FORMATTED_OUTPUT, true);
            marshaller.setProperty(Marshaller.JAXB_ENCODING, StandardCharsets.UTF_8.name());
            marshaller.marshal(model, writer);
        } catch (final Exception e) {
            throw new IllegalArgumentException("Could not save category", e);
        }
    }

    /**
     * Formats and reduces given {@link ODX} model and adjusts all {@link LocationAware} values.
     * The XML output can no longer be parsed again!
     */
    public static void marshalModelFormattedAndSimplified(final ODX model, final Writer writer) {
        try {
            final Marshaller marshaller = ODX_CONTEXT.createMarshaller();
            new LocationAwareXMLStreamWriter(marshaller, writer).formatModel(model);
        } catch (final Exception e) {
            throw new IllegalArgumentException("Could not save category", e);
        }
    }

    public static Category unmarshalCategoryContent(final String content, final URI filePath) {
        try {
            final ODX odx = unmarshallDocument(xif.createXMLStreamReader(new StringReader(content)), filePath);
            return extractContainer(odx);
        } catch (final XMLStreamException e) {
            throw new IllegalStateException("Could not create streamreader for file path", e);
        }
    }

    public static Optional<Category> unmarshalCategory(final Path path, final boolean formatAndSimplify) {
        try {
            final byte[] content = getFileContentsSynchronized(path);
            final XMLStreamReader xmlStreamReader = xif.createXMLStreamReader(new ByteArrayInputStream(content));
            final ODX odx = unmarshallDocument(xmlStreamReader, path.toUri());
            if (odx != null) {

                final StringWriter writer = new StringWriter();
                if (formatAndSimplify) {
                    marshalModelFormattedAndSimplified(odx, writer);
                }

                return Optional.ofNullable(extractContainer(odx));
            }

        } catch (final Exception e) {
            LOGGER.info("Could not parse file: {}", path);
        }

        return Optional.empty();
    }


    private static synchronized byte[] getFileContentsSynchronized(final Path path) throws IOException {
        try (final InputStream is = new BufferedInputStream(Files.newInputStream(path, StandardOpenOption.READ))) {
            return ByteStreams.toByteArray(is);
        }
    }

    private static ODX unmarshallDocument(final XMLStreamReader xmlStreamReader, final URI filePath) {
        final ReaderDelegate xsr = new ReaderDelegate(xmlStreamReader);
        try {
            final Unmarshaller unmarshaller = ODX_CONTEXT.createUnmarshaller();
            unmarshaller.setListener(new ODXStructureAwareListener(xsr, filePath));
            final JAXBElement jaxbElement = unmarshaller.unmarshal(xsr, ODX.class);
            return (ODX) jaxbElement.getValue();

        } catch (final Exception ex) {
            throw new LocationAwareParsingException(ex, xsr.getLastLocation(), xsr.getCurrentLocation());
        }
    }

    private static Category extractContainer(final ODX odx) {
        if (odx.getDIAGLAYERCONTAINER() != null) {
            return odx.getDIAGLAYERCONTAINER();
        }
        if (odx.getCOMPARAMSPEC() != null) {
            return odx.getCOMPARAMSPEC();
        }
        if (odx.getCOMPARAMSUBSET() != null) {
            return odx.getCOMPARAMSUBSET();
        }
        if (odx.getECUCONFIG() != null) {
            return odx.getECUCONFIG();
        }
        if (odx.getVEHICLEINFOSPEC() != null) {
            return odx.getVEHICLEINFOSPEC();
        }
        if (odx.getFLASH() != null) {
            return odx.getFLASH();
        }
        if (odx.getMULTIPLEECUJOBSPEC() != null) {
            return odx.getMULTIPLEECUJOBSPEC();
        }
        if (odx.getFUNCTIONDICTIONARY() != null) {
            return odx.getFUNCTIONDICTIONARY();
        }

        return null;
    }

}
