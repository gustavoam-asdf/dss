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
    private List<XmlDisclosableClaim> claimList;

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
            return getPayloadClaimStringValue(eaaPresentation.getEAAPayload().getIdentifier());
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
            return getPayloadClaimStringValue(eaaPresentation.getEAAPayload().getIssuer());
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
            return getPayloadClaimStringValue(eaaPresentation.getEAAPayload().getSubject());
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
            return getPayloadClaimStringValue(eaaPresentation.getEAAPayload().getAudience());
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
            return getPayloadClaimStringValue(eaaPresentation.getEAAPayload().getCategory());
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
            return getPayloadClaimStringValue(eaaPresentation.getEAAPayload().getMetadataType());
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
            return getPayloadClaimStringValue(eaaPresentation.getEAAPayload().getStatus().getUri());
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
            return getPayloadClaimStringValue(eaaPresentation.getEAAPayload().getNonce());
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
            return getPayloadClaimStringValue(eaaPresentation.getEAAPayload().getFullName());
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
            return getPayloadClaimStringValue(eaaPresentation.getEAAPayload().getFirstName());
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
            return getPayloadClaimStringValue(eaaPresentation.getEAAPayload().getLastName());
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
            return getPayloadClaimStringValue(eaaPresentation.getEAAPayload().getMiddleName());
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
            return getPayloadClaimStringValue(eaaPresentation.getEAAPayload().getNickname());
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
            return getPayloadClaimStringValue(eaaPresentation.getEAAPayload().getShortName());
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
            return getPayloadClaimStringValue(eaaPresentation.getEAAPayload().getProfileUrl());
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
            return getPayloadClaimStringValue(eaaPresentation.getEAAPayload().getPictureUrl());
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
            return getPayloadClaimStringValue(eaaPresentation.getEAAPayload().getWebsiteUrl());
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
            return getPayloadClaimStringValue(eaaPresentation.getEAAPayload().getEmail());
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
            return getPayloadClaimStringValue(eaaPresentation.getEAAPayload().getGender());
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
            return getPayloadClaimStringValue(eaaPresentation.getEAAPayload().getTimezone());
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
            return getPayloadClaimStringValue(eaaPresentation.getEAAPayload().getLocale());
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
            return getPayloadClaimStringValue(eaaPresentation.getEAAPayload().getAddress().getPostalAddress());
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
            return getPayloadClaimStringValue(eaaPresentation.getEAAPayload().getAddress().getCity());
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
            return getPayloadClaimStringValue(eaaPresentation.getEAAPayload().getAddress().getStateOrProvince());
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
            return getPayloadClaimStringValue(eaaPresentation.getEAAPayload().getAddress().getPostalCode());
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
            return getPayloadClaimStringValue(eaaPresentation.getEAAPayload().getAddress().getCountryName());
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
            return getPayloadClaimStringValue(eaaPresentation.getEAAPayload().getAddress().getStreetAddress());
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
            return getPayloadClaimStringValue(eaaPresentation.getEAAPayload().getPhoneNumber());
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
            return getPayloadClaimStringValue(eaaPresentation.getEAAPayload().getPlaceOfBirth().getCountry());
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
            return getPayloadClaimStringValue(eaaPresentation.getEAAPayload().getPlaceOfBirth().getRegion());
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
            return getPayloadClaimStringValue(eaaPresentation.getEAAPayload().getPlaceOfBirth().getCity());
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
            return getPayloadClaimStringValue(eaaPresentation.getEAAPayload().getBirthLastName());
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
            return getPayloadClaimStringValue(eaaPresentation.getEAAPayload().getBirthFirstName());
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
            return getPayloadClaimStringValue(eaaPresentation.getEAAPayload().getBirthMiddleName());
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
            return getPayloadClaimStringValue(eaaPresentation.getEAAPayload().getSalutation());
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
            return getPayloadClaimStringValue(eaaPresentation.getEAAPayload().getTitle());
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
            return getPayloadClaimStringValue(eaaPresentation.getEAAPayload().getMobilePhoneNumber());
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
            return getPayloadClaimStringValue(eaaPresentation.getEAAPayload().getPseudonym());
        }
        return null;
    }

    /**
     * Gets a list of claims incorporated within the EAA Payload or provided as disclosures,
     * which are not (yet) directly supported by the implementation.
     *
     * @return a lust of {@link XmlDisclosableClaim}s
     */
    public List<XmlDisclosableClaim> getOtherClaims() {
        if (eaaPresentation.getEAAPayload() != null) {
            return eaaPresentation.getEAAPayload().getOtherClaim();
        }
        return null;
    }

    /**
     * This method returns a claim using the header name used within the EAA payload
     *
     * @param headerName {@link String} representing the header name
     * @return {@link XmlDisclosableClaim} if present, or NULL otherwise
     */
    public XmlDisclosableClaim getClaimByHeaderName(String headerName) {
        if (headerName == null) {
            return null;
        }
        List<XmlDisclosableClaim> eaaPayloadClaims = getAllEAAPayloadClaims();
        if (eaaPresentation != null && !eaaPayloadClaims.isEmpty()) {
            for (XmlDisclosableClaim claim : eaaPayloadClaims) {
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
     * @return a list of {@link XmlDisclosableClaim}s
     */
    public List<XmlDisclosableClaim> getSelectivelyDisclosableClaims() {
        final List<XmlDisclosableClaim> result = new ArrayList<>();
        List<XmlDisclosableClaim> eaaPayloadClaims = getAllEAAPayloadClaims();
        if (eaaPresentation != null && !eaaPayloadClaims.isEmpty()) {
            for (XmlDisclosableClaim claim : eaaPayloadClaims) {
                if (claim.isDisclosure() != null && claim.isDisclosure()) {
                    result.add(claim);
                }
            }
        }
        return result;
    }

    private String getPayloadClaimStringValue(XmlDisclosableClaim xmlDisclosableClaim) {
        if (xmlDisclosableClaim == null) {
            return null;
        }
        return xmlDisclosableClaim.getValue();
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
        return xmlDisclosableClaim.getItem().stream().map(XmlDisclosableClaim::getValue)
                .filter(Objects::nonNull).collect(Collectors.toList());
    }

    private byte[] getEncoded(XmlDisclosableClaim xmlDisclosableClaim) {
        if (xmlDisclosableClaim == null) {
            return null;
        }
        return xmlDisclosableClaim.getEncoded();
    }

    /**
     * Gets a list of all disclosable claims present within an EAA Payload
     *
     * @return a list of {@link XmlDisclosableClaim}s
     */
    public List<XmlDisclosableClaim> getAllEAAPayloadClaims() {
        if (claimList == null) {
            claimList = new ArrayList<>();

            if (eaaPresentation.getEAAPayload() != null) {
                if (eaaPresentation.getEAAPayload().getIdentifier() != null) {
                    claimList.add(eaaPresentation.getEAAPayload().getIdentifier());
                }
                if (eaaPresentation.getEAAPayload().getIssuer() != null) {
                    claimList.add(eaaPresentation.getEAAPayload().getIssuer());
                }
                if (eaaPresentation.getEAAPayload().getSubject() != null) {
                    claimList.add(eaaPresentation.getEAAPayload().getSubject());
                }
                if (eaaPresentation.getEAAPayload().getAudience() != null) {
                    claimList.add(eaaPresentation.getEAAPayload().getAudience());
                }
                if (eaaPresentation.getEAAPayload().getExpirationTime() != null) {
                    claimList.add(eaaPresentation.getEAAPayload().getExpirationTime());
                }
                if (eaaPresentation.getEAAPayload().getNotBefore() != null) {
                    claimList.add(eaaPresentation.getEAAPayload().getNotBefore());
                }
                if (eaaPresentation.getEAAPayload().getIssuedAt() != null) {
                    claimList.add(eaaPresentation.getEAAPayload().getIssuedAt());
                }
                if (eaaPresentation.getEAAPayload().getUpdatedAt() != null) {
                    claimList.add(eaaPresentation.getEAAPayload().getUpdatedAt());
                }
                if (eaaPresentation.getEAAPayload().getCategory() != null) {
                    claimList.add(eaaPresentation.getEAAPayload().getCategory());
                }
                if (eaaPresentation.getEAAPayload().getMetadataType() != null) {
                    claimList.add(eaaPresentation.getEAAPayload().getMetadataType());
                    if (eaaPresentation.getEAAPayload().getMetadataType().getDigestAlgoAndValue() != null) {
                        claimList.add(eaaPresentation.getEAAPayload().getMetadataType().getDigestAlgoAndValue());
                    }
                }
                if (eaaPresentation.getEAAPayload().getStatus() != null) {
                    claimList.add(eaaPresentation.getEAAPayload().getStatus());
                }
                if (eaaPresentation.getEAAPayload().getNonce() != null) {
                    claimList.add(eaaPresentation.getEAAPayload().getNonce());
                }
                if (eaaPresentation.getEAAPayload().getFullName() != null) {
                    claimList.add(eaaPresentation.getEAAPayload().getFullName());
                }
                if (eaaPresentation.getEAAPayload().getFirstName() != null) {
                    claimList.add(eaaPresentation.getEAAPayload().getFirstName());
                }
                if (eaaPresentation.getEAAPayload().getLastName() != null) {
                    claimList.add(eaaPresentation.getEAAPayload().getLastName());
                }
                if (eaaPresentation.getEAAPayload().getMiddleName() != null) {
                    claimList.add(eaaPresentation.getEAAPayload().getMiddleName());
                }
                if (eaaPresentation.getEAAPayload().getNickname() != null) {
                    claimList.add(eaaPresentation.getEAAPayload().getNickname());
                }
                if (eaaPresentation.getEAAPayload().getShortName() != null) {
                    claimList.add(eaaPresentation.getEAAPayload().getShortName());
                }
                if (eaaPresentation.getEAAPayload().getProfileUrl() != null) {
                    claimList.add(eaaPresentation.getEAAPayload().getProfileUrl());
                }
                if (eaaPresentation.getEAAPayload().getPictureUrl() != null) {
                    claimList.add(eaaPresentation.getEAAPayload().getPictureUrl());
                }
                if (eaaPresentation.getEAAPayload().getWebsiteUrl() != null) {
                    claimList.add(eaaPresentation.getEAAPayload().getWebsiteUrl());
                }
                if (eaaPresentation.getEAAPayload().getEmail() != null) {
                    claimList.add(eaaPresentation.getEAAPayload().getEmail());
                }
                if (eaaPresentation.getEAAPayload().getEmailVerified() != null) {
                    claimList.add(eaaPresentation.getEAAPayload().getEmailVerified());
                }
                if (eaaPresentation.getEAAPayload().getGender() != null) {
                    claimList.add(eaaPresentation.getEAAPayload().getGender());
                }
                if (eaaPresentation.getEAAPayload().getBirthdate() != null) {
                    claimList.add(eaaPresentation.getEAAPayload().getBirthdate());
                }
                if (eaaPresentation.getEAAPayload().getTimezone() != null) {
                    claimList.add(eaaPresentation.getEAAPayload().getTimezone());
                }
                if (eaaPresentation.getEAAPayload().getLocale() != null) {
                    claimList.add(eaaPresentation.getEAAPayload().getLocale());
                }
                if (eaaPresentation.getEAAPayload().getAddress() != null) {
                    claimList.add(eaaPresentation.getEAAPayload().getAddress());
                }
                if (eaaPresentation.getEAAPayload().getPhoneNumber() != null) {
                    claimList.add(eaaPresentation.getEAAPayload().getPhoneNumber());
                }
                if (eaaPresentation.getEAAPayload().getPhoneNumberVerified() != null) {
                    claimList.add(eaaPresentation.getEAAPayload().getPhoneNumberVerified());
                }
                if (eaaPresentation.getEAAPayload().getPlaceOfBirth() != null) {
                    claimList.add(eaaPresentation.getEAAPayload().getPlaceOfBirth());
                }
                if (eaaPresentation.getEAAPayload().getNationalities() != null) {
                    claimList.add(eaaPresentation.getEAAPayload().getNationalities());
                }
                if (eaaPresentation.getEAAPayload().getBirthLastName() != null) {
                    claimList.add(eaaPresentation.getEAAPayload().getBirthLastName());
                }
                if (eaaPresentation.getEAAPayload().getBirthFirstName() != null) {
                    claimList.add(eaaPresentation.getEAAPayload().getBirthFirstName());
                }
                if (eaaPresentation.getEAAPayload().getBirthMiddleName() != null) {
                    claimList.add(eaaPresentation.getEAAPayload().getBirthMiddleName());
                }
                if (eaaPresentation.getEAAPayload().getSalutation() != null) {
                    claimList.add(eaaPresentation.getEAAPayload().getSalutation());
                }
                if (eaaPresentation.getEAAPayload().getTitle() != null) {
                    claimList.add(eaaPresentation.getEAAPayload().getTitle());
                }
                if (eaaPresentation.getEAAPayload().getMobilePhoneNumber() != null) {
                    claimList.add(eaaPresentation.getEAAPayload().getMobilePhoneNumber());
                }
                if (eaaPresentation.getEAAPayload().getPseudonym() != null) {
                    claimList.add(eaaPresentation.getEAAPayload().getPseudonym());
                }
                if (eaaPresentation.getEAAPayload().getOtherClaim() != null && !eaaPresentation.getEAAPayload().getOtherClaim().isEmpty()) {
                    claimList.addAll(eaaPresentation.getEAAPayload().getOtherClaim());
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
