package eu.europa.esig.dss.eaa.common.validation;

import eu.europa.esig.dss.enumerations.DigestAlgorithm;
import eu.europa.esig.dss.model.eaa.Disclosure;
import eu.europa.esig.dss.model.eaa.DisclosureValidation;
import eu.europa.esig.dss.spi.eaa.EAAPayload;

import java.util.List;

/**
 * Abstract implementation of EAA Payload Verifier
 *
 */
public abstract class EAAPayloadVerifier {

    /**
     * List of disclosures attached to the EAA Presentation
     */
    protected List<Disclosure> disclosures;

    /**
     * Extracted Digest Algorithm value to be used on hash of disclosures computation
     */
    protected DigestAlgorithm sdDigestAlgorithm;

    /**
     * Computed list of disclosure validations
     */
    protected List<DisclosureValidation> disclosureValidations;

    /**
     * Computed payload, with the matching disclosures
     */
    protected EAAPayload verifiedPayload;

    /**
     * Default constructor
     */
    public EAAPayloadVerifier() {
        // empty
    }

    /**
     * Sets the disclosures, requiring for EAA Payload selectively disclosable claims validation
     *
     * @param disclosures a list of {@link Disclosure}s
     * @return this {@link EAAPayloadVerifier}
     */
    public EAAPayloadVerifier setDisclosures(List<Disclosure> disclosures) {
        this.disclosures = disclosures;
        return this;
    }

    /**
     * This method returns a list of disclosure validation results.
     * Please call the method {@code #verify} before accessing the result of this method.
     *
     * @return a list of {@link DisclosureValidation}s
     */
    public List<DisclosureValidation> getDisclosureValidations() {
        if (disclosureValidations == null) {
            throw new IllegalStateException("The verification of the disclosures has not been yet performed! " +
                    "Please call #verify method before querying the results.");
        }
        return disclosureValidations;
    }

    /**
     * This method returns a payload map constructed using the provided disclosures.
     * Please call the method {@code #verify} before accessing the result of this method.
     *
     * @return {@link EAAPayload}
     */
    public EAAPayload getVerifiedPayload() {
        if (verifiedPayload == null) {
            throw new IllegalStateException("The verification of the payload and matching disclosures has not been yet performed! " +
                    "Please call #verify method before querying the results.");
        }
        return verifiedPayload;
    }

    /**
     * This method performs the verification process for the provided payload and disclosures
     * NOTE: The process can be executed only once
     */
    public abstract void verify();

}
