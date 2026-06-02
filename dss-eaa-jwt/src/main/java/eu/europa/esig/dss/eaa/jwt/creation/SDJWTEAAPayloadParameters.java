package eu.europa.esig.dss.eaa.jwt.creation;

import eu.europa.esig.dss.eaa.common.creation.AbstractEAAPayloadParameters;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

/**
 * Provides configuration for the SD-JWT VC payload creation
 *
 */
public class SDJWTEAAPayloadParameters extends AbstractEAAPayloadParameters {

    /** Map of custom claims */
    private final List<SDJWTEAAClaim> claims = new ArrayList<>();

    private final SDJWTClaimParameters selectivelyDisclosableParameters = new SDJWTClaimParameters();
    private final SDJWTClaimParameters nonSelectivelyDisclosableParameters = new SDJWTClaimParameters();

    /** Decoy digest used to obscure amount of actually disclosed data claims */
    private final List<String> decoyDigests = new ArrayList<>(); // TODO : review

    /**
     * Default constructor to instantiate SD-JWT VC Payload parameters
     */
    public SDJWTEAAPayloadParameters() {
        // empty
    }

    public SDJWTClaimParameters selectivelyDisclosable() {
        return selectivelyDisclosableParameters;
    }

    public SDJWTClaimParameters nonSelectivelyDisclosable() {
        return nonSelectivelyDisclosableParameters;
    }

    /**
     * Adds a custom claim to the list
     *
     * @param claim {@link SDJWTEAAClaim}
     */
    public void addClaim(final SDJWTEAAClaim claim) {
        Objects.requireNonNull(claim, "Claim cannot be null!");
        claims.add(claim);
    }

    /**
     * Adds a custom claim with the given name and a value.
     * The claim will be added to the root level of the payload.
     *
     * @param name {@link String}
     * @param value {@link Object}
     */
    public void addClaim(final String name, final Object value) {
        addClaim(SDJWTEAAClaim.create(name, value));
    }

    /**
     * Adds a selectively disclosable custom claim with the given name and a value.
     * The claim will be added to the root level of the payload.
     *
     * @param name {@link String}
     * @param value {@link Object}
     */
    public void addSelectivelyDisclosableClaim(final String name, final Object value) {
        addClaim(SDJWTEAAClaim.createSelectivelyDisclosable(name, value));
    }

    /**
     * Gets arbitrary provided claims
     *
     * @return a list of {@link SDJWTEAAClaim}s
     */
    public List<SDJWTEAAClaim> getClaims() {
        return claims;
    }

    public void addDecoyDigest(String digest) {
        decoyDigests.add(digest);
    }

    public void addDecoyDigests(Collection<String> digests) {
        decoyDigests.addAll(digests);
    }

    public List<String> getDecoyDigests() {
        return decoyDigests;
    }

    @Override
    public String toString() {
        return "SDJWTEAAPayloadParameters [" +
                "claims=" + claims +
                ", decoyDigests=" + decoyDigests +
                "] " + super.toString();
    }

}
