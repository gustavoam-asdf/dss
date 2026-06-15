package eu.europa.esig.dss.eaa.mdoc.validation;

import eu.europa.esig.dss.model.DSSDocument;
import eu.europa.esig.dss.spi.eaa.EAAValidationParameters;

/**
 * Contains supplementary data parameters for validation of Mdoc EAA presentation
 *
 */
public class MdocValidationParameters implements EAAValidationParameters {

    private static final long serialVersionUID = 8075475063597375229L;

    /** Contains transcript of communication used for the device retrieval (mdoc key binding signature) */
    private DSSDocument sessionTranscript;

    /**
     * Default constructor
     */
    public MdocValidationParameters() {
        // empty
    }

    /**
     * Gets the session transcript of communication used for the device retrieval (mdoc key binding signature)
     *
     * @return {@link DSSDocument}
     */
    public DSSDocument getSessionTranscript() {
        return sessionTranscript;
    }

    /**
     * Sets the session transcript of communication used for the device retrieval (mdoc key binding signature)
     *
     * @param sessionTranscript {@link DSSDocument}
     */
    public void setSessionTranscript(DSSDocument sessionTranscript) {
        this.sessionTranscript = sessionTranscript;
    }

}
