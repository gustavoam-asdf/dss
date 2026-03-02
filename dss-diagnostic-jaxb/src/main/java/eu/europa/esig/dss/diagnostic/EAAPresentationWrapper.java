package eu.europa.esig.dss.diagnostic;

import eu.europa.esig.dss.diagnostic.claim.AddressClaimWrapper;
import eu.europa.esig.dss.diagnostic.claim.ClaimWrapper;
import eu.europa.esig.dss.diagnostic.claim.IntegrityClaimWrapper;
import eu.europa.esig.dss.diagnostic.claim.PlaceOfBirthClaimWrapper;
import eu.europa.esig.dss.diagnostic.claim.StatusClaimWrapper;
import eu.europa.esig.dss.diagnostic.jaxb.XmlDigestMatcher;
import eu.europa.esig.dss.diagnostic.jaxb.XmlEAAPresentation;
import eu.europa.esig.dss.diagnostic.jaxb.XmlEAAPresentationSignature;
import eu.europa.esig.dss.enumerations.DigestAlgorithm;
import eu.europa.esig.dss.enumerations.EAAPresentationType;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Provides a user-friendly interface for information extraction from a {@code eu.europa.esig.dss.diagnostic.jaxb.XmlEAAPresentation} JAXB object
 *
 */
public class EAAPresentationWrapper {

    /** Wrapped EAA Presentation object */
    private final XmlEAAPresentation eaaPresentation;

    /** Cached list of all claims present within the EAA Payload or provided as disclosures */
    private List<ClaimWrapper> claimList;

    /**
     * Default constructor
     *
     * @param eaaPresentation {@link XmlEAAPresentation} to read
     */
    public EAAPresentationWrapper(final XmlEAAPresentation eaaPresentation) {
        this.eaaPresentation = eaaPresentation;
    }

    /**
     * Gets unique identifier
     *
     * @return {@link String}
     */
    public String getId() {
        return eaaPresentation.getId();
    }

    /**
     * Returns name of the EAA presentation's document, when applicable
     *
     * @return {@link String}
     */
    public String getFilename() {
        return eaaPresentation.getDocumentName();
    }

    /**
     * Gets a list of digest matchers representing the associated hashes and disclosures validation
     *
     * @return a list of {@link XmlDigestMatcher}
     */
    public List<XmlDigestMatcher> getDigestMatchers() {
        return eaaPresentation.getDigestMatchers();
    }

    /**
     * Gets signatures used to create the EAA presentation.
     * NOTE: in most of the cases a single signature is expected,
     * but it is possible for EAA presentation to be signed by multiple signers.
     *
     * @return a list of {@link SignatureWrapper}s
     */
    public List<SignatureWrapper> getEAAPresentationSignatures() {
        final List<SignatureWrapper> result = new ArrayList<>();
        for (XmlEAAPresentationSignature xmlEAAPresentationSignature : eaaPresentation.getEAAPresentationSignature()) {
            result.add(new SignatureWrapper(xmlEAAPresentationSignature.getSignature()));
        }
        return result;
    }

    /**
     * Gets a list of identifiers of signatures used to create the EAA
     *
     * @return a list of {@link String}s
     */
    public List<String> getEAAPresentationSignatureIds() {
        List<SignatureWrapper> eaaPresentationSignatures = getEAAPresentationSignatures();
        if (eaaPresentationSignatures != null && !eaaPresentationSignatures.isEmpty()) {
            return eaaPresentationSignatures.stream().map(SignatureWrapper::getId).collect(Collectors.toList());
        }
        return Collections.emptyList();
    }

    /**
     * Gets a key binding signature, when present
     *
     * @return {@link SignatureWrapper}
     */
    public SignatureWrapper getKeyBindingSignature() {
        if (eaaPresentation.getKeyBindingSignature() != null) {
            return new SignatureWrapper(eaaPresentation.getKeyBindingSignature().getSignature());
        }
        return null;
    }

    /**
     * Gets unique identifier of the key binding signature, when present
     *
     * @return {@link String}
     */
    public String getKeyBindingSignatureId() {
        SignatureWrapper keyBindingSignature = getKeyBindingSignature();
        if (keyBindingSignature != null) {
            return keyBindingSignature.getId();
        }
        return null;
    }

    /**
     * Gets access to the EAA payload, containing complete claims data
     * 
     * @return {@link EAAPayloadProxy}
     */
    public EAAPayloadProxy getEAAPayload() {
        return new EAAPayloadProxy(eaaPresentation.getEAAPayload());
    }

    /**
     * Gets EAA Presentation identifier provided in the EAA payload
     *
     * @return {@link String}
     */
    public String getEAAIdentifier() {
        return getPayloadClaimTextValue(getEAAPayload().getEAAIdentifier());
    }

    /**
     * Gets EAA Presentation issuer as defined in the EAA payload
     *
     * @return {@link String}
     */
    public String getEAAIssuer() {
        return getPayloadClaimTextValue(getEAAPayload().getEAAIssuer());
    }

    /**
     * Gets EAA Presentation subject as defined in the EAA payload
     *
     * @return {@link String}
     */
    public String getEAASubject() {
        return getPayloadClaimTextValue(getEAAPayload().getEAASubject());
    }

    /**
     * Gets EAA Presentation audience as defined in the EAA payload
     *
     * @return {@link String}
     */
    public String getEAAAudience() {
        return getPayloadClaimTextValue(getEAAPayload().getEAAAudience());
    }

    /**
     * Gets EAA Presentation expiration time as defined in the EAA payload
     *
     * @return {@link Date}
     */
    public Date getEAAExpirationTime() {
        return getPayloadClaimDateValue(getEAAPayload().getEAAExpirationTime());
    }

    /**
     * Gets EAA Presentation not before time as defined in the EAA payload
     *
     * @return {@link Date}
     */
    public Date getEAANotBefore() {
        return getPayloadClaimDateValue(getEAAPayload().getEAANotBefore());
    }

    /**
     * Gets EAA Presentation issuance time as defined in the EAA payload
     *
     * @return {@link Date}
     */
    public Date getEAAIssuedAt() {
        return getPayloadClaimDateValue(getEAAPayload().getEAAIssuedAt());
    }

    /**
     * Gets EAA Presentation update time as defined in the EAA payload
     *
     * @return {@link Date}
     */
    public Date getEAAUpdatedAt() {
        return getPayloadClaimDateValue(getEAAPayload().getEAAUpdatedAt());
    }

    /**
     * Gets category URN provided in the EAA payload
     *
     * @return {@link String}
     */
    public String getEAACategory() {
        return getPayloadClaimTextValue(getEAAPayload().getEAACategory());
    }

    /**
     * Gets EAA Presentation metadata URI (e.g. 'vct' claim) as defined in the EAA payload
     *
     * @return {@link String}
     */
    public String getEAAMetadataUri() {
        return getPayloadClaimTextValue(getEAAPayload().getEAAMetadataType());
    }

    /**
     * Gets Digest Algorithm used to compute the integrity material for the EAA Presentation metadata (when present)
     *
     * @return {@link DigestAlgorithm}
     */
    public DigestAlgorithm getEAAMetadataIntegrityDigestAlgorithm() {
        IntegrityClaimWrapper eaaMetadataIntegrity = getEAAPayload().getEAAMetadataIntegrity();
        if (eaaMetadataIntegrity != null) {
            return eaaMetadataIntegrity.getDigestAlgorithm();
        }
        return null;
    }

    /**
     * Gets the integrity material for the EAA Presentation metadata (when present)
     *
     * @return byte array representing the EAA Presentation's metadata hash
     */
    public byte[] getEAAMetadataIntegrityBytes() {
        IntegrityClaimWrapper eaaMetadataIntegrity = getEAAPayload().getEAAMetadataIntegrity();
        if (eaaMetadataIntegrity != null) {
            return eaaMetadataIntegrity.getDigestValue();
        }
        return null;
    }

    /**
     * Gets EAA Presentation status index as defined in the EAA payload
     *
     * @return {@link BigInteger}
     */
    public BigInteger getEAAStatusIndex() {
        StatusClaimWrapper eaaStatus = getEAAPayload().getEAAStatus();
        if (eaaStatus != null) {
            return getPayloadClaimNumberValue(eaaStatus.getIndex());
        }
        return null;
    }

    /**
     * Gets EAA Presentation status URI as defined in the EAA payload
     *
     * @return {@link String}
     */
    public String getEAAStatusUri() {
        StatusClaimWrapper eaaStatus = getEAAPayload().getEAAStatus();
        if (eaaStatus != null) {
            return getPayloadClaimTextValue(eaaStatus.getUri());
        }
        return null;
    }

    /**
     * Gets EAA Presentation nonce when defined in the EAA payload
     *
     * @return {@link String}
     */
    public String getEAANonce() {
        return getPayloadClaimTextValue(getEAAPayload().getEAANonce());
    }

    /**
     * Gets user's full name when defined within EAA Payload claims
     *
     * @return {@link String}
     */
    public String getUserFullName() {
        return getPayloadClaimTextValue(getEAAPayload().getUserFullName());
    }

    /**
     * Gets user's first name when defined within EAA Payload claims
     *
     * @return {@link String}
     */
    public String getUserFirstName() {
        return getPayloadClaimTextValue(getEAAPayload().getUserFirstName());
    }

    /**
     * Gets user's last or family name when defined within EAA Payload claims
     *
     * @return {@link String}
     */
    public String getUserLastName() {
        return getPayloadClaimTextValue(getEAAPayload().getUserLastName());
    }

    /**
     * Gets user's middle name when defined within EAA Payload claims
     *
     * @return {@link String}
     */
    public String getUserMiddleName() {
        return getPayloadClaimTextValue(getEAAPayload().getUserMiddleName());
    }

    /**
     * Gets user's alternative name when defined within EAA Payload claims
     *
     * @return {@link String}
     */
    public String getUserNickname() {
        return getPayloadClaimTextValue(getEAAPayload().getUserNickname());
    }

    /**
     * Gets user's preferred or short name when defined within EAA Payload claims
     *
     * @return {@link String}
     */
    public String getUserShortName() {
        return getPayloadClaimTextValue(getEAAPayload().getUserShortName());
    }

    /**
     * Gets user's profile URL when defined within EAA Payload claims
     *
     * @return {@link String}
     */
    public String getUserProfileUrl() {
        return getPayloadClaimTextValue(getEAAPayload().getUserProfileUrl());
    }

    /**
     * Gets user's picture URL when defined within EAA Payload claims
     *
     * @return {@link String}
     */
    public String getUserPictureUrl() {
        return getPayloadClaimTextValue(getEAAPayload().getUserPictureUrl());
    }

    /**
     * Gets user's website when defined within EAA Payload claims
     *
     * @return {@link String}
     */
    public String getUserWebsiteUrl() {
        return getPayloadClaimTextValue(getEAAPayload().getUserWebsiteUrl());
    }

    /**
     * Gets user's email when defined within EAA Payload claims
     *
     * @return {@link String}
     */
    public String getUserEmail() {
        return getPayloadClaimTextValue(getEAAPayload().getUserEmail());
    }

    /**
     * Gets whether the user's website has been verified if defined within EAA Payload claims
     *
     * @return {@link Boolean}
     */
    public Boolean getUserEmailVerified() {
        return getPayloadClaimBooleanValue(getEAAPayload().getUserEmailVerified());
    }

    /**
     * Gets user's gender when defined within EAA Payload claims
     *
     * @return {@link String}
     */
    public String getUserGender() {
        return getPayloadClaimTextValue(getEAAPayload().getUserGender());
    }

    /**
     * Gets user's birthdate when defined within EAA Payload claims
     *
     * @return {@link Date}
     */
    public Date getUserBirthdate() {
        return getPayloadClaimDateValue(getEAAPayload().getUserBirthdate());
    }

    /**
     * Gets user's timezone when defined within EAA Payload claims
     *
     * @return {@link String}
     */
    public String getUserTimezone() {
        return getPayloadClaimTextValue(getEAAPayload().getUserTimezone());
    }

    /**
     * Gets user's locale when defined within EAA Payload claims
     *
     * @return {@link String}
     */
    public String getUserLocale() {
        return getPayloadClaimTextValue(getEAAPayload().getUserLocale());
    }

    /**
     * Gets user's full postal address, formatted, when defined within EAA Payload claims
     *
     * @return {@link String}
     */
    public String getUserPostalAddress() {
        AddressClaimWrapper userAddress = getEAAPayload().getUserAddress();
        if (userAddress != null) {
            return getPayloadClaimTextValue(userAddress.getPostalAddress());
        }
        return null;
    }

    /**
     * Gets user's city address when defined within EAA Payload claims
     *
     * @return {@link String}
     */
    public String getUserAddressCity() {
        AddressClaimWrapper userAddress = getEAAPayload().getUserAddress();
        if (userAddress != null) {
            return getPayloadClaimTextValue(userAddress.getCity());
        }
        return null;
    }

    /**
     * Gets user's state or region address when defined within EAA Payload claims
     *
     * @return {@link String}
     */
    public String getUserAddressStateOrProvince() {
        AddressClaimWrapper userAddress = getEAAPayload().getUserAddress();
        if (userAddress != null) {
            return getPayloadClaimTextValue(userAddress.getStateOrProvince());
        }
        return null;
    }

    /**
     * Gets user's postal code address when defined within EAA Payload claims
     *
     * @return {@link String}
     */
    public String getUserAddressPostalCode() {
        AddressClaimWrapper userAddress = getEAAPayload().getUserAddress();
        if (userAddress != null) {
            return getPayloadClaimTextValue(userAddress.getPostalCode());
        }
        return null;
    }

    /**
     * Gets user's country address when defined within EAA Payload claims.
     * NOTE: The returned value is usually represented by 2-letter ISO country code.
     *
     * @return {@link String}
     */
    public String getUserAddressCountry() {
        AddressClaimWrapper userAddress = getEAAPayload().getUserAddress();
        if (userAddress != null) {
            return getPayloadClaimTextValue(userAddress.getCountry());
        }
        return null;
    }

    /**
     * Gets user's street address when defined within EAA Payload claims
     *
     * @return {@link String}
     */
    public String getUserStreetAddress() {
        AddressClaimWrapper userAddress = getEAAPayload().getUserAddress();
        if (userAddress != null) {
            return getPayloadClaimTextValue(userAddress.getStreetAddress());
        }
        return null;
    }

    /**
     * Gets user's phone number when defined within EAA Payload claims
     *
     * @return {@link String}
     */
    public String getUserPhoneNumber() {
        return getPayloadClaimTextValue(getEAAPayload().getUserPhoneNumber());
    }

    /**
     * Gets whether the user's phone number has been verified if defined within EAA Payload claims
     *
     * @return {@link Boolean}
     */
    public Boolean getUserPhoneNumberVerified() {
        return getPayloadClaimBooleanValue(getEAAPayload().getUserPhoneNumberVerified());
    }

    /**
     * Gets user's country of birth when defined within EAA Payload claims
     *
     * @return {@link String}
     */
    public String getUserPlaceOfBirthCountry() {
        PlaceOfBirthClaimWrapper userPlaceOfBirth = getEAAPayload().getUserPlaceOfBirth();
        if (userPlaceOfBirth != null) {
            return getPayloadClaimTextValue(userPlaceOfBirth.getCountry());
        }
        return null;
    }

    /**
     * Gets user's state or region of birth when defined within EAA Payload claims
     *
     * @return {@link String}
     */
    public String getUserPlaceOfBirthRegion() {
        PlaceOfBirthClaimWrapper userPlaceOfBirth = getEAAPayload().getUserPlaceOfBirth();
        if (userPlaceOfBirth != null) {
            return getPayloadClaimTextValue(userPlaceOfBirth.getRegion());
        }
        return null;
    }

    /**
     * Gets user's city of birth when defined within EAA Payload claims
     *
     * @return {@link String}
     */
    public String getUserPlaceOfBirthCity() {
        PlaceOfBirthClaimWrapper userPlaceOfBirth = getEAAPayload().getUserPlaceOfBirth();
        if (userPlaceOfBirth != null) {
            return getPayloadClaimTextValue(userPlaceOfBirth.getCity());
        }
        return null;
    }

    /**
     * Gets user's nationalities list when defined within EAA Payload claims.
     * NOTE: The values are usually represented by 3-letter nationality codes.
     *
     * @return a list of {@link String}s
     */
    public List<String> getUserNationalities() {
        return getPayloadClaimArrayAsStringsValue(getEAAPayload().getUserNationalities());
    }

    /**
     * Gets user's last or family name at birth when defined within EAA Payload claims
     *
     * @return {@link String}
     */
    public String getUserBirthLastName() {
        return getPayloadClaimTextValue(getEAAPayload().getUserBirthLastName());
    }

    /**
     * Gets user's first name at birth when defined within EAA Payload claims
     *
     * @return {@link String}
     */
    public String getUserBirthFirstName() {
        return getPayloadClaimTextValue(getEAAPayload().getUserBirthFirstName());
    }

    /**
     * Gets user's middle name at birth when defined within EAA Payload claims
     *
     * @return {@link String}
     */
    public String getUserBirthMiddleName() {
        return getPayloadClaimTextValue(getEAAPayload().getUserBirthMiddleName());
    }

    /**
     * Gets user's preferred salutation when defined within EAA Payload claims
     *
     * @return {@link String}
     */
    public String getUserSalutation() {
        return getPayloadClaimTextValue(getEAAPayload().getUserSalutation());
    }

    /**
     * Gets user's title when defined within EAA Payload claims
     *
     * @return {@link String}
     */
    public String getUserTitle() {
        return getPayloadClaimTextValue(getEAAPayload().getUserTitle());
    }

    /**
     * Gets user's mobile phone number when defined within EAA Payload claims
     *
     * @return {@link String}
     */
    public String getUserMobilePhoneNumber() {
        return getPayloadClaimTextValue(getEAAPayload().getUserMobilePhoneNumber());
    }

    /**
     * Gets user's scenic name or pseudonym, they are known as, when defined within EAA Payload claims
     *
     * @return {@link String}
     */
    public String getUserPseudonym() {
        return getPayloadClaimTextValue(getEAAPayload().getUserPseudonym());
    }

    /**
     * Gets a list of claims incorporated within the EAA Payload or provided as disclosures,
     * which are not (yet) directly supported by the implementation.
     *
     * @return a lust of {@link ClaimWrapper}s
     */
    public List<ClaimWrapper> getOtherClaims() {
        return getEAAPayload().getOtherClaims();
    }

    /**
     * This method returns a claim using the header name used within the EAA payload
     *
     * @param headerName {@link String} representing the header name
     * @return {@link ClaimWrapper} if present, or NULL otherwise
     */
    public ClaimWrapper getClaimByHeaderName(String headerName) {
        if (headerName == null) {
            return null;
        }
        List<ClaimWrapper> eaaPayloadClaims = getAllEAAPayloadClaims();
        if (eaaPresentation != null && !eaaPayloadClaims.isEmpty()) {
            for (ClaimWrapper claim : eaaPayloadClaims) {
                if (headerName.equals(claim.getName())) {
                    return claim;
                }
            }
        }
        return null;
    }

    /**
     * This method returns all claims that have been selectively disclosed and identified on the EAA Presentation
     * (i.e. provided in the form of disclosures).
     *
     * @return a list of {@link ClaimWrapper}s
     */
    public List<ClaimWrapper> getSelectivelyDisclosableClaims() {
        final List<ClaimWrapper> result = new ArrayList<>();
        List<ClaimWrapper> eaaPayloadClaims = getAllEAAPayloadClaims();
        if (eaaPresentation != null && !eaaPayloadClaims.isEmpty()) {
            for (ClaimWrapper claim : eaaPayloadClaims) {
                if (claim.isSelectivelyDisclosable()) {
                    result.add(claim);
                }
            }
        }
        return result;
    }

    private String getPayloadClaimTextValue(ClaimWrapper xmlDisclosableClaim) {
        if (xmlDisclosableClaim == null) {
            return null;
        }
        return xmlDisclosableClaim.getText();
    }

    private BigInteger getPayloadClaimNumberValue(ClaimWrapper xmlDisclosableClaim) {
        if (xmlDisclosableClaim == null) {
            return null;
        }
        return xmlDisclosableClaim.getNumber();
    }

    private Date getPayloadClaimDateValue(ClaimWrapper xmlDisclosableClaim) {
        if (xmlDisclosableClaim == null) {
            return null;
        }
        return xmlDisclosableClaim.getDateTime();
    }

    private Boolean getPayloadClaimBooleanValue(ClaimWrapper xmlDisclosableClaim) {
        if (xmlDisclosableClaim == null) {
            return null;
        }
        return xmlDisclosableClaim.isBoolean();
    }

    private List<String> getPayloadClaimArrayAsStringsValue(ClaimWrapper xmlDisclosableClaim) {
        if (xmlDisclosableClaim == null) {
            return null;
        }
        return xmlDisclosableClaim.getList().stream().map(ClaimWrapper::getText)
                .filter(Objects::nonNull).collect(Collectors.toList());
    }

    /**
     * Gets a list of all disclosable claims present within an EAA Payload
     *
     * @return a list of {@link ClaimWrapper}s
     */
    public List<ClaimWrapper> getAllEAAPayloadClaims() {
        return getEAAPayload().getAllEAAPayloadClaims();
    }

    /**
     * Gets type of the EAA Presentation
     *
     * @return {@link EAAPresentationType}
     */
    public EAAPresentationType getType() {
        return eaaPresentation.getType();
    }

}
