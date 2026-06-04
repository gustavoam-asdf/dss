package eu.europa.esig.dss.eaa.common.creation;

/**
 * Generates salt for the selectively disclosable claims within an Electronic Attestation of Attributes
 *
 */
public interface EAASaltGenerator {

    /**
     * Generates next salt
     *
     * @return byte array containing the salt value
     */
    byte[] generateSalt();

}
