package eu.europa.esig.dss.eaa.common.creation.claim;

import java.io.Serializable;

/**
 * Base interface for defining a claim
 */
public interface EAAClaim extends Serializable {

    /**
     * Gets the name of the EAA claim
     *
     * @return {@link String}
     */
    String getName();

    /**
     * Gets the value of the EAA claim
     *
     * @return {@link Object}
     */
    Object getValue();

}
