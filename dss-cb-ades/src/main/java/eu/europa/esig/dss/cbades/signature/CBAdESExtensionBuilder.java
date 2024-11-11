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
     * Checks if the type of uHeaders components is consistent
     *
     * @param signature {@link CBAdESSignature} to check
     * @param isCborByteStringWrapperComponents if the new component shall be of CBOR Byte String wrapped incorporation type
     */
    protected void assertUHeadersComponentsConsistent(CBAdESSignature signature, boolean isCborByteStringWrapperComponents) {
        CBORArray uHeaders = signature.getCoseSignature().getUHeaders();
        if (uHeaders != null && !uHeaders.isEmpty()) {
            if (!CBORUtils.checkComponentsUnicity(uHeaders)) {
                throw new IllegalInputException("Extension is not possible, because components of the 'uHeaders' header " +
                        "parameter have different format! Shall be all clear instances or CBOR byte string.");
            }
            if (CBORUtils.areAllCborBtsrComponents(uHeaders) != isCborByteStringWrapperComponents) {
                throw new IllegalInputException(String.format("Extension is not possible! The encoding of 'uHeaders' "
                                + "components shall match! Use cbadesSignatureParameters.setCborByteStringWrapperComponents(%s)",
                        !isCborByteStringWrapperComponents));
            }
        }
    }
    
}
