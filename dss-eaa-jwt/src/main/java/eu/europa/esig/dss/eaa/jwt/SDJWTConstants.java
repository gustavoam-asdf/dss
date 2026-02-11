package eu.europa.esig.dss.eaa.jwt;

/**
 * Contains a list of SD-JWT constants requiring for processing the token
 *
 */
public class SDJWTConstants {

    /**
     * Utils class
     */
    private SDJWTConstants() {
        // singleton
    }

    // SD-JWT payload parameters

    /** SD-JWT payload "category" header */
    public static final String CATEGORY = "category";

    /** SD-JWT payload header used to define a hash value of a selectively disclosable array element */
    public static final String HASH = "...";

    /** SD-JWT payload "_sd" header */
    public static final String SD = "_sd";

    /** SD-JWT payload "_sd_alg" header */
    public static final String SD_ALG = "_sd_alg";

    // SD-JWT unprotected header parameters

    /** SD-JWT unprotected header "disclosures" header */
    public static final String DISCLOSURES = "disclosures";

    /** SD-JWT unprotected header "kb_jwt" (key binding JWT) header */
    public static final String KB_JWT = "kb_jwt";

}
