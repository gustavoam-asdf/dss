package eu.europa.esig.dss.eaa.common.creation;

import eu.europa.esig.dss.eaa.common.creation.claim.EAAClaim;
import eu.europa.esig.dss.enumerations.MimeType;
import eu.europa.esig.dss.enumerations.SigningOperation;
import eu.europa.esig.dss.model.DSSDocument;
import eu.europa.esig.dss.model.SerializableSignatureParameters;
import eu.europa.esig.dss.spi.signature.FileNameBuilder;
import eu.europa.esig.dss.spi.validation.CertificateVerifier;

import java.util.Objects;

/**
 * Abstract implementation of an EAA creation service.
 *
 * @param <SP>
 *         implementation of signature parameters corresponding to the supported signature format
 * @param <B>
 *         implementation of EAA payload parameters to the EAA format
 * @param <C>
 *         implementation of EAA Claim for the EAA format
 * @param <D>
 *         implementation of EAA disclosure for the EAA format
 * @param <E>
 *         implementation of EAA key binding parameters for the EAA format
 */
public abstract class AbstractEAAService<SP extends SerializableSignatureParameters, B extends EAAPayloadParameters, C extends EAAClaim, D extends EAADisclosure, E extends KeyBindingParameters> implements EAAService<SP, B, D, E> {

    private static final long serialVersionUID = -8272997238108493534L;

    /** CertificateVerifier used to provide configuration on the validation of the signing certificate and its chain */
    protected final CertificateVerifier certificateVerifier;

    /** Builds the EAA payload */
    protected EAAPayloadBuilder<B, C, D> payloadBuilder;

    /**
     * Default constructor
     *
     * @param certificateVerifier {@link CertificateVerifier}
     */
    protected AbstractEAAService(final CertificateVerifier certificateVerifier) {
        Objects.requireNonNull(certificateVerifier, "CertificateVerifier cannot be null !");
        this.certificateVerifier = certificateVerifier;
    }

    /**
     * Gets the payload builder. If not set, instantiates a default value (format specific)
     *
     * @return {@link EAAPayloadBuilder}
     */
    protected EAAPayloadBuilder<B, C, D> getPayloadBuilder() {
        if (payloadBuilder == null) {
            payloadBuilder = initDefaultPayloadBuilder();
        }
        return payloadBuilder;
    }

    /**
     * Instantiates a default {@code EAAPayloadBuilder} implementation
     *
     * @return {@link EAAPayloadBuilder}
     */
    protected abstract EAAPayloadBuilder<B, C, D> initDefaultPayloadBuilder();

    /**
     * Sets the builder used to create an EAA Payload based on the input parameters.
     * Default : provided format specific implementation is used by default
     *
     * @param payloadBuilder {@link EAAPayloadBuilder}
     */
    public void setPayloadBuilder(EAAPayloadBuilder<B, C, D> payloadBuilder) {
        Objects.requireNonNull(payloadBuilder, "EAAPayloadBuilder cannot be null!");
        this.payloadBuilder = payloadBuilder;
    }

    /**
     * Gets the final document name when original document is present
     *
     * @param originalFile {@link DSSDocument} original document
     * @return {@link String}
     */
    protected String getFinalDocumentName(DSSDocument originalFile) {
        return getFinalDocumentNameBuilder().setOriginalFilename(originalFile.getName()).build();
    }

    /**
     * Gets the final document name when original document is present
     *
     * @return {@link String}
     */
    protected FileNameBuilder getFinalDocumentNameBuilder() {
        return new FileNameBuilder().setSigningOperation(SigningOperation.EAA_PRESENTATION).setMimeType(getEAAPresentationMimeType());
    }

    /**
     * Gets the MimeType of the EAA Presentation for the given EAA format
     *
     * @return {@link MimeType}
     */
    protected abstract MimeType getEAAPresentationMimeType();

}
