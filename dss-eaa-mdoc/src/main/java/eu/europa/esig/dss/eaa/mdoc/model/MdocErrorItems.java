package eu.europa.esig.dss.eaa.mdoc.model;

import java.util.Map;

/**
 * Contains errors for not returned data items.
 *
 */
public class MdocErrorItems {

    /**
     * Map between data element identifiers and their corresponding error codes
     */
    private Map<String, Long> errorsMap;

    /**
     * Default constructor
     */
    public MdocErrorItems() {
        // empty
    }

    /**
     * Gets the error map
     *
     * @return map between data element identifiers and their corresponding error codes
     */
    public Map<String, Long> getErrorsMap() {
        return errorsMap;
    }

    /**
     * Sets the error map
     *
     * @param errorsMap map between data element identifiers and their corresponding error codes
     */
    public void setErrorsMap(Map<String, Long> errorsMap) {
        this.errorsMap = errorsMap;
    }

}
