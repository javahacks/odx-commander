package com.javahacks.odx.lsp;

import com.javahacks.odx.index.DocumentIndex;
import com.javahacks.odx.index.JaxbUtil;
import com.javahacks.odx.index.LayerProxy;
import com.javahacks.odx.index.ProxyIndex;
import com.javahacks.odx.lsp.dtos.Configuration;
import com.javahacks.odx.model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.concurrent.TimeUnit;

import static com.javahacks.odx.model.TestHelper.saveOdxFile;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.mock;

class IndexServiceTest {

    private final ECUVARIANT ecuVariant = TestHelper.createEcuVariant();
    private final DIAGLAYERCONTAINER diaglayercontainer = TestHelper.newDiagLayerContainer();
    private final OdxLanguageClient languageClient = mock(OdxLanguageClient.class);
    private final DocumentIndex documentIndex = new DocumentIndex();
    private final IndexService indexService = new IndexService(() -> languageClient, documentIndex);
    private final Configuration configuration = new Configuration();

    @TempDir
    Path tempDir;

    @BeforeEach
    void setup() {
        diaglayercontainer.getEcuVariants().add(ecuVariant);
        configuration.setActiveIndexLocation(tempDir.toString());
    }

    @Test
    void proxyIndexIsWrittenWhenFileIsDeleted() throws Exception {
        final Path layerPath = tempDir.resolve("layer.odx");
        saveOdxFile(diaglayercontainer, layerPath);
        indexService.updateProxyIndex(configuration);
        final long timeStamp1 = Files.getLastModifiedTime(tempDir.resolve(".odc-index")).toMillis();
        Files.delete(layerPath);

        TimeUnit.MILLISECONDS.sleep(1);

        indexService.updateProxyIndex(configuration);

        assertThat(Files.getLastModifiedTime(tempDir.resolve(".odc-index")).toMillis()).isNotEqualTo(timeStamp1);
    }

    @Test
    void proxyIndexIsWritten() throws Exception {
        saveOdxFile(diaglayercontainer, tempDir.resolve("layer.odx"));
        indexService.updateProxyIndex(configuration);

        final ProxyIndex index = JaxbUtil.loadProxyIndex(tempDir.resolve(".odc-index"));
        assertThat(index.getCategories().get(0).getLayers().get(0).getShortName()).isEqualTo(ecuVariant.getSHORTNAME());
    }


    @Test
    void proxyIndexIsNotRewritten() throws Exception {
        saveOdxFile(diaglayercontainer, tempDir.resolve("layer.odx"));
        indexService.updateProxyIndex(configuration);
        final long timeStamp1 = Files.getLastModifiedTime(tempDir.resolve(".odc-index")).toMillis();
        indexService.updateProxyIndex(configuration);

        assertThat(Files.getLastModifiedTime(tempDir.resolve(".odc-index")).toMillis()).isEqualTo(timeStamp1);
    }

    @Test
    void proxyIndexIsRewritten() throws Exception {
        saveOdxFile(diaglayercontainer, tempDir.resolve("layer.odx"));
        indexService.updateProxyIndex(configuration);
        final long timeStamp1 = Files.getLastModifiedTime(tempDir.resolve(".odc-index")).toMillis();

        saveOdxFile(diaglayercontainer, tempDir.resolve("layer.odx"));
        indexService.updateProxyIndex(configuration);
        assertThat(Files.getLastModifiedTime(tempDir.resolve(".odc-index")).toMillis()).isNotEqualTo(timeStamp1);
    }


    @Test
    void corruptProxyIndexIsHandled() throws Exception {
        saveOdxFile(diaglayercontainer, tempDir.resolve(".odc-index"));

        catchThrowableOfType(() -> indexService.updateProxyIndex(configuration), Exception.class);
    }

    @Test
    void indexingJobCanBeCanceled() throws IOException {
        saveOdxFile(diaglayercontainer, tempDir.resolve("layer.odx"));

        final Configuration mock = mock(Configuration.class);
        Mockito.when(mock.getActiveIndexLocation()).thenAnswer(new Answer<String>() {
            @Override
            public String answer(final InvocationOnMock invocationOnMock) throws Throwable {
                indexService.cancelRunningJob();
                return configuration.getActiveIndexLocation();
            }
        });

        indexService.updateProxyIndex(mock);

        assertThat(documentIndex.getIndexedCategories()).isEmpty();

    }

    @Test
    void emptyIndexLocationIsHandled() {
        assertThatCode(() -> indexService.updateProxyIndex(new Configuration())).doesNotThrowAnyException();
    }


    @Test
    void proxyChildrenIsWritten() throws Exception {
        final BASEVARIANT basevariant = addBaseVariantLayer();
        saveOdxFile(diaglayercontainer, tempDir.resolve("layer.odx"));
        indexService.updateProxyIndex(configuration);

        final ProxyIndex index = JaxbUtil.loadProxyIndex(tempDir.resolve(".odc-index"));
        final LayerProxy bvProxy = index.getCategories().get(0).getLayers().stream().filter(l -> l.getId().equals(basevariant.getID())).findFirst().get();

        assertThat(bvProxy.getChildren()).hasSize(1);
        assertThat(bvProxy.getChildren().get(0).getId()).isEqualTo(ecuVariant.getID());
    }

    @Test
    void proxyParentIsWritten() throws Exception {
        final BASEVARIANT basevariant = addBaseVariantLayer();
        saveOdxFile(diaglayercontainer, tempDir.resolve("layer.odx"));
        indexService.updateProxyIndex(configuration);

        final ProxyIndex index = JaxbUtil.loadProxyIndex(tempDir.resolve(".odc-index"));
        final LayerProxy evProxy = index.getCategories().get(0).getLayers().stream().filter(l -> l.getId().equals(ecuVariant.getID())).findFirst().get();

        assertThat(evProxy.getParents()).hasSize(1);
        assertThat(evProxy.getParents().get(0).getId()).isEqualTo(basevariant.getID());
    }

    private BASEVARIANT addBaseVariantLayer() {
        final BASEVARIANT baseVariant = TestHelper.createBaseVariant();
        diaglayercontainer.getBaseVariants().add(baseVariant);

        final PARENTREF parentref = new BASEVARIANTREF();
        parentref.setDOCREF(baseVariant.getSHORTNAME());
        parentref.setIDREF(baseVariant.getID());
        parentref.setDOCTYPE(DOCTYPE.LAYER);
        ecuVariant.getParentRefs().add(parentref);
        return baseVariant;
    }
}