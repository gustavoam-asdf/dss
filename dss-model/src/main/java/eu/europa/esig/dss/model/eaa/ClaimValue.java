package eu.europa.esig.dss.model.eaa;

import java.io.Serializable;

/**
 * Represents a value of a provided selective disclosure
 *
 */
public interface ClaimValue extends Serializable {

    /**
     * Converts the disclosure value to a corresponding string representation
     *
     * @return {@link String}
     */
    String toString();

}
