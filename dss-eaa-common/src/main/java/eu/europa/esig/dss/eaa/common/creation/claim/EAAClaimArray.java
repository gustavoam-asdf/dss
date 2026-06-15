package eu.europa.esig.dss.eaa.common.creation.claim;

import java.util.ArrayList;
import java.util.List;

/**
 * Interface representing an array claim
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

