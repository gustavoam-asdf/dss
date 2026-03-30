package eu.europa.esig.dss.lote.json.parsing;

import eu.europa.esig.dss.enumerations.ListType;
import eu.europa.esig.dss.jades.DSSJsonUtils;
import eu.europa.esig.dss.jades.JWSCompactSerializationParser;
import eu.europa.esig.dss.jades.validation.JWS;
import eu.europa.esig.dss.lote.json.parsing.function.JsonOtherLoTEPointerConverter;
import eu.europa.esig.dss.lote.json.parsing.function.JsonTrustedEntityConverter;
import eu.europa.esig.dss.lote.parsing.ListParsingResult;
import eu.europa.esig.dss.lote.parsing.ParsingResult;
import eu.europa.esig.dss.lote.parsing.predicate.NonEmptyTENamePredicate;
import eu.europa.esig.dss.lote.parsing.predicate.NonEmptyTESInformationPredicate;
import eu.europa.esig.dss.lote.parsing.predicate.NonEmptyTrustedEntityServicePredicate;
import eu.europa.esig.dss.lote.source.ListSource;
import eu.europa.esig.dss.model.DSSDocument;
import eu.europa.esig.dss.model.DSSException;
import eu.europa.esig.dss.model.lote.TrustedEntity;
import eu.europa.esig.dss.model.lote.TrustedEntityService;
import eu.europa.esig.dss.spi.DSSUtils;
import eu.europa.esig.dss.utils.Utils;
import eu.europa.esig.json.JSONParser;
import eu.europa.esig.json.JsonObjectWrapper;
import eu.europa.esig.lote.json.LOTEJsonUtils;

import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Supplier;
import java.util.stream.Collectors;

/**
 * This class performs reading and information extraction from an obtained JWS compact LoTE
 *
 */
public class LoTEJWSCompactParsingTask implements Supplier<ParsingResult> {

    /** Document ot parse */
    private final DSSDocument document;

    /** The List Source to parse */
    private final ListSource listSource;

    /**
     * The default constructor
     *
     * @param document {@link DSSDocument} List document to parse
     * @param listSource {@link ListSource}
     */
    public LoTEJWSCompactParsingTask(DSSDocument document, ListSource listSource) {
        Objects.requireNonNull(document, "Document is null");
        Objects.requireNonNull(listSource, "The TLSource is null");
        this.document = document;
        this.listSource = listSource;
    }

    @Override
    public ListParsingResult get() {
        ListParsingResult result = new ListParsingResult();

        String unverifiedPayload = getUnverifiedLoTEPayload();
        Map<?, ?> jsonLoTEPayload = getJsonLoTEPayload(unverifiedPayload);

        parseListAndSchemeInformation(result, DSSJsonUtils.getAsMap(jsonLoTEPayload, JsonLoTEHeaderParameterNames.LIST_AND_SCHEME_INFORMATION));
        parseTrustedEntitiesList(result, DSSJsonUtils.getAsList(jsonLoTEPayload, JsonLoTEHeaderParameterNames.TRUSTED_ENTITIES_LIST));
        verifyStructure(result, unverifiedPayload);

        return result;
    }

    /**
     * Gets the LoTE payload
     *
     * @return {@link String}
     */
    protected String getUnverifiedLoTEPayload() {
        JWSCompactSerializationParser parser = new JWSCompactSerializationParser(document);
        JWS jws = parser.parse();
        return jws.getUnverifiedPayload();
    }

    /**
     * Gets the LoTE payload map representing the signed content of the JSON LoTE
     *
     * @return {@link Map}
     */
    protected Map<?, ?> getJsonLoTEPayload(String payloadString) {
        try  {
            Map<String, Object> jsonMap = DSSJsonUtils.parseJsonStringToMap(payloadString);
            Map<?, ?> lote = DSSJsonUtils.getAsMap(jsonMap, JsonLoTEHeaderParameterNames.LOTE);
            Objects.requireNonNull(lote, "Json LoTE shall have the header 'LoTE' as the root!");
            return lote;

        } catch (Exception e) {
            String message = "Unable to parse binaries. Reason : '%s'";
            // get complete error message in case if the message string is not defined directly
            if (e.getMessage() == null && e.getCause() != null) {
                throw new DSSException(String.format(message, e.getCause().getMessage()), e);
            }
            throw new DSSException(String.format(message, e.getMessage()), e);
        }
    }

    private void parseListAndSchemeInformation(ListParsingResult result, Map<?, ?> listAndSchemeInformation) {
        if (listAndSchemeInformation != null) {
            extractTSLType(result, listAndSchemeInformation);
            extractSequenceNumber(result, listAndSchemeInformation);
            extractTerritory(result, listAndSchemeInformation);
            extractVersion(result, listAndSchemeInformation);
            extractIssueDate(result, listAndSchemeInformation);
            extractNextUpdateDate(result, listAndSchemeInformation);
            extractDistributionPoints(result, listAndSchemeInformation);
            extractOtherLoTEPointers(result, listAndSchemeInformation);
        }
    }

    private void extractTSLType(ListParsingResult result, Map<?, ?> listAndSchemeInformation) {
        String loTEType = DSSJsonUtils.getAsString(listAndSchemeInformation, JsonLoTEHeaderParameterNames.LOTE_TYPE);
        if (Utils.isStringNotEmpty(loTEType)) {
            result.setType(ListType.fromUri(loTEType));
        }
    }

    private void extractSequenceNumber(ListParsingResult result, Map<?, ?> listAndSchemeInformation) {
        Number sequenceNumber = DSSJsonUtils.getAsNumber(listAndSchemeInformation, JsonLoTEHeaderParameterNames.LOTE_SEQUENCE_NUMBER);
        if (sequenceNumber != null) {
            result.setSequenceNumber(sequenceNumber.intValue());
        }
    }

    private void extractTerritory(ListParsingResult result, Map<?, ?> listAndSchemeInformation) {
        String schemeTerritory = DSSJsonUtils.getAsString(listAndSchemeInformation, JsonLoTEHeaderParameterNames.SCHEME_TERRITORY);
        if (Utils.isStringNotEmpty(schemeTerritory)) {
            result.setTerritory(schemeTerritory);
        }
    }

    private void extractVersion(ListParsingResult result, Map<?, ?> listAndSchemeInformation) {
        Number versionIdentifier = DSSJsonUtils.getAsNumber(listAndSchemeInformation, JsonLoTEHeaderParameterNames.LOTE_VERSION_IDENTIFIER);
        if (versionIdentifier != null) {
            result.setVersion(versionIdentifier.intValue());
        }
    }

    private void extractIssueDate(ListParsingResult result, Map<?, ?> listAndSchemeInformation) {
        String listIssueDateTimeString = DSSJsonUtils.getAsString(listAndSchemeInformation, JsonLoTEHeaderParameterNames.LIST_ISSUE_DATE_TIME);
        if (Utils.isStringNotEmpty(listIssueDateTimeString)) {
            Date listIssueDateTime = DSSUtils.parseRFCDate(listIssueDateTimeString);
            if (listIssueDateTime != null) {
                result.setIssueDate(listIssueDateTime);
            }
        }
    }

    private void extractNextUpdateDate(ListParsingResult result, Map<?, ?> listAndSchemeInformation) {
        String nextUpdateString = DSSJsonUtils.getAsString(listAndSchemeInformation, JsonLoTEHeaderParameterNames.NEXT_UPDATE);
        if (Utils.isStringNotEmpty(nextUpdateString)) {
            Date nextUpdate = DSSUtils.parseRFCDate(nextUpdateString);
            if (nextUpdate != null) {
                result.setNextUpdateDate(nextUpdate);
            }
        }
    }

    private void extractDistributionPoints(ListParsingResult result, Map<?, ?> listAndSchemeInformation) {
        List<?> distributionPointsList = DSSJsonUtils.getAsList(listAndSchemeInformation, JsonLoTEHeaderParameterNames.DISTRIBUTION_POINTS);
        if (Utils.isCollectionNotEmpty(distributionPointsList)) {
            List<String> distributionPoints = DSSJsonUtils.toListOfStrings(distributionPointsList);
            result.setDistributionPoints(Collections.unmodifiableList(distributionPoints));
        } else {
            result.setDistributionPoints(Collections.emptyList());
        }
    }

    private void extractOtherLoTEPointers(ListParsingResult result, Map<?, ?> listAndSchemeInformation) {
        List<?> pointersToOtherLoTE = DSSJsonUtils.getAsList(listAndSchemeInformation, JsonLoTEHeaderParameterNames.POINTERS_TO_OTHER_LOTE);
        if (Utils.isCollectionNotEmpty(pointersToOtherLoTE)) {
            JsonOtherLoTEPointerConverter converter = new JsonOtherLoTEPointerConverter();
            if (listSource.getOtherListPointerPredicate() != null) {
                result.setOtherListPointers(pointersToOtherLoTE.stream().map(DSSJsonUtils::toMap).filter(Utils::isMapNotEmpty)
                        .map(converter).filter(listSource.getOtherListPointerPredicate()).collect(Collectors.toList()));
            }
        }
    }

    private void parseTrustedEntitiesList(ListParsingResult result, List<?> trustedEntitiesList) {
        if (Utils.isCollectionNotEmpty(trustedEntitiesList)) {
            List<TrustedEntity> trustedEntities = trustedEntitiesList.stream()
                    .map(DSSJsonUtils::toMap).filter(Utils::isMapNotEmpty)
                    .map(new JsonTrustedEntityConverter().territory(result.getTerritory()))
                    .collect(Collectors.toList());
            List<TrustedEntity> filteredTrustedEntities = filter(trustedEntities);
            result.setTrustedEntities(Collections.unmodifiableList(filteredTrustedEntities));
        } else {
            result.setTrustedEntities(Collections.emptyList());
        }
    }

    private List<TrustedEntity> filter(List<TrustedEntity> trustedEntities) {
        List<TrustedEntity> filteredEntities = trustedEntities;

        // 1. Remove TSPs with invalid structure
        filteredEntities = filteredEntities.stream().filter(new NonEmptyTENamePredicate()).collect(Collectors.toList());

        // 2. Filter the TSP with the predicate
        if (listSource.getTrustedEntityPredicate() != null) {
            filteredEntities = filteredEntities.stream().filter(listSource.getTrustedEntityPredicate()).collect(Collectors.toList());
        }

        // 3. Foreach TSP, remove invalid trust services
        for (TrustedEntity trustedEntity : filteredEntities) {
            List<?> services = trustedEntity.getServices();
            if (Utils.isCollectionNotEmpty(services)) {
                List<TrustedEntityService> filteredServices = (List<TrustedEntityService>) services;
                filteredServices = filteredServices.stream()
                        .filter(new NonEmptyTESInformationPredicate()).collect(Collectors.toList());

                // 4. Filter the trust services with the predicate
                if (listSource.getTrustedServicePredicate() != null) {
                    filteredServices = filteredServices.stream()
                            .filter(listSource.getTrustedServicePredicate()).collect(Collectors.toList());
                }

                if (!filteredServices.isEmpty()) {
                    trustedEntity.setServices(filteredServices);
                }
            }
        }

        // 5. Remove TSPs with empty trust services
        return filteredEntities.stream().filter(new NonEmptyTrustedEntityServicePredicate()).collect(Collectors.toList());
    }

    /**
     * Verifies the structure conformity of the List of Trusted Entities
     *
     * @param result {@link ListParsingResult}
     */
    protected void verifyStructure(ListParsingResult result, String payload) {
        JsonObjectWrapper jsonObject = new JSONParser().parse(payload);
        List<String> errors = LOTEJsonUtils.getInstance().validateAgainstSchema(jsonObject);
        if (Utils.isCollectionNotEmpty(errors)) {
            result.setStructureValidationMessages(errors);
        }
    }

}
