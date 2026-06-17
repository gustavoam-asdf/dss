package eu.europa.esig.dss.ws.eaa.creation.common.converter;

import eu.europa.esig.dss.eaa.jwt.creation.SDJWTEAAClaim;
import eu.europa.esig.dss.eaa.jwt.creation.SDJWTEAAClaimArray;
import eu.europa.esig.dss.eaa.jwt.creation.SDJWTEAAClaimObject;
import eu.europa.esig.dss.ws.eaa.creation.dto.parameters.ClaimDTO;
import eu.europa.esig.dss.ws.eaa.creation.dto.parameters.ClaimValueDTO;

import java.util.Base64;
import java.util.Objects;

/**
 * Converts a {@code ClaimDTO} into an {@code SDJWTEAAClaim}
 *
 */
public class SDJWTEAAClaimFromDTOConverter extends AbstractEAAClaimFromDTOConverter<SDJWTEAAClaim> {

    /**
     * Default constructor
     */
    public SDJWTEAAClaimFromDTOConverter() {
        super();
    }

    @Override
    public SDJWTEAAClaim apply(ClaimDTO claimDTO) {
        Objects.requireNonNull(claimDTO, "ClaimDTO cannot be null!");
        verifyClaimValueDTO(claimDTO.getValue());

        ClaimValueDTO claimValue = claimDTO.getValue();
        if (claimValue.getArrayValue() != null) {
            SDJWTEAAClaimArray sdjwtClaimArray;
            if (isSelectivelyDisclosable(claimDTO)) {
                sdjwtClaimArray = SDJWTEAAClaim.createArraySelectivelyDisclosableWithSalt(claimDTO.getName(), getSalt(claimDTO.getSalt()));
            } else {
                sdjwtClaimArray = SDJWTEAAClaim.createArray(claimDTO.getName());
            }
            claimValue.getArrayValue().forEach(c -> sdjwtClaimArray.addElement(apply(c)));
            return sdjwtClaimArray;

        } else if (claimValue.getObjectValue() != null) {
            SDJWTEAAClaimObject sdjwtClaimObject;
            if (isSelectivelyDisclosable(claimDTO)) {
                sdjwtClaimObject = SDJWTEAAClaim.createObjectSelectivelyDisclosableWithSalt(claimDTO.getName(), getSalt(claimDTO.getSalt()));
            } else {
                sdjwtClaimObject = SDJWTEAAClaim.createObject(claimDTO.getName());
            }
            claimValue.getObjectValue().forEach(c -> sdjwtClaimObject.addChild(apply(c)));
            return sdjwtClaimObject;

        } else {
            if (isSelectivelyDisclosable(claimDTO)) {
                return SDJWTEAAClaim.createSelectivelyDisclosableWithSalt(claimDTO.getName(), getValue(claimValue), getSalt(claimDTO.getSalt()));
            } else {
                return SDJWTEAAClaim.create(claimDTO.getName(), getValue(claimValue));
            }
        }
    }

    /**
     * Gets a plain value of the {@code claimValueDTO}
     *
     * @param claimValueDTO {@link ClaimValueDTO}
     * @return {@link Object}
     */
    protected Object getValue(ClaimValueDTO claimValueDTO) {
        if (claimValueDTO.getStringValue() != null) {
            return claimValueDTO.getStringValue();
        } else if (claimValueDTO.getNumberValue() != null) {
            return claimValueDTO.getNumberValue();
        } else if (claimValueDTO.getBooleanValue() != null) {
            return claimValueDTO.getBooleanValue();
        } else if (claimValueDTO.getBinaryValue() != null) {
            return claimValueDTO.getBinaryValue();
        } else if (claimValueDTO.getDateValue() != null) {
            return claimValueDTO.getDateValue();
        }
        return null;
    }

    /**
     * Gets base64url-encoded salt
     *
     * @param salt byte array containing salt
     * @return {@link String}
     */
    protected String getSalt(byte[] salt) {
        if (salt == null) {
            return null;
        }
        return Base64.getUrlEncoder().withoutPadding().encodeToString(salt);
    }

}
