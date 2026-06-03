package eu.europa.esig.dss.eaa.mdoc.creation;

import eu.europa.esig.dss.cbades.cbor.CBORByteString;
import eu.europa.esig.dss.cbades.cbor.CBORObject;
import eu.europa.esig.dss.cbades.cbor.CBORUtils;
import eu.europa.esig.dss.eaa.common.creation.AbstractEAADisclosure;
import eu.europa.esig.dss.enumerations.DigestAlgorithm;
import eu.europa.esig.dss.model.Digest;
import eu.europa.esig.dss.spi.DSSUtils;
import eu.europa.esig.dss.spi.exception.IllegalInputException;

import java.util.Objects;

/**
 * Implementation of a disclosure for an ISO/IEC 18013-5 token
 *
 */
public class MdocEAADisclosure extends AbstractEAADisclosure {

    private static final long serialVersionUID = 4647204332079766021L;

    /** Namespace of the disclosure */
    private final String namespace;

    /** DigestId of the disclosure */
    private final int digestId;

    /** Represents serialized  */
    private final CBORByteString issuerSignedItemBytes;

    /**
     * Constructor to instantiate a void
     *
     * @param issuerSignedItemBytes serialized IssuerSignedItemBytes object
     */
    protected MdocEAADisclosure(final int digestId, final CBORByteString issuerSignedItemBytes) {
        Objects.requireNonNull(issuerSignedItemBytes, "IssuerSignedItemBytes cannot be null!");
        this.namespace = null;
        this.digestId = digestId;
        this.issuerSignedItemBytes = issuerSignedItemBytes;
    }

    /**
     * Constructor to instantiate the mdoc disclosure from a serialized IssuerSignedItemBytes object
     *
     * @param namespace {@link String} namespace of the element claim
     * @param digestId unique integer identifying the element within the EAA namespace
     * @param issuerSignedItemBytes serialized IssuerSignedItemBytes object
     */
    public MdocEAADisclosure(final String namespace, final int digestId, final byte[] issuerSignedItemBytes) {
        Objects.requireNonNull(namespace, "Namespace cannot be null!");
        Objects.requireNonNull(issuerSignedItemBytes, "IssuerSignedItemBytes cannot be null!");
        this.namespace = namespace;
        this.digestId = digestId;
        this.issuerSignedItemBytes = parse(issuerSignedItemBytes);
    }

    private static CBORByteString parse(byte[] issuerSignedItemBytes) {
        CBORObject cborObject;
        try {
            cborObject = CBORUtils.parseCbor(issuerSignedItemBytes);
        } catch (Exception e) {
            throw new IllegalInputException(String.format("The issuerSignedItemBytes shall be CBOR encoded : %s", e.getMessage()), e);
        }
        if (!cborObject.isByteString()) {
            throw new IllegalInputException("The issuerSignedItemBytes shall be CBOR Byte String encoded!");
        }
        return (CBORByteString) cborObject;
    }

    /**
     * Constructor to instantiate the mdoc disclosure from a IssuerSignedItemBytes object
     *
     * @param namespace {@link String} namespace of the element claim
     * @param digestId unique integer identifying the element within the EAA namespace
     * @param issuerSignedItemBytes {@link CBORByteString}
     */
    public MdocEAADisclosure(final String namespace, final int digestId, final CBORByteString issuerSignedItemBytes) {
        Objects.requireNonNull(namespace, "Namespace cannot be null!");
        Objects.requireNonNull(issuerSignedItemBytes, "IssuerSignedItemBytes cannot be null!");
        this.namespace = namespace;
        this.digestId = digestId;
        this.issuerSignedItemBytes = issuerSignedItemBytes;
    }

    /**
     * Gets the element namespace
     *
     * @return {@link String}
     */
    public String getNamespace() {
        return namespace;
    }

    /**
     * Gets the disclosure digestId
     *
     * @return integer
     */
    public int getDigestId() {
        return digestId;
    }

    /**
     * Gets the IssuerSignedItemBytes
     *
     * @return {@link CBORByteString} IssuerSignedItemBytes
     */
    public CBORByteString getIssuerSignedItemBytes() {
        return issuerSignedItemBytes;
    }

    @Override
    protected Digest computeDigest(DigestAlgorithm digestAlgorithm) {
        byte[] serialized = CBORUtils.serializeCborObject(issuerSignedItemBytes);
        byte[] digestValue = DSSUtils.digest(digestAlgorithm, serialized);
        return new Digest(digestAlgorithm, digestValue);
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;

        MdocEAADisclosure that = (MdocEAADisclosure) object;
        return digestId == that.digestId
                && namespace.equals(that.namespace)
                && Objects.equals(issuerSignedItemBytes, that.issuerSignedItemBytes);
    }

    @Override
    public int hashCode() {
        int result = namespace.hashCode();
        result = 31 * result + digestId;
        result = 31 * result + Objects.hashCode(issuerSignedItemBytes);
        return result;
    }

}
