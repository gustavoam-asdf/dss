/**
 * DSS - Digital Signature Services
 * Copyright (C) 2015 European Commission, provided under the CEF programme
 * <p>
 * This file is part of the "DSS - Digital Signature Services" project.
 * <p>
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 * <p>
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 * <p>
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301  USA
 */
package eu.europa.esig.dss.eaa.revocation.validation.statuslist;

import eu.europa.esig.dss.eaa.revocation.validation.EAARevocationValidator;
import eu.europa.esig.dss.eaa.revocation.x509.EAARevocationListCertificateSource;
import eu.europa.esig.dss.model.DSSException;
import eu.europa.esig.dss.model.eaa.claim.ClaimNumber;
import eu.europa.esig.dss.model.eaa.claim.ClaimString;
import eu.europa.esig.dss.spi.eaa.EAA;
import eu.europa.esig.dss.spi.eaa.EAARevocationToken;
import eu.europa.esig.dss.spi.x509.TokenCertificateSource;
import eu.europa.esig.dss.utils.Utils;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.ServiceLoader;

/**
 * Performs validation of the EAA status using the Token Status List mechanism, as defined in
 * <a href="https://www.ietf.org/archive/id/draft-ietf-oauth-status-list-20.html">IETF Token Status List (TSL)</a>.
 *
 */
public class IETFEAAStatusListValidator implements EAARevocationValidator {

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
    public List<String> getUris(EAA eaa) {
        if (!isSupported(eaa)) {
            throw new UnsupportedOperationException("The provided EAA token does not contain 'status_list' or not supported!");
        }
        ClaimString uriClaim = eaa.getPayload().getStatus().getStatusList().getUri();
        if (uriClaim != null && Utils.isStringNotEmpty(uriClaim.getStringValue())) {
            return Collections.singletonList(uriClaim.getStringValue());
        } else {
            throw new DSSException("No 'uri' claim is present for the 'status_list' claim!");
        }
    }

    @Override
    public EAARevocationToken validate(EAA eaa, byte[] statusListDocument) {
        if (!isSupported(eaa)) {
            throw new UnsupportedOperationException("The provided EAA token does not contain 'status_list' or not supported!");
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
                        EAARevocationToken statusToken = statusListValidator.getRevocationToken(eaaIndex);
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
        return new EAARevocationListCertificateSource(eaa.getPayload().getStatus().getStatusList());
    }

}
