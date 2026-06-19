package eu.europa.esig.dss.ws.eaa.creation.dto.parameters;

import java.io.Serializable;
import java.util.Objects;

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

    @Override
    public String toString() {
        return "AgeOverNNDTO [" +
                "age=" + age +
                ", isOver=" + isOver +
                ']';
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;

        AgeOverNNDTO that = (AgeOverNNDTO) object;
        return Objects.equals(age, that.age)
                && Objects.equals(isOver, that.isOver);
    }

    @Override
    public int hashCode() {
        int result = Objects.hashCode(age);
        result = 31 * result + Objects.hashCode(isOver);
        return result;
    }

}
