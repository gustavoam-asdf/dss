package eu.europa.esig.dss.eaa.mdoc;

/**
 * Contains a list of header names as defined in "6 Implementation of EAA based on ISO/IEC-mdoc" of ETSI TS 119 472-1.
 *
 */
public final class ETSI194721Headers {

    /**
     * Singleton
     */
    private ETSI194721Headers() {
        // empty
    }

    /** An explicit signal identifying the category of the EAA in the context where the EAA has been issued */
    public static final String CATEGORY = "category";

}
