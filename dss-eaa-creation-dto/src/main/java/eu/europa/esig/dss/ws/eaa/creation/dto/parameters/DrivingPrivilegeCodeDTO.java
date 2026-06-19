package eu.europa.esig.dss.ws.eaa.creation.dto.parameters;

import java.io.Serializable;
import java.util.Objects;

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

    @Override
    public String toString() {
        return "DrivingPrivilegeCodeDTO [" +
                "code='" + code + '\'' +
                ", sign='" + sign + '\'' +
                ", value='" + value + '\'' +
                ']';
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;

        DrivingPrivilegeCodeDTO codeDTO = (DrivingPrivilegeCodeDTO) object;
        return Objects.equals(code, codeDTO.code)
                && Objects.equals(sign, codeDTO.sign)
                && Objects.equals(value, codeDTO.value);
    }

    @Override
    public int hashCode() {
        int result = Objects.hashCode(code);
        result = 31 * result + Objects.hashCode(sign);
        result = 31 * result + Objects.hashCode(value);
        return result;
    }

}
