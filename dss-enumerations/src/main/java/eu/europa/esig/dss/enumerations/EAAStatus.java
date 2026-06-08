package eu.europa.esig.dss.enumerations;

/**
 * Represents a status of an EAA
 *
 */
public enum EAAStatus {

    /**
     * The status of the Referenced Token is valid, correct or legal.
     */
    VALID(0x00),

    /**
     * The status of the Referenced Token is revoked, annulled, taken back, recalled or cancelled.
     */
    INVALID(0x01),

    /**
     * The status of the Referenced Token is temporarily invalid, hanging, debarred from privilege. This status is usually temporary.
     */
    SUSPENDED(0x02),

    /**
     * The status of the Referenced Token is application specific.
     */
    APPLICATION_SPECIFIC(0x03),

    /**
     * The EAA status is not known
     */
    UNKNOWN();

    /**
     * The bit representation of the Status Type in a byte hex representation.
     * Valid Status Type values range from 0x00-0xFF. Values are filled up with zeros if they have less than 8 bits.
     */
    private final Integer bitValue;

    /**
     * Empty constructor
     */
    EAAStatus() {
        this.bitValue = null;
    }

    /**
     * Constructor with a value
     *
     * @param bitValue bit representation of the Status Type
     */
    EAAStatus(final int bitValue) {
        this.bitValue = bitValue;
    }

    /**
     * Checks if the EAA status is valid
     *
     * @return TRUE if the EAA status is valid, FALSE otherwise
     */
    public boolean isValid() {
        return VALID == this;
    }

    /**
     * Gets the bit representation of the Status Type in a byte hex representation.
     *
     * @return status type int value
     */
    public Integer getBitValue() {
        return bitValue;
    }

    /**
     * Gets a corresponding {@code EAAStatusValue} type for the given {@code bitValue}
     *
     * @param bitValue the bit representation of the Status Type in a byte hex representation
     * @return {@link EAAStatus}
     */
    public static EAAStatus forBitValue(int bitValue) {
        for (EAAStatus eaaStatus : values()) {
            if (bitValue == eaaStatus.bitValue) {
                return eaaStatus;
            }
        }
        return UNKNOWN;
    }

}
