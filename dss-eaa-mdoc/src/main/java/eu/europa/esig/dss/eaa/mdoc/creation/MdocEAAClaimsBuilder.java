package eu.europa.esig.dss.eaa.mdoc.creation;

import eu.europa.esig.dss.eaa.mdoc.creation.claim.MdocEAAClaim;

import java.util.List;

/**
 * Builds a relevant MdocEAAClaim implementations based on the document type
 *
 */
public interface MdocEAAClaimsBuilder {

    /**
     * Creates claims for the payload parameters
     *
     * @param payloadParameters {@link MdocEAAPayloadParameters}
     * @return a list of {@link MdocEAAClaim}s
     */
    List<MdocEAAClaim> build(MdocEAAPayloadParameters payloadParameters);

}
