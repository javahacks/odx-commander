package com.javahacks.odx.integration;

import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.javahacks.odx.ServerLauncher;
import com.javahacks.odx.lsp.OdxLanguageClient;
import com.javahacks.odx.lsp.OdxLanguageServer;
import org.eclipse.lsp4j.*;
import org.eclipse.lsp4j.jsonrpc.Launcher;
import org.eclipse.lsp4j.launch.LSPLauncher;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CompletableFuture;

/**
 * Base class for all integration tests
 */
public abstract class LspTestBase {
    private static final String FOLDER_WITH_WHITESPACE = "working dir";

    protected OdxLanguageServer languageServer;
    protected TestClient languageClient = new TestClient();
    protected Path workspacePath;

    @TempDir()
    Path tempDir;

    private static void startServer(final ServerSocket serverSocket) {
        final Thread serverThread = new Thread(() -> {
            try {
                new ServerLauncher().main(new String[]{String.valueOf(serverSocket.getLocalPort())});
            } catch (final Exception e) {
                //
            }
        }
        );
        serverThread.setDaemon(true);
        serverThread.start();
    }

    @BeforeEach
    void setup() throws IOException {
        workspacePath = tempDir.resolve(FOLDER_WITH_WHITESPACE);
        Files.createDirectories(workspacePath);

    }

    @BeforeEach
    void initLanguageServer() throws Exception {
        final ServerSocket serverSocket = new ServerSocket(0, 1, InetAddress.getLoopbackAddress());
        startServer(serverSocket);

        awaitConnection(serverSocket);
    }

    protected OdxLanguageServer getLanguageServer() {
        return languageServer;
    }

    protected void awaitConnection(final ServerSocket serverSocket) {
        try {
            final Socket socket = serverSocket.accept();
            final Launcher<OdxLanguageServer> launcher = new LSPLauncher.Builder<OdxLanguageServer>()
                    .setLocalService(languageClient)
                    .setRemoteInterface(OdxLanguageServer.class)
                    .setInput(socket.getInputStream())
                    .setOutput(socket.getOutputStream())
                    .create();

            languageServer = launcher.getRemoteProxy();

            launcher.startListening();

        } catch (final Exception ex) {
            throw new IllegalStateException(ex);
        }
    }

    @AfterEach
    void shutdownServer() {
        languageServer.shutdown();
    }

    class TestClient implements OdxLanguageClient {
        public CompletableFuture<ConfigurationParams> configurationRequest = new CompletableFuture<>();
        public CompletableFuture<PublishDiagnosticsParams> diagnosticsRequest = new CompletableFuture<>();
        public CompletableFuture<MessageParams> messageRequest = new CompletableFuture<>();

        @Override
        public void indexChanged() {

        }

        @Override
        public void telemetryEvent(final Object object) {

        }

        @Override
        public void publishDiagnostics(final PublishDiagnosticsParams diagnostics) {
            diagnosticsRequest.complete(diagnostics);
        }

        @Override
        public void showMessage(final MessageParams messageParams) {
            messageRequest.complete(messageParams);
        }

        @Override
        public CompletableFuture<List<Object>> configuration(final ConfigurationParams configurationParams) {
            this.configurationRequest.complete(configurationParams);
            final JsonObject jsonObject = new JsonObject();
            jsonObject.add("activeIndexLocation", new JsonPrimitive(tempDir.resolve(FOLDER_WITH_WHITESPACE).toString()));
            jsonObject.add("maxHeapSpace", new JsonPrimitive("1G"));
            return CompletableFuture.completedFuture(Collections.singletonList(jsonObject));
        }

        @Override
        public CompletableFuture<MessageActionItem> showMessageRequest(final ShowMessageRequestParams requestParams) {
            return null;
        }

        @Override
        public void logMessage(final MessageParams message) {

        }

        @Override
        public CompletableFuture<Void> createProgress(final WorkDoneProgressCreateParams params) {
            return CompletableFuture.completedFuture(null);
        }

        @Override
        public void notifyProgress(final ProgressParams params) {
            //nothing to report
        }
    }

}
