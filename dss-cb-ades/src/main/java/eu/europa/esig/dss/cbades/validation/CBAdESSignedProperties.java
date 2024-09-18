package eu.europa.esig.dss.cbades.validation;

import eu.europa.esig.dss.cbades.COSEProtectedHeader;
import eu.europa.esig.dss.cbades.cbor.CBORObject;
import eu.europa.esig.dss.spi.validation.SignatureProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Represents a list of CB-AdES signed properties (protected header)
 *
 */
public class CBAdESSignedProperties implements SignatureProperties<CBAdESAttribute> {

    private static final Logger LOG = LoggerFactory.getLogger(CBAdESSignedProperties.class);

    /** Represent the protected header of the signature's body structure */
    private final COSEProtectedHeader bodyProtectedHeader;

    /** Represent the protected header of the signature's signer structure */
    private final COSEProtectedHeader signerProtectedHeader;

    /**
     * Default constructor
     *
     * @param bodyProtectedHeader {@link COSEProtectedHeader} of the COSE body structure
     * @param signerProtectedHeader {@link COSEProtectedHeader} of the COSE signer structure (only for COSE_Sign)
     */
    public CBAdESSignedProperties(COSEProtectedHeader bodyProtectedHeader, COSEProtectedHeader signerProtectedHeader) {
        this.bodyProtectedHeader = bodyProtectedHeader;
        this.signerProtectedHeader = signerProtectedHeader;
    }

    @Override
    public boolean isExist() {
        return bodyProtectedHeader != null || signerProtectedHeader != null;
    }

    @Override
    public List<CBAdESAttribute> getAttributes() {
        final List<CBAdESAttribute> attributes = new ArrayList<>();

        if (bodyProtectedHeader != null) {
            for (Map.Entry<Long, CBORObject> entry : bodyProtectedHeader.entrySet()) {
                attributes.add(new CBAdESAttribute(entry.getKey(), entry.getValue()));
            }
        }
        if (signerProtectedHeader != null) {
            for (Map.Entry<Long, CBORObject> entry : signerProtectedHeader.entrySet()) {
                if (bodyProtectedHeader != null && bodyProtectedHeader.containsKey(entry.getKey())) {
                    if (bodyProtectedHeader.getHeader(entry.getKey()).equals(entry.getValue())) {
                        LOG.warn("The header with key '{}' is present in both body and signer protected header!",
                                entry.getKey());
                    } else {
                        LOG.warn("Conflict between headers with key '{}' from body and signer protected header! " +
                                "Ignore entry.", entry.getKey());
                        continue;
                    }
                }
                attributes.add(new CBAdESAttribute(entry.getKey(), entry.getValue()));
            }
        }

        return attributes;
    }

}
