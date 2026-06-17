package eu.europa.esig.dss.ws.eaa.creation.dto.parameters;

import eu.europa.esig.dss.enumerations.DigestAlgorithm;
import eu.europa.esig.dss.enumerations.EAAType;
import eu.europa.esig.dss.ws.dto.RemoteDocument;

import java.util.Date;
import java.util.List;

/**
 * DTO for key binding signature parameters
 *
 */
public class RemoteKeyBindingParameters {

    /** (Required) Type of the EAA to be created */
    private EAAType eaaType;

    /* SD-JWT VC parameters */

    /** DigestAlgorithm used to compute the hash for the key binding signature, it should the same value as the digest algorithm of the EAA */
    private DigestAlgorithm digestAlgorithm;

    /** Issuance time of the key binding signature */
    private Date issuanceTime;

    /** Intended receiver of the key binding */
    private String audience;

    /** Nonce of the key binding */
    private String nonce;

    /* Mdoc parameters */

    /** The session transcript to use for the creation of the key binding signature */
    private RemoteDocument sessionTranscript;

    /** Doc type to use for the key binding signature, the value should be the same as in EAA */
    private String docType;

    /** The list of device signed data elements */
    private List<ClaimDTO> deviceSignedDataElements;

    /**
     * Default constructor
     */
    public RemoteKeyBindingParameters() {
        super();
    }

    /**
     * Constructor with EAA type provided
     */
    public RemoteKeyBindingParameters(EAAType eaaType) {
        this.eaaType = eaaType;
    }

    /**
     * Gets the EAA Type
     *
     * @return {@link EAAType}
     */
    public EAAType getEaaType() {
        return eaaType;
    }

    /**
     * Sets the target EAA type
     *
     * @param eaaType {@link EAAType}
     */
    public void setEaaType(EAAType eaaType) {
        this.eaaType = eaaType;
    }

    /**
     * Gets the digest algorithm used to compute the hash for the key binding signature
     *
     * @return {@link DigestAlgorithm}
     */
    public DigestAlgorithm getDigestAlgorithm() {
        return digestAlgorithm;
    }

    /**
     * (SD-JWT VC) Sets the digest algorithm used to compute the hash for the key binding signature
     *
     * @param digestAlgorithm {@link DigestAlgorithm}
     */
    public void setDigestAlgorithm(final DigestAlgorithm digestAlgorithm) {
        this.digestAlgorithm = digestAlgorithm;
    }

    /**
     * Gets the issuance time of the key binding signature
     *
     * @return {@link Date}
     */
    public Date getIssuanceTime() {
        return issuanceTime;
    }

    /**
     * (SD-JWT VC) Sets the issuance time of the key binding signature
     *
     * @param issuanceTime {@link Date}
     */
    public void setIssuanceTime(final Date issuanceTime) {
        this.issuanceTime = issuanceTime;
    }

    /**
     * Gets the intended receiver of the key binding
     *
     * @return {@link String}
     */
    public String getAudience() {
        return audience;
    }

    /**
     * (SD-JWT VC) Sets the intended receiver of the key binding
     *
     * @param audience {@link String}
     */
    public void setAudience(final String audience) {
        this.audience = audience;
    }

    /**
     * Gets the nonce of the key binding
     *
     * @return {@link String}
     */
    public String getNonce() {
        return nonce;
    }

    /**
     * (SD-JWT VC) Sets the nonce of the key binding
     *
     * @param nonce {@link String}
     */
    public void setNonce(final String nonce) {
        this.nonce = nonce;
    }

    /**
     * Gets the session transcript to use for the creation of the key binding signature
     *
     * @return {@link RemoteDocument}
     */
    public RemoteDocument getSessionTranscript() {
        return sessionTranscript;
    }

    /**
     * (Mdoc) Sets the session transcript to use for the creation of the key binding signature
     *
     * @param sessionTranscript {@link RemoteDocument}
     */
    public void setSessionTranscript(final RemoteDocument sessionTranscript) {
        this.sessionTranscript = sessionTranscript;
    }

    /**
     * Gets the document type to use for the key binding signature
     *
     * @return {@link String}
     */
    public String getDocType() {
        return docType;
    }

    /**
     * (Mdoc) Sets the document type to use for the key binding signature
     *
     * @param docType {@link String}
     */
    public void setDocType(final String docType) {
        this.docType = docType;
    }

    /**
     * Gets the list of device signed data elements
     *
     * @return {@link List<ClaimDTO>}
     */
    public List<ClaimDTO> getDeviceSignedDataElements() {
        return deviceSignedDataElements;
    }

    /**
     * (Mdoc) Sets the list of device signed data elements
     *
     * @param deviceSignedDataElements {@link List<ClaimDTO>}
     */
    public void setDeviceSignedDataElements(final List<ClaimDTO> deviceSignedDataElements) {
        this.deviceSignedDataElements = deviceSignedDataElements;
    }

}
