package eu.europa.esig.dss.eaa.common.validation;

import eu.europa.esig.dss.diagnostic.jaxb.XmlAddressClaim;
import eu.europa.esig.dss.diagnostic.jaxb.XmlAgeOverNNClaim;
import eu.europa.esig.dss.diagnostic.jaxb.XmlAttestedAttributesSubjectClaim;
import eu.europa.esig.dss.diagnostic.jaxb.XmlAttestedAttributesSubjectIdClaim;
import eu.europa.esig.dss.diagnostic.jaxb.XmlBiometricTemplateXXClaim;
import eu.europa.esig.dss.diagnostic.jaxb.XmlClaim;
import eu.europa.esig.dss.diagnostic.jaxb.XmlCredentialSubjectClaim;
import eu.europa.esig.dss.diagnostic.jaxb.XmlDeviceKeyClaim;
import eu.europa.esig.dss.diagnostic.jaxb.XmlDiagnosticData;
import eu.europa.esig.dss.diagnostic.jaxb.XmlDigestMatcher;
import eu.europa.esig.dss.diagnostic.jaxb.XmlDisclosableClaim;
import eu.europa.esig.dss.diagnostic.jaxb.XmlDrivingPrivilegeClaim;
import eu.europa.esig.dss.diagnostic.jaxb.XmlDrivingPrivilegeCodeClaim;
import eu.europa.esig.dss.diagnostic.jaxb.XmlDrivingPrivilegeCodesClaim;
import eu.europa.esig.dss.diagnostic.jaxb.XmlDrivingPrivilegesClaim;
import eu.europa.esig.dss.diagnostic.jaxb.XmlEAADocument;
import eu.europa.esig.dss.diagnostic.jaxb.XmlEAAPayload;
import eu.europa.esig.dss.diagnostic.jaxb.XmlEAAPresentationInfo;
import eu.europa.esig.dss.diagnostic.jaxb.XmlEAASignature;
import eu.europa.esig.dss.diagnostic.jaxb.XmlEAA;
import eu.europa.esig.dss.diagnostic.jaxb.XmlIntegrityClaim;
import eu.europa.esig.dss.diagnostic.jaxb.XmlKeyBindingSignature;
import eu.europa.esig.dss.diagnostic.jaxb.XmlMetadataTypeClaim;
import eu.europa.esig.dss.diagnostic.jaxb.XmlPlaceOfBirthClaim;
import eu.europa.esig.dss.diagnostic.jaxb.XmlSignature;
import eu.europa.esig.dss.diagnostic.jaxb.XmlStatusClaim;
import eu.europa.esig.dss.diagnostic.jaxb.XmlStatusListClaim;
import eu.europa.esig.dss.diagnostic.jaxb.XmlValidityInfoClaim;
import eu.europa.esig.dss.diagnostic.jaxb.XmlX509Certificate;
import eu.europa.esig.dss.model.Digest;
import eu.europa.esig.dss.model.ReferenceValidation;
import eu.europa.esig.dss.model.eaa.DisclosureValidation;
import eu.europa.esig.dss.model.eaa.claim.Claim;
import eu.europa.esig.dss.model.eaa.claim.ClaimAddress;
import eu.europa.esig.dss.model.eaa.claim.ClaimAgeOverNN;
import eu.europa.esig.dss.model.eaa.claim.ClaimAttestedAttributesSubject;
import eu.europa.esig.dss.model.eaa.claim.ClaimAttestedAttributesSubjectId;
import eu.europa.esig.dss.model.eaa.claim.ClaimBiometricTemplateXX;
import eu.europa.esig.dss.model.eaa.claim.ClaimCredentialSubject;
import eu.europa.esig.dss.model.eaa.claim.ClaimDeviceKey;
import eu.europa.esig.dss.model.eaa.claim.ClaimDrivingPrivilege;
import eu.europa.esig.dss.model.eaa.claim.ClaimDrivingPrivilegeCode;
import eu.europa.esig.dss.model.eaa.claim.ClaimDrivingPrivilegeCodes;
import eu.europa.esig.dss.model.eaa.claim.ClaimDrivingPrivileges;
import eu.europa.esig.dss.model.eaa.claim.ClaimIntegrity;
import eu.europa.esig.dss.model.eaa.claim.ClaimPlaceOfBirth;
import eu.europa.esig.dss.model.eaa.claim.ClaimStatus;
import eu.europa.esig.dss.model.eaa.claim.ClaimStatusList;
import eu.europa.esig.dss.model.eaa.claim.ClaimString;
import eu.europa.esig.dss.model.eaa.claim.ClaimValidityInfo;
import eu.europa.esig.dss.model.x509.CertificateToken;
import eu.europa.esig.dss.spi.eaa.EAAPayload;
import eu.europa.esig.dss.spi.eaa.EAAPresentation;
import eu.europa.esig.dss.spi.eaa.EAA;
import eu.europa.esig.dss.spi.signature.AdvancedSignature;
import eu.europa.esig.dss.utils.Utils;
import eu.europa.esig.dss.validation.reports.diagnostic.SignedDocumentDiagnosticDataBuilder;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Builds DiagnosticData for a presentation of Electronic Attestation of Attributes validation
 *
 */
public class EAAPresentationDiagnosticDataBuilder extends SignedDocumentDiagnosticDataBuilder {

    /** The EAA presentation */
    protected EAAPresentation eaaPresentation;

    /** Builder used to build a signature object */
    private SignedDocumentDiagnosticDataBuilder signatureDiagnosticDataBuilder;

    /** The cached map of EAAs */
    protected Map<String, XmlEAA> xmlEAAMap = new HashMap<>();

    /**
     * Default constructor
     */
    public EAAPresentationDiagnosticDataBuilder() {
        // empty
    }

    /**
     * Sets found EAA presentation
     *
     * @param eaaPresentation {@code EAAPresentation}
     * @return this builder
     */
    public EAAPresentationDiagnosticDataBuilder foundEAAPresentation(EAAPresentation eaaPresentation) {
        this.eaaPresentation = eaaPresentation;
        return this;
    }
    /**
     * Sets a builder for a signature object
     *
     * @param signatureDiagnosticDataBuilder {@link SignedDocumentDiagnosticDataBuilder}
     * @return {@link EAAPresentationDiagnosticDataBuilder}
     */
    public EAAPresentationDiagnosticDataBuilder setSignatureDiagnosticDataBuilder(SignedDocumentDiagnosticDataBuilder signatureDiagnosticDataBuilder) {
        this.signatureDiagnosticDataBuilder = signatureDiagnosticDataBuilder;
        return this;
    }

    @Override
    public XmlDiagnosticData build() {
        XmlDiagnosticData xmlDiagnosticData = super.build();
        if (eaaPresentation != null) {
            xmlDiagnosticData.setEAAPresentationInfo(buildXmlEAAPresentationInfo(eaaPresentation));
            List<EAA> eaas = eaaPresentation.getElectronicAttestationsOfAttributes();
            Collection<XmlEAA> xmlEAAs = buildXmlEAA(eaas);
            xmlDiagnosticData.getEAAs().addAll(xmlEAAs);
        }
        return xmlDiagnosticData;
    }

    /**
     * Builds {@code XmlEAAPresentationInfo} based on the {@code EAAPresentation}
     *
     * @param eaaPresentation {@link EAAPresentation}
     * @return {@link XmlEAAPresentationInfo}
     */
    protected XmlEAAPresentationInfo buildXmlEAAPresentationInfo(EAAPresentation eaaPresentation) {
        final XmlEAAPresentationInfo xmlEAAPresentationInfo = new XmlEAAPresentationInfo();
        xmlEAAPresentationInfo.setEAAPresentationType(eaaPresentation.getEAAPresentationType());
        if (Utils.isCollectionNotEmpty(eaaPresentation.getElectronicAttestationsOfAttributes())) {
            for (EAA eaa : eaaPresentation.getElectronicAttestationsOfAttributes()) {
                xmlEAAPresentationInfo.getDocuments().add(buildXmlEAADocument(eaa));
            }
        }
        return xmlEAAPresentationInfo;
    }

    /**
     * Builds an instance of {@code XmlEAADocument} for a {@code ElectronicAttestationOfAttributes}
     *
     * @param eaa {@link EAA}
     * @return {@link XmlEAADocument}
     */
    protected XmlEAADocument buildXmlEAADocument(EAA eaa) {
        final XmlEAADocument xmlEAADocument = new XmlEAADocument();
        xmlEAADocument.setEAA(getXmlEAA(eaa));
        return xmlEAADocument;
    }

    private Collection<XmlEAA> buildXmlEAA(Collection<EAA> eaas) {
        List<XmlEAA> builtEAAPresentations = new ArrayList<>();
        for (EAA eaa : eaas) {
            XmlEAA xmlEAAPresentation = getXmlEAA(eaa);
            builtEAAPresentations.add(xmlEAAPresentation);
        }
        return builtEAAPresentations;
    }

    private XmlEAA getXmlEAA(EAA eaa) {
        return xmlEAAMap.computeIfAbsent(eaa.getId(), k -> buildDetachedXmlEAA(eaa));
    }

    /**
     * Builds an {@code XmlEAA} instance
     *
     * @param eaa {@link EAA}
     * @return {@link XmlEAA}
     */
    protected XmlEAA buildDetachedXmlEAA(EAA eaa) {
        final XmlEAA xmlEAAPresentation = new XmlEAA();
        xmlEAAPresentation.setId(eaa.getId());
        xmlEAAPresentation.setDocumentName(eaa.getFilename());
        xmlEAAPresentation.setEAAType(eaa.getEAAType());
        for (AdvancedSignature signature : eaa.getSignatures()) {
            xmlEAAPresentation.getEAASignature().add(getXmlEAASignature(signature));
        }
        xmlEAAPresentation.setDigestMatchers(buildXmlDigestMatchers(eaa.getDisclosureValidations()));
        if (eaa.getKeyBindingSignature() != null) {
            xmlEAAPresentation.setKeyBindingSignature(getXmlKeyBindingSignature(eaa.getKeyBindingSignature()));
        }
        xmlEAAPresentation.setEAAPayload(getXmlEAAPayload(eaa.getPayload()));
        return xmlEAAPresentation;
    }

    private XmlEAASignature getXmlEAASignature(AdvancedSignature signature) {
        XmlEAASignature xmlEAAPresentationSignature = new XmlEAASignature();
        XmlSignature xmlSignature = xmlSignaturesMap.get(signature.getId());
        if (xmlSignature == null) {
            throw new IllegalStateException(String.format(
                    "XmlSignature shall be built at this moment! Not found signature with id '%s'.", signature.getId()));
        }
        xmlEAAPresentationSignature.setSignature(xmlSignature);
        return xmlEAAPresentationSignature;
    }

    private XmlKeyBindingSignature getXmlKeyBindingSignature(AdvancedSignature signature) {
        XmlKeyBindingSignature xmlKeyBindingSignature = new XmlKeyBindingSignature();
        XmlSignature xmlSignature = xmlSignaturesMap.get(signature.getId());
        if (xmlSignature == null) {
            throw new IllegalStateException(String.format("XmlSignature for key binding shall be built at this moment! " +
                    "Not found signature with id '%s'.", signature.getId()));
        }
        xmlSignature.setKeyBindingSignature(signature.isKeyBindingSignature());
        xmlKeyBindingSignature.setSignature(xmlSignature);
        return xmlKeyBindingSignature;
    }

    private List<XmlDigestMatcher> buildXmlDigestMatchers(List<DisclosureValidation> disclosureValidations) {
        if (Utils.isCollectionEmpty(disclosureValidations)) {
            return Collections.emptyList();
        }
        final List<XmlDigestMatcher> result = new ArrayList<>();
        for (DisclosureValidation validation : disclosureValidations) {
            buildXmlDigestMatcherRecursively(validation, result);
        }
        return result;
    }

    private void buildXmlDigestMatcherRecursively(DisclosureValidation disclosureValidation, List<XmlDigestMatcher> digestMatchersList) {
        XmlDigestMatcher ref = getXmlDigestMatcher(disclosureValidation);
        digestMatchersList.add(ref);

        if (Utils.isCollectionNotEmpty(disclosureValidation.getDependentValidations())) {
            for (ReferenceValidation refValidation : disclosureValidation.getDependentValidations()) {
                if (!(refValidation instanceof DisclosureValidation)) {
                    throw new IllegalStateException("DisclosureValidation's dependent validations shall be of DisclosureValidation type!");
                }
                buildXmlDigestMatcherRecursively((DisclosureValidation) refValidation, digestMatchersList);
            }
        }
    }

    /**
     * Builds {@code XmlDigestMatcher} from the {@code DisclosureValidation}
     *
     * @param disclosureValidation {@link DisclosureValidation}
     * @return {@link XmlDigestMatcher}
     */
    protected XmlDigestMatcher getXmlDigestMatcher(DisclosureValidation disclosureValidation) {
        XmlDigestMatcher ref = new XmlDigestMatcher();
        ref.setType(disclosureValidation.getType());
        ref.setDisclosableClaim(getXmlDisclosableClaim(disclosureValidation));
        Digest digest = disclosureValidation.getDigest();
        if (digest != null) {
            ref.setDigestValue(digest.getValue());
            ref.setDigestMethod(digest.getAlgorithm());
        }
        ref.setDataFound(disclosureValidation.isFound());
        ref.setDataIntact(disclosureValidation.isIntact());
        return ref;
    }

    /**
     * Builds {@code XmlDisclosableClaim} from the {@code DisclosureValidation}
     *
     * @param disclosureValidation {@link DisclosureValidation}
     * @return {@link XmlDisclosableClaim}
     */
    protected XmlDisclosableClaim getXmlDisclosableClaim(DisclosureValidation disclosureValidation) {
        if (disclosureValidation == null || (disclosureValidation.getClaimName() == null && disclosureValidation.getValue() == null
                && disclosureValidation.getNamespace() == null && disclosureValidation.getDigestId() == null)) {
            return null;
        }
        XmlDisclosableClaim xmlClaim = new XmlDisclosableClaim();
        if (disclosureValidation.getDigestId() != null) {
            xmlClaim.setId(BigInteger.valueOf(disclosureValidation.getDigestId()));
        }
        xmlClaim.setName(disclosureValidation.getClaimName());
        xmlClaim.setNamespace(disclosureValidation.getNamespace());
        if (disclosureValidation.getValue() != null) {
            xmlClaim.setValue(disclosureValidation.getValue().getValueAsString());
        }
        return xmlClaim;
    }

    private XmlEAAPayload getXmlEAAPayload(EAAPayload eaaPayload) {
        final List<XmlClaim> supportedClaims = new ArrayList<>();
        final XmlEAAPayload xmlEAAPayload = new XmlEAAPayload();

        xmlEAAPayload.setIdentifier(getXmlClaim(eaaPayload.getIdentifier(), supportedClaims));
        xmlEAAPayload.setIssuer(getXmlClaim(eaaPayload.getIssuer(), supportedClaims));
        xmlEAAPayload.setSubject(getXmlClaim(eaaPayload.getSubject(), supportedClaims));
        xmlEAAPayload.setAudience(getXmlClaim(eaaPayload.getAudience(), supportedClaims));
        xmlEAAPayload.setNotAfter(getXmlClaim(eaaPayload.getNotAfterTime(), supportedClaims));
        xmlEAAPayload.setNotBefore(getXmlClaim(eaaPayload.getNotBeforeTime(), supportedClaims));
        xmlEAAPayload.setIssuedAt(getXmlClaim(eaaPayload.getIssuedAtTime(), supportedClaims));
        xmlEAAPayload.setUpdatedAt(getXmlClaim(eaaPayload.getUpdatedAtTime(), supportedClaims));
        xmlEAAPayload.setCategory(getXmlClaim(eaaPayload.getCategory(), supportedClaims));
        xmlEAAPayload.setMetadataType(getXmlMetadataType(eaaPayload, supportedClaims));
        xmlEAAPayload.setStatus(getXmlStatus(eaaPayload.getStatus(), supportedClaims));
        xmlEAAPayload.setNonce(getXmlClaim(eaaPayload.getNonce(), supportedClaims));
        xmlEAAPayload.setDeviceKey(getXmlDeviceKeyClaim(eaaPayload.getDeviceKey(), supportedClaims));

        xmlEAAPayload.setVersion(getXmlClaim(eaaPayload.getVersion(), supportedClaims));
        xmlEAAPayload.setDocType(getXmlClaim(eaaPayload.getDocType(), supportedClaims));
        xmlEAAPayload.setValidityInfo(getXmlValidityInfoClaim(eaaPayload.getValidityInfo(), supportedClaims));

        xmlEAAPayload.setAdministrativeIssuanceDate(getXmlClaim(eaaPayload.getAdministrativeIssuanceDate(), supportedClaims));
        xmlEAAPayload.setAdministrativeExpirationDate(getXmlClaim(eaaPayload.getAdministrativeExpirationDate(), supportedClaims));
        xmlEAAPayload.setOneTimeUse(getXmlClaim(eaaPayload.getOneTimeUse(), supportedClaims));
        xmlEAAPayload.setShortLived(getXmlClaim(eaaPayload.getShortLived(), supportedClaims));
        xmlEAAPayload.setEvidence(getXmlClaim(eaaPayload.getEvidence(), supportedClaims));
        xmlEAAPayload.setAttestedAttributesSubject(getXmlAttestedAttributesSubjectClaim(eaaPayload.getAttestedAttributesSubject(), supportedClaims)); // TODO : enhance with AttestedAttributesSubjectWrapper

        xmlEAAPayload.setFullName(getXmlClaim(eaaPayload.getFullName(), supportedClaims));
        xmlEAAPayload.setFirstName(getXmlClaim(eaaPayload.getFirstName(), supportedClaims));
        xmlEAAPayload.setLastName(getXmlClaim(eaaPayload.getLastName(), supportedClaims));
        xmlEAAPayload.setMiddleName(getXmlClaim(eaaPayload.getMiddleName(), supportedClaims));
        xmlEAAPayload.setNickname(getXmlClaim(eaaPayload.getNickname(), supportedClaims));
        xmlEAAPayload.setShortName(getXmlClaim(eaaPayload.getShortName(), supportedClaims));
        xmlEAAPayload.setProfileUrl(getXmlClaim(eaaPayload.getProfileUrl(), supportedClaims));
        xmlEAAPayload.setPictureUrl(getXmlClaim(eaaPayload.getPictureUrl(), supportedClaims));
        xmlEAAPayload.setWebsiteUrl(getXmlClaim(eaaPayload.getWebsiteUrl(), supportedClaims));
        xmlEAAPayload.setEmail(getXmlClaim(eaaPayload.getEmail(), supportedClaims));
        xmlEAAPayload.setEmailVerified(getXmlClaim(eaaPayload.getEmailVerified(), supportedClaims));
        xmlEAAPayload.setGender(getXmlClaim(eaaPayload.getGender(), supportedClaims));
        xmlEAAPayload.setBirthdate(getXmlClaim(eaaPayload.getBirthdate(), supportedClaims));
        xmlEAAPayload.setTimezone(getXmlClaim(eaaPayload.getTimezone(), supportedClaims));
        xmlEAAPayload.setLocale(getXmlClaim(eaaPayload.getLocale(), supportedClaims));
        xmlEAAPayload.setAddress(getXmlAddressClaim(eaaPayload.getAddress(), supportedClaims));
        xmlEAAPayload.setPhoneNumber(getXmlClaim(eaaPayload.getPhoneNumber(), supportedClaims));
        xmlEAAPayload.setPhoneNumberVerified(getXmlClaim(eaaPayload.getPhoneNumberVerified(), supportedClaims));
        xmlEAAPayload.setPlaceOfBirth(getXmlPlaceOfBirthClaim(eaaPayload.getPlaceOfBirth(), supportedClaims));
        xmlEAAPayload.setNationalities(getXmlClaim(eaaPayload.getNationalities(), supportedClaims));
        xmlEAAPayload.setBirthLastName(getXmlClaim(eaaPayload.getBirthLastName(), supportedClaims));
        xmlEAAPayload.setBirthFirstName(getXmlClaim(eaaPayload.getBirthFirstName(), supportedClaims));
        xmlEAAPayload.setBirthMiddleName(getXmlClaim(eaaPayload.getBirthMiddleName(), supportedClaims));
        xmlEAAPayload.setSalutation(getXmlClaim(eaaPayload.getSalutation(), supportedClaims));
        xmlEAAPayload.setTitle(getXmlClaim(eaaPayload.getTitle(), supportedClaims));
        xmlEAAPayload.setMobilePhoneNumber(getXmlClaim(eaaPayload.getMobilePhoneNumber(), supportedClaims));
        xmlEAAPayload.setPseudonym(getXmlClaim(eaaPayload.getPseudonym(), supportedClaims));
        xmlEAAPayload.getCredentialSubject().addAll(getXmlCredentialSubjectClaimList(eaaPayload.getCredentialSubjects(), supportedClaims));

        xmlEAAPayload.setIssuingCountry(getXmlClaim(eaaPayload.getIssuingCountry(), supportedClaims));
        xmlEAAPayload.setIssuingAuthority(getXmlClaim(eaaPayload.getIssuingAuthority(), supportedClaims));
        xmlEAAPayload.setDocumentNumber(getXmlClaim(eaaPayload.getDocumentNumber(), supportedClaims));
        xmlEAAPayload.setPortrait(getXmlClaim(eaaPayload.getPortrait(), supportedClaims));
        xmlEAAPayload.setDrivingPrivileges(getXmlDrivingPrivilegesClaim(eaaPayload.getDrivingPrivileges(), supportedClaims));
        xmlEAAPayload.setUNDistinguishingSign(getXmlClaim(eaaPayload.getUNDistinguishingSign(), supportedClaims));
        xmlEAAPayload.setAdministrativeNumber(getXmlClaim(eaaPayload.getAdministrativeNumber(), supportedClaims));
        xmlEAAPayload.setHeight(getXmlClaim(eaaPayload.getHeight(), supportedClaims));
        xmlEAAPayload.setWeight(getXmlClaim(eaaPayload.getWeight(), supportedClaims));
        xmlEAAPayload.setEyeColor(getXmlClaim(eaaPayload.getEyeColor(), supportedClaims));
        xmlEAAPayload.setHairColor(getXmlClaim(eaaPayload.getHairColor(), supportedClaims));
        xmlEAAPayload.setResidentAddress(getXmlClaim(eaaPayload.getResidentAddress(), supportedClaims));
        xmlEAAPayload.setPortraitCaptureDate(getXmlClaim(eaaPayload.getPortraitCaptureDate(), supportedClaims));
        xmlEAAPayload.setAgeInYears(getXmlClaim(eaaPayload.getAgeInYears(), supportedClaims));
        xmlEAAPayload.setAgeBirthYear(getXmlClaim(eaaPayload.getAgeBirthYear(), supportedClaims));
        xmlEAAPayload.getAgeOverNN().addAll(getXmlAgeOverNNClaims(eaaPayload.getAgeOverNN(), supportedClaims));
        xmlEAAPayload.setIssuingJurisdiction(getXmlClaim(eaaPayload.getIssuingJurisdiction(), supportedClaims));
        xmlEAAPayload.setResidentCity(getXmlClaim(eaaPayload.getResidentCity(), supportedClaims));
        xmlEAAPayload.setResidentState(getXmlClaim(eaaPayload.getResidentState(), supportedClaims));
        xmlEAAPayload.setResidentPostalCode(getXmlClaim(eaaPayload.getResidentPostalCode(), supportedClaims));
        xmlEAAPayload.setResidentCountry(getXmlClaim(eaaPayload.getResidentCountry(), supportedClaims));
        xmlEAAPayload.getBiometricTemplate().addAll(getXmlBiometricTemplateXXClaim(eaaPayload.getBiometricTemplate(), supportedClaims));
        xmlEAAPayload.setSignatureUsualMark(getXmlClaim(eaaPayload.getSignatureUsualMark(), supportedClaims));
        xmlEAAPayload.setFingerprint(getXmlClaim(eaaPayload.getFingerprint(), supportedClaims));
        xmlEAAPayload.setBusinessName(getXmlClaim(eaaPayload.getBusinessName(), supportedClaims));
        xmlEAAPayload.setOrganizationName(getXmlClaim(eaaPayload.getOrganizationName(), supportedClaims));
        xmlEAAPayload.setBirthFullName(getXmlClaim(eaaPayload.getBirthFullName(), supportedClaims));
        xmlEAAPayload.setProfession(getXmlClaim(eaaPayload.getProfession(), supportedClaims));
        xmlEAAPayload.setRelationshipFather(getXmlClaim(eaaPayload.getRelationshipFather(), supportedClaims));
        xmlEAAPayload.setRelationshipMother(getXmlClaim(eaaPayload.getRelationshipMother(), supportedClaims));
        xmlEAAPayload.setRelationshipParent(getXmlClaim(eaaPayload.getRelationshipParent(), supportedClaims));
        xmlEAAPayload.setRelationshipSon(getXmlClaim(eaaPayload.getRelationshipSon(), supportedClaims));
        xmlEAAPayload.setRelationshipDaughter(getXmlClaim(eaaPayload.getRelationshipDaughter(), supportedClaims));
        xmlEAAPayload.setRelationshipBrother(getXmlClaim(eaaPayload.getRelationshipBrother(), supportedClaims));
        xmlEAAPayload.setRelationshipSister(getXmlClaim(eaaPayload.getRelationshipSister(), supportedClaims));
        xmlEAAPayload.setRelationshipSibling(getXmlClaim(eaaPayload.getRelationshipSibling(), supportedClaims));
        xmlEAAPayload.setRelationshipSpouse(getXmlClaim(eaaPayload.getRelationshipSpouse(), supportedClaims));
        xmlEAAPayload.setRelationshipFatherInLaw(getXmlClaim(eaaPayload.getRelationshipFatherInLaw(), supportedClaims));
        xmlEAAPayload.setRelationshipMotherInLaw(getXmlClaim(eaaPayload.getRelationshipMotherInLaw(), supportedClaims));
        xmlEAAPayload.setRelationshipParentInLaw(getXmlClaim(eaaPayload.getRelationshipParentInLaw(), supportedClaims));
        xmlEAAPayload.setRelationshipSonInLaw(getXmlClaim(eaaPayload.getRelationshipSonInLaw(), supportedClaims));
        xmlEAAPayload.setRelationshipDaughterInLaw(getXmlClaim(eaaPayload.getRelationshipDaughterInLaw(), supportedClaims));
        xmlEAAPayload.setRelationshipChildInLaw(getXmlClaim(eaaPayload.getRelationshipChildInLaw(), supportedClaims));
        xmlEAAPayload.setRelationshipParentalAuthority(getXmlClaim(eaaPayload.getRelationshipParentalAuthority(), supportedClaims));
        xmlEAAPayload.setRelationshipLegalRepresentative(getXmlClaim(eaaPayload.getRelationshipLegalRepresentative(), supportedClaims));
        xmlEAAPayload.setRelationshipAgent(getXmlClaim(eaaPayload.getRelationshipAgent(), supportedClaims));
        xmlEAAPayload.setDocumentType(getXmlClaim(eaaPayload.getDocumentType(), supportedClaims));

        xmlEAAPayload.setIssuingRegistrationIdentifier(getXmlClaim(eaaPayload.getIssuingRegistrationIdentifier(), supportedClaims));
        xmlEAAPayload.setTrustAnchor(getXmlClaim(eaaPayload.getTrustAnchor(), supportedClaims));
        xmlEAAPayload.setResidentStreet(getXmlClaim(eaaPayload.getResidentStreet(), supportedClaims));
        xmlEAAPayload.setResidentHouseNumber(getXmlClaim(eaaPayload.getResidentHouseNumber(), supportedClaims));

        xmlEAAPayload.getOtherClaim().addAll(getOtherClaims(eaaPayload, supportedClaims));

        return xmlEAAPayload;
    }

    private XmlClaim getXmlClaim(Claim claim) {
        return getXmlClaim(claim, (List<XmlClaim>) null);
    }

    private XmlClaim getXmlClaim(Claim claim, List<XmlClaim> supportedClaims) {
        return getXmlClaim(claim, new XmlClaim(), supportedClaims);
    }

    private <T extends XmlClaim> T getXmlClaim(Claim claim, T xmlClaim) {
        return getXmlClaim(claim, xmlClaim, null);
    }

    private <T extends XmlClaim> T getXmlClaim(Claim claim, T xmlClaim, List<XmlClaim> supportedClaims) {
        if (claim != null) {
            appendGenericInfo(xmlClaim, claim, supportedClaims);
            if (claim.isStringValueType()) {
                xmlClaim.setText(claim.getStringValue());
            } else if (claim.isNumberValueType()) {
                xmlClaim.setNumber(BigInteger.valueOf(claim.getNumberValue().longValue()));
            } else if (claim.isDateValueType()) {
                xmlClaim.setDateTime(claim.getDateValue());
            } else if (claim.isBooleanValueType()) {
                xmlClaim.setBoolean(claim.getBooleanValue());
            } else if (claim.isBinaryValueType()) {
                xmlClaim.setBinary(claim.getBinaryValue());
            } else if (claim.isArrayValueType()) {
                for (Claim claimItem : claim.getListValue()) {
                    xmlClaim.getItem().add(getXmlClaim(claimItem, new XmlClaim()));
                }
            } else if (claim.isMapValueType()) {
                for (Map.Entry<String, Claim> entry : claim.getMapValue().entrySet()) {
                    xmlClaim.getEntry().add(getXmlClaim(entry.getValue(), new XmlClaim()));
                }
            } else if (claim.isNullValueType()) {
                // no information is to be embedded
            } else {
                throw new UnsupportedOperationException(String.format("Unsupported Claim type '%s'", claim.getClass().getSimpleName()));
            }
            return xmlClaim;
        }
        return null;
    }

    private XmlMetadataTypeClaim getXmlMetadataType(EAAPayload eaaPayload, List<XmlClaim> supportedClaims) {
        ClaimString metadata = eaaPayload.getMetadataType();
        if (metadata != null) {
            XmlMetadataTypeClaim xmlMetadataType = getXmlClaim(metadata, new XmlMetadataTypeClaim(), supportedClaims);
            if (eaaPayload.getMetadataIntegrity() != null) {
                xmlMetadataType.setIntegrity(getXmlIntegrityClaim(eaaPayload.getMetadataIntegrity(), supportedClaims));
            }
            return xmlMetadataType;
        }
        return null;
    }

    private XmlStatusClaim getXmlStatus(ClaimStatus claimStatus, List<XmlClaim> supportedClaims) {
        if (claimStatus == null) {
            return null;
        }
        XmlStatusClaim xmlStatus = new XmlStatusClaim();
        appendGenericInfo(xmlStatus, claimStatus, supportedClaims);

        List<XmlClaim> claimSupportedClaims = new ArrayList<>();
        if (claimStatus.getStatusList() != null) {
            xmlStatus.setStatusList(getXmlStatusList(claimStatus.getStatusList(), claimSupportedClaims));
        }
        if (claimStatus.getIndex() != null) {
            xmlStatus.setIndex(getXmlClaim(claimStatus.getIndex(), claimSupportedClaims));
        }
        if (claimStatus.getUri() != null) {
            xmlStatus.setUri(getXmlClaim(claimStatus.getUri(), claimSupportedClaims));
        }
        if (claimStatus.getType() != null) {
            xmlStatus.setType(getXmlClaim(claimStatus.getType(), claimSupportedClaims));
        }
        if (claimStatus.getPurpose() != null) {
            xmlStatus.setPurpose(getXmlClaim(claimStatus.getPurpose(), claimSupportedClaims));
        }
        xmlStatus.getEntry().addAll(getOtherClaims(claimStatus, claimSupportedClaims));
        return xmlStatus;
    }

    private XmlStatusListClaim getXmlStatusList(ClaimStatusList claimStatusList, List<XmlClaim> supportedClaims) {
        if (claimStatusList == null) {
            return null;
        }
        XmlStatusListClaim xmlStatusList = new XmlStatusListClaim();
        appendGenericInfo(xmlStatusList, claimStatusList, supportedClaims);

        List<XmlClaim> claimSupportedClaims = new ArrayList<>();
        if (claimStatusList.getIndex() != null) {
            xmlStatusList.setIndex(getXmlClaim(claimStatusList.getIndex(), claimSupportedClaims));
        }
        if (claimStatusList.getUri() != null) {
            xmlStatusList.setUri(getXmlClaim(claimStatusList.getUri(), claimSupportedClaims));
        }
        xmlStatusList.getEntry().addAll(getOtherClaims(claimStatusList, claimSupportedClaims));
        return xmlStatusList;
    }

    private XmlDeviceKeyClaim getXmlDeviceKeyClaim(ClaimDeviceKey deviceKey, List<XmlClaim> supportedClaims) {
        if (deviceKey == null) {
            return null;
        }
        // NOTE: here we aim to preserve the original structure of the claim
        XmlDeviceKeyClaim xmlDeviceKeyClaim = getXmlClaim(deviceKey, new XmlDeviceKeyClaim(), supportedClaims);
        if (deviceKey.getPublicKey() != null) {
            xmlDeviceKeyClaim.setPublicKey(deviceKey.getPublicKey().getEncoded());
        }
        List<CertificateToken> certificates = deviceKey.getCertificates();
        if (Utils.isCollectionNotEmpty(certificates)) {
            for (CertificateToken certificateToken : certificates) {
                XmlX509Certificate xmlX509Certificate = new XmlX509Certificate();
                xmlX509Certificate.setCertificate(xmlCertsMap.get(certificateToken.getDSSIdAsString()));
                xmlDeviceKeyClaim.getX509Certificate().add(xmlX509Certificate);
            }
        }
        List<Digest> certificateDigests = deviceKey.getCertificateDigests();
        if (Utils.isCollectionNotEmpty(certificateDigests)) {
            for (Digest digest : certificateDigests) {
                xmlDeviceKeyClaim.getDigestAlgoAndValue().add(getXmlDigestAlgoAndValue(digest));
            }
        }
        List<String> certificateKeyIdentifiers = deviceKey.getCertificateKeyIdentifiers();
        if (Utils.isCollectionNotEmpty(certificateKeyIdentifiers)) {
            for (String kid : certificateKeyIdentifiers) {
                xmlDeviceKeyClaim.getKID().add(kid);
            }
        }
        List<String> certificateUrls = deviceKey.getCertificateUrls();
        if (Utils.isCollectionNotEmpty(certificateUrls)) {
            for (String url : certificateUrls) {
                xmlDeviceKeyClaim.getX509Url().add(url);
            }
        }
        return xmlDeviceKeyClaim;
    }

    private XmlValidityInfoClaim getXmlValidityInfoClaim(ClaimValidityInfo validityInfo, List<XmlClaim> supportedClaims) {
        if (validityInfo == null) {
            return null;
        }
        XmlValidityInfoClaim xmlValidityInfoClaim = new XmlValidityInfoClaim();
        appendGenericInfo(xmlValidityInfoClaim, validityInfo, supportedClaims);

        List<XmlClaim> claimSupportedClaims = new ArrayList<>();
        if (validityInfo.getSigned() != null) {
            xmlValidityInfoClaim.setSigned(getXmlClaim(validityInfo.getSigned(), claimSupportedClaims));
        }
        if (validityInfo.getValidFrom() != null) {
            xmlValidityInfoClaim.setValidFrom(getXmlClaim(validityInfo.getValidFrom(), claimSupportedClaims));
        }
        if (validityInfo.getValidUntil() != null) {
            xmlValidityInfoClaim.setValidUntil(getXmlClaim(validityInfo.getValidUntil(), claimSupportedClaims));
        }
        if (validityInfo.getExpectedUpdate() != null) {
            xmlValidityInfoClaim.setExpectedUpdate(getXmlClaim(validityInfo.getExpectedUpdate(), claimSupportedClaims));
        }
        xmlValidityInfoClaim.getEntry().addAll(getOtherClaims(validityInfo, claimSupportedClaims));
        return xmlValidityInfoClaim;
    }

    private XmlAddressClaim getXmlAddressClaim(ClaimAddress claimAddress, List<XmlClaim> supportedClaims) {
        if (claimAddress == null) {
            return null;
        }
        XmlAddressClaim xmlAddress = new XmlAddressClaim();
        appendGenericInfo(xmlAddress, claimAddress, supportedClaims);

        List<XmlClaim> claimSupportedClaims = new ArrayList<>();
        if (claimAddress.getPostalAddress() != null) {
            xmlAddress.setPostalAddress(getXmlClaim(claimAddress.getPostalAddress(), claimSupportedClaims));
        }
        if (claimAddress.getStreetAddress() != null) {
            xmlAddress.setStreetAddress(getXmlClaim(claimAddress.getStreetAddress(), claimSupportedClaims));
        }
        if (claimAddress.getCity() != null) {
            xmlAddress.setCity(getXmlClaim(claimAddress.getCity(), claimSupportedClaims));
        }
        if (claimAddress.getStateOrProvince() != null) {
            xmlAddress.setStateOrProvince(getXmlClaim(claimAddress.getStateOrProvince(), claimSupportedClaims));
        }
        if (claimAddress.getPostalCode() != null) {
            xmlAddress.setPostalCode(getXmlClaim(claimAddress.getPostalCode(), claimSupportedClaims));
        }
        if (claimAddress.getCountry() != null) {
            xmlAddress.setCountryName(getXmlClaim(claimAddress.getCountry(), claimSupportedClaims));
        }
        xmlAddress.getEntry().addAll(getOtherClaims(claimAddress, claimSupportedClaims));
        return xmlAddress;
    }

    private XmlPlaceOfBirthClaim getXmlPlaceOfBirthClaim(Claim claim, List<XmlClaim> supportedClaims) {
        if (claim == null) {
            return null;
        }
        if (claim instanceof ClaimPlaceOfBirth) {
            ClaimPlaceOfBirth claimPlaceOfBirth = (ClaimPlaceOfBirth) claim;
            XmlPlaceOfBirthClaim xmlPlaceOfBirthClaim = new XmlPlaceOfBirthClaim();
            appendGenericInfo(xmlPlaceOfBirthClaim, claimPlaceOfBirth, supportedClaims);

            List<XmlClaim> claimSupportedClaims = new ArrayList<>();
            if (claimPlaceOfBirth.getCountry() != null) {
                xmlPlaceOfBirthClaim.setCountry(getXmlClaim(claimPlaceOfBirth.getCountry(), claimSupportedClaims));
            }
            if (claimPlaceOfBirth.getStateOrProvince() != null) {
                xmlPlaceOfBirthClaim.setRegion(getXmlClaim(claimPlaceOfBirth.getStateOrProvince(), claimSupportedClaims));
            }
            if (claimPlaceOfBirth.getCity() != null) {
                xmlPlaceOfBirthClaim.setCity(getXmlClaim(claimPlaceOfBirth.getCity(), claimSupportedClaims));
            }
            xmlPlaceOfBirthClaim.getEntry().addAll(getOtherClaims(claimPlaceOfBirth, claimSupportedClaims));
            return xmlPlaceOfBirthClaim;

        }
        return getXmlClaim(claim, new XmlPlaceOfBirthClaim(), supportedClaims);
    }

    private XmlIntegrityClaim getXmlIntegrityClaim(ClaimIntegrity claimIntegrity, List<XmlClaim> supportedClaims) {
        if (claimIntegrity == null) {
            return null;
        }
        XmlIntegrityClaim xmlIntegrityClaim = getXmlClaim(claimIntegrity, new XmlIntegrityClaim(), supportedClaims);
        if (claimIntegrity.getDigestAlgorithm() != null) {
            xmlIntegrityClaim.setDigestMethod(claimIntegrity.getDigestAlgorithm());
        }
        if (claimIntegrity.getDigestValue() != null) {
            xmlIntegrityClaim.setDigestValue(claimIntegrity.getDigestValue());
        }
        return xmlIntegrityClaim;
    }

    private List<XmlCredentialSubjectClaim> getXmlCredentialSubjectClaimList(List<ClaimCredentialSubject> credentialSubjects, List<XmlClaim> supportedClaims) {
        if (Utils.isCollectionEmpty(credentialSubjects)) {
            return Collections.emptyList();
        }
        return credentialSubjects.stream().map(s -> getXmlCredentialSubjectClaim(s, supportedClaims)).collect(Collectors.toList());
    }

    private XmlCredentialSubjectClaim getXmlCredentialSubjectClaim(ClaimCredentialSubject credentialSubject, List<XmlClaim> supportedClaims) {
        XmlCredentialSubjectClaim xmlCredentialSubjectClaim = new XmlCredentialSubjectClaim();
        appendGenericInfo(xmlCredentialSubjectClaim, credentialSubject, supportedClaims);
        xmlCredentialSubjectClaim.setFullName(getXmlClaim(credentialSubject.getFullName(), supportedClaims));
        xmlCredentialSubjectClaim.setFirstName(getXmlClaim(credentialSubject.getFirstName(), supportedClaims));
        xmlCredentialSubjectClaim.setLastName(getXmlClaim(credentialSubject.getLastName(), supportedClaims));
        xmlCredentialSubjectClaim.setMiddleName(getXmlClaim(credentialSubject.getMiddleName(), supportedClaims));
        xmlCredentialSubjectClaim.setNickname(getXmlClaim(credentialSubject.getNickname(), supportedClaims));
        xmlCredentialSubjectClaim.setShortName(getXmlClaim(credentialSubject.getShortName(), supportedClaims));
        xmlCredentialSubjectClaim.setProfileUrl(getXmlClaim(credentialSubject.getProfileUrl(), supportedClaims));
        xmlCredentialSubjectClaim.setPictureUrl(getXmlClaim(credentialSubject.getPictureUrl(), supportedClaims));
        xmlCredentialSubjectClaim.setWebsiteUrl(getXmlClaim(credentialSubject.getWebsiteUrl(), supportedClaims));
        xmlCredentialSubjectClaim.setEmail(getXmlClaim(credentialSubject.getEmail(), supportedClaims));
        xmlCredentialSubjectClaim.setEmailVerified(getXmlClaim(credentialSubject.getEmailVerified(), supportedClaims));
        xmlCredentialSubjectClaim.setGender(getXmlClaim(credentialSubject.getGender(), supportedClaims));
        xmlCredentialSubjectClaim.setBirthdate(getXmlClaim(credentialSubject.getBirthdate(), supportedClaims));
        xmlCredentialSubjectClaim.setTimezone(getXmlClaim(credentialSubject.getTimezone(), supportedClaims));
        xmlCredentialSubjectClaim.setLocale(getXmlClaim(credentialSubject.getLocale(), supportedClaims));
        xmlCredentialSubjectClaim.setAddress(getXmlAddressClaim(credentialSubject.getAddress(), supportedClaims));
        xmlCredentialSubjectClaim.setPhoneNumber(getXmlClaim(credentialSubject.getPhoneNumber(), supportedClaims));
        xmlCredentialSubjectClaim.setPhoneNumberVerified(getXmlClaim(credentialSubject.getPhoneNumberVerified(), supportedClaims));
        xmlCredentialSubjectClaim.setPlaceOfBirth(getXmlPlaceOfBirthClaim(credentialSubject.getPlaceOfBirth(), supportedClaims));
        xmlCredentialSubjectClaim.setNationalities(getXmlClaim(credentialSubject.getNationalities(), supportedClaims));
        xmlCredentialSubjectClaim.setBirthLastName(getXmlClaim(credentialSubject.getBirthLastName(), supportedClaims));
        xmlCredentialSubjectClaim.setBirthFirstName(getXmlClaim(credentialSubject.getBirthFirstName(), supportedClaims));
        xmlCredentialSubjectClaim.setBirthMiddleName(getXmlClaim(credentialSubject.getBirthMiddleName(), supportedClaims));
        xmlCredentialSubjectClaim.setSalutation(getXmlClaim(credentialSubject.getSalutation(), supportedClaims));
        xmlCredentialSubjectClaim.setTitle(getXmlClaim(credentialSubject.getTitle(), supportedClaims));
        xmlCredentialSubjectClaim.setMobilePhoneNumber(getXmlClaim(credentialSubject.getMobilePhoneNumber(), supportedClaims));
        xmlCredentialSubjectClaim.setPseudonym(getXmlClaim(credentialSubject.getPseudonym(), supportedClaims));

        xmlCredentialSubjectClaim.getOtherClaim().addAll(getOtherClaims(credentialSubject, supportedClaims));
        return xmlCredentialSubjectClaim;
    }

    private XmlDrivingPrivilegesClaim getXmlDrivingPrivilegesClaim(ClaimDrivingPrivileges claimDrivingPrivileges, List<XmlClaim> supportedClaims) {
        if (claimDrivingPrivileges == null) {
            return null;
        }
        XmlDrivingPrivilegesClaim xmlDrivingPrivilegesClaim = new XmlDrivingPrivilegesClaim();
        appendGenericInfo(xmlDrivingPrivilegesClaim, claimDrivingPrivileges, supportedClaims);
        if (Utils.isCollectionNotEmpty(claimDrivingPrivileges.getDrivingPrivileges())) {
            for (ClaimDrivingPrivilege claimDrivingPrivilege : claimDrivingPrivileges.getDrivingPrivileges()) {
                XmlDrivingPrivilegeClaim xmlDrivingPrivilegeClaim = getXmlDrivingPrivilegeClaim(claimDrivingPrivilege);
                if (xmlDrivingPrivilegeClaim != null) {
                    xmlDrivingPrivilegesClaim.getDrivingPrivilege().add(xmlDrivingPrivilegeClaim);
                }
            }
        }
        return xmlDrivingPrivilegesClaim;
    }

    private XmlDrivingPrivilegeClaim getXmlDrivingPrivilegeClaim(ClaimDrivingPrivilege claimDrivingPrivilege) {
        if (claimDrivingPrivilege == null) {
            return null;
        }
        XmlDrivingPrivilegeClaim xmlDrivingPrivilegeClaim = new XmlDrivingPrivilegeClaim();
        appendGenericInfo(xmlDrivingPrivilegeClaim, claimDrivingPrivilege);

        List<XmlClaim> claimSupportedClaims = new ArrayList<>();
        if (claimDrivingPrivilege.getVehicleCategoryCode() != null) {
            xmlDrivingPrivilegeClaim.setVehicleCategoryCode(getXmlClaim(claimDrivingPrivilege.getVehicleCategoryCode(), claimSupportedClaims));
        }
        if (claimDrivingPrivilege.getIssueDate() != null) {
            xmlDrivingPrivilegeClaim.setIssueDate(getXmlClaim(claimDrivingPrivilege.getIssueDate(), claimSupportedClaims));
        }
        if (claimDrivingPrivilege.getExpiryDate() != null) {
            xmlDrivingPrivilegeClaim.setExpiryDate(getXmlClaim(claimDrivingPrivilege.getExpiryDate(), claimSupportedClaims));
        }
        if (claimDrivingPrivilege.getCodes() != null) {
            xmlDrivingPrivilegeClaim.setCodes(getXmlDrivingPrivilegeCodesClaim(claimDrivingPrivilege.getCodes(), claimSupportedClaims));
        }
        xmlDrivingPrivilegeClaim.getEntry().addAll(getOtherClaims(claimDrivingPrivilege, claimSupportedClaims));
        return xmlDrivingPrivilegeClaim;
    }

    private XmlDrivingPrivilegeCodesClaim getXmlDrivingPrivilegeCodesClaim(ClaimDrivingPrivilegeCodes claimDrivingPrivilegeCodes, List<XmlClaim> supportedClaims) {
        if (claimDrivingPrivilegeCodes == null) {
            return null;
        }
        XmlDrivingPrivilegeCodesClaim XmlDrivingPrivilegeCodesClaim = new XmlDrivingPrivilegeCodesClaim();
        appendGenericInfo(XmlDrivingPrivilegeCodesClaim, claimDrivingPrivilegeCodes, supportedClaims);
        if (Utils.isCollectionNotEmpty(claimDrivingPrivilegeCodes.getCodes())) {
            for (ClaimDrivingPrivilegeCode claimDrivingPrivilegeCode : claimDrivingPrivilegeCodes.getCodes()) {
                XmlDrivingPrivilegeCodeClaim XmlDrivingPrivilegeCodeClaim = getXmlDrivingPrivilegeCodeClaim(claimDrivingPrivilegeCode);
                if (XmlDrivingPrivilegeCodeClaim != null) {
                    XmlDrivingPrivilegeCodesClaim.getCode().add(XmlDrivingPrivilegeCodeClaim);
                }
            }
        }
        return XmlDrivingPrivilegeCodesClaim;
    }

    private XmlDrivingPrivilegeCodeClaim getXmlDrivingPrivilegeCodeClaim(ClaimDrivingPrivilegeCode claimDrivingPrivilegeCode) {
        if (claimDrivingPrivilegeCode == null) {
            return null;
        }
        XmlDrivingPrivilegeCodeClaim xmlDrivingPrivilegeCodeClaim = new XmlDrivingPrivilegeCodeClaim();
        appendGenericInfo(xmlDrivingPrivilegeCodeClaim, claimDrivingPrivilegeCode);

        List<XmlClaim> claimSupportedClaims = new ArrayList<>();
        if (claimDrivingPrivilegeCode.getCode() != null) {
            xmlDrivingPrivilegeCodeClaim.setCode(getXmlClaim(claimDrivingPrivilegeCode.getCode(), claimSupportedClaims));
        }
        if (claimDrivingPrivilegeCode.getSign() != null) {
            xmlDrivingPrivilegeCodeClaim.setSign(getXmlClaim(claimDrivingPrivilegeCode.getSign(), claimSupportedClaims));
        }
        if (claimDrivingPrivilegeCode.getValue() != null) {
            xmlDrivingPrivilegeCodeClaim.setValue(getXmlClaim(claimDrivingPrivilegeCode.getValue(), claimSupportedClaims));
        }
        xmlDrivingPrivilegeCodeClaim.getEntry().addAll(getOtherClaims(claimDrivingPrivilegeCode, claimSupportedClaims));
        return xmlDrivingPrivilegeCodeClaim;
    }

    private List<XmlAgeOverNNClaim> getXmlAgeOverNNClaims(List<ClaimAgeOverNN> claimsAgeOverNN, List<XmlClaim> supportedClaims) {
        if (Utils.isCollectionEmpty(claimsAgeOverNN)) {
            return Collections.emptyList();
        }
        final List<XmlAgeOverNNClaim> result = new ArrayList<>();
        for (ClaimAgeOverNN claimAgeOverNN : claimsAgeOverNN) {
            XmlAgeOverNNClaim xmlAgeOverNNClaim = getXmlAgeOverNNClaim(claimAgeOverNN, supportedClaims);
            if (xmlAgeOverNNClaim != null) {
                result.add(xmlAgeOverNNClaim);
            }
        }
        return result;
    }

    private XmlAgeOverNNClaim getXmlAgeOverNNClaim(ClaimAgeOverNN claimAgeOverNN, List<XmlClaim> supportedClaims) {
        if (claimAgeOverNN == null) {
            return null;
        }
        XmlAgeOverNNClaim xmlAgeOverNNClaim = new XmlAgeOverNNClaim();
        appendGenericInfo(xmlAgeOverNNClaim, claimAgeOverNN, supportedClaims);
        if (claimAgeOverNN.getAge() != null) {
            xmlAgeOverNNClaim.setAge(claimAgeOverNN.getAge());
        }
        return xmlAgeOverNNClaim;
    }

    private List<XmlBiometricTemplateXXClaim> getXmlBiometricTemplateXXClaim(List<ClaimBiometricTemplateXX> claimsBiometricTemplateXX, List<XmlClaim> supportedClaims) {
        if (Utils.isCollectionEmpty(claimsBiometricTemplateXX)) {
            return Collections.emptyList();
        }
        final List<XmlBiometricTemplateXXClaim> result = new ArrayList<>();
        for (ClaimBiometricTemplateXX claimBiometricTemplateXX : claimsBiometricTemplateXX) {
            XmlBiometricTemplateXXClaim xmlBiometricTemplateXXClaim = getXmlBiometricTemplateXXClaim(claimBiometricTemplateXX, supportedClaims);
            if (xmlBiometricTemplateXXClaim != null) {
                result.add(xmlBiometricTemplateXXClaim);
            }
        }
        return result;
    }

    private XmlBiometricTemplateXXClaim getXmlBiometricTemplateXXClaim(ClaimBiometricTemplateXX claimBiometricTemplateXX, List<XmlClaim> supportedClaims) {
        if (claimBiometricTemplateXX == null) {
            return null;
        }
        XmlBiometricTemplateXXClaim xmlBiometricTemplateXXClaim = new XmlBiometricTemplateXXClaim();
        appendGenericInfo(xmlBiometricTemplateXXClaim, claimBiometricTemplateXX, supportedClaims);
        if (claimBiometricTemplateXX.getType() != null) {
            xmlBiometricTemplateXXClaim.setType(claimBiometricTemplateXX.getType());
        }
        return xmlBiometricTemplateXXClaim;
    }

    private XmlAttestedAttributesSubjectClaim getXmlAttestedAttributesSubjectClaim(ClaimAttestedAttributesSubject attestedAttributesSubject, List<XmlClaim> supportedClaims) {
        if (attestedAttributesSubject == null) {
            return null;
        }

        XmlAttestedAttributesSubjectClaim xmlAttestedAttributesSubject = new XmlAttestedAttributesSubjectClaim();
        appendGenericInfo(xmlAttestedAttributesSubject, attestedAttributesSubject, supportedClaims);

        List<XmlClaim> claimSupportedClaims = new ArrayList<>();
        if (attestedAttributesSubject.getSubjectId() != null) {
            xmlAttestedAttributesSubject.setSubjectId(getXmlAttestedAttributesSubjectIdClaim(attestedAttributesSubject.getSubjectId(), claimSupportedClaims));
        }
        if (attestedAttributesSubject.getSubjectPseudonym() != null) {
            xmlAttestedAttributesSubject.setSubjectPseudonym(getXmlClaim(attestedAttributesSubject.getSubjectPseudonym(), claimSupportedClaims));
        }
        if (attestedAttributesSubject.getAttributes() != null) {
            xmlAttestedAttributesSubject.setAttributes(getXmlClaim(attestedAttributesSubject.getAttributes(), claimSupportedClaims));
        }
        xmlAttestedAttributesSubject.getEntry().addAll(getOtherClaims(attestedAttributesSubject, claimSupportedClaims));
        return xmlAttestedAttributesSubject;
    }

    private XmlAttestedAttributesSubjectIdClaim getXmlAttestedAttributesSubjectIdClaim(Claim claim, List<XmlClaim> supportedClaims) {
        if (claim == null) {
            return null;
        }

        if (claim instanceof ClaimAttestedAttributesSubjectId) {
            XmlAttestedAttributesSubjectIdClaim xmlAttestedAttributesSubjectIdClaim = new XmlAttestedAttributesSubjectIdClaim();
            appendGenericInfo(xmlAttestedAttributesSubjectIdClaim, claim, supportedClaims);

            List<XmlClaim> claimSupportedClaims = new ArrayList<>();

            ClaimAttestedAttributesSubjectId attestedAttributesSubjectId = (ClaimAttestedAttributesSubjectId) claim;
            if (attestedAttributesSubjectId.getFamilyName() != null) {
                xmlAttestedAttributesSubjectIdClaim.setFamilyName(getXmlClaim(attestedAttributesSubjectId.getFamilyName(), claimSupportedClaims));
            }
            if (attestedAttributesSubjectId.getGivenName() != null) {
                xmlAttestedAttributesSubjectIdClaim.setGivenName(getXmlClaim(attestedAttributesSubjectId.getGivenName(), claimSupportedClaims));
            }
            if (attestedAttributesSubjectId.getDocumentNumber() != null) {
                xmlAttestedAttributesSubjectIdClaim.setDocumentNumber(getXmlClaim(attestedAttributesSubjectId.getDocumentNumber(), claimSupportedClaims));
            }
            xmlAttestedAttributesSubjectIdClaim.getEntry().addAll(getOtherClaims(claim, claimSupportedClaims));
            return xmlAttestedAttributesSubjectIdClaim;
        }
        return getXmlClaim(claim, new XmlAttestedAttributesSubjectIdClaim(), supportedClaims);
    }

    private List<XmlClaim> getOtherClaims(Claim claim, List<XmlClaim> supportedClaims) {
        if (claim.isMapValueType() && !claim.isNullOrEmpty()) {
            final List<XmlClaim> otherClaims = new ArrayList<>();
            Collection<String> processedHeaderNames = getHeaderNames(supportedClaims);
            Map<String, Claim> mapValue = claim.getMapValue();
            for (String headerName : mapValue.keySet()) {
                if (!processedHeaderNames.contains(headerName)) {
                    Claim claimValue = mapValue.get(headerName);
                    if (claimValue != null) {
                        XmlClaim otherClaim = getXmlClaim(claimValue);
                        otherClaims.add(otherClaim);
                    }
                }
            }
            return otherClaims;
        }

        return Collections.emptyList();
    }

    private Collection<String> getHeaderNames(List<XmlClaim> claimsList) {
        Set<String> result = new HashSet<>();
        for (XmlClaim xmlClaim : claimsList) {
            addHeaderNameSecurely(xmlClaim, result);
        }
        return result;
    }

    private void addHeaderNameSecurely(XmlClaim xmlClaim, Set<String> result) {
        if (xmlClaim != null && xmlClaim.getName() != null) {
            result.add(xmlClaim.getName());
        }
    }

    private void appendGenericInfo(XmlClaim xmlClaim, Claim claim) {
        appendGenericInfo(xmlClaim, claim, null);
    }

    private void appendGenericInfo(XmlClaim xmlClaim, Claim claim, List<XmlClaim> supportedClaims) {
        if (claim != null) {
            if (claim.getName() != null) {
                xmlClaim.setName(claim.getName());
            }
            if (claim.getNamespace() != null) {
                xmlClaim.setNamespace(claim.getNamespace());
            }
            if (claim.isSelectivelyDisclosable()) {
                xmlClaim.setDisclosure(claim.isSelectivelyDisclosable());
            }
            if (supportedClaims != null) {
                supportedClaims.add(xmlClaim);
            }
        }
    }

    @Override
    public XmlSignature buildDetachedXmlSignature(AdvancedSignature signature) {
        return signatureDiagnosticDataBuilder.buildDetachedXmlSignature(signature);
    }

}
