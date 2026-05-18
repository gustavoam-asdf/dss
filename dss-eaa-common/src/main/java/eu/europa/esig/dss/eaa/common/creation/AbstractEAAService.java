package eu.europa.esig.dss.eaa.common.creation;

import eu.europa.esig.dss.model.SerializableSignatureParameters;
import eu.europa.esig.dss.spi.validation.CertificateVerifier;

import java.util.Objects;

/**
 * Abstract implementation of an EAA creation service.
 *
 */
public abstract class AbstractEAAService<SP extends SerializableSignatureParameters, B extends EAAPayloadBuilder> implements EAAService<SP, B> {

    private static final long serialVersionUID = -8272997238108493534L;

    /** CertificateVerifier used to provide configuration on the validation of the signing certificate and its chain */
    protected final CertificateVerifier certificateVerifier;

    /**
     * Default constructor
     *
     * @param certificateVerifier {@link CertificateVerifier}
     */
    protected AbstractEAAService(final CertificateVerifier certificateVerifier) {
        Objects.requireNonNull(certificateVerifier, "CertificateVerifier cannot be null !");
        this.certificateVerifier = certificateVerifier;
    }

}
