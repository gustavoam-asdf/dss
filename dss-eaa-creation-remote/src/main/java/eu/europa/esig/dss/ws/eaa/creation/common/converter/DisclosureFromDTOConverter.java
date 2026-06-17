package eu.europa.esig.dss.ws.eaa.creation.common.converter;

import eu.europa.esig.dss.eaa.common.creation.EAADisclosure;
import eu.europa.esig.dss.eaa.jwt.creation.SDJWTEAADisclosure;
import eu.europa.esig.dss.eaa.mdoc.creation.MdocEAADisclosure;
import eu.europa.esig.dss.enumerations.EAAType;
import eu.europa.esig.dss.utils.Utils;
import eu.europa.esig.dss.ws.eaa.creation.dto.DisclosureDTO;

import java.util.Objects;
import java.util.function.Function;

/**
 * Converts a {@code DisclosureDTO} into {@code EAADisclosure} of a corresponding format
 */
public class DisclosureFromDTOConverter implements Function<DisclosureDTO, EAADisclosure> {

    /** EAA Type */
    private final EAAType eaaType;

    /**
     * Default constructor
     *
     * @param eaaType {@link EAAType} to create a corresponding implementation of disclosures
     */
    public DisclosureFromDTOConverter(final EAAType eaaType) {
        Objects.requireNonNull(eaaType, "eaaType is mandatory!");
        this.eaaType = eaaType;
    }

    @Override
    public EAADisclosure apply(DisclosureDTO disclosureDTO) {
        switch (eaaType) {
            case SD_JWT_VC:
                return new SDJWTEAADisclosure(disclosureDTO.getValue());
            case ISO_IEC_MDOC:
                return new MdocEAADisclosure(disclosureDTO.getNamespace(), disclosureDTO.getDigestId(), Utils.fromBase64(disclosureDTO.getValue()));
            default:
                throw new UnsupportedOperationException(String.format("The EAA Type '%s' is not supported!", eaaType));
        }
    }

}
