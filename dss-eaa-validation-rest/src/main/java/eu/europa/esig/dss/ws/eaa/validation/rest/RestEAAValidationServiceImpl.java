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
package eu.europa.esig.dss.ws.eaa.validation.rest;

import eu.europa.esig.dss.ws.eaa.validation.common.RemoteEAAValidationService;
import eu.europa.esig.dss.ws.eaa.validation.dto.EAAToValidateDTO;
import eu.europa.esig.dss.ws.eaa.validation.rest.client.RestEAAValidationService;
import eu.europa.esig.dss.ws.validation.dto.WSReportsDTO;

/**
 * REST implementation of the EAA validation service
 *
 */
public class RestEAAValidationServiceImpl implements RestEAAValidationService {

    private static final long serialVersionUID = -7198332175850813486L;

    /** The validation service to use */
    private RemoteEAAValidationService validationService;

    /**
     * Default construction instantiating object with null RemoteDocumentValidationService
     */
    public RestEAAValidationServiceImpl() {
        // empty
    }

    /**
     * Default constructor
     *
     * @param validationService {@link RemoteEAAValidationService}
     */
    public void setValidationService(RemoteEAAValidationService validationService) {
        this.validationService = validationService;
    }

    @Override
    public WSReportsDTO validateEAA(EAAToValidateDTO dataToValidate) {
        return validationService.validateEAA(dataToValidate);
    }

}
