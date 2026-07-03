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
package eu.europa.esig.dss.validation.eaa;

import eu.europa.esig.dss.spi.eaa.EAAPresentation;
import eu.europa.esig.dss.spi.eaa.EAAValidationParameters;
import eu.europa.esig.dss.spi.eaa.status.EAARevocationSource;
import eu.europa.esig.dss.validation.DocumentValidator;

/**
 * This class is used to validate an Electronic Attestation of Attributes presentation
 *
 */
public interface EAAPresentationValidator extends DocumentValidator {

    /**
     * Gets EAAPresentation created from the provided document on validation
     *
     * @return {@link EAAPresentation}
     */
    EAAPresentation getEAAPresentation();

    /**
     * Sets the EAA revocation source providing access to the information about the EAA validity status
     *
     * @param eaaRevocationSource {@link EAARevocationSource}
     */
    void setEAARevocationSource(EAARevocationSource eaaRevocationSource);

    /**
     * Sets supplementary data requiring for validation of EAA presentation (format specific)
     *
     * @param eaaValidationParameters {@link EAAValidationParameters}
     */
    void setEAAValidationParameters(EAAValidationParameters eaaValidationParameters);

}
