package eu.europa.esig.dss.cbades.validation;

import eu.europa.esig.dss.cbades.CBAdESSignatureIntegrityValidator;
import eu.europa.esig.dss.cbades.CBAdESUtils;
import eu.europa.esig.dss.cbades.COSEConstants;
import eu.europa.esig.dss.cbades.COSESignatureContext;
import eu.europa.esig.dss.cbades.COSEStructure;
import eu.europa.esig.dss.cbades.COSEUnprotectedHeader;
import eu.europa.esig.dss.cbades.cbor.CBORArray;
import eu.europa.esig.dss.cbades.cbor.CBORByteString;
import eu.europa.esig.dss.cbades.cbor.CBORMap;
import eu.europa.esig.dss.cbades.cbor.CBORObject;
import eu.europa.esig.dss.cbades.cbor.CBORSimpleObject;
import eu.europa.esig.dss.cbades.cbor.CBORUtils;
import eu.europa.esig.dss.cbades.validation.scope.CBAdESSignatureScopeFinder;
import eu.europa.esig.dss.cbades.validation.timestamp.CBAdESTimestampSource;
import eu.europa.esig.dss.enumerations.DigestAlgorithm;
import eu.europa.esig.dss.enumerations.DigestMatcherType;
import eu.europa.esig.dss.enumerations.EndorsementType;
import eu.europa.esig.dss.enumerations.SigDMechanism;
import eu.europa.esig.dss.enumerations.SignatureAlgorithm;
import eu.europa.esig.dss.enumerations.SignatureForm;
import eu.europa.esig.dss.enumerations.SignatureLevel;
import eu.europa.esig.dss.model.DSSDocument;
import eu.europa.esig.dss.model.DSSException;
import eu.europa.esig.dss.model.Digest;
import eu.europa.esig.dss.model.InMemoryDocument;
import eu.europa.esig.dss.model.ReferenceValidation;
import eu.europa.esig.dss.model.SignaturePolicyStore;
import eu.europa.esig.dss.model.SpDocSpecification;
import eu.europa.esig.dss.model.UserNotice;
import eu.europa.esig.dss.model.scope.SignatureScope;
import eu.europa.esig.dss.model.signature.CommitmentTypeIndication;
import eu.europa.esig.dss.model.signature.SignatureCryptographicVerification;
import eu.europa.esig.dss.model.signature.SignatureDigestReference;
import eu.europa.esig.dss.model.signature.SignaturePolicy;
import eu.europa.esig.dss.model.signature.SignatureProductionPlace;
import eu.europa.esig.dss.model.signature.SignerRole;
import eu.europa.esig.dss.spi.DSSUtils;
import eu.europa.esig.dss.spi.SignatureCertificateSource;
import eu.europa.esig.dss.spi.signature.AdvancedSignature;
import eu.europa.esig.dss.spi.signature.DefaultAdvancedSignature;
import eu.europa.esig.dss.spi.signature.identifier.SignatureIdentifierBuilder;
import eu.europa.esig.dss.spi.validation.CertificateVerifier;
import eu.europa.esig.dss.spi.x509.CandidatesForSigningCertificate;
import eu.europa.esig.dss.spi.x509.CertificateValidity;
import eu.europa.esig.dss.spi.x509.SignatureIntegrityValidator;
import eu.europa.esig.dss.spi.x509.revocation.crl.OfflineCRLSource;
import eu.europa.esig.dss.spi.x509.revocation.ocsp.OfflineOCSPSource;
import eu.europa.esig.dss.spi.x509.tsp.TimestampToken;
import eu.europa.esig.dss.utils.Utils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Represents an implementation of a CB-AdES ETSI TS 119 152-1 signature
 *
 */
public class CBAdESSignature extends DefaultAdvancedSignature {

    private static final Logger LOG = LoggerFactory.getLogger(CBAdESSignature.class);

    /** The COSE signature object */
    private final CBORSignature cose;

    /** The list of unsigned properties embedded into the 'uHeaders' array */
    private CBAdESUHeaders uHeaders;

    /** Defines if the validating signature is detached */
    private final boolean isDetached;

    /**
     * The counter signature component embedding the current signature
     * NOTE: used for counter signatures only
     */
    private CBAdESUHeadersComponent masterCounterSignatureComponent;

    /**
     * Default constructor
     *
     * @param cose {@link CBORSignature}
     */
    public CBAdESSignature(CBORSignature cose) {
        this.cose = cose;
        this.isDetached = cose.getPayload() == null || cose.getPayload().isNull();
    }

    /**
     * Sets externally supplied data as per RFC 9052 "4.3. Externally Supplied Data".
     * This is an optional protected element of COSE signatures.
     *
     * @param externallySuppliedData {@link DSSDocument}
     */
    public void setExternallySuppliedData(DSSDocument externallySuppliedData) {
        if (externallySuppliedData != null) {
            cose.setExternalAttributesBytes(DSSUtils.toByteArray(externallySuppliedData));
        } else {
            cose.setExternalAttributesBytes(null);
        }
    }

    /**
     * Returns the corresponding {@link CBORSignature} object
     *
     * @return {@link CBORSignature}
     */
    public CBORSignature getCoseSignature() {
        return cose;
    }

    /**
     * Gets context of the COSE signature
     *
     * @return {@link COSESignatureContext}
     */
    public COSESignatureContext getCOSESignatureContext() {
        return cose.getContext();
    }

    @Override
    public SignatureForm getSignatureForm() {
        return SignatureForm.CBAdES;
    }

    @Override
    public SignatureCertificateSource getCertificateSource() {
        if (offlineCertificateSource == null) {
            offlineCertificateSource = new CBAdESCertificateSource(cose, getUHeaders());
        }
        return offlineCertificateSource;
    }

    @Override
    public OfflineCRLSource getCRLSource() {
        if (signatureCRLSource == null) {
            signatureCRLSource = new CBAdESCRLSource(getUHeaders());
        }
        return signatureCRLSource;
    }

    @Override
    public OfflineOCSPSource getOCSPSource() {
        if (signatureOCSPSource == null) {
            signatureOCSPSource = new CBAdESOCSPSource(getUHeaders());
        }
        return signatureOCSPSource;
    }

    @Override
    public CBAdESTimestampSource getTimestampSource() {
        if (signatureTimestampSource == null) {
            signatureTimestampSource = new CBAdESTimestampSource(this);
        }
        return (CBAdESTimestampSource) signatureTimestampSource;
    }

    /**
     * Returns unsigned properties embedded into the 'uHeaders' array
     *
     * @return {@link CBAdESUHeaders}
     */
    public CBAdESUHeaders getUHeaders() {
        if (uHeaders == null) {
            uHeaders = new CBAdESUHeaders(cose);
        }
        return uHeaders;
    }

    /**
     * Gets a counter signature component embedding the current signature
     *
     * @return {@link CBAdESUHeadersComponent} 'cSig' embedding the current signature
     */
    public CBAdESUHeadersComponent getMasterCounterSignatureComponent() {
        return masterCounterSignatureComponent;
    }

    /**
     * Sets a 'cSig' component embedding the current signature
     *
     * @param masterCounterSignatureComponent {@link CBAdESUHeadersComponent} 'cSig' embedding the current signature
     */
    public void setMasterCounterSignatureComponent(CBAdESUHeadersComponent masterCounterSignatureComponent) {
        this.masterCounterSignatureComponent = masterCounterSignatureComponent;
    }

    @Override
    public SignatureAlgorithm getSignatureAlgorithm() {
        return cose.getAlgorithm();
    }

    @Override
    public Date getSigningTime() {
        Long sigT = cose.getProtectedHeaderValueAsLong(COSEConstants.SIG_T);
        if (sigT != null) {
            long timeValueInMilliseconds = DSSUtils.getTimeValueInMilliseconds(sigT);
            return DSSUtils.getDateFromMilliseconds(timeValueInMilliseconds);
        }
        LOG.debug("Unable to extract claimed signing-time: No 'sigT' header was found.");
        return null;
    }

    @Override
    public String getContentType() {
        // not applicable (see JAdES)
        return null;
    }

    @Override
    public String getMimeType() {
        /*
         * content type: This header parameter is used to indicate the content
         * type of the data in the "payload" or "ciphertext" field.  Integers
         * are from the "CoAP Content-Formats" IANA registry table
         * [COAP.Formats]. Text values follow the syntax of "<type-
         * name>/<subtype-name>", where <type-name> and <subtype-name> are
         * defined in Section 4.2 of [RFC6838].
         */
        String value = cose.getProtectedHeaderValueAsString(COSEConstants.CONTENT_TYPE);
        if (Utils.isStringEmpty(value)) {
            Long valueAsLong = cose.getProtectedHeaderValueAsLong(COSEConstants.CONTENT_TYPE);
            if (valueAsLong != null) {
                // TODO : add support of 'content type' of uint type
                LOG.warn("'content type' protected header of UnsignedInteger type is not supported by implementation");
            }
        }
        if (Utils.isStringEmpty(value)) {
            // sigD: return the first one when present
            List<String> ctys = getSignedDataContentTypeList();
            if (Utils.isCollectionNotEmpty(ctys)) {
                value = ctys.get(0);
            }
        }
        return value;
    }

    @Override
    public SignatureProductionPlace getSignatureProductionPlace() {
        CBORMap sigPl = cose.getProtectedHeaderValueAsMap(COSEConstants.SIG_PL);
        if (sigPl != null && !sigPl.isEmpty()) {
            SignatureProductionPlace result = new SignatureProductionPlace();
            result.setCountryName(sigPl.getAsString(COSEConstants.SIG_PL_ADDRESS_COUNTRY));
            result.setCity(sigPl.getAsString(COSEConstants.SIG_PL_ADDRESS_LOCALITY));
            result.setStateOrProvince(sigPl.getAsString(COSEConstants.SIG_PL_ADDRESS_REGION));
            result.setPostOfficeBoxNumber(sigPl.getAsString(COSEConstants.SIG_PL_POST_OFFICE_BOX_NUMBER));
            result.setPostalCode(sigPl.getAsString(COSEConstants.SIG_PL_POSTAL_CODE));
            result.setStreetAddress(sigPl.getAsString(COSEConstants.SIG_PL_STREET_ADDRESS));
            return result;
        }
        return null;
    }

    @Override
    public List<CommitmentTypeIndication> getCommitmentTypeIndications() {
        List<CommitmentTypeIndication> result = new ArrayList<>();
        CBORArray srCms = cose.getProtectedHeaderValueAsArray(COSEConstants.SR_CMS);
        if (srCms != null && !srCms.isEmpty()) {
            for (CBORObject srCm : srCms.getItems()) {
                if (!srCm.isMap()) {
                    LOG.warn("Item of 'srCms' protected header shall be a type of CBOR Map! Found object of class : {}",
                            srCm.getClass().getSimpleName());
                    continue;
                }
                CBORMap srCmMap = (CBORMap) srCm;
                if (!srCmMap.isEmpty()) {
                    CBORMap commId = srCmMap.getAsMap(COSEConstants.SR_CM_COMM_ID);
                    if (commId != null && !commId.isEmpty()) {
                        String oid = commId.getAsString(COSEConstants.OID_ID);
                        oid = DSSUtils.getObjectIdentifierValue(oid);
                        if (Utils.isStringNotBlank(oid)) {
                            CommitmentTypeIndication commitmentTypeIndication = new CommitmentTypeIndication(oid);
                            String desc = commId.getAsString(COSEConstants.OID_DESC);
                            commitmentTypeIndication.setDescription(desc);
                            CBORArray docRefs = commId.getAsArray(COSEConstants.OID_DOC_REFS);
                            if (docRefs != null && !docRefs.isEmpty()) {
                                commitmentTypeIndication.setDocumentReferences(docRefs.toListOfStrings());
                            }
                            result.add(commitmentTypeIndication);

                        } else {
                            LOG.warn("Id parameter in the OID with the value '{}' is not conformant! The entry is skipped.", oid);
                        }
                    }
                }
            }
        }
        return result;
    }

    @Override
    public List<SignerRole> getCertifiedSignerRoles() {
        List<SignerRole> result = new ArrayList<>();
        CBORMap srAts = getSignerAttributes();
        if (srAts != null && !srAts.isEmpty()) {
            CBORArray certifiedAttrs = srAts.getAsArray(COSEConstants.SR_ATS_CERTIFIED_ATTRS);
            if (certifiedAttrs != null && !certifiedAttrs.isEmpty()) {
                for (CBORObject certifiedItem : certifiedAttrs.getItems()) {
                    String certifiedVal = getCertifiedVal(certifiedItem);
                    if (Utils.isStringNotEmpty(certifiedVal)) {
                        result.add(new SignerRole(certifiedVal, EndorsementType.CERTIFIED));
                    }
                }
            }
        }
        return result;
    }

    private String getCertifiedVal(CBORObject certifiedItem) {
        if (!certifiedItem.isMap()) {
            LOG.warn("Item of a 'certifiedAttrs' shall be of type CBOR Map. Found type : {}", certifiedItem.getClass().getSimpleName());
            return null;
        }

        CBORMap certifiedItemMap = (CBORMap) certifiedItem;
        CBORMap x509AttrCert = certifiedItemMap.getAsMap(COSEConstants.CERTIFIED_ATTR_X509_ATTR_CERT);
        if (x509AttrCert != null && !x509AttrCert.isEmpty()) {
            byte[] pkiObVal = x509AttrCert.getAsBinaries(COSEConstants.PKI_OB_VAL);
            if (pkiObVal != null) {
                // TODO : support other encodings ?
                // DER encoding by default -> return b64
                return Utils.toBase64(pkiObVal);
            }
        }

        CBORMap otherAttrCert = certifiedItemMap.getAsMap(COSEConstants.CERTIFIED_ATTR_OTHER_ATTR_CERT);
        if (otherAttrCert != null && !otherAttrCert.isEmpty()) {
            LOG.warn("Unsupported 'otherAttrCert' type found.");
            return null;
        }

        LOG.warn("One of types 'x509AttrCert' or 'otherAttrCert' is expected in 'CertifiedAttr'.");
        return null;
    }

    @Override
    public List<SignerRole> getClaimedSignerRoles() {
        CBORMap srAts = getSignerAttributes();
        if (srAts != null && !srAts.isEmpty()) {
            CBORArray claimed = srAts.getAsArray(COSEConstants.SR_ATS_CLAIMED);
            if (claimed != null && !claimed.isEmpty()) {
                return getSignerRoles(claimed, EndorsementType.CLAIMED);
            }
        }
        return Collections.emptyList();
    }

    @Override
    public List<SignerRole> getSignedAssertions() {
        CBORMap srAts = getSignerAttributes();
        if (srAts != null && !srAts.isEmpty()) {
            CBORArray signedAssertions = srAts.getAsArray(COSEConstants.SR_ATS_SIGNED_ASSERTIONS);
            if (signedAssertions != null && !signedAssertions.isEmpty()) {
                return getSignerRoles(signedAssertions, EndorsementType.SIGNED);
            }
        }
        return Collections.emptyList();
    }

    private List<SignerRole> getSignerRoles(CBORArray attrArrays, EndorsementType category) {
        final List<SignerRole> result = new ArrayList<>();
        if (attrArrays != null && !attrArrays.isEmpty()) {
            /*
             * NotCertifiedItem = [
             * 	mediaType : tstr,	;String identifying the type of claimed attributes or signed assertions
             * 	encoding : tstr,	;String identifying the encoding of claimed attributes or signed assertions
             * 	qVals : [+any]	;Array with the claimed attributes or signed assertions
             * ]
             */
            for (CBORObject notCertifiedItem : attrArrays.getItems()) {
                try {
                    if (!notCertifiedItem.isArray()) {
                        LOG.warn("'NotCertifiedItem' item of 'AttrArrays' CBOR array shall be of CBOR Array type.");
                        continue;
                    }
                    CBORArray notCertifiedItemArray = (CBORArray) notCertifiedItem;
                    CBORArray qVals = notCertifiedItemArray.getAsArray(COSEConstants.NOT_CERTIFIED_ITEM_QVALS);
                    if (qVals != null) {
                        for (CBORObject val : qVals.getItems()) {
                            if (val.isUnicodeString()) {
                                result.add(new SignerRole(((CBORSimpleObject) val).getValueAsString(), category));
                            } else if (val.isByteString()) {
                                LOG.debug("Item of 'qVals' array is of ByteString type found. Return base64-encoded value.");
                                result.add(new SignerRole(Utils.toBase64(((CBORByteString) val).getBytes()), category));
                            } else if (val.isNegativeInteger() || val.isUnsignedInteger()) {
                                LOG.debug("Item of 'qVals' array is of NegativeInteger or UnsignedInteger type found. " +
                                        "Convert to String value.");
                                result.add(new SignerRole(String.valueOf(((CBORSimpleObject) val).getValueAsLong()), category));
                            } else {
                                LOG.warn("Unsupported type of item from 'qVals' array : {}", val.getClass().getSimpleName());
                            }
                        }
                    } else {
                        LOG.warn("Unable to extract 'qVals'. Shall be a CBOR Array at the position '{}' " +
                                "of a 'NotCertifiedItem' object.", COSEConstants.NOT_CERTIFIED_ITEM_QVALS);
                    }

                } catch (Exception e) {
                    String errorMessage = "An error occurred on 'attrArrays' processing : {}";
                    if (LOG.isDebugEnabled()) {
                        LOG.warn(errorMessage, e.getMessage(), e);
                    } else {
                        LOG.warn(errorMessage, e.getMessage());
                    }
                }

            }
        }
        return result;
    }

    private CBORMap getSignerAttributes() {
        return cose.getProtectedHeaderValueAsMap(COSEConstants.SR_ATS);
    }

    @Override
    protected SignaturePolicy buildSignaturePolicy() {
        CBORMap sigPId = cose.getProtectedHeaderValueAsMap(COSEConstants.SIG_PID);
        if (sigPId != null && !sigPId.isEmpty()) {
            CBORMap sigPOid = sigPId.getAsMap(COSEConstants.SIG_P_ID_ID);
            if (sigPOid != null && !sigPOid.isEmpty()) {
                String oid = sigPOid.getAsString(COSEConstants.OID_ID);
                oid = DSSUtils.getObjectIdentifierValue(oid);
                if (Utils.isStringNotBlank(oid)) {
                    signaturePolicy = new SignaturePolicy(oid);
                    String desc = sigPOid.getAsString(COSEConstants.OID_DESC);
                    signaturePolicy.setDescription(desc);
                    CBORArray docRefs = sigPOid.getAsArray(COSEConstants.OID_DOC_REFS);
                    if (docRefs != null && !docRefs.isEmpty()) {
                        signaturePolicy.setDocumentationReferences(docRefs.toListOfStrings());
                    }

                } else {
                    LOG.warn("Id parameter in the OID with the value '{}' is not conformant! " +
                            "The 'sigPId' entry cannot be extracted.", oid);
                    return null;
                }

                CBORArray digAlgVal = sigPId.getAsArray(COSEConstants.SIG_P_ID_DIG_ALG_VAL);
                signaturePolicy.setDigest(CBAdESUtils.getDigestAlgAndVal(digAlgVal));

                Boolean digPSp = sigPId.getAsBoolean(COSEConstants.SIG_P_ID_DIG_P_SP);
                if (digPSp != null) {
                    signaturePolicy.setHashAsInTechnicalSpecification(digPSp);
                }

                CBORArray qualifiers = sigPId.getAsArray(COSEConstants.SIG_P_ID_SIG_P_QUALS);
                if (qualifiers != null && !qualifiers.isEmpty()) {
                    signaturePolicy.setUri(getSPUri(qualifiers));
                    signaturePolicy.setUserNotice(getSPUserNotice(qualifiers));
                    signaturePolicy.setDocSpecification(getSPDSpec(qualifiers));
                }
            }

        }
        return signaturePolicy;
    }

    private String getSPUri(CBORArray qualifiers) {
        String spUri = null;
        for (CBORObject qualifier : qualifiers.getItems()) {
            if (!qualifier.isMap()) {
                LOG.warn("Item of 'sigPQuals' array shall be of type CBOR Map.");
                continue;
            }
            CBORMap qualifierMap = (CBORMap) qualifier;
            if (!qualifierMap.isEmpty()) {
                String spUriStr = qualifierMap.getAsString(COSEConstants.SP_URI);
                if (Utils.isStringNotEmpty(spUriStr)) {
                    if (spUri == null) {
                        spUri = spUriStr;
                    } else {
                        LOG.warn("Multiple 'spUri' qualifiers found. Only one entry is supported by the current implementation");
                    }
                }
            }
        }
        return spUri;
    }

    private UserNotice getSPUserNotice(CBORArray qualifiers) {
        UserNotice userNotice = null;
        for (CBORObject qualifier : qualifiers.getItems()) {
            if (!qualifier.isMap()) {
                // warn is logged before
                continue;
            }
            CBORMap qualifierMap = (CBORMap) qualifier;
            CBORMap spUserNotice = qualifierMap.getAsMap(COSEConstants.SP_USER_NOTICE);
            if (spUserNotice != null && !spUserNotice.isEmpty()) {
                if (userNotice != null) {
                    LOG.warn("Multiple 'spUserNotice' qualifiers found. Only one entry is supported by the current implementation");
                    continue;
                }

                try {
                    userNotice = new UserNotice();

                    // TODO : the standard is unclear with 'NoticeRef' type. Use CBOR Map as a more appropriate type.
                    CBORMap noticeRef = spUserNotice.getAsMap(COSEConstants.SP_USER_NOTICE_NOTICE_REF);
                    if (noticeRef != null && !noticeRef.isEmpty()) {
                        final String organization = noticeRef.getAsString(COSEConstants.NOTICE_REF_ORG);
                        if (Utils.isStringNotBlank(organization)) {
                            userNotice.setOrganization(organization);
                        }

                        final CBORArray noticeNumbers = noticeRef.getAsArray(COSEConstants.NOTICE_REF_NOT_NUMBERS);
                        if (noticeNumbers != null && !noticeNumbers.isEmpty()) {
                            userNotice.setNoticeNumbers(noticeNumbers.toListOfLongs().stream()
                                    .mapToInt(Number::intValue).toArray());
                        }
                    }
                    final String explTest = spUserNotice.getAsString(COSEConstants.SP_USER_NOTICE_EXPL_TEXT);
                    if (Utils.isStringNotBlank(explTest)) {
                        userNotice.setExplicitText(explTest);
                    }

                } catch (Exception e) {
                    String errorMessage = "Unable to build SPUserNotice qualifier. Reason : {}";
                    if (LOG.isDebugEnabled()) {
                        LOG.warn(errorMessage, e.getMessage(), e);
                    } else {
                        LOG.warn(errorMessage, e.getMessage());
                    }
                }
            }
        }
        return userNotice;
    }

    private SpDocSpecification getSPDSpec(CBORArray qualifiers) {
        SpDocSpecification spDocSpecification = null;
        for (CBORObject qualifier : qualifiers.getItems()) {
            if (!qualifier.isMap()) {
                // warn is logged before
                continue;
            }
            CBORMap qualifierMap = (CBORMap) qualifier;
            CBORMap spDSpec = qualifierMap.getAsMap(COSEConstants.SP_D_SPEC);
            if (spDSpec != null && !spDSpec.isEmpty()) {
                if (spDocSpecification != null) {
                    LOG.warn("Multiple 'spDSpec' qualifiers found. Only one entry is supported by the current implementation");
                    continue;
                }

                String oid = spDSpec.getAsString(COSEConstants.OID_ID);
                oid = DSSUtils.getObjectIdentifierValue(oid);
                if (Utils.isStringNotBlank(oid)) {
                    spDocSpecification = new SpDocSpecification();
                    spDocSpecification.setId(oid);
                    String desc = spDSpec.getAsString(COSEConstants.OID_DESC);
                    spDocSpecification.setDescription(desc);
                    CBORArray docRefs = spDSpec.getAsArray(COSEConstants.OID_DOC_REFS);
                    if (docRefs != null && !docRefs.isEmpty()) {
                        spDocSpecification.setDocumentationReferences(docRefs.toListOfStrings().toArray(new String[] {}));
                    }

                } else {
                    LOG.warn("Id parameter in the OID with the value '{}' is not conformant! " +
                            "The 'spDSpec' qualifier cannot be extracted.", oid);
                    return null;
                }
            }
        }
        return spDocSpecification;
    }

    /**
     * Checks if the CB-AdES Signature is detached (payload is not present within the signature structure)
     *
     * @return TRUE if the signature is detached, FALSE otherwise
     */
    public boolean isDetachedSignature() {
        return isDetached && !isCounterSignature();
    }

    /**
     * Returns a mechanism used in 'sigD' to cover a detached content
     *
     * @return {@link SigDMechanism}
     */
    public SigDMechanism getSigDMechanism() {
        CBORMap sigD = cose.getProtectedHeaderValueAsMap(COSEConstants.SIG_D);
        if (sigD != null && !sigD.isEmpty()) {
            String mechanismUri = sigD.getAsString(COSEConstants.SIG_D_MID);
            if (Utils.isStringNotEmpty(mechanismUri)) {
                SigDMechanism sigDMechanism = SigDMechanism.forCBAdESUri(mechanismUri);
                if (sigDMechanism == null) {
                    LOG.warn("The sigDMechanism with uri '{}' is not supported!", mechanismUri);
                }
                return sigDMechanism;
            } else {
                LOG.warn("No 'mId' field is found within 'sigD' protected header!");
            }
        }
        return null;
    }

    @Override
    public byte[] getSignatureValue() {
        return cose.getSignatureValue();
    }

    @Override
    public SignatureDigestReference getSignatureDigestReference(DigestAlgorithm digestAlgorithm) {
        // TODO : no definition is available -> build a signature structure based on its context
        COSEStructure coseSignStructure = COSESignatureContext.COSE_SIGN1 == cose.getContext() ? cose.getCoseSignStructure() : cose.getSignerSignature();
        byte[] serializedBytes = coseSignStructure.serialize();
        byte[] digestValue = DSSUtils.digest(digestAlgorithm, serializedBytes);
        return new SignatureDigestReference(new Digest(digestAlgorithm, digestValue));
    }

    @Override
    public Digest getDataToBeSignedRepresentation() {
        List<ReferenceValidation> referenceValidations = getReferenceValidations();
        for (ReferenceValidation referenceValidation : referenceValidations) {
            if (DigestMatcherType.COSE_SIG_STRUCTURE.equals(referenceValidation.getType())) {
                return referenceValidation.isFound() ? referenceValidation.getDigest() : null;
            }
        }
        // shall not happen
        throw new DSSException("COSE_SIG_STRUCTURE is not found! Unable to compute DTBSR.");
    }

    @Override
    protected SignatureIdentifierBuilder getSignatureIdentifierBuilder() {
        return new CBAdESSignatureIdentifierBuilder(this);
    }

    @Override
    public void checkSignatureIntegrity() {

        if (signatureCryptographicVerification != null) {
            return;
        }

        signatureCryptographicVerification = new SignatureCryptographicVerification();

        boolean refsFound = false;
        boolean refsIntact = false;

        List<ReferenceValidation> referenceValidations = getReferenceValidations();

        if (Utils.isCollectionNotEmpty(referenceValidations)) {
            refsFound = true;
            refsIntact = true;

            for (ReferenceValidation referenceValidation : referenceValidations) {
                if (DigestMatcherType.COSE_SIG_STRUCTURE.equals(referenceValidation.getType())) {
                    signatureCryptographicVerification.setSignatureIntact(referenceValidation.isIntact());

                    for (String errorMessage : referenceValidation.getErrorMessages()) {
                        signatureCryptographicVerification.setErrorMessage(errorMessage);
                    }
                }
                refsFound = refsFound && referenceValidation.isFound();
                refsIntact = refsIntact && referenceValidation.isIntact();
            }
        }

        signatureCryptographicVerification.setReferenceDataFound(refsFound);
        signatureCryptographicVerification.setReferenceDataIntact(refsIntact);

    }

    @Override
    public List<ReferenceValidation> getReferenceValidations() {
        if (referenceValidations == null) {
            referenceValidations = new ArrayList<>();

            ReferenceValidation signingInputReferenceValidation = getSigningInputReferenceValidation();
            referenceValidations.add(signingInputReferenceValidation);

            if (isDetachedSignature()) {
                List<ReferenceValidation> detachedReferenceValidations = getDetachedReferenceValidations();
                if (Utils.isCollectionNotEmpty(detachedReferenceValidations)) {
                    referenceValidations.addAll(detachedReferenceValidations);
                }
            }

            if (isCounterSignature()) {
                referenceValidations.add(getCounterSignatureReferenceValidation());
            }

        }
        return referenceValidations;
    }

    private ReferenceValidation getSigningInputReferenceValidation() {
        ReferenceValidation signatureValueReferenceValidation = new ReferenceValidation();
        signatureValueReferenceValidation.setType(DigestMatcherType.COSE_SIG_STRUCTURE);

        try {
            try {
                SigDMechanism sigDMechanism = getSigDMechanism();
                boolean singleDetachedDocumentProvided = Utils.collectionSize(detachedContents) == 1;
                if (!isDetachedSignature()) {
                    // not detached
                    signatureValueReferenceValidation.setFound(true);

                } else if (sigDMechanism == null && singleDetachedDocumentProvided) {
                    // simple detached signature
                    byte[] payload = DSSUtils.toByteArray(detachedContents.get(0));
                    cose.setPayloadBytes(payload);
                    signatureValueReferenceValidation.setFound(true);

                } else if (SigDMechanism.OBJECT_ID_BY_URI.equals(sigDMechanism)) {
                    // detached with OBJECT_ID_BY_URI mechanism
                    byte[] payload = getPayloadForObjectIdByUriMechanism();
                    cose.setPayloadBytes(payload);
                    signatureValueReferenceValidation.setFound(payload != null);

                } else if (SigDMechanism.OBJECT_ID_BY_URI_HASH.equals(sigDMechanism)) {
                    // the sigD itself is signed with OBJECT_ID_BY_URI_HASH mechanism
                    signatureValueReferenceValidation.setFound(true);

                } else {
                    // otherwise original content is not found
                    LOG.warn("The payload is not found! The detached content must be provided!");
                }

            } catch (Exception e) {
                String errorMessage = "Unable to determine a JWS payload. Reason : {}";
                if (LOG.isDebugEnabled()) {
                    LOG.warn(errorMessage, e.getMessage(), e);
                } else {
                    LOG.warn(errorMessage, e.getMessage());
                }
            }

            SignatureAlgorithm signatureAlgorithm = getSignatureAlgorithm();
            if (signatureAlgorithm != null) {
                byte[] dataToSign = cose.getSignatureInputBytes();
                DigestAlgorithm digestAlgorithm = signatureAlgorithm.getDigestAlgorithm();
                Digest digest = new Digest(digestAlgorithm, DSSUtils.digest(digestAlgorithm, dataToSign));
                signatureValueReferenceValidation.setDigest(digest);

                CandidatesForSigningCertificate candidatesForSigningCertificate = getCandidatesForSigningCertificate();

                SignatureIntegrityValidator signingCertificateValidator = new CBAdESSignatureIntegrityValidator(cose);
                CertificateValidity certificateValidity = signingCertificateValidator.validate(candidatesForSigningCertificate);
                if (certificateValidity != null) {
                    candidatesForSigningCertificate.setTheCertificateValidity(certificateValidity);
                }

                List<String> errorMessages = signingCertificateValidator.getErrorMessages();
                signatureValueReferenceValidation.setErrorMessages(errorMessages);
                signatureValueReferenceValidation.setIntact(certificateValidity != null);
            }

        } catch (Exception e) {
            LOG.warn("The validation of signed input failed! Reason : {}", e.getMessage(), e);
        }

        return signatureValueReferenceValidation;
    }

    private byte[] getPayloadForObjectIdByUriMechanism() {
        if (Utils.isCollectionEmpty(detachedContents)) {
            throw new IllegalArgumentException("The detached contents shall be provided for validating a detached signature!");
        }

        List<DSSDocument> signedDocumentsByUri = getSignedDocumentsForObjectIdByUriMechanism();
        return CBAdESUtils.concatenateDSSDocuments(signedDocumentsByUri);
    }

    /**
     * This method returns a list of documents for ObjectIdByUrl or ObjectIdByUriHash mechanisms
     * Keeps the original order according to 'pars' dictionary content
     *
     * @return a list of {@link DSSDocument}s
     */
    public List<DSSDocument> getSignedDocumentsForObjectIdByUriMechanism() {
        List<String> signedDataUriList = getSignedDataUriList();
        List<DSSDocument> signedDocumentsByUri = Collections.emptyList();
        if (Utils.collectionSize(signedDataUriList) == 1 && Utils.collectionSize(detachedContents) == 1) {
            signedDocumentsByUri = Collections.singletonList(detachedContents.iterator().next());

        } else if (Utils.isCollectionNotEmpty(signedDataUriList)) {
            signedDocumentsByUri = new ArrayList<>();
            for (String signedDataName : signedDataUriList) {
                DSSDocument detachedDocumentByName = getDetachedDocumentByName(signedDataName, detachedContents);
                if (detachedDocumentByName != null) {
                    signedDocumentsByUri.add(detachedDocumentByName);
                } else {
                    throw new IllegalArgumentException(String.format(
                            "The detached content for a signed data with name '%s' has not been found!", signedDataName));
                }
            }
        }
        return signedDocumentsByUri;
    }

    private List<String> getSignedDataUriList() {
        CBORMap sigD = cose.getProtectedHeaderValueAsMap(COSEConstants.SIG_D);
        if (sigD != null && !sigD.isEmpty()) {
            CBORArray pars = sigD.getAsArray(COSEConstants.SIG_D_PARS);
            if (pars != null && !pars.isEmpty()) {
                return pars.toListOfStrings();
            } else {
                LOG.warn("'pars' member of 'sigD' protected header shall be present and be of a CBOR Array type!");
            }
        }
        return Collections.emptyList();
    }

    private List<byte[]> getSignedDataHashList() {
        CBORMap sigD = cose.getProtectedHeaderValueAsMap(COSEConstants.SIG_D);
        if (sigD != null && !sigD.isEmpty()) {
            CBORArray hashV = sigD.getAsArray(COSEConstants.SIG_D_HASH_V);
            if (hashV != null && !hashV.isEmpty()) {
                return hashV.toListOfBinaries();
            } else {
                LOG.warn("'hashV' member of 'sigD' protected header shall be present and be of a CBOR Array type!");
            }
        }
        return Collections.emptyList();
    }

    private List<String> getSignedDataContentTypeList() {
        CBORMap sigD = cose.getProtectedHeaderValueAsMap(COSEConstants.SIG_D);
        if (sigD != null && !sigD.isEmpty()) {
            CBORArray ctys = sigD.getAsArray(COSEConstants.SIG_D_CTYS);
            if (ctys != null && !ctys.isEmpty()) {
                return ctys.toListOfStrings();
            } else {
                LOG.warn("'ctys' member of 'sigD' protected header shall be present and be of a CBOR Array type!");
            }
        }
        return Collections.emptyList();
    }

    private DSSDocument getDetachedDocumentByName(String documentName, List<DSSDocument> detachedContent) {
        documentName = DSSUtils.decodeURI(documentName);
        return DSSUtils.getDocumentWithName(detachedContent, documentName);
    }

    private List<ReferenceValidation> getDetachedReferenceValidations() {
        SigDMechanism sigDMechanism = getSigDMechanism();
        if (sigDMechanism != null) {
            switch (sigDMechanism) {
                case HTTP_HEADERS:
                case OBJECT_ID_BY_URI:
                    // the documents are added to the payload, not possible to extract separate reference validations
                    break;
                case OBJECT_ID_BY_URI_HASH:
                    return getReferenceValidationsByUriHashMechanism();
                default:
                    LOG.warn("The SigDMechanism '{}' is not supported!", sigDMechanism);
                    break;
            }
        }
        return Collections.emptyList();
    }

    private List<ReferenceValidation> getReferenceValidationsByUriHashMechanism() {
        List<DSSDocument> detachedDocuments = detachedContents;

        if (Utils.isCollectionEmpty(detachedContents)) {
            LOG.warn("The detached content is not provided! Validation of 'sigD' is not possible.");
            detachedDocuments = Collections.emptyList();
            // continue in order to extract signed data references
        }

        Map<String, byte[]> signedDataHashMap = getSignedDataUriHashMap();
        if (Utils.isMapEmpty(signedDataHashMap)) {
            LOG.warn("The SignedData has not been found or incorrect for detached content.");
            ReferenceValidation emptyReference = new ReferenceValidation();
            emptyReference.setType(DigestMatcherType.SIG_D_ENTRY);
            return Collections.singletonList(emptyReference);
        }

        DigestAlgorithm digestAlgorithm = getDigestAlgorithmForDetachedContent();
        if (digestAlgorithm == null) {
            LOG.warn("The DigestAlgorithm has not been found for the detached content.");
        }

        List<ReferenceValidation> detachedReferenceValidations = new ArrayList<>();

        for (Map.Entry<String, byte[]> signedDataEntry : signedDataHashMap.entrySet()) {
            ReferenceValidation referenceValidation = new ReferenceValidation();
            referenceValidation.setType(DigestMatcherType.SIG_D_ENTRY);

            String signedDataName = signedDataEntry.getKey();
            referenceValidation.setUri(signedDataName);

            byte[] expectedDigest = signedDataEntry.getValue();
            if (digestAlgorithm != null) {
                referenceValidation.setDigest(new Digest(digestAlgorithm, expectedDigest));
            }

            DSSDocument detachedDocument;
            if (Utils.collectionSize(signedDataHashMap.entrySet()) == 1 && Utils.collectionSize(detachedDocuments) == 1) {
                detachedDocument = detachedDocuments.iterator().next();
            } else {
                detachedDocument = getDetachedDocumentByDigest(digestAlgorithm, expectedDigest, detachedDocuments);
                if (detachedDocument == null) {
                    detachedDocument = getDetachedDocumentByName(signedDataName, detachedDocuments);
                }
            }

            if (detachedDocument != null) {
                referenceValidation.setFound(true);
                referenceValidation.setDocumentName(detachedDocument.getName());
                if (digestAlgorithm != null && isDocumentDigestMatch(detachedDocument, digestAlgorithm, expectedDigest)) {
                    referenceValidation.setIntact(true);
                }
            } else {
                LOG.warn("A detached document for the 'sigD' header with name '{}' has not been found!", signedDataName);
            }

            detachedReferenceValidations.add(referenceValidation);
        }

        if (Utils.isCollectionEmpty(detachedReferenceValidations)) {
            // add an empty reference if none found
            ReferenceValidation referenceValidation = new ReferenceValidation();
            referenceValidation.setType(DigestMatcherType.SIG_D_ENTRY);
            detachedReferenceValidations.add(referenceValidation);
        }

        return detachedReferenceValidations;
    }

    private DSSDocument getDetachedDocumentByDigest(DigestAlgorithm digestAlgorithm, byte[] expectedDigest, List<DSSDocument> detachedContent) {
        if (digestAlgorithm == null || expectedDigest == null) {
            return null;
        }
        for (DSSDocument detachedDocument : detachedContent) {
            if (isDocumentDigestMatch(detachedDocument, digestAlgorithm, expectedDigest)) {
                return detachedDocument;
            }
        }
        return null;
    }

    private boolean isDocumentDigestMatch(DSSDocument detachedDocument, DigestAlgorithm digestAlgorithm, byte[] expectedDigest) {
        return Arrays.equals(expectedDigest, detachedDocument.getDigestValue(digestAlgorithm));
    }

    private Map<String, byte[]> getSignedDataUriHashMap() {
        Map<String, byte[]> signedDataHashMap = new LinkedHashMap<>(); // LinkedHashMap is used to keep the original order

        List<String> signedDataUriList = getSignedDataUriList();
        List<byte[]> signedDataHashList = getSignedDataHashList();
        if (signedDataUriList.size() != signedDataHashList.size()) {
            LOG.warn("The size of 'pars' and 'hashV' dictionaries does not match! See '5.2.9 The sigD header parameter'.");
            return signedDataHashMap;
        }

        for (int ii = 0; ii < signedDataUriList.size(); ii++) {
            signedDataHashMap.put(signedDataUriList.get(ii), signedDataHashList.get(ii));
        }
        return signedDataHashMap;
    }

    private DigestAlgorithm getDigestAlgorithmForDetachedContent() {
        CBORMap sigD = cose.getProtectedHeaderValueAsMap(COSEConstants.SIG_D);
        if (sigD != null && !sigD.isEmpty()) {
            Long hashM = sigD.getAsLong(COSEConstants.SIG_D_HASH_M);
            if (hashM != null) {
                DigestAlgorithm digestAlgorithm = CBORUtils.getDigestAlgorithmForCoseId(hashM);
                if (digestAlgorithm != null) {
                    return digestAlgorithm;
                }
                LOG.warn("Not supported Digest Algorithm from 'sigD' protected header with Id : {}", hashM);

            } else {
                LOG.warn("'hashM' member of 'sigD' protected header shall be present for {} mechanism.",
                        SigDMechanism.OBJECT_ID_BY_URI_HASH.getCBAdESUri());
            }
        }
        return null;
    }

    private ReferenceValidation getCounterSignatureReferenceValidation() {
        ReferenceValidation referenceValidation = new ReferenceValidation();
        referenceValidation.setType(DigestMatcherType.COUNTER_SIGNED_SIGNATURE_VALUE);

        CBAdESSignature masterSignature = (CBAdESSignature) getMasterSignature();
        if (masterSignature != null) {

            byte[] signatureValue = masterSignature.getSignatureValue();
            if (Utils.isArrayNotEmpty(signatureValue)) {
                referenceValidation.setFound(true);
            }

            // signature value of the master signature can be embedded as a payload or other_fields,
            // depending on master signature type
            byte[] unverifiedBytes = cose.getOtherFieldsBytes();
            if (unverifiedBytes == null) {
                unverifiedBytes = cose.getPayloadBytes();
            }

            if (Utils.isArrayNotEmpty(unverifiedBytes)) {
                boolean intact = Arrays.equals(signatureValue, unverifiedBytes);
                if (!intact) {
                    LOG.warn("The payload of a countersignature with Id '{}' does not match the signature value of its master signature!",
                            getDSSId().asXmlId());
                }
                referenceValidation.setIntact(intact);
            } else {
                LOG.warn("No payload found for a countersignature with Id '{}'!", getDSSId().asXmlId());
                referenceValidation.setIntact(false);
            }
        }

        return referenceValidation;
    }

    /**
     * Returns a list of original documents signed by the signature
     *
     * @return a list of {@link DSSDocument}s
     */
    public List<DSSDocument> getOriginalDocuments() {
        if (isCounterSignature()) {
            final List<DSSDocument> originalDocuments = new ArrayList<>();
            CBAdESSignature masterSignature = (CBAdESSignature) getMasterSignature();
            originalDocuments.add(new InMemoryDocument(masterSignature.getSignatureValue()));
            if (COSESignatureContext.COSE_SIGN1 == masterSignature.getCOSESignatureContext()) {
                originalDocuments.addAll(masterSignature.getOriginalDocuments());
            }
            return originalDocuments;

        } else if (isDetachedSignature()) {

            List<DSSDocument> originalDocuments = new ArrayList<>();

            List<ReferenceValidation> referenceValidations = getReferenceValidations();
            for (ReferenceValidation referenceValidation : referenceValidations) {
                if (DigestMatcherType.SIG_D_ENTRY.equals(referenceValidation.getType()) && referenceValidation.isIntact()) {
                    String signedDataName = DSSUtils.decodeURI(referenceValidation.getUri());
                    DSSDocument detachedDocument = getDetachedDocumentByName(signedDataName, detachedContents);
                    if (detachedDocument != null) {
                        originalDocuments.add(detachedDocument);
                    }
                }
            }

            if (Utils.isCollectionEmpty(originalDocuments)) {
                // check if the signature of an old detached format
                SignatureCryptographicVerification signatureCryptographicVerification = getSignatureCryptographicVerification();
                if (signatureCryptographicVerification.isSignatureIntact()) {
                    if (isDetachedSignature() && Utils.collectionSize(detachedContents) == 1) {
                        return Collections.singletonList(detachedContents.get(0));
                    } else if (SigDMechanism.OBJECT_ID_BY_URI.equals(getSigDMechanism())) {
                        return getSignedDocumentsForObjectIdByUriMechanism();
                    }
                }
            }

            return originalDocuments;

        } else {
            byte[] payloadBytes = cose.getPayloadBytes();
            return Collections.singletonList(new InMemoryDocument(payloadBytes));
        }
    }

    @Override
    public SignatureLevel getDataFoundUpToLevel() {
        if (!hasBProfile()) {
            return SignatureLevel.CBOR_NOT_ETSI;
        }
        if (!hasTProfile()) {
            return SignatureLevel.CB_AdES_BASELINE_B;
        }
        if (hasLTProfile()) {
            if (hasLTAProfile()) {
                return SignatureLevel.CB_AdES_BASELINE_LTA;
            }
            return SignatureLevel.CB_AdES_BASELINE_LT;
        }
        return SignatureLevel.CB_AdES_BASELINE_T;
    }

    @Override
    protected CBAdESBaselineRequirementsChecker createBaselineRequirementsChecker(CertificateVerifier certificateVerifier) {
        return new CBAdESBaselineRequirementsChecker(this, certificateVerifier);
    }

    @Override
    protected List<SignatureScope> findSignatureScopes() {
        return new CBAdESSignatureScopeFinder().findSignatureScope(this);
    }

    @Override
    public SignaturePolicyStore getSignaturePolicyStore() {
        // TODO : not implemented
        return null;
    }

    @Override
    public List<AdvancedSignature> getCounterSignatures() {
        if (counterSignatures != null) {
            return counterSignatures;
        }
        counterSignatures = new ArrayList<>();

        COSEUnprotectedHeader bodyUnprotectedHeader = cose.getBodyUnprotectedHeader();
        COSEUnprotectedHeader signerUnprotectedHeader = cose.getSignerUnprotectedHeader();
        for (COSESignatureContext coseContext : COSESignatureContext.values()) {
            Long headerKey = coseContext.getCounterSignatureHeaderKey();
            if (headerKey != null) {
                CBORObject headerValue = null;
                boolean bodyStructure = false;
                if (signerUnprotectedHeader != null) {
                    headerValue = signerUnprotectedHeader.getHeader(headerKey);
                }
                if (headerValue == null && bodyUnprotectedHeader != null && !isCounterSignature()) {
                    headerValue = bodyUnprotectedHeader.getHeader(headerKey);
                    bodyStructure = true;
                }
                if (headerValue != null) { // is present
                    counterSignatures.addAll(CBAdESUtils.buildCounterSignatures(this, headerKey, headerValue, bodyStructure));
                }
            }
        }

        List<CBAdESUHeadersComponent> uHeaders = getUHeaders().getAttributes();
        if (Utils.isCollectionNotEmpty(uHeaders)) {
            for (CBAdESUHeadersComponent uHeader : uHeaders) {
                counterSignatures.addAll(CBAdESUtils.buildCounterSignatures(this, uHeader));
            }
        }
        return counterSignatures;
    }

    @Override
    public String getDAIdentifier() {
        // not applicable for CB-AdES
        return null;
    }

    @Override
    public void addExternalTimestamp(TimestampToken timestamp) {
        throw new UnsupportedOperationException("The method addExternalTimestamp(timestamp) is not supported for CB-AdES!");
    }

}
