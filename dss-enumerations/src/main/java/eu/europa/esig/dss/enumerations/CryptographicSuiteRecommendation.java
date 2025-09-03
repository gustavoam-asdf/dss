package eu.europa.esig.dss.enumerations;

/**
 * The recommendation element shall be used to indicate that a mechanism and its parameters are either
 * Recommended (R) or Legacy (L), as defined in ETSI TS 119 312 [i.2], clause 3.1.
 *
 */
public enum CryptographicSuiteRecommendation {

    /** Recommended cryptographic algorithm */
    RECOMMENDED("R"),

    /** Legacy cryptographic algorithm */
    LEGACY("L");

    /** The string value identifying the recommendation type */
    private final String value;

    /**
     * Default constructor
     *
     * @param value {@link String}
     */
    CryptographicSuiteRecommendation(final String value) {
        this.value = value;
    }

    /**
     * Gets the value of the recommendation type
     *
     * @return {@link String}
     */
    public String getValue() {
        return value;
    }

    /**
     * Returns a {@code CryptographicSuiteRecommendation} by the given value
     *
     * @param value {@link String} to get {@link CryptographicSuiteRecommendation} for
     * @return {@link CryptographicSuiteRecommendation}
     */
    public static CryptographicSuiteRecommendation fromValue(String value) {
        if (value != null) {
            for (CryptographicSuiteRecommendation recommendation : CryptographicSuiteRecommendation.values()) {
                if (recommendation.value.equals(value)) {
                    return recommendation;
                }
            }
        }
        return null;
    }

}
