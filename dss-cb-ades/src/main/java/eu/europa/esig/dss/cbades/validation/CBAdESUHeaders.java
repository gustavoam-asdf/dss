package eu.europa.esig.dss.cbades.validation;

import eu.europa.esig.dss.cbades.COSEConstants;
import eu.europa.esig.dss.cbades.COSESign1;
import eu.europa.esig.dss.cbades.COSESignature;
import eu.europa.esig.dss.cbades.COSEUnprotectedHeader;
import eu.europa.esig.dss.cbades.cbor.CBORArray;
import eu.europa.esig.dss.cbades.cbor.CBORMap;
import eu.europa.esig.dss.cbades.cbor.CBORObject;
import eu.europa.esig.dss.spi.exception.IllegalInputException;
import eu.europa.esig.dss.spi.validation.SignatureProperties;
import eu.europa.esig.dss.utils.Utils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

/**
 * Represents the list of components present inside the unprotected 'uHeaders' header
 *
 */
public class CBAdESUHeaders implements SignatureProperties<CBAdESUHeadersComponent> {

    private static final Logger LOG = LoggerFactory.getLogger(CBAdESUHeaders.class);

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
        if (components == null) {
            components = new ArrayList<>();
            COSEUnprotectedHeader unprotectedHeader = getUnprotectedHeader();
            if (unprotectedHeader != null && !unprotectedHeader.isEmpty()) {
                CBORArray uHeaders = unprotectedHeader.getAsArray(COSEConstants.U_HEADERS);
                if (uHeaders != null && !uHeaders.isEmpty()) {
                    for (int i = 0; i < uHeaders.getSize(); i++) {
                        CBORObject uHeadersEntry = uHeaders.getItem(i);
                        CBAdESUHeadersComponent component = CBAdESUHeadersComponent.build(uHeadersEntry, i);
                        if (component != null) {
                            components.add(component);
                        }
                        // else : unable to create, skip
                    }
                }
            }
        }
        return components;
    }

    /**
     * Adds a new entry to the 'uHeaders' array
     *
     * @param headerKey        {@link Long} representing the name of the 'uHeaders' entry
     * @param value            {@link CBORObject} represents a value of the 'uHeaders' entry
     * @param cborBtsrWrapped  defines if the entry shall be incorporated in its
     *                         corresponding CBOR btsr representation
     */
    public void addComponent(Long headerKey, CBORObject value, boolean cborBtsrWrapped) {
        CBORArray uHeaders = getUHeadersToEdit();
        CBORObject etsiEntry = getComponent(headerKey, value, cborBtsrWrapped);
        uHeaders.add(etsiEntry);
    }

    private CBORObject getComponent(Long headerKey, CBORObject value, boolean cborBtsrWrapped) {
        CBORMap cborMap = new CBORMap();
        cborMap.put(headerKey, value);
        return cborBtsrWrapped ? cborMap.getByteString() : cborMap;
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

    private CBORArray getUHeadersToEdit() {
        COSEUnprotectedHeader unprotectedHeader = getUnprotectedHeader();

        clearCachedAttributes();
        if (unprotectedHeader.isEmpty()) {
            assignUnprotectedHeader(unprotectedHeader);
        }

        CBORObject uHeaders = unprotectedHeader.getHeader(COSEConstants.U_HEADERS);
        if (uHeaders != null) {
            if (!uHeaders.isArray()) {
                throw new IllegalInputException("'uHeaders' header parameter shall be of type CBORArray!");
            }
            // continue

        } else {
            uHeaders = new CBORArray(1);
            unprotectedHeader.put(COSEConstants.U_HEADERS, uHeaders);
        }
        return (CBORArray) uHeaders;
    }

    private COSEUnprotectedHeader getUnprotectedHeader() {
        COSEUnprotectedHeader unprotectedHeader = cose.getSignerUnprotectedHeader();
        if (unprotectedHeader == null) {
            // NOTE : instantiated if empty
            unprotectedHeader = cose.getBodyUnprotectedHeader();
        }
        return unprotectedHeader;
    }

    private void assignUnprotectedHeader(COSEUnprotectedHeader unprotectedHeader) {
        switch (cose.getContext()) {
            case COSE_SIGN1:
                COSESign1 coseSign1 = (COSESign1) cose.getCoseSignStructure();
                coseSign1.setUnprotectedHeader(unprotectedHeader);
                break;

            case COSE_SIGN:
            case COSE_COUNTER_SIGNATURE:
            case COSE_COUNTER_SIGNATURE_V2:
                COSESignature signerSignature = (COSESignature) cose.getSignerSignature();
                signerSignature.setUnprotectedHeader(unprotectedHeader);
                break;

            default:
                throw new UnsupportedOperationException(
                        String.format("The context '%s' is not supported!", cose.getContext()));
        }
    }

    private void clearCachedAttributes() {
        this.components = null;
    }

    /**
     * Replaces the given component within the 'uHeaders' header array
     *
     * @param uHeader {@link CBAdESUHeadersComponent} to replace
     */
    public void replaceComponent(CBAdESUHeadersComponent uHeader) {
        CBORArray uHeaders = getUHeadersToEdit();
        for (int i = 0; i < uHeaders.getSize(); i++) {
            CBORObject item = uHeaders.getItem(i);
            CBAdESUHeadersComponent currentComponent = CBAdESUHeadersComponent.build(item, i);
            if (uHeader.equals(currentComponent)) {
                uHeaders.set(i, uHeader.getComponent());
                break;
            }
        }
    }

    /**
     * Removes the 'uHeaders' components with the given {@code headerName}
     *
     * @param headerId {@link Long} identifier of the 'uHeaders' entry to remove
     */
    public void removeComponent(Long headerId) {
        CBORArray uHeaders = getUHeadersToEdit();
        if (!uHeaders.isEmpty()) {
            ListIterator<CBORObject> iterator = getBackwardIterator(uHeaders);
            while (iterator.hasPrevious()) {
                removeLastIfMatches(iterator, headerId);
            }
        }
    }

    /**
     * Removes the last 'uHeaders' item if the name matches to the given {@code headerName}
     *
     * @param headerId {@link Long} identifier of the 'uHeaders' entry to remove
     */
    public void removeLastComponent(Long headerId) {
        CBORArray uHeaders = getUHeadersToEdit();
        if (!uHeaders.isEmpty()) {
            ListIterator<CBORObject> iterator = getBackwardIterator(uHeaders);
            removeLastIfMatches(iterator, headerId);
        }
    }

    private ListIterator<CBORObject> getBackwardIterator(CBORArray uHeaders) {
        return uHeaders.getItems().listIterator(uHeaders.getSize());
    }

    private void removeLastIfMatches(ListIterator<CBORObject> iterator, Long headerId) {
        CBORObject object = iterator.previous();
        if (object.isMap() && ((CBORMap) object).getKeys().contains(headerId)) {
            iterator.remove();
        }
    }

}
