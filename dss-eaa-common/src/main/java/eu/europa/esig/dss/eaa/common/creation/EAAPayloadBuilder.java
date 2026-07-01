package eu.europa.esig.dss.eaa.common.creation;

import eu.europa.esig.dss.model.DSSDocument;

import java.util.List;

/**
 * Builds the EAA payload according to the provided configuration
 *
 * @param <P> implementation of {@link EAAPayloadParameters} for the EAA format
 * @param <D> implementation of {@link EAADisclosure} for the EAA format
 */
public interface EAAPayloadBuilder<P extends EAAPayloadParameters, D extends EAADisclosure> {

    /**
     * Builds the EAA payload to be signed
     *
     * @param payloadParameters {@link EAAPayloadParameters}
     * @return {@link DSSDocument}
     */
    DSSDocument buildPayload(P payloadParameters);

    /**
     * Builds a list of selectively disclosable EAA claims to be used for Digest computation, format specific
     *
     * @param payloadParameters {@link EAAPayloadParameters}
     * @return {@link EAADisclosure} representing the disclosure structure
     */
    List<D> buildDisclosures(P payloadParameters);

}
