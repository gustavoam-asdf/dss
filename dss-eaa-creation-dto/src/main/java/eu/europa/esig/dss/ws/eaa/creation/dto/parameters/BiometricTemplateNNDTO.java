package eu.europa.esig.dss.ws.eaa.creation.dto.parameters;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Objects;

/**
 * Represents an "biometric_template_NN" claim parameter
 *
 */
public class BiometricTemplateNNDTO implements Serializable {

    private static final long serialVersionUID = -6262181747635128573L;

    /** Type name of the biometric template */
    private String type;

    /** Data content of the biometric template */
    private byte[] data;

    /**
     * Default constructor
     */
    public BiometricTemplateNNDTO() {
        // empty
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public byte[] getData() {
        return data;
    }

    public void setData(byte[] data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "BiometricTemplateNNDTO [" +
                "type='" + type + '\'' +
                ", data=" + Arrays.toString(data) +
                ']';
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;

        BiometricTemplateNNDTO that = (BiometricTemplateNNDTO) object;
        return Objects.equals(type, that.type)
                && Arrays.equals(data, that.data);
    }

    @Override
    public int hashCode() {
        int result = Objects.hashCode(type);
        result = 31 * result + Arrays.hashCode(data);
        return result;
    }

}
