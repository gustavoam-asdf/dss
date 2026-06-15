package eu.europa.esig.dss.eaa.jwt.creation;

import eu.europa.esig.dss.eaa.common.creation.AbstractEAADisclosure;
import eu.europa.esig.dss.enumerations.DigestAlgorithm;
import eu.europa.esig.dss.model.Digest;
import eu.europa.esig.dss.spi.DSSUtils;

import java.util.Objects;

/**
 * Implementation of a disclosure for an SD-JWT VC token
 *
 */
public class SDJWTEAADisclosure extends AbstractEAADisclosure {

    private static final long serialVersionUID = -1978354313189364987L;

    /** Base64Url encoded string */
    private final String disclosure;

    /**
     * Default constructor to instantiate an SD-JWT VC disclosure from a base64url encoded disclosure string.
     * NOTE: the class does not verify the validity of the data structure.
     *
     * @param disclosure {@link String}
     */
    public SDJWTEAADisclosure(final String disclosure) {
        Objects.requireNonNull(disclosure, "Disclosure string cannot be null!");
        this.disclosure = disclosure;
    }

    /**
     * Gets the disclosure string
     *
     * @return {@link String}
     */
    public String getDisclosure() {
        return disclosure;
    }

    @Override
    protected Digest computeDigest(DigestAlgorithm digestAlgorithm) {
        /*
         * 4.2.3. Hashing Disclosures (draft-ietf-oauth-selective-disclosure-jwt-22)
         *
         * The input to the hash function MUST be the base64url-encoded Disclosure,
         * not the bytes encoded by the base64url string.
         */
        byte[] digestValue = DSSUtils.digest(digestAlgorithm, disclosure.getBytes());
        return new Digest(digestAlgorithm, digestValue);
    }

}
