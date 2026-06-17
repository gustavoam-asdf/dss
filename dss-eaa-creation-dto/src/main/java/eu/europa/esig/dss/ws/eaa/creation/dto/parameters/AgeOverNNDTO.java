package eu.europa.esig.dss.ws.eaa.creation.dto.parameters;

import java.io.Serializable;

/**
 * Represents an "age_over_NN" claim parameter
 *
 */
public class AgeOverNNDTO implements Serializable {

    private static final long serialVersionUID = -7185379420422139031L;

    /** Age in years */
    private Integer age;

    /** Whether age of the EAA holder is equal or over the age value */
    private Boolean isOver;

    /**
     * Default constructor
     */
    public AgeOverNNDTO() {
        // empty
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public Boolean getOver() {
        return isOver;
    }

    public void setOver(Boolean over) {
        isOver = over;
    }

}
