package eu.europa.esig.dss.eaa.common.creation;

import eu.europa.esig.dss.eaa.common.creation.claim.EAAClaim;
import eu.europa.esig.dss.model.SerializableSignatureParameters;
import eu.europa.esig.dss.spi.validation.CertificateVerifier;

import java.util.Objects;

/**
 * Abstract implementation of an EAA creation service.
 *
 */
public abstract class AbstractEAAService<SP extends SerializableSignatureParameters, B extends EAAPayloadParameters, C extends EAAClaim, D extends EAADisclosure> implements EAAService<SP, B, C, D> {

    private static final long serialVersionUID = -8272997238108493534L;

    /** CertificateVerifier used to provide configuration on the validation of the signing certificate and its chain */
    protected final CertificateVerifier certificateVerifier;

    /** Builds the EAA payload */
    protected EAAPayloadBuilder<B, C, D> payloadBuilder;

    /** Builds the EAA payload */
    protected EAADisclosureBuilder<C, D> disclosureBuilder;

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

}
