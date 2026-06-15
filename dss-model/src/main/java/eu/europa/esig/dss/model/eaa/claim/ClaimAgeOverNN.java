package eu.europa.esig.dss.model.eaa.claim;

/**
 * Defined a claim containing a boolean value whether the age of EAA holder is over or less a defined value
 *
 */
public interface ClaimAgeOverNN extends Claim {

    /**
     * Gets the value of the age corresponding to the claim definition
     *
     * @return {@link Integer} representing an age
     */
    Integer getAge();

}
