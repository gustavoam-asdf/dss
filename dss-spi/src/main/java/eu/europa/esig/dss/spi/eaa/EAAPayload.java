package eu.europa.esig.dss.spi.eaa;

import java.io.Serializable;

/**
 * Provides an interface for accessing the content of the EAA payload
 */
public interface EAAPayload extends Serializable {

    /**
     * Gets the EAA category URN, when present
     *
     * @return {@link String}
     */
    String getCategory();

}
