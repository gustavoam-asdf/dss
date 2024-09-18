package eu.europa.esig.dss.cbades.validation.timestamp;

import eu.europa.esig.dss.cbades.COSEConstants;
import eu.europa.esig.dss.cbades.cbor.CBORArray;
import eu.europa.esig.dss.cbades.cbor.CBORMap;
import eu.europa.esig.dss.cbades.cbor.CBORObject;
import eu.europa.esig.dss.cbades.validation.CBAdESAttribute;
import eu.europa.esig.dss.cbades.validation.CBAdESSignature;
import eu.europa.esig.dss.cbades.validation.CBAdESSignedProperties;
import eu.europa.esig.dss.crl.CRLBinary;
import eu.europa.esig.dss.enumerations.ArchiveTimestampType;
import eu.europa.esig.dss.enumerations.DigestAlgorithm;
import eu.europa.esig.dss.enumerations.TimestampType;
import eu.europa.esig.dss.model.identifier.Identifier;
import eu.europa.esig.dss.spi.signature.AdvancedSignature;
import eu.europa.esig.dss.spi.validation.SignatureProperties;
import eu.europa.esig.dss.spi.validation.timestamp.SignatureTimestampIdentifierBuilder;
import eu.europa.esig.dss.spi.validation.timestamp.SignatureTimestampSource;
import eu.europa.esig.dss.spi.validation.timestamp.TimestampMessageDigestBuilder;
import eu.europa.esig.dss.spi.x509.CertificateRef;
import eu.europa.esig.dss.spi.x509.evidencerecord.EvidenceRecord;
import eu.europa.esig.dss.spi.x509.revocation.crl.CRLRef;
import eu.europa.esig.dss.spi.x509.revocation.ocsp.OCSPRef;
import eu.europa.esig.dss.spi.x509.revocation.ocsp.OCSPResponseBinary;
import eu.europa.esig.dss.spi.x509.tsp.TimestampToken;
import eu.europa.esig.dss.spi.x509.tsp.TimestampedReference;
import eu.europa.esig.dss.utils.Utils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Extracts timestamps from a CB-AdES signature
 *
 */
public class CBAdESTimestampSource extends SignatureTimestampSource<CBAdESSignature, CBAdESAttribute> {

    private static final Logger LOG = LoggerFactory.getLogger(CBAdESTimestampSource.class);

    /** Map between time-stamp tokens and corresponding CB-AdES attributes */
    private final Map<TimestampToken, CBAdESAttribute> timestampAttributeMap = new HashMap<>();

    /**
     * Default constructor
     *
     * @param signature {@link CBAdESSignature}
     */
    public CBAdESTimestampSource(final CBAdESSignature signature) {
        super(signature);
    }

    @Override
    protected SignatureProperties<CBAdESAttribute> buildSignedSignatureProperties() {
        return new CBAdESSignedProperties(signature.getCoseSignature().getBodyProtectedHeader(),
                signature.getCoseSignature().getSignerProtectedHeader());
    }

    @Override
    @SuppressWarnings({ "unchecked", "rawtypes" })
    protected SignatureProperties<CBAdESAttribute> buildUnsignedSignatureProperties() {
        return (SignatureProperties) signature.getUHeaders();
    }

    @Override
    protected boolean isContentTimestamp(CBAdESAttribute signedAttribute) {
        return COSEConstants.ADO_TST == signedAttribute.getHeaderId();
    }

    @Override
    protected boolean isAllDataObjectsTimestamp(CBAdESAttribute signedAttribute) {
        // not supported
        return false;
    }

    @Override
    protected boolean isIndividualDataObjectsTimestamp(CBAdESAttribute signedAttribute) {
        // not supported
        return false;
    }

    @Override
    protected boolean isSignatureTimestamp(CBAdESAttribute unsignedAttribute) {
        return false;
    }

    @Override
    protected boolean isCompleteCertificateRef(CBAdESAttribute unsignedAttribute) {
        return false;
    }

    @Override
    protected boolean isAttributeCertificateRef(CBAdESAttribute unsignedAttribute) {
        return false;
    }

    @Override
    protected boolean isCompleteRevocationRef(CBAdESAttribute unsignedAttribute) {
        return false;
    }

    @Override
    protected boolean isAttributeRevocationRef(CBAdESAttribute unsignedAttribute) {
        return false;
    }

    @Override
    protected boolean isRefsOnlyTimestamp(CBAdESAttribute unsignedAttribute) {
        return false;
    }

    @Override
    protected boolean isSigAndRefsTimestamp(CBAdESAttribute unsignedAttribute) {
        return false;
    }

    @Override
    protected boolean isCertificateValues(CBAdESAttribute unsignedAttribute) {
        return false;
    }

    @Override
    protected boolean isRevocationValues(CBAdESAttribute unsignedAttribute) {
        return false;
    }

    @Override
    protected boolean isAttrAuthoritiesCertValues(CBAdESAttribute unsignedAttribute) {
        return false;
    }

    @Override
    protected boolean isAttributeRevocationValues(CBAdESAttribute unsignedAttribute) {
        return false;
    }

    @Override
    protected boolean isArchiveTimestamp(CBAdESAttribute unsignedAttribute) {
        return false;
    }

    @Override
    protected boolean isTimeStampValidationData(CBAdESAttribute unsignedAttribute) {
        return false;
    }

    @Override
    protected boolean isCounterSignature(CBAdESAttribute unsignedAttribute) {
        return false;
    }

    @Override
    protected boolean isSignaturePolicyStore(CBAdESAttribute unsignedAttribute) {
        return false;
    }

    @Override
    protected boolean isEvidenceRecord(CBAdESAttribute unsignedAttribute) {
        return false;
    }

    @Override
    protected TimestampToken makeTimestampToken(CBAdESAttribute signatureAttribute, TimestampType timestampType, List<TimestampedReference> references) {
        return null;
    }

    @Override
    protected List<TimestampToken> makeTimestampTokens(CBAdESAttribute signatureAttribute, TimestampType timestampType, List<TimestampedReference> references) {
        if (TimestampType.ARCHIVE_TIMESTAMP.equals(timestampType)) {
            // TODO : arcTst ?
            throw new UnsupportedOperationException("Not implemented");
        } else {
            CBORObject tstContainer = signatureAttribute.getValue();
            return extractTimestampTokens(signatureAttribute, tstContainer, timestampType, references);
        }
    }

    private List<TimestampToken> extractTimestampTokens(CBAdESAttribute signatureAttribute, CBORObject tstContainer,
                                                        TimestampType timestampType, List<TimestampedReference> references) {
        final List<TimestampToken> result = new LinkedList<>();
        if (tstContainer != null && tstContainer.isMap()) {
            CBORArray tstTokens = ((CBORMap) tstContainer).getAsArray(COSEConstants.TST_CONTAINER_TST_TOKENS);
            if (tstTokens != null && !tstTokens.isEmpty()) {
                for (int i = 0; i < tstTokens.getSize(); i++) {
                    CBORMap tstToken = tstTokens.getAsMap(i);
                    if (tstToken == null) {
                        LOG.warn("Item of array 'tstTokens' has unsupported type! Shall be a CBOR map");
                    }
                    TimestampToken timestampToken = toTimestampToken(tstToken, signatureAttribute, i, timestampType, references);
                    if (timestampToken != null) {
                        timestampAttributeMap.put(timestampToken, signatureAttribute);
                        result.add(timestampToken);
                    }
                }

            } else {
                LOG.warn("'tstTokens' element is not found! Returns an empty array if timestamps.");
            }
        }
        return result;
    }

    private TimestampToken toTimestampToken(CBORMap tstToken, CBAdESAttribute signatureAttribute, Integer orderWithinAttribute,
                                            TimestampType timestampType, List<TimestampedReference> references) {
        if (tstToken != null) {
            String encoding = tstToken.getAsString(COSEConstants.TST_TOKEN_ENCODING);
            if (Utils.isStringNotEmpty(encoding)) {
                /*
                 * The tstToken's encoding member shall be an URI value and shall identify the encoding used for
                 * the time-stamp token. For IETF RFC 3161 [13] time-stamp tokens this member shall not be present.
                 */
                LOG.warn("Unsupported encoding of timestamp token '{}'. For IETF RFC 3161 time-stamp tokens " +
                        "the value of 'encoding' field shall not be present.", encoding);
                return null;
            }

            byte[] tstTokenVal = tstToken.getAsBinaries(COSEConstants.TST_TOKEN_VAL);
            if (tstTokenVal != null) {
                try {
                    final SignatureTimestampIdentifierBuilder identifierBuilder = new SignatureTimestampIdentifierBuilder(tstTokenVal)
                            .setSignature(signature)
                            .setAttribute(signatureAttribute)
                            .setOrderOfAttribute(getAttributeOrder(signatureAttribute))
                            .setOrderWithinAttribute(orderWithinAttribute);
                    return new TimestampToken(tstTokenVal, timestampType, references, identifierBuilder);

                } catch (Exception e) {
                    LOG.warn("Unable to create timestamp from base64-encoded string '{}'. Reason : {}",
                            Utils.toBase64(tstTokenVal), e.getMessage(), e);
                }

            } else {
                LOG.warn("No 'tstToken' value have been found. Reject the entry.");
            }

        }
        return null;
    }

    @Override
    protected List<EvidenceRecord> makeEvidenceRecords(CBAdESAttribute signatureAttribute, List<TimestampedReference> references) {
        return Collections.emptyList();
    }

    @Override
    protected List<CertificateRef> getCertificateRefs(CBAdESAttribute unsignedAttribute) {
        return Collections.emptyList();
    }

    @Override
    protected List<CRLRef> getCRLRefs(CBAdESAttribute unsignedAttribute) {
        return Collections.emptyList();
    }

    @Override
    protected List<OCSPRef> getOCSPRefs(CBAdESAttribute unsignedAttribute) {
        return Collections.emptyList();
    }

    @Override
    protected List<Identifier> getEncapsulatedCertificateIdentifiers(CBAdESAttribute unsignedAttribute) {
        return Collections.emptyList();
    }

    @Override
    protected List<CRLBinary> getEncapsulatedCRLIdentifiers(CBAdESAttribute unsignedAttribute) {
        return Collections.emptyList();
    }

    @Override
    protected List<OCSPResponseBinary> getEncapsulatedOCSPIdentifiers(CBAdESAttribute unsignedAttribute) {
        return Collections.emptyList();
    }

    @Override
    protected List<AdvancedSignature> getCounterSignatures(CBAdESAttribute unsignedAttribute) {
        return Collections.emptyList();
    }

    @Override
    protected ArchiveTimestampType getArchiveTimestampType(CBAdESAttribute unsignedAttribute) {
        return null;
    }

    @Override
    protected TimestampMessageDigestBuilder getTimestampMessageImprintDigestBuilder(DigestAlgorithm digestAlgorithm) {
        return null;
    }

    @Override
    protected TimestampMessageDigestBuilder getTimestampMessageImprintDigestBuilder(TimestampToken timestampToken) {
        return null;
    }

}
