package eu.europa.esig.dss.eaa.common.creation.claim;

import java.util.List;

/**
 * Interface representing an array claim
 *
 * @param <T> The implementation of {@link EAAClaim} for the EAA format
 */
public interface EAAClaimArray <T extends EAAClaim> extends EAAClaim {

    /**
     * Adds an element to the array
     *
     * @param element {@link T}
     */
    void addElement(final T element);

    /**
     * Gets the elements of the array
     *
     * @return A list of {@link T}
     */
    List<T> getElements();
}

