package eu.europa.esig.dss.cbades;

import eu.europa.esig.dss.cbades.validation.CBORSignature;
import eu.europa.esig.dss.model.DSSException;
import eu.europa.esig.dss.spi.x509.SignatureIntegrityValidator;

import java.security.PublicKey;

/**
 * Checks the integrity of a COSE/CB-AdES SignatureValue
 *
 */
public class CBAdESSignatureIntegrityValidator extends SignatureIntegrityValidator {

    /** The COSE signature to validate */
    private final CBORSignature cose;

    /**
     * Default constructor
     *
     * @param cose {@link CBORSignature}
     */
    public CBAdESSignatureIntegrityValidator(final CBORSignature cose) {
        this.cose = cose;
    }

    @Override
    protected boolean verify(PublicKey publicKey) throws DSSException {
        try {
            cose.setKey(publicKey);
            return cose.verifySignature();
        } catch (Exception e) {
            throw new DSSException(e);
        }
    }

}
