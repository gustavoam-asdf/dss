package eu.europa.esig.dss.model.eaa.claim;

/**
 * Represents a "4.8 Credential Subject" claim defined in W3C Verifiable Credentials Data Model v2.0.
 *
 */
public interface ClaimCredentialSubject extends Claim {

    /**
     * Gets the user's full name information, when present
     *
     * @return {@link ClaimString}
     */
    ClaimString getFullName();

    /**
     * Gets the user's first or given name information, when present
     *
     * @return {@link ClaimString}
     */
    ClaimString getFirstName();

    /**
     * Gets the user's last name or surname information, when present
     *
     * @return {@link ClaimString}
     */
    ClaimString getLastName();

    /**
     * Gets the user's middle name information, when present
     *
     * @return {@link ClaimString}
     */
    ClaimString getMiddleName();

    /**
     * Gets the user's casual name information, when present
     *
     * @return {@link ClaimString}
     */
    ClaimString getNickname();

    /**
     * Gets the user's preferred name, usually a shorthand name, when present
     *
     * @return {@link ClaimString}
     */
    ClaimString getShortName();

    /**
     * Gets the user's profile page URL, when present
     *
     * @return {@link ClaimString}
     */
    ClaimString getProfileUrl();

    /**
     * Gets the user's profile picture URL, when present
     *
     * @return {@link ClaimString}
     */
    ClaimString getPictureUrl();

    /**
     * Gets the user's website or blog URL, when present
     *
     * @return {@link ClaimString}
     */
    ClaimString getWebsiteUrl();

    /**
     * Gets the user's preferred email address, when present
     *
     * @return {@link ClaimString}
     */
    ClaimString getEmail();

    /**
     * Gets whether the user's email address has been verified, when present
     *
     * @return {@link ClaimBoolean}
     */
    ClaimBoolean getEmailVerified();

    /**
     * Gets the user's gender, when present
     *
     * @return {@link ClaimString}
     */
    ClaimString getGender();

    /**
     * Gets the user's birthdate, when present
     *
     * @return {@link ClaimDate}
     */
    ClaimDate getBirthdate();

    /**
     * Gets the user's TimeZone, when present
     *
     * @return {@link ClaimString}
     */
    ClaimString getTimezone();

    /**
     * Gets the user's locale, when present
     *
     * @return {@link ClaimString}
     */
    ClaimString getLocale();

    /**
     * Gets the user's full postal or physical address, when present
     *
     * @return {@link ClaimString}
     */
    ClaimAddress getAddress();

    /**
     * Gets the user's preferred telephone number, when present
     *
     * @return {@link ClaimString}
     */
    ClaimString getPhoneNumber();

    /**
     * Gets whether the user's preferred telephone number has been verified, when present
     *
     * @return {@link ClaimBoolean}
     */
    ClaimBoolean getPhoneNumberVerified();

    /**
     * Gets user's place of birth, when present
     *
     * @return {@link ClaimPlaceOfBirth}
     */
    ClaimPlaceOfBirth getPlaceOfBirth();

    /**
     * Gets user's nationalities using ICAO 3-letter codes, when present
     *
     * @return {@link ClaimArray}
     */
    ClaimArray getNationalities();

    /**
     * Gets user's first or given name when they were born, when present
     *
     * @return {@link ClaimString}
     */
    ClaimString getBirthFirstName();

    /**
     * Gets user's family or last name when they were born, when present
     *
     * @return {@link ClaimString}
     */
    ClaimString getBirthLastName();

    /**
     * Gets user's middle name when they were born, when present
     *
     * @return {@link ClaimString}
     */
    ClaimString getBirthMiddleName();

    /**
     * Gets user's salutation, e.g., "Mr", when present
     *
     * @return {@link ClaimString}
     */
    ClaimString getSalutation();

    /**
     * Gets user's title, e.g., "Dr", when present
     *
     * @return {@link ClaimString}
     */
    ClaimString getTitle();

    /**
     * Gets user's mobile phone number, when present
     *
     * @return {@link ClaimString}
     */
    ClaimString getMobilePhoneNumber();

    /**
     * Gets user's stage name, religious name or any other type of alias/pseudonym, when present
     *
     * @return {@link ClaimString}
     */
    ClaimString getPseudonym();

}
