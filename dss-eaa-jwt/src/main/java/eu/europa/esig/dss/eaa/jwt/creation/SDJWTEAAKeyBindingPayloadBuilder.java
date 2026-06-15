package eu.europa.esig.dss.eaa.jwt.creation;

import java.util.List;

import eu.europa.esig.dss.model.DSSDocument;

/**
 * Builds the payload of the key binding JWT
 */
public interface SDJWTEAAKeyBindingPayloadBuilder {

    /**
     * Build the payload of the key binding JWT
     *
     * @param eaa the EAA
     * @param disclosures the disclosures
     * @param keyBindingParameters the key binding parameters
     * @return {@link DSSDocument} the payload
     */
    DSSDocument buildPayload(DSSDocument eaa, List<SDJWTEAADisclosure> disclosures, SDJWTKeyBindingParameters keyBindingParameters);
}
