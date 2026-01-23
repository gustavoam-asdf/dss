package eu.europa.esig.dss.lote.xml.parsing;

import eu.europa.esig.dss.enumerations.ListType;
import eu.europa.esig.dss.lote.parsing.ListParsingResult;
import eu.europa.esig.dss.lote.parsing.ParsingResult;
import eu.europa.esig.dss.lote.source.ListSource;
import eu.europa.esig.dss.lote.xml.parsing.function.TrustedEntityConverter;
import eu.europa.esig.dss.lote.xml.parsing.predicate.NonEmptyTEName;
import eu.europa.esig.dss.lote.xml.parsing.predicate.NonEmptyTESInformation;
import eu.europa.esig.dss.lote.xml.parsing.predicate.NonEmptyTrustedEntityService;
import eu.europa.esig.dss.model.DSSDocument;
import eu.europa.esig.dss.model.DSSException;
import eu.europa.esig.dss.model.lote.TrustedEntity;
import eu.europa.esig.dss.model.lote.TrustedEntityService;
import eu.europa.esig.dss.utils.Utils;
import eu.europa.esig.lote.jaxb.ListOfTrustedEntitiesType;
import eu.europa.esig.lote.jaxb.LoTESchemeInformationType;
import eu.europa.esig.lote.jaxb.NextUpdateType;
import eu.europa.esig.lote.jaxb.NonEmptyURIListType;
import eu.europa.esig.lote.jaxb.TrustedEntitiesListType;
import eu.europa.esig.lote.xml.LOTEFacade;

import javax.xml.datatype.XMLGregorianCalendar;
import java.io.InputStream;
import java.math.BigInteger;
import java.util.Collections;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Objects;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public class LoTEXmlParsingTask implements Supplier<ParsingResult> {

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
    public LoTEXmlParsingTask(DSSDocument document, ListSource listSource) {
        Objects.requireNonNull(document, "Document is null");
        Objects.requireNonNull(listSource, "The TLSource is null");
        this.document = document;
        this.listSource = listSource;
    }

    @Override
    public ListParsingResult get() {
        ListParsingResult result = new ListParsingResult();
        ListOfTrustedEntitiesType jaxbObject = getJAXBObject();

        parseSchemeInformation(result, jaxbObject.getListAndSchemeInformation());
        parseTrustedEntitiesList(result, jaxbObject.getTrustedEntitiesList());
        // TODO : verify version conformity ?

        return result;
    }

    /**
     * Gets the {@code ListOfTrustedEntitiesType}
     *
     * @return {@link ListOfTrustedEntitiesType}
     */
    protected ListOfTrustedEntitiesType getJAXBObject() {
        try (InputStream is = document.openStream()) {
            return createLoTEFacade().unmarshall(is, false); // lax processing, validate XSD after
        } catch (Exception e) {
            String message = "Unable to parse binaries. Reason : '%s'";
            // get complete error message in case if the message string is not defined directly
            if (e.getMessage() == null && e.getCause() != null) {
                throw new DSSException(String.format(message, e.getCause().getMessage()), e);
            }
            throw new DSSException(String.format(message, e.getMessage()), e);
        }
    }

    /**
     * This method loads a {@code LOTEFacade}
     *
     * @return {@link LOTEFacade}
     */
    protected LOTEFacade createLoTEFacade() {
        return LOTEFacade.newFacade();
    }

    private void parseSchemeInformation(ListParsingResult result, LoTESchemeInformationType schemeInformation) {
        commonParseSchemeInformation(result, schemeInformation);
    }

    /**
     * Extracts the common values
     *
     * @param result {@link ListParsingResult}
     * @param schemeInformation {@link LoTESchemeInformationType}
     */
    protected void commonParseSchemeInformation(ListParsingResult result, LoTESchemeInformationType schemeInformation) {
        if (schemeInformation != null) {
            extractTSLType(result, schemeInformation);
            extractSequenceNumber(result, schemeInformation);
            extractTerritory(result, schemeInformation);
            extractVersion(result, schemeInformation);
            extractIssueDate(result, schemeInformation);
            extractNextUpdateDate(result, schemeInformation);
            extractDistributionPoints(result, schemeInformation);
        }
    }

    private void extractTSLType(ListParsingResult result, LoTESchemeInformationType schemeInformation) {
        String loTEType = schemeInformation.getLoTEType();
        if (Utils.isStringNotEmpty(loTEType)) {
            result.setType(ListType.fromUri(loTEType));
        }
    }

    private void extractSequenceNumber(ListParsingResult result, LoTESchemeInformationType schemeInformation) {
        BigInteger sequenceNumber = schemeInformation.getLoTESequenceNumber();
        if (sequenceNumber != null) {
            result.setSequenceNumber(sequenceNumber.intValue());
        }
    }

    private void extractTerritory(ListParsingResult result, LoTESchemeInformationType schemeInformation) {
        result.setTerritory(schemeInformation.getSchemeTerritory());
    }

    private void extractVersion(ListParsingResult result, LoTESchemeInformationType schemeInformation) {
        BigInteger versionIdentifier = schemeInformation.getLoTEVersionIdentifier();
        if (versionIdentifier != null) {
            result.setVersion(versionIdentifier.intValue());
        }
    }

    private void extractIssueDate(ListParsingResult result, LoTESchemeInformationType schemeInformation) {
        result.setIssueDate(convertToDate(schemeInformation.getListIssueDateTime()));
    }

    private void extractNextUpdateDate(ListParsingResult result, LoTESchemeInformationType schemeInformation) {
        NextUpdateType nextUpdate = schemeInformation.getNextUpdate();
        if (nextUpdate != null) {
            result.setNextUpdateDate(convertToDate(nextUpdate.getDateTime()));
        }
    }

    private Date convertToDate(XMLGregorianCalendar gregorianCalendar) {
        if (gregorianCalendar != null) {
            GregorianCalendar toGregorianCalendar = gregorianCalendar.toGregorianCalendar();
            if (toGregorianCalendar != null) {
                return toGregorianCalendar.getTime();
            }
        }
        return null;
    }

    private void extractDistributionPoints(ListParsingResult result, LoTESchemeInformationType schemeInformation) {
        NonEmptyURIListType distributionPoints = schemeInformation.getDistributionPoints();
        if (distributionPoints != null && Utils.isCollectionNotEmpty(distributionPoints.getURI())) {
            result.setDistributionPoints(Collections.unmodifiableList(distributionPoints.getURI()));
        } else {
            result.setDistributionPoints(Collections.emptyList());
        }
    }

    private void parseTrustedEntitiesList(ListParsingResult result, TrustedEntitiesListType trustedEntitiesListType) {
        if (trustedEntitiesListType != null && Utils.isCollectionNotEmpty(trustedEntitiesListType.getTrustedEntity())) {
            List<TrustedEntity> trustedEntities = trustedEntitiesListType.getTrustedEntity().stream()
                    .map(new TrustedEntityConverter().territory(result.getTerritory())).collect(Collectors.toList());
            List<TrustedEntity> filteredTrustedEntities = filter(trustedEntities);
            result.setTrustedEntities(Collections.unmodifiableList(filteredTrustedEntities));
        } else {
            result.setTrustedEntities(Collections.emptyList());
        }
    }

    private List<TrustedEntity> filter(List<TrustedEntity> trustedEntities) {
        List<TrustedEntity> filteredEntities = trustedEntities;

        // 1. Remove TSPs with invalid structure
        filteredEntities = filteredEntities.stream().filter(new NonEmptyTEName()).collect(Collectors.toList());

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
                        .filter(new NonEmptyTESInformation()).collect(Collectors.toList());

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
        return filteredEntities.stream().filter(new NonEmptyTrustedEntityService()).collect(Collectors.toList());
    }

}
