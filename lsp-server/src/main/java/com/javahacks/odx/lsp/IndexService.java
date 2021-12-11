package com.javahacks.odx.lsp;

import com.google.common.base.Strings;
import com.javahacks.odx.index.*;
import com.javahacks.odx.lsp.dtos.Configuration;
import com.javahacks.odx.lsp.features.MessageHelper;
import com.javahacks.odx.lsp.features.ProgressMonitor;
import com.javahacks.odx.model.DOCTYPE;
import com.javahacks.odx.model.HIERARCHYELEMENT;
import com.javahacks.odx.model.PARENTREF;
import com.javahacks.odx.utils.OdxUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.inject.Provider;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.concurrent.CancellationException;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;

import static com.javahacks.odx.index.ProxyRef.create;
import static com.javahacks.odx.lsp.mapper.Types.getDocumentType;
import static com.javahacks.odx.utils.OdxUtils.getTimeStamp;
import static com.javahacks.odx.utils.OdxUtils.patchImportsAndExports;


/**
 * This service initializes and updates the global document index.
 */
public class IndexService {
    private static final Logger LOGGER = LoggerFactory.getLogger(IndexService.class);
    public static final String ODX_INDEX_FILE_NAME = ".odc-index";
    private static final String EMPTY_SHORT_NAME = "EMPTY_SHORT_NAME";
    private final Provider<OdxLanguageClient> languageClientProvider;
    private final AtomicBoolean cancelFlag = new AtomicBoolean(false);
    private final DocumentIndex documentIndex;
    private ProxyIndex proxyIndex;

    @Inject
    public IndexService(final Provider<OdxLanguageClient> languageClientProvider, final DocumentIndex index) {
        this.languageClientProvider = languageClientProvider;
        this.documentIndex = index;
    }

    /**
     * If there is a running indexing job it will be canceled asap
     */
    public void cancelRunningJob() {
        cancelFlag.set(true);
    }

    /**
     * Evaluates if the given change set affects the current index.
     *
     * @param changedFiles The uris of all files that have changed
     * @return <code>true</code> if any of the given uris invalidates the current index
     */
    public boolean indexInvalid(final List<URI> changedFiles) {
        return changedFiles.stream()
                .anyMatch(documentIndex::partOfIndex);
    }

    /**
     * Update proxy index and notify LSP client about updated index
     */
    public void reindexDocuments() {
        cancelFlag.set(false);
        if (proxyIndex != null) {
            updateAndWriteProxyIndex();
            languageClientProvider.get().indexChanged();
        }
    }

    /**
     * Builds the {@link ProxyIndex} for given configuration
     */
    public void updateProxyIndex(final Configuration configuration) {
        cancelFlag.set(false);

        if (Strings.isNullOrEmpty(configuration.getActiveIndexLocation())) {
            return;
        }

        final Path indexLocation = Paths.get(configuration.getActiveIndexLocation());
        if (Files.exists(indexLocation)) {
            processIndexLocation(indexLocation);
        }
    }

    private void processIndexLocation(final Path indexLocation) {
        final boolean isPdx = indexLocation.toString().toLowerCase().endsWith(".pdx");
        final String fileName = isPdx ? indexLocation.getFileName() + ODX_INDEX_FILE_NAME : ODX_INDEX_FILE_NAME;

        final Path odxSourceFolder = Files.isDirectory(indexLocation) ? indexLocation : indexLocation.getParent();
        final Path indexPath = odxSourceFolder.resolve(Paths.get(fileName));

        proxyIndex = restoreOrCreateIndex(indexPath, isPdx);
        proxyIndex.setIndexPath(indexPath);

        final URI odxSourceUri = isPdx ? URI.create("jar:" + indexLocation.toUri() + "!/") : odxSourceFolder.toUri();

        try {
            OdxUtils.initializeFileSystem(odxSourceUri);
            proxyIndex.setOdxSourcePath(Paths.get(odxSourceUri));
        } catch (final IOException e) {
            throw new UncheckedIOException(e);
        }

        updateAndWriteProxyIndex();
    }

    private void updateAndWriteProxyIndex() {

        final ProgressMonitor monitor = new ProgressMonitor(languageClientProvider.get());
        try {
            documentIndex.resetIndex(proxyIndex);
            if (updateProxyIndex(resolveFilesInIndex(), monitor)) {
                saveProxyIndex();
            }
        } catch (final CancellationException e) {
            //operation was canceled
        } catch (final OutOfMemoryError error) {
            MessageHelper.showError(languageClientProvider.get(), "Indexing process run out of memory. Increase heap space size in ODX server configuration!");
            LOGGER.error("An out of memory error occurred while indexing path: {} ", proxyIndex.getIndexPath());
            System.gc();
        } finally {
            monitor.done();
            cleanupIndex();
        }
    }

    private boolean updateProxyIndex(final List<Path> files, final ProgressMonitor monitor) {
        monitor.begin("Indexing ODX Files ", files.size());

        final List<CategoryProxy> proxies = files.stream()
                .parallel()
                .map(path -> getOrCreateProxy(monitor, path))
                .collect(Collectors.toList());

        final boolean indexHasChanged = proxies.stream()
                .anyMatch(p -> !proxyIndex.getCategories().contains(p))
                || proxies.size() != proxyIndex.getCategories().size();
        proxyIndex.setCategories(proxies);

        updateDependencies(proxies);

        return indexHasChanged;
    }

    private void updateDependencies(final List<CategoryProxy> proxies) {
        final List<LayerProxy> layerProxies = proxies.stream().flatMap(c -> c.getLayers().stream()).collect(Collectors.toList());
        layerProxies.forEach(p -> p.getChildren().clear());
        layerProxies.forEach(layerProxy -> {
            for (final ProxyRef parentRef : layerProxy.getParents()) {
                documentIndex.findLayerProxyForOdxLink(parentRef.toOdxLink()).ifPresent(parentProxy -> {
                    parentProxy.getChildren().add(create(layerProxy.getDocType(), layerProxy.getShortName(), layerProxy.getId()));
                });
            }
        });
    }

    private CategoryProxy getOrCreateProxy(final ProgressMonitor monitor, final Path path) {
        checkCancelFlag();
        CategoryProxy existingProxy = documentIndex.getCategoryProxyForUri(path.toUri()).orElse(null);
        if (existingProxy == null || proxyExpired(path, existingProxy)) {
            existingProxy = createNewProxy(path, proxyIndex.isPacked());
        }
        monitor.progressOneStep();
        return existingProxy;
    }

    private boolean proxyExpired(final Path path, final CategoryProxy existingProxy) {
        return existingProxy.getLastModification() != getTimeStamp(path);
    }

    private CategoryProxy createNewProxy(final Path path, final boolean packed) {
        final CategoryProxy proxy = new CategoryProxy(path.toUri(), getTimeStamp(path));
        documentIndex.loadCategoryWithFallback(path.toUri(), packed).ifPresent(category -> {
            patchImportsAndExports(category);
            updateCategoryProxy(proxy, category);
        });
        return proxy;
    }

    private void updateCategoryProxy(final CategoryProxy proxy, final Category category) {

        proxy.setShortName(category.getSHORTNAME() != null ? category.getSHORTNAME() : EMPTY_SHORT_NAME);
        proxy.setLocation(category.getLocation());
        proxy.setDocType(category.getDocType());
        proxy.setId(category.getID());
        proxy.setExpandable(category.isExpandable());
        category.getLayers().stream()
                .map(this::createLayerProxy)
                .collect(Collectors.toCollection(() -> proxy.getLayers()));
    }


    private LayerProxy createLayerProxy(final Layer layer) {
        final LayerProxy description = new LayerProxy();
        description.setLocation(layer.getLocation());
        description.setShortName(layer.getSHORTNAME() != null ? layer.getSHORTNAME() : EMPTY_SHORT_NAME);
        description.setLayerType(getDocumentType(layer));
        description.setDocType(DOCTYPE.LAYER.name());
        description.setId(layer.getID());
        description.setExpandable(layer.isExpandable());

        if (layer instanceof HIERARCHYELEMENT && ((HIERARCHYELEMENT) layer).getParentRefs() != null) {
            for (final PARENTREF parentRef : ((HIERARCHYELEMENT) layer).getParentRefs()) {
                description.getParents().add(create(parentRef.getDOCTYPE().name(), parentRef.getDOCREF(), parentRef.getIDREF()));
            }
        }

        return description;
    }


    private void saveProxyIndex() {
        LOGGER.info("Index has changed! Saving new index to file: {} ", proxyIndex.getIndexPath());
        try {
            JaxbUtil.saveProxyIndex(proxyIndex, proxyIndex.getIndexPath());
        } catch (final Exception e) {
            LOGGER.error("Could not save proxy index", e);
        }
    }

    /**
     * Remove invalid files
     */
    private void cleanupIndex() {
        proxyIndex.getCategories().removeIf(c -> c.getDocType() == null);
    }

    private List<Path> resolveFilesInIndex() {
        try {
            return Files.walk(proxyIndex.getOdxSourcePath())
                    .filter(p -> validFile(p.toString().toLowerCase()))
                    .collect(Collectors.toList());
        } catch (final IOException e) {
            throw new UncheckedIOException(e);
        }
    }


    private boolean validFile(final String fileNameLc) {
        checkCancelFlag();
        if (fileNameLc.endsWith(ODX_INDEX_FILE_NAME)) {
            return false;
        }
        return fileNameLc.matches(".*\\.odx(-.+)?$");
    }

    private void checkCancelFlag() {
        if (cancelFlag.get()) {
            throw new CancellationException();
        }
    }

    private ProxyIndex restoreOrCreateIndex(final Path indexPath, final boolean packed) {
        if (Files.exists(indexPath)) {
            try {
                final ProxyIndex proxyIndex = JaxbUtil.loadProxyIndex(indexPath);
                if (proxyIndex.getIndexVersion() == ProxyIndex.INDEX_VERSION && proxyIndex.isPacked() == packed) {
                    return proxyIndex;
                }
            } catch (final Exception e) {
                LOGGER.info("Could not restore proxy index {}", indexPath);
            }
        }
        return new ProxyIndex(packed);
    }
}
