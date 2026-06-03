package eu.europa.esig.dss.eaa.jwt.creation;

import eu.europa.esig.dss.eaa.common.creation.AbstractEAAPayloadParameters;

/**
 * Provides configuration for the SD-JWT VC payload creation
 *
 */
public class SDJWTEAAPayloadParameters extends AbstractEAAPayloadParameters {

    /** EAA issuer subject */
    private String issuer;

    /** EAA subject */
    private String subject;

    /** Catalogue of parameters to be made selectively disclosable */
    private final SDJWTClaimParameters selectivelyDisclosableParameters = new SDJWTClaimParameters();

    /** Catalogue of parameters to be made non-selectively disclosable */
    private final SDJWTClaimParameters nonSelectivelyDisclosableParameters = new SDJWTClaimParameters();

    /**
     * Default constructor to instantiate SD-JWT VC Payload parameters
     */
    public SDJWTEAAPayloadParameters() {
        // empty
    }

    /**
     * Gets the EAA issuer subject
     *
     * @return {@link String}
     */
    public String getIssuer() {
        return issuer;
    }

    /**
     * Sets the EAA issue subject
     *
     * @param issuer {@link String}
     */
    public void setIssuer(final String issuer) {
        this.issuer = issuer;
    }

    /**
     * Gets the EAA subject
     *
     * @return {@link String}
     */
    public String getSubject() {
        return subject;
    }

    /**
     * Sets the EAA subject
     *
     * @param subject {@link String}
     */
    public void setSubject(final String subject) {
        this.subject = subject;
    }

    /**
     * Gets a catalogue of claims to be made selectively disclosable within the produced SD-JWT VC EAA.
     * When parameters are defined within the object, the computed hashes will be computed and
     * incorporated within "_sd" header parameter of the EAA Payload.
     * To provide the plain values on presentation, the disclosures shall be generated.
     *
     * @return {@link SDJWTClaimParameters}
     */
    public SDJWTClaimParameters selectivelyDisclosable() {
        return selectivelyDisclosableParameters;
    }

    /**
     * Gets a catalogue of claims to be mase non-selectively disclosable and
     * thus to be included within the SD-JWT VC EAA Payload in the plain form.
     *
     * @return {@link SDJWTClaimParameters}
     */
    public SDJWTClaimParameters nonSelectivelyDisclosable() {
        return nonSelectivelyDisclosableParameters;
    }

    @Override
    public String toString() {
        return "SDJWTEAAPayloadParameters [" +
                "issuer='" + issuer + '\'' +
                ", subject='" + subject + '\'' +
                ", selectivelyDisclosableParameters=" + selectivelyDisclosableParameters +
                ", nonSelectivelyDisclosableParameters=" + nonSelectivelyDisclosableParameters +
                "] " + super.toString();
    }

}
