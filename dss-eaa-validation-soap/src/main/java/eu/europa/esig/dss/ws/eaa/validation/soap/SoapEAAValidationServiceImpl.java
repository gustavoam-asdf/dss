package eu.europa.esig.dss.ws.eaa.validation.soap;

import eu.europa.esig.dss.ws.eaa.validation.common.RemoteEAAValidationService;
import eu.europa.esig.dss.ws.eaa.validation.dto.EAAToValidateDTO;
import eu.europa.esig.dss.ws.eaa.validation.soap.client.SoapEAAValidationService;
import eu.europa.esig.dss.ws.validation.dto.WSReportsDTO;

/**
 * SOAP implementation of the EAA validation service
 *
 */
public class SoapEAAValidationServiceImpl implements SoapEAAValidationService {

    private static final long serialVersionUID = 131175555211631990L;

    /** The validation service to use */
    private RemoteEAAValidationService validationService;

    /**
     * Default construction instantiating object with null RemoteDocumentValidationService
     */
    public SoapEAAValidationServiceImpl() {
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
