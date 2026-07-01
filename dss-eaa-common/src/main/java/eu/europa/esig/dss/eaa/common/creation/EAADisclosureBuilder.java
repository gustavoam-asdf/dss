package eu.europa.esig.dss.eaa.common.creation;

import eu.europa.esig.dss.eaa.common.creation.claim.EAAClaim;

/**
 * Builds selective disclosure objects for the given EAA format
 *
 * @param <C> The implementation of {@link EAAClaim} for the EAA format
 * @param <D> The implementation of {@link EAADisclosure} for the EAA format
 */
public interface EAADisclosureBuilder<C extends EAAClaim, D extends EAADisclosure> {

    /**
     * Builds a selectively disclosable EAA claim to be used for Digest computation, format specific
     *
     * @param claim {@link EAAClaim} to create a disclosure for
     * @return {@link EAADisclosure} representing the disclosure structure
     */
    D build(C claim);

}
