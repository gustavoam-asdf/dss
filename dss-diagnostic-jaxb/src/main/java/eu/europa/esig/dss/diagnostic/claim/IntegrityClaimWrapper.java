package eu.europa.esig.dss.diagnostic.claim;

import eu.europa.esig.dss.diagnostic.jaxb.XmlClaim;
import eu.europa.esig.dss.diagnostic.jaxb.XmlIntegrityClaim;
import eu.europa.esig.dss.enumerations.DigestAlgorithm;

/**
 * Represents an integrity claim for a certain claim attribute
 *
 */
public class IntegrityClaimWrapper extends ClaimWrapper {

    /**
     * Default constructor
     *
     * @param wrapped {@link XmlClaim}
     */
    public IntegrityClaimWrapper(final XmlIntegrityClaim wrapped) {
        super(wrapped);
    }

    /**
     * Constructor with a parent claim provided
     *
     * @param wrapped {@link XmlClaim}
     * @param parent {@link ClaimWrapper}
     */
    public IntegrityClaimWrapper(final XmlIntegrityClaim wrapped, final ClaimWrapper parent) {
        super(wrapped, parent);
    }

    /**
     * Gets the digest algorithm used for the claim hash computation
     *
     * @return {@link DigestAlgorithm}
     */
    public DigestAlgorithm getDigestAlgorithm() {
        return getWrapped().getDigestMethod();
    }

    /**
     * Gets the digest value of the computed claim integrity hash
     *
     * @return byte array
     */
    public byte[] getDigestValue() {
        return getWrapped().getDigestValue();
    }

    @Override
    public XmlIntegrityClaim getWrapped() {
        return (XmlIntegrityClaim) super.getWrapped();
    }

}
