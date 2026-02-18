package eu.europa.esig.dss.model.eaa;

import eu.europa.esig.dss.model.ReferenceValidation;
import eu.europa.esig.dss.model.eaa.claim.Claim;
import eu.europa.esig.dss.model.eaa.claim.ClaimArray;
import eu.europa.esig.dss.model.eaa.claim.ClaimBinaries;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

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
    private Claim value;

    /** List of nested claim bimaries */
    private List<ClaimBinaries> nestedClaimBinaries;

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
        Objects.requireNonNull(disclosure, "Disclosure cannot be null!");
        this.disclosure = disclosure;
        this.name = disclosure.getClaimName();
        this.value = disclosure.getClaimValue();
        this.nestedClaimBinaries = disclosure.getNestedSelectivelyDisclosableClaims();
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
    public String getClaimName() {
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
     * Gets the original provided disclosure claim value
     *
     * @return {@link Claim}
     */
    public Claim getValue() {
        return value;
    }

    /**
     * Sets the provided disclosure claim value
     *
     * @param value {@link Claim}
     */
    public void setValue(Claim value) {
        this.value = value;
    }

    /**
     * Sets claim binaries nested within the validated claim
     *
     * @param nestedClaimBinaries a list of {@link ClaimBinaries}
     */
    public void setNestedClaimBinaries(List<ClaimBinaries> nestedClaimBinaries) {
        this.nestedClaimBinaries = nestedClaimBinaries;
    }

    /**
     * Gets the disclosure value combined from the processing of the disclosure together with nested disclosures,
     * when applicable
     *
     * @return {@link Claim}
     */
    public Claim getProcessedValue() {
        if (nestedClaimBinaries == null || nestedClaimBinaries.isEmpty()) {
            return value;
        }
        final List<Claim> claimsList = new ArrayList<>();
        for (ClaimBinaries claimBytes : nestedClaimBinaries) {
            DisclosureValidation matchingDisclosure = getMatchingDisclosureValidation(claimBytes, getNestedDisclosures());
            if (matchingDisclosure != null) {
                claimsList.add(matchingDisclosure.getValue());
            }
        }
        return new ClaimArray(name, claimsList, value != null && value.isSelectivelyDisclosable());
    }

    private DisclosureValidation getMatchingDisclosureValidation(ClaimBinaries claimBytes, List<DisclosureValidation> disclosures) {
        if (disclosures != null && !disclosures.isEmpty()) {
            for (DisclosureValidation nestedDisclosureValidation : disclosures) {
                if (nestedDisclosureValidation.getDigest() != null &&
                        Arrays.equals(claimBytes.getBinariesValue(), nestedDisclosureValidation.getDigest().getValue())) {
                    return nestedDisclosureValidation;
                }
            }
        }
        return null;
    }

    /**
     * Gets a list of nested disclosure validations
     *
     * @return a list of {@link DisclosureValidation}s
     */
    public List<DisclosureValidation> getNestedDisclosures() {
        List<ReferenceValidation> dependentValidations = getDependentValidations();
        if (dependentValidations == null || dependentValidations.isEmpty()) {
            return Collections.emptyList();
        }
        return dependentValidations.stream().map(v -> (DisclosureValidation) v).collect(Collectors.toList());
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
