package eu.europa.esig.dss.eaa.common.creation.claim;

import java.io.Serializable;
import java.util.Objects;

/**
 * Abstract implementation of an EAA Claim
 */
public abstract class AbstractEAAClaim implements EAAClaim {

    private static final long serialVersionUID = 1L;

    private final Object value;

    private final Object name;

    /**
     * Constructor with the value and claim name
     *
     * @param name {@link Object} the claim name
     * @param value {@link Object} the value of the claim
     */
    protected AbstractEAAClaim(final Object name, final Object value) {
        this.name = name;
        this.value = value;
    }

    /**
     * Gets the claim name
     *
     * @return {@link Object}
     */
    @Override
    public Object getName() {
        return name;
    }

    /**
     * Gets the value
     *
     * @return {@link Object} the value
     */
    @Override
    public Object getValue() {
        return value;
    }
}

