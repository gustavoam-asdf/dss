package eu.europa.esig.dss.eaa.common.validation;

import eu.europa.esig.dss.model.DSSDocument;
import eu.europa.esig.dss.spi.eaa.EAAPresentation;
import eu.europa.esig.dss.spi.signature.AdvancedSignature;
import eu.europa.esig.dss.spi.validation.CertificateVerifier;
import eu.europa.esig.dss.spi.validation.ValidationContext;
import eu.europa.esig.dss.spi.validation.analyzer.DefaultDocumentAnalyzer;
import eu.europa.esig.dss.spi.validation.analyzer.eaa.EAAPresentationAnalyzer;
import eu.europa.esig.dss.spi.validation.analyzer.eaa.EAAPresentationAnalyzerFactory;
import eu.europa.esig.dss.spi.x509.CertificateSource;
import eu.europa.esig.dss.spi.x509.ListCertificateSource;
import eu.europa.esig.dss.spi.x509.ProofOfPossessionCertificateSource;
import eu.europa.esig.dss.spi.x509.evidencerecord.EvidenceRecord;
import eu.europa.esig.dss.spi.x509.tsp.TimestampToken;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

/**
 * Abstract class containing common code for validation of presentations of Electronic Attestation of Attributes.
 * This class can be used as the first point of the EAA presentation validation.
 *
 */
public abstract class DefaultEAAPresentationAnalyzer extends DefaultDocumentAnalyzer implements EAAPresentationAnalyzer {

    /** Cached list of presentations of Electronic Attestation of Attributes */
    private List<EAAPresentation> eaaPresentations;

    /**
     * Empty constructor
     */
    protected DefaultEAAPresentationAnalyzer() {
        // empty
    }

    /**
     * Instantiates the class with a document to be validated
     *
     * @param document {@link DSSDocument} to be validated
     */
    protected DefaultEAAPresentationAnalyzer(DSSDocument document) {
        Objects.requireNonNull(document, "Document to be validated cannot be null!");
        this.document = document;
    }

    /**
     * This method guesses the document format and returns an appropriate EAA presentation reader.
     *
     * @param dssDocument
     *            The instance of {@code DSSDocument} to validate
     * @return returns the specific instance of {@link EAAPresentationAnalyzer} in terms of the document type
     */
    public static EAAPresentationAnalyzer fromDocument(final DSSDocument dssDocument) {
        return EAAPresentationAnalyzerFactory.fromDocument(dssDocument);
    }

    @Override
    public List<EAAPresentation> getEAAPresentations() {
        if (eaaPresentations == null) {
            eaaPresentations = buildEAAPresentations();
            // TODO : scopes ?
        }
        return eaaPresentations;
    }

    /**
     * Builds a list of presentation of Electronic Attestation of Attributes objects
     *
     * @return a list of {@link EAAPresentation}s
     */
    protected abstract List<EAAPresentation> buildEAAPresentations();

    @Override
    protected List<AdvancedSignature> getAllSignatures() {
        List<EAAPresentation> presentations = getEAAPresentations();

        final List<AdvancedSignature> result = new ArrayList<>();
        for (EAAPresentation presentation : presentations) {
            result.addAll(presentation.getSignatures());
            if (presentation.getKeyBindingSignature() != null) {
                result.add(presentation.getKeyBindingSignature());
            }
        }
        return result;
    }

    @Override
    protected <T extends AdvancedSignature> ValidationContext prepareValidationContext(
            Collection<T> signatures, Collection<TimestampToken> detachedTimestamps,
            Collection<EvidenceRecord> detachedEvidenceRecords, CertificateVerifier certificateVerifier) {
        ValidationContext validationContext = super.prepareValidationContext(signatures, detachedTimestamps, detachedEvidenceRecords, certificateVerifier);
        for (EAAPresentation eaaPresentation : getEAAPresentations()) {
            CertificateSource deviceKeyCertificateSource = getDeviceKeyCertificateSource(eaaPresentation);
            if (deviceKeyCertificateSource != null) {
                validationContext.addDocumentCertificateSource(deviceKeyCertificateSource);
            }
        }
        return validationContext;
    }

    private CertificateSource getDeviceKeyCertificateSource(EAAPresentation eaaPresentation) {
        AdvancedSignature keyBindingSignature = eaaPresentation.getKeyBindingSignature();
        if (keyBindingSignature != null) {
            return getProofOfPossessionCertificateSource(keyBindingSignature.getSigningCertificateSource());
        }
        return null;
    }

    private CertificateSource getProofOfPossessionCertificateSource(CertificateSource certificateSource) {
        if (certificateSource instanceof ProofOfPossessionCertificateSource) {
            return certificateSource;
        } else if (certificateSource instanceof ListCertificateSource) {
            for (CertificateSource embeddedCertSource : ((ListCertificateSource) certificateSource).getSources()) {
                CertificateSource popCertificateSource = getProofOfPossessionCertificateSource(embeddedCertSource);
                if (popCertificateSource != null) {
                    return popCertificateSource;
                }
            }
        }
        return null;
    }

    @Override
    public List<DSSDocument> getOriginalDocuments(String signatureId) {
        throw new UnsupportedOperationException("getOriginalDocuments(String signatureId) is " +
                "not supported for DefaultEAAPresentationAnalyzer!");
    }

    @Override
    public List<DSSDocument> getOriginalDocuments(AdvancedSignature advancedSignature) {
        throw new UnsupportedOperationException("getOriginalDocuments(AdvancedSignature advancedSignature) is " +
                "not supported for DefaultEAAPresentationAnalyzer!");
    }

}
