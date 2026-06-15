package eu.europa.esig.dss.cbades.cbor;

import co.nstant.in.cbor.model.ByteString;
import eu.europa.esig.dss.spi.DSSUtils;

/**
 * A wrapper for a CBOR ByteString object implementation
 */
public class CBORByteString extends AbstractCBORObject<ByteString> {

    /**
     * Constructor to create an empty CBOR ByteString object
     */
    public CBORByteString() {
        this(new ByteString(DSSUtils.EMPTY_BYTE_ARRAY));
    }

    /**
     * Constructor to create a CBOR ByteString object from a byte array
     *
     * @param byteArray a byte array
     */
    public CBORByteString(final byte[] byteArray) {
        this(new ByteString(byteArray));
    }

    /**
     * Constructor to create a CBOR ByteString object from a {@code ByteString} implementation
     *
     * @param byteString {@link ByteString}
     */
    public CBORByteString(final ByteString byteString) {
        super(byteString);
    }

    @Override
    public byte[] getValueAsBytes() {
        return toDataItem().getBytes();
    }

}
