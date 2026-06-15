package eu.europa.esig.dss.ws.eaa.validation.rest.client;


import eu.europa.esig.dss.ws.eaa.validation.dto.EAAToValidateDTO;
import eu.europa.esig.dss.ws.validation.dto.WSReportsDTO;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

import java.io.Serializable;

/**
 * This REST interface provides operations for the validation of EAA presentation.
 *
 */
@Path("/")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public interface RestEAAValidationService extends Serializable {

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
    @POST
    @Path("validateEAA")
    WSReportsDTO validateEAA(EAAToValidateDTO dataToValidate);

}
