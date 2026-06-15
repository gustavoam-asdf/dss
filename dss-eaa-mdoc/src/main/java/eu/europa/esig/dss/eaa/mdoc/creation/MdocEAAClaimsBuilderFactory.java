package eu.europa.esig.dss.eaa.mdoc.creation;

/**
 * Loads the relevant {@code eu.europa.esig.dss.eaa.mdoc.creation.MdocEAAClaimsBuilder} based on the input configuration
 *
 */
public interface MdocEAAClaimsBuilderFactory {

    /**
     * Loads a relevant {@code MdocEAAClaimsBuilder} based on the {@code payloadParameters}
     *
     * @param payloadParameters {@link MdocEAAPayloadParameters}
     * @return {@link MdocEAAClaimsBuilder}
     */
    MdocEAAClaimsBuilder create(MdocEAAPayloadParameters payloadParameters);

}
