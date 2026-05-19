package eu.europa.esig.dss.eaa.jwt.validation;

import eu.europa.esig.dss.eaa.jwt.SDJWTUtils;
import eu.europa.esig.dss.enumerations.DigestAlgorithm;
import eu.europa.esig.dss.jades.DSSJsonUtils;
import eu.europa.esig.dss.model.Digest;
import eu.europa.esig.dss.model.eaa.Disclosure;
import eu.europa.esig.dss.spi.DSSUtils;
import eu.europa.esig.dss.spi.exception.IllegalInputException;

import java.util.List;

/**
 * Represents an EAA Disclosure extracted from an SD-JWT VC token
 *
 */
public class SDJWTDisclosure extends Disclosure {

    private static final long serialVersionUID = -5284795899819648729L;

    /** The original disclosure value */
    private final String disclosureB64Url;

    /**
     * Default constructor
     *
     * @param disclosureB64Url {@link String} base64url encoded,
     *                         representing the original provided value of the disclosure
     */
    public SDJWTDisclosure(final String disclosureB64Url) {
        this.disclosureB64Url = disclosureB64Url;
        parseDisclosure(disclosureB64Url);
    }

    private List<?> getDisclosureArray(final String disclosureB64Url) {
        Object disclosureObject = DSSJsonUtils.parseBase64UrlEncoded(disclosureB64Url);

        if (!(disclosureObject instanceof List<?>)) {
            throw new IllegalInputException("Invalid disclosure format! An object of a JSON Array type is expected.");
        }
        List<?> disclosureList = (List<?>) disclosureObject;
        if (disclosureList.size() != 2 && disclosureList.size() != 3) {
            throw new IllegalInputException("Invalid disclosure format! An array of 2 or 3 elements is expected.");
        }
        return disclosureList;
    }
    
    private void parseDisclosure(final String disclosureB64Url) {
        List<?> value = getDisclosureArray(disclosureB64Url);
        Object saltObject = value.get(0);
        if (!(saltObject instanceof String)) {
            throw new IllegalInputException("Invalid disclosure format! The first element of the array (salt) shall be of String type!");
        }
        String saltString = (String) saltObject;
        this.salt = saltString.getBytes();

        String claimName = null;
        Object claimValue;
        if (value.size() == 2) {
            // array or recursive disclosure
            claimValue = value.get(1);

        } else {
            Object claimNameObject = value.get(1);
            if (!(claimNameObject instanceof String)) {
                throw new IllegalInputException("Invalid disclosure format! The second element of the array (claim name) shall be of String type!");
            }
            claimName = (String) claimNameObject;
            claimValue = value.get(2);
        }
        this.claim = SDJWTUtils.createClaim(claimName, null, claimValue, true);
    }

    @Override
    protected Digest computeDigest(DigestAlgorithm digestAlgorithm) {
        /*
         * 4.2.3. Hashing Disclosures (draft-ietf-oauth-selective-disclosure-jwt-22)
         *
         * The input to the hash function MUST be the base64url-encoded Disclosure,
         * not the bytes encoded by the base64url string.
         */
        byte[] digestValue = DSSUtils.digest(digestAlgorithm, disclosureB64Url.getBytes());
        return new Digest(digestAlgorithm, digestValue);
    }

}
