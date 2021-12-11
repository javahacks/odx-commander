package com.javahacks.odx.utils;

import com.javahacks.odx.index.XmlElementLocation;
import org.junit.jupiter.api.Test;

import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;

import static com.javahacks.odx.model.TestHelper.createStartEndTagLocation;
import static com.javahacks.odx.utils.OdxUtils.*;
import static org.assertj.core.api.Assertions.assertThat;

class OdxUtilsTest {

    @Test
    void readOnlyProtocolIsRemoved() {
        assertThat(parseUri("odx:file://test.txt")).isEqualTo(URI.create("file://test.txt"));
    }

    @Test
    void textToHexStringIsConverted() {
        assertThat(convertTextToHexStringOrDefault("10", "default")).isEqualTo("0A");
        assertThat(convertTextToHexStringOrDefault("255", "default")).isEqualTo("FF");
        assertThat(convertTextToHexStringOrDefault("xx", "default")).isEqualTo("default");
    }

    @Test
    void sameLineMatchesStartColumn() {
        final XmlElementLocation location = createStartEndTagLocation(1, 1, 1, 10);
        assertThat(isBetweenStartAndEndTag(location,1,1)).isTrue();
        assertThat(isBetweenStartAndEndTag(location,1,0)).isFalse();
    }

    @Test
    void sameLineMatchesEndColumn() {
        final XmlElementLocation location = createStartEndTagLocation(1, 0, 1, 10);
        assertThat(isBetweenStartAndEndTag(location,1,10)).isTrue();
        assertThat(isBetweenStartAndEndTag(location,1,11)).isFalse();
    }

    @Test
    void multipleLineMatchesStartColumn() {
        final XmlElementLocation location = createStartEndTagLocation(1, 1, 2, 10);
        assertThat(isBetweenStartAndEndTag(location,1,1)).isTrue();
        assertThat(isBetweenStartAndEndTag(location,1,0)).isFalse();
    }

    @Test
    void multipleLineMatchesEndColumn() {
        final XmlElementLocation location = createStartEndTagLocation(1, 1, 2, 10);
        assertThat(isBetweenStartAndEndTag(location,2,10)).isTrue();
        assertThat(isBetweenStartAndEndTag(location,2,11)).isFalse();
    }

    @Test
    void jumpToLineCommandIsCorrect() throws UnsupportedEncodingException {
        final XmlElementLocation location = createStartEndTagLocation(10, 10, 20, 10);
        final String commandString = URLDecoder.decode(createGotoLocationCommand(location), StandardCharsets.UTF_8.name());

        assertThat(commandString).isEqualTo("command:odx.jumpToLine?[{\"fileUri\":\"/temp/test.odx\",\"startLine\":10,\"startColumn\":10},false]");
    }


}