package eu.europa.esig.dss.diagnostic.claim;

import eu.europa.esig.dss.diagnostic.jaxb.XmlCredentialSubject;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * This class provides a NPE safe initialization and returns always the first credential sibject value, when applicable
 * 
 */
public class CredentialSubjectProxy {
    
    /** Wrapped list of credential subjects */
    private final List<XmlCredentialSubject> xmlCredentialSubjectList;

    /**
     * Default constructor
     * 
     * @param xmlCredentialSubjectList a list of {@link XmlCredentialSubject}s
     */
    public CredentialSubjectProxy(final List<XmlCredentialSubject> xmlCredentialSubjectList) {
        this.xmlCredentialSubjectList = xmlCredentialSubjectList;
    }

    /**
     * Gets a list of credential subjects
     * 
     * @return a list of {@link CredentialSubjectWrapper}s
     */
    public List<CredentialSubjectWrapper> getCredentialSubjects() {
        if (xmlCredentialSubjectList == null || xmlCredentialSubjectList.isEmpty()) {
            return Collections.emptyList();
        }
        return xmlCredentialSubjectList.stream().map(CredentialSubjectWrapper::new).collect(Collectors.toList());
    }

    /**
     * Gets the first credential subject, when defined. Returns null otherwise
     * 
     * @return {@link CredentialSubjectWrapper}
     */
    public CredentialSubjectWrapper getFirstCredentialSubject() {
        if (xmlCredentialSubjectList == null || xmlCredentialSubjectList.isEmpty()) {
            return null;
        }
        return new CredentialSubjectWrapper(xmlCredentialSubjectList.get(0));
    }
    
    /**
     * Gets 's full name when defined within the first Credential Subject claim
     *
     * @return {@link ClaimWrapper}
     */
    public ClaimWrapper getFullName() {
        CredentialSubjectWrapper credentialSubject = getFirstCredentialSubject();
        if (credentialSubject != null) {
            return credentialSubject.getFullName();
        }
        return null;
    }

    /**
     * Gets 's first name when defined within the first Credential Subject claim
     *
     * @return {@link ClaimWrapper}
     */
    public ClaimWrapper getFirstName() {
        CredentialSubjectWrapper credentialSubject = getFirstCredentialSubject();
        if (credentialSubject != null) {
            return credentialSubject.getFirstName();
        }
        return null;
    }

    /**
     * Gets 's last or family name when defined within the first Credential Subject claim
     *
     * @return {@link ClaimWrapper}
     */
    public ClaimWrapper getLastName() {
        CredentialSubjectWrapper credentialSubject = getFirstCredentialSubject();
        if (credentialSubject != null) {
            return credentialSubject.getLastName();
        }
        return null;
    }

    /**
     * Gets 's middle name when defined within the first Credential Subject claim
     *
     * @return {@link ClaimWrapper}
     */
    public ClaimWrapper getMiddleName() {
        CredentialSubjectWrapper credentialSubject = getFirstCredentialSubject();
        if (credentialSubject != null) {
            return credentialSubject.getMiddleName();
        }
        return null;
    }

    /**
     * Gets 's alternative name when defined within the first Credential Subject claim
     *
     * @return {@link ClaimWrapper}
     */
    public ClaimWrapper getNickname() {
        CredentialSubjectWrapper credentialSubject = getFirstCredentialSubject();
        if (credentialSubject != null) {
            return credentialSubject.getNickname();
        }
        return null;
    }

    /**
     * Gets 's preferred or short name when defined within the first Credential Subject claim
     *
     * @return {@link ClaimWrapper}
     */
    public ClaimWrapper getShortName() {
        CredentialSubjectWrapper credentialSubject = getFirstCredentialSubject();
        if (credentialSubject != null) {
            return credentialSubject.getShortName();
        }
        return null;
    }

    /**
     * Gets 's profile URL when defined within the first Credential Subject claim
     *
     * @return {@link ClaimWrapper}
     */
    public ClaimWrapper getProfileUrl() {
        CredentialSubjectWrapper credentialSubject = getFirstCredentialSubject();
        if (credentialSubject != null) {
            return credentialSubject.getProfileUrl();
        }
        return null;
    }

    /**
     * Gets 's picture URL when defined within the first Credential Subject claim
     *
     * @return {@link ClaimWrapper}
     */
    public ClaimWrapper getPictureUrl() {
        CredentialSubjectWrapper credentialSubject = getFirstCredentialSubject();
        if (credentialSubject != null) {
            return credentialSubject.getPictureUrl();
        }
        return null;
    }

    /**
     * Gets 's website when defined within the first Credential Subject claim
     *
     * @return {@link ClaimWrapper}
     */
    public ClaimWrapper getWebsiteUrl() {
        CredentialSubjectWrapper credentialSubject = getFirstCredentialSubject();
        if (credentialSubject != null) {
            return credentialSubject.getWebsiteUrl();
        }
        return null;
    }

    /**
     * Gets 's email when defined within the first Credential Subject claim
     *
     * @return {@link ClaimWrapper}
     */
    public ClaimWrapper getEmail() {
        CredentialSubjectWrapper credentialSubject = getFirstCredentialSubject();
        if (credentialSubject != null) {
            return credentialSubject.getEmail();
        }
        return null;
    }

    /**
     * Gets whether the 's website has been verified if defined within the first Credential Subject claim
     *
     * @return {@link ClaimWrapper}
     */
    public ClaimWrapper getEmailVerified() {
        CredentialSubjectWrapper credentialSubject = getFirstCredentialSubject();
        if (credentialSubject != null) {
            return credentialSubject.getEmailVerified();
        }
        return null;
    }

    /**
     * Gets 's gender when defined within the first Credential Subject claim
     *
     * @return {@link ClaimWrapper}
     */
    public ClaimWrapper getGender() {
        CredentialSubjectWrapper credentialSubject = getFirstCredentialSubject();
        if (credentialSubject != null) {
            return credentialSubject.getGender();
        }
        return null;
    }

    /**
     * Gets 's birthdate when defined within the first Credential Subject claim
     *
     * @return {@link ClaimWrapper}
     */
    public ClaimWrapper getBirthdate() {
        CredentialSubjectWrapper credentialSubject = getFirstCredentialSubject();
        if (credentialSubject != null) {
            return credentialSubject.getBirthdate();
        }
        return null;
    }

    /**
     * Gets 's timezone when defined within the first Credential Subject claim
     *
     * @return {@link ClaimWrapper}
     */
    public ClaimWrapper getTimezone() {
        CredentialSubjectWrapper credentialSubject = getFirstCredentialSubject();
        if (credentialSubject != null) {
            return credentialSubject.getTimezone();
        }
        return null;
    }

    /**
     * Gets 's locale when defined within the first Credential Subject claim
     *
     * @return {@link ClaimWrapper}
     */
    public ClaimWrapper getLocale() {
        CredentialSubjectWrapper credentialSubject = getFirstCredentialSubject();
        if (credentialSubject != null) {
            return credentialSubject.getLocale();
        }
        return null;
    }

    /**
     * Gets 's full address, when defined within the first Credential Subject claim
     *
     * @return {@link AddressClaimWrapper}
     */
    public AddressClaimWrapper getAddress() {
        CredentialSubjectWrapper credentialSubject = getFirstCredentialSubject();
        if (credentialSubject != null) {
            return credentialSubject.getAddress();
        }
        return null;
    }

    /**
     * Gets 's city address when defined within the first Credential Subject claim
     *
     * @return {@link ClaimWrapper}
     */
    public ClaimWrapper getAddressCity() {
        AddressClaimWrapper Address = getAddress();
        if (Address != null) {
            return Address.getCity();
        }
        return null;
    }

    /**
     * Gets 's state or region address when defined within the first Credential Subject claim
     *
     * @return {@link ClaimWrapper}
     */
    public ClaimWrapper getAddressStateOrProvince() {
        AddressClaimWrapper Address = getAddress();
        if (Address != null) {
            return Address.getStateOrProvince();
        }
        return null;
    }

    /**
     * Gets 's postal code address when defined within the first Credential Subject claim
     *
     * @return {@link ClaimWrapper}
     */
    public ClaimWrapper getAddressPostalCode() {
        AddressClaimWrapper Address = getAddress();
        if (Address != null) {
            return Address.getPostalCode();
        }
        return null;
    }

    /**
     * Gets 's country address when defined within the first Credential Subject claim.
     * NOTE: The returned value is usually represented by 2-letter ISO country code.
     *
     * @return {@link ClaimWrapper}
     */
    public ClaimWrapper getAddressCountry() {
        AddressClaimWrapper Address = getAddress();
        if (Address != null) {
            return Address.getCountry();
        }
        return null;
    }

    /**
     * Gets 's street address when defined within the first Credential Subject claim
     *
     * @return {@link ClaimWrapper}
     */
    public ClaimWrapper getStreetAddress() {
        AddressClaimWrapper Address = getAddress();
        if (Address != null) {
            return Address.getStreetAddress();
        }
        return null;
    }

    /**
     * Gets 's phone number when defined within the first Credential Subject claim
     *
     * @return {@link ClaimWrapper}
     */
    public ClaimWrapper getPhoneNumber() {
        CredentialSubjectWrapper credentialSubject = getFirstCredentialSubject();
        if (credentialSubject != null) {
            return credentialSubject.getPhoneNumber();
        }
        return null;
    }

    /**
     * Gets whether the 's phone number has been verified if defined within the first Credential Subject claim
     *
     * @return {@link ClaimWrapper}
     */
    public ClaimWrapper getPhoneNumberVerified() {
        CredentialSubjectWrapper credentialSubject = getFirstCredentialSubject();
        if (credentialSubject != null) {
            return credentialSubject.getPhoneNumberVerified();
        }
        return null;
    }

    /**
     * Gets 's place of birth when defined within the first Credential Subject claim
     *
     * @return {@link PlaceOfBirthClaimWrapper}
     */
    public PlaceOfBirthClaimWrapper getPlaceOfBirth() {
        CredentialSubjectWrapper credentialSubject = getFirstCredentialSubject();
        if (credentialSubject != null) {
            return credentialSubject.getPlaceOfBirth();
        }
        return null;
    }

    /**
     * Gets 's nationalities list when defined within the first Credential Subject claim.
     * NOTE: The values are usually represented by 3-letter nationality codes.
     *
     * @return {@link ClaimWrapper}
     */
    public ClaimWrapper getNationalities() {
        CredentialSubjectWrapper credentialSubject = getFirstCredentialSubject();
        if (credentialSubject != null) {
            return credentialSubject.getNationalities();
        }
        return null;
    }

    /**
     * Gets 's last or family name at birth when defined within the first Credential Subject claim
     *
     * @return {@link ClaimWrapper}
     */
    public ClaimWrapper getBirthLastName() {
        CredentialSubjectWrapper credentialSubject = getFirstCredentialSubject();
        if (credentialSubject != null) {
            return credentialSubject.getBirthLastName();
        }
        return null;
    }

    /**
     * Gets 's first name at birth when defined within the first Credential Subject claim
     *
     * @return {@link ClaimWrapper}
     */
    public ClaimWrapper getBirthFirstName() {
        CredentialSubjectWrapper credentialSubject = getFirstCredentialSubject();
        if (credentialSubject != null) {
            return credentialSubject.getBirthFirstName();
        }
        return null;
    }

    /**
     * Gets 's middle name at birth when defined within the first Credential Subject claim
     *
     * @return {@link ClaimWrapper}
     */
    public ClaimWrapper getBirthMiddleName() {
        CredentialSubjectWrapper credentialSubject = getFirstCredentialSubject();
        if (credentialSubject != null) {
            return credentialSubject.getBirthMiddleName();
        }
        return null;
    }

    /**
     * Gets 's preferred salutation when defined within the first Credential Subject claim
     *
     * @return {@link ClaimWrapper}
     */
    public ClaimWrapper getSalutation() {
        CredentialSubjectWrapper credentialSubject = getFirstCredentialSubject();
        if (credentialSubject != null) {
            return credentialSubject.getSalutation();
        }
        return null;
    }

    /**
     * Gets 's title when defined within the first Credential Subject claim
     *
     * @return {@link ClaimWrapper}
     */
    public ClaimWrapper getTitle() {
        CredentialSubjectWrapper credentialSubject = getFirstCredentialSubject();
        if (credentialSubject != null) {
            return credentialSubject.getTitle();
        }
        return null;
    }

    /**
     * Gets 's mobile phone number when defined within the first Credential Subject claim
     *
     * @return {@link ClaimWrapper}
     */
    public ClaimWrapper getMobilePhoneNumber() {
        CredentialSubjectWrapper credentialSubject = getFirstCredentialSubject();
        if (credentialSubject != null) {
            return credentialSubject.getMobilePhoneNumber();
        }
        return null;
    }

    /**
     * Gets 's scenic name or pseudonym, they are known as, when defined within the first Credential Subject claim
     *
     * @return {@link ClaimWrapper}
     */
    public ClaimWrapper getPseudonym() {
        CredentialSubjectWrapper credentialSubject = getFirstCredentialSubject();
        if (credentialSubject != null) {
            return credentialSubject.getPseudonym();
        }
        return null;
    }

    /**
     * Gets a list of claims incorporated within the Credential Subject or provided as disclosures,
     * which are not (yet) directly supported by the implementation.
     *
     * @return a lust of {@link ClaimWrapper}s
     */
    public List<ClaimWrapper> getOtherClaims() {
        CredentialSubjectWrapper credentialSubject = getFirstCredentialSubject();
        if (credentialSubject != null) {
            return credentialSubject.getOtherClaims();
        }
        return Collections.emptyList();
    }
    
}
