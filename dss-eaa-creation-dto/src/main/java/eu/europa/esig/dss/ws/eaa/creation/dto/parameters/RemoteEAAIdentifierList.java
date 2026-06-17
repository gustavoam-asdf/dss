package eu.europa.esig.dss.ws.eaa.creation.dto.parameters;

import eu.europa.esig.dss.ws.dto.RemoteCertificate;

/**
 * DTO representing a Token identifier list
 *
 */
public class RemoteEAAIdentifierList {

    /** Byte array representing the index to check for status information in the Status List */
    private byte[] identifier;

    /** String value that identifies the Status List Token containing the status information */
    private String uri;

    /** (Optional) Certificate containing the public key that signed or sealed the top-level certificate in the MSO revocation list structure */
    private RemoteCertificate certificate;

    /**
     * Default constructor
     */
    public RemoteEAAIdentifierList() {
        // empty
    }

    /**
     * Constructor with index and uri provided
     *
     * @param identifier byte array
     * @param uri {@link String}
     */
    public RemoteEAAIdentifierList(byte[] identifier, String uri) {
        this(identifier, uri, null);
    }

    /**
     * Constructor with index, uri and certificate provided
     *
     * @param identifier byte array
     * @param uri {@link String}
     * @param certificate {@link RemoteCertificate}
     */
    public RemoteEAAIdentifierList(byte[] identifier, String uri, RemoteCertificate certificate) {
        this.identifier = identifier;
        this.uri = uri;
        this.certificate = certificate;
    }

    public byte[] getIdentifier() {
        return identifier;
    }

    public void setIdentifier(byte[] identifier) {
        this.identifier = identifier;
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

}
