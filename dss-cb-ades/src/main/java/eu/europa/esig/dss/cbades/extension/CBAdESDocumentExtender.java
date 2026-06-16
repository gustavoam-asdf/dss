package eu.europa.esig.dss.cbades.extension;

import eu.europa.esig.dss.cbades.signature.CBAdESService;
import eu.europa.esig.dss.cbades.signature.CBAdESSignatureParameters;
import eu.europa.esig.dss.cbades.signature.CBAdESTimestampParameters;
import eu.europa.esig.dss.cbades.validation.COSEDocumentAnalyzer;
import eu.europa.esig.dss.enumerations.SignatureForm;
import eu.europa.esig.dss.extension.AbstractDocumentExtender;
import eu.europa.esig.dss.model.DSSDocument;
import eu.europa.esig.dss.model.SerializableSignatureParameters;
import eu.europa.esig.dss.signature.DocumentSignatureService;

import java.util.Objects;

/**
 * CB-AdES specific implementation of a {@code eu.europa.esig.dss.spi.augmentation.DocumentExtender}.
 *
 */
public class CBAdESDocumentExtender extends AbstractDocumentExtender<CBAdESSignatureParameters, CBAdESTimestampParameters> {

    /**
     * Empty constructor
     */
    CBAdESDocumentExtender() {
        // empty
    }

    /**
     * Default constructor
     *
     * @param document {@link DSSDocument} to be extended
     */
    public CBAdESDocumentExtender(final DSSDocument document) {
        Objects.requireNonNull(document, "Document to be extended cannot be null!");
        this.document = document;
    }

    @Override
    protected DocumentSignatureService<CBAdESSignatureParameters, CBAdESTimestampParameters> createSignatureService() {
        Objects.requireNonNull(certificateVerifier, "Please provide CertificateVerifier or corresponding CBAdESService!");
        final CBAdESService service = new CBAdESService(certificateVerifier);
        service.setTspSource(tspSource);
        return service;
    }

    @Override
    public boolean isSupported(DSSDocument dssDocument) {
        return new COSEDocumentAnalyzer().isSupported(dssDocument);
    }

    @Override
    protected CBAdESSignatureParameters emptySignatureParameters() {
        return new CBAdESSignatureParameters();
    }

    @Override
    protected boolean isSupportedParameters(SerializableSignatureParameters parameters) {
        return parameters instanceof CBAdESSignatureParameters;
    }

    @Override
    protected boolean isSupportedService(DocumentSignatureService<?, ?> service) {
        return service instanceof CBAdESService;
    }

    @Override
    public SignatureForm getSignatureForm() {
        return SignatureForm.CBAdES;
    }

}
