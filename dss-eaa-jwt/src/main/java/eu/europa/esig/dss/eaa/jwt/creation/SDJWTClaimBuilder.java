package eu.europa.esig.dss.eaa.jwt.creation;

import java.util.List;
import java.util.Objects;

import eu.europa.esig.dss.eaa.jwt.creation.claim.SDJWTArrayPresentableClaim;
import eu.europa.esig.dss.eaa.jwt.creation.claim.SDJWTStringPresentableClaim;

/**
 * Build SD-JWT claims
 */
public class SDJWTClaimBuilder {

    private final SDJWTSaltGenerator saltGenerator;

    /**
     * Default constructor that uses {@link SDJWTDefaultSaltGenerator} as salt generator
     */
    public SDJWTClaimBuilder() {
        this(new SDJWTDefaultSaltGenerator());
    }

    /**
     * Constructor with a custom {@link SDJWTSaltGenerator}
     *
     * @param saltGenerator
     *         {@link SDJWTSaltGenerator}
     */
    public SDJWTClaimBuilder(final SDJWTSaltGenerator saltGenerator) {
        this.saltGenerator = saltGenerator;
    }

    /**
     * Builds a claim for a {@link String} value
     *
     * @param name
     *         the name of the claim
     * @param value
     *         the value
     * @param selectivelyDisclosable
     *         if the claim is selectively disclosable
     * @return {@link SDJWTStringPresentableClaim}
     */
    public SDJWTStringPresentableClaim createStringClaim(final String name, final String value, final boolean selectivelyDisclosable) {
        Objects.requireNonNull(value, "value must not be null");

        if (selectivelyDisclosable) {
            return new SDJWTStringPresentableClaim(name, value, true, saltGenerator.generateSalt());
        }
        return new SDJWTStringPresentableClaim(name, value);
    }

    /**
     * Builds a claim for an array of {@link String}
     *
     * @param name
     *         the name of the claim
     * @param values
     *         the values to add in the array
     * @param selectivelyDisclosable
     *         if the claim is selectively disclosable
     * @param contentSelectivelyDisclosable
     *         if the values are selectively disclosable
     * @return @{@link SDJWTArrayPresentableClaim}
     */
    public SDJWTArrayPresentableClaim createStringListClaim(final String name, final List<String> values, final boolean selectivelyDisclosable,
                                                            final boolean contentSelectivelyDisclosable) {
        Objects.requireNonNull(values, "values must not be null");

        final SDJWTArrayPresentableClaim arrayClaim = new SDJWTArrayPresentableClaim(name, selectivelyDisclosable,
                selectivelyDisclosable ? saltGenerator.generateSalt() : null);
        values.stream()
                .map(value -> createStringClaim(null, value, contentSelectivelyDisclosable))
                .forEach(arrayClaim::addElement);

        return arrayClaim;
    }
}
