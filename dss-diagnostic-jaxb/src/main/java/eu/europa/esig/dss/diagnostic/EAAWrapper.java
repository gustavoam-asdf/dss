package eu.europa.esig.dss.diagnostic;

import eu.europa.esig.dss.diagnostic.claim.AddressClaimWrapper;
import eu.europa.esig.dss.diagnostic.claim.AgeOverNNClaimWrapper;
import eu.europa.esig.dss.diagnostic.claim.AttestedAttributesSubjectClaimWrapper;
import eu.europa.esig.dss.diagnostic.claim.BiometricTemplateXXClaimWrapper;
import eu.europa.esig.dss.diagnostic.claim.ClaimWrapper;
import eu.europa.esig.dss.diagnostic.claim.DeviceKeyClaimWrapper;
import eu.europa.esig.dss.diagnostic.claim.DrivingPrivilegesClaimWrapper;
import eu.europa.esig.dss.diagnostic.claim.IntegrityClaimWrapper;
import eu.europa.esig.dss.diagnostic.claim.PlaceOfBirthClaimWrapper;
import eu.europa.esig.dss.diagnostic.claim.StatusClaimWrapper;
import eu.europa.esig.dss.diagnostic.claim.ValidityInfoClaimWrapper;
import eu.europa.esig.dss.diagnostic.jaxb.XmlDigestMatcher;
import eu.europa.esig.dss.diagnostic.jaxb.XmlEAASignature;
import eu.europa.esig.dss.diagnostic.jaxb.XmlEAA;
import eu.europa.esig.dss.enumerations.DigestAlgorithm;
import eu.europa.esig.dss.enumerations.EAACategory;
import eu.europa.esig.dss.enumerations.EAAQualification;
import eu.europa.esig.dss.enumerations.EAAType;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Provides a user-friendly interface for information extraction from a {@code eu.europa.esig.dss.diagnostic.jaxb.XmlEAA} JAXB object
 *
 */
public class EAAWrapper {

    /** Wrapped EAA object */
    private final XmlEAA eaa;

    /**
     * Default constructor
     *
     * @param eaa {@link XmlEAA} to read
     */
    public EAAWrapper(final XmlEAA eaa) {
        this.eaa = eaa;
    }

    /**
     * Gets unique identifier
     *
     * @return {@link String}
     */
    public String getId() {
        return eaa.getId();
    }

    /**
     * Returns name of the EAA presentation's document, when applicable
     *
     * @return {@link String}
     */
    public String getFilename() {
        return eaa.getDocumentName();
    }

    /**
     * Gets claimed document type.
     * NOTE: used in mdoc and the returned value corresponds to a string incorporated within a 'docType' element
     *
     * @return {@link String}
     */
    public String getEAADocumentType() {
        return eaa.getDocumentType();
    }

    /**
     * Gets a list of digest matchers representing the associated hashes and disclosures validation
     *
     * @return a list of {@link XmlDigestMatcher}
     */
    public List<XmlDigestMatcher> getDigestMatchers() {
        return eaa.getDigestMatchers();
    }

    /**
     * Gets signatures used to create the EAA.
     * NOTE: in most of the cases a single signature is expected,
     * but it is possible for EAA to be signed by multiple signers.
     *
     * @return a list of {@link SignatureWrapper}s
     */
    public List<SignatureWrapper> getEAASignatures() {
        final List<SignatureWrapper> result = new ArrayList<>();
        for (XmlEAASignature xmlEAASignature : eaa.getEAASignature()) {
            result.add(new SignatureWrapper(xmlEAASignature.getSignature()));
        }
        return result;
    }

    /**
     * Gets a list of identifiers of signatures used to create the EAA
     *
     * @return a list of {@link String}s
     */
    public List<String> getEAASignatureIds() {
        List<SignatureWrapper> eaaPresentationSignatures = getEAASignatures();
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
        if (eaa.getKeyBindingSignature() != null) {
            return new SignatureWrapper(eaa.getKeyBindingSignature().getSignature());
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
        return new EAAPayloadProxy(eaa.getEAAPayload());
    }

    /**
     * Gets EAA identifier provided in the EAA payload
     *
     * @return {@link String}
     */
    public String getEAAIdentifier() {
        return getPayloadClaimTextValue(getEAAPayload().getEAAIdentifier());
    }

    /**
     * Gets EAA issuer as defined in the EAA payload
     *
     * @return {@link String}
     */
    public String getEAAIssuer() {
        return getPayloadClaimTextValue(getEAAPayload().getEAAIssuer());
    }

    /**
     * Gets EAA subject as defined in the EAA payload
     *
     * @return {@link String}
     */
    public String getEAASubject() {
        return getPayloadClaimTextValue(getEAAPayload().getEAASubject());
    }

    /**
     * Gets EAA audience as defined in the EAA payload
     *
     * @return {@link String}
     */
    public String getEAAAudience() {
        return getPayloadClaimTextValue(getEAAPayload().getEAAAudience());
    }

    /**
     * Gets EAA issuance time as defined in the EAA payload
     *
     * @return {@link Date}
     */
    public Date getEAAIssuedAt() {
        Date issuedAt = getPayloadClaimDateValue(getEAAPayload().getEAAIssuedAt());
        if (issuedAt != null) {
            return issuedAt;
        }
        ValidityInfoClaimWrapper eaaValidityInfo = getEAAPayload().getEAAValidityInfo();
        if (eaaValidityInfo != null) {
            return getPayloadClaimDateValue(eaaValidityInfo.getSigned());
        }
        return null;
    }

    /**
     * Gets EAA not before time as defined in the EAA payload
     *
     * @return {@link Date}
     */
    public Date getEAANotBefore() {
        Date notBefore = getPayloadClaimDateValue(getEAAPayload().getEAANotBefore());
        if (notBefore != null) {
            return notBefore;
        }
        ValidityInfoClaimWrapper eaaValidityInfo = getEAAPayload().getEAAValidityInfo();
        if (eaaValidityInfo != null) {
            return getPayloadClaimDateValue(eaaValidityInfo.getValidFrom());
        }
        return null;
    }

    /**
     * Gets EAA expiration time as defined in the EAA payload
     *
     * @return {@link Date}
     */
    public Date getEAAExpiration() {
        Date expirationTime = getPayloadClaimDateValue(getEAAPayload().getEAAExpiration());
        if (expirationTime != null) {
            return expirationTime;
        }
        ValidityInfoClaimWrapper eaaValidityInfo = getEAAPayload().getEAAValidityInfo();
        if (eaaValidityInfo != null) {
            return getPayloadClaimDateValue(eaaValidityInfo.getValidUntil());
        }
        return null;
    }

    /**
     * Gets EAA update time as defined in the EAA payload
     *
     * @return {@link Date}
     */
    public Date getEAAUpdatedAt() {
        return getPayloadClaimDateValue(getEAAPayload().getEAAUpdatedAt());
    }

    /**
     * Gets EAA expected next update time
     *
     * @return {@link Date}
     */
    public Date getEAANextUpdate() {
        ValidityInfoClaimWrapper eaaValidityInfo = getEAAPayload().getEAAValidityInfo();
        if (eaaValidityInfo != null) {
            return getPayloadClaimDateValue(eaaValidityInfo.getExpectedUpdate());
        }
        return null;
    }

    /**
     * Gets category URN provided in the EAA payload
     *
     * @return {@link String}
     */
    public String getEAACategory() {
        return getPayloadClaimTextValue(getEAAPayload().getEAACategory());
    }

    public EAAQualification getCategoryQualification() {
        String eaaCategory = getEAACategory();
        if (EAACategory.EU_QEAA.getUrn().equals(eaaCategory)) {
            return EAAQualification.QEAA;
        } else if (EAACategory.EU_PUBEAA.getUrn().equals(eaaCategory)) {
            return EAAQualification.PUBEAA;
        } else if (eaaCategory == null) {
            /*
             * EAA-5.2.2.1-01: SD-JWT VC EAAs issued by EAAs issuers registered in the European Union,
             * which are neither SD-JWT VC QEAAs nor SD-JWT VC PuB-EAAs, shall not include the category claim.
             */
            return EAAQualification.EAA;
        } else {
            return EAAQualification.UNKNOWN;
        }
    }

    /**
     * Gets EAA metadata URI (e.g. 'vct' claim) as defined in the EAA payload
     *
     * @return {@link String}
     */
    public String getEAAMetadataUri() {
        return getPayloadClaimTextValue(getEAAPayload().getEAAMetadataType());
    }

    /**
     * Gets Digest Algorithm used to compute the integrity material for the EAA metadata (when present)
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
     * Gets the integrity material for the EAA metadata (when present)
     *
     * @return byte array representing the EAA's metadata hash
     */
    public byte[] getEAAMetadataIntegrityBytes() {
        IntegrityClaimWrapper eaaMetadataIntegrity = getEAAPayload().getEAAMetadataIntegrity();
        if (eaaMetadataIntegrity != null) {
            return eaaMetadataIntegrity.getDigestValue();
        }
        return null;
    }

    /**
     * Gets EAA status index as defined in the EAA payload
     *
     * @return {@link BigInteger}
     */
    public BigInteger getEAAStatusIndex() {
        StatusClaimWrapper eaaStatus = getEAAPayload().getEAAStatus();
        if (eaaStatus != null) {
            if (eaaStatus.getIndex() != null) {
                return getPayloadClaimNumberValue(eaaStatus.getIndex());
            } else if (eaaStatus.getStatusList() != null) {
                return getPayloadClaimNumberValue(eaaStatus.getStatusList().getIndex());
            }
        }
        return null;
    }

    /**
     * Gets EAA status URI as defined in the EAA payload
     *
     * @return {@link String}
     */
    public String getEAAStatusUri() {
        StatusClaimWrapper eaaStatus = getEAAPayload().getEAAStatus();
        if (eaaStatus != null) {
            if (eaaStatus.getUri() != null) {
                return getPayloadClaimTextValue(eaaStatus.getUri());
            } else if (eaaStatus.getStatusList() != null) {
                return getPayloadClaimTextValue(eaaStatus.getStatusList().getUri());
            }
        }
        return null;
    }

    /**
     * Gets EAA status type as defined in the EAA payload
     *
     * @return {@link String}
     */
    public String getEAAStatusType() {
        StatusClaimWrapper eaaStatus = getEAAPayload().getEAAStatus();
        if (eaaStatus != null) {
            return getPayloadClaimTextValue(eaaStatus.getType());
        }
        return null;
    }

    /**
     * Gets EAA status purpose as defined in the EAA payload
     *
     * @return {@link String}
     */
    public String getEAAStatusPurpose() {
        StatusClaimWrapper eaaStatus = getEAAPayload().getEAAStatus();
        if (eaaStatus != null) {
            return getPayloadClaimTextValue(eaaStatus.getPurpose());
        }
        return null;
    }

    /**
     * Gets EAA nonce when defined in the EAA payload
     *
     * @return {@link String}
     */
    public String getEAANonce() {
        return getPayloadClaimTextValue(getEAAPayload().getEAANonce());
    }

    /**
     * Gets EAA device public key when defined in the EAA payload
     *
     * @return byte array containing an encoded device public key
     */
    public byte[] getEAADevicePublicKey() {
        DeviceKeyClaimWrapper eaaDeviceKey = getEAAPayload().getEAADeviceKey();
        if (eaaDeviceKey != null) {
            return eaaDeviceKey.getPublicKey();
        }
        return null;
    }

    /**
     * Gets EAA device certificate token when defined in the EAA payload
     *
     * @return {@link CertificateWrapper}
     */
    public CertificateWrapper getEAADeviceCertificate() {
        DeviceKeyClaimWrapper eaaDeviceKey = getEAAPayload().getEAADeviceKey();
        if (eaaDeviceKey != null) {
            List<CertificateWrapper> certificates = eaaDeviceKey.getCertificates();
            if (certificates != null && !certificates.isEmpty()) {
                return certificates.get(0);
            }
        }
        return null;
    }

    /**
     * Gets EAA device certificate chain when defined in the EAA payload
     *
     * @return a list of {@link CertificateWrapper}s
     */
    public List<CertificateWrapper> getEAADeviceCertificateChain() {
        DeviceKeyClaimWrapper eaaDeviceKey = getEAAPayload().getEAADeviceKey();
        if (eaaDeviceKey != null) {
            return eaaDeviceKey.getCertificates();
        }
        return null;
    }

    /**
     * Gets a version of the MobileSecurityObject.
     *
     * @return {@link String}
     */
    public String getEAAVersion() {
        return getPayloadClaimTextValue(getEAAPayload().getEAAVersion());
    }

    /**
     * Gets user's full name when defined within EAA Payload claims
     *
     * @return {@link String}
     */
    public String getHolderFullName() {
        return getPayloadClaimTextValue(getEAAPayload().getHolderFullName());
    }

    /**
     * Gets user's first name when defined within EAA Payload claims
     *
     * @return {@link String}
     */
    public String getHolderFirstName() {
        return getPayloadClaimTextValue(getEAAPayload().getHolderFirstName());
    }

    /**
     * Gets user's last or family name when defined within EAA Payload claims
     *
     * @return {@link String}
     */
    public String getHolderLastName() {
        return getPayloadClaimTextValue(getEAAPayload().getHolderLastName());
    }

    /**
     * Gets user's middle name when defined within EAA Payload claims
     *
     * @return {@link String}
     */
    public String getHolderMiddleName() {
        return getPayloadClaimTextValue(getEAAPayload().getHolderMiddleName());
    }

    /**
     * Gets user's alternative name when defined within EAA Payload claims
     *
     * @return {@link String}
     */
    public String getHolderNickname() {
        return getPayloadClaimTextValue(getEAAPayload().getHolderNickname());
    }

    /**
     * Gets user's preferred or short name when defined within EAA Payload claims
     *
     * @return {@link String}
     */
    public String getHolderShortName() {
        return getPayloadClaimTextValue(getEAAPayload().getHolderShortName());
    }

    /**
     * Gets user's profile URL when defined within EAA Payload claims
     *
     * @return {@link String}
     */
    public String getHolderProfileUrl() {
        return getPayloadClaimTextValue(getEAAPayload().getHolderProfileUrl());
    }

    /**
     * Gets user's picture URL when defined within EAA Payload claims
     *
     * @return {@link String}
     */
    public String getHolderPictureUrl() {
        return getPayloadClaimTextValue(getEAAPayload().getHolderPictureUrl());
    }

    /**
     * Gets user's website when defined within EAA Payload claims
     *
     * @return {@link String}
     */
    public String getHolderWebsiteUrl() {
        return getPayloadClaimTextValue(getEAAPayload().getHolderWebsiteUrl());
    }

    /**
     * Gets user's email when defined within EAA Payload claims
     *
     * @return {@link String}
     */
    public String getHolderEmail() {
        return getPayloadClaimTextValue(getEAAPayload().getHolderEmail());
    }

    /**
     * Gets whether the user's website has been verified if defined within EAA Payload claims
     *
     * @return {@link Boolean}
     */
    public Boolean getHolderEmailVerified() {
        return getPayloadClaimBooleanValue(getEAAPayload().getHolderEmailVerified());
    }

    /**
     * Gets user's gender when defined within EAA Payload claims
     *
     * @return {@link String}
     */
    public String getHolderGender() {
        return getPayloadClaimTextValue(getEAAPayload().getHolderGender());
    }

    /**
     * Gets user's birthdate when defined within EAA Payload claims
     *
     * @return {@link Date}
     */
    public Date getHolderBirthdate() {
        return getPayloadClaimDateValue(getEAAPayload().getHolderBirthdate());
    }

    /**
     * Gets user's timezone when defined within EAA Payload claims
     *
     * @return {@link String}
     */
    public String getHolderTimezone() {
        return getPayloadClaimTextValue(getEAAPayload().getHolderTimezone());
    }

    /**
     * Gets user's locale when defined within EAA Payload claims
     *
     * @return {@link String}
     */
    public String getHolderLocale() {
        return getPayloadClaimTextValue(getEAAPayload().getHolderLocale());
    }

    /**
     * Gets user's full postal address, formatted, when defined within EAA Payload claims
     *
     * @return {@link String}
     */
    public String getHolderPostalAddress() {
        AddressClaimWrapper userAddress = getEAAPayload().getHolderAddress();
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
    public String getHolderAddressCity() {
        AddressClaimWrapper userAddress = getEAAPayload().getHolderAddress();
        if (userAddress != null) {
            return getPayloadClaimTextValue(userAddress.getCity());
        }
        ClaimWrapper residentCity = getEAAPayload().getHolderResidentCity();
        if (residentCity != null) {
            return getPayloadClaimTextValue(residentCity);
        }
        return null;
    }

    /**
     * Gets user's state or region address when defined within EAA Payload claims
     *
     * @return {@link String}
     */
    public String getHolderAddressStateOrProvince() {
        AddressClaimWrapper userAddress = getEAAPayload().getHolderAddress();
        if (userAddress != null) {
            return getPayloadClaimTextValue(userAddress.getStateOrProvince());
        }
        ClaimWrapper residentState = getEAAPayload().getHolderResidentState();
        if (residentState != null) {
            return getPayloadClaimTextValue(residentState);
        }
        return null;
    }

    /**
     * Gets user's postal code address when defined within EAA Payload claims
     *
     * @return {@link String}
     */
    public String getHolderAddressPostalCode() {
        AddressClaimWrapper userAddress = getEAAPayload().getHolderAddress();
        if (userAddress != null) {
            return getPayloadClaimTextValue(userAddress.getPostalCode());
        }
        ClaimWrapper residentPostalCode = getEAAPayload().getHolderResidentPostalCode();
        if (residentPostalCode != null) {
            return getPayloadClaimTextValue(residentPostalCode);
        }
        return null;
    }

    /**
     * Gets user's country address when defined within EAA Payload claims.
     * NOTE: The returned value is usually represented by 2-letter ISO country code.
     *
     * @return {@link String}
     */
    public String getHolderAddressCountry() {
        AddressClaimWrapper userAddress = getEAAPayload().getHolderAddress();
        if (userAddress != null) {
            return getPayloadClaimTextValue(userAddress.getCountry());
        }
        ClaimWrapper residentCountry = getEAAPayload().getHolderResidentCountry();
        if (residentCountry != null) {
            return getPayloadClaimTextValue(residentCountry);
        }
        return null;
    }

    /**
     * Gets user's street address when defined within EAA Payload claims
     *
     * @return {@link String}
     */
    public String getHolderStreetAddress() {
        AddressClaimWrapper userAddress = getEAAPayload().getHolderAddress();
        if (userAddress != null) {
            return getPayloadClaimTextValue(userAddress.getStreetAddress());
        }
        ClaimWrapper residentAddress = getEAAPayload().getHolderResidentAddress();
        if (residentAddress != null) {
            return getPayloadClaimTextValue(residentAddress);
        }
        return null;
    }

    /**
     * Gets user's phone number when defined within EAA Payload claims
     *
     * @return {@link String}
     */
    public String getHolderPhoneNumber() {
        return getPayloadClaimTextValue(getEAAPayload().getHolderPhoneNumber());
    }

    /**
     * Gets whether the user's phone number has been verified if defined within EAA Payload claims
     *
     * @return {@link Boolean}
     */
    public Boolean getHolderPhoneNumberVerified() {
        return getPayloadClaimBooleanValue(getEAAPayload().getHolderPhoneNumberVerified());
    }

    /**
     * Gets user's country of birth when defined within EAA Payload claims
     *
     * @return {@link String}
     */
    public String getHolderPlaceOfBirthCountry() {
        PlaceOfBirthClaimWrapper userPlaceOfBirth = getEAAPayload().getHolderPlaceOfBirth();
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
    public String getHolderPlaceOfBirthRegion() {
        PlaceOfBirthClaimWrapper userPlaceOfBirth = getEAAPayload().getHolderPlaceOfBirth();
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
    public String getHolderPlaceOfBirthCity() {
        PlaceOfBirthClaimWrapper userPlaceOfBirth = getEAAPayload().getHolderPlaceOfBirth();
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
    public List<String> getHolderNationalities() {
        return getPayloadClaimArrayAsStringsValue(getEAAPayload().getHolderNationalities());
    }

    /**
     * Gets user's last or family name at birth when defined within EAA Payload claims
     *
     * @return {@link String}
     */
    public String getHolderBirthLastName() {
        return getPayloadClaimTextValue(getEAAPayload().getHolderBirthLastName());
    }

    /**
     * Gets user's first name at birth when defined within EAA Payload claims
     *
     * @return {@link String}
     */
    public String getHolderBirthFirstName() {
        return getPayloadClaimTextValue(getEAAPayload().getHolderBirthFirstName());
    }

    /**
     * Gets user's middle name at birth when defined within EAA Payload claims
     *
     * @return {@link String}
     */
    public String getHolderBirthMiddleName() {
        return getPayloadClaimTextValue(getEAAPayload().getHolderBirthMiddleName());
    }

    /**
     * Gets user's preferred salutation when defined within EAA Payload claims
     *
     * @return {@link String}
     */
    public String getHolderSalutation() {
        return getPayloadClaimTextValue(getEAAPayload().getHolderSalutation());
    }

    /**
     * Gets the name(s) which holder was born.
     *
     * @return {@link String}
     */
    public String getHolderBirthFullName() {
        return getPayloadClaimTextValue(getEAAPayload().getHolderBirthFullName());
    }

    /**
     * Gets user's title when defined within EAA Payload claims
     *
     * @return {@link String}
     */
    public String getHolderTitle() {
        return getPayloadClaimTextValue(getEAAPayload().getHolderTitle());
    }

    /**
     * Gets user's mobile phone number when defined within EAA Payload claims
     *
     * @return {@link String}
     */
    public String getHolderMobilePhoneNumber() {
        return getPayloadClaimTextValue(getEAAPayload().getHolderMobilePhoneNumber());
    }

    /**
     * Gets user's scenic name or pseudonym, they are known as, when defined within EAA Payload claims
     *
     * @return {@link String}
     */
    public String getHolderPseudonym() {
        return getPayloadClaimTextValue(getEAAPayload().getHolderPseudonym());
    }

    /* mdoc claims */

    /**
     * Gets issuing authority name.
     * The value shall only use latin1 characters and shall have a maximum length of 150 characters.
     *
     * @return {@link String}
     */
    public String getDocumentIssuingAuthority() {
        return getPayloadClaimTextValue(getEAAPayload().getDocumentIssuingAuthority());
    }

    /**
     * Gets alpha-2 country code, as defined in ISO 3166-1, of the issuing authority’s country or territory
     *
     * @return {@link String}
     */
    public String getDocumentIssuingAuthorityCountry() {
        return getPayloadClaimTextValue(getEAAPayload().getDocumentIssuingAuthorityCountry());
    }

    /**
     * Gets a country subdivision code of the jurisdiction that issued the mDL as defined in
     * ISO 3166-2:2020, Clause 8. The first part of the code shall be the same as the value for issuing_country.
     *
     * @return {@link String}
     */
    public String getDocumentIssuingAuthorityJurisdiction() {
        return getPayloadClaimTextValue(getEAAPayload().getDocumentIssuingAuthorityJurisdiction());
    }

    /**
     * An audit control number assigned by the issuing authority.
     * The value shall only use latin1 characters and shall have a maximum length of 150 characters.
     *
     * @return {@link String}
     */
    public String getDocumentIssuingAuthorityAdministrativeNumber() {
        return getPayloadClaimTextValue(getEAAPayload().getDocumentIssuingAuthorityAdministrativeNumber());
    }

    /**
     * Gets the distinguishing sign of the issuing country according to ISO/IEC 18013-1:2018, Annex F.
     * If no applicable distinguishing sign is available in ISO/IEC 18013-1, an IA may
     * use an empty identifier or another identifier by which it is internationally recognized.
     * In this case the IA should ensure there is no collision with other IA’s.
     *
     * @return {@link String}
     */
    public String getDocumentIssuingAuthorityCountryUNDistinguishingSign() {
        return getPayloadClaimTextValue(getEAAPayload().getDocumentIssuingAuthorityUNDistinguishingSign());
    }

    /**
     * Gets the number assigned or calculated by the issuing authority.
     * The value shall only use latin1 characters and shall have a maximum length of 150 characters.
     *
     * @return {@link String}
     */
    public String getDocumentNumber() {
        return getPayloadClaimTextValue(getEAAPayload().getDocumentNumber());
    }

    /**
     * Gets the document type.
     *
     * @return {@link String}
     */
    public String getDocumentType() {
        // TODO : see if to use a single getter or two separate ones
        String docType = getPayloadClaimTextValue(getEAAPayload().getEAADocType());
        if (docType != null) {
            return docType;
        }
        return getPayloadClaimTextValue(getEAAPayload().getDocumentType());
    }

    /**
     * Gets a reproduction of the mDL holder’s portrait.
     *
     * @return byte array
     */
    public byte[] getHolderPortrait() {
        return getPayloadClaimByteValue(getEAAPayload().getHolderPortrait());
    }

    /**
     * Gets the categories of vehicles/restrictions/conditions contain information describing the driving privileges
     * of the mDL holder.
     *
     * @return {@link DrivingPrivilegesClaimWrapper}
     */
    public DrivingPrivilegesClaimWrapper getHolderDrivingPrivileges() {
        return getEAAPayload().getHolderDrivingPrivileges();
    }

    /**
     * Gets the holder’s height in centimetres
     *
     * @return {@link Integer}
     */
    public Integer getHolderHeight() {
        return getPayloadClaimIntegerValue(getEAAPayload().getHolderHeight());
    }

    /**
     * Gets the holder’s height in centimetres
     *
     * @return {@link Integer}
     */
    public Integer getHolderWeight() {
        return getPayloadClaimIntegerValue(getEAAPayload().getHolderWeight());
    }

    /**
     * Gets the mDL holder’s eye colour. The value shall be one of the following: “black”, “blue”,
     * “brown”, “dichromatic”, “grey”, “green”, “hazel”, “maroon”, “pink”, “unknown”.
     *
     * @return {@link String}
     */
    public String getHolderEyeColor() {
        return getPayloadClaimTextValue(getEAAPayload().getHolderEyeColor());
    }

    /**
     * Gets the mDL holder’s hair colour. The value shall be one of the following: “bald”, “black”,
     * “blond”, “brown”, “grey”, “red”, “auburn”, “sandy”, “white”, “unknown”.
     *
     * @return {@link String}
     */
    public String getHolderHairColor() {
        return getPayloadClaimTextValue(getEAAPayload().getHolderHairColor());
    }

    /**
     * Gets the date when portrait was taken.
     *
     * @return {@link Date}
     */
    public Date getHolderPortraitCaptureDate() {
        return getPayloadClaimDateValue(getEAAPayload().getHolderPortraitCaptureDate());
    }

    /**
     * Gets the date the age of the mDL holder
     *
     * @return {@link Integer}
     */
    public Integer getHolderAgeInYears() {
        return getPayloadClaimIntegerValue(getEAAPayload().getHolderAgeInYears());
    }

    /**
     * Gets the year when the mDL holder was born
     *
     * @return {@link Integer}
     */
    public Integer getHolderAgeBirthYear() {
        return getPayloadClaimIntegerValue(getEAAPayload().getHolderAgeBirthYear());
    }

    /**
     * Returns the claim value whether the age of an EAA's holder is over the {@code age}.
     * NOTE: if there is no claim provided for the requested age, NULL is returned.
     *
     * @param age integer age to verify against
     * @return {@link Boolean}
     */
    public Boolean isHolderAgeOver(int age) {
        List<AgeOverNNClaimWrapper> ageOverList = getEAAPayload().getHolderAgeOverList();
        if (ageOverList != null && !ageOverList.isEmpty()) {
            for (AgeOverNNClaimWrapper ageOverNNClaim : ageOverList) {
                if (age == ageOverNNClaim.getAge()) {
                    return getPayloadClaimBooleanValue(ageOverNNClaim);
                }
            }
        }
        return null;
    }

    /**
     * Returns the biometric template for thr given value.
     * The list of supported values is defined in ISO/IEC 18013-2:2020.
     * NOTE: if there is no claim provided for the requested type, NULL is returned.
     *
     * @param type {@link String} type to get biometric template for
     * @return byte array
     */
    public byte[] getHolderBiometricTemplate(String type) {
        if (type == null) {
            return null;
        }
        /*
         * A biometric template identifier has the format biometric_template_xx 
         * where xx shall be replaced with the corresponding “Abstract value name” found in ISO/IEC 19785-3:2020, 
         * Table 7, according to the following convention: capitalized characters are replaced with their 
         * lowercase equivalent and spaces or non-alphanumeric characters are replaced by underscores (_).
         */
        type = normalizeType(type);
        List<BiometricTemplateXXClaimWrapper> biometricTemplateList = getEAAPayload().getHolderBiometricTemplateList();
        if (biometricTemplateList != null && !biometricTemplateList.isEmpty()) {
            for (BiometricTemplateXXClaimWrapper biometricTemplate : biometricTemplateList) {
                if (type.equals(normalizeType(biometricTemplate.getType()))) {
                    return getPayloadClaimByteValue(biometricTemplate);
                }
            }
        }
        return null;
    }
    
    private String normalizeType(String type) {
        if (type == null) {
            return null;
        }
        type = type.toLowerCase();
        return type.replaceAll("[^\\p{L}\\p{Nd}]+", "_");
    }

    /**
     * Gets an image of the signature or usual mark of the mDL holder, see 7.2.7 ISO/IEC 18013-5.
     *
     * @return byte array
     */
    public byte[] getHolderSignatureUsualMark() {
        return getPayloadClaimByteValue(getEAAPayload().getHolderSignatureUsualMark());
    }

    /**
     * Gets a reproduction of the holder’s fingerprint data (TBC).
     *
     * @return byte array
     */
    public byte[] getHolderFingerprint() {
        return getPayloadClaimByteValue(getEAAPayload().getHolderFingerprint());
    }

    /**
     * Gets a business name of the holder.
     *
     * @return {@link String}
     */
    public String getHolderBusinessName() {
        return getPayloadClaimTextValue(getEAAPayload().getHolderBusinessName());
    }

    /**
     * Gets a name of legal person.
     *
     * @return {@link String}
     */
    public String getHolderOrganizationName() {
        return getPayloadClaimTextValue(getEAAPayload().getHolderOrganizationName());
    }

    /**
     * Gets the profession of the holder.
     *
     * @return {@link String}
     */
    public String getHolderProfession() {
        return getPayloadClaimTextValue(getEAAPayload().getHolderProfession());
    }

    /**
     * Gets the father of the holder
     *
     * @return {@link String}
     */
    public String getHolderRelationshipFather() {
        return getPayloadClaimTextValue(getEAAPayload().getHolderRelationshipFather());
    }

    /**
     * Gets the mother of the holder
     *
     * @return {@link String}
     */
    public String getHolderRelationshipMother() {
        return getPayloadClaimTextValue(getEAAPayload().getHolderRelationshipMother());
    }

    /**
     * Gets the parent of the holder
     *
     * @return {@link String}
     */
    public String getHolderRelationshipParent() {
        return getPayloadClaimTextValue(getEAAPayload().getHolderRelationshipParent());
    }

    /**
     * Gets the son of the holder
     *
     * @return {@link String}
     */
    public String getHolderRelationshipSon() {
        return getPayloadClaimTextValue(getEAAPayload().getHolderRelationshipSon());
    }

    /**
     * Gets the daughter of the holder
     *
     * @return {@link String}
     */
    public String getHolderRelationshipDaughter() {
        return getPayloadClaimTextValue(getEAAPayload().getHolderRelationshipDaughter());
    }

    /**
     * Gets the brother of the holder
     *
     * @return {@link String}
     */
    public String getHolderRelationshipBrother() {
        return getPayloadClaimTextValue(getEAAPayload().getHolderRelationshipBrother());
    }

    /**
     * Gets the sister of the holder
     *
     * @return {@link String}
     */
    public String getHolderRelationshipSister() {
        return getPayloadClaimTextValue(getEAAPayload().getHolderRelationshipSister());
    }

    /**
     * Gets the sibling of the holder
     *
     * @return {@link String}
     */
    public String getHolderRelationshipSibling() {
        return getPayloadClaimTextValue(getEAAPayload().getHolderRelationshipSibling());
    }

    /**
     * Gets the spouse of the holder
     *
     * @return {@link String}
     */
    public String getHolderRelationshipSpouse() {
        return getPayloadClaimTextValue(getEAAPayload().getHolderRelationshipSpouse());
    }

    /**
     * Gets the father-in-law of the holder
     *
     * @return {@link String}
     */
    public String getHolderRelationshipFatherInLaw() {
        return getPayloadClaimTextValue(getEAAPayload().getHolderRelationshipFatherInLaw());
    }

    /**
     * Gets the mother-in-law of the holder
     *
     * @return {@link String}
     */
    public String getHolderRelationshipMotherInLaw() {
        return getPayloadClaimTextValue(getEAAPayload().getHolderRelationshipMotherInLaw());
    }

    /**
     * Gets the parent-in-law of the holder
     *
     * @return {@link String}
     */
    public String getHolderRelationshipParentInLaw() {
        return getPayloadClaimTextValue(getEAAPayload().getHolderRelationshipParentInLaw());
    }

    /**
     * Gets the son-in-law of the holder
     *
     * @return {@link String}
     */
    public String getHolderRelationshipSonInLaw() {
        return getPayloadClaimTextValue(getEAAPayload().getHolderRelationshipSonInLaw());
    }

    /**
     * Gets the daughter-in-law of the holder
     *
     * @return {@link String}
     */
    public String getHolderRelationshipDaughterInLaw() {
        return getPayloadClaimTextValue(getEAAPayload().getHolderRelationshipDaughterInLaw());
    }

    /**
     * Gets the child-in-law of the holder
     *
     * @return {@link String}
     */
    public String getHolderRelationshipChildInLaw() {
        return getPayloadClaimTextValue(getEAAPayload().getHolderRelationshipChildInLaw());
    }

    /**
     * Gets the parental authority of the holder
     *
     * @return {@link String}
     */
    public String getHolderRelationshipParentalAuthority() {
        return getPayloadClaimTextValue(getEAAPayload().getHolderRelationshipParentalAuthority());
    }

    /**
     * Gets the legal representative of the holder
     *
     * @return {@link String}
     */
    public String getHolderRelationshipLegalRepresentative() {
        return getPayloadClaimTextValue(getEAAPayload().getHolderRelationshipLegalRepresentative());
    }

    /**
     * Gets the voluntary agent of the holder
     *
     * @return {@link String}
     */
    public String getHolderRelationshipAgent() {
        return getPayloadClaimTextValue(getEAAPayload().getHolderRelationshipAgent());
    }

    /**
     * Gets the date when the data (e.g. a PID) was issued
     *
     * @return {@link Date}
     */
    public Date getAdministrativeIssuanceDate() {
        return getPayloadClaimDateValue(getEAAPayload().getAdministrativeIssuanceDate());
    }

    /**
     * Gets the date when the data (e.g. a PID) will expire
     *
     * @return {@link Date}
     */
    public Date getAdministrativeExpirationDate() {
        return getPayloadClaimDateValue(getEAAPayload().getAdministrativeExpirationDate());
    }

    /**
     * Gets the URL at which a machine-readable version of the trust anchor to be used for
     * verifying the PID can be found or looked up.
     *
     * @return {@link String}
     */
    public String getTrustAnchor() {
        return getPayloadClaimTextValue(getEAAPayload().getTrustAnchor());
    }

    /**
     * Gets the name of the street where the user to whom the person identification data relates currently resides.
     *
     * @return {@link String}
     */
    public String getResidentStreet() {
        return getPayloadClaimTextValue(getEAAPayload().getResidentStreet());
    }

    /**
     * Gets the house number where the user to whom the person identification data relates currently resides,
     * including any affix or suffix.
     *
     * @return {@link String}
     */
    public String getResidentHouseNumber() {
        return getPayloadClaimTextValue(getEAAPayload().getResidentHouseNumber());
    }

    /* ETSI TS 119 472-1 "5 Implementation of EAA based on SD-JWT VC" header parameters */

    /**
     * Gets the registration identifier of the legal entity on whose behalf the EAA has been issued.
     *
     * @return {@link String}
     */
    public String getIssuingRegistrationIdentifier() {
        return getPayloadClaimTextValue(getEAAPayload().getIssuingRegistrationIdentifier());
    }

    /**
     * Gets the signal indicating that the EAA shall be used only once, and that it shall not be retained for future use.
     *
     * @return {@link Boolean}
     */
    public Boolean getOneTimeUse() {
        return getPayloadClaimBooleanValue(getEAAPayload().getOneTimeUse());
    }

    /**
     * Gets the EAA short-lived component indicating that the validity period of the EAA is so short that
     * it shall not be necessary to check its revocation status.
     *
     * @return {@link Boolean}
     */
    public Boolean getShortLived() {
        return getPayloadClaimBooleanValue(getEAAPayload().getShortLived());
    }

    /**
     * Gets the identifier of the attribute subject, which shall associate the attributes to this attribute subject
     *
     * @return {@link String}
     */
    public String getAttestedAttributesSubjectId() {
        AttestedAttributesSubjectClaimWrapper attestedAttributesSubject = getEAAPayload().getAttestedAttributesSubject();
        if (attestedAttributesSubject != null) {
            return getPayloadClaimTextValue(attestedAttributesSubject.getSubjectId());
        }
        return null;
    }

    /**
     * Gets the family name of the attribute subject, which shall associate the attributes to this attribute subject
     *
     * @return {@link String}
     */
    public String getAttestedAttributesSubjectFamilyName() {
        AttestedAttributesSubjectClaimWrapper attestedAttributesSubject = getEAAPayload().getAttestedAttributesSubject();
        if (attestedAttributesSubject != null && attestedAttributesSubject.getSubjectId() != null) {
            return getPayloadClaimTextValue(attestedAttributesSubject.getSubjectId().getFamilyName());
        }
        return null;
    }

    /**
     * Gets the given name of the attribute subject, which shall associate the attributes to this attribute subject
     *
     * @return {@link String}
     */
    public String getAttestedAttributesSubjectGivenName() {
        AttestedAttributesSubjectClaimWrapper attestedAttributesSubject = getEAAPayload().getAttestedAttributesSubject();
        if (attestedAttributesSubject != null && attestedAttributesSubject.getSubjectId() != null) {
            return getPayloadClaimTextValue(attestedAttributesSubject.getSubjectId().getGivenName());
        }
        return null;
    }

    /**
     * Gets the document number of the attribute subject, which shall associate the attributes to this attribute subject
     *
     * @return {@link String}
     */
    public String getAttestedAttributesSubjectDocumentNumber() {
        AttestedAttributesSubjectClaimWrapper attestedAttributesSubject = getEAAPayload().getAttestedAttributesSubject();
        if (attestedAttributesSubject != null && attestedAttributesSubject.getSubjectId() != null) {
            return getPayloadClaimTextValue(attestedAttributesSubject.getSubjectId().getDocumentNumber());
        }
        return null;
    }

    /**
     * Gets the claim for associating a set of attributes to one entity different than the EAA subject.
     *
     * @return {@link String}
     */
    public String getAttestedAttributesSubjectPseudonym() {
        AttestedAttributesSubjectClaimWrapper attestedAttributesSubject = getEAAPayload().getAttestedAttributesSubject();
        if (attestedAttributesSubject != null) {
            return getPayloadClaimTextValue(attestedAttributesSubject.getSubjectPseudonym());
        }
        return null;
    }

    /**
     * Gets the attributes associated to the attribute subject whose identifier appears in the sub_id member or
     * whose pseudonym appears in the sub_aka member.
     *
     * @return {@link String}
     */
    public List<String> getAttestedAttributes() {
        AttestedAttributesSubjectClaimWrapper attestedAttributesSubject = getEAAPayload().getAttestedAttributesSubject();
        if (attestedAttributesSubject != null) {
            return getPayloadClaimArrayAsStringsValue(attestedAttributesSubject.getAttributes());
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
        if (eaa != null && !eaaPayloadClaims.isEmpty()) {
            for (ClaimWrapper claim : eaaPayloadClaims) {
                if (headerName.equals(claim.getName())) {
                    return claim;
                }
            }
        }
        return null;
    }

    /**
     * This method returns all claims that have been selectively disclosed and identified on the EAA
     * (i.e. provided in the form of disclosures).
     *
     * @return a list of {@link ClaimWrapper}s
     */
    public List<ClaimWrapper> getSelectivelyDisclosableClaims() {
        final List<ClaimWrapper> result = new ArrayList<>();
        List<ClaimWrapper> eaaPayloadClaims = getAllEAAPayloadClaims();
        if (eaa != null && !eaaPayloadClaims.isEmpty()) {
            for (ClaimWrapper claim : eaaPayloadClaims) {
                result.addAll(getSelectivelyDisclosableClaimsRecursively(claim));
            }
        }
        return result;
    }

    public List<ClaimWrapper> getSelectivelyDisclosableClaimsRecursively(ClaimWrapper claimWrapper) {
        List<ClaimWrapper> result = new ArrayList<>();
        if (claimWrapper.isSelectivelyDisclosable()) {
            result.add(claimWrapper);
        }
        if (claimWrapper.isList()) {
            for (ClaimWrapper listItem : claimWrapper.getList()) {
                result.addAll(getSelectivelyDisclosableClaimsRecursively(listItem));
            }
        } else if (claimWrapper.isMap()) {
            for (ClaimWrapper entryItem : claimWrapper.getMap().values()) {
                result.addAll(getSelectivelyDisclosableClaimsRecursively(entryItem));
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

    private Integer getPayloadClaimIntegerValue(ClaimWrapper xmlDisclosableClaim) {
        BigInteger bigIntegerValue = getPayloadClaimNumberValue(xmlDisclosableClaim);
        if (bigIntegerValue != null) {
            return bigIntegerValue.intValue();
        }
        return null;
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
        if (xmlDisclosableClaim.isNull()) {
            return true; // handle as a true flag
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

    private byte[] getPayloadClaimByteValue(ClaimWrapper xmlDisclosableClaim) {
        if (xmlDisclosableClaim == null) {
            return null;
        }
        return xmlDisclosableClaim.getBinary();
    }

    /**
     * Gets a list of all disclosable claims present within an EAA Payload
     * NOTE: The method retrieves claims from the root payload level only
     *
     * @return a list of {@link ClaimWrapper}s
     */
    public List<ClaimWrapper> getAllEAAPayloadClaims() {
        return getEAAPayload().getAllEAAPayloadClaims();
    }

    /**
     * Gets a list of names (keys) for all disclosable claims present within an EAA Payload
     * NOTE: The method retrieves names from the root payload level only
     *
     * @return a list of {@link ClaimWrapper}s
     */
    public List<String> getAllEAAPayloadClaimNames() {
        return getAllEAAPayloadClaims().stream().map(ClaimWrapper::getName).collect(Collectors.toList());
    }

    /**
     * Gets type of the EAA
     *
     * @return {@link EAAType}
     */
    public EAAType getEAAType() {
        return eaa.getEAAType();
    }

}
