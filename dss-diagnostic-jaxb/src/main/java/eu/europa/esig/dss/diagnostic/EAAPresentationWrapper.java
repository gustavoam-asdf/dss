package eu.europa.esig.dss.diagnostic;

import eu.europa.esig.dss.diagnostic.jaxb.XmlDigestMatcher;
import eu.europa.esig.dss.diagnostic.jaxb.XmlDisclosableClaim;
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
     * Gets EAA Presentation identifier provided in the EAA payload
     *
     * @return {@link String}
     */
    public String getEAAIdentifier() {
        if (eaaPresentation.getEAAPayload() != null) {
            return getPayloadClaimTextValue(eaaPresentation.getEAAPayload().getIdentifier());
        }
        return null;
    }

    /**
     * Gets EAA Presentation issuer as defined in the EAA payload
     *
     * @return {@link String}
     */
    public String getEAAIssuer() {
        if (eaaPresentation.getEAAPayload() != null) {
            return getPayloadClaimTextValue(eaaPresentation.getEAAPayload().getIssuer());
        }
        return null;
    }

    /**
     * Gets EAA Presentation subject as defined in the EAA payload
     *
     * @return {@link String}
     */
    public String getEAASubject() {
        if (eaaPresentation.getEAAPayload() != null) {
            return getPayloadClaimTextValue(eaaPresentation.getEAAPayload().getSubject());
        }
        return null;
    }

    /**
     * Gets EAA Presentation audience as defined in the EAA payload
     *
     * @return {@link String}
     */
    public String getEAAAudience() {
        if (eaaPresentation.getEAAPayload() != null) {
            return getPayloadClaimTextValue(eaaPresentation.getEAAPayload().getAudience());
        }
        return null;
    }

    /**
     * Gets EAA Presentation expiration time as defined in the EAA payload
     *
     * @return {@link Date}
     */
    public Date getEAAExpirationTime() {
        if (eaaPresentation.getEAAPayload() != null) {
            return getPayloadClaimDateValue(eaaPresentation.getEAAPayload().getExpirationTime());
        }
        return null;
    }

    /**
     * Gets EAA Presentation not before time as defined in the EAA payload
     *
     * @return {@link Date}
     */
    public Date getEAANotBefore() {
        if (eaaPresentation.getEAAPayload() != null) {
            return getPayloadClaimDateValue(eaaPresentation.getEAAPayload().getNotBefore());
        }
        return null;
    }

    /**
     * Gets EAA Presentation issuance time as defined in the EAA payload
     *
     * @return {@link Date}
     */
    public Date getEAAIssuedAt() {
        if (eaaPresentation.getEAAPayload() != null) {
            return getPayloadClaimDateValue(eaaPresentation.getEAAPayload().getIssuedAt());
        }
        return null;
    }

    /**
     * Gets EAA Presentation update time as defined in the EAA payload
     *
     * @return {@link Date}
     */
    public Date getEAAUpdatedAt() {
        if (eaaPresentation.getEAAPayload() != null) {
            return getPayloadClaimDateValue(eaaPresentation.getEAAPayload().getUpdatedAt());
        }
        return null;
    }

    /**
     * Gets category URN provided in the EAA payload
     *
     * @return {@link String}
     */
    public String getEAACategory() {
        if (eaaPresentation.getEAAPayload() != null) {
            return getPayloadClaimTextValue(eaaPresentation.getEAAPayload().getCategory());
        }
        return null;
    }

    /**
     * Gets EAA Presentation metadata URI (e.g. 'vct' claim) as defined in the EAA payload
     *
     * @return {@link String}
     */
    public String getEAAMetadataUri() {
        if (eaaPresentation.getEAAPayload() != null && eaaPresentation.getEAAPayload().getMetadataType() != null) {
            return getPayloadClaimTextValue(eaaPresentation.getEAAPayload().getMetadataType());
        }
        return null;
    }

    /**
     * Gets Digest Algorithm used to compute the integrity material for the EAA Presentation metadata (when present)
     *
     * @return {@link DigestAlgorithm}
     */
    public DigestAlgorithm getEAAMetadataIntegrityDigestAlgorithm() {
        if (eaaPresentation.getEAAPayload() != null && eaaPresentation.getEAAPayload().getMetadataType() != null
                && eaaPresentation.getEAAPayload().getMetadataType().getDigestAlgoAndValue() != null) {
            return eaaPresentation.getEAAPayload().getMetadataType().getDigestAlgoAndValue().getDigestMethod();
        }
        return null;
    }

    /**
     * Gets the integrity material for the EAA Presentation metadata (when present)
     *
     * @return byte array representing the EAA Presentation's metadata hash
     */
    public byte[] getEAAMetadataIntegrityBytes() {
        if (eaaPresentation.getEAAPayload() != null && eaaPresentation.getEAAPayload().getMetadataType() != null
                && eaaPresentation.getEAAPayload().getMetadataType().getDigestAlgoAndValue() != null) {
            return eaaPresentation.getEAAPayload().getMetadataType().getDigestAlgoAndValue().getDigestValue();
        }
        return null;
    }

    /**
     * Gets EAA Presentation status index as defined in the EAA payload
     *
     * @return {@link BigInteger}
     */
    public BigInteger getEAAStatusIndex() {
        if (eaaPresentation.getEAAPayload() != null && eaaPresentation.getEAAPayload().getStatus() != null) {
            return getPayloadClaimNumberValue(eaaPresentation.getEAAPayload().getStatus().getIndex());
        }
        return null;
    }

    /**
     * Gets EAA Presentation status URI as defined in the EAA payload
     *
     * @return {@link String}
     */
    public String getEAAStatusUri() {
        if (eaaPresentation.getEAAPayload() != null && eaaPresentation.getEAAPayload().getStatus() != null) {
            return getPayloadClaimTextValue(eaaPresentation.getEAAPayload().getStatus().getUri());
        }
        return null;
    }

    /**
     * Gets EAA Presentation nonce when defined in the EAA payload
     *
     * @return {@link String}
     */
    public String getEAANonce() {
        if (eaaPresentation.getEAAPayload() != null) {
            return getPayloadClaimTextValue(eaaPresentation.getEAAPayload().getNonce());
        }
        return null;
    }

    /**
     * Gets user's full name when defined within EAA Payload claims
     *
     * @return {@link String}
     */
    public String getUserFullName() {
        if (eaaPresentation.getEAAPayload() != null) {
            return getPayloadClaimTextValue(eaaPresentation.getEAAPayload().getFullName());
        }
        return null;
    }

    /**
     * Gets user's first name when defined within EAA Payload claims
     *
     * @return {@link String}
     */
    public String getUserFirstName() {
        if (eaaPresentation.getEAAPayload() != null) {
            return getPayloadClaimTextValue(eaaPresentation.getEAAPayload().getFirstName());
        }
        return null;
    }

    /**
     * Gets user's last or family name when defined within EAA Payload claims
     *
     * @return {@link String}
     */
    public String getUserLastName() {
        if (eaaPresentation.getEAAPayload() != null) {
            return getPayloadClaimTextValue(eaaPresentation.getEAAPayload().getLastName());
        }
        return null;
    }

    /**
     * Gets user's middle name when defined within EAA Payload claims
     *
     * @return {@link String}
     */
    public String getUserMiddleName() {
        if (eaaPresentation.getEAAPayload() != null) {
            return getPayloadClaimTextValue(eaaPresentation.getEAAPayload().getMiddleName());
        }
        return null;
    }

    /**
     * Gets user's alternative name when defined within EAA Payload claims
     *
     * @return {@link String}
     */
    public String getUserNickname() {
        if (eaaPresentation.getEAAPayload() != null) {
            return getPayloadClaimTextValue(eaaPresentation.getEAAPayload().getNickname());
        }
        return null;
    }

    /**
     * Gets user's preferred or short name when defined within EAA Payload claims
     *
     * @return {@link String}
     */
    public String getUserShortName() {
        if (eaaPresentation.getEAAPayload() != null) {
            return getPayloadClaimTextValue(eaaPresentation.getEAAPayload().getShortName());
        }
        return null;
    }

    /**
     * Gets user's profile URL when defined within EAA Payload claims
     *
     * @return {@link String}
     */
    public String getUserProfileUrl() {
        if (eaaPresentation.getEAAPayload() != null) {
            return getPayloadClaimTextValue(eaaPresentation.getEAAPayload().getProfileUrl());
        }
        return null;
    }

    /**
     * Gets user's picture URL when defined within EAA Payload claims
     *
     * @return {@link String}
     */
    public String getUserPictureUrl() {
        if (eaaPresentation.getEAAPayload() != null) {
            return getPayloadClaimTextValue(eaaPresentation.getEAAPayload().getPictureUrl());
        }
        return null;
    }

    /**
     * Gets user's website when defined within EAA Payload claims
     *
     * @return {@link String}
     */
    public String getUserWebsiteUrl() {
        if (eaaPresentation.getEAAPayload() != null) {
            return getPayloadClaimTextValue(eaaPresentation.getEAAPayload().getWebsiteUrl());
        }
        return null;
    }

    /**
     * Gets user's email when defined within EAA Payload claims
     *
     * @return {@link String}
     */
    public String getUserEmail() {
        if (eaaPresentation.getEAAPayload() != null) {
            return getPayloadClaimTextValue(eaaPresentation.getEAAPayload().getEmail());
        }
        return null;
    }

    /**
     * Gets whether the user's website has been verified if defined within EAA Payload claims
     *
     * @return {@link Boolean}
     */
    public Boolean getUserEmailVerified() {
        if (eaaPresentation.getEAAPayload() != null) {
            return getPayloadClaimBooleanValue(eaaPresentation.getEAAPayload().getEmailVerified());
        }
        return null;
    }

    /**
     * Gets user's gender when defined within EAA Payload claims
     *
     * @return {@link String}
     */
    public String getUserGender() {
        if (eaaPresentation.getEAAPayload() != null) {
            return getPayloadClaimTextValue(eaaPresentation.getEAAPayload().getGender());
        }
        return null;
    }

    /**
     * Gets user's birthdate when defined within EAA Payload claims
     *
     * @return {@link Date}
     */
    public Date getUserBirthdate() {
        if (eaaPresentation.getEAAPayload() != null) {
            return getPayloadClaimDateValue(eaaPresentation.getEAAPayload().getBirthdate());
        }
        return null;
    }

    /**
     * Gets user's timezone when defined within EAA Payload claims
     *
     * @return {@link String}
     */
    public String getUserTimezone() {
        if (eaaPresentation.getEAAPayload() != null) {
            return getPayloadClaimTextValue(eaaPresentation.getEAAPayload().getTimezone());
        }
        return null;
    }

    /**
     * Gets user's locale when defined within EAA Payload claims
     *
     * @return {@link String}
     */
    public String getUserLocale() {
        if (eaaPresentation.getEAAPayload() != null) {
            return getPayloadClaimTextValue(eaaPresentation.getEAAPayload().getLocale());
        }
        return null;
    }

    /**
     * Gets user's full postal address, formatted, when defined within EAA Payload claims
     *
     * @return {@link String}
     */
    public String getUserPostalAddress() {
        if (eaaPresentation.getEAAPayload() != null && eaaPresentation.getEAAPayload().getAddress() != null) {
            return getPayloadClaimTextValue(eaaPresentation.getEAAPayload().getAddress().getPostalAddress());
        }
        return null;
    }

    /**
     * Gets user's city address when defined within EAA Payload claims
     *
     * @return {@link String}
     */
    public String getUserAddressCity() {
        if (eaaPresentation.getEAAPayload() != null && eaaPresentation.getEAAPayload().getAddress() != null) {
            return getPayloadClaimTextValue(eaaPresentation.getEAAPayload().getAddress().getCity());
        }
        return null;
    }

    /**
     * Gets user's state or region address when defined within EAA Payload claims
     *
     * @return {@link String}
     */
    public String getUserAddressStateOrProvince() {
        if (eaaPresentation.getEAAPayload() != null && eaaPresentation.getEAAPayload().getAddress() != null) {
            return getPayloadClaimTextValue(eaaPresentation.getEAAPayload().getAddress().getStateOrProvince());
        }
        return null;
    }

    /**
     * Gets user's postal code address when defined within EAA Payload claims
     *
     * @return {@link String}
     */
    public String getUserAddressPostalCode() {
        if (eaaPresentation.getEAAPayload() != null && eaaPresentation.getEAAPayload().getAddress() != null) {
            return getPayloadClaimTextValue(eaaPresentation.getEAAPayload().getAddress().getPostalCode());
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
        if (eaaPresentation.getEAAPayload() != null && eaaPresentation.getEAAPayload().getAddress() != null) {
            return getPayloadClaimTextValue(eaaPresentation.getEAAPayload().getAddress().getCountryName());
        }
        return null;
    }

    /**
     * Gets user's street address when defined within EAA Payload claims
     *
     * @return {@link String}
     */
    public String getUserStreetAddress() {
        if (eaaPresentation.getEAAPayload() != null && eaaPresentation.getEAAPayload().getAddress() != null) {
            return getPayloadClaimTextValue(eaaPresentation.getEAAPayload().getAddress().getStreetAddress());
        }
        return null;
    }

    /**
     * Gets user's phone number when defined within EAA Payload claims
     *
     * @return {@link String}
     */
    public String getUserPhoneNumber() {
        if (eaaPresentation.getEAAPayload() != null) {
            return getPayloadClaimTextValue(eaaPresentation.getEAAPayload().getPhoneNumber());
        }
        return null;
    }

    /**
     * Gets whether the user's phone number has been verified if defined within EAA Payload claims
     *
     * @return {@link Boolean}
     */
    public Boolean getUserPhoneNumberVerified() {
        if (eaaPresentation.getEAAPayload() != null) {
            return getPayloadClaimBooleanValue(eaaPresentation.getEAAPayload().getPhoneNumberVerified());
        }
        return null;
    }

    /**
     * Gets user's country of birth when defined within EAA Payload claims
     *
     * @return {@link String}
     */
    public String getUserPlaceOfBirthCountry() {
        if (eaaPresentation.getEAAPayload() != null && eaaPresentation.getEAAPayload().getPlaceOfBirth() != null) {
            return getPayloadClaimTextValue(eaaPresentation.getEAAPayload().getPlaceOfBirth().getCountry());
        }
        return null;
    }

    /**
     * Gets user's state or region of birth when defined within EAA Payload claims
     *
     * @return {@link String}
     */
    public String getUserPlaceOfBirthRegion() {
        if (eaaPresentation.getEAAPayload() != null && eaaPresentation.getEAAPayload().getPlaceOfBirth() != null) {
            return getPayloadClaimTextValue(eaaPresentation.getEAAPayload().getPlaceOfBirth().getRegion());
        }
        return null;
    }

    /**
     * Gets user's city of birth when defined within EAA Payload claims
     *
     * @return {@link String}
     */
    public String getUserPlaceOfBirthCity() {
        if (eaaPresentation.getEAAPayload() != null && eaaPresentation.getEAAPayload().getPlaceOfBirth() != null) {
            return getPayloadClaimTextValue(eaaPresentation.getEAAPayload().getPlaceOfBirth().getCity());
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
        if (eaaPresentation.getEAAPayload() != null) {
            return getPayloadClaimArrayAsStringsValue(eaaPresentation.getEAAPayload().getNationalities());
        }
        return Collections.emptyList();
    }

    /**
     * Gets user's last or family name at birth when defined within EAA Payload claims
     *
     * @return {@link String}
     */
    public String getUserBirthLastName() {
        if (eaaPresentation.getEAAPayload() != null) {
            return getPayloadClaimTextValue(eaaPresentation.getEAAPayload().getBirthLastName());
        }
        return null;
    }

    /**
     * Gets user's first name at birth when defined within EAA Payload claims
     *
     * @return {@link String}
     */
    public String getUserBirthFirstName() {
        if (eaaPresentation.getEAAPayload() != null) {
            return getPayloadClaimTextValue(eaaPresentation.getEAAPayload().getBirthFirstName());
        }
        return null;
    }

    /**
     * Gets user's middle name at birth when defined within EAA Payload claims
     *
     * @return {@link String}
     */
    public String getUserBirthMiddleName() {
        if (eaaPresentation.getEAAPayload() != null) {
            return getPayloadClaimTextValue(eaaPresentation.getEAAPayload().getBirthMiddleName());
        }
        return null;
    }

    /**
     * Gets user's preferred salutation when defined within EAA Payload claims
     *
     * @return {@link String}
     */
    public String getUserSalutation() {
        if (eaaPresentation.getEAAPayload() != null) {
            return getPayloadClaimTextValue(eaaPresentation.getEAAPayload().getSalutation());
        }
        return null;
    }

    /**
     * Gets user's title when defined within EAA Payload claims
     *
     * @return {@link String}
     */
    public String getUserTitle() {
        if (eaaPresentation.getEAAPayload() != null) {
            return getPayloadClaimTextValue(eaaPresentation.getEAAPayload().getTitle());
        }
        return null;
    }

    /**
     * Gets user's mobile phone number when defined within EAA Payload claims
     *
     * @return {@link String}
     */
    public String getUserMobilePhoneNumber() {
        if (eaaPresentation.getEAAPayload() != null) {
            return getPayloadClaimTextValue(eaaPresentation.getEAAPayload().getMobilePhoneNumber());
        }
        return null;
    }

    /**
     * Gets user's scenic name or pseudonym, they are known as, when defined within EAA Payload claims
     *
     * @return {@link String}
     */
    public String getUserPseudonym() {
        if (eaaPresentation.getEAAPayload() != null) {
            return getPayloadClaimTextValue(eaaPresentation.getEAAPayload().getPseudonym());
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
        if (eaaPresentation.getEAAPayload() != null && eaaPresentation.getEAAPayload().getOtherClaim() != null) {
            return eaaPresentation.getEAAPayload().getOtherClaim().stream().map(ClaimWrapper::new).collect(Collectors.toList());
        }
        return null;
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

    private ClaimWrapper getClaim(XmlDisclosableClaim xmlDisclosableClaim) {
        if (xmlDisclosableClaim == null) {
            return null;
        }
        return new ClaimWrapper(xmlDisclosableClaim);
    }

    private String getPayloadClaimTextValue(XmlDisclosableClaim xmlDisclosableClaim) {
        if (xmlDisclosableClaim == null) {
            return null;
        }
        return xmlDisclosableClaim.getText();
    }

    private BigInteger getPayloadClaimNumberValue(XmlDisclosableClaim xmlDisclosableClaim) {
        if (xmlDisclosableClaim == null) {
            return null;
        }
        return xmlDisclosableClaim.getNumber();
    }

    private Date getPayloadClaimDateValue(XmlDisclosableClaim xmlDisclosableClaim) {
        if (xmlDisclosableClaim == null) {
            return null;
        }
        return xmlDisclosableClaim.getDateTime();
    }

    private Boolean getPayloadClaimBooleanValue(XmlDisclosableClaim xmlDisclosableClaim) {
        if (xmlDisclosableClaim == null) {
            return null;
        }
        return xmlDisclosableClaim.isBoolean();
    }

    private List<String> getPayloadClaimArrayAsStringsValue(XmlDisclosableClaim xmlDisclosableClaim) {
        if (xmlDisclosableClaim == null) {
            return null;
        }
        return xmlDisclosableClaim.getItem().stream().map(XmlDisclosableClaim::getText)
                .filter(Objects::nonNull).collect(Collectors.toList());
    }

    /**
     * Gets a list of all disclosable claims present within an EAA Payload
     *
     * @return a list of {@link ClaimWrapper}s
     */
    public List<ClaimWrapper> getAllEAAPayloadClaims() {
        if (claimList == null) {
            claimList = new ArrayList<>();

            if (eaaPresentation.getEAAPayload() != null) {
                if (eaaPresentation.getEAAPayload().getIdentifier() != null) {
                    claimList.add(getClaim(eaaPresentation.getEAAPayload().getIdentifier()));
                }
                if (eaaPresentation.getEAAPayload().getIssuer() != null) {
                    claimList.add(getClaim(eaaPresentation.getEAAPayload().getIssuer()));
                }
                if (eaaPresentation.getEAAPayload().getSubject() != null) {
                    claimList.add(getClaim(eaaPresentation.getEAAPayload().getSubject()));
                }
                if (eaaPresentation.getEAAPayload().getAudience() != null) {
                    claimList.add(getClaim(eaaPresentation.getEAAPayload().getAudience()));
                }
                if (eaaPresentation.getEAAPayload().getExpirationTime() != null) {
                    claimList.add(getClaim(eaaPresentation.getEAAPayload().getExpirationTime()));
                }
                if (eaaPresentation.getEAAPayload().getNotBefore() != null) {
                    claimList.add(getClaim(eaaPresentation.getEAAPayload().getNotBefore()));
                }
                if (eaaPresentation.getEAAPayload().getIssuedAt() != null) {
                    claimList.add(getClaim(eaaPresentation.getEAAPayload().getIssuedAt()));
                }
                if (eaaPresentation.getEAAPayload().getUpdatedAt() != null) {
                    claimList.add(getClaim(eaaPresentation.getEAAPayload().getUpdatedAt()));
                }
                if (eaaPresentation.getEAAPayload().getCategory() != null) {
                    claimList.add(getClaim(eaaPresentation.getEAAPayload().getCategory()));
                }
                if (eaaPresentation.getEAAPayload().getMetadataType() != null) {
                    claimList.add(getClaim(eaaPresentation.getEAAPayload().getMetadataType()));
                    if (eaaPresentation.getEAAPayload().getMetadataType().getDigestAlgoAndValue() != null) {
                        claimList.add(getClaim(eaaPresentation.getEAAPayload().getMetadataType().getDigestAlgoAndValue()));
                    }
                }
                if (eaaPresentation.getEAAPayload().getStatus() != null) {
                    claimList.add(getClaim(eaaPresentation.getEAAPayload().getStatus()));
                }
                if (eaaPresentation.getEAAPayload().getNonce() != null) {
                    claimList.add(getClaim(eaaPresentation.getEAAPayload().getNonce()));
                }
                if (eaaPresentation.getEAAPayload().getFullName() != null) {
                    claimList.add(getClaim(eaaPresentation.getEAAPayload().getFullName()));
                }
                if (eaaPresentation.getEAAPayload().getFirstName() != null) {
                    claimList.add(getClaim(eaaPresentation.getEAAPayload().getFirstName()));
                }
                if (eaaPresentation.getEAAPayload().getLastName() != null) {
                    claimList.add(getClaim(eaaPresentation.getEAAPayload().getLastName()));
                }
                if (eaaPresentation.getEAAPayload().getMiddleName() != null) {
                    claimList.add(getClaim(eaaPresentation.getEAAPayload().getMiddleName()));
                }
                if (eaaPresentation.getEAAPayload().getNickname() != null) {
                    claimList.add(getClaim(eaaPresentation.getEAAPayload().getNickname()));
                }
                if (eaaPresentation.getEAAPayload().getShortName() != null) {
                    claimList.add(getClaim(eaaPresentation.getEAAPayload().getShortName()));
                }
                if (eaaPresentation.getEAAPayload().getProfileUrl() != null) {
                    claimList.add(getClaim(eaaPresentation.getEAAPayload().getProfileUrl()));
                }
                if (eaaPresentation.getEAAPayload().getPictureUrl() != null) {
                    claimList.add(getClaim(eaaPresentation.getEAAPayload().getPictureUrl()));
                }
                if (eaaPresentation.getEAAPayload().getWebsiteUrl() != null) {
                    claimList.add(getClaim(eaaPresentation.getEAAPayload().getWebsiteUrl()));
                }
                if (eaaPresentation.getEAAPayload().getEmail() != null) {
                    claimList.add(getClaim(eaaPresentation.getEAAPayload().getEmail()));
                }
                if (eaaPresentation.getEAAPayload().getEmailVerified() != null) {
                    claimList.add(getClaim(eaaPresentation.getEAAPayload().getEmailVerified()));
                }
                if (eaaPresentation.getEAAPayload().getGender() != null) {
                    claimList.add(getClaim(eaaPresentation.getEAAPayload().getGender()));
                }
                if (eaaPresentation.getEAAPayload().getBirthdate() != null) {
                    claimList.add(getClaim(eaaPresentation.getEAAPayload().getBirthdate()));
                }
                if (eaaPresentation.getEAAPayload().getTimezone() != null) {
                    claimList.add(getClaim(eaaPresentation.getEAAPayload().getTimezone()));
                }
                if (eaaPresentation.getEAAPayload().getLocale() != null) {
                    claimList.add(getClaim(eaaPresentation.getEAAPayload().getLocale()));
                }
                if (eaaPresentation.getEAAPayload().getAddress() != null) {
                    claimList.add(getClaim(eaaPresentation.getEAAPayload().getAddress()));
                }
                if (eaaPresentation.getEAAPayload().getPhoneNumber() != null) {
                    claimList.add(getClaim(eaaPresentation.getEAAPayload().getPhoneNumber()));
                }
                if (eaaPresentation.getEAAPayload().getPhoneNumberVerified() != null) {
                    claimList.add(getClaim(eaaPresentation.getEAAPayload().getPhoneNumberVerified()));
                }
                if (eaaPresentation.getEAAPayload().getPlaceOfBirth() != null) {
                    claimList.add(getClaim(eaaPresentation.getEAAPayload().getPlaceOfBirth()));
                }
                if (eaaPresentation.getEAAPayload().getNationalities() != null) {
                    claimList.add(getClaim(eaaPresentation.getEAAPayload().getNationalities()));
                }
                if (eaaPresentation.getEAAPayload().getBirthLastName() != null) {
                    claimList.add(getClaim(eaaPresentation.getEAAPayload().getBirthLastName()));
                }
                if (eaaPresentation.getEAAPayload().getBirthFirstName() != null) {
                    claimList.add(getClaim(eaaPresentation.getEAAPayload().getBirthFirstName()));
                }
                if (eaaPresentation.getEAAPayload().getBirthMiddleName() != null) {
                    claimList.add(getClaim(eaaPresentation.getEAAPayload().getBirthMiddleName()));
                }
                if (eaaPresentation.getEAAPayload().getSalutation() != null) {
                    claimList.add(getClaim(eaaPresentation.getEAAPayload().getSalutation()));
                }
                if (eaaPresentation.getEAAPayload().getTitle() != null) {
                    claimList.add(getClaim(eaaPresentation.getEAAPayload().getTitle()));
                }
                if (eaaPresentation.getEAAPayload().getMobilePhoneNumber() != null) {
                    claimList.add(getClaim(eaaPresentation.getEAAPayload().getMobilePhoneNumber()));
                }
                if (eaaPresentation.getEAAPayload().getPseudonym() != null) {
                    claimList.add(getClaim(eaaPresentation.getEAAPayload().getPseudonym()));
                }
                if (eaaPresentation.getEAAPayload().getOtherClaim() != null && !eaaPresentation.getEAAPayload().getOtherClaim().isEmpty()) {
                    List<ClaimWrapper> claimWrappers = eaaPresentation.getEAAPayload().getOtherClaim().stream()
                            .map(this::getClaim).collect(Collectors.toList());
                    claimList.addAll(claimWrappers);
                }
            }

        }
        return claimList;
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
