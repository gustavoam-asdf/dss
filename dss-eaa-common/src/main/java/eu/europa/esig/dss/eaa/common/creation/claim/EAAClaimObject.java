package eu.europa.esig.dss.eaa.common.creation.claim;

import java.util.List;

/**
 * Interface representing an object claim
 *
 * @param <T> The implementation of {@link EAAClaim} for the EAA format
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

