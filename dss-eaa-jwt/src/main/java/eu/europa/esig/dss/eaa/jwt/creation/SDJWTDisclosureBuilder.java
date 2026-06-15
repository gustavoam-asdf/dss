package eu.europa.esig.dss.eaa.jwt.creation;

/**
 * Builds a disclosure String to be used on a selectively disclosable claim digest computation and/or EAA Presentation
 *
 */
public interface SDJWTDisclosureBuilder {

    /**
     * Builds a String for a selectively disclosable EAA claim to be used for Digest computation
     *
     * @param name {@link String} element name of the claim
     * @param value {@link Object} value of the claim
     * @param salt {@link String} high entropy data used to reduce hash collision
     * @return {@link SDJWTEAADisclosure}
     */
    SDJWTEAADisclosure build(String name, Object value, String salt);

}
