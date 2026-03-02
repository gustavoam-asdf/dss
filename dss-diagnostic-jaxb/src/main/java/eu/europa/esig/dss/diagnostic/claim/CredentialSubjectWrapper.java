package eu.europa.esig.dss.diagnostic.claim;

import eu.europa.esig.dss.diagnostic.jaxb.XmlAddressClaim;
import eu.europa.esig.dss.diagnostic.jaxb.XmlClaim;
import eu.europa.esig.dss.diagnostic.jaxb.XmlCredentialSubject;
import eu.europa.esig.dss.diagnostic.jaxb.XmlPlaceOfBirthClaim;

import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Wraps an {@code eu.europa.esig.dss.diagnostic.jaxb.XmlCredentialSubject}
 *
 */
public class CredentialSubjectWrapper extends ClaimWrapper {

    /**
     * Default constructor
     *
     * @param wrapped {@link XmlCredentialSubject}
     */
    public CredentialSubjectWrapper(final XmlCredentialSubject wrapped) {
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
        XmlPlaceOfBirthClaim placeOfBirth = getWrapped().getPlaceOfBirth();
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
    public XmlCredentialSubject getWrapped() {
        return (XmlCredentialSubject) super.getWrapped();
    }

}
