package eu.europa.esig.dss.model.eaa;

import eu.europa.esig.dss.enumerations.DigestMatcherType;
import eu.europa.esig.dss.model.Digest;

import java.io.Serializable;
import java.util.List;

/**
 * This class represents a validation result of a selectable disclosure provided
 * with presentation of Electronic Attestation of Attributes
 *
 */
public class DisclosureValidation implements Serializable {

    private static final long serialVersionUID = -191049727174569696L;

    /** Type of the selectively disclosable claim */
    private DigestMatcherType type;

    /** Defines whether the corr */
    private boolean found;

    /** The pointed reference is intact */
    private boolean intact;

    /** The digest value embedded in reference element */
    private Digest digest;

    /** Salt value of provided disclosures */
    private byte[] salt;

    /** Name of the disclosure */
    private String name;

    /** Value of the disclosure */
    private Object value;

    /** Nested disclosures, when applicable */
    private List<DisclosureValidation> nestedDisclosures;

    /**
     * Default constructor
     */
    public DisclosureValidation() {
        // empty
    }

    /**
     * Gets type of the selectively disclosable claim
     *
     * @return {@link DigestMatcherType}
     */
    public DigestMatcherType getType() {
        return type;
    }

    /**
     * Sets type of the selectively disclosable claim
     *
     * @param type {@link DigestMatcherType}
     */
    public void setType(DigestMatcherType type) {
        this.type = type;
    }

    /**
     * Gets whether the matching hash or disclosure has been found
     *
     * @return TRUE if a match was found, FALSE otherwise
     */
    public boolean isFound() {
        return found;
    }

    /**
     * Sets whether the matching hash or disclosure has been found
     *
     * @param found whether the matching hash or disclosure has been found
     */
    public void setFound(boolean found) {
        this.found = found;
    }

    /**
     * Gets whether the matching disclosure is intact
     *
     * @return TRUE if the matching disclosure matches the hash value, FALSE otherwise
     */
    public boolean isIntact() {
        return intact;
    }

    /**
     * Sets whether the matching disclosure is intact
     *
     * @param intact whether the matching disclosure is intact
     */
    public void setIntact(boolean intact) {
        this.intact = intact;
    }

    /**
     * Gets the selectively disclosable claim digest
     *
     * @return {@link Digest}
     */
    public Digest getDigest() {
        return digest;
    }

    /**
     * Sets the selectively disclosable claim digest
     *
     * @param digest {@link Digest}
     */
    public void setDigest(Digest digest) {
        this.digest = digest;
    }

    /**
     * Gets the salt incorporated within the provided disclosure, when applicable
     *
     * @return byte array representing salt value
     */
    public byte[] getSalt() {
        return salt;
    }

    /**
     * Sets the salt incorporated within the provided disclosure, when applicable
     *
     * @param salt byte array representing salt value
     */
    public void setSalt(byte[] salt) {
        this.salt = salt;
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
     * Gets the provided disclosure value
     *
     * @return {@link Object}
     */
    public Object getValue() {
        return value;
    }

    /**
     * Sets the provided disclosure value
     *
     * @param value {@link Object}
     */
    public void setValue(Object value) {
        this.value = value;
    }

    /**
     * Gets a list of nested disclosures' validations
     *
     * @return a list of {@link DisclosureValidation}s
     */
    public List<DisclosureValidation> getNestedDisclosures() {
        return nestedDisclosures;
    }

    /**
     * Sets a list of nested disclosures' validations
     *
     * @param nestedDisclosures a list of {@link DisclosureValidation}s
     */
    public void setNestedDisclosures(List<DisclosureValidation> nestedDisclosures) {
        this.nestedDisclosures = nestedDisclosures;
    }

}
