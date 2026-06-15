package eu.europa.esig.dss.ws.eaa.validation.common;

import eu.europa.esig.dss.eaa.mdoc.validation.MdocValidationParameters;
import eu.europa.esig.dss.ws.converter.RemoteDocumentConverter;
import eu.europa.esig.dss.ws.dto.RemoteDocument;

/**
 * Builds EAA validation parameters for Mdoc validation
 *
 */
public class RemoteMdocValidationParametersBuilder {

    /** Document containing SessionTranscript structure */
    private RemoteDocument sessionTranscript;

    /**
     * Default constructor
     */
    public RemoteMdocValidationParametersBuilder() {
        // empty
    }

    /**
     * Sets SessionTranscript
     *
     * @param sessionTranscript {@link RemoteDocument}
     * @return this {@link RemoteMdocValidationParametersBuilder}
     */
    public RemoteMdocValidationParametersBuilder setSessionTranscript(RemoteDocument sessionTranscript) {
        this.sessionTranscript = sessionTranscript;
        return this;
    }

    /**
     * Builds the MdocValidationParameters
     *
     * @return {@link MdocValidationParameters}
     */
    public MdocValidationParameters build() {
        final MdocValidationParameters mdocValidationParameters = new MdocValidationParameters();
        if (sessionTranscript != null) {
            mdocValidationParameters.setSessionTranscript(RemoteDocumentConverter.toDSSDocument(sessionTranscript));
        }
        return mdocValidationParameters;
    }

}
