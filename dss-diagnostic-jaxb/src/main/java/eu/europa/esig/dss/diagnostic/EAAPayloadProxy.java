package eu.europa.esig.dss.diagnostic;

import eu.europa.esig.dss.diagnostic.claim.AddressClaimWrapper;
import eu.europa.esig.dss.diagnostic.claim.ClaimWrapper;
import eu.europa.esig.dss.diagnostic.claim.IntegrityClaimWrapper;
import eu.europa.esig.dss.diagnostic.claim.PlaceOfBirthClaimWrapper;
import eu.europa.esig.dss.diagnostic.claim.StatusClaimWrapper;
import eu.europa.esig.dss.diagnostic.jaxb.XmlAddressClaim;
import eu.europa.esig.dss.diagnostic.jaxb.XmlClaim;
import eu.europa.esig.dss.diagnostic.jaxb.XmlEAAPayload;
import eu.europa.esig.dss.diagnostic.jaxb.XmlIntegrityClaim;
import eu.europa.esig.dss.diagnostic.jaxb.XmlPlaceOfBirthClaim;
import eu.europa.esig.dss.diagnostic.jaxb.XmlStatusClaim;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * This class provides an interface for selectively disposable claims extraction
 * 
 */
public class EAAPayloadProxy {
    
    /** Wrapped EAA Payload to get access to */
    private final XmlEAAPayload xmlEAAPayload;

    /**
     * Default constructor
     * 
     * @param xmlEAAPayload {@link XmlEAAPayload}
     */
    public EAAPayloadProxy(final XmlEAAPayload xmlEAAPayload) {
        this.xmlEAAPayload = xmlEAAPayload;
    }
    
    /**
     * Gets EAA Presentation identifier provided in the EAA payload
     *
     * @return {@link ClaimWrapper}
     */
    public ClaimWrapper getEAAIdentifier() {
        if (xmlEAAPayload != null) {
            return getClaim(xmlEAAPayload.getIdentifier());
        }
        return null;
    }

    /**
     * Gets EAA Presentation issuer as defined in the EAA payload
     *
     * @return {@link ClaimWrapper}
     */
    public ClaimWrapper getEAAIssuer() {
        if (xmlEAAPayload != null) {
            return getClaim(xmlEAAPayload.getIssuer());
        }
        return null;
    }

    /**
     * Gets EAA Presentation subject as defined in the EAA payload
     *
     * @return {@link ClaimWrapper}
     */
    public ClaimWrapper getEAASubject() {
        if (xmlEAAPayload != null) {
            return getClaim(xmlEAAPayload.getSubject());
        }
        return null;
    }

    /**
     * Gets EAA Presentation audience as defined in the EAA payload
     *
     * @return {@link ClaimWrapper}
     */
    public ClaimWrapper getEAAAudience() {
        if (xmlEAAPayload != null) {
            return getClaim(xmlEAAPayload.getAudience());
        }
        return null;
    }

    /**
     * Gets EAA Presentation expiration time as defined in the EAA payload
     *
     * @return {@link ClaimWrapper}
     */
    public ClaimWrapper getEAAExpirationTime() {
        if (xmlEAAPayload != null) {
            return getClaim(xmlEAAPayload.getExpirationTime());
        }
        return null;
    }

    /**
     * Gets EAA Presentation not before time as defined in the EAA payload
     *
     * @return {@link ClaimWrapper}
     */
    public ClaimWrapper getEAANotBefore() {
        if (xmlEAAPayload != null) {
            return getClaim(xmlEAAPayload.getNotBefore());
        }
        return null;
    }

    /**
     * Gets EAA Presentation issuance time as defined in the EAA payload
     *
     * @return {@link ClaimWrapper}
     */
    public ClaimWrapper getEAAIssuedAt() {
        if (xmlEAAPayload != null) {
            return getClaim(xmlEAAPayload.getIssuedAt());
        }
        return null;
    }

    /**
     * Gets EAA Presentation update time as defined in the EAA payload
     *
     * @return {@link ClaimWrapper}
     */
    public ClaimWrapper getEAAUpdatedAt() {
        if (xmlEAAPayload != null) {
            return getClaim(xmlEAAPayload.getUpdatedAt());
        }
        return null;
    }

    /**
     * Gets category URN provided in the EAA payload
     *
     * @return {@link ClaimWrapper}
     */
    public ClaimWrapper getEAACategory() {
        if (xmlEAAPayload != null) {
            return getClaim(xmlEAAPayload.getCategory());
        }
        return null;
    }

    /**
     * Gets EAA Presentation metadata type (e.g. 'vct' claim) as defined in the EAA payload
     *
     * @return {@link ClaimWrapper}
     */
    public ClaimWrapper getEAAMetadataType() {
        if (xmlEAAPayload != null && xmlEAAPayload.getMetadataType() != null) {
            return getClaim(xmlEAAPayload.getMetadataType());
        }
        return null;
    }

    /**
     * Gets the integrity material for the EAA Presentation metadata (when present)
     *
     * @return {@link ClaimWrapper}
     */
    public IntegrityClaimWrapper getEAAMetadataIntegrity() {
        if (xmlEAAPayload != null && xmlEAAPayload.getMetadataType() != null) {
            return getIntegrityClaim(xmlEAAPayload.getMetadataType().getIntegrity());
        }
        return null;
    }

    /**
     * Gets EAA Presentation status as defined in the EAA payload
     *
     * @return {@link StatusClaimWrapper}
     */
    public StatusClaimWrapper getEAAStatus() {
        if (xmlEAAPayload != null) {
            return getStatusClaim(xmlEAAPayload.getStatus());
        }
        return null;
    }

    /**
     * Gets EAA Presentation nonce when defined in the EAA payload
     *
     * @return {@link ClaimWrapper}
     */
    public ClaimWrapper getEAANonce() {
        if (xmlEAAPayload != null) {
            return getClaim(xmlEAAPayload.getNonce());
        }
        return null;
    }

    /**
     * Gets user's full name when defined within EAA Payload claims
     *
     * @return {@link ClaimWrapper}
     */
    public ClaimWrapper getUserFullName() {
        if (xmlEAAPayload != null) {
            return getClaim(xmlEAAPayload.getFullName());
        }
        return null;
    }

    /**
     * Gets user's first name when defined within EAA Payload claims
     *
     * @return {@link ClaimWrapper}
     */
    public ClaimWrapper getUserFirstName() {
        if (xmlEAAPayload != null) {
            return getClaim(xmlEAAPayload.getFirstName());
        }
        return null;
    }

    /**
     * Gets user's last or family name when defined within EAA Payload claims
     *
     * @return {@link ClaimWrapper}
     */
    public ClaimWrapper getUserLastName() {
        if (xmlEAAPayload != null) {
            return getClaim(xmlEAAPayload.getLastName());
        }
        return null;
    }

    /**
     * Gets user's middle name when defined within EAA Payload claims
     *
     * @return {@link ClaimWrapper}
     */
    public ClaimWrapper getUserMiddleName() {
        if (xmlEAAPayload != null) {
            return getClaim(xmlEAAPayload.getMiddleName());
        }
        return null;
    }

    /**
     * Gets user's alternative name when defined within EAA Payload claims
     *
     * @return {@link ClaimWrapper}
     */
    public ClaimWrapper getUserNickname() {
        if (xmlEAAPayload != null) {
            return getClaim(xmlEAAPayload.getNickname());
        }
        return null;
    }

    /**
     * Gets user's preferred or short name when defined within EAA Payload claims
     *
     * @return {@link ClaimWrapper}
     */
    public ClaimWrapper getUserShortName() {
        if (xmlEAAPayload != null) {
            return getClaim(xmlEAAPayload.getShortName());
        }
        return null;
    }

    /**
     * Gets user's profile URL when defined within EAA Payload claims
     *
     * @return {@link ClaimWrapper}
     */
    public ClaimWrapper getUserProfileUrl() {
        if (xmlEAAPayload != null) {
            return getClaim(xmlEAAPayload.getProfileUrl());
        }
        return null;
    }

    /**
     * Gets user's picture URL when defined within EAA Payload claims
     *
     * @return {@link ClaimWrapper}
     */
    public ClaimWrapper getUserPictureUrl() {
        if (xmlEAAPayload != null) {
            return getClaim(xmlEAAPayload.getPictureUrl());
        }
        return null;
    }

    /**
     * Gets user's website when defined within EAA Payload claims
     *
     * @return {@link ClaimWrapper}
     */
    public ClaimWrapper getUserWebsiteUrl() {
        if (xmlEAAPayload != null) {
            return getClaim(xmlEAAPayload.getWebsiteUrl());
        }
        return null;
    }

    /**
     * Gets user's email when defined within EAA Payload claims
     *
     * @return {@link ClaimWrapper}
     */
    public ClaimWrapper getUserEmail() {
        if (xmlEAAPayload != null) {
            return getClaim(xmlEAAPayload.getEmail());
        }
        return null;
    }

    /**
     * Gets whether the user's website has been verified if defined within EAA Payload claims
     *
     * @return {@link ClaimWrapper}
     */
    public ClaimWrapper getUserEmailVerified() {
        if (xmlEAAPayload != null) {
            return getClaim(xmlEAAPayload.getEmailVerified());
        }
        return null;
    }

    /**
     * Gets user's gender when defined within EAA Payload claims
     *
     * @return {@link ClaimWrapper}
     */
    public ClaimWrapper getUserGender() {
        if (xmlEAAPayload != null) {
            return getClaim(xmlEAAPayload.getGender());
        }
        return null;
    }

    /**
     * Gets user's birthdate when defined within EAA Payload claims
     *
     * @return {@link ClaimWrapper}
     */
    public ClaimWrapper getUserBirthdate() {
        if (xmlEAAPayload != null) {
            return getClaim(xmlEAAPayload.getBirthdate());
        }
        return null;
    }

    /**
     * Gets user's timezone when defined within EAA Payload claims
     *
     * @return {@link ClaimWrapper}
     */
    public ClaimWrapper getUserTimezone() {
        if (xmlEAAPayload != null) {
            return getClaim(xmlEAAPayload.getTimezone());
        }
        return null;
    }

    /**
     * Gets user's locale when defined within EAA Payload claims
     *
     * @return {@link ClaimWrapper}
     */
    public ClaimWrapper getUserLocale() {
        if (xmlEAAPayload != null) {
            return getClaim(xmlEAAPayload.getLocale());
        }
        return null;
    }

    /**
     * Gets user's full address, when defined within EAA Payload claims
     *
     * @return {@link AddressClaimWrapper}
     */
    public AddressClaimWrapper getUserAddress() {
        if (xmlEAAPayload != null) {
            return getAddressClaim(xmlEAAPayload.getAddress());
        }
        return null;
    }

    /**
     * Gets user's city address when defined within EAA Payload claims
     *
     * @return {@link ClaimWrapper}
     */
    public ClaimWrapper getUserAddressCity() {
        if (xmlEAAPayload != null && xmlEAAPayload.getAddress() != null) {
            return getClaim(xmlEAAPayload.getAddress().getCity());
        }
        return null;
    }

    /**
     * Gets user's state or region address when defined within EAA Payload claims
     *
     * @return {@link ClaimWrapper}
     */
    public ClaimWrapper getUserAddressStateOrProvince() {
        if (xmlEAAPayload != null && xmlEAAPayload.getAddress() != null) {
            return getClaim(xmlEAAPayload.getAddress().getStateOrProvince());
        }
        return null;
    }

    /**
     * Gets user's postal code address when defined within EAA Payload claims
     *
     * @return {@link ClaimWrapper}
     */
    public ClaimWrapper getUserAddressPostalCode() {
        if (xmlEAAPayload != null && xmlEAAPayload.getAddress() != null) {
            return getClaim(xmlEAAPayload.getAddress().getPostalCode());
        }
        return null;
    }

    /**
     * Gets user's country address when defined within EAA Payload claims.
     * NOTE: The returned value is usually represented by 2-letter ISO country code.
     *
     * @return {@link ClaimWrapper}
     */
    public ClaimWrapper getUserAddressCountry() {
        if (xmlEAAPayload != null && xmlEAAPayload.getAddress() != null) {
            return getClaim(xmlEAAPayload.getAddress().getCountryName());
        }
        return null;
    }

    /**
     * Gets user's street address when defined within EAA Payload claims
     *
     * @return {@link ClaimWrapper}
     */
    public ClaimWrapper getUserStreetAddress() {
        if (xmlEAAPayload != null && xmlEAAPayload.getAddress() != null) {
            return getClaim(xmlEAAPayload.getAddress().getStreetAddress());
        }
        return null;
    }

    /**
     * Gets user's phone number when defined within EAA Payload claims
     *
     * @return {@link ClaimWrapper}
     */
    public ClaimWrapper getUserPhoneNumber() {
        if (xmlEAAPayload != null) {
            return getClaim(xmlEAAPayload.getPhoneNumber());
        }
        return null;
    }

    /**
     * Gets whether the user's phone number has been verified if defined within EAA Payload claims
     *
     * @return {@link ClaimWrapper}
     */
    public ClaimWrapper getUserPhoneNumberVerified() {
        if (xmlEAAPayload != null) {
            return getClaim(xmlEAAPayload.getPhoneNumberVerified());
        }
        return null;
    }

    /**
     * Gets user's place of birth when defined within EAA Payload claims
     *
     * @return {@link PlaceOfBirthClaimWrapper}
     */
    public PlaceOfBirthClaimWrapper getUserPlaceOfBirth() {
        if (xmlEAAPayload != null) {
            return getPlaceOfBirthClaim(xmlEAAPayload.getPlaceOfBirth());
        }
        return null;
    }

    /**
     * Gets user's nationalities list when defined within EAA Payload claims.
     * NOTE: The values are usually represented by 3-letter nationality codes.
     *
     * @return {@link ClaimWrapper}
     */
    public ClaimWrapper getUserNationalities() {
        if (xmlEAAPayload != null) {
            return getClaim(xmlEAAPayload.getNationalities());
        }
        return null;
    }

    /**
     * Gets user's last or family name at birth when defined within EAA Payload claims
     *
     * @return {@link ClaimWrapper}
     */
    public ClaimWrapper getUserBirthLastName() {
        if (xmlEAAPayload != null) {
            return getClaim(xmlEAAPayload.getBirthLastName());
        }
        return null;
    }

    /**
     * Gets user's first name at birth when defined within EAA Payload claims
     *
     * @return {@link ClaimWrapper}
     */
    public ClaimWrapper getUserBirthFirstName() {
        if (xmlEAAPayload != null) {
            return getClaim(xmlEAAPayload.getBirthFirstName());
        }
        return null;
    }

    /**
     * Gets user's middle name at birth when defined within EAA Payload claims
     *
     * @return {@link ClaimWrapper}
     */
    public ClaimWrapper getUserBirthMiddleName() {
        if (xmlEAAPayload != null) {
            return getClaim(xmlEAAPayload.getBirthMiddleName());
        }
        return null;
    }

    /**
     * Gets user's preferred salutation when defined within EAA Payload claims
     *
     * @return {@link ClaimWrapper}
     */
    public ClaimWrapper getUserSalutation() {
        if (xmlEAAPayload != null) {
            return getClaim(xmlEAAPayload.getSalutation());
        }
        return null;
    }

    /**
     * Gets user's title when defined within EAA Payload claims
     *
     * @return {@link ClaimWrapper}
     */
    public ClaimWrapper getUserTitle() {
        if (xmlEAAPayload != null) {
            return getClaim(xmlEAAPayload.getTitle());
        }
        return null;
    }

    /**
     * Gets user's mobile phone number when defined within EAA Payload claims
     *
     * @return {@link ClaimWrapper}
     */
    public ClaimWrapper getUserMobilePhoneNumber() {
        if (xmlEAAPayload != null) {
            return getClaim(xmlEAAPayload.getMobilePhoneNumber());
        }
        return null;
    }

    /**
     * Gets user's scenic name or pseudonym, they are known as, when defined within EAA Payload claims
     *
     * @return {@link ClaimWrapper}
     */
    public ClaimWrapper getUserPseudonym() {
        if (xmlEAAPayload != null) {
            return getClaim(xmlEAAPayload.getPseudonym());
        }
        return null;
    }

    /**
     * Gets a list of claims incorporated within the EAA Payload or provided as disclosures,
     * which are not (yet) directly supported by the implementation.
     *
     * @return a lust of {@link ClaimWrapper}s
     */
    public List<ClaimWrapper> getOtherClaims() {
        if (xmlEAAPayload != null && xmlEAAPayload.getOtherClaim() != null) {
            return xmlEAAPayload.getOtherClaim().stream().map(ClaimWrapper::new).collect(Collectors.toList());
        }
        return Collections.emptyList();
    }

    /**
     * Gets a list of all disclosable claims present within an EAA Payload
     *
     * @return a list of {@link ClaimWrapper}s
     */
    public List<ClaimWrapper> getAllEAAPayloadClaims() {
        if (xmlEAAPayload == null) {
            return Collections.emptyList();
        }

        final List<ClaimWrapper> claimList = new ArrayList<>();

        if (xmlEAAPayload.getIdentifier() != null) {
            claimList.add(getClaim(xmlEAAPayload.getIdentifier()));
        }
        if (xmlEAAPayload.getIssuer() != null) {
            claimList.add(getClaim(xmlEAAPayload.getIssuer()));
        }
        if (xmlEAAPayload.getSubject() != null) {
            claimList.add(getClaim(xmlEAAPayload.getSubject()));
        }
        if (xmlEAAPayload.getAudience() != null) {
            claimList.add(getClaim(xmlEAAPayload.getAudience()));
        }
        if (xmlEAAPayload.getExpirationTime() != null) {
            claimList.add(getClaim(xmlEAAPayload.getExpirationTime()));
        }
        if (xmlEAAPayload.getNotBefore() != null) {
            claimList.add(getClaim(xmlEAAPayload.getNotBefore()));
        }
        if (xmlEAAPayload.getIssuedAt() != null) {
            claimList.add(getClaim(xmlEAAPayload.getIssuedAt()));
        }
        if (xmlEAAPayload.getUpdatedAt() != null) {
            claimList.add(getClaim(xmlEAAPayload.getUpdatedAt()));
        }
        if (xmlEAAPayload.getCategory() != null) {
            claimList.add(getClaim(xmlEAAPayload.getCategory()));
        }
        if (xmlEAAPayload.getMetadataType() != null) {
            claimList.add(getClaim(xmlEAAPayload.getMetadataType()));
            if (xmlEAAPayload.getMetadataType().getIntegrity() != null) {
                claimList.add(getIntegrityClaim(xmlEAAPayload.getMetadataType().getIntegrity()));
            }
        }
        if (xmlEAAPayload.getStatus() != null) {
            claimList.add(getStatusClaim(xmlEAAPayload.getStatus()));
        }
        if (xmlEAAPayload.getNonce() != null) {
            claimList.add(getClaim(xmlEAAPayload.getNonce()));
        }
        if (xmlEAAPayload.getFullName() != null) {
            claimList.add(getClaim(xmlEAAPayload.getFullName()));
        }
        if (xmlEAAPayload.getFirstName() != null) {
            claimList.add(getClaim(xmlEAAPayload.getFirstName()));
        }
        if (xmlEAAPayload.getLastName() != null) {
            claimList.add(getClaim(xmlEAAPayload.getLastName()));
        }
        if (xmlEAAPayload.getMiddleName() != null) {
            claimList.add(getClaim(xmlEAAPayload.getMiddleName()));
        }
        if (xmlEAAPayload.getNickname() != null) {
            claimList.add(getClaim(xmlEAAPayload.getNickname()));
        }
        if (xmlEAAPayload.getShortName() != null) {
            claimList.add(getClaim(xmlEAAPayload.getShortName()));
        }
        if (xmlEAAPayload.getProfileUrl() != null) {
            claimList.add(getClaim(xmlEAAPayload.getProfileUrl()));
        }
        if (xmlEAAPayload.getPictureUrl() != null) {
            claimList.add(getClaim(xmlEAAPayload.getPictureUrl()));
        }
        if (xmlEAAPayload.getWebsiteUrl() != null) {
            claimList.add(getClaim(xmlEAAPayload.getWebsiteUrl()));
        }
        if (xmlEAAPayload.getEmail() != null) {
            claimList.add(getClaim(xmlEAAPayload.getEmail()));
        }
        if (xmlEAAPayload.getEmailVerified() != null) {
            claimList.add(getClaim(xmlEAAPayload.getEmailVerified()));
        }
        if (xmlEAAPayload.getGender() != null) {
            claimList.add(getClaim(xmlEAAPayload.getGender()));
        }
        if (xmlEAAPayload.getBirthdate() != null) {
            claimList.add(getClaim(xmlEAAPayload.getBirthdate()));
        }
        if (xmlEAAPayload.getTimezone() != null) {
            claimList.add(getClaim(xmlEAAPayload.getTimezone()));
        }
        if (xmlEAAPayload.getLocale() != null) {
            claimList.add(getClaim(xmlEAAPayload.getLocale()));
        }
        if (xmlEAAPayload.getAddress() != null) {
            claimList.add(getAddressClaim(xmlEAAPayload.getAddress()));
        }
        if (xmlEAAPayload.getPhoneNumber() != null) {
            claimList.add(getClaim(xmlEAAPayload.getPhoneNumber()));
        }
        if (xmlEAAPayload.getPhoneNumberVerified() != null) {
            claimList.add(getClaim(xmlEAAPayload.getPhoneNumberVerified()));
        }
        if (xmlEAAPayload.getPlaceOfBirth() != null) {
            claimList.add(getPlaceOfBirthClaim(xmlEAAPayload.getPlaceOfBirth()));
        }
        if (xmlEAAPayload.getNationalities() != null) {
            claimList.add(getClaim(xmlEAAPayload.getNationalities()));
        }
        if (xmlEAAPayload.getBirthLastName() != null) {
            claimList.add(getClaim(xmlEAAPayload.getBirthLastName()));
        }
        if (xmlEAAPayload.getBirthFirstName() != null) {
            claimList.add(getClaim(xmlEAAPayload.getBirthFirstName()));
        }
        if (xmlEAAPayload.getBirthMiddleName() != null) {
            claimList.add(getClaim(xmlEAAPayload.getBirthMiddleName()));
        }
        if (xmlEAAPayload.getSalutation() != null) {
            claimList.add(getClaim(xmlEAAPayload.getSalutation()));
        }
        if (xmlEAAPayload.getTitle() != null) {
            claimList.add(getClaim(xmlEAAPayload.getTitle()));
        }
        if (xmlEAAPayload.getMobilePhoneNumber() != null) {
            claimList.add(getClaim(xmlEAAPayload.getMobilePhoneNumber()));
        }
        if (xmlEAAPayload.getPseudonym() != null) {
            claimList.add(getClaim(xmlEAAPayload.getPseudonym()));
        }
        if (xmlEAAPayload.getOtherClaim() != null && !xmlEAAPayload.getOtherClaim().isEmpty()) {
            List<ClaimWrapper> claimWrappers = xmlEAAPayload.getOtherClaim().stream()
                    .map(this::getClaim).collect(Collectors.toList());
            claimList.addAll(claimWrappers);
        }

        return claimList;
    }

    private ClaimWrapper getClaim(XmlClaim xmlDisclosableClaim) {
        if (xmlDisclosableClaim == null) {
            return null;
        }
        return new ClaimWrapper(xmlDisclosableClaim);
    }

    private IntegrityClaimWrapper getIntegrityClaim(XmlIntegrityClaim xmlIntegrityClaim) {
        if (xmlIntegrityClaim == null) {
            return null;
        }
        return new IntegrityClaimWrapper(xmlIntegrityClaim);
    }

    private AddressClaimWrapper getAddressClaim(XmlAddressClaim xmlAddressClaim) {
        if (xmlAddressClaim == null) {
            return null;
        }
        return new AddressClaimWrapper(xmlAddressClaim);
    }

    private PlaceOfBirthClaimWrapper getPlaceOfBirthClaim(XmlPlaceOfBirthClaim xmlPlaceOfBirthClaim) {
        if (xmlPlaceOfBirthClaim == null) {
            return null;
        }
        return new PlaceOfBirthClaimWrapper(xmlPlaceOfBirthClaim);
    }

    private StatusClaimWrapper getStatusClaim(XmlStatusClaim xmlStatusClaim) {
        if (xmlStatusClaim == null) {
            return null;
        }
        return new StatusClaimWrapper(xmlStatusClaim);
    }
    
}
