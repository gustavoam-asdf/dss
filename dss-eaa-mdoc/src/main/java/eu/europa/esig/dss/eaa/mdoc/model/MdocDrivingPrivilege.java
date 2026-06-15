package eu.europa.esig.dss.eaa.mdoc.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Represents a DrivingPrivilege structure as defined in ISO/IEC 18013-5 "7.2.4 Categories of vehicles/restrictions/conditions".
 *
 */
public class MdocDrivingPrivilege implements Serializable {

    private static final long serialVersionUID = 6153862012367616220L;

    /** Vehicle category code as per ISO/IEC 18013-1 Annex B */
    private final String vehicleCategoryCode;

    /** Date of issue encoded as full-date */
    private Date issueDate;

    /** Date of expiry encoded as full-date */
    private Date expiryDate;

    /** Array of code info */
    private List<Code> codes;

    /**
     * Default constructor
     *
     * @param vehicleCategoryCode {@link String} vehicle category code as per ISO/IEC 18013-1 Annex B
     */
    public MdocDrivingPrivilege(final String vehicleCategoryCode) {
        this.vehicleCategoryCode = vehicleCategoryCode;
    }

    /**
     * Gets the vehicle category code
     *
     * @return {@link String}
     */
    public String getVehicleCategoryCode() {
        return vehicleCategoryCode;
    }

    /**
     * Gets the issue date
     *
     * @return {@link Date}
     */
    public Date getIssueDate() {
        return issueDate;
    }

    /**
     * Sets date of issue
     *
     * @param issueDate {@link Date}
     */
    public void setIssueDate(Date issueDate) {
        this.issueDate = issueDate;
    }

    /**
     * Gets the expiry date
     *
     * @return {@link Date}
     */
    public Date getExpiryDate() {
        return expiryDate;
    }

    /**
     * Sets date of expiry
     *
     * @param expiryDate {@link Date}
     */
    public void setExpiryDate(Date expiryDate) {
        this.expiryDate = expiryDate;
    }

    /**
     * Gets a list of code info
     *
     * @return a list of {@link Code}s
     */
    public List<Code> getCodes() {
        return codes;
    }

    /**
     * Adds a code to the array of code info
     *
     * @param code {@link Code}
     */
    public void addCode(Code code) {
        if (codes == null) {
            this.codes = new ArrayList<>();
        }
        codes.add(code);
    }

    /**
     * Adds a code with code to the array of code info
     *
     * @param code code as per ISO/IEC 18013-2 Annex A
     */
    public void addCode(String code) {
        addCode(new Code(code));
    }

    /**
     * Adds a code with code, sign and value to the array of code info
     *
     * @param code code as per ISO/IEC 18013-2 Annex A
     * @param sign sign as per ISO/IEC 18013-2 Annex A
     * @param value value as per ISO/IEC 18013-2 Annex A
     */
    public void addCode(String code, String sign, String value) {
        addCode(new Code(code, sign, value));
    }

    /**
     * Represents a Code structure as defined in ISO/IEC 18013-5 "7.2.4 Categories of vehicles/restrictions/conditions".
     */
    public static class Code implements Serializable {

        private static final long serialVersionUID = -2322614345103093927L;

        /** Code as per ISO/IEC 18013-2 Annex A */
        private final String code;

        /** Sign as per ISO/IEC 18013-2 Annex A */
        private final String sign;

        /** Value as per ISO/IEC 18013-2 Annex A */
        private final String value;

        /**
         * Constructor with code definition only
         *
         * @param code code as per ISO/IEC 18013-2 Annex A
         */
        public Code(final String code) {
            this(code, null, null);
        }

        /**
         * Constructor with code, sign and value definitions
         *
         * @param code code as per ISO/IEC 18013-2 Annex A
         * @param sign sign as per ISO/IEC 18013-2 Annex A
         * @param value value as per ISO/IEC 18013-2 Annex A
         */
        public Code(final String code, final String sign, final String value) {
            this.code = code;
            this.sign = sign;
            this.value = value;
        }

        /**
         * Gets the code
         *
         * @return {@link String}
         */
        public String getCode() {
            return code;
        }

        /**
         * Gets the sign
         *
         * @return {@link String}
         */
        public String getSign() {
            return sign;
        }

        /**
         * Gets the value
         *
         * @return {@link String}
         */
        public String getValue() {
            return value;
        }

    }

}
