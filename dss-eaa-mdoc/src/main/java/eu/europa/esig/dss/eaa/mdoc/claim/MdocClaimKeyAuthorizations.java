package eu.europa.esig.dss.eaa.mdoc.claim;

import eu.europa.esig.dss.eaa.mdoc.MdocConstants;
import eu.europa.esig.dss.model.eaa.claim.ClaimArray;
import eu.europa.esig.dss.model.eaa.claim.ClaimMap;

/**
 * Contains all the namespaces and/or data elements the key may sign or MAC.
 *
 */
public class MdocClaimKeyAuthorizations extends MdocClaimMap {

    private static final long serialVersionUID = -6321124455458258021L;

    /**
     * Constructor to initialize MdocClaimKeyAuthorizations from a ClaimMap
     *
     * @param value {@link ClaimMap}
     */
    public MdocClaimKeyAuthorizations(ClaimMap value) {
        super(value.getName(), value.getNamespace(), value.getMapValue(), value.isSelectivelyDisclosable(), value.getParent());
    }

    /**
     * Gets a list of namespaces the key is authorized to sign or MAC
     *
     * @return {@link MdocClaimAuthorizedNameSpaces}
     */
    public MdocClaimAuthorizedNameSpaces getAuthorizedNamespaces() {
        ClaimArray namespaces = getAsArray(MdocConstants.NAMESPACES);
        if (namespaces != null) {
            return new MdocClaimAuthorizedNameSpaces(namespaces);
        }
        return null;
    }

    /**
     * Gets a map of namespaces and applicable data elements the key is allowed to sign or MAC
     *
     * @return {@link MdocClaimAuthorizedNameSpaces}
     */
    public MdocClaimAuthorizedDataElements getAuthorizedDataElements() {
        ClaimMap dataElements = getAsMap(MdocConstants.DATA_ELEMENTS);
        if (dataElements != null) {
            return new MdocClaimAuthorizedDataElements(dataElements);
        }
        return null;
    }

}
