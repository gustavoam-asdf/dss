package eu.europa.esig.dss.eaa.common.creation;

import eu.europa.esig.dss.enumerations.DigestAlgorithm;
import eu.europa.esig.dss.model.Digest;

import java.io.Serializable;

/**
 * Represents a selective disclosure object to be provided within an EAA Presentation
 *
 */
public interface EAADisclosure extends Serializable {

    /**
     * Gets digest value of the for the {@code DigestAlgorithm}
     *
     * @param digestAlgorithm {@link DigestAlgorithm} to be used to compute digest with
     * @return {@link Digest}
     */
    Digest getDigest(DigestAlgorithm digestAlgorithm);

}
