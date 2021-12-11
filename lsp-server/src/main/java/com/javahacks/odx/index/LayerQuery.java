package com.javahacks.odx.index;

import com.javahacks.odx.model.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.stream.Stream;

import static com.google.common.base.Strings.isNullOrEmpty;

public class LayerQuery {
    private static final Logger LOGGER = LoggerFactory.getLogger(LayerQuery.class);
    private final Collection<DIAGCOMM> notInheritedDiagComms = new ArrayList<>();
    private final DIAGLAYER layer;

    private Collection<DIAGCOMM> visibleDiagComms;
    private Collection<DOPBASE> visibleDops;
    private Collection<GLOBALNEGRESPONSE> visibleNegResponses;
    private Collection<TABLE> visibleTables;

    public LayerQuery(final Layer layer) {
        this.layer = (DIAGLAYER) layer;
    }

    public Optional<DOPBASE> dopByShortName(final String shortName) {
        return visibleDops().stream()
                .filter(d -> shortName != null && shortName.equals(d.getSHORTNAME()))
                .findFirst();
    }

    public Optional<DIAGCOMM> diagCommByShortName(final String shortName, final boolean includeNotInherited) {
        final Stream<DIAGCOMM> serviceStream = includeNotInherited ?
                Stream.concat(visibleDiagComms().stream(), notInheritedDiagComms.stream()) : visibleDiagComms().stream();
        return serviceStream
                .filter(d -> shortName != null && shortName.equals(d.getSHORTNAME()))
                .findFirst();
    }


    public Collection<DIAGCOMM> visibleDiagComms() {
        if (visibleDiagComms == null) {
            visibleDiagComms = initDiagComms();
            traceInitialization(visibleDiagComms.size(), "Services");
        }
        return visibleDiagComms;
    }

    public Collection<GLOBALNEGRESPONSE> visibleGlobalNegResponses() {
        if (visibleNegResponses == null) {
            visibleNegResponses = initGlobalNegResponses();
            traceInitialization(visibleNegResponses.size(), "Neg-Responses");
        }
        return visibleNegResponses;
    }

    Collection<DOPBASE> visibleDops() {
        if (visibleDops == null) {
            visibleDops = initDops();
            traceInitialization(visibleDops.size(), "Dops");
        }
        return visibleDops;
    }

    public Collection<TABLE> visibleTables() {
        if (visibleTables == null) {
            visibleTables = initTables();
            traceInitialization(visibleTables.size(), "Tables");
        }
        return visibleTables;
    }

    public void reset() {
        visibleTables = null;
        visibleNegResponses = null;
        visibleDiagComms = null;
        visibleDops = null;
        notInheritedDiagComms.clear();
    }

    private Collection<TABLE> initTables() {
        final Map<String, TABLE> idToObjectMap = new HashMap<>();

        consumeParentLayers((parentRef, parentLayer) -> {
            for (final TABLE table : parentLayer.query().visibleTables()) {
                idToObjectMap.put(table.getSHORTNAME(), table);
            }
        });

        addLayerElements(idToObjectMap, TABLE.class, TABLE::getSHORTNAME);

        return idToObjectMap.values();
    }

    private Collection<DIAGCOMM> initDiagComms() {
        final Map<String, DIAGCOMM> idToObjectMap = new HashMap<>();

        consumeParentLayers((parentRef, parentLayer) -> {
            for (final DIAGCOMM diagcomm : parentLayer.query().visibleDiagComms()) {
                if (!isExcluded(diagcomm, parentRef)) {
                    idToObjectMap.put(diagcomm.getSHORTNAME(), diagcomm);
                } else {
                    notInheritedDiagComms.add(diagcomm);
                }
            }

        });

        addLayerElements(idToObjectMap, DIAGCOMM.class, DIAGCOMM::getSHORTNAME);

        //include all services referenced by ODX link
        layer.getDiagServicesSnRefs().stream()
                .map(link -> layer.getIndex().resolveLink(link, DIAGSERVICE.class))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .filter(service -> !isNullOrEmpty(service.getSHORTNAME()))
                .forEach(service -> idToObjectMap.put(service.getSHORTNAME(), service));

        return idToObjectMap.values();
    }


    private Collection<DOPBASE> initDops() {
        final Map<String, DOPBASE> idToObjectMap = new HashMap<>();

        consumeParentLayers((parentRef, parentLayer) -> {
            for (final DOPBASE dop : parentLayer.query().visibleDops()) {
                if (!isExcluded(dop, parentRef)) {
                    idToObjectMap.put(dop.getSHORTNAME(), dop);
                }
            }
        });

        addLayerElements(idToObjectMap, DOPBASE.class, DOPBASE::getSHORTNAME);

        return idToObjectMap.values();
    }


    private Collection<GLOBALNEGRESPONSE> initGlobalNegResponses() {
        final Map<String, GLOBALNEGRESPONSE> idToObjectMap = new HashMap<>();

        consumeParentLayers((parentRef, parentLayer) -> {
            for (final GLOBALNEGRESPONSE negResponse : parentLayer.query().visibleGlobalNegResponses()) {
                if (!isExcluded(negResponse, parentRef)) {
                    idToObjectMap.put(negResponse.getSHORTNAME(), negResponse);
                }
            }
        });

        addLayerElements(idToObjectMap, GLOBALNEGRESPONSE.class, GLOBALNEGRESPONSE::getSHORTNAME);

        return idToObjectMap.values();
    }


    private boolean isExcluded(final DIAGCOMM diagcomm, final PARENTREF parentref) {
        return parentref.getNotinheriteddiagcomms().stream()
                .anyMatch(ni -> shortNameMatches(ni.getDIAGCOMMSNREF(), diagcomm.getSHORTNAME()));
    }

    private boolean isExcluded(final DOPBASE dop, final PARENTREF parentref) {
        return parentref.getNotinheriteddops().stream()
                .anyMatch(ni -> shortNameMatches(ni.getDOPBASESNREF(), dop.getSHORTNAME()));
    }

    private boolean isExcluded(final GLOBALNEGRESPONSE globalnegresponse, final PARENTREF parentref) {
        return parentref.getNotinheritedglobalnegresponses().stream()
                .anyMatch(ni -> shortNameMatches(ni.getGLOBALNEGRESPONSESNREF(), globalnegresponse.getSHORTNAME()));
    }

    private boolean shortNameMatches(final SNREF snref, final String shortname) {
        return (snref != null && snref.getSHORTNAME() != null && snref.getSHORTNAME().equals(shortname));
    }

    private void consumeParentLayers(final BiConsumer<PARENTREF, Layer> layerConsumer) {
        if (layer instanceof HIERARCHYELEMENT) {
            ((HIERARCHYELEMENT) layer).getParentRefs().forEach(parentRef ->
                    layer.getIndex().resolveLink(parentRef, DIAGLAYER.class)
                            .ifPresent(layer -> layerConsumer.accept(parentRef, layer))
            );
        }
    }

    private <T> void addLayerElements(final Map<String, T> idToObjectMap, final Class<T> type, final Function<T, String> nameSupplier) {
        layer.getValueInheritedElements().stream()
                .filter(type::isInstance)
                .map(type::cast)
                .forEach(d -> idToObjectMap.put(nameSupplier.apply(d), d));
    }

    private void traceInitialization(final int size, final String type) {
        LOGGER.trace("{}: Initialized {} {} in layer {}", Thread.currentThread().getName(), size, type, layer.getSHORTNAME());
    }

}
