package eu.europa.esig.dss.eaa;

import eu.europa.esig.dss.enumerations.EAAStatus;
import eu.europa.esig.dss.model.DSSException;
import eu.europa.esig.dss.spi.eaa.EAAStatusToken;
import eu.europa.esig.dss.spi.eaa.StatusTokenBinary;
import eu.europa.esig.dss.spi.eaa.statuslist.EAAStatusListToken;
import eu.europa.esig.dss.spi.eaa.statuslist.StatusListPayload;
import eu.europa.esig.dss.spi.eaa.statuslist.StatusListValidator;
import eu.europa.esig.dss.spi.signature.AdvancedSignature;
import eu.europa.esig.dss.utils.Utils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Objects;
import java.util.zip.InflaterInputStream;

/**
 * Contains common methods and logic for validation of a Token Status List
 *
 */
public abstract class AbstractEAAStatusListValidator implements StatusListValidator {

    /** Binaries of the retrieved status list */
    protected byte[] statusListDocument;

    /** Cached instance of a signature used to sign the token */
    private AdvancedSignature tokenSignature;

    /**
     * Empty constructor
     */
    public AbstractEAAStatusListValidator() {
        // empty
    }

    /**
     * Empty constructor
     */
    public AbstractEAAStatusListValidator(final byte[] statusListDocument) {
        this.statusListDocument = statusListDocument;
    }

    @Override
    public EAAStatusToken getStatusToken(int index) {
        Objects.requireNonNull(statusListDocument, "Token Status List Document cannot be null!");

        /*
         * 8.2. Status List Response
         *
         * The body of such an HTTP response contains the raw Status List Token,
         * that means the binary encoding as defined in Section 9.2.1 of [RFC8392] for
         * a Status List Token in CWT format and the JWS Compact Serialization form for
         * a Status List Token in JWT format.
         */
        AdvancedSignature signature = getTokenSignature();
        if (signature != null) {
            StatusListPayload statusListPayload = getPayload(signature);
            return EAAStatusListToken.initBuilder()
                    .setBinary(new StatusTokenBinary(statusListDocument))
                    .setSignature(signature)
                    .setPayload(statusListPayload)
                    .setStatus(getEAAStatus(statusListPayload, index))
                    .build();
        }
        return null;
    }

    /**
     * Gets the token signature. If already built, returns the cached value.
     *
     * @return {@link AdvancedSignature}
     */
    protected AdvancedSignature getTokenSignature() {
        if (tokenSignature == null) {
            tokenSignature = buildTokenSignature();
        }
        return tokenSignature;
    }

    /**
     * Builds a signature of the token
     *
     * @return {@link AdvancedSignature}
     */
    protected abstract AdvancedSignature buildTokenSignature();

    /**
     * Gets the representation of a Token Status List Payload signed by the {@code signature}
     *
     * @param signature {@link AdvancedSignature}
     * @return {@link StatusListPayload}
     */
    protected abstract StatusListPayload getPayload(AdvancedSignature signature);

    /**
     * Gets the EAA Status for the given {@code eaa} based on the information retrieved from {@code statusListPayload}
     *
     * @param statusListPayload {@link StatusListPayload} of the retrieved token
     * @return {@link EAAStatus}
     */
    protected EAAStatus getEAAStatus(StatusListPayload statusListPayload, int index) {
        byte[] statusListEncoded = statusListPayload.getStatusListEncoded();
        byte[] statusListDecompressed = decompressStatusList(statusListEncoded);

        Number statusListBits = statusListPayload.getStatusListBits();
        if (statusListBits == null) {
            throw new DSSException("The 'bits' claim of the 'status_list' is not present or null!");
        }

        return getStatus(statusListDecompressed, index, statusListBits.intValue());
    }

    /**
     * Decompresses the Status List with a decompressor that is compatible with DEFLATE (RFC1951) and ZLIB (RFC1950)
     *
     * @param statusListArray byte array containing the original status list
     * @return byte array containing the decompressed status list
     */
    protected byte[] decompressStatusList(byte[] statusListArray) {
        try (ByteArrayInputStream bais = new ByteArrayInputStream(statusListArray);
             InflaterInputStream inflater = new InflaterInputStream(bais);
             ByteArrayOutputStream baos = new ByteArrayOutputStream()) {

            Utils.copy(inflater, baos);
            return baos.toByteArray();

        } catch (IOException e) {
            throw new DSSException(String.format(
                    "An error occurred during on attempt to decompress the 'status_list' : %s", e.getMessage()), e);
        }
    }

    /**
     * Retrieves the status value of the index specified in the Referenced Token.
     *
     * @param decompressed decompressed status list byte array
     * @param index position of the status of the token in question
     * @param bits number of bits per status
     * @return {@link EAAStatus}
     */
    protected EAAStatus getStatus(byte[] decompressed, int index, int bits) {
        if (!(bits == 1 || bits == 2 || bits == 4 || bits == 8)) {
            throw new DSSException(String.format("'bits' must be 1, 2, 4 or 8. Obtained value '%s'", bits));
        }

        int statusesPerByte = 8 / bits;
        int byteIndex = index / statusesPerByte;

        if (byteIndex >= decompressed.length) {
            throw new DSSException(String.format("The position of the index '%s' is out of bounds of " +
                    "the obtained status list array with size '%s' bytes (%s bits)!", index, decompressed.length, decompressed.length * 8));
        }

        int positionInByte = index % statusesPerByte;

        int shift = positionInByte * bits;

        int mask = (1 << bits) - 1;

        int value = decompressed[byteIndex] & 0xFF;

        int statusValue = (value >> shift) & mask;
        return EAAStatus.forBitValue(statusValue);
    }

}
