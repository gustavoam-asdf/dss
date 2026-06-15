package eu.europa.esig.dss.model.eaa.claim;

/**
 * Univocally identifies the attribute subject
 *
 */
public interface ClaimAttestedAttributesSubjectId extends Claim {

    /**
     * Gets the family name of the attribute subject
     *
     * @return {@link ClaimString}
     */
    ClaimString getFamilyName();

    /**
     * Gets the given name of the attribute subject
     *
     * @return {@link ClaimString}
     */
    ClaimString getGivenName();

    /**
     * Gets the number of the personal identification data assigned to the attribute subject
     *
     * @return {@link ClaimString}
     */
    ClaimString getDocumentNumber();

}
