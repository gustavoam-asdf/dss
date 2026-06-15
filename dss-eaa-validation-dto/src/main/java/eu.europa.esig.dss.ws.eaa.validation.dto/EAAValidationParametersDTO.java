package eu.europa.esig.dss.ws.eaa.validation.dto;

import eu.europa.esig.dss.ws.dto.RemoteDocument;

/**
 * DTO for supplementary EAA validation data parameters
 */
public class EAAValidationParametersDTO {

    /**
     * Represents SessionTranscript data structure used for generation of Mdoc's deviceAuth (key binding) signature
     */
    private RemoteDocument sessionTranscript;

    /**
     * Empty constructor
     */
    public EAAValidationParametersDTO() {
        // empty
    }

    /**
     * Constructor with SessionTranscript provided
     *
     * @param sessionTranscript {@link RemoteDocument}
     */
    public EAAValidationParametersDTO(RemoteDocument sessionTranscript) {
        this.sessionTranscript = sessionTranscript;
    }

    /**
     * Gets SessionTranscript
     *
     * @return {@link RemoteDocument}
     */
    public RemoteDocument getSessionTranscript() {
        return sessionTranscript;
    }

    /**
     * Sets SessionTranscript data structure used for generation of Mdoc's deviceAuth (key binding) signature
     *
     * @param sessionTranscript {@link RemoteDocument}
     */
    public void setSessionTranscript(RemoteDocument sessionTranscript) {
        this.sessionTranscript = sessionTranscript;
    }

}
