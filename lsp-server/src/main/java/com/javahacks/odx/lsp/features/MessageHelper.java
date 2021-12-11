package com.javahacks.odx.lsp.features;

import org.eclipse.lsp4j.MessageParams;
import org.eclipse.lsp4j.MessageType;
import org.eclipse.lsp4j.services.LanguageClient;

/**
 * Utility class used to send message requests to the client
 */
public class MessageHelper {

    public static void showError(final LanguageClient languageClient, final String message) {
        languageClient.showMessage(getParams(message, MessageType.Error));
    }

    public static void showInfo(final LanguageClient languageClient, final String message) {
        languageClient.showMessage(getParams(message, MessageType.Info));
    }

    public static void showWarning(final LanguageClient languageClient, final String message) {
        languageClient.showMessage(getParams(message, MessageType.Warning));
    }

    public static void showLog(final LanguageClient languageClient, final String message) {
        languageClient.showMessage(getParams(message, MessageType.Log));
    }

    private static MessageParams getParams(String errorMessage, MessageType type) {
        final MessageParams params = new MessageParams();
        params.setType(type);
        params.setMessage(errorMessage);
        return params;
    }
}
