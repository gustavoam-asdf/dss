package eu.europa.esig.dss.model.eaa;

import eu.europa.esig.dss.model.ReferenceValidation;

import java.util.Objects;

/**
 * This class represents a validation result of a selectable disclosure provided
 * with presentation of Electronic Attestation of Attributes
 *
 */
public class DisclosureValidation extends ReferenceValidation {

    private static final long serialVersionUID = -191049727174569696L;

    /** Disclosure object, when applicable */
    private Disclosure disclosure;

    /** Name of the disclosure */
    private String name;

    /** Value of the disclosure */
    private ClaimValue value;

    /**
     * Default constructor
     */
    public DisclosureValidation() {
        // empty
    }

    /**
     * Constructor with a provided disclosure
     */
    public DisclosureValidation(Disclosure disclosure) {
        this.disclosure = disclosure;
        this.name = disclosure.getClaimName();
        this.value = disclosure.getClaimValue();
    }

    /**
     * Gets disclosure when applicable
     *
     * @return {@link Disclosure}
     */
    public Disclosure getDisclosure() {
        return disclosure;
    }

    /**
     * Gets the provided disclosure name
     *
     * @return {@link String}
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the provided disclosure name
     *
     * @param name {@link String}
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Gets the provided disclosure claim value
     *
     * @return {@link ClaimValue}
     */
    public ClaimValue getValue() {
        return value;
    }

    /**
     * Sets the provided disclosure claim value
     *
     * @param value {@link ClaimValue}
     */
    public void setValue(ClaimValue value) {
        this.value = value;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;

        DisclosureValidation that = (DisclosureValidation) object;
        return Objects.equals(disclosure, that.disclosure)
                && Objects.equals(name, that.name)
                && Objects.equals(value, that.value);
    }

    @Override
    public int hashCode() {
        int result = Objects.hashCode(disclosure);
        result = 31 * result + Objects.hashCode(name);
        result = 31 * result + Objects.hashCode(value);
        return result;
    }

}
