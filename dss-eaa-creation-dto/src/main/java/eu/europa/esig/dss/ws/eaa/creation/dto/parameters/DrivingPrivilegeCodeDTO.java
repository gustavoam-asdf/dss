package eu.europa.esig.dss.ws.eaa.creation.dto.parameters;

import java.io.Serializable;

/**
 * DTO representing a code of a driving privilege as per ISO/IEC 18013-5
 *
 */
public class DrivingPrivilegeCodeDTO implements Serializable {

    private static final long serialVersionUID = 6276269021612834847L;

    /** Code as per ISO/IEC 18013-2 Annex A */
    private String code;

    /** Sign as per ISO/IEC 18013-2 Annex A */
    private String sign;

    /** Value as per ISO/IEC 18013-2 Annex A */
    private String value;

    /**
     * Default constructor
     */
    public DrivingPrivilegeCodeDTO() {
        // empty
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

}
