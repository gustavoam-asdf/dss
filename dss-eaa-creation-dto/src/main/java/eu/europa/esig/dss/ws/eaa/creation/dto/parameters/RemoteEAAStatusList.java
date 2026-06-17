package eu.europa.esig.dss.ws.eaa.creation.dto.parameters;

import eu.europa.esig.dss.ws.dto.RemoteCertificate;

/**
 * DTO representing a Token Status List claim
 *
 */
public class RemoteEAAStatusList {

    /** Non-negative integer representing the index check for status information in the Status List */
    private Integer index;

    /** String value that identifies the Status List Token containing the status information */
    private String uri;

    /** Certificate containing the public key that signed or sealed the top-level certificate in the MSO revocation list structure */
    private RemoteCertificate certificate;

    /** Type of the EAA status (ETSI specification only) */
    private String type;

    /** Purpose of the EAA status (ETSI specification only) */
    private String purpose;

    /**
     * Default constructor
     */
    public RemoteEAAStatusList() {
        // empty
    }

    /**
     * Constructor with index and uri provided
     *
     * @param index {@link Integer}
     * @param uri {@link String}
     */
    public RemoteEAAStatusList(Integer index, String uri) {
        this(index, uri, null);
    }

    /**
     * Constructor with index, uri and certificate provided
     *
     * @param index {@link Integer}
     * @param uri {@link String}
     * @param certificate {@link RemoteCertificate}
     */
    public RemoteEAAStatusList(Integer index, String uri, RemoteCertificate certificate) {
        this.index = index;
        this.uri = uri;
        this.certificate = certificate;
    }

    /**
     * Constructor with type, purpose index and uri provided (ETSI TS 119 472-1 v1.2.1 definition)
     *
     * @param type {@link String}
     * @param purpose {@link String}
     * @param index {@link Integer}
     * @param uri {@link String}
     */
    public RemoteEAAStatusList(String type, String purpose, Integer index, String uri) {
        this.type = type;
        this.purpose = purpose;
        this.index = index;
        this.uri = uri;
    }

    public Integer getIndex() {
        return index;
    }

    public void setIndex(Integer index) {
        this.index = index;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public RemoteCertificate getCertificate() {
        return certificate;
    }

    public void setCertificate(RemoteCertificate certificate) {
        this.certificate = certificate;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getPurpose() {
        return purpose;
    }

    public void setPurpose(String purpose) {
        this.purpose = purpose;
    }

}
