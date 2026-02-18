package eu.europa.esig.dss.spi.eaa;

import eu.europa.esig.dss.enumerations.DigestAlgorithm;
import eu.europa.esig.dss.model.eaa.claim.Claim;
import eu.europa.esig.dss.model.eaa.claim.ClaimAddress;
import eu.europa.esig.dss.model.eaa.claim.ClaimArray;
import eu.europa.esig.dss.model.eaa.claim.ClaimBinaries;
import eu.europa.esig.dss.model.eaa.claim.ClaimBoolean;
import eu.europa.esig.dss.model.eaa.claim.ClaimDate;
import eu.europa.esig.dss.model.eaa.claim.ClaimPlaceOfBirth;
import eu.europa.esig.dss.model.eaa.claim.ClaimStatus;
import eu.europa.esig.dss.model.eaa.claim.ClaimString;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * Provides an interface for accessing the content of the EAA payload
 */
public interface EAAPayload extends Serializable {

    /**
     * Gets a list of selectively disclosable claims provided within the EAA payload
     *
     * @return a list of {@link ClaimBinaries}s
     */
    List<ClaimBinaries> getSelectiveDisclosableClaims();

    /**
     * Gets a DigestAlgorithm defined within an EAA payload used to create hashes for the selective disclosures
     *
     * @return {@link DigestAlgorithm}
     */
    DigestAlgorithm getSelectiveDisclosableClaimDigestAlgorithm();

    /**
     * Gets the EAA's unique identifier, when present
     *
     * @return {@link ClaimString}
     */
    ClaimString getIdentifier();

    /**
     * Gets the EAA's issuer, when present
     *
     * @return {@link ClaimString}
     */
    ClaimString getIssuer();

    /**
     * Gets the EAA's subject, when present
     *
     * @return {@link ClaimString}
     */
    ClaimString getSubject();

    /**
     * Gets the list of recipients the EAA is intended for, when present
     *
     * @return {@link ClaimArray}
     */
    ClaimArray getAudience();

    /**
     * Gets the expiration time of the EAA, after which the EAA is not accepted for processing, when present
     *
     * @return {@link ClaimDate}
     */
    ClaimDate getExpirationTime();

    /**
     * Gets the time before which the EAA is not accepted for processing, when present
     *
     * @return {@link ClaimDate}
     */
    ClaimDate getNotBeforeTime();

    /**
     * Gets the time at which the EAA was issued, when present
     *
     * @return {@link ClaimDate}
     */
    ClaimDate getIssuedAtTime();

    /**
     * Gets the time at which the information present within the EAA was the last time updated, when present
     *
     * @return {@link ClaimDate}
     */
    ClaimDate getUpdatedAtTime();

    /**
     * Gets the EAA category URN, when present
     *
     * @return {@link ClaimString}
     */
    ClaimString getCategory();

    /**
     * Gets the EAA's Metadata type, when present
     *
     * @return {@link ClaimString}
     */
    ClaimString getMetadataType();

    /**
     * Gets the Digest Algorithm used to compute EAA's Metadata integrity digest, when present
     *
     * @return {@link DigestAlgorithm}
     */
    DigestAlgorithm getMetadataDigestAlgorithm();

    /**
     * Gets the EAA's Metadata integrity digest value, when present
     *
     * @return {@link ClaimBinaries}
     */
    ClaimBinaries getMetadataDigestValue();

    /**
     * Gets the EAA's Status value, when present
     *
     * @return {@link ClaimStatus}
     */
    ClaimStatus getStatus();

    /**
     * Gets the EAA's nonce value, used to associate the Client's session Id with the EAA, when present
     *
     * @return {@link ClaimString}
     */
    ClaimString getNonce();

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

    /**
     * Gets a map of all used header names and claims present within the EAA payload.
     * NOTE: the map contains only clear clames and not selective disclosure digests.
     *
     * @return a map between header names and claim values
     */
    Map<String, Claim> getClaimMap();

}
