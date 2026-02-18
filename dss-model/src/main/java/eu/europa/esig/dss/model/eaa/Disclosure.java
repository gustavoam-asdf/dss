package eu.europa.esig.dss.model.eaa;

import eu.europa.esig.dss.enumerations.DigestAlgorithm;
import eu.europa.esig.dss.model.Digest;
import eu.europa.esig.dss.model.eaa.claim.Claim;
import eu.europa.esig.dss.model.eaa.claim.ClaimBinaries;

import java.io.Serializable;
import java.util.Arrays;
import java.util.EnumMap;
import java.util.List;
import java.util.Objects;

/**
 * Generic implementation of an EAA Disclosure
 *
 */
public abstract class Disclosure implements Serializable {

    private static final long serialVersionUID = -6025755119813037143L;

    /** Salt value */
    protected byte[] salt;

    /** Value of the disclosure claim */
    protected Claim claim;

    /** List of nested selectively disclosable claims  */
    protected List<ClaimBinaries> nestedSDClaims;

    /** Cached map containing computed digest values */
    private final EnumMap<DigestAlgorithm, Digest> digestMap = new EnumMap<>(DigestAlgorithm.class);

    /**
     * Default constructor
     */
    protected Disclosure() {
        // empty
    }

    /**
     * Gets salt of the disclosure
     *
     * @return byte array representing disclosure's salt
     */
    public byte[] getSalt() {
        return salt;
    }

    /**
     * Gets the name of the disclosure claim
     *
     * @return {@link String}
     */
    public String getClaimName() {
        return claim != null ? claim.getName() : null;
    }

    /**
     * Gets the value of the disclosure claim value
     *
     * @return {@link Claim}
     */
    public Claim getClaimValue() {
        return claim;
    }

    /**
     * Gets the nested selectively disclosable claims, when applicable.
     * NOTE: It is possible to have nested claims within a disclosure, to create recursive disclosures.
     * Please see {@link <a href="https://www.ietf.org/archive/id/draft-ietf-oauth-selective-disclosure-jwt-22.html#name-recursive-disclosures">Recursive Disclosures</a>
     * for more information.
     * <p>
     * @return a list of {@link ClaimBinaries}s
     */
    public List<ClaimBinaries> getNestedSelectivelyDisclosableClaims() {
        return nestedSDClaims;
    }

    /**
     * Gets digest value of the for the {@code DigestAlgorithm}
     *
     * @param digestAlgorithm {@link DigestAlgorithm} to be used to compute digest with
     * @return {@link Digest}
     */
    public Digest getDigest(DigestAlgorithm digestAlgorithm) {
        if (digestAlgorithm == null) {
            return new Digest(); // empty digest
        }
        return digestMap.computeIfAbsent(digestAlgorithm, d -> computeDigest(digestAlgorithm));
    }

    /**
     * Computes digest according to the rules for the given EAA presentation type
     *
     * @param digestAlgorithm {@link DigestAlgorithm} to be used on the hash computation
     * @return {@link Digest}
     */
    protected abstract Digest computeDigest(DigestAlgorithm digestAlgorithm);

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;

        Disclosure that = (Disclosure) object;
        return Arrays.equals(salt, that.salt)
                && Objects.equals(claim, that.claim)
                && Objects.equals(nestedSDClaims, that.nestedSDClaims)
                && digestMap.equals(that.digestMap);
    }

    @Override
    public int hashCode() {
        int result = Arrays.hashCode(salt);
        result = 31 * result + Objects.hashCode(claim);
        result = 31 * result + Objects.hashCode(nestedSDClaims);
        result = 31 * result + digestMap.hashCode();
        return result;
    }

}
