package com.javahacks.odx.lsp.features;

import com.javahacks.odx.index.CommentFoldable;
import com.javahacks.odx.index.LocationAware;
import com.javahacks.odx.index.RegionFoldable;
import com.javahacks.odx.index.XmlElementLocation;
import com.javahacks.odx.lsp.VirtualDocument;
import org.eclipse.lsp4j.FoldingRange;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class FoldingProvider {

    static final String COMMENT_KIND = "comment";
    static final String REGION_KIND = "region";

    public List<FoldingRange> createFoldingRanges(final VirtualDocument document) {
        if (document.getVirtualModel() == null) {
            return Collections.emptyList();
        }

        return document.getVirtualModel().getLocationAwares().stream()
                .map(this::createRange)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    private FoldingRange createRange(final LocationAware locationAware) {
        if (locationAware instanceof CommentFoldable) {
            return mapRange(locationAware, COMMENT_KIND);
        }
        if (locationAware instanceof RegionFoldable) {
            return mapRange(locationAware, REGION_KIND);
        }

        return null;
    }

    private FoldingRange mapRange(final LocationAware locationAware, final String kind) {
        final FoldingRange range = new FoldingRange();
        final XmlElementLocation location = locationAware.getLocation();
        range.setStartCharacter(location.getStartColumn());
        range.setStartLine(location.getStartLine());
        range.setEndCharacter(location.getEndColumn());
        range.setEndLine(location.getEndLine());
        range.setKind(kind);
        return range;
    }
}
