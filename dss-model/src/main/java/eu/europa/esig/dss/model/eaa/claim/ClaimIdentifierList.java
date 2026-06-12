package eu.europa.esig.dss.model.eaa.claim;

/**
 * Represents an EAA Identifier List claim
 *
 */
public interface ClaimIdentifierList extends ClaimRevocationList {

    /**
     * Gets the EAA's Identifier's List identifier value, when present
     *
     * @return {@link ClaimByteString}
     */
    ClaimByteString getIdentifier();

}
