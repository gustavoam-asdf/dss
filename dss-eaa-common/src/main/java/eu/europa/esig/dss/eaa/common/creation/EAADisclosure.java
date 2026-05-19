package eu.europa.esig.dss.eaa.common.creation;

import java.io.Serializable;

/**
 * Represents a selective disclosure object to be provided within an EAA Presentation
 *
 */
public interface EAADisclosure extends Serializable {

    /**
     * Gets binaries representing the disclosure for which the hash is going to be computed
     *
     * @return disclosure representation binaries
     */
    byte[] getBytesToBeSigned();

}
