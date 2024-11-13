package eu.europa.esig.dss.cbades.validation;

import eu.europa.esig.dss.cbades.CBAdESUtils;
import eu.europa.esig.dss.cbades.COSEConstants;
import eu.europa.esig.dss.cbades.cbor.CBORArray;
import eu.europa.esig.dss.cbades.cbor.CBORMap;
import eu.europa.esig.dss.cbades.cbor.CBORObject;
import eu.europa.esig.dss.crl.CRLUtils;
import eu.europa.esig.dss.enumerations.RevocationOrigin;
import eu.europa.esig.dss.enumerations.RevocationRefOrigin;
import eu.europa.esig.dss.spi.x509.revocation.crl.CRLRef;
import eu.europa.esig.dss.spi.x509.revocation.crl.OfflineCRLSource;
import eu.europa.esig.dss.utils.Utils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Extracts and stores CRLs from a CB-AdES signature
 *
 */
public class CBAdESCRLSource extends OfflineCRLSource {

    private static final Logger LOG = LoggerFactory.getLogger(CBAdESCRLSource.class);

    /** Represents the unsigned 'uHeaders' header */
    private final transient CBAdESUHeaders uHeaders;

    /**
     * Default constructor
     *
     * @param uHeaders {@link CBAdESUHeaders} containing the unsigned properties of the signature
     */
    public CBAdESCRLSource(final CBAdESUHeaders uHeaders) {
        this.uHeaders = uHeaders;

        extractUHeaders();
    }

    private void extractUHeaders() {
        if (uHeaders == null || !uHeaders.isExist()) {
            return;
        }

        for (CBAdESAttribute attribute : uHeaders.getAttributes()) {
            extractValidationData(attribute);
            extractCompleteRevocationRefs(attribute);
        }
    }

    private void extractValidationData(CBAdESAttribute attribute) {
        if (COSEConstants.VAL_DATA == attribute.getHeaderId()) {
            CBORObject valData = attribute.getValue();
            if (valData.isMap()) {
                CBORMap valDataMap = (CBORMap) valData;
                CBORMap rVals = valDataMap.getAsMap(COSEConstants.VAL_DATA_R_VALS);
                if (rVals != null && !rVals.isEmpty()) {
                    extractRevocationValues(rVals, RevocationOrigin.REVOCATION_VALUES);
                }
            } else {
                LOG.warn("The value of header 'valData' shall be represented by a CBOR Map! Entry is skilled.");
            }
        }
    }

    private void extractRevocationValues(CBORMap rVals, RevocationOrigin origin) {
        CBORArray crlVals = rVals.getAsArray(COSEConstants.R_VALS_CRL_VALS);
        if (crlVals != null && !crlVals.isEmpty()) {
            for (CBORObject pkiOb : crlVals.getItems()) {
                if (pkiOb.isMap()) {
                    extractCRL((CBORMap) pkiOb, origin);
                } else {
                    LOG.warn("The header 'pkiOb' shall be represented by a CBOR Map! The entry is skipped.");
                }
            }
        }
    }

    private void extractCRL(CBORMap pkiOb, RevocationOrigin origin) {
        byte[] val = CBAdESUtils.extractDerEncodedPkiObject(pkiOb);
        if (Utils.isArrayNotEmpty(val)) {
            try {
                addBinary(CRLUtils.buildCRLBinary(val), origin);
            } catch (Exception e) {
                LOG.warn("Unable to extract CRL from '{}'. Reason : {}", Utils.toBase64(val), e.getMessage(), e);
            }
        }
    }

    private void extractCompleteRevocationRefs(CBAdESAttribute attribute) {
        if (COSEConstants.REFS == attribute.getHeaderId()) {
            CBORObject refs = attribute.getValue();
            if (refs.isMap()) {
                CBORMap refsMap = (CBORMap) refs;
                CBORMap rRefs = refsMap.getAsMap(COSEConstants.REFS_R_REFS);
                if (rRefs != null && !rRefs.isEmpty()) {
                    extractRevocationRefs(rRefs, RevocationRefOrigin.COMPLETE_REVOCATION_REFS);
                }
            } else {
                LOG.warn("The value of header 'refs' shall be represented by a CBOR Map! Entry is skilled.");
            }
        }
    }

    private void extractRevocationRefs(CBORMap rRefs, RevocationRefOrigin origin) {
        CBORArray crlRefs = rRefs.getAsArray(COSEConstants.R_REFS_CRL_REF);
        if (crlRefs != null) {
            for (CBORObject crlRefObject : crlRefs.getItems()) {
                if (crlRefObject.isMap()) {
                    CBORMap crlRefMap = (CBORMap) crlRefObject;
                    CRLRef crlRef = CBAdESUtils.createCRLRef(crlRefMap);
                    if (crlRef != null) {
                        addRevocationReference(crlRef, origin);
                    }
                } else {
                    LOG.warn("The header 'CRLRef' shall be represented by a CBOR Map! The entry is skipped.");
                }
            }
        }
    }

}
