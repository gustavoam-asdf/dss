package eu.europa.esig.dss.cbades.signature;

import eu.europa.esig.dss.cbades.cbor.CBORArray;
import eu.europa.esig.dss.cbades.cbor.CBORUtils;
import eu.europa.esig.dss.cbades.validation.CBAdESSignature;
import eu.europa.esig.dss.signature.SignatureExtension;
import eu.europa.esig.dss.spi.exception.IllegalInputException;

/**
 * The abstract class contains common methods for allowing the signature extension
 * 
 */
public abstract class CBAdESExtensionBuilder implements SignatureExtension<CBAdESSignatureParameters> {

    /**
     * Default constructor
     */
    protected CBAdESExtensionBuilder() {
        // empty
    }

    /**
     * Checks if the uHeaders components are represented by CBOR byte strings
     *
     * @param signature {@link CBAdESSignature} to check
     */
    protected void assertUHeadersComponentsConsistent(CBAdESSignature signature) {
        CBORArray uHeaders = signature.getCoseSignature().getUHeaders();
        if (uHeaders != null && !uHeaders.isEmpty()) {
            if (!CBORUtils.areAllCborBtsrComponents(uHeaders)) {
                throw new IllegalInputException("Extension is not possible! " +
                        "The members of 'uHeaders' component shall be represented by CBOR byte strings.");
            }
        }
    }
    
}
