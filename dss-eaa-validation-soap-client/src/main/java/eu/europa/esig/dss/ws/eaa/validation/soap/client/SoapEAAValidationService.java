package eu.europa.esig.dss.ws.eaa.validation.soap.client;

import eu.europa.esig.dss.ws.eaa.validation.dto.EAAToValidateDTO;
import eu.europa.esig.dss.ws.validation.dto.WSReportsDTO;
import jakarta.jws.WebParam;
import jakarta.jws.WebResult;
import jakarta.jws.WebService;

import java.io.Serializable;

/**
 * This SOAP interface provides operations for the validation of EAA presentation.
 *
 */
@WebService(targetNamespace = "http://eaa.validation.dss.esig.europa.eu/")
public interface SoapEAAValidationService extends Serializable {

    /**
     * This method returns the result of the validation of the EAA Presentation.
     * The results contain a Diagnostic Data, simple report, detailed report and
     * ETSI Validation report
     *
     * @param dataToValidate
     *                       a {@code EAAToValidateDTO} which contains the
     *                       EAA, the optional validation parameters
     * @return a {@code ReportsDTO} with  4 reports : the diagnostic data, the
     *         detailed report, the simple report and the ETSI validation report
     */
    @WebResult(name = "WSReportsDTO")
    WSReportsDTO validateEAA(@WebParam(name = "dataToValidateDTO") EAAToValidateDTO dataToValidate);

}
