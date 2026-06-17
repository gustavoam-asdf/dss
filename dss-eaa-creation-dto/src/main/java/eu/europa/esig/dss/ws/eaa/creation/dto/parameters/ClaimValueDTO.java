package eu.europa.esig.dss.ws.eaa.creation.dto.parameters;

import java.util.Date;
import java.util.List;

/**
 * DTO representing a custom claim's value
 *
 */
public class ClaimValueDTO {

    /** String value */
    private String stringValue;

    /** Numeric value */
    private Number numberValue;

    /** Boolean value */
    private Boolean booleanValue;

    /** Date value */
    private Date dateValue;

    /** Binary value */
    private byte[] binaryValue;

    /** Array value represented as a list of claims */
    private List<ClaimDTO> arrayValue;

    /** Object value represented as a list of named claims */
    private List<ClaimDTO> objectValue;

    /**
     * Default constructor
     */
    public ClaimValueDTO() {
        // empty
    }

    /**
     * Constructor with String as a value
     *
     * @param stringValue {@link String}
     */
    public ClaimValueDTO(String stringValue) {
        this.stringValue = stringValue;
    }

    /**
     * Constructor with Number as a value
     *
     * @param numberValue {@link Number}
     */
    public ClaimValueDTO(Number numberValue) {
        this.numberValue = numberValue;
    }

    /**
     * Constructor with Boolean as a value
     *
     * @param booleanValue {@link Boolean}
     */
    public ClaimValueDTO(Boolean booleanValue) {
        this.booleanValue = booleanValue;
    }

    /**
     * Constructor with Date as a value
     *
     * @param dateValue {@link Date}
     */
    public ClaimValueDTO(Date dateValue) {
        this.dateValue = dateValue;
    }

    /**
     * Constructor with byte array as a value
     *
     * @param binaryValue byte array
     */
    public ClaimValueDTO(byte[] binaryValue) {
        this.binaryValue = binaryValue;
    }

    /**
     * Gets the string value
     *
     * @return {@link String}
     */
    public String getStringValue() {
        return stringValue;
    }

    /**
     * Sets the string value
     *
     * @param stringValue {@link String}
     */
    public void setStringValue(String stringValue) {
        this.stringValue = stringValue;
    }

    /**
     * Gets the numeric value
     *
     * @return {@link Number}
     */
    public Number getNumberValue() {
        return numberValue;
    }

    /**
     * Sets the numeric value
     *
     * @param numberValue {@link Number}
     */
    public void setNumberValue(Number numberValue) {
        this.numberValue = numberValue;
    }

    /**
     * Gets the boolean value
     *
     * @return {@link Boolean}
     */
    public Boolean getBooleanValue() {
        return booleanValue;
    }

    /**
     * Sets the boolean value
     *
     * @param booleanValue {@link Boolean}
     */
    public void setBooleanValue(Boolean booleanValue) {
        this.booleanValue = booleanValue;
    }

    /**
     * Gets the date value
     *
     * @return {@link Date}
     */
    public Date getDateValue() {
        return dateValue;
    }

    /**
     * Sets the date value
     *
     * @param dateValue {@link Date}
     */
    public void setDateValue(Date dateValue) {
        this.dateValue = dateValue;
    }

    /**
     * Gets the binary value
     *
     * @return byte[]
     */
    public byte[] getBinaryValue() {
        return binaryValue;
    }

    /**
     * Sets the binary value
     *
     * @param binaryValue byte[]
     */
    public void setBinaryValue(byte[] binaryValue) {
        this.binaryValue = binaryValue;
    }

    /**
     * Gets the array value
     *
     * @return {@link List<ClaimDTO>}
     */
    public List<ClaimDTO> getArrayValue() {
        return arrayValue;
    }

    /**
     * Sets the array value
     *
     * @param arrayValue {@link List<ClaimDTO>}
     */
    public void setArrayValue(List<ClaimDTO> arrayValue) {
        this.arrayValue = arrayValue;
    }

    /**
     * Gets the object value
     *
     * @return {@link List<ClaimDTO>}
     */
    public List<ClaimDTO> getObjectValue() {
        return objectValue;
    }

    /**
     * Sets the object value
     *
     * @param objectValue {@link List<ClaimDTO>}
     */
    public void setObjectValue(List<ClaimDTO> objectValue) {
        this.objectValue = objectValue;
    }

}