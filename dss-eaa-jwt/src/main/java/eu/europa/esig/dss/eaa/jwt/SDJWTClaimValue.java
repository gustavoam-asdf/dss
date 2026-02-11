package eu.europa.esig.dss.eaa.jwt;

import eu.europa.esig.dss.model.eaa.ClaimValue;
import org.jose4j.json.internal.json_simple.JSONValue;

import java.util.Objects;

/**
 * Represents a disclosure claim value provided for the SD-JWT token
 *
 */
public class SDJWTClaimValue implements ClaimValue {

    private static final long serialVersionUID = -8756100455697595777L;

    /** The value of the disclosure */
    private final Object value;

    /**
     * Default constructor
     *
     * @param value {@link Object}
     */
    public SDJWTClaimValue(final Object value) {
        this.value = value;
    }

    /**
     * Gets the value object
     *
     * @return {@link Object}
     */
    public Object getValue() {
        return value;
    }

    @Override
    public String toString() {
        if (value instanceof String) {
            return (String) value;
        }
        return JSONValue.toJSONString(value);
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;

        SDJWTClaimValue that = (SDJWTClaimValue) object;
        return Objects.equals(value, that.value);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(value);
    }

}

