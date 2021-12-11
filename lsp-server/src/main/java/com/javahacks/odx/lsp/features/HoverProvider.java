package com.javahacks.odx.lsp.features;

import com.google.common.base.Strings;
import com.javahacks.odx.index.AbstractLink;
import com.javahacks.odx.index.ElementWithParams;
import com.javahacks.odx.index.LocationAware;
import com.javahacks.odx.index.XmlElementLocation;
import com.javahacks.odx.lsp.VirtualDocument;
import com.javahacks.odx.model.PARAM;
import com.javahacks.odx.model.POSITIONABLEPARAM;
import org.eclipse.lsp4j.Hover;
import org.eclipse.lsp4j.MarkupContent;
import org.eclipse.lsp4j.Position;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static com.javahacks.odx.lsp.mapper.ParamMapper.mapName;
import static com.javahacks.odx.utils.OdxUtils.createGotoLocationCommand;
import static com.javahacks.odx.utils.OdxUtils.isBetweenStartAndEndTag;

/**
 * Provides hover support for ODX elements
 */
public class HoverProvider {

    public static final int SHORT_NAME_MAX_LENGTH = 20;

    public Optional<Hover> createHover(final VirtualDocument document, final Position position) {
        if (document.getVirtualModel() == null) {
            return Optional.empty();
        }

        return reverseStream(document.getVirtualModel().getLocationAwares())
                .filter(LocationAware.class::isInstance)
                .map(LocationAware.class::cast)
                .filter(link -> isBetweenStartAndEndTag(link.getLocation(), position.getLine(), position.getCharacter()))
                .map(link -> map(link))
                .filter(Objects::nonNull)
                .findFirst();
    }

    private Hover map(final LocationAware locationAware) {
        if (locationAware instanceof AbstractLink) {
            return createMarkdownHover("");
        }

        if (locationAware instanceof ElementWithParams) {
            return createParamsHover((ElementWithParams) locationAware);
        }

        return null;
    }


    private Hover createParamsHover(final ElementWithParams parent) {
        final StringBuilder sb = new StringBuilder();
        sb.append("### ").append(parent.getClass().getSimpleName()).append(": ").append(limitName(parent.getSHORTNAME(), SHORT_NAME_MAX_LENGTH)).append("\n");
        sb.append("---").append("\n");
        sb.append("Shortname | Type | Byte | Bit").append("\n");
        sb.append(":------------------|:------------:|:---:|:---:").append("\n");
        appendParams(sb, parent.getParams());
        return createMarkdownHover(sb.toString());
    }

    private void appendParams(final StringBuilder sb, final List<PARAM> params) {
        params.stream().filter(POSITIONABLEPARAM.class::isInstance).
                map(POSITIONABLEPARAM.class::cast)
                .forEach(param ->
                        sb.append(mapNameWithCommand(param.getSHORTNAME(), param.getLocation())).append("|")
                                .append(mapName(param)).append("|")
                                .append(nullToZero(param.getBYTEPOSITION())).append("|")
                                .append(nullToZero(param.getBITPOSITION())).append("\n")
                );
    }


    private String mapNameWithCommand(final String name, final XmlElementLocation location) {
        return "[" + limitName(name, SHORT_NAME_MAX_LENGTH) + "](" + createGotoLocationCommand(location) + ")";
    }

    private String limitName(final String shortName, final int maxLength) {
        if (shortName != null && shortName.length() > maxLength) {
            return shortName.substring(0, maxLength) + " ...";
        }
        return Strings.nullToEmpty(shortName);
    }

    private static Stream<LocationAware> reverseStream(final List<LocationAware> input) {
        return IntStream.range(0, input.size())
                .mapToObj(i -> input.get(input.size() - i - 1));
    }

    private Hover createMarkdownHover(final String value) {
        final Hover hover = new Hover();
        final MarkupContent markupContent = new MarkupContent();
        markupContent.setKind("markdown");
        markupContent.setValue(value);
        hover.setContents(markupContent);
        return hover;
    }

    private String nullToZero(final Long value) {
        return value != null ? String.valueOf(value) : "0";
    }
}
