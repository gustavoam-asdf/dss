package eu.europa.esig.dss.lote.json.parsing;

import eu.europa.esig.dss.jades.DSSJsonUtils;
import eu.europa.esig.dss.lote.json.parsing.function.JsonOtherLoTEPointerConverter;
import eu.europa.esig.dss.lote.parsing.LoLoTEParsingResult;
import eu.europa.esig.dss.lote.source.LoLoTESource;
import eu.europa.esig.dss.model.DSSDocument;
import eu.europa.esig.dss.utils.Utils;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Parses a JWS List of TS 119 602 Lists of Trusted Entities
 *
 */
public class JsonLoLoTEParsingTask extends AbstractJsonLoTEParsingTask {

    /** The List Source to parse */
    private final LoLoTESource loloteSource;

    /**
     * The default constructor
     *
     * @param document {@link DSSDocument} List document to parse
     * @param loloteSource {@link LoLoTESource}
     */
    public JsonLoLoTEParsingTask(DSSDocument document, LoLoTESource loloteSource) {
        super(document);
        Objects.requireNonNull(loloteSource, "The LoLoTESource is null");
        this.loloteSource = loloteSource;
    }

    @Override
    public LoLoTEParsingResult get() {
        LoLoTEParsingResult result = new LoLoTEParsingResult();

        String unverifiedPayload = getUnverifiedLoTEPayload();
        Map<?, ?> jsonLoTEPayload = getJsonLoTEPayload(unverifiedPayload);

        parseSchemeInformation(result, DSSJsonUtils.getAsMap(jsonLoTEPayload, JsonLoTEHeaderParameterNames.LIST_AND_SCHEME_INFORMATION));
        verifyStructure(result, unverifiedPayload);

        return result;
    }

    private void parseSchemeInformation(LoLoTEParsingResult result, Map<?, ?> listAndSchemeInformation) {
        commonParseListAndSchemeInformation(result, listAndSchemeInformation);
        extractOtherLoTEPointers(result, listAndSchemeInformation);
    }

    private void extractOtherLoTEPointers(LoLoTEParsingResult result, Map<?, ?> listAndSchemeInformation) {
        List<?> pointersToOtherLoTE = DSSJsonUtils.getAsList(listAndSchemeInformation, JsonLoTEHeaderParameterNames.POINTERS_TO_OTHER_LOTE);
        if (Utils.isCollectionNotEmpty(pointersToOtherLoTE)) {
            JsonOtherLoTEPointerConverter converter = new JsonOtherLoTEPointerConverter();
            if (loloteSource.getLotePredicate() != null) {
                result.setOtherListPointers(pointersToOtherLoTE.stream().map(DSSJsonUtils::toMap).filter(Utils::isMapNotEmpty)
                        .map(converter).filter(loloteSource.getLotePredicate()).collect(Collectors.toList()));
            }
            if (loloteSource.getLolotePredicate() !=  null) {
                result.setCurrentListPointers(pointersToOtherLoTE.stream().map(DSSJsonUtils::toMap).filter(Utils::isMapNotEmpty)
                        .map(converter).filter(loloteSource.getLolotePredicate()).collect(Collectors.toList()));
            }
        }
    }

}
