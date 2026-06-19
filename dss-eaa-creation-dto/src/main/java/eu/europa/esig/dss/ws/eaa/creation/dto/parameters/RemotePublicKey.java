package eu.europa.esig.dss.ws.eaa.creation.dto.parameters;

import eu.europa.esig.dss.ws.dto.RemoteCertificate;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Objects;

/**
 * DTO containing a public key representation
 *
 */
public class RemotePublicKey implements Serializable {

    private static final long serialVersionUID = 5446356296735375771L;

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

    @Override
    public String toString() {
        return "RemotePublicKey [" +
                "publicKey=" + Arrays.toString(publicKey) +
                ", certificate=" + certificate +
                ']';
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;

        RemotePublicKey publicKey1 = (RemotePublicKey) object;
        return Arrays.equals(publicKey, publicKey1.publicKey)
                && Objects.equals(certificate, publicKey1.certificate);
    }

    @Override
    public int hashCode() {
        int result = Arrays.hashCode(publicKey);
        result = 31 * result + Objects.hashCode(certificate);
        return result;
    }

}
