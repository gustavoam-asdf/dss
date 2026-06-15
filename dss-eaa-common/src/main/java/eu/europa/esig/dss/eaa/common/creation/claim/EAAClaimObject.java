package eu.europa.esig.dss.eaa.common.creation.claim;

import java.util.ArrayList;
import java.util.List;

/**
 * Interface representing an object claim
 */
public interface EAAClaimObject <T extends EAAClaim> extends EAAClaim {

    /**
     * Adds a child claim
     *
     * @param child {@link T}
     */
    void addChild(final T child);

    /**
     * Gets the children of the object
     *
     * @return A list of {@link T}
     */
    List<T> getChildren();

}

