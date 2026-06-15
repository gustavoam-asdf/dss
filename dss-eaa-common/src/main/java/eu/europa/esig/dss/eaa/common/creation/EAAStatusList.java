package eu.europa.esig.dss.eaa.common.creation;

import eu.europa.esig.dss.model.x509.CertificateToken;

import java.io.Serializable;
import java.util.Objects;

/**
 * Represents a status_list structure as specified in clause 6 of IETF draft-ietf-oauth-status-list-13.
 *
 */
public class EAAStatusList implements Serializable {

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
    public EAAStatusList(final int index, final String uri) {
        this(index, uri, null);
    }

    /**
     * Constructor with a certificate
     *
     * @param index integer
     * @param uri {@link String}
     */
    public EAAStatusList(final int index, final String uri, final CertificateToken certificate) {
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

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;

        EAAStatusList that = (EAAStatusList) object;
        return index == that.index
                && Objects.equals(uri, that.uri)
                && Objects.equals(certificate, that.certificate);
    }

    @Override
    public int hashCode() {
        int result = index;
        result = 31 * result + Objects.hashCode(uri);
        result = 31 * result + Objects.hashCode(certificate);
        return result;
    }

    @Override
    public String toString() {
        return "EAAStatusList [" +
                "index=" + index +
                ", uri='" + uri + '\'' +
                ", certificate=" + certificate +
                ']';
    }

}