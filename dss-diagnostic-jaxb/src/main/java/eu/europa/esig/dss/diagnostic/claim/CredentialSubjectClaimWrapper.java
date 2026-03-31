package eu.europa.esig.dss.diagnostic.claim;

import eu.europa.esig.dss.diagnostic.jaxb.XmlAddressClaim;
import eu.europa.esig.dss.diagnostic.jaxb.XmlClaim;
import eu.europa.esig.dss.diagnostic.jaxb.XmlCredentialSubjectClaim;

import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Wraps an {@code eu.europa.esig.dss.diagnostic.jaxb.XmlCredentialSubjectClaimClaim}
 *
 */
public class CredentialSubjectClaimWrapper extends ClaimWrapper {

    /**
     * Default constructor
     *
     * @param wrapped {@link XmlCredentialSubjectClaim}
     */
    public CredentialSubjectClaimWrapper(final XmlCredentialSubjectClaim wrapped) {
        super(wrapped);
    }
    
    /**
     * Gets user's full name when defined within Credential Subject claim
     *
     * @return {@link ClaimWrapper}
     */
    public ClaimWrapper getFullName() {
        XmlClaim xmlClaim = getWrapped().getFullName();
        if (xmlClaim != null) {
            return new ClaimWrapper(xmlClaim, this);
        }
        return null;
    }

    /**
     * Gets user's first name when defined within Credential Subject claim
     *
     * @return {@link ClaimWrapper}
     */
    public ClaimWrapper getFirstName() {
        XmlClaim xmlClaim = getWrapped().getFirstName();
        if (xmlClaim != null) {
            return new ClaimWrapper(xmlClaim, this);
        }
        return null;
    }

    /**
     * Gets user's last or family name when defined within Credential Subject claim
     *
     * @return {@link ClaimWrapper}
     */
    public ClaimWrapper getLastName() {
        XmlClaim xmlClaim = getWrapped().getLastName();
        if (xmlClaim != null) {
            return new ClaimWrapper(xmlClaim, this);
        }
        return null;
    }

    /**
     * Gets user's middle name when defined within Credential Subject claim
     *
     * @return {@link ClaimWrapper}
     */
    public ClaimWrapper getMiddleName() {
        XmlClaim xmlClaim = getWrapped().getMiddleName();
        if (xmlClaim != null) {
            return new ClaimWrapper(xmlClaim, this);
        }
        return null;
    }

    /**
     * Gets user's alternative name when defined within Credential Subject claim
     *
     * @return {@link ClaimWrapper}
     */
    public ClaimWrapper getNickname() {
        XmlClaim xmlClaim = getWrapped().getNickname();
        if (xmlClaim != null) {
            return new ClaimWrapper(xmlClaim, this);
        }
        return null;
    }

    /**
     * Gets user's preferred or short name when defined within Credential Subject claim
     *
     * @return {@link ClaimWrapper}
     */
    public ClaimWrapper getShortName() {
        XmlClaim xmlClaim = getWrapped().getShortName();
        if (xmlClaim != null) {
            return new ClaimWrapper(xmlClaim, this);
        }
        return null;
    }

    /**
     * Gets user's profile URL when defined within Credential Subject claim
     *
     * @return {@link ClaimWrapper}
     */
    public ClaimWrapper getProfileUrl() {
        XmlClaim xmlClaim = getWrapped().getProfileUrl();
        if (xmlClaim != null) {
            return new ClaimWrapper(xmlClaim, this);
        }
        return null;
    }

    /**
     * Gets user's picture URL when defined within Credential Subject claim
     *
     * @return {@link ClaimWrapper}
     */
    public ClaimWrapper getPictureUrl() {
        XmlClaim xmlClaim = getWrapped().getPictureUrl();
        if (xmlClaim != null) {
            return new ClaimWrapper(xmlClaim, this);
        }
        return null;
    }

    /**
     * Gets user's website when defined within Credential Subject claim
     *
     * @return {@link ClaimWrapper}
     */
    public ClaimWrapper getWebsiteUrl() {
        XmlClaim xmlClaim = getWrapped().getWebsiteUrl();
        if (xmlClaim != null) {
            return new ClaimWrapper(xmlClaim, this);
        }
        return null;
    }

    /**
     * Gets user's email when defined within Credential Subject claim
     *
     * @return {@link ClaimWrapper}
     */
    public ClaimWrapper getEmail() {
        XmlClaim xmlClaim = getWrapped().getEmail();
        if (xmlClaim != null) {
            return new ClaimWrapper(xmlClaim, this);
        }
        return null;
    }

    /**
     * Gets whether the user's website has been verified if defined within Credential Subject claim
     *
     * @return {@link Boolean}
     */
    public ClaimWrapper getEmailVerified() {
        XmlClaim xmlClaim = getWrapped().getEmailVerified();
        if (xmlClaim != null) {
            return new ClaimWrapper(xmlClaim, this);
        }
        return null;
    }

    /**
     * Gets user's gender when defined within Credential Subject claim
     *
     * @return {@link ClaimWrapper}
     */
    public ClaimWrapper getGender() {
        XmlClaim xmlClaim = getWrapped().getGender();
        if (xmlClaim != null) {
            return new ClaimWrapper(xmlClaim, this);
        }
        return null;
    }

    /**
     * Gets user's birthdate when defined within Credential Subject claim
     *
     * @return {@link Date}
     */
    public ClaimWrapper getBirthdate() {
        XmlClaim xmlClaim = getWrapped().getBirthdate();
        if (xmlClaim != null) {
            return new ClaimWrapper(xmlClaim, this);
        }
        return null;
    }

    /**
     * Gets user's timezone when defined within Credential Subject claim
     *
     * @return {@link ClaimWrapper}
     */
    public ClaimWrapper getTimezone() {
        XmlClaim xmlClaim = getWrapped().getTimezone();
        if (xmlClaim != null) {
            return new ClaimWrapper(xmlClaim, this);
        }
        return null;
    }

    /**
     * Gets user's locale when defined within Credential Subject claim
     *
     * @return {@link ClaimWrapper}
     */
    public ClaimWrapper getLocale() {
        XmlClaim xmlClaim = getWrapped().getLocale();
        if (xmlClaim != null) {
            return new ClaimWrapper(xmlClaim, this);
        }
        return null;
    }

    /**
     * Gets user's full address, when defined within Credential Subject claim
     *
     * @return {@link ClaimWrapper}
     */
    public AddressClaimWrapper getAddress() {
        XmlAddressClaim xmlAddressClaim = getWrapped().getAddress();
        if (xmlAddressClaim != null) {
            return new AddressClaimWrapper(xmlAddressClaim, this);
        }
        return null;
    }

    /**
     * Gets user's phone number when defined within Credential Subject claim
     *
     * @return {@link ClaimWrapper}
     */
    public ClaimWrapper getPhoneNumber() {
        XmlClaim xmlClaim = getWrapped().getPhoneNumber();
        if (xmlClaim != null) {
            return new ClaimWrapper(xmlClaim, this);
        }
        return null;
    }

    /**
     * Gets whether the user's phone number has been verified if defined within Credential Subject claim
     *
     * @return {@link ClaimWrapper}
     */
    public ClaimWrapper getPhoneNumberVerified() {
        XmlClaim xmlClaim = getWrapped().getPhoneNumberVerified();
        if (xmlClaim != null) {
            return new ClaimWrapper(xmlClaim, this);
        }
        return null;
    }

    /**
     * Gets user's country of birth when defined within Credential Subject claim
     *
     * @return {@link PlaceOfBirthClaimWrapper}
     */
    public PlaceOfBirthClaimWrapper getPlaceOfBirth() {
        XmlClaim placeOfBirth = getWrapped().getPlaceOfBirth();
        if (placeOfBirth != null) {
            return new PlaceOfBirthClaimWrapper(placeOfBirth, this);
        }
        return null;
    }

    /**
     * Gets user's nationalities list when defined within Credential Subject claim.
     * NOTE: The values are usually represented by 3-letter nationality codes.
     *
     * @return a list of {@link String}s
     */
    public ClaimWrapper getNationalities() {
        XmlClaim nationalities = getWrapped().getNationalities();
        if (nationalities != null) {
            return new ClaimWrapper(nationalities, this);
        }
        return null;
    }

    /**
     * Gets user's last or family name at birth when defined within Credential Subject claim
     *
     * @return {@link ClaimWrapper}
     */
    public ClaimWrapper getBirthLastName() {
        XmlClaim xmlClaim = getWrapped().getBirthLastName();
        if (xmlClaim != null) {
            return new ClaimWrapper(xmlClaim, this);
        }
        return null;
    }

    /**
     * Gets user's first name at birth when defined within Credential Subject claim
     *
     * @return {@link ClaimWrapper}
     */
    public ClaimWrapper getBirthFirstName() {
        XmlClaim xmlClaim = getWrapped().getBirthFirstName();
        if (xmlClaim != null) {
            return new ClaimWrapper(xmlClaim, this);
        }
        return null;
    }

    /**
     * Gets user's middle name at birth when defined within Credential Subject claim
     *
     * @return {@link ClaimWrapper}
     */
    public ClaimWrapper getBirthMiddleName() {
        XmlClaim xmlClaim = getWrapped().getBirthMiddleName();
        if (xmlClaim != null) {
            return new ClaimWrapper(xmlClaim, this);
        }
        return null;
    }

    /**
     * Gets user's preferred salutation when defined within Credential Subject claim
     *
     * @return {@link ClaimWrapper}
     */
    public ClaimWrapper getSalutation() {
        XmlClaim xmlClaim = getWrapped().getSalutation();
        if (xmlClaim != null) {
            return new ClaimWrapper(xmlClaim, this);
        }
        return null;
    }

    /**
     * Gets user's title when defined within Credential Subject claim
     *
     * @return {@link ClaimWrapper}
     */
    public ClaimWrapper getTitle() {
        XmlClaim xmlClaim = getWrapped().getTitle();
        if (xmlClaim != null) {
            return new ClaimWrapper(xmlClaim, this);
        }
        return null;
    }

    /**
     * Gets user's mobile phone number when defined within Credential Subject claim
     *
     * @return {@link ClaimWrapper}
     */
    public ClaimWrapper getMobilePhoneNumber() {
        XmlClaim xmlClaim = getWrapped().getMobilePhoneNumber();
        if (xmlClaim != null) {
            return new ClaimWrapper(xmlClaim, this);
        }
        return null;
    }

    /**
     * Gets user's scenic name or pseudonym, they are known as, when defined within Credential Subject claim
     *
     * @return {@link ClaimWrapper}
     */
    public ClaimWrapper getPseudonym() {
        XmlClaim xmlClaim = getWrapped().getPseudonym();
        if (xmlClaim != null) {
            return new ClaimWrapper(xmlClaim, this);
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
        if (getWrapped().getOtherClaim() != null) {
            return getWrapped().getOtherClaim().stream().map(c -> new ClaimWrapper(c, this)).collect(Collectors.toList());
        }
        return Collections.emptyList();
    }

    @Override
    public boolean isMap() {
        return true;
    }

    @Override
    public Map<String, ClaimWrapper> getMap() {
        final Map<String, ClaimWrapper> result = new HashMap<>(super.getMap());
        ClaimWrapper fullName = getFullName();
        if (fullName != null) {
            result.put(fullName.getName(), fullName);
        }
        ClaimWrapper firstName = getFirstName();
        if (firstName != null) {
            result.put(firstName.getName(), firstName);
        }
        ClaimWrapper lastName = getLastName();
        if (lastName != null) {
            result.put(lastName.getName(), lastName);
        }
        ClaimWrapper middleName = getMiddleName();
        if (middleName != null) {
            result.put(middleName.getName(), middleName);
        }
        ClaimWrapper nickname = getNickname();
        if (nickname != null) {
            result.put(nickname.getName(), nickname);
        }
        ClaimWrapper shortName = getShortName();
        if (shortName != null) {
            result.put(shortName.getName(), shortName);
        }
        ClaimWrapper profileUrl = getProfileUrl();
        if (profileUrl != null) {
            result.put(profileUrl.getName(), profileUrl);
        }
        ClaimWrapper pictureUrl = getPictureUrl();
        if (pictureUrl != null) {
            result.put(pictureUrl.getName(), pictureUrl);
        }
        ClaimWrapper websiteUrl = getWebsiteUrl();
        if (websiteUrl != null) {
            result.put(websiteUrl.getName(), websiteUrl);
        }
        ClaimWrapper email = getEmail();
        if (email != null) {
            result.put(email.getName(), email);
        }
        ClaimWrapper emailVerified = getEmailVerified();
        if (emailVerified != null) {
            result.put(emailVerified.getName(), emailVerified);
        }
        ClaimWrapper gender = getGender();
        if (gender != null) {
            result.put(gender.getName(), gender);
        }
        ClaimWrapper birthdate = getBirthdate();
        if (birthdate != null) {
            result.put(birthdate.getName(), birthdate);
        }
        ClaimWrapper timezone = getTimezone();
        if (timezone != null) {
            result.put(timezone.getName(), timezone);
        }
        ClaimWrapper locale = getLocale();
        if (locale != null) {
            result.put(locale.getName(), locale);
        }
        ClaimWrapper address = getAddress();
        if (address != null) {
            result.put(address.getName(), address);
        }
        ClaimWrapper phoneNumber = getPhoneNumber();
        if (phoneNumber != null) {
            result.put(phoneNumber.getName(), phoneNumber);
        }
        ClaimWrapper phoneNumberVerified = getPhoneNumberVerified();
        if (phoneNumberVerified != null) {
            result.put(phoneNumberVerified.getName(), phoneNumberVerified);
        }
        ClaimWrapper placeOfBirth = getPlaceOfBirth();
        if (placeOfBirth != null) {
            result.put(placeOfBirth.getName(), placeOfBirth);
        }
        ClaimWrapper nationalities = getNationalities();
        if (nationalities != null) {
            result.put(nationalities.getName(), nationalities);
        }
        ClaimWrapper birthLastName = getBirthLastName();
        if (birthLastName != null) {
            result.put(birthLastName.getName(), birthLastName);
        }
        ClaimWrapper birthFirstName = getBirthFirstName();
        if (birthFirstName != null) {
            result.put(birthFirstName.getName(), birthFirstName);
        }
        ClaimWrapper birthMiddleName = getBirthMiddleName();
        if (birthMiddleName != null) {
            result.put(birthMiddleName.getName(), birthMiddleName);
        }
        ClaimWrapper salutation = getSalutation();
        if (salutation != null) {
            result.put(salutation.getName(), salutation);
        }
        ClaimWrapper title = getTitle();
        if (title != null) {
            result.put(title.getName(), title);
        }
        ClaimWrapper mobilePhoneNumber = getMobilePhoneNumber();
        if (mobilePhoneNumber != null) {
            result.put(mobilePhoneNumber.getName(), mobilePhoneNumber);
        }
        ClaimWrapper pseudonym = getPseudonym();
        if (pseudonym != null) {
            result.put(pseudonym.getName(), pseudonym);
        }
        List<ClaimWrapper> otherClaims = getOtherClaims();
        if (otherClaims != null && !otherClaims.isEmpty()) {
            for (ClaimWrapper otherClaim : otherClaims) {
                if (otherClaim != null) {
                    result.put(otherClaim.getName(), otherClaim);
                }
            }
        }
        return result;
    }

    @Override
    public XmlCredentialSubjectClaim getWrapped() {
        return (XmlCredentialSubjectClaim) super.getWrapped();
    }

}
