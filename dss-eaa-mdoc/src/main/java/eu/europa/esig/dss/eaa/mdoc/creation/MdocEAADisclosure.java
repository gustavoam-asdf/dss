package eu.europa.esig.dss.eaa.mdoc.creation;

import eu.europa.esig.dss.cbades.cbor.CBORByteString;
import eu.europa.esig.dss.cbades.cbor.CBORUtils;
import eu.europa.esig.dss.eaa.common.creation.EAADisclosure;

import java.util.Objects;

/**
 * Implementation of a disclosure for an ISO/IEC 18013-5 token
 *
 */
public class MdocEAADisclosure implements EAADisclosure {

    private static final long serialVersionUID = 4647204332079766021L;

    /** Namespace of the disclosure */
    private final String namespace;

    /** Represents serialized  */
    private final byte[] issuerSignedItemBytes;

    /**
     * Constructor to instantiate the mdoc disclosure from a serialized IssuerSignedItemBytes object
     *
     * @param namespace {@link String} namespace of the element claim
     * @param issuerSignedItemBytes serialized IssuerSignedItemBytes object
     */
    public MdocEAADisclosure(final String namespace, final byte[] issuerSignedItemBytes) {
        Objects.requireNonNull(namespace, "Namespace cannot be null!");
        Objects.requireNonNull(issuerSignedItemBytes, "IssuerSignedItemBytes cannot be null!");
        this.namespace = namespace;
        this.issuerSignedItemBytes = issuerSignedItemBytes;
    }

    /**
     * Constructor to instantiate the mdoc disclosure from a IssuerSignedItemBytes object
     *
     * @param namespace {@link String} namespace of the element claim
     * @param issuerSignedItemBytes {@link CBORByteString}
     */
    public MdocEAADisclosure(final String namespace, final CBORByteString issuerSignedItemBytes) {
        Objects.requireNonNull(namespace, "Namespace cannot be null!");
        Objects.requireNonNull(issuerSignedItemBytes, "IssuerSignedItemBytes cannot be null!");
        this.namespace = namespace;
        this.issuerSignedItemBytes = CBORUtils.serializeCborObject(issuerSignedItemBytes);
    }

    /**
     * Gets the element namespace
     *
     * @return {@link String}
     */
    public String getNamespace() {
        return namespace;
    }

    @Override
    public byte[] getBytesToBeSigned() {
        return issuerSignedItemBytes;
    }

}
