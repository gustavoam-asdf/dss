package eu.europa.esig.dss.eaa.jwt;

import eu.europa.esig.dss.jades.JWSJsonSerializationObject;
import eu.europa.esig.dss.model.EAADisclosure;

import java.io.Serializable;
import java.util.List;

/**
 * This class represents a parsed SD-JWT object
 *
 */
public class SDJWTSerializationObject implements Serializable {

    private static final long serialVersionUID = 2321956568937413822L;

    /** The EAA signature */
    private JWSJsonSerializationObject signature;

    /** List of disclosures attached to the presentation */
    private List<EAADisclosure> disclosures;

    /** Key binding signature attached to the presentation */
    private JWSJsonSerializationObject keyBindingSignature;

    /**
     * Default constructor instantiating object with null values
     */
    public SDJWTSerializationObject() {
        // empty
    }

    /**
     * Gets the JWS signature used to create the EAA
     *
     * @return {@link JWSJsonSerializationObject}
     */
    public JWSJsonSerializationObject getSignature() {
        return signature;
    }

    /**
     * Sets the JWS signature used to create the EAA
     *
     * @param signature {@link JWSJsonSerializationObject}
     */
    public void setSignature(JWSJsonSerializationObject signature) {
        this.signature = signature;
    }

    /**
     * Gets a list of disclosures supplied with the presentation of Electronic Attestation of Attributes
     *
     * @return a list of {@link EAADisclosure}s
     */
    public List<EAADisclosure> getDisclosures() {
        return disclosures;
    }

    /**
     * Sets a list of disclosures supplied with the presentation of Electronic Attestation of Attributes
     *
     * @param disclosures a list of {@link EAADisclosure}s
     */
    public void setDisclosures(List<EAADisclosure> disclosures) {
        this.disclosures = disclosures;
    }

    /**
     * Gets a key binding signature supplied with the presentation of Electronic Attestation of Attributes
     *
     * @return {@link JWSJsonSerializationObject}
     */
    public JWSJsonSerializationObject getKeyBindingSignature() {
        return keyBindingSignature;
    }

    /**
     * Sets a key binding signature supplied with the presentation of Electronic Attestation of Attributes
     *
     * @param keyBindingSignature {@link JWSJsonSerializationObject}
     */
    public void setKeyBindingSignature(JWSJsonSerializationObject keyBindingSignature) {
        this.keyBindingSignature = keyBindingSignature;
    }

}
