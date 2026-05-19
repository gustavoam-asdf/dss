package eu.europa.esig.dss.eaa.common.creation.claim;

/**
 * Abstract implementation of an EAA Claim
 */
public abstract class AbstractEAAClaim implements EAAClaim {

    private static final long serialVersionUID = -1092016241135884116L;

    /** Name of the claim element */
    private final String name;

    /** Value of the claim element */
    private final Object value;

    /**
     * Constructor with the value and claim name
     *
     * @param name {@link String} the claim name
     * @param value {@link Object} the value of the claim
     */
    protected AbstractEAAClaim(final String name, final Object value) {
        this.name = name;
        this.value = value;
    }

    /**
     * Gets the claim name
     *
     * @return {@link String}
     */
    @Override
    public String getName() {
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

