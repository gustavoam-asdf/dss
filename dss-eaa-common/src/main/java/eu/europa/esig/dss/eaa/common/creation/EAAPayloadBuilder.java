package eu.europa.esig.dss.eaa.common.creation;

import eu.europa.esig.dss.model.DSSDocument;

/**
 * Builds the EAA payload according to the provided configuration
 *
 */
public interface EAAPayloadBuilder {

    /**
     * Builds the EAA payload to be signed
     *
     * @return {@link DSSDocument}
     */
    DSSDocument buildPayload();

}
