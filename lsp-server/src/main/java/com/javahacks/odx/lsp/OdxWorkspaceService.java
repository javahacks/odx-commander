package com.javahacks.odx.lsp;

import com.google.gson.JsonObject;
import com.javahacks.odx.lsp.dtos.Configuration;
import com.javahacks.odx.lsp.features.MessageHelper;
import com.javahacks.odx.utils.OdxUtils;
import org.eclipse.lsp4j.ConfigurationItem;
import org.eclipse.lsp4j.ConfigurationParams;
import org.eclipse.lsp4j.DidChangeConfigurationParams;
import org.eclipse.lsp4j.DidChangeWatchedFilesParams;
import org.eclipse.lsp4j.services.WorkspaceService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.inject.Provider;
import java.net.URI;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * Implementation for all ODX workspace services.
 */
public class OdxWorkspaceService implements WorkspaceService {
    private static final Logger LOGGER = LoggerFactory.getLogger(OdxWorkspaceService.class);
    private final ExecutorService executorService;
    private final IndexService indexService;
    private final Provider<OdxLanguageClient> languageClientProvider;

    @Inject
    public OdxWorkspaceService(final ExecutorService executorService, final IndexService indexService, final Provider<OdxLanguageClient> languageClientProvider) {
        this.executorService = executorService;
        this.indexService = indexService;
        this.languageClientProvider = languageClientProvider;
    }

    @Override
    public void didChangeConfiguration(final DidChangeConfigurationParams params) {
        indexService.cancelRunningJob();
        executorService.execute(() -> indexService.updateProxyIndex(readConfiguration()));
    }

    @Override
    public void didChangeWatchedFiles(final DidChangeWatchedFilesParams params) {
        final List<URI> changedFiles = params.getChanges().stream()
                .map(change -> OdxUtils.parseUri(change.getUri()))
                .collect(Collectors.toList());

        if (indexService.indexInvalid(changedFiles)) {
            LOGGER.trace("Files that belong to the current index have changed on the file system");
            indexService.cancelRunningJob();
            executorService.execute(() -> indexService.reindexDocuments());
        }
    }

    public void onClientInitialized() {
        executorService.execute(() -> indexService.updateProxyIndex(readConfiguration()));
    }

    public boolean shutDown() {
        executorService.shutdownNow();
        return true;
    }

    private Configuration readConfiguration() {
        final ConfigurationParams configurationParams = new ConfigurationParams();
        final ConfigurationItem item = new ConfigurationItem();
        item.setSection("odx-server");
        configurationParams.setItems(Collections.singletonList(item));

        final List<Object> objects = readConfigurationSynchronized(configurationParams);
        return Configuration.fromJsonObject((JsonObject) objects.get(0));
    }

    private List<Object> readConfigurationSynchronized(final ConfigurationParams configurationParams) {
        try {
            return languageClientProvider.get().configuration(configurationParams).get(5, TimeUnit.SECONDS);
        } catch (final Exception e) {
            MessageHelper.showError(languageClientProvider.get(), "Something went wrong, could not read index configuration");
        }
        return Collections.emptyList();
    }

}