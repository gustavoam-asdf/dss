package eu.europa.esig.dss.spi.eaa;

import eu.europa.esig.dss.spi.signature.AdvancedSignature;

import java.math.BigInteger;

/**
 * Represents a key binding content and signature made on an Electronic Attestation of Attributes
 *
 */
public interface EAAKeyBinding {

    /**
     * Returns a name of the key binding document, when present
     *
     * @return {@link String}
     */
    String getFilename();

    /**
     * Gets key binding signature
     *
     * @return {@link AdvancedSignature}
     */
    AdvancedSignature getSignature();

    /**
     * Gets nonce value, when present
     *
     * @return {@link BigInteger}
     */
    BigInteger getNonce();

    /**
     * This method returns the DSS unique id. It allows to unambiguously identify each token.
     *
     * @return {@link String} unique Id
     */
    String getId();

    /**
     * Returns binaries of the key binding
     *
     * @return byte array
     */
    byte[] getEncoded();

}
