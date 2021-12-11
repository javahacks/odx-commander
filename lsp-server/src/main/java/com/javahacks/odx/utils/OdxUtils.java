package com.javahacks.odx.utils;

import com.javahacks.odx.index.*;
import com.javahacks.odx.model.*;
import org.eclipse.lsp4j.Position;
import org.eclipse.lsp4j.Range;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collections;
import java.util.Objects;
import java.util.Optional;

public class OdxUtils {
    private static final Logger LOGGER = LoggerFactory.getLogger(OdxUtils.class);

    public static void initializeFileSystem(final URI pdx) throws IOException {
        if ("file".equals(pdx.getScheme())) {
            return;
        }
        try {
            FileSystems.getFileSystem(pdx);
        } catch (final Exception ex) {
            FileSystems.newFileSystem(pdx, Collections.emptyMap());
        }
    }


    public static URI parseUri(final String uri) {
        try {
            final String decodedUri = URLDecoder.decode(removeReadOnlyProtocol(uri), StandardCharsets.UTF_8.name());
            return URI.create(decodedUri.replaceAll(" ", "%20"));
        } catch (final UnsupportedEncodingException e) {
            throw new IllegalStateException("Default charset not available", e);
        }
    }

    private static String removeReadOnlyProtocol(final String uri) {
        return uri.replaceFirst("^odx\\:", "");
    }

    public static String prependReadOnlyProtocol(final String uri) {
        if (uri.startsWith("jar:")) {
            return "odx:" + uri;
        }
        return uri;
    }


    public static long getTimeStamp(final Path path) {
        try {
            return Files.getLastModifiedTime(path).toMillis();
        } catch (final IOException e) {
            LOGGER.error("Could not get timestamp for file {} ", path);
        }
        return -1;
    }

    /**
     * Convert text that represents a integer value as HEX string.
     *
     * @param text         The text that represents the numeric value
     * @param defaultValue A default value if text can not be formatted
     * @return The numeric text as hex string
     */
    public static String convertTextToHexStringOrDefault(final String text, final String defaultValue) {
        try {
            return formatHexString(Long.toHexString(Long.parseLong(text)));
        } catch (final Exception e) {
            return defaultValue;
        }
    }

    /**
     * Formats and pads given string that represents a HEX string
     *
     * @param hexString The string to format
     * @return The padded and formatted string
     */
    public static String formatHexString(final String hexString) {
        return hexString.length() % 2 == 0 ? hexString.toUpperCase() : "0" + hexString.toUpperCase();
    }

    /**
     * Converts an instance of type {@link XmlElementLocation} into an instance of type {@link Range}
     *
     * @param location The location to convert
     * @return The resulting {@link Range} instance
     */
    public static Range locationToRange(final XmlElementLocation location) {
        final Position start = new Position(location.getStartLine(), location.getStartColumn());
        final Position end = new Position(location.getEndLine(), location.getEndColumn());
        return new Range(start, end);
    }

    /**
     * Converts an instance of type {@link XmlElementLocation} into an instance of type {@link Range} that only spans the start tag range.
     *
     * @param location The location to convert
     * @return The resulting {@link Range} instance
     */
    public static Range locationToSingleLineRange(final XmlElementLocation location) {
        final Position start = new Position(location.getStartLine(), location.getStartColumn());
        final Position end = new Position(location.getStartTagEndLine(), location.getStartTagEndColumn());
        return new Range(start, end);
    }


    /**
     * Checks if the given {@link LocationAware} start tag spans line and column position.
     *
     * @param locationAware The xml element to check the location for
     * @param line          The line position to check
     * @param column        The column position to check
     * @return Returns <code>true</code> if element is at line and column position
     */
    public static boolean isOverStartTag(final LocationAware locationAware, final int line, final int column) {
        Objects.requireNonNull(locationAware.getLocation());
        final XmlElementLocation location = locationAware.getLocation();
        if (!(location.getStartLine() <= line && line <= location.getStartTagEndLine())) {
            return false; //outside start and end tag
        }

        if (line == location.getStartLine()) {
            return location.getStartColumn() <= column;
        }
        if (line == location.getStartTagEndLine()) {
            return column <= location.getStartTagEndColumn();
        }

        return true;
    }

    /**
     * Checks if the given {@link LocationAware} start and end tags span line and column position.
     *
     * @param location The location that spans the range to check
     * @param line     The line position to check
     * @param column   The column position to check
     * @return Returns <code>true</code> if element spans line and column position
     */
    public static boolean isBetweenStartAndEndTag(final XmlElementLocation location, final int line, final int column) {
        if (!(location.getStartLine() <= line && line <= location.getEndLine())) {
            return false; //outside start and end tag
        }

        if (line == location.getStartLine() && line == location.getEndLine()) {
            return location.getStartColumn() <= column && column <= location.getEndColumn();
        }

        if (line == location.getStartLine()) {
            return location.getStartColumn() <= column;
        }
        if (line == location.getEndLine()) {
            return column <= location.getEndColumn();
        }

        return true;

    }

    /**
     * Creates a goto location command that is executed in VS Code
     */
    public static String createGotoLocationCommand(final XmlElementLocation location) {
        try {
            final String command = "[{\"fileUri\":\"" + location.getFileUri().toString() + "\",\"startLine\":" + location.getStartLine() + ",\"startColumn\":" + location.getStartColumn() + "},false]";
            return "command:odx.jumpToLine?" + URLEncoder.encode(command, StandardCharsets.UTF_8.name());
        } catch (final UnsupportedEncodingException e) {
            throw new IllegalArgumentException("Encoding does not exist", e);
        }
    }

    /**
     * Convenient method to resolve ODX links
     *
     * @param odxlink The ODX link to resolve
     * @param clazz   The expected type of link target
     * @return Returns an optional that contains the link target if available
     */
    public static <T> Optional<T> resolveODXLink(final ODXLINK odxlink, final Class<T> clazz) {
        return odxlink.getDocument().getIndex().resolveLink(odxlink, clazz);
    }

    /**
     * Convenient method to resolve shortname refs
     *
     * @param diaglayer The diaglayer from which the search is started
     * @param snRef     The shortname ref
     * @return Returns an optional that contains the link target if available
     */
    public static Optional<? extends AbstractLinkTarget> resolveSnRef(final DIAGLAYER diaglayer, final SNREF snRef) {
        if ("DOP-SNREF".equals(snRef.getTagName())) {
            return diaglayer.query().dopByShortName(snRef.getSHORTNAME());
        }
        if ("DIAG-COMM-SNREF".equals(snRef.getTagName())) {
            return diaglayer.query().diagCommByShortName(snRef.getSHORTNAME(), true);
        }
        return Optional.empty();
    }

    /**
     * Ensures that all import and parent ref links are in absolute form (doctype, shortname and idRef)
     *
     * @param category The category for which all layer refs should be patched
     */
    public static void patchImportsAndExports(final Category category) {
        for (final Layer layer : category.getLayers()) {
            if (layer instanceof DIAGLAYER) {
                ((DIAGLAYER) layer).getImportRefs().forEach(ref -> patchOdxLink(layer, ref));
            }
            if (layer instanceof HIERARCHYELEMENT) {
                ((HIERARCHYELEMENT) layer).getParentRefs().forEach(ref -> patchOdxLink(layer, ref));
            }
        }
    }

    private static void patchOdxLink(final Layer container, final ODXLINK odxlink) {
        if (odxlink.getDOCREF() == null) {
            odxlink.setDOCREF(container.getCategory().getSHORTNAME());//we assume that link target is enclosing container
            odxlink.setDOCTYPE(DOCTYPE.CONTAINER);
        }
    }
}
