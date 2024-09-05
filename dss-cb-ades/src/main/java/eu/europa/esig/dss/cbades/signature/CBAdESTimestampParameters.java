package eu.europa.esig.dss.cbades.signature;

import eu.europa.esig.dss.enumerations.DigestAlgorithm;
import eu.europa.esig.dss.model.TimestampParameters;

/**
 * The parameters to create a CB-AdES timestamp
 *
 */
public class CBAdESTimestampParameters extends TimestampParameters {

    /**
     * The canonicalization method to use for timestamp's message imprint computation
     */
    private String canonicalizationMethod;

    /**
     * Empty constructor
     */
    public CBAdESTimestampParameters() {
        // empty
    }

    /**
     * The default constructor
     *
     * @param digestAlgorithm {@link DigestAlgorithm} to use for a message-imprint calculation
     */
    public CBAdESTimestampParameters(DigestAlgorithm digestAlgorithm) {
        super(digestAlgorithm);
    }

    /**
     * Gets the canonicalization algorithm for the timestamp
     *
     * @return {@link String} canonicalization algorithm
     */
    public String getCanonicalizationMethod() {
        return canonicalizationMethod;
    }

    /**
     * Sets the canonicalization algorithm for the timestamp
     *
     * @param canonicalizationMethod {@link String}
     */
    public void setCanonicalizationMethod(String canonicalizationMethod) {
        throw new UnsupportedOperationException("Canonicalization is not supported in the current version.");
        // TODO : this.canonicalizationMethod = canonicalizationMethod;
    }

}
