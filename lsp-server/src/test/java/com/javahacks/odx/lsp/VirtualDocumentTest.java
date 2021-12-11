package com.javahacks.odx.lsp;

import org.assertj.core.api.Assertions;
import org.eclipse.lsp4j.Position;
import org.eclipse.lsp4j.Range;
import org.eclipse.lsp4j.TextDocumentContentChangeEvent;
import org.junit.jupiter.api.Test;

import java.net.URI;
import java.util.Collections;
import java.util.List;

import static java.net.URI.create;

class VirtualDocumentTest {
    public static final URI URI = create("file://test.txt");
    private static final String LINE = "1234567890";

    @Test
    void firstCharacterIsReplaced() {
        final VirtualDocument document = new VirtualDocument(URI, LINE, false);
        final List<TextDocumentContentChangeEvent> changeEvents = Collections.singletonList(createChangeEvent(0, 0, 0, 1, "x"));
        document.applyTextChanges(changeEvents);
        Assertions.assertThat(document.getTextContent()).isEqualTo("x234567890");
    }

    @Test
    void firstLineIsReplaced() {
        final VirtualDocument document = new VirtualDocument(URI, LINE, false);
        final List<TextDocumentContentChangeEvent> changeEvents = Collections.singletonList(createChangeEvent(0, 0, 0, 10, "x"));
        document.applyTextChanges(changeEvents);
        Assertions.assertThat(document.getTextContent()).isEqualTo("x");
    }

    @Test
    void secondLineIsRemoved() {
        final VirtualDocument document = new VirtualDocument(URI, LINE + "\n" + LINE + "\n" + LINE, false);
        final List<TextDocumentContentChangeEvent> changeEvents = Collections.singletonList(createChangeEvent(1, 0, 1, 11, ""));
        document.applyTextChanges(changeEvents);
        Assertions.assertThat(document.getTextContent()).isEqualTo(LINE + "\n" + LINE);
    }

    private TextDocumentContentChangeEvent createChangeEvent(final int lineStart, final int charStart, final int lineEnd, final int charEnd, final String text) {
        final TextDocumentContentChangeEvent event = new TextDocumentContentChangeEvent();
        event.setRange(new Range(new Position(lineStart, charStart), new Position(lineEnd, charEnd)));
        event.setText(text);
        return event;
    }


}