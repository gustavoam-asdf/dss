package eu.europa.esig.dss.eaa.jwt.creation;

import eu.europa.esig.dss.eaa.common.creation.EAADisclosure;

import java.util.Objects;

/**
 * Implementation of a disclosure for an SD-JWT VC token
 *
 */
public class SDJWTEAADisclosure implements EAADisclosure {

    private static final long serialVersionUID = -1978354313189364987L;

    /** Base64Url encoded string */
    private final String disclosure;

    /**
     * Default constructor to instantiate an SD-JWT VC disclosure from a base64url encoded disclosure string.
     * NOTE: the class does not verify the validity of the data structure.
     *
     * @param disclosure {@link String}
     */
    public SDJWTEAADisclosure(final String disclosure) {
        Objects.requireNonNull(disclosure, "Disclosure string cannot be null!");
        this.disclosure = disclosure;
    }

    /**
     * Gets the disclosure string
     *
     * @return {@link String}
     */
    public String getDisclosure() {
        return disclosure;
    }

    @Override
    public byte[] getBytesToBeSigned() {
        return disclosure.getBytes();
    }

}
