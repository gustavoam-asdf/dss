package eu.europa.esig.dss.eaa.mdoc.creation.claim;

import eu.europa.esig.dss.eaa.common.creation.claim.AbstractEAAClaim;

/**
 * Represents an ISO/IEC 18013-5 implementation of a selectively disclosable claim
 *
 */
public class MdocEAAClaim extends AbstractEAAClaim {

    private static final long serialVersionUID = -3226410210721510170L;

    /** Namespace of the element claim */
    private final String namespace;

    /** Integer identifier of the claim digest */
    private Integer digestId;

    /** Salt of the selectively disclosable claim, when applicable */
    private byte[] salt;

    /**
     * Constructor with the claim namespace, name and value
     *
     * @param namespace {@link String}
     * @param name  {@link String} the claim name
     * @param value {@link Object} the value of the claim
     */
    public MdocEAAClaim(String namespace, String name, Object value) {
        this(namespace, name, value, null);
    }

    /**
     * Constructor with the claim namespace, digestId, name and value
     *
     * @param namespace {@link String}
     * @param digestId integer identifier of the claim digest
     * @param name  {@link String} the claim name
     * @param value {@link Object} the value of the claim
     */
    public MdocEAAClaim(String namespace, int digestId, String name, Object value) {
        this(namespace, digestId, name, value, null);
    }

    /**
     * Constructor with the claim namespace, name, value and salt
     *
     * @param namespace {@link String}
     * @param name  {@link String} the claim name
     * @param value {@link Object} the value of the claim
     * @param salt byte array containing a salt with a high entropy used for a digest computation
     */
    public MdocEAAClaim(String namespace, String name, Object value, byte[] salt) {
        super(name, value);
        this.namespace = namespace;
        this.salt = salt;
    }

    /**
     * Constructor with the claim namespace, digestId, name, value and salt
     *
     * @param namespace {@link String}
     * @param digestId integer identifier of the claim digest
     * @param name  {@link String} the claim name
     * @param value {@link Object} the value of the claim
     * @param salt byte array containing a salt with a high entropy used for a digest computation
     */
    public MdocEAAClaim(String namespace, int digestId, String name, Object value, byte[] salt) {
        super(name, value);
        this.namespace = namespace;
        this.digestId = digestId;
        this.salt = salt;
    }

    /**
     * Gets the applicable namespace of the element claim
     *
     * @return {@link String}
     */
    public String getNamespace() {
        return namespace;
    }

    /**
     * Gets the digestId of the claim hash
     *
     * @return {@link Integer}
     */
    public Integer getDigestId() {
        return digestId;
    }

    /**
     * Sets a digest id
     *
     * @param digestId {@link Integer}
     */
    public void setDigestId(Integer digestId) {
        this.digestId = digestId;
    }

    /**
     * Gets the salt
     *
     * @return byte array
     */
    public byte[] getSalt() {
        return salt;
    }

    /**
     * Sets the salt
     *
     * @param salt byte array
     */
    public void setSalt(byte[] salt) {
        this.salt = salt;
    }

}
