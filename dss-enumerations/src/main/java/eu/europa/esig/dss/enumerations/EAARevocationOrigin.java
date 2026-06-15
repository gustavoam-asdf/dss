package eu.europa.esig.dss.enumerations;

/**
 * Represents an origin type of the EAA revocation data
 *
 */
public enum EAARevocationOrigin {

    /**
     * The status data was provided by the user or extracted from online source
     */
    EXTERNAL,

    /**
     * The status data was obtained from a local DB or cache
     */
    CACHED;

}
