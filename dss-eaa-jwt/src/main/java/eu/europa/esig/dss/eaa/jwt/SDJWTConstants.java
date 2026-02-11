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

    /** SD-JWT payload header used to define a hash value of a selectively disclosable array element */
    public static final String HASH = "...";

    /** SD-JWT payload "category" header */
    public static final String CATEGORY = "category";

    /** SD-JWT payload "_sd" header */
    public static final String SD = "_sd";

    /** SD-JWT payload "_sd_alg" header */
    public static final String SD_ALG = "_sd_alg";

}
