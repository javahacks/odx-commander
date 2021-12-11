package com.javahacks.odx.lsp.features;

import org.eclipse.lsp4j.*;
import org.eclipse.lsp4j.jsonrpc.messages.Either;
import org.eclipse.lsp4j.services.LanguageClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.UUID;

/**
 * LSP based progress monitor that propagates its current state to an associated language client
 */
public class ProgressMonitor {
    private static final Logger LOGGER = LoggerFactory.getLogger(ProgressMonitor.class);
    private final String token = UUID.randomUUID().toString();
    private final LanguageClient languageClient;
    private int totalNumberOfSteps;
    private int worked;
    private int lastPercentage;

    public ProgressMonitor(final LanguageClient languageClient) {
        this.languageClient = languageClient;
        start();
    }

    private void start() {
        final WorkDoneProgressCreateParams createParams = new WorkDoneProgressCreateParams();
        createParams.setToken(token);
        languageClient.createProgress(createParams);
    }

    public void begin(final String title, final int totalNumberOfSteps) {
        this.totalNumberOfSteps = totalNumberOfSteps;
        final WorkDoneProgressBegin begin = new WorkDoneProgressBegin();
        begin.setMessage("0 %");
        begin.setTitle(title);
        begin.setPercentage(0);
        notifyProgress(begin);
    }

    public void progressOneStep() {
        final int percentage = (int) (((double) ++worked / totalNumberOfSteps) * 100);

        if (percentage == lastPercentage) {
            //avoid sending to many client requests
            return;
        }

        lastPercentage = percentage;

        final WorkDoneProgressReport report = new WorkDoneProgressReport();
        report.setMessage(percentage + " %");
        report.setPercentage(percentage);
        notifyProgress(report);
    }

    public void done() {
        final WorkDoneProgressEnd end = new WorkDoneProgressEnd();
        end.setMessage("100 %");
        notifyProgress(end);
    }

    private synchronized void notifyProgress(final WorkDoneProgressNotification notification) {
        final ProgressParams params = new ProgressParams();
        params.setValue(Either.forLeft(notification));
        params.setToken(token);

        languageClient.notifyProgress(params);
    }

}
