package com.javahacks.odx.lsp.mapper;

import com.javahacks.odx.index.CategoryProxy;
import com.javahacks.odx.index.DocumentIndex;
import com.javahacks.odx.index.XmlElementLocation;
import com.javahacks.odx.lsp.dtos.Document;
import com.javahacks.odx.lsp.dtos.Link;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import static java.util.Comparator.comparing;

public class CategoryMapper {
    private final DocumentIndex documentIndex;

    public CategoryMapper(final DocumentIndex documentIndex) {
        this.documentIndex = documentIndex;
    }

    public List<Document> map(final String categoryType) {
        return documentIndex.getIndexedCategories().stream()
                .filter(category -> category.getDocType().equalsIgnoreCase(categoryType))
                .map(this::mapCategory)
                .sorted(comparing(Document::getName))
                .collect(Collectors.toList());
    }

    private Document mapCategory(final CategoryProxy proxy) {
        final Document description = new Document();
        description.setExpandable(proxy.isExpandable());
        description.setOdxLink(new Link(proxy.getDocType(), proxy.getShortName(), proxy.getId()));
        description.setName(proxy.getShortName());
        description.setLocation(proxy.getLocation());
        return description;
    }
}
