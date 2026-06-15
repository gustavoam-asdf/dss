package eu.europa.esig.dss.ws.eaa.validation.common;

import eu.europa.esig.dss.spi.eaa.EAAValidationParameters;
import eu.europa.esig.dss.ws.eaa.validation.dto.EAAValidationParametersDTO;

/**
 * Builds EAA validation parameters
 *
 */
public class RemoteEAAValidationParametersBuilder {

    /** DTO EAA Validation parameters */
    private final EAAValidationParametersDTO eaaValidationParametersDTO;

    /**
     * Default constructor
     *
     * @param eaaValidationParametersDTO {@link EAAValidationParametersDTO}
     */
    public RemoteEAAValidationParametersBuilder(final EAAValidationParametersDTO eaaValidationParametersDTO) {
        this.eaaValidationParametersDTO = eaaValidationParametersDTO;
    }

    /**
     * Builds the EAA validation parameters from the DTO
     *
     * @return {@link EAAValidationParameters}
     */
    public EAAValidationParameters build() {
        if (eaaValidationParametersDTO != null && eaaValidationParametersDTO.getSessionTranscript() != null) {
            return new RemoteMdocValidationParametersBuilder()
                    .setSessionTranscript(eaaValidationParametersDTO.getSessionTranscript())
                    .build();
        }
        return null;
    }

}
