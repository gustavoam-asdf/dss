package eu.europa.esig.dss.ws.eaa.creation.common.converter;

import eu.europa.esig.dss.eaa.common.creation.claim.EAAClaim;
import eu.europa.esig.dss.ws.eaa.creation.dto.parameters.ClaimDTO;
import eu.europa.esig.dss.ws.eaa.creation.dto.parameters.ClaimValueDTO;

import java.util.Objects;
import java.util.function.Function;

/**
 * Abstract implementation of a claim DTO into {@code EAAClaim} converter
 *
 * @param <C> {@link EAAClaim}
 */
public abstract class AbstractEAAClaimFromDTOConverter<C extends EAAClaim> implements Function<ClaimDTO, C> {

    /**
     * Default constructor
     */
    protected AbstractEAAClaimFromDTOConverter() {
        super();
    }

    /**
     * Checks whether the claim is made selectively disclosable
     *
     * @param claimDTO {@link ClaimDTO}
     * @return whether the claim is selectively disclosable
     */
    protected boolean isSelectivelyDisclosable(ClaimDTO claimDTO) {
        return Boolean.TRUE == claimDTO.getSelectivelyDisclosable();
    }

    /**
     * Verifies validity of the claim value
     *
     * @param claimValueDTO {@link ClaimValueDTO} to verify
     */
    protected void verifyClaimValueDTO(ClaimValueDTO claimValueDTO) {
        Objects.requireNonNull(claimValueDTO, "ClaimValueDTO cannot be null!");
        int definedValues = 0;
        if (claimValueDTO.getStringValue() != null) ++definedValues;
        if (claimValueDTO.getNumberValue() != null) ++definedValues;
        if (claimValueDTO.getBinaryValue() != null) ++definedValues;
        if (claimValueDTO.getBooleanValue() != null) ++definedValues;
        if (claimValueDTO.getDateValue() != null) ++definedValues;
        if (claimValueDTO.getArrayValue() != null) ++definedValues;
        if (claimValueDTO.getObjectValue() != null) ++definedValues;
        if (definedValues > 1) {
            throw new IllegalArgumentException("More than one data value is provided for a claim!");
        }
    }

}
