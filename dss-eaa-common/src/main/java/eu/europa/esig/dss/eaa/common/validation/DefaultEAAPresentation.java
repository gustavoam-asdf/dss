package eu.europa.esig.dss.eaa.common.validation;

import eu.europa.esig.dss.eaa.common.validation.identifier.EAAPresentationIdentifierBuilder;
import eu.europa.esig.dss.enumerations.DigestAlgorithm;
import eu.europa.esig.dss.enumerations.DigestMatcherType;
import eu.europa.esig.dss.model.Digest;
import eu.europa.esig.dss.model.ReferenceValidation;
import eu.europa.esig.dss.model.eaa.Disclosure;
import eu.europa.esig.dss.model.eaa.DisclosureValidation;
import eu.europa.esig.dss.model.eaa.SelectivelyDisclosableClaim;
import eu.europa.esig.dss.model.identifier.Identifier;
import eu.europa.esig.dss.spi.eaa.EAAPayload;
import eu.europa.esig.dss.spi.eaa.EAAPresentation;
import eu.europa.esig.dss.spi.signature.AdvancedSignature;
import eu.europa.esig.dss.utils.Utils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Abstract implementation of an EAA Presentation
 *
 */
public abstract class DefaultEAAPresentation implements EAAPresentation {

    private static final Logger LOG = LoggerFactory.getLogger(DefaultEAAPresentation.class);

    /** Cached signature objects used to create the EAA */
    private List<AdvancedSignature> signatures;

    /** List of disclosures attached to the EAA Presentation */
    private List<Disclosure> disclosures;

    /** Key binding signature (optional) */
    private AdvancedSignature keyBindingSignature;

    /** Payload of the EAA */
    private EAAPayload payload;

    /** The name of the EAA document */
    private String filename;

    /** Unique EAA Presentation identifier */
    private Identifier identifier;

    /** Cached list of the disclosure validation */
    private List<DisclosureValidation> disclosureValidations;

    /**
     * Default constructor
     */
    protected DefaultEAAPresentation() {
        // empty
    }

    @Override
    public String getFilename() {
        return filename;
    }

    @Override
    public List<AdvancedSignature> getSignatures() {
        return signatures;
    }

    /**
     * Gets a list of disclosures
     *
     * @return a list of {@link Disclosure}s
     */
    public List<Disclosure> getDisclosures() {
        return disclosures;
    }

    @Override
    public List<DisclosureValidation> getDisclosureValidations() {
        if (disclosureValidations == null) {
            disclosureValidations = validateDisclosures();
        }
        return disclosureValidations;
    }

    /**
     * Validates attached disclosures
     *
     * @return a list of {@link DisclosureValidation}s
     */
    protected List<DisclosureValidation> validateDisclosures() {
        List<SelectivelyDisclosableClaim> sdClaims = getPayload().getSelectiveDisclosableClaims();
        if (Utils.isCollectionEmpty(sdClaims)) {
            LOG.info("The EAA Presentation does not contain selectively disclosable claims.");
        }
        DigestAlgorithm digestAlgorithm = getPayload().getSelectiveDisclosableClaimDigestAlgorithm();
        if (digestAlgorithm == null) {
            LOG.warn("No DigestAlgorithm has been found for the selectively disclosable claim hashes! Validation is not possible.");
        }

        final List<DisclosureValidation> validations = validateDisclosuresRecursively(
                sdClaims, digestAlgorithm, disclosures, DigestMatcherType.EAA_DISCLOSURE);

        if (Utils.isCollectionNotEmpty(disclosures)) {
            List<Disclosure> identifiedDisclosures = getIdentifiedDisclosures(validations);
            for (Disclosure disclosure : disclosures) {
                if (!identifiedDisclosures.contains(disclosure)) {
                    DisclosureValidation disclosureValidation = new DisclosureValidation(disclosure);
                    disclosureValidation.setType(DigestMatcherType.EAA_DISCLOSURE);
                    disclosureValidation.setDigest(disclosure.getDigest(digestAlgorithm));
                    disclosureValidation.setFound(false);
                    disclosureValidation.setIntact(false);
                    validations.add(disclosureValidation);
                }
            }

        } else {
            LOG.info("No disclosures have been provided with the EAA Presentation.");
        }

        return validations;
    }

    private List<DisclosureValidation> validateDisclosuresRecursively(List<SelectivelyDisclosableClaim> sdClaims,
                                                                      DigestAlgorithm digestAlgorithm, List<Disclosure> disclosures, DigestMatcherType disclosureType) {
        final List<DisclosureValidation> result = new ArrayList<>();

        for (SelectivelyDisclosableClaim sdClaim : sdClaims) {
            DisclosureValidation disclosureValidation;
            Disclosure disclosure = getDisclosureForClaim(disclosures, digestAlgorithm, sdClaim);
            if (disclosure != null) {
                disclosureValidation = new DisclosureValidation(disclosure);
                disclosureValidation.setType(disclosureType);
                disclosureValidation.setDigest(new Digest(digestAlgorithm, sdClaim.getDigestValue()));
                disclosureValidation.setFound(true);
                disclosureValidation.setIntact(true);

                if (disclosure.getClaimName() == null) {
                    if (sdClaim.getClaimName() != null) {
                        disclosureValidation.setName(sdClaim.getClaimName());

                    } else {
                        LOG.warn("The disclosure does not contain a claim name, when matching a " +
                                "selectively disclosable claim hash entry. The disclosure will be invalidated.");
                        disclosureValidation.setIntact(false);
                    }

                } else if (sdClaim.getClaimName() != null && !sdClaim.getClaimName().equals(disclosure.getClaimName())) {
                    LOG.warn("The matching disclosure's claim name '{}', does not correspond to the name of " +
                                    "the selectively disclosable claim '{}'. The disclosure will be invalidated",
                            disclosure.getClaimName(), sdClaim.getClaimName());
                    disclosureValidation.setIntact(false);

                }

                List<SelectivelyDisclosableClaim> nestedSDClaims = disclosure.getNestedSelectivelyDisclosableClaims();
                if (Utils.isCollectionNotEmpty(nestedSDClaims)) {
                    List<DisclosureValidation> nestedDisclosuresValidations =
                            validateDisclosuresRecursively(nestedSDClaims, digestAlgorithm, disclosures, DigestMatcherType.EAA_NESTED_DISCLOSURE);
                    disclosureValidation.getDependentValidations().addAll(nestedDisclosuresValidations);
                }

            } else {
                disclosureValidation = new DisclosureValidation();
                disclosureValidation.setType(DigestMatcherType.EAA_ORPHAN_SELECTIVELY_DISCLOSABLE_CLAIM);
                disclosureValidation.setDigest(new Digest(digestAlgorithm, sdClaim.getDigestValue()));
                disclosureValidation.setName(sdClaim.getClaimName()); // can be null
            }
            result.add(disclosureValidation);
        }

        return result;
    }

    private Disclosure getDisclosureForClaim(List<Disclosure> disclosures, DigestAlgorithm digestAlgorithm, SelectivelyDisclosableClaim sdClaim) {
        for (Disclosure disclosure : disclosures) {
            if (Arrays.equals(sdClaim.getDigestValue(), disclosure.getDigest(digestAlgorithm).getValue())) {
                return disclosure;
            }
        }
        return null;
    }

    private List<Disclosure> getIdentifiedDisclosures(List<DisclosureValidation> validations) {
        if (Utils.isCollectionEmpty(validations)) {
            return Collections.emptyList();
        }
        final List<Disclosure> disclosuresList = new ArrayList<>();
        for (DisclosureValidation validation : validations) {
            disclosuresList.addAll(extractApplicableDisclosuresRecursively(validation));
        }
        return disclosuresList;
    }

    private List<Disclosure> extractApplicableDisclosuresRecursively(DisclosureValidation validation) {
        final List<Disclosure> disclosuresList = new ArrayList<>();
        if (validation.getDisclosure() != null) {
            disclosuresList.add(validation.getDisclosure());
        }
        List<ReferenceValidation> dependentValidations = validation.getDependentValidations();
        if (Utils.isCollectionNotEmpty(dependentValidations)) {
            for (ReferenceValidation referenceValidation : dependentValidations) {
                if (!(referenceValidation instanceof DisclosureValidation)) {
                    throw new IllegalStateException("DisclosureValidation's dependent references shall be of DisclosureValidation type!");
                }
                DisclosureValidation dependentValidation = (DisclosureValidation) referenceValidation;
                disclosuresList.addAll(extractApplicableDisclosuresRecursively(dependentValidation));
            }
        }
        return disclosuresList;
    }

    @Override
    public AdvancedSignature getKeyBindingSignature() {
        return keyBindingSignature;
    }

    @Override
    public EAAPayload getPayload() {
        if (payload == null) {
            payload = buildPayload();
        }
        return payload;
    }

    /**
     * Builds the EAA payload object for values accessing
     *
     * @return {@link EAAPayload}
     */
    protected abstract EAAPayload buildPayload();

    @Override
    public String getId() {
        return getDSSId().asXmlId();
    }

    @Override
    public Identifier getDSSId() {
        if (identifier == null) {
            identifier = new EAAPresentationIdentifierBuilder().build(this);
        }
        return identifier;
    }

    /**
     * This class is used to build a DefaultEAAPresentation
     *
     */
    protected static abstract class DefaultEAAPresentationBuilder {

        /** Cached signature objects used to create the EAA */
        private List<AdvancedSignature> signatures;

        /** List of disclosures attached to the EAA Presentation */
        private List<Disclosure> disclosures;

        /** Key binding signature (optional) */
        private AdvancedSignature keyBindingSignature;

        /** The name of the EAA document */
        private String filename;

        /**
         * Default constructor
         */
        public DefaultEAAPresentationBuilder() {
            // empty
        }

        /**
         * Sets signatures list used to create the EAA
         *
         * @param signatures a list of {@link AdvancedSignature}s
         * @return this builder
         */
        public DefaultEAAPresentationBuilder setSignatures(List<AdvancedSignature> signatures) {
            this.signatures = signatures;
            return this;
        }

        /**
         * Sets a list of disclosures provided with the SD-JWT VC token
         *
         * @param disclosures a list of {@link Disclosure}s
         * @return this builder
         */
        public DefaultEAAPresentationBuilder setDisclosures(List<Disclosure> disclosures) {
            this.disclosures = disclosures;
            return this;
        }

        /**
         * Sets the key binding signature, when present
         *
         * @param keyBindingSignature {@link AdvancedSignature}
         * @return this builder
         */
        public DefaultEAAPresentationBuilder setKeyBindingSignature(AdvancedSignature keyBindingSignature) {
            this.keyBindingSignature = keyBindingSignature;
            return this;
        }

        /**
         * Sets the document filename
         *
         * @param filename {@link String}
         * @return this builder
         */
        public DefaultEAAPresentationBuilder setFilename(String filename) {
            this.filename = filename;
            return this;
        }

        /**
         * Builds a new EAA Presentation object
         *
         * @return {@link DefaultEAAPresentation}
         */
        public DefaultEAAPresentation build() {
            if (Utils.isCollectionEmpty(signatures)) {
                throw new NullPointerException("Signatures list cannot be null or empty!");
            }
            DefaultEAAPresentation eaaPresentation = initEAAPresentation();
            eaaPresentation.signatures = signatures;
            eaaPresentation.disclosures = disclosures;
            eaaPresentation.keyBindingSignature = keyBindingSignature;
            eaaPresentation.filename = filename;
            return eaaPresentation;
        }

        protected abstract DefaultEAAPresentation initEAAPresentation();

    }

}
