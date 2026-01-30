package eu.europa.esig.dss.diagnostic;

import eu.europa.esig.dss.diagnostic.jaxb.XmlTrustSourceList;

public class TrustedEntityServiceWrapper extends TrustedSourceServiceWrapper {

    /** Corresponding Trusted Source List */
    private XmlTrustSourceList trustedSourceList;

    /** Corresponding List of Trusted Source Lists */
    private XmlTrustSourceList listOfTrustedSourceList;

    /**
     * Default constructor
     */
    public TrustedEntityServiceWrapper() {
        // empty
    }

    /**
     * Gets corresponding Trusted Source List
     *
     * @return {@link XmlTrustSourceList}
     */
    public XmlTrustSourceList getTrustedSourceList() {
        return trustedSourceList;
    }

    /**
     * Sets corresponding Trusted Source List
     *
     * @param trustedSourceList {@link XmlTrustSourceList}
     */
    public void setTrustedSourceList(XmlTrustSourceList trustedSourceList) {
        this.trustedSourceList = trustedSourceList;
    }

    /**
     * Gets corresponding List of Trusted Source Lists
     *
     * @return {@link XmlTrustSourceList}
     */
    public XmlTrustSourceList getListOfTrustedSourceList() {
        return listOfTrustedSourceList;
    }

    /**
     * Sets corresponding List of Trusted Source Lists
     *
     * @param listOfTrustedSourceList {@link XmlTrustSourceList}
     */
    public void setListOfTrustedSourceList(XmlTrustSourceList listOfTrustedSourceList) {
        this.listOfTrustedSourceList = listOfTrustedSourceList;
    }

}
