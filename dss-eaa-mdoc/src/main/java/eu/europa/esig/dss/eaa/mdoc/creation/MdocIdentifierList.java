package eu.europa.esig.dss.eaa.mdoc.creation;

import eu.europa.esig.dss.model.x509.CertificateToken;

import java.io.Serializable;
import java.util.Objects;

/**
 * Provides configuration of the Identifiers List as defined in ISO/IEC 18013-5 "12.3.6.4 Identifier list details"
 *
 */
public class MdocIdentifierList implements Serializable {

    private static final long serialVersionUID = -8538801549100678146L;

    /** Byte array representing the index to check for status information in the Status List */
    private final byte[] identifier;

    /** String value that identifies the Status List Token containing the status information */
    private final String uri;

    /** (Optional) Certificate containing the public key that signed or sealed the top-level certificate in the MSO revocation list structure */
    private final CertificateToken certificate;

    /**
     * Default constructor
     *
     * @param identifier integer
     * @param uri {@link String}
     */
    public MdocIdentifierList(final byte[] identifier, final String uri) {
        this(identifier, uri, null);
    }

    /**
     * Constructor with a certificate
     *
     * @param identifier integer
     * @param certificate {@link CertificateToken}
     * @param uri {@link String}
     */
    public MdocIdentifierList(final byte[] identifier, final String uri, final CertificateToken certificate) {
        Objects.requireNonNull(identifier, "Identifier cannot be null!");
        Objects.requireNonNull(uri, "Uri cannot be null!");
        this.identifier = identifier;
        this.uri = uri;
        this.certificate = certificate;
    }

    /**
     * Gets index of the token within a status list
     *
     * @return non-negative integer
     */
    public byte[] getIdentifier() {
        return identifier;
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