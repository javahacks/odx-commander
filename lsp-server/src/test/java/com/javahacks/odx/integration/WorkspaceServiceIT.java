package com.javahacks.odx.integration;

import com.javahacks.odx.index.JaxbUtil;
import com.javahacks.odx.lsp.IndexService;
import com.javahacks.odx.lsp.dtos.Document;
import com.javahacks.odx.lsp.dtos.Value;
import com.javahacks.odx.model.DIAGLAYERCONTAINER;
import com.javahacks.odx.model.DOCTYPE;
import com.javahacks.odx.model.ODX;
import com.javahacks.odx.utils.OdxUtils;
import org.eclipse.lsp4j.DidChangeConfigurationParams;
import org.eclipse.lsp4j.DidChangeWatchedFilesParams;
import org.eclipse.lsp4j.FileChangeType;
import org.eclipse.lsp4j.FileEvent;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.nio.file.attribute.FileTime;
import java.time.Duration;
import java.time.Instant;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

import static com.javahacks.odx.model.TestHelper.createEcuVariant;
import static com.javahacks.odx.model.TestHelper.newDiagLayerContainer;
import static org.assertj.core.api.Assertions.assertThat;

public class WorkspaceServiceIT extends LspTestBase {

    @Test
    void configurationChangeReadsConfiguration() {
        getLanguageServer().getWorkspaceService().didChangeConfiguration(new DidChangeConfigurationParams());

        assertThat(languageClient.configurationRequest).succeedsWithin(1, TimeUnit.SECONDS);
    }

    @Test
    void odxIndexIsWritten() throws IOException {
        createAndWriteSampleModel();

        getLanguageServer().getWorkspaceService().didChangeConfiguration(new DidChangeConfigurationParams());
        waitUntilIndexingJobFinished();

        final Path indexPath = workspacePath.resolve(IndexService.ODX_INDEX_FILE_NAME);
        assertThat(Files.exists(indexPath)).isTrue();
    }


    @Test
    void proxyIndexIsUpdated() throws IOException, InterruptedException {
        final Path odxFile = createAndWriteSampleModel();
        getLanguageServer().getWorkspaceService().didChangeConfiguration(new DidChangeConfigurationParams());
        waitUntilIndexingJobFinished();
        final Path indexPath = workspacePath.resolve(IndexService.ODX_INDEX_FILE_NAME);
        final long timeStamp1 = OdxUtils.getTimeStamp(indexPath);

        TimeUnit.MILLISECONDS.sleep(10);

        Files.setLastModifiedTime(odxFile, FileTime.from(Instant.now()));
        getLanguageServer().getWorkspaceService().didChangeConfiguration(new DidChangeConfigurationParams());
        waitUntilIndexingJobFinished();
        final long timeStamp2 = OdxUtils.getTimeStamp(indexPath);

        assertThat(timeStamp1).isNotEqualTo(timeStamp2);

    }


    @Test
    void proxyIndexIsNotUpdated() throws IOException {
        final Path odxFile = createAndWriteSampleModel();
        getLanguageServer().getWorkspaceService().didChangeConfiguration(new DidChangeConfigurationParams());
        waitUntilIndexingJobFinished();
        final Path indexPath = workspacePath.resolve(IndexService.ODX_INDEX_FILE_NAME);
        final long timeStamp1 = OdxUtils.getTimeStamp(indexPath);

        getLanguageServer().getWorkspaceService().didChangeConfiguration(new DidChangeConfigurationParams());
        waitUntilIndexingJobFinished();
        final long timeStamp2 = OdxUtils.getTimeStamp(indexPath);

        assertThat(timeStamp1).isEqualTo(timeStamp2);

    }


    private DidChangeWatchedFilesParams createChangeFileParams(final Path odxFile, final boolean updateFile) throws InterruptedException, IOException {
        if (updateFile) {
            TimeUnit.MILLISECONDS.sleep(10);
            Files.setLastModifiedTime(odxFile, FileTime.from(Instant.now()));
        }

        final DidChangeWatchedFilesParams params = new DidChangeWatchedFilesParams();
        final FileEvent fileEvent = new FileEvent();
        fileEvent.setUri(odxFile.toUri().toString());
        fileEvent.setType(FileChangeType.Changed);
        params.setChanges(Collections.singletonList(fileEvent));
        return params;
    }


    private void waitUntilIndexingJobFinished() {
        final CompletableFuture<List<Document>> categories = getLanguageServer().getOdxExtension().getCategoriesByType(new Value(DOCTYPE.CONTAINER.value()));
        assertThat(categories).succeedsWithin(Duration.ofSeconds(10));
    }

    private Path createAndWriteSampleModel() throws IOException {

        final Path odxFile = workspacePath.resolve("layer.odx");
        try (final Writer writer = Files.newBufferedWriter(odxFile, StandardCharsets.UTF_8, StandardOpenOption.CREATE_NEW)) {
            JaxbUtil.marshalModel(createTestModel(), writer);
        }
        return odxFile;
    }

    private ODX createTestModel() {
        final DIAGLAYERCONTAINER container = newDiagLayerContainer();
        container.getEcuVariants().add(createEcuVariant());

        final ODX odx = new ODX();
        odx.setDIAGLAYERCONTAINER(container);
        return odx;
    }


}
