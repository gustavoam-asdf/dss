package eu.europa.esig.dss.model;

import java.io.Serializable;

/**
 * This class represents a selective disclosure attached to a presentation of Electronic Attestation of Attribiutes
 *
 */
public class EAADisclosure implements Serializable {

    private static final long serialVersionUID = -6025755119813037143L;

    /** Salt value */
    private byte[] salt;

    /** Name of the disclosure claim */
    private String claimName;

    /** Value of the disclosure claim */
    private Object claimValue;

    /**
     * Default constructor
     */
    public EAADisclosure() {
        // empty
    }

    /**
     * Gets salt of the disclosure
     *
     * @return byte array representing disclosure's salt
     */
    public byte[] getSalt() {
        return salt;
    }

    /**
     * Sets salt of the disclosure
     *
     * @param salt byte array representing disclosure's salt
     */
    public void setSalt(byte[] salt) {
        this.salt = salt;
    }

    /**
     * Gets the name of the disclosure claim
     *
     * @return {@link String}
     */
    public String getClaimName() {
        return claimName;
    }

    /**
     * Sets the name of the disclosure claim
     *
     * @param claimName {@link String}
     */
    public void setClaimName(String claimName) {
        this.claimName = claimName;
    }

    /**
     * Gets the value of the disclosure claim
     *
     * @return {@link Object}
     */
    public Object getClaimValue() {
        return claimValue;
    }

    /**
     * Sets the value of the disclosure claim
     *
     * @param claimValue {@link Object}
     */
    public void setClaimValue(Object claimValue) {
        this.claimValue = claimValue;
    }

}
