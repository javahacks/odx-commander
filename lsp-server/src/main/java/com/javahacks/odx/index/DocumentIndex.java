package com.javahacks.odx.index;

import com.google.common.base.Strings;
import com.javahacks.odx.lsp.VirtualDocument;
import com.javahacks.odx.model.*;
import com.javahacks.odx.utils.OdxUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.ref.SoftReference;
import java.net.URI;
import java.nio.file.Paths;
import java.util.*;

import static com.javahacks.odx.index.JaxbUtil.unmarshalCategory;
import static com.javahacks.odx.utils.OdxUtils.patchImportsAndExports;

/**
 * Global index for all (virtual) ODX documents and proxies
 */
public class DocumentIndex {
    private static final Logger LOGGER = LoggerFactory.getLogger(DocumentIndex.class);
    private final Map<URI, VirtualDocument> virtualDocumentMap = new HashMap<>();
    private ProxyIndex proxyIndex = new ProxyIndex();


    /**
     * Returns a list of  all indexed categories
     *
     * @return A list of indexed categories
     */
    public List<CategoryProxy> getIndexedCategories() {
        return proxyIndex.getCategories();
    }


    /**
     * Checks if a file with given uri is part of the active document index.
     *
     * @return <code>true</code> When uri is part of index
     */
    public boolean partOfIndex(final URI uri) {
        return proxyIndex.getOdxSourcePath() != null && Paths.get(uri).startsWith(proxyIndex.getOdxSourcePath());
    }

    /**
     * Set new proxy index and reset all cached ODX categories and virtual layer queries
     *
     * @param index The new proxy index for this document index
     */
    public void resetIndex(final ProxyIndex index) {
        this.proxyIndex = index;
        this.proxyIndex.getCategories().forEach(categoryProxy -> categoryProxy.setProxyTarget(null));
        for (final VirtualDocument document : virtualDocumentMap.values()) {
            if (document.getVirtualModel() != null) {
                document.getVirtualModel().getLayers().forEach(layer -> layer.query().reset());
            }
        }
    }

    public Optional<Category> getCategoryByUri(final URI uri) {
        for (final CategoryProxy categoryProxy : proxyIndex.getCategories()) {
            if (uri.equals(categoryProxy.getIndexPath())) {
                return findObjectInDocument(categoryProxy.getDocType(), categoryProxy.getShortName(), categoryProxy.getId(), Category.class);
            }
        }
        return Optional.empty();
    }

    /**
     * Searches for a {@link CategoryProxy} that matches the given {@link URI} in the global proxy pool.
     *
     * @param uri The {@link URI} that represents the {@link ODXCATEGORY} in the index.
     * @return An optional that contains the appropriate category proxy if present
     */
    public Optional<CategoryProxy> getCategoryProxyForUri(final URI uri) {
        return proxyIndex.getCategories().stream()
                .filter(proxy -> uri.equals(proxy.getIndexPath()))
                .findFirst();
    }

    /**
     * Searches for a {@link LayerProxy} that matches the given {@link ODXLINK} in the global proxy pool.
     *
     * @param link The {@link ODXLINK} that represents the {@link DIAGLAYER} in the index.
     * @return An optional that contains the appropriate layer if present
     */
    public Optional<LayerProxy> findLayerProxyForOdxLink(final ODXLINK link) {
        return proxyIndex.getCategories().stream()
                .flatMap(categoryProxy -> categoryProxy.getLayers().stream())
                .filter(layerProxy -> layerProxy.matchesLink(link))
                .findFirst();
    }

    /**
     * Searches for a {@link CategoryProxy} that matches the given {@link ODXLINK} in the global proxy pool.
     *
     * @param link The {@link ODXLINK} that represents the {@link ODXCATEGORY} in the index.
     * @return An optional that contains the appropriate category if present
     */
    public Optional<CategoryProxy> findCategoryProxyForOdxLink(final ODXLINK link) {
        return proxyIndex.getCategories().stream()
                .filter(categoryProxy -> categoryProxy.matchesLink(link))
                .findFirst();
    }

    /**
     * Returns link target fully identified by the link
     *
     * @param odxlink
     * @param clazz   The object type
     */
    public <T> Optional<T> resolveLink(final ODXLINK odxlink, final Class<T> clazz) {
        if (odxlink.getDOCTYPE() != null && !Strings.isNullOrEmpty(odxlink.getDOCREF())) {
            return findObjectInDocument(odxlink.getDOCTYPE().name(), odxlink.getDOCREF(), odxlink.getIDREF(), clazz);
        }
        if (odxlink.getDocument() instanceof Layer) {
            final Layer layer = (Layer) odxlink.getDocument();
            return Optional.ofNullable(clazz.cast(layer.getElementById(odxlink.getIDREF())));
        }
        if (odxlink.getDocument() instanceof Category) {
            final Category container = (Category) odxlink.getDocument();
            return Optional.ofNullable(clazz.cast(container.getElementById(odxlink.getIDREF())));
        }
        return Optional.empty();
    }

    /**
     * Returns a single odx target fully identified by all odx link parts.
     *
     * @param docType The  document's type
     * @param docRef  The short name of the container
     * @param idRef   The id of the object to find
     * @param clazz   The object type
     */
    public <T> Optional<T> findObjectInDocument(final String docType, final String docRef, final String idRef, final Class<T> clazz) {

        Objects.requireNonNull(docType, "docType must not be null");
        Objects.requireNonNull(docRef, "docRef must not be null");
        Objects.requireNonNull(idRef, "idRef must not be null");

        final IndexableDocument document = resolveProxyDocument(docType, docRef);
        if (document == null) {
            LOGGER.trace("Document for docType {} and docRef {} does not exist", docType, docType);
            return Optional.empty();
        }
        return Optional.ofNullable(clazz.cast(document.getElementById(idRef)));
    }

    /**
     * Returns the un-proxied {@link IndexableDocument} for given doctype and docRef combination
     *
     * @param doctype The document type of the indexed document
     * @param docRef  The short name of the indexed document
     * @return The document or else <code>null</code>
     */
    public IndexableDocument resolveProxyDocument(final String doctype, final String docRef) {
        for (final CategoryProxy category : proxyIndex.getCategories()) {
            if (category.matchesDocument(doctype, docRef)) {
                return getOrLoadCategory(category);
            }

            for (final LayerProxy layerProxy : category.getLayers()) {
                if (layerProxy.matchesDocument(doctype, docRef)) {
                    return getLayerByShortName(category, docRef);
                }
            }
        }
        return null;
    }

    public void addVirtualDocument(final VirtualDocument document) {
        virtualDocumentMap.put(document.getUri(), document);
    }

    public VirtualDocument removeVirtualDocument(final URI uri) {
        return virtualDocumentMap.remove(uri);
    }

    public Optional<VirtualDocument> getVirtualDocument(final URI uri) {
        return Optional.ofNullable(virtualDocumentMap.get(uri));
    }

    public void updateVirtualDocument(final VirtualDocument document) throws LocationAwareParsingException {
        final Category category = JaxbUtil.unmarshalCategoryContent(document.getTextContent(), document.getUri());
        prepareCategory(category);
        document.setVirtualModel(category);
    }

    /**
     * Loads the {@link ODXCATEGORY} located at given uri, however if there is a virtual document with same uri that is erroneous
     * the method returns the stored virtual category.
     */
    public Optional<Category> loadCategoryWithFallback(final URI uri, final boolean packed) {
        final Optional<VirtualDocument> virtualDocument = getVirtualDocument(uri);
        if (virtualDocument.isPresent() && virtualDocument.get().isErroneous()) {
            return Optional.ofNullable(virtualDocument.get().getVirtualModel());
        }
        return unmarshalCategory(Paths.get(uri), packed);

    }

    private void prepareCategory(final Category category) {
        patchImportsAndExports(category);
        category.setIndex(this);
        for (final Layer layer : category.getLayers()) {
            layer.setIndex(this);
            initImportRefs(layer);
            initParentRefs(layer);
        }
    }

    private void initImportRefs(final Layer layer) {
        for (final ODXLINK importRef : ((DIAGLAYER) layer).getImportRefs()) {
            resolveLink(importRef, Layer.class).ifPresent(parentLayer -> {
                layer.getImports().add(parentLayer);
            });
        }
    }

    private void initParentRefs(final Layer layer) {
        if (layer instanceof HIERARCHYELEMENT) {
            for (final PARENTREF parentref : ((HIERARCHYELEMENT) layer).getParentRefs()) {
                resolveLink(parentref, Layer.class).ifPresent(parentLayer -> {
                    layer.getParents().add(parentLayer);
                });
            }
        }
    }





    private Layer getLayerByShortName(final CategoryProxy category, final String docRef) {
        final Category resolvedProxy = getOrLoadCategory(category);
        if (resolvedProxy == null) {
            return null;
        }
        return resolvedProxy.getLayers().stream()
                .filter(layer -> docRef.equals(layer.getSHORTNAME()))
                .findFirst()
                .orElse(null);
    }

    private Category getOrLoadCategory(final CategoryProxy proxy) {
        if (proxy.getProxyTarget() != null && proxy.getProxyTarget().get() != null) {
            return proxy.getProxyTarget().get();
        }

        return loadCategory(proxy);
    }

    private Category loadCategory(final CategoryProxy proxy) {
        final Optional<Category> optional = loadCategoryWithFallback(proxy.getIndexPath(), proxyIndex.isPacked());
        optional.ifPresent(category -> {
            proxy.setProxyTarget(new SoftReference<>(category));
            prepareCategory(category);
        });
        return optional.orElse(null);
    }

}
