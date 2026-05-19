package eu.europa.esig.dss.eaa.mdoc.creation;

import eu.europa.esig.dss.cbades.cbor.CBORArray;
import eu.europa.esig.dss.cbades.cbor.CBORByteString;
import eu.europa.esig.dss.cbades.cbor.CBORMap;
import eu.europa.esig.dss.cbades.cbor.CBORUtils;
import eu.europa.esig.dss.cbades.cose.COSEKeyFactory;
import eu.europa.esig.dss.cbades.cose.DefaultCOSEKeyFactory;
import eu.europa.esig.dss.eaa.common.creation.AbstractEAAPayloadBuilder;
import eu.europa.esig.dss.eaa.mdoc.MdocConstants;
import eu.europa.esig.dss.eaa.mdoc.creation.claim.MdocEAAClaim;
import eu.europa.esig.dss.enumerations.DigestAlgorithm;
import eu.europa.esig.dss.model.DSSDocument;
import eu.europa.esig.dss.model.InMemoryDocument;
import eu.europa.esig.dss.model.x509.CertificateToken;
import eu.europa.esig.dss.spi.DSSUtils;
import eu.europa.esig.dss.utils.Utils;

import java.io.Serializable;
import java.security.PublicKey;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * This class provides access to a configuration to build a payload for an ISO/IEC 18013-5 mdoc EAA.
 *
 */
public class MdocPayloadBuilder extends AbstractEAAPayloadBuilder {

    /**
     * Version of the "MobileSecurityObject" structure.
     * Default : "1.0"
     */
    private String version = "1.0";

    /**
     * Contains the public part of the key pair used for mdoc authentication.
     */
    private PublicKey deviceKey;

    /**
     * (Optional) A map between namespaces and corresponding data element identifiers, the device key may sign.
     */
    private Map<String, List<String>> keyAuthorizationsMap;

    /**
     * (Optional) May contain extra info about the key. Positive integers for KeyInfo labels are RFU.
     * If application specific extensions are present, they shall use negative integers for the labels.
     */
    private Map<Integer, Object> keyInfoMap;

    /**
     * The document type of the document and shall be identical to the DocType element in the mdoc response.
     * Example: "org.iso.18013.5.1.mDL" for a document conformant to ISO/IEC 18013-5; or
     *          "org.iso.23220.1.mID" for a document conformant to ISO/IEC 23220-2.
     */
    private String docType;

    /**
     * Defined when the signature of the MSO is created.
     * NOTE: the value is taken from the corresponding signature parameters, if not provided explicitly.
     */
    private Date signed;

    /**
     * Contains a date before which the MSO is not yet valid.
     * NOTE: the value is taken from the corresponding signature parameters, if not provided explicitly.
     */
    private Date validFrom;

    /**
     * Contains a date after which the MSO is no longer valid.
     * NOTE: the value is taken from the "notAfter" of the signing certificate, if applicable and not provided explicitly.
     */
    private Date validUntil;

    /**
     * (Optional) Contains a date at which the issuing authority expects to re-sign the MSO
     * (and potentially update the elements).
     */
    private Date expectedUpdate;

    /**
     * (Optional) Contains an "identifier_list".
     */
    private RevocationList identifierList;

    /**
     * (Optional) Contains a "status_list" as defined in IETF draft-ietf-oauth-status-list-20.
     */
    private RevocationList statusList;

    /**
     * The factory is used to build a representation of a COSE_Key from a {@code java.security.PublicKey}
     * Default : {@code DefaultCOSEKeyFactory}
     */
    private COSEKeyFactory coseKeyFactory = new DefaultCOSEKeyFactory();

    /**
     * Builds disclosures
     */
    private MdocDisclosureBuilder disclosureBuilder = new DefaultMdocDisclosureBuilder();

    /**
     * Contains a map between element namespaces and corresponding selectively disclosable claims
     */
    private final Map<String, List<MdocEAAClaim>> claimsMap = new HashMap<>();

    /**
     * Empty constructor
     */
    public MdocPayloadBuilder() {
        // empty
    }

    /**
     * Sets version of the "MobileSecurityObject" structure.
     * Default : "1.0" (structure conformant to ISO/IEC 18013-5:2021)
     *
     * @param version {@link String}
     */
    public void setVersion(String version) {
        Objects.requireNonNull(version, "Version cannot be null!");
        this.version = version;
    }

    /**
     * Sets the digest algorithm used for a hash calculation of the selectively disclosable claims
     * Default : DigestAlgorithm.SHA512
     *
     * @param digestAlgorithm {@link DigestAlgorithm}
     */
    public void setDigestAlgorithm(DigestAlgorithm digestAlgorithm) {
        Objects.requireNonNull(digestAlgorithm, "DigestAlgorithm cannot be null!");
        if (digestAlgorithm.getMSOId() == null) {
            throw new IllegalArgumentException(String.format(
                    "DigestAlgorithm '%s' is not supported for the ISO/IEC 18013-5 mdoc!", digestAlgorithm));
        }
        this.digestAlgorithm = digestAlgorithm;
    }

    /**
     * Sets the public part of the key pair used for mdoc authentication.
     *
     * @param deviceKey {@link PublicKey}
     */
    public void setDeviceKey(PublicKey deviceKey) {
        this.deviceKey = deviceKey;
    }

    /**
     * Sets the certificate token used for mdoc authentication.
     * NOTE: only the public key part of the token will be represented within the MSO object.
     *
     * @param certificateToken {@link CertificateToken}
     */
    public void setDeviceKey(CertificateToken certificateToken) {
        if (certificateToken != null) {
            setDeviceKey(certificateToken.getPublicKey());
        }
    }

    /**
     * (Optional) Sets a map between namespaces and corresponding data element identifiers, the device key may sign.
     *
     * @param keyAuthorizationsMap a map between namespaces and data element identifiers
     */
    public void setKeyAuthorizations(Map<String, List<String>> keyAuthorizationsMap) {
        this.keyAuthorizationsMap = keyAuthorizationsMap;
    }

    /**
     * (Optional) Sets a map containing extra information about the device key.
     * Positive integers for KeyInfo labels are RFU. If application
     * specific extensions are present, they shall use negative integers for the labels.
     * NOTE: value of {@code Object} type is a subject to support by the implementation.
     * It is recommended to use an implementation of a {@code eu.europa.esig.dss.cbades.cbor.CBORObject} if applicable.
     *
     * @param keyInfoMap a map between information identifiers and the corresponding data elements
     */
    public void setKeyInfo(Map<Integer, Object> keyInfoMap) {
        this.keyInfoMap = keyInfoMap;
    }

    /**
     * (Optional) allows modifying the default behavior for a COSE_Key computation from a {@code java.security.PublicKey}.
     * Default : an instance of {@code COSEKeyFactory} is used, relying on JDK 8 and BouncyCastle utility methods.
     *
     * @param coseKeyFactory {@link COSEKeyFactory}
     */
    public void setCoseKeyFactory(COSEKeyFactory coseKeyFactory) {
        Objects.requireNonNull(coseKeyFactory, "COSEKeyFactory cannot be null!");
        this.coseKeyFactory = coseKeyFactory;
    }

    /**
     * Sets a disclosure builder.
     * Default : {@code eu.europa.esig.dss.eaa.mdoc.creation.DefaultMdocDisclosureBuilder}
     *
     * @param disclosureBuilder {@link MdocDisclosureBuilder}
     */
    public void setDisclosureBuilder(MdocDisclosureBuilder disclosureBuilder) {
        Objects.requireNonNull(disclosureBuilder, "Disclosure builder cannot be null!");
        this.disclosureBuilder = disclosureBuilder;
    }

    /**
     * Adds a new selectively disclosable claim.
     * A hash will be computed for the claim.
     *
     * @param claim {@link MdocEAAClaim} to add
     * @return {@link MdocEAAClaim} this added claim
     */
    public MdocEAAClaim addClaim(MdocEAAClaim claim) {
        if (claim == null) {
            return null;
        }
        final List<MdocEAAClaim> claims = claimsMap.computeIfAbsent(claim.getNamespace(), k -> new ArrayList<>());
        if (claim.getDigestId() == null) {
            claim.setDigestId(getNextDigestId(claims));
        }
        if (claim.getSalt() == null) {
            claim.setSalt(saltGenerator.generateSalt());
        }
        claims.add(claim);
        return claim;
    }

    private int getNextDigestId(List<MdocEAAClaim> claims) {
        int digestId = claims.size() + 1;
        while (isDigestIdUsed(digestId, claims)) {
            ++digestId;
        }
        return digestId;
    }

    private boolean isDigestIdUsed(int digestId, List<MdocEAAClaim> claims) {
        return claims.stream().anyMatch(c -> digestId == c.getDigestId());
    }

    @Override
    public DSSDocument buildPayload() {
        CBORMap mso = buildMobileSecurityObject();
        CBORByteString msoBytes = CBORUtils.toCborBtsrWrappedTagged(mso);
        return new InMemoryDocument(CBORUtils.serializeCborObject(msoBytes));
    }

    /**
     * Builds a Mobile Security Object (MSO) as defined in "9.1.2.4 Signing method and structure for MSO"
     * {@code
     *   MobileSecurityObject = {
     *     "version" : tstr,                       ; Version of the MobileSecurityObject
     *     "digestAlgorithm" : tstr,               ; Message digest algorithm used
     *     "valueDigests" : ValueDigests,          ; Digests of all data elements per namespace
     *     "deviceKeyInfo" : DeviceKeyInfo,
     *     "docType" : tstr,                       ; docType as used in Documents
     *     "validityInfo" : ValidityInfo
     *   }
     * }
     *
     * @return {@link CBORMap}
     */
    protected CBORMap buildMobileSecurityObject() {
        Objects.requireNonNull(version, "Version cannot be null!");
        Objects.requireNonNull(digestAlgorithm, "DigestAlgorithm cannot be null!");
        Objects.requireNonNull(docType, "DocType cannot be null!");

        final CBORMap mso = new CBORMap();
        mso.put(MdocConstants.VERSION, version);
        mso.put(MdocConstants.DIGEST_ALGORITHM, digestAlgorithm.getMSOId());
        mso.put(MdocConstants.VALUE_DIGEST, buildValueDigests());
        mso.put(MdocConstants.DEVICE_KEY_INFO, buildDeviceKeyInfo());
        mso.put(MdocConstants.DOC_TYPE, docType);
        mso.put(MdocConstants.VALIDITY_INFO, buildValidityInfo());
        CBORMap status = buildStatus();
        if (status != null) {
            mso.put(MdocConstants.STATUS, status);
        }
        return mso;
    }

    /**
     * Builds a ValueDigests based on the set claims.
     * {@code
     *   ValueDigests = {
     *     + NameSpace => DigestIDs
     *   }
     * }
     *
     * @return {@link CBORMap}
     */
    protected CBORMap buildValueDigests() {
        if (Utils.isMapEmpty(claimsMap)) {
            throw new IllegalArgumentException("No claims has been provided! Please use method #addClaim to enrich the list.");
        }
        final CBORMap valueDigests = new CBORMap();
        for (Map.Entry<String, List<MdocEAAClaim>> claimsEntry : claimsMap.entrySet()) {
            String namespace = claimsEntry.getKey();
            List<MdocEAAClaim> claims = claimsEntry.getValue();
            valueDigests.put(namespace, buildDigestIDs(claims));
        }
        return valueDigests;
    }

    /**
     * Builds a DigestIDs structure.
     * {@code
     *   DigestIDs = {
     *     + DigestID => Digest
     *   }
     * }
     *
     * @return {@link CBORMap}
     */
    protected CBORMap buildDigestIDs(List<MdocEAAClaim> claims) {
        if (Utils.isCollectionEmpty(claims)) {
            throw new IllegalStateException("The list of claims is empty!");
        }
        final CBORMap digestIDs = new CBORMap();
        claims.forEach(c -> digestIDs.put(c.getDigestId(),
                DSSUtils.digest(digestAlgorithm, disclosureBuilder.build(c).getBytesToBeSigned())));
        return digestIDs;
    }

    /**
     * Builds a DeviceKeyInfo structure.
     * {@code
     *   DeviceKeyInfo = {
     *     "deviceKey" : DeviceKey
     *     ? "keyAuthorizations" : KeyAuthorizations,
     *     ? "keyInfo" : KeyInfo
     *   }
     *   DeviceKey = COSE_Key
     * }
     *
     * @return {@link CBORMap}
     */
    protected CBORMap buildDeviceKeyInfo() {
        Objects.requireNonNull(deviceKey, "DeviceKey shall be provided for an mdoc payload building!");
        final CBORMap deviceKeyInfo = new CBORMap();
        deviceKeyInfo.put(MdocConstants.DEVICE_KEY, coseKeyFactory.create(deviceKey));
        CBORMap keyAuthorizations = buildKeyAuthorizations();
        if (keyAuthorizations != null && !keyAuthorizations.isEmpty()) {
            deviceKeyInfo.put(MdocConstants.KEY_AUTHORIZATIONS, keyAuthorizations);
        }
        CBORMap keyInfo = buildKeyInfo();
        if (keyInfo != null && !keyInfo.isEmpty()) {
            deviceKeyInfo.put(MdocConstants.KEY_INFO, keyInfo);
        }
        return deviceKeyInfo;
    }

    /**
     * Builds a KeyAuthorizations structure.
     * {@code
     *   KeyAuthorizations = {
     *     ? "nameSpaces" : AuthorizedNameSpaces
     *     ? "dataElements" : AuthorizedDataElements
     *   }
     *   AuthorizedNameSpaces = [+ NameSpace]
     *   AuthorizedDataElements = {+ NameSpace => DataElementsArray}
     *   DataElementsArray = [+ DataElementIdentifier]
     * }
     *
     * @return {@link CBORMap}
     */
    protected CBORMap buildKeyAuthorizations() {
        if (Utils.isMapEmpty(keyAuthorizationsMap)) {
            return null;
        }
        final CBORMap keyAuthorizations = new CBORMap();
        keyAuthorizations.put(MdocConstants.NAMESPACES, new CBORArray(keyAuthorizationsMap.keySet()));
        CBORMap authorizedDataElements = new CBORMap();
        keyAuthorizationsMap.forEach((k, v) -> authorizedDataElements.put(k, new CBORArray(v)));
        keyAuthorizations.put(MdocConstants.DATA_ELEMENTS, authorizedDataElements);
        return keyAuthorizations;
    }

    /**
     * Builds a KeyInfo structure.
     * {@code
     *   KeyInfo = { * int => any}   ; Positive integers are RFU, negative integers may be used for
     *   proprietary use
     * }
     *
     * @return {@link CBORMap}
     */
    protected CBORMap buildKeyInfo() {
        if (Utils.isMapEmpty(keyInfoMap)) {
            return null;
        }
        final CBORMap keyInfo = new CBORMap();
        keyInfoMap.forEach(keyInfo::put);
        return keyInfo;
    }

    /**
     * Builds a ValidityInfo structure.
     * {@code
     *   ValidityInfo = {
     *     "signed" : tdate,
     *     "validFrom" : tdate,
     *     "validUntil" : tdate,
     *     ? "expectedUpdate" : tdate
     *   }
     * }
     *
     * @return {@link CBORMap}
     */
    protected CBORMap buildValidityInfo() {
        Objects.requireNonNull(signed, "signed date cannot be null!");
        Objects.requireNonNull(validFrom, "validFrom date cannot be null!");
        Objects.requireNonNull(validUntil, "validUntil date cannot be null!");

        final CBORMap validityInfo = new CBORMap();
        validityInfo.put(MdocConstants.SIGNED, signed);
        validityInfo.put(MdocConstants.VALID_FROM, validFrom);
        validityInfo.put(MdocConstants.VALID_UNTIL, validUntil);
        if (expectedUpdate != null) {
            validityInfo.put(MdocConstants.EXPECTED_UPDATE, expectedUpdate);
        }
        return validityInfo;
    }

    /**
     * Builds a Status structure.
     * NOTE: The "status" is not defined in ISO/IEC 18013-5:2021,
     * but referenced in the draft of the amendments to the EU Implementing Acts.
     * {@code
     *   Status = {
     *     ? "identifier_list”: IdentifierListInfo,
     *     ? "status_list”: StatusListInfo,
     *     * tstr => RFU
     *   }
     * }
     *
     * @return {@link CBORMap}
     */
    protected CBORMap buildStatus() {
        // TODO : review with the new revision of ISO/IEC 18013-5
        if (identifierList == null && statusList == null) {
            return null;
        }
        final CBORMap status = new CBORMap();
        CBORMap identifierListInfo = buildIdentifierListInfo();
        if (identifierListInfo != null) {
            status.put(MdocConstants.IDENTIFIER_LIST, identifierListInfo);
        }
        CBORMap statusListInfo = buildStatusListInfo();
        if (statusListInfo != null) {
            status.put(MdocConstants.STATUS_LIST, statusListInfo);
        }
        return status;
    }

    /**
     * Builds an IdentifierListInfo  structure.
     * {@code
     *   IdentifierListInfo = {
     *     "id": Identifier ,
     *     "uri": URI,
     *     ? "certificate": Certificate
     *     * tstr => RFU
     *   }
     * }
     *
     * @return {@link CBORMap}
     */
    protected CBORMap buildIdentifierListInfo() {
        if (identifierList == null) {
            return null;
        }
        final CBORMap identifierListInfo = new CBORMap();
        identifierListInfo.put(MdocConstants.IDENTIFIER_ID, identifierList.getIndex());
        identifierListInfo.put(MdocConstants.IDENTIFIER_URI, identifierList.getUri());
        if (identifierList.getCertificate() != null) {
            identifierListInfo.put(MdocConstants.IDENTIFIER_CERTIFICATE, identifierList.getCertificate().getEncoded());
        }
        return identifierListInfo;
    }

    /**
     * Builds an StatusListInfo structure.
     * {@code
     *   StatusListInfo = {
     *     "idx": Identifier,
     *     "uri": URI,
     *     ? "certificate": Certificate
     *     * tstr => RFU
     *   }
     * }
     *
     * @return {@link CBORMap}
     */
    protected CBORMap buildStatusListInfo() {
        if (statusList == null) {
            return null;
        }
        final CBORMap statusListInfo = new CBORMap();
        statusListInfo.put(MdocConstants.STATUS_IDX, statusList.getIndex());
        statusListInfo.put(MdocConstants.STATUS_URI, statusList.getUri());
        if (statusList.getCertificate() != null) {
            statusListInfo.put(MdocConstants.STATUS_CERTIFICATE, statusList.getCertificate().getEncoded());
        }
        return statusListInfo;
    }

    /**
     * Represents a status_list structure as specified in clause 6 of IETF draft-ietf-oauth-status-list-13.
     */
    protected class RevocationList implements Serializable {

        private static final long serialVersionUID = -8538801549100678146L;

        /** Non-negative integer representing the index check for status information in the Status List */
        private final int index;

        /** String value that identifies the Status List Token containing the status information */
        private final String uri;

        /** (Optional) Certificate containing the public key that signed or sealed the top-level certificate in the MSO revocation list structure */
        private final CertificateToken certificate;

        /**
         * Default constructor
         *
         * @param index integer
         * @param uri {@link String}
         */
        protected RevocationList(final int index, final String uri) {
            this(index, uri, null);
        }

        /**
         * Constructor with a certificate
         *
         * @param index integer
         * @param uri {@link String}
         */
        protected RevocationList(final int index, final String uri, final CertificateToken certificate) {
            Objects.requireNonNull(uri, "Uri cannot be null!");
            if (index < 0) {
                throw new IllegalArgumentException("Index shall be a non-negative integer!");
            }
            this.index = index;
            this.uri = uri;
            this.certificate = certificate;
        }

        /**
         * Gets index of the token within a status list
         *
         * @return non-negative integer
         */
        public int getIndex() {
            return index;
        }

        /**
         * Gets URI of the status list
         *
         * @return {@link String}
         */
        public String getUri() {
            return uri;
        }

        /**
         * Gets a certificate
         *
         * @return {@link CertificateToken}
         */
        public CertificateToken getCertificate() {
            return certificate;
        }

    }

}
