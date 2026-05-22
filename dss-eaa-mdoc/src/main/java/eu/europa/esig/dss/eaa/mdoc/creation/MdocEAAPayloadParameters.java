package eu.europa.esig.dss.eaa.mdoc.creation;

import eu.europa.esig.dss.eaa.common.creation.AbstractEAAPayloadParameters;
import eu.europa.esig.dss.eaa.common.creation.EAARevocationList;
import eu.europa.esig.dss.model.x509.CertificateToken;

import java.security.PublicKey;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * Provides a payload configuration for the ISO/IEC 18013-5 mdoc
 *
 */
public class MdocEAAPayloadParameters extends AbstractEAAPayloadParameters {

    /* MobileSecurityObject claims */

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
     * (Optional) Contains a date at which the issuing authority expects to re-sign the MSO
     * (and potentially update the elements).
     */
    private Date expectedUpdate;

    /**
     * (Optional) Contains an "identifier_list".
     */
    private EAARevocationList identifierList;

    /**
     * (Optional) Contains a "status_list" as defined in IETF draft-ietf-oauth-status-list-20.
     */
    private EAARevocationList statusList;

    /* ETSI technical claims */

    /**
     * Category of the EAA (e.g. QEAA, Pub-EAA, or other)
     */
    private String category;

    /**
     * Whether the EAA is short-lived
     */
    private boolean shortLived;

    /**
     * Whether the EAA is issued for a one time use
     */
    private boolean oneTime;

    /**
     * Contains other optional selectively disclosable parameters
     */
    private MdocSelectivelyDisclosableParameters selectivelyDisclosableParameters;

    /**
     * Gets version of the "MobileSecurityObject" structure.
     *
     * @return {@link String}
     */
    public String getVersion() {
        return version;
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
     * Gets the public part of the key pair used for mdoc authentication.
     *
     * @return {@link PublicKey}
     */
    public PublicKey getDeviceKey() {
        return deviceKey;
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
     * Gets a map between namespaces and corresponding data element identifiers, the device key may sign.
     *
     * @return a map between namespaces and data element identifiers
     */
    public Map<String, List<String>> getKeyAuthorizationsMap() {
        return keyAuthorizationsMap;
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
     * Gets a map containing extra information about the device key.
     *
     * @return a map between information identifiers and the corresponding data elements
     */
    public Map<Integer, Object> getKeyInfoMap() {
        return keyInfoMap;
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
     * Gets the document type.
     *
     * @return {@link String}
     */
    public String getDocType() {
        return docType;
    }

    /**
     * Sets the document type.
     * If not defined, the docType will be derived from the list of provided claims, if applicable.
     *
     * @param docType {@link String}
     */
    public void setDocType(String docType) {
        this.docType = docType;
    }

    /**
     * Gets the date when the EAA was signed
     *
     * @return {@link Date}
     */
    public Date getSigned() {
        return signed;
    }

    /**
     * Sets the date when the EAA was signed.
     * For the ISO/IEC 18013-5 mdoc this value corresponds to the ValidityInfo.signed date.
     *
     * @param signed {@link Date}
     */
    public void setSigned(Date signed) {
        this.signed = signed;
    }

    /**
     * Gets the date from which the EAA is valid
     *
     * @return {@link Date}
     */
    public Date getValidFrom() {
        return getNotBeforeDate();
    }

    /**
     * Sets the EAA notBefore date (technical validity start date).
     * For the ISO/IEC 18013-5 mdoc this value corresponds to the ValidityInfo.validFrom date.
     *
     * @param validFrom {@link Date}
     */
    public void setValidFrom(Date validFrom) {
        setNotBeforeDate(validFrom);
    }

    /**
     * Gets the date after which the EAA is no longer valid
     *
     * @return {@link Date}
     */
    public Date getValidUntil() {
        return getExpirationDate();
    }

    /**
     * Sets the EAA expiration date (technical validity end date).
     * For the ISO/IEC 18013-5 mdoc this value corresponds to the ValidityInfo.validFrom date.
     *
     * @param expirationDate {@link Date}
     */
    public void setValidUntil(Date expirationDate) {
        setExpirationDate(expirationDate);
    }

    /**
     * Gets the expected update date.
     *
     * @return {@link Date}
     */
    public Date getExpectedUpdate() {
        return expectedUpdate;
    }

    /**
     * Sets the date when EAA issuer expects the EAA or associated data to be updated.
     * For the ISO/IEC 18013-5 mdoc this value corresponds to the ValidityInfo.expectedUpdate date.
     *
     * @param expectedUpdate {@link Date}
     */
    public void setExpectedUpdate(Date expectedUpdate) {
        this.expectedUpdate = expectedUpdate;
    }

    /**
     * Gets the identifier_list
     *
     * @return {@link EAARevocationList}
     */
    public EAARevocationList getIdentifierList() {
        return identifierList;
    }

    /**
     * Sets the identifier_list
     *
     * @param identifierList {@link EAARevocationList}
     */
    public void setIdentifierList(EAARevocationList identifierList) {
        this.identifierList = identifierList;
    }

    /**
     * Sets the identifier_list, by specifying an index of the EAA and a status distribution URL
     *
     * @param index integer representing an EAA identifier within the identifier_list
     * @param url {@link String} where the identifier_list can be accessed from
     */
    public void setIdentifierList(int index, String url) {
        this.identifierList = new EAARevocationList(index, url);
    }

    /**
     * Sets the identifier_list, by specifying an index of the EAA and a status distribution URL
     *
     * @param index integer representing an EAA identifier within the identifier_list
     * @param url {@link String} where the identifier_list can be accessed from
     * @param certificateToken {@link CertificateToken} containing the public key that signed or sealed
     *                         the top-level certificate in the x5chain element in the MSO revocation list structure
     */
    public void setIdentifierList(int index, String url, CertificateToken certificateToken) {
        this.identifierList = new EAARevocationList(index, url, certificateToken);
    }

    /**
     * Gets the status_list
     *
     * @return {@link EAARevocationList}
     */
    public EAARevocationList getStatusList() {
        return statusList;
    }

    /**
     * Sets the status_list
     *
     * @param statusList {@link EAARevocationList}
     */
    public void setStatusList(EAARevocationList statusList) {
        this.statusList = statusList;
    }

    /**
     * Sets the status_list, by specifying an index of the EAA and a status distribution URL
     *
     * @param index integer representing an EAA identifier within the status_list
     * @param url {@link String} where the status_list can be accessed from
     */
    public void setStatusList(int index, String url) {
        this.identifierList = new EAARevocationList(index, url);
    }

    /**
     * Sets the status_list, by specifying an index of the EAA and a status distribution URL
     *
     * @param index integer representing an EAA identifier within the status_list
     * @param url {@link String} where the status_list can be accessed from
     * @param certificateToken {@link CertificateToken} containing the public key that signed or sealed
     *                         the top-level certificate in the x5chain element in the MSO revocation list structure
     */
    public void setStatusList(int index, String url, CertificateToken certificateToken) {
        this.identifierList = new EAARevocationList(index, url, certificateToken);
    }

    @Override
    public boolean isOneTime() {
        return oneTime;
    }

    @Override
    public void setOneTime(boolean oneTime) {
        this.oneTime = oneTime;
    }

    @Override
    public boolean isShortLived() {
        return shortLived;
    }

    @Override
    public void setShortLived(boolean shortLived) {
        this.shortLived = shortLived;
    }
    /**
     * Gets the EAA category URN
     *
     * @return {@link String}
     */
    public String getCategory() {
        return category;
    }

    /**
     * Sets the EAA category URN.
     * Example: "urn:etsi:esi:eaa:eu:qualified" for QEAA, "urn:etsi:esi:eaa:eu:pub" for Pub-EAA
     *
     * @param category {@link String}
     */
    public void setCategory(String category) {
        this.category = category;
    }

    /**
     * Gets parameters containing configuration of selectively disclosable claims
     *
     * @return {@link MdocSelectivelyDisclosableParameters}
     */
    public MdocSelectivelyDisclosableParameters selectivelyDisclosable() {
        if (selectivelyDisclosableParameters == null) {
            selectivelyDisclosableParameters = new MdocSelectivelyDisclosableParameters();
        }
        return selectivelyDisclosableParameters;
    }
    
}
