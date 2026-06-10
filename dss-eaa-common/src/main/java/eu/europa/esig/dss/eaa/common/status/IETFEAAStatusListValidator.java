package eu.europa.esig.dss.eaa.common.status;

import eu.europa.esig.dss.model.DSSException;
import eu.europa.esig.dss.model.eaa.claim.ClaimNumber;
import eu.europa.esig.dss.model.eaa.claim.ClaimString;
import eu.europa.esig.dss.spi.eaa.EAA;
import eu.europa.esig.dss.spi.eaa.EAAStatusToken;
import eu.europa.esig.dss.spi.eaa.EAAStatusValidator;
import eu.europa.esig.dss.spi.eaa.statuslist.StatusListValidator;
import eu.europa.esig.dss.spi.eaa.statuslist.StatusListValidatorFactory;
import eu.europa.esig.dss.spi.x509.TokenCertificateSource;
import eu.europa.esig.dss.utils.Utils;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.ServiceLoader;

/**
 * Performs validation of the EAA status using the Token Status List mechanism, as defined in
 * {@link <a href="https://www.ietf.org/archive/id/draft-ietf-oauth-status-list-20.html">IETF Token Status List (TSL)</a>}.
 *
 */
public class IETFEAAStatusListValidator implements EAAStatusValidator {

    /**
     * Default constructor
     */
    public IETFEAAStatusListValidator() {
        // empty
    }

    @Override
    public boolean isSupported(EAA eaa) {
        return eaa.getPayload() != null && eaa.getPayload().getStatus() != null && eaa.getPayload().getStatus().getStatusList() != null;
    }

    @Override
    public List<String> getStatusUris(EAA eaa) {
        if (isSupported(eaa)) {
            ClaimString uriClaim = eaa.getPayload().getStatus().getStatusList().getUri();
            if (uriClaim != null && Utils.isStringNotEmpty(uriClaim.getStringValue())) {
                return Collections.singletonList(uriClaim.getStringValue());
            } else {
                throw new DSSException("No 'uri' claim is present for the 'status_list' claim!");
            }
        }
        return Collections.emptyList();
    }

    @Override
    public EAAStatusToken validate(EAA eaa, byte[] statusListDocument) {
        if (!isSupported(eaa)) {
            throw new IllegalStateException("The provided EAA token does not contain 'status_list' or not supported!");
        }
        ClaimNumber indexClaim = eaa.getPayload().getStatus().getStatusList().getIndex();
        if (indexClaim != null && indexClaim.getNumberValue() != null) {
            int eaaIndex = indexClaim.getNumberValue().intValue();

            ServiceLoader<StatusListValidatorFactory> loader = ServiceLoader.load(StatusListValidatorFactory.class);
            Iterator<StatusListValidatorFactory> validatorOptions = loader.iterator();

            if (validatorOptions.hasNext()) {
                for (StatusListValidatorFactory factory : loader) {
                    if (factory.isSupported(statusListDocument)) {
                        StatusListValidator statusListValidator = factory.create(statusListDocument);
                        EAAStatusToken statusToken = statusListValidator.getStatusToken(eaaIndex);
                        statusToken.setCertificateSource(getCertificateSource(eaa));
                        return statusToken;
                    }
                }
            }
            throw new UnsupportedOperationException("Status document format not recognized/handled");

        } else {
            throw new DSSException("No 'idx' claim is present for the 'status_list' claim!");
        }
    }

    /**
     * Gets the certificate source based on the certificate present within the "status_list" claim, if any
     *
     * @param eaa {@link EAA}
     * @return {@link TokenCertificateSource}
     */
    protected TokenCertificateSource getCertificateSource(EAA eaa) {
        return new EAAStatusListCertificateSource(eaa.getPayload().getStatus().getStatusList());
    }

}
