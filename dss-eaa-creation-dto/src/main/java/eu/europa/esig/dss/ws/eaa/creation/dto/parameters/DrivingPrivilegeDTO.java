package eu.europa.esig.dss.ws.eaa.creation.dto.parameters;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Objects;

/**
 * DTO representing a driving privilege as per ISO/IEC 18013-5
 *
 */
public class DrivingPrivilegeDTO implements Serializable {

    private static final long serialVersionUID = 6064844332387506591L;

    /** Vehicle category code as per ISO/IEC 18013-1 Annex B */
    private String vehicleCategoryCode;

    /** Date of issue encoded as full-date */
    private Date issueDate;

    /** Date of expiry encoded as full-date */
    private Date expiryDate;

    /** Array of code info */
    private List<DrivingPrivilegeCodeDTO> codes;

    /**
     * Default constructor
     */
    public DrivingPrivilegeDTO() {
        // empty
    }

    /**
     * Constructor with vehicle category code
     */
    public DrivingPrivilegeDTO(String vehicleCategoryCode) {
        this.vehicleCategoryCode = vehicleCategoryCode;
    }

    public String getVehicleCategoryCode() {
        return vehicleCategoryCode;
    }

    public void setVehicleCategoryCode(String vehicleCategoryCode) {
        this.vehicleCategoryCode = vehicleCategoryCode;
    }

    public Date getIssueDate() {
        return issueDate;
    }

    public void setIssueDate(Date issueDate) {
        this.issueDate = issueDate;
    }

    public Date getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(Date expiryDate) {
        this.expiryDate = expiryDate;
    }

    public List<DrivingPrivilegeCodeDTO> getCodes() {
        return codes;
    }

    public void setCodes(List<DrivingPrivilegeCodeDTO> codes) {
        this.codes = codes;
    }

    @Override
    public String toString() {
        return "DrivingPrivilegeDTO [" +
                "vehicleCategoryCode='" + vehicleCategoryCode + '\'' +
                ", issueDate=" + issueDate +
                ", expiryDate=" + expiryDate +
                ", codes=" + codes +
                ']';
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;

        DrivingPrivilegeDTO that = (DrivingPrivilegeDTO) object;
        return Objects.equals(vehicleCategoryCode, that.vehicleCategoryCode)
                && Objects.equals(issueDate, that.issueDate)
                && Objects.equals(expiryDate, that.expiryDate)
                && Objects.equals(codes, that.codes);
    }

    @Override
    public int hashCode() {
        int result = Objects.hashCode(vehicleCategoryCode);
        result = 31 * result + Objects.hashCode(issueDate);
        result = 31 * result + Objects.hashCode(expiryDate);
        result = 31 * result + Objects.hashCode(codes);
        return result;
    }

}
