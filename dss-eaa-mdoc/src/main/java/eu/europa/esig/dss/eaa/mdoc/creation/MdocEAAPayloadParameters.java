package eu.europa.esig.dss.eaa.mdoc.creation;

import eu.europa.esig.dss.eaa.common.creation.AbstractEAAPayloadParameters;
import eu.europa.esig.dss.eaa.common.creation.EAARevocationList;
import eu.europa.esig.dss.eaa.mdoc.creation.claim.MdocEAAClaim;
import eu.europa.esig.dss.model.x509.CertificateToken;

import java.security.PublicKey;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class MdocEAAPayloadParameters extends AbstractEAAPayloadParameters {

    /**
     * Contains a map between element namespaces and corresponding selectively disclosable claims
     */
    private final Map<String, List<MdocEAAClaim>> claimsMap = new HashMap<>();

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
    private EAARevocationList identifierList;

    /**
     * (Optional) Contains a "status_list" as defined in IETF draft-ietf-oauth-status-list-20.
     */
    private EAARevocationList statusList;

    public String getVersion() {
        return version;
    }

    public PublicKey getDeviceKey() {
        return deviceKey;
    }

    public Map<String, List<String>> getKeyAuthorizationsMap() {
        return keyAuthorizationsMap;
    }

    public Map<Integer, Object> getKeyInfoMap() {
        return keyInfoMap;
    }

    public String getDocType() {
        return docType;
    }

    public Date getSigned() {
        return signed;
    }

    public Date getValidFrom() {
        return validFrom;
    }

    public Date getValidUntil() {
        return validUntil;
    }

    public Date getExpectedUpdate() {
        return expectedUpdate;
    }

    public EAARevocationList getIdentifierList() {
        return identifierList;
    }

    public EAARevocationList getStatusList() {
        return statusList;
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
     * Adds a new selectively disclosable claim.
     * A hash will be computed for the claim.
     *
     * @param claim {@link MdocEAAClaim} to add
     */
    public void addClaim(MdocEAAClaim claim) {
        if (claim != null) {
            final List<MdocEAAClaim> claims = claimsMap.computeIfAbsent(claim.getNamespace(), k -> new ArrayList<>());
            claims.add(claim);
        }
    }

    public Map<String, List<MdocEAAClaim>> getClaimsMap() {
        return claimsMap;
    }
    
}
