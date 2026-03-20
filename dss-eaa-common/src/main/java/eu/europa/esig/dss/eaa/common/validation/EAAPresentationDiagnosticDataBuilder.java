package eu.europa.esig.dss.eaa.common.validation;

import eu.europa.esig.dss.diagnostic.jaxb.XmlAddressClaim;
import eu.europa.esig.dss.diagnostic.jaxb.XmlClaim;
import eu.europa.esig.dss.diagnostic.jaxb.XmlCredentialSubject;
import eu.europa.esig.dss.diagnostic.jaxb.XmlDiagnosticData;
import eu.europa.esig.dss.diagnostic.jaxb.XmlDigestMatcher;
import eu.europa.esig.dss.diagnostic.jaxb.XmlDisclosableClaim;
import eu.europa.esig.dss.diagnostic.jaxb.XmlEAAPayload;
import eu.europa.esig.dss.diagnostic.jaxb.XmlEAAPresentation;
import eu.europa.esig.dss.diagnostic.jaxb.XmlEAAPresentationSignature;
import eu.europa.esig.dss.diagnostic.jaxb.XmlIntegrityClaim;
import eu.europa.esig.dss.diagnostic.jaxb.XmlKeyBindingSignature;
import eu.europa.esig.dss.diagnostic.jaxb.XmlMetadataTypeClaim;
import eu.europa.esig.dss.diagnostic.jaxb.XmlPlaceOfBirthClaim;
import eu.europa.esig.dss.diagnostic.jaxb.XmlSignature;
import eu.europa.esig.dss.diagnostic.jaxb.XmlStatusClaim;
import eu.europa.esig.dss.model.Digest;
import eu.europa.esig.dss.model.ReferenceValidation;
import eu.europa.esig.dss.model.eaa.DisclosureValidation;
import eu.europa.esig.dss.model.eaa.claim.Claim;
import eu.europa.esig.dss.model.eaa.claim.ClaimAddress;
import eu.europa.esig.dss.model.eaa.claim.ClaimCredentialSubject;
import eu.europa.esig.dss.model.eaa.claim.ClaimIntegrity;
import eu.europa.esig.dss.model.eaa.claim.ClaimPlaceOfBirth;
import eu.europa.esig.dss.model.eaa.claim.ClaimStatus;
import eu.europa.esig.dss.model.eaa.claim.ClaimString;
import eu.europa.esig.dss.spi.eaa.EAAPayload;
import eu.europa.esig.dss.spi.eaa.EAAPresentation;
import eu.europa.esig.dss.spi.signature.AdvancedSignature;
import eu.europa.esig.dss.utils.Utils;
import eu.europa.esig.dss.validation.reports.diagnostic.SignedDocumentDiagnosticDataBuilder;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
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

    /** The collection of EAA presentations */
    protected Collection<EAAPresentation> eaaPresentations;

    /** Builder used to build a signature object */
    private SignedDocumentDiagnosticDataBuilder signatureDiagnosticDataBuilder;

    /**
     * Default constructor
     */
    public EAAPresentationDiagnosticDataBuilder() {
        // empty
    }

    /**
     * Sets a collection of found EAA presentations
     *
     * @param eaaPresentations a collection of {@code EAAPresentation}s
     * @return this builder
     */
    public EAAPresentationDiagnosticDataBuilder foundEAAPresentations(Collection<EAAPresentation> eaaPresentations) {
        this.eaaPresentations = eaaPresentations;
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
        if (Utils.isCollectionNotEmpty(eaaPresentations)) {
            Collection<XmlEAAPresentation> xmlEAAPresentations = buildXmlEAAPresentations(eaaPresentations);
            xmlDiagnosticData.getEAAPresentations().addAll(xmlEAAPresentations);
        }
        return xmlDiagnosticData;
    }

    private Collection<XmlEAAPresentation> buildXmlEAAPresentations(Collection<EAAPresentation> eaaPresentations) {
        List<XmlEAAPresentation> builtEAAPresentations = new ArrayList<>();
        for (EAAPresentation eaaPresentation : eaaPresentations) {
            XmlEAAPresentation xmlEAAPresentation = buildDetachedXmlEAAPresentation(eaaPresentation);
            builtEAAPresentations.add(xmlEAAPresentation);
        }
        return builtEAAPresentations;
    }

    private XmlEAAPresentation buildDetachedXmlEAAPresentation(EAAPresentation eaaPresentation) {
        final XmlEAAPresentation xmlEAAPresentation = new XmlEAAPresentation();
        xmlEAAPresentation.setId(eaaPresentation.getId());
        xmlEAAPresentation.setDocumentName(eaaPresentation.getFilename());
        xmlEAAPresentation.setType(eaaPresentation.getEAAPresentationType());
        for (AdvancedSignature signature : eaaPresentation.getSignatures()) {
            xmlEAAPresentation.getEAAPresentationSignature().add(getXmlEAAPresentationSignature(signature));
        }
        xmlEAAPresentation.setDigestMatchers(buildXmlDigestMatchers(eaaPresentation.getDisclosureValidations()));
        if (eaaPresentation.getKeyBindingSignature() != null) {
            xmlEAAPresentation.setKeyBindingSignature(getXmlKeyBindingSignature(eaaPresentation.getKeyBindingSignature()));
        }
        xmlEAAPresentation.setEAAPayload(getXmlEAAPayload(eaaPresentation.getPayload()));
        return xmlEAAPresentation;
    }

    private XmlEAAPresentationSignature getXmlEAAPresentationSignature(AdvancedSignature signature) {
        XmlEAAPresentationSignature xmlEAAPresentationSignature = new XmlEAAPresentationSignature();
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
        XmlDigestMatcher ref = new XmlDigestMatcher();
        ref.setType(disclosureValidation.getType());
        if (disclosureValidation.getClaimName() != null || disclosureValidation.getValue() != null) {
            XmlDisclosableClaim xmlClaim = new XmlDisclosableClaim();
            xmlClaim.setName(disclosureValidation.getClaimName());
            if (disclosureValidation.getValue() != null) {
                xmlClaim.setValue(disclosureValidation.getValue().getValueAsString());
            }
            ref.setDisclosableClaim(xmlClaim);
        }
        Digest digest = disclosureValidation.getDigest();
        if (digest != null) {
            ref.setDigestValue(digest.getValue());
            ref.setDigestMethod(digest.getAlgorithm());
        }
        ref.setDataFound(disclosureValidation.isFound());
        ref.setDataIntact(disclosureValidation.isIntact());

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

    private XmlEAAPayload getXmlEAAPayload(EAAPayload eaaPayload) {
        final List<XmlClaim> supportedClaims = new ArrayList<>();
        final XmlEAAPayload xmlEAAPayload = new XmlEAAPayload();

        xmlEAAPayload.setIdentifier(getXmlClaim(eaaPayload.getIdentifier(), supportedClaims));
        xmlEAAPayload.setIssuer(getXmlClaim(eaaPayload.getIssuer(), supportedClaims));
        xmlEAAPayload.setSubject(getXmlClaim(eaaPayload.getSubject(), supportedClaims));
        xmlEAAPayload.setAudience(getXmlClaim(eaaPayload.getAudience(), supportedClaims));
        xmlEAAPayload.setExpirationTime(getXmlClaim(eaaPayload.getExpirationTime(), supportedClaims));
        xmlEAAPayload.setNotBefore(getXmlClaim(eaaPayload.getNotBeforeTime(), supportedClaims));
        xmlEAAPayload.setIssuedAt(getXmlClaim(eaaPayload.getIssuedAtTime(), supportedClaims));
        xmlEAAPayload.setUpdatedAt(getXmlClaim(eaaPayload.getUpdatedAtTime(), supportedClaims));
        xmlEAAPayload.setCategory(getXmlClaim(eaaPayload.getCategory(), supportedClaims));
        xmlEAAPayload.setMetadataType(getXmlMetadataType(eaaPayload, supportedClaims));
        xmlEAAPayload.setStatus(getXmlStatus(eaaPayload.getStatus(), supportedClaims));
        xmlEAAPayload.setNonce(getXmlClaim(eaaPayload.getNonce(), supportedClaims));
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
        xmlEAAPayload.getCredentialSubject().addAll(getXmlCredentialSubjectList(eaaPayload.getCredentialSubjects(), supportedClaims));

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
            appendGenericInfo(xmlClaim, claim);
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
            } else {
                throw new UnsupportedOperationException(String.format("Unsupported Claim type '%s'", claim.getClass().getSimpleName()));
            }
            if (supportedClaims != null) {
                supportedClaims.add(xmlClaim);
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
        XmlStatusClaim xmlStatus = getXmlClaim(claimStatus, new XmlStatusClaim(), supportedClaims);
        if (claimStatus.getIndex() != null) {
            xmlStatus.setIndex(getXmlClaim(claimStatus.getIndex(), supportedClaims));
        }
        if (claimStatus.getUri() != null) {
            xmlStatus.setUri(getXmlClaim(claimStatus.getUri(), supportedClaims));
        }
        return xmlStatus;
    }

    private XmlAddressClaim getXmlAddressClaim(ClaimAddress claimAddress, List<XmlClaim> supportedClaims) {
        if (claimAddress == null) {
            return null;
        }
        XmlAddressClaim xmlAddress = getXmlClaim(claimAddress, new XmlAddressClaim(), supportedClaims);
        if (claimAddress.getPostalAddress() != null) {
            xmlAddress.setPostalAddress(getXmlClaim(claimAddress.getPostalAddress()));
        }
        if (claimAddress.getStreetAddress() != null) {
            xmlAddress.setStreetAddress(getXmlClaim(claimAddress.getStreetAddress()));
        }
        if (claimAddress.getCity() != null) {
            xmlAddress.setCity(getXmlClaim(claimAddress.getCity()));
        }
        if (claimAddress.getStateOrProvince() != null) {
            xmlAddress.setStateOrProvince(getXmlClaim(claimAddress.getStateOrProvince()));
        }
        if (claimAddress.getPostalCode() != null) {
            xmlAddress.setPostalCode(getXmlClaim(claimAddress.getPostalCode()));
        }
        if (claimAddress.getCountry() != null) {
            xmlAddress.setCountryName(getXmlClaim(claimAddress.getCountry()));
        }
        return xmlAddress;
    }

    private XmlPlaceOfBirthClaim getXmlPlaceOfBirthClaim(ClaimPlaceOfBirth claimPlaceOfBirth, List<XmlClaim> supportedClaims) {
        if (claimPlaceOfBirth == null) {
            return null;
        }
        XmlPlaceOfBirthClaim xmlPlaceOfBirthClaim = getXmlClaim(claimPlaceOfBirth, new XmlPlaceOfBirthClaim(), supportedClaims);
        if (claimPlaceOfBirth.getCountry() != null) {
            xmlPlaceOfBirthClaim.setCountry(getXmlClaim(claimPlaceOfBirth.getCountry()));
        }
        if (claimPlaceOfBirth.getStateOrProvince() != null) {
            xmlPlaceOfBirthClaim.setRegion(getXmlClaim(claimPlaceOfBirth.getStateOrProvince()));
        }
        if (claimPlaceOfBirth.getCity() != null) {
            xmlPlaceOfBirthClaim.setCity(getXmlClaim(claimPlaceOfBirth.getCity()));
        }
        return xmlPlaceOfBirthClaim;
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

    private List<XmlCredentialSubject> getXmlCredentialSubjectList(List<ClaimCredentialSubject> credentialSubjects, List<XmlClaim> supportedClaims) {
        if (Utils.isCollectionEmpty(credentialSubjects)) {
            return Collections.emptyList();
        }
        return credentialSubjects.stream().map(s -> getXmlCredentialSubject(s, supportedClaims)).collect(Collectors.toList());
    }

    private XmlCredentialSubject getXmlCredentialSubject(ClaimCredentialSubject credentialSubject, List<XmlClaim> supportedClaims) {
        XmlCredentialSubject xmlCredentialSubject = new XmlCredentialSubject();
        appendGenericInfo(xmlCredentialSubject, credentialSubject);
        xmlCredentialSubject.setFullName(getXmlClaim(credentialSubject.getFullName(), supportedClaims));
        xmlCredentialSubject.setFirstName(getXmlClaim(credentialSubject.getFirstName(), supportedClaims));
        xmlCredentialSubject.setLastName(getXmlClaim(credentialSubject.getLastName(), supportedClaims));
        xmlCredentialSubject.setMiddleName(getXmlClaim(credentialSubject.getMiddleName(), supportedClaims));
        xmlCredentialSubject.setNickname(getXmlClaim(credentialSubject.getNickname(), supportedClaims));
        xmlCredentialSubject.setShortName(getXmlClaim(credentialSubject.getShortName(), supportedClaims));
        xmlCredentialSubject.setProfileUrl(getXmlClaim(credentialSubject.getProfileUrl(), supportedClaims));
        xmlCredentialSubject.setPictureUrl(getXmlClaim(credentialSubject.getPictureUrl(), supportedClaims));
        xmlCredentialSubject.setWebsiteUrl(getXmlClaim(credentialSubject.getWebsiteUrl(), supportedClaims));
        xmlCredentialSubject.setEmail(getXmlClaim(credentialSubject.getEmail(), supportedClaims));
        xmlCredentialSubject.setEmailVerified(getXmlClaim(credentialSubject.getEmailVerified(), supportedClaims));
        xmlCredentialSubject.setGender(getXmlClaim(credentialSubject.getGender(), supportedClaims));
        xmlCredentialSubject.setBirthdate(getXmlClaim(credentialSubject.getBirthdate(), supportedClaims));
        xmlCredentialSubject.setTimezone(getXmlClaim(credentialSubject.getTimezone(), supportedClaims));
        xmlCredentialSubject.setLocale(getXmlClaim(credentialSubject.getLocale(), supportedClaims));
        xmlCredentialSubject.setAddress(getXmlAddressClaim(credentialSubject.getAddress(), supportedClaims));
        xmlCredentialSubject.setPhoneNumber(getXmlClaim(credentialSubject.getPhoneNumber(), supportedClaims));
        xmlCredentialSubject.setPhoneNumberVerified(getXmlClaim(credentialSubject.getPhoneNumberVerified(), supportedClaims));
        xmlCredentialSubject.setPlaceOfBirth(getXmlPlaceOfBirthClaim(credentialSubject.getPlaceOfBirth(), supportedClaims));
        xmlCredentialSubject.setNationalities(getXmlClaim(credentialSubject.getNationalities(), supportedClaims));
        xmlCredentialSubject.setBirthLastName(getXmlClaim(credentialSubject.getBirthLastName(), supportedClaims));
        xmlCredentialSubject.setBirthFirstName(getXmlClaim(credentialSubject.getBirthFirstName(), supportedClaims));
        xmlCredentialSubject.setBirthMiddleName(getXmlClaim(credentialSubject.getBirthMiddleName(), supportedClaims));
        xmlCredentialSubject.setSalutation(getXmlClaim(credentialSubject.getSalutation(), supportedClaims));
        xmlCredentialSubject.setTitle(getXmlClaim(credentialSubject.getTitle(), supportedClaims));
        xmlCredentialSubject.setMobilePhoneNumber(getXmlClaim(credentialSubject.getMobilePhoneNumber(), supportedClaims));
        xmlCredentialSubject.setPseudonym(getXmlClaim(credentialSubject.getPseudonym(), supportedClaims));

        xmlCredentialSubject.getOtherClaim().addAll(getOtherClaims(credentialSubject, supportedClaims));
        if (supportedClaims != null) {
            supportedClaims.add(xmlCredentialSubject);
        }
        return xmlCredentialSubject;
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
        if (claim != null) {
            if (claim.getName() != null) {
                xmlClaim.setName(claim.getName());
            }
            if (claim.isSelectivelyDisclosable()) {
                xmlClaim.setDisclosure(claim.isSelectivelyDisclosable());
            }
        }
    }

    @Override
    public XmlSignature buildDetachedXmlSignature(AdvancedSignature signature) {
        return signatureDiagnosticDataBuilder.buildDetachedXmlSignature(signature);
    }

}
