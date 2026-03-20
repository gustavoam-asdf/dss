package eu.europa.esig.dss.eaa.mdoc.model;

import eu.europa.esig.dss.cbades.COSESignStructure;

import java.util.List;
import java.util.Map;

/**
 * IssuerSigned contains the mobile security object for issuer data authentication and the data elements
 * protected by issuer data authentication. nameSpaces contains the returned data elements as part of
 * their corresponding namespaces.
 *
 */
public class MdocIssuerSigned {

    /** Map of namespaces and corresponding issuer signed items */
    private Map<String, List<MdocIssuerSignedItem>> namespaces;

    /** Contains the issuer authentication signature */
    private COSESignStructure issuerAuth;

    /**
     * Default constructor
     */
    public MdocIssuerSigned() {
        // empty
    }

    /**
     * Gets a map of namespaces and their corresponding authenticated data
     *
     * @return a map of namespaces and issuer signed items
     */
    public Map<String, List<MdocIssuerSignedItem>> getNamespaces() {
        return namespaces;
    }

    /**
     * Sets a map of namespaces and their corresponding authenticated data
     *
     * @param namespaces a map of namespaces and issuer signed items
     */
    public void setNamespaces(Map<String, List<MdocIssuerSignedItem>> namespaces) {
        this.namespaces = namespaces;
    }

    /**
     * Gets the issuer signature
     *
     * @return {@link COSESignStructure}
     */
    public COSESignStructure getIssuerAuth() {
        return issuerAuth;
    }

    /**
     * Sets the issuer signature
     *
     * @param issuerAuth {@link COSESignStructure}
     */
    public void setIssuerAuth(COSESignStructure issuerAuth) {
        this.issuerAuth = issuerAuth;
    }

}
