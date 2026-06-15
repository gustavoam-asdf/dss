package eu.europa.esig.dss.cbades.signature;

import eu.europa.esig.dss.enumerations.DigestAlgorithm;
import eu.europa.esig.dss.model.TimestampParameters;

/**
 * The parameters to create a CB-AdES timestamp
 *
 */
public class CBAdESTimestampParameters extends TimestampParameters {

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

}
