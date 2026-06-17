package eu.europa.esig.dss.ws.eaa.creation.dto.parameters;

import eu.europa.esig.dss.ws.dto.RemoteCertificate;

/**
 * DTO containing a public key representation
 *
 */
public class RemotePublicKey {

    /** Public key */
    private byte[] publicKey;

    /** X.509 PKI Certificate */
    private RemoteCertificate certificate;

    /**
     * Default constructor
     */
    public RemotePublicKey() {
        // empty
    }

    public byte[] getPublicKey() {
        return publicKey;
    }

    public void setPublicKey(byte[] publicKey) {
        this.publicKey = publicKey;
    }

    public RemoteCertificate getCertificate() {
        return certificate;
    }

    public void setCertificate(RemoteCertificate certificate) {
        this.certificate = certificate;
    }

}
