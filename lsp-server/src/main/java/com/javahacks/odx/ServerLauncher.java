package com.javahacks.odx;

import com.javahacks.odx.index.DocumentIndex;
import com.javahacks.odx.lsp.*;
import com.javahacks.odx.utils.JavaUtilLoggingInitializer;
import org.eclipse.lsp4j.jsonrpc.Launcher;
import org.eclipse.lsp4j.launch.LSPLauncher;
import org.eclipse.lsp4j.services.LanguageServer;
import org.eclipse.lsp4j.services.TextDocumentService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Provider;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicReference;

/**
 * This class initializes and starts the ODX language server.
 */
public class ServerLauncher {
    private static final Logger LOGGER = LoggerFactory.getLogger(ServerLauncher.class);

    public static void main(final String[] args) throws Exception {
        JavaUtilLoggingInitializer.init();

        final ServerLauncher serverLauncher = new ServerLauncher();

        if (args.length > 0) {
            //when client port is passed the server connects to the language client
            serverLauncher.connectToClient(Integer.parseInt(args[0]));
        } else {
            //the client must connect to the server (development mode)
            serverLauncher.awaitConnection(8090);
        }

        Runtime.getRuntime().exit(1);
    }

    private void connectToClient(final int port) throws Exception {
        launchLanguageServer(new Socket("127.0.0.1", port));
    }

    private void awaitConnection(final int port) throws Exception {
        final ServerSocket serverSocket = new ServerSocket(port);

        while (true) {
            LOGGER.info("Awaiting client connection ...");
            try (final Socket socket = serverSocket.accept()) {
                LOGGER.info("Client connection accepted");
                launchLanguageServer(socket);
            } catch (final IOException ex) {
                LOGGER.info("Client connection was closed");
            }
        }
    }

    private void launchLanguageServer(final Socket socket) throws Exception {
        final AtomicReference<OdxLanguageClient> clientReference = new AtomicReference<>();

        final Launcher<OdxLanguageClient> launcher = new LSPLauncher.Builder<OdxLanguageClient>()
                .setLocalService(createLanguageServer(() -> clientReference.get()))
                .setRemoteInterface(OdxLanguageClient.class)
                .setInput(socket.getInputStream())
                .setOutput(socket.getOutputStream())
                .create();

        clientReference.set(launcher.getRemoteProxy());
        launcher.startListening().get();

    }

    /**
     * Simple context configuration could be replaced by any lightweight DI framework later.
     */
    private LanguageServer createLanguageServer(final Provider<OdxLanguageClient> clientProvider) {
        final ExecutorService executorService = Executors.newSingleThreadExecutor();

        final DocumentIndex documentIndex = new DocumentIndex();
        final IndexService indexService = new IndexService(clientProvider, documentIndex);
        final OdxLspExtension lspExtension = new OdxLspExtensionImpl(documentIndex, executorService);
        final TextDocumentService textDocumentService = new OdxTextDocumentService(documentIndex, executorService, clientProvider);
        final OdxWorkspaceService workspaceService = new OdxWorkspaceService(executorService, indexService, clientProvider);

        return new OdxLanguageServerImpl(textDocumentService, workspaceService, lspExtension);
    }


}