package eu.europa.esig.dss.enumerations;

/**
 * Provides a list of EAA category definitions
 *
 */
public enum EAACategory {

    /**
     * Indication, that the attestation has been issued as a qualified electronic attestation of attributes
     */
    EU_QEAA("urn:etsi:esi:eaa:eu:qualified"),

    /**
     * Indication, that the attestation has been issued as an electronic attestation of attributes issued by or on
     * behalf of a public body responsible for an authentic source
     */
    EU_PUBEAA("urn:etsi:esi:eaa:eu:pub");

    /** URN defined for the category */
    private final String urn;

    /**
     * Default constructor
     *
     * @param urn {@link String}
     */
    EAACategory(final String urn) {
        this.urn = urn;
    }

    /**
     * Gets URN defined for the EAA category
     *
     * @return {@link String}
     */
    public String getUrn() {
        return urn;
    }

}
