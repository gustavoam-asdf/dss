package eu.europa.esig.dss.ws.eaa.creation.dto.parameters;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

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

}
