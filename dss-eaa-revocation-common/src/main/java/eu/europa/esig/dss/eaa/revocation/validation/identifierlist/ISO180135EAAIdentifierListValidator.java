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
package eu.europa.esig.dss.eaa.revocation.validation.identifierlist;

import eu.europa.esig.dss.eaa.revocation.validation.EAARevocationValidator;
import eu.europa.esig.dss.eaa.revocation.x509.EAARevocationListCertificateSource;
import eu.europa.esig.dss.model.DSSException;
import eu.europa.esig.dss.model.eaa.claim.ClaimByteString;
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
 * Verifies EAA's revocation status using the Identifier List mechanism as defined
 * in ISO/IEC 18013-5 "12.3.6.4 Identifier list details"
 *
 */
public class ISO180135EAAIdentifierListValidator implements EAARevocationValidator {

    /**
     * Default constructor
     */
    public ISO180135EAAIdentifierListValidator() {
        // empty
    }

    @Override
    public boolean isSupported(EAA eaa) {
        return eaa.getPayload() != null && eaa.getPayload().getStatus() != null && eaa.getPayload().getStatus().getIdentifierList() != null;
    }

    @Override
    public List<String> getUris(EAA eaa) {
        if (!isSupported(eaa)) {
            throw new UnsupportedOperationException("The provided EAA token does not contain 'identifier_list' or not supported!");
        }
        ClaimString uriClaim = eaa.getPayload().getStatus().getIdentifierList().getUri();
        if (uriClaim != null && Utils.isStringNotEmpty(uriClaim.getStringValue())) {
            return Collections.singletonList(uriClaim.getStringValue());
        } else {
            throw new DSSException("No 'uri' claim is present for the 'identifier_list' claim!");
        }
    }

    @Override
    public EAARevocationToken validate(EAA eaa, byte[] identifierListDocument) {
        if (!isSupported(eaa)) {
            throw new UnsupportedOperationException("The provided EAA token does not contain 'identifier_list' or not supported!");
        }
        ClaimByteString identifier = eaa.getPayload().getStatus().getIdentifierList().getIdentifier();
        if (identifier != null && identifier.getBinaryValue() != null) {
            byte[] identifierBytes = identifier.getBinaryValue();

            ServiceLoader<IdentifierListValidatorFactory> loader = ServiceLoader.load(IdentifierListValidatorFactory.class);
            Iterator<IdentifierListValidatorFactory> validatorOptions = loader.iterator();

            if (validatorOptions.hasNext()) {
                for (IdentifierListValidatorFactory factory : loader) {
                    if (factory.isSupported(identifierListDocument)) {
                        IdentifierListValidator identifierListValidator = factory.create(identifierListDocument);
                        EAARevocationToken statusToken = identifierListValidator.getRevocationToken(identifierBytes);
                        statusToken.setCertificateSource(getCertificateSource(eaa));
                        return statusToken;
                    }
                }
            }
            throw new UnsupportedOperationException("Status document format not recognized/handled");

        } else {
            throw new DSSException("No 'id' claim is present for the 'identifier_list' claim!");
        }
    }

    /**
     * Gets the certificate source based on the certificate present within the "status_list" claim, if any
     *
     * @param eaa {@link EAA}
     * @return {@link TokenCertificateSource}
     */
    protected TokenCertificateSource getCertificateSource(EAA eaa) {
        return new EAARevocationListCertificateSource(eaa.getPayload().getStatus().getIdentifierList());
    }

}
