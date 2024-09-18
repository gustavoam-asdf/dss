package eu.europa.esig.dss.cbades.validation;

import eu.europa.esig.dss.spi.validation.SignatureProperties;
import eu.europa.esig.dss.utils.Utils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Represents the list of components present inside the unprotected 'uHeaders' header
 *
 */
public class CBAdESUHeaders implements SignatureProperties<CBAdESUHeadersComponent> {

    private static final long serialVersionUID = 2141375341919119408L;

    /** The COSE signature */
    private final CBORSignature cose;

    /** The list of 'uHeaders' components */
    private List<CBAdESUHeadersComponent> components;

    /**
     * The default constructor
     *
     * @param cose {@link CBORSignature} signature
     */
    public CBAdESUHeaders(final CBORSignature cose) {
        this.cose = cose;
    }

    @Override
    public boolean isExist() {
        return Utils.isCollectionNotEmpty(getAttributes());
    }

    @Override
    public List<CBAdESUHeadersComponent> getAttributes() {
        // TODO : to be implemented
        return Collections.emptyList();
    }

    /**
     * Gets a list of 'uHeaders' entries with matching {@code headerId}
     *
     * @param headerId {@link Long} representing an 'uHeaders' entry identifier
     * @return a list of {@link CBAdESUHeadersComponent}
     */
    public List<CBAdESUHeadersComponent> getUnsignedPropertiesWithHeaderId(Long headerId) {
        List<CBAdESUHeadersComponent> componentsWithHeaderName = new ArrayList<>();
        for (CBAdESUHeadersComponent attribute : getAttributes()) {
            if (headerId.equals(attribute.getHeaderId())) {
                componentsWithHeaderName.add(attribute);
            }
        }
        return componentsWithHeaderName;
    }

}
