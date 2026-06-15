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
