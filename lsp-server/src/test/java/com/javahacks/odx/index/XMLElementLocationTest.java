package com.javahacks.odx.index;

import org.junit.jupiter.api.Test;

import java.net.URI;

import static org.assertj.core.api.Assertions.assertThat;

class XMLElementLocationTest {

    private static final URI FILE_URI = URI.create("file://d:/test.txt");

    @Test
    void constructorSetsStartLocation() {
        final XmlElementLocation location = new XmlElementLocation(FILE_URI, 1, 2);

        assertThat(location.getFileUri()).isEqualTo(FILE_URI);
        assertThat(location.getStartLine()).isEqualTo(1);
        assertThat(location.getStartColumn()).isEqualTo(2);
    }

    @Test
    void uriIsSet() {
        final XmlElementLocation location = new XmlElementLocation(FILE_URI);

        assertThat(location.getFileUri()).isEqualTo(FILE_URI);
    }

    @Test
    void settersInitFields() {
        final XmlElementLocation location = new XmlElementLocation();
        location.setFileUri(FILE_URI);
        location.setStartLine(1);
        location.setStartColumn(2);
        location.setStartTagEndLine(3);
        location.setStartTagEndColumn(4);
        location.setEndLine(5);
        location.setEndColumn(6);

        assertThat(location.getFileUri()).isEqualTo(FILE_URI);
        assertThat(location.getStartLine()).isEqualTo(1);
        assertThat(location.getStartColumn()).isEqualTo(2);
        assertThat(location.getStartTagEndLine()).isEqualTo(3);
        assertThat(location.getStartTagEndColumn()).isEqualTo(4);
        assertThat(location.getEndLine()).isEqualTo(5);
        assertThat(location.getEndColumn()).isEqualTo(6);

    }

}