package com.javahacks.odx.integration;

import com.javahacks.odx.index.JaxbUtil;
import com.javahacks.odx.lsp.dtos.Value;
import com.javahacks.odx.model.DIAGLAYERCONTAINER;
import com.javahacks.odx.model.ODX;
import org.eclipse.lsp4j.DidChangeConfigurationParams;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.time.Duration;
import java.util.concurrent.CompletableFuture;

import static com.javahacks.odx.index.LocationAwareXMLStreamWriter.NOTICE;
import static com.javahacks.odx.model.TestHelper.createEcuVariant;
import static com.javahacks.odx.model.TestHelper.newDiagLayerContainer;
import static org.assertj.core.api.Assertions.assertThat;

public class OdxLspExtensionTestIT extends LspTestBase {


    @Test
    void documentContentIsReturned() throws IOException {
        final Path path = createAndWriteSampleModel();
        getLanguageServer().getWorkspaceService().didChangeConfiguration(new DidChangeConfigurationParams());

        final CompletableFuture<String> content = getLanguageServer().getOdxExtension().getContent(new Value(path.toUri().toString()));

        assertThat(content).succeedsWithin(Duration.ofSeconds(10)).asString().contains(NOTICE);
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
