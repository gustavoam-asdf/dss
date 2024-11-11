package eu.europa.esig.dss.cbades.signature;

import eu.europa.esig.dss.signature.SignatureExtension;
import eu.europa.esig.dss.signature.SigningOperation;

/**
 * CB-AdES augmentation
 *
 */
public interface CBAdESLevelBaselineExtension extends SignatureExtension<CBAdESSignatureParameters> {

    /**
     * Sets the signing operation.
     * NOTE: the internal variable, used in the signature creation/extension process
     *
     * @param signingOperation {@link SigningOperation}
     */
    void setOperationKind(SigningOperation signingOperation);

}
