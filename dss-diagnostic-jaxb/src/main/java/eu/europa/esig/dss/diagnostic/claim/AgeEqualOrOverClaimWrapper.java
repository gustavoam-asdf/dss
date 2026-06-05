package eu.europa.esig.dss.diagnostic.claim;

import eu.europa.esig.dss.diagnostic.jaxb.XmlAgeEqualOrOverClaim;
import eu.europa.esig.dss.diagnostic.jaxb.XmlAgeOverNNClaim;
import eu.europa.esig.dss.diagnostic.jaxb.XmlClaim;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Wraps {@code eu.europa.esig.dss.diagnostic.jaxb.XmlAgeEqualOrOverClaim} claim
 *
 */
public class AgeEqualOrOverClaimWrapper extends ClaimWrapper {

    /**
     * Default constructor
     *
     * @param wrapped {@link XmlClaim}
     */
    public AgeEqualOrOverClaimWrapper(final XmlAgeEqualOrOverClaim wrapped) {
        super(wrapped);
    }

    /**
     * Constructor with a parent claim provided
     *
     * @param wrapped {@link XmlClaim}
     * @param parent {@link ClaimWrapper}
     */
    public AgeEqualOrOverClaimWrapper(final XmlAgeEqualOrOverClaim wrapped, final ClaimWrapper parent) {
        super(wrapped, parent);
    }

    /**
     * Gets a list of age specific claims embedded within the map
     *
     * @return a list of {@link AgeOverNNClaimWrapper}s
     */
    public List<AgeOverNNClaimWrapper> getAgeEqualOrOverList() {
        List<XmlAgeOverNNClaim> ageOverNN = getWrapped().getAgeOverNNClaim();
        if (ageOverNN != null && !ageOverNN.isEmpty()) {
            return ageOverNN.stream().map(AgeOverNNClaimWrapper::new).collect(Collectors.toList());
        }
        return Collections.emptyList();
    }

    @Override
    public XmlAgeEqualOrOverClaim getWrapped() {
        return (XmlAgeEqualOrOverClaim) super.getWrapped();
    }

}
