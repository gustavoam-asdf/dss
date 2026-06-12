package eu.europa.esig.dss.eaa.revocation.validation.statuslist;

import eu.europa.esig.dss.eaa.revocation.validation.EAARevocationValidator;
import eu.europa.esig.dss.model.DSSException;
import eu.europa.esig.dss.model.eaa.claim.ClaimNumber;
import eu.europa.esig.dss.model.eaa.claim.ClaimString;
import eu.europa.esig.dss.spi.eaa.EAA;
import eu.europa.esig.dss.spi.eaa.EAARevocationToken;
import eu.europa.esig.dss.utils.Utils;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.ServiceLoader;

/**
 * Validates EAA status, with a declared structure as defined in ETSI TS 119 472-1 v1.2.1 "5.2.10 EAA status service".
 *
 */
public class ETSI194721V121EAAStatusListValidator implements EAARevocationValidator {

    /** Token Status List as specified in IETF draft-ietf-oauth-status-list-13 */
    private static final String TOKEN_STATUS_LIST = "TokenStatusList";

    /**
     * Default constructor
     */
    public ETSI194721V121EAAStatusListValidator() {
        // empty
    }

    @Override
    public boolean isSupported(EAA eaa) {
        return eaa.getPayload() != null && eaa.getPayload().getStatus() != null
                && eaa.getPayload().getStatus().getType() != null
                && TOKEN_STATUS_LIST.equals(eaa.getPayload().getStatus().getType().getStringValue());
    }

    @Override
    public List<String> getUris(EAA eaa) {
        if (isSupported(eaa)) {
            ClaimString uriClaim = eaa.getPayload().getStatus().getUri();
            if (uriClaim != null && Utils.isStringNotEmpty(uriClaim.getStringValue())) {
                return Collections.singletonList(uriClaim.getStringValue());
            } else {
                throw new DSSException("No 'uri' claim is present for the 'TokenStatusList' claim!");
            }
        }
        return Collections.emptyList();
    }

    @Override
    public EAARevocationToken validate(EAA eaa, byte[] statusListDocument) {
        if (!isSupported(eaa)) {
            throw new IllegalStateException("The provided EAA token does not contain 'status' or not supported!");
        }
        ClaimNumber indexClaim = eaa.getPayload().getStatus().getIndex();
        if (indexClaim != null && indexClaim.getNumberValue() != null) {
            int eaaIndex = indexClaim.getNumberValue().intValue();

            ServiceLoader<StatusListValidatorFactory> loader = ServiceLoader.load(StatusListValidatorFactory.class);
            Iterator<StatusListValidatorFactory> validatorOptions = loader.iterator();

            if (validatorOptions.hasNext()) {
                for (StatusListValidatorFactory factory : loader) {
                    if (factory.isSupported(statusListDocument)) {
                        StatusListValidator statusListValidator = factory.create(statusListDocument);
                        return statusListValidator.getRevocationToken(eaaIndex);
                    }
                }
            }
            throw new UnsupportedOperationException("Status document format not recognized/handled");

        } else {
            throw new DSSException("No 'index' claim is present for the 'status' claim!");
        }
    }

}
