package eu.europa.esig.dss.model.eaa.claim;

/**
 * Contains biometric data of an EAA holder.
 * The type of data can be extracted using the {@code #getType} method.
 *
 */
public interface ClaimBiometricTemplateXX extends Claim {

    /**
     * Gets type of the biometric data
     *
     * @return {@link String}
     */
    String getType();

}
