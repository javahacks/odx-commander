package com.javahacks.odx.lsp.features;

import com.google.common.collect.Streams;
import com.javahacks.odx.index.AbstractLinkTarget;
import com.javahacks.odx.index.LocationAware;
import com.javahacks.odx.index.XmlElementLocation;
import com.javahacks.odx.lsp.VirtualDocument;
import com.javahacks.odx.model.*;
import com.javahacks.odx.utils.OdxUtils;
import org.eclipse.lsp4j.DocumentLink;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.javahacks.odx.utils.OdxUtils.*;

/**
 * Provides document links for flash data files
 */
public class DocumentLinkProvider {
    private static final Logger LOGGER = LoggerFactory.getLogger(DocumentLinkProvider.class);

    public List<DocumentLink> getDocumentLinks(final VirtualDocument document) {
        if (document.getVirtualModel() == null) {
            return Collections.emptyList();
        }

        final Stream<DocumentLink> odxLinksStream = getOdxLinksStream(document);
        final Stream<DocumentLink> snRefStream = getSNRefStream(document);
        final Stream<DocumentLink> dataFileStream = getDataFileStream(document);

        return Streams.concat(odxLinksStream, snRefStream, dataFileStream)
                .collect(Collectors.toList());
    }

    private Stream<DocumentLink> getOdxLinksStream(final VirtualDocument document) {
        return document.getVirtualModel().getLocationAwares().stream()
                .filter(ODXLINK.class::isInstance)
                .map(ODXLINK.class::cast)
                .filter(this::isVisibleLink)
                .map(this::mapODXLink)
                .filter(Objects::nonNull);
    }

    private boolean isVisibleLink(final ODXLINK link) {
        return link.getLocation().getEndLine() > -1; // links removed in read only view
    }

    private DocumentLink mapODXLink(final ODXLINK link) {
        return resolveODXLink(link, AbstractLinkTarget.class)
                .map(target -> createDocumentLink(link.getLocation(), target.getLocation()))
                .orElse(null);
    }

    private Stream<DocumentLink> getSNRefStream(final VirtualDocument document) {
        return document.getVirtualModel().getLocationAwares().stream()
                .filter(SNREF.class::isInstance)
                .map(SNREF.class::cast)
                .filter(this::isValidReference)
                .map(this::mapSNRef)
                .filter(Objects::nonNull);
    }

    private boolean isValidReference(final SNREF snref) {
        return snref.getLocation().getEndLine() > -1 && snref.getDocument() instanceof DIAGLAYER;
    }

    private DocumentLink mapSNRef(final SNREF snRef) {
        return resolveSnRef((DIAGLAYER) snRef.getDocument(), snRef).map(target -> createDocumentLink(snRef
                .getLocation(), target.getLocation()))
                .orElse(null);
    }

    private DocumentLink createDocumentLink(final XmlElementLocation sourceLocation,
            final XmlElementLocation targetLocation) {
        return new DocumentLink(locationToSingleLineRange(sourceLocation), createGotoLocationCommand(targetLocation),
                null, "Goto Definition");
    }

    private Stream<DocumentLink> getDataFileStream(final VirtualDocument document) {
        if (!(document.getVirtualModel() instanceof FLASH)) {
            return Stream.empty();
        }
        final Path documentParentPath = Paths.get(document.getUri()).getParent();
        return document.getVirtualModel().getLocationAwares().stream()
                .filter(DATAFILE.class::isInstance)
                .map(DATAFILE.class::cast)
                .filter(DATAFILE::isLATEBOUNDDATAFILE)
                .map(dataFile -> createFlashDataLink(dataFile, documentParentPath));
    }

    private DocumentLink createFlashDataLink(final DATAFILE dataFile, final Path documentParentPath) {
        final Path absolutePath = createAbsolutePath(documentParentPath, dataFile.getValue());
        return createLink(dataFile, absolutePath.toUri().toString(), null, absolutePath.toString());
    }

    private Path createAbsolutePath(final Path documentParentPath, final String path) {
        final Path linkPath = Paths.get(path);
        if (linkPath.isAbsolute()) {
            LOGGER.debug("Path {} is absolute. Use it as is.", path);
            return linkPath;
        }
        final Path absolutePath = documentParentPath.resolve(path).normalize();
        LOGGER.debug("Using '{}' for relative path '{}' and document parent {}", absolutePath, path,
                documentParentPath);
        return absolutePath;
    }

    private DocumentLink createLink(final LocationAware locationAware, final String value, final Integer data,
            final String tooltip) {
        return new DocumentLink(locationToRange(locationAware.getLocation()), value, data, tooltip);
    }

}
