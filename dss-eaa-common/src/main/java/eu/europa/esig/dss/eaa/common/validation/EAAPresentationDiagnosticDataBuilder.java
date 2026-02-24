package eu.europa.esig.dss.eaa.common.validation;

import eu.europa.esig.dss.diagnostic.jaxb.XmlAddressClaim;
import eu.europa.esig.dss.diagnostic.jaxb.XmlClaim;
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
        xmlEAAPresentation.setEAAPayload(getXmlEAAPayload(eaaPresentation.getPayloadWithDisclosures()));
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
            XmlClaim xmlClaim = new XmlClaim();
            xmlClaim.setName(disclosureValidation.getClaimName());
            if (disclosureValidation.getValue() != null) {
                xmlClaim.setValue(disclosureValidation.getValue().getValueAsString());
            }
            ref.setClaim(xmlClaim);
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
        final List<XmlDisclosableClaim> supportedClaims = new ArrayList<>();
        final XmlEAAPayload xmlEAAPayload = new XmlEAAPayload();

        xmlEAAPayload.setIdentifier(getXmlDisclosableClaim(eaaPayload.getIdentifier(), supportedClaims));
        xmlEAAPayload.setIssuer(getXmlDisclosableClaim(eaaPayload.getIssuer(), supportedClaims));
        xmlEAAPayload.setSubject(getXmlDisclosableClaim(eaaPayload.getSubject(), supportedClaims));
        xmlEAAPayload.setAudience(getXmlDisclosableClaim(eaaPayload.getAudience(), supportedClaims));
        xmlEAAPayload.setExpirationTime(getXmlDisclosableClaim(eaaPayload.getExpirationTime(), supportedClaims));
        xmlEAAPayload.setNotBefore(getXmlDisclosableClaim(eaaPayload.getNotBeforeTime(), supportedClaims));
        xmlEAAPayload.setIssuedAt(getXmlDisclosableClaim(eaaPayload.getIssuedAtTime(), supportedClaims));
        xmlEAAPayload.setUpdatedAt(getXmlDisclosableClaim(eaaPayload.getUpdatedAtTime(), supportedClaims));
        xmlEAAPayload.setCategory(getXmlDisclosableClaim(eaaPayload.getCategory(), supportedClaims));
        xmlEAAPayload.setMetadataType(getXmlMetadataType(eaaPayload, supportedClaims));
        xmlEAAPayload.setStatus(getXmlStatus(eaaPayload.getStatus(), supportedClaims));
        xmlEAAPayload.setNonce(getXmlDisclosableClaim(eaaPayload.getNonce(), supportedClaims));
        xmlEAAPayload.setFullName(getXmlDisclosableClaim(eaaPayload.getFullName(), supportedClaims));
        xmlEAAPayload.setFirstName(getXmlDisclosableClaim(eaaPayload.getFirstName(), supportedClaims));
        xmlEAAPayload.setLastName(getXmlDisclosableClaim(eaaPayload.getLastName(), supportedClaims));
        xmlEAAPayload.setMiddleName(getXmlDisclosableClaim(eaaPayload.getMiddleName(), supportedClaims));
        xmlEAAPayload.setNickname(getXmlDisclosableClaim(eaaPayload.getNickname(), supportedClaims));
        xmlEAAPayload.setShortName(getXmlDisclosableClaim(eaaPayload.getShortName(), supportedClaims));
        xmlEAAPayload.setProfileUrl(getXmlDisclosableClaim(eaaPayload.getProfileUrl(), supportedClaims));
        xmlEAAPayload.setPictureUrl(getXmlDisclosableClaim(eaaPayload.getPictureUrl(), supportedClaims));
        xmlEAAPayload.setWebsiteUrl(getXmlDisclosableClaim(eaaPayload.getWebsiteUrl(), supportedClaims));
        xmlEAAPayload.setEmail(getXmlDisclosableClaim(eaaPayload.getEmail(), supportedClaims));
        xmlEAAPayload.setEmailVerified(getXmlDisclosableClaim(eaaPayload.getEmailVerified(), supportedClaims));
        xmlEAAPayload.setGender(getXmlDisclosableClaim(eaaPayload.getGender(), supportedClaims));
        xmlEAAPayload.setBirthdate(getXmlDisclosableClaim(eaaPayload.getBirthdate(), supportedClaims));
        xmlEAAPayload.setTimezone(getXmlDisclosableClaim(eaaPayload.getTimezone(), supportedClaims));
        xmlEAAPayload.setLocale(getXmlDisclosableClaim(eaaPayload.getLocale(), supportedClaims));
        xmlEAAPayload.setAddress(getXmlAddressClaim(eaaPayload.getAddress(), supportedClaims));
        xmlEAAPayload.setPhoneNumber(getXmlDisclosableClaim(eaaPayload.getPhoneNumber(), supportedClaims));
        xmlEAAPayload.setPhoneNumberVerified(getXmlDisclosableClaim(eaaPayload.getPhoneNumberVerified(), supportedClaims));
        xmlEAAPayload.setPlaceOfBirth(getXmlPlaceOfBirthClaim(eaaPayload.getPlaceOfBirth(), supportedClaims));
        xmlEAAPayload.setNationalities(getXmlDisclosableClaim(eaaPayload.getNationalities(), supportedClaims));
        xmlEAAPayload.setBirthLastName(getXmlDisclosableClaim(eaaPayload.getBirthLastName(), supportedClaims));
        xmlEAAPayload.setBirthFirstName(getXmlDisclosableClaim(eaaPayload.getBirthFirstName(), supportedClaims));
        xmlEAAPayload.setBirthMiddleName(getXmlDisclosableClaim(eaaPayload.getBirthMiddleName(), supportedClaims));
        xmlEAAPayload.setSalutation(getXmlDisclosableClaim(eaaPayload.getSalutation(), supportedClaims));
        xmlEAAPayload.setTitle(getXmlDisclosableClaim(eaaPayload.getTitle(), supportedClaims));
        xmlEAAPayload.setMobilePhoneNumber(getXmlDisclosableClaim(eaaPayload.getMobilePhoneNumber(), supportedClaims));
        xmlEAAPayload.setPseudonym(getXmlDisclosableClaim(eaaPayload.getPseudonym(), supportedClaims));

        xmlEAAPayload.getOtherClaim().addAll(getOtherClaims(eaaPayload, supportedClaims));

        return xmlEAAPayload;
    }

    private XmlDisclosableClaim getXmlDisclosableClaim(Claim claim) {
        return getXmlDisclosableClaim(claim, (List<XmlDisclosableClaim>) null);
    }

    private XmlDisclosableClaim getXmlDisclosableClaim(Claim claim, List<XmlDisclosableClaim> supportedClaims) {
        return getXmlDisclosableClaim(claim, new XmlDisclosableClaim(), supportedClaims);
    }

    private <T extends XmlDisclosableClaim> T getXmlDisclosableClaim(Claim claim, T xmlDisclosableClaim) {
        return getXmlDisclosableClaim(claim, xmlDisclosableClaim, null);
    }

    private <T extends XmlDisclosableClaim> T getXmlDisclosableClaim(Claim claim, T xmlDisclosableClaim, List<XmlDisclosableClaim> supportedClaims) {
        if (claim != null) {
            appendGenericInfo(xmlDisclosableClaim, claim);
            if (claim.isStringValueType()) {
                xmlDisclosableClaim.setText(claim.getStringValue());
            } else if (claim.isNumberValueType()) {
                xmlDisclosableClaim.setNumber(BigInteger.valueOf(claim.getNumberValue().longValue()));
            } else if (claim.isDateValueType()) {
                xmlDisclosableClaim.setDateTime(claim.getDateValue());
            } else if (claim.isBooleanValueType()) {
                xmlDisclosableClaim.setBoolean(claim.getBooleanValue());
            } else if (claim.isBinaryValueType()) {
                xmlDisclosableClaim.setSerialized(claim.getBinariesValue());
            } else if (claim.isArrayValueType()) {
                for (Claim claimItem : claim.getListValue()) {
                    xmlDisclosableClaim.getItem().add(getXmlDisclosableClaim(claimItem, new XmlDisclosableClaim()));
                }
            } else {
                // e.g. map
                xmlDisclosableClaim.setSerialized(claim.getValueAsString().getBytes());
            }
            if (supportedClaims != null) {
                supportedClaims.add(xmlDisclosableClaim);
            }
            return xmlDisclosableClaim;
        }
        return null;
    }

    private XmlMetadataTypeClaim getXmlMetadataType(EAAPayload eaaPayload, List<XmlDisclosableClaim> supportedClaims) {
        ClaimString metadata = eaaPayload.getMetadataType();
        if (metadata != null) {
            XmlMetadataTypeClaim xmlMetadataType = getXmlDisclosableClaim(metadata, new XmlMetadataTypeClaim(), supportedClaims);
            if (eaaPayload.getMetadataIntegrity() != null) {
                xmlMetadataType.setIntegrity(getXmlIntegrityClaim(eaaPayload.getMetadataIntegrity(), supportedClaims));
            }
            return xmlMetadataType;
        }
        return null;
    }

    private XmlStatusClaim getXmlStatus(ClaimStatus claimStatus, List<XmlDisclosableClaim> supportedClaims) {
        if (claimStatus == null) {
            return null;
        }
        XmlStatusClaim xmlStatus = getXmlDisclosableClaim(claimStatus, new XmlStatusClaim(), supportedClaims);
        if (claimStatus.getIndex() != null) {
            xmlStatus.setIndex(getXmlDisclosableClaim(claimStatus.getIndex(), supportedClaims));
        }
        if (claimStatus.getUri() != null) {
            xmlStatus.setUri(getXmlDisclosableClaim(claimStatus.getUri(), supportedClaims));
        }
        return xmlStatus;
    }

    private XmlAddressClaim getXmlAddressClaim(ClaimAddress claimAddress, List<XmlDisclosableClaim> supportedClaims) {
        if (claimAddress == null) {
            return null;
        }
        XmlAddressClaim xmlAddress = getXmlDisclosableClaim(claimAddress, new XmlAddressClaim(), supportedClaims);
        if (claimAddress.getPostalAddress() != null) {
            xmlAddress.setPostalAddress(getXmlDisclosableClaim(claimAddress.getPostalAddress()));
        }
        if (claimAddress.getStreetAddress() != null) {
            xmlAddress.setStreetAddress(getXmlDisclosableClaim(claimAddress.getStreetAddress()));
        }
        if (claimAddress.getCity() != null) {
            xmlAddress.setCity(getXmlDisclosableClaim(claimAddress.getCity()));
        }
        if (claimAddress.getStateOrProvince() != null) {
            xmlAddress.setStateOrProvince(getXmlDisclosableClaim(claimAddress.getStateOrProvince()));
        }
        if (claimAddress.getPostalCode() != null) {
            xmlAddress.setPostalCode(getXmlDisclosableClaim(claimAddress.getPostalCode()));
        }
        if (claimAddress.getCountry() != null) {
            xmlAddress.setCountryName(getXmlDisclosableClaim(claimAddress.getCountry()));
        }
        return xmlAddress;
    }

    private XmlPlaceOfBirthClaim getXmlPlaceOfBirthClaim(ClaimPlaceOfBirth claimPlaceOfBirth, List<XmlDisclosableClaim> supportedClaims) {
        if (claimPlaceOfBirth == null) {
            return null;
        }
        XmlPlaceOfBirthClaim xmlPlaceOfBirthClaim = getXmlDisclosableClaim(claimPlaceOfBirth, new XmlPlaceOfBirthClaim(), supportedClaims);
        if (claimPlaceOfBirth.getCountry() != null) {
            xmlPlaceOfBirthClaim.setCountry(getXmlDisclosableClaim(claimPlaceOfBirth.getCountry()));
        }
        if (claimPlaceOfBirth.getStateOrProvince() != null) {
            xmlPlaceOfBirthClaim.setRegion(getXmlDisclosableClaim(claimPlaceOfBirth.getStateOrProvince()));
        }
        if (claimPlaceOfBirth.getCity() != null) {
            xmlPlaceOfBirthClaim.setCity(getXmlDisclosableClaim(claimPlaceOfBirth.getCity()));
        }
        return xmlPlaceOfBirthClaim;
    }

    private XmlIntegrityClaim getXmlIntegrityClaim(ClaimIntegrity claimIntegrity, List<XmlDisclosableClaim> supportedClaims) {
        if (claimIntegrity == null) {
            return null;
        }
        XmlIntegrityClaim xmlIntegrityClaim = getXmlDisclosableClaim(claimIntegrity, new XmlIntegrityClaim(), supportedClaims);
        if (claimIntegrity.getDigestAlgorithm() != null) {
            xmlIntegrityClaim.setDigestMethod(claimIntegrity.getDigestAlgorithm());
        }
        if (claimIntegrity.getDigestValue() != null) {
            xmlIntegrityClaim.setDigestValue(claimIntegrity.getDigestValue());
        }
        return xmlIntegrityClaim;
    }

    private List<XmlDisclosableClaim> getOtherClaims(EAAPayload eaaPayload, List<XmlDisclosableClaim> supportedClaims) {
        Map<String, Claim> claimMap = eaaPayload.getClaimMap();
        if (Utils.isMapNotEmpty(claimMap)) {
            final List<XmlDisclosableClaim> otherClaims = new ArrayList<>();
            Collection<String> processedHeaderNames = getHeaderNames(supportedClaims);
            for (String headerName : claimMap.keySet()) {
                if (!processedHeaderNames.contains(headerName)) {
                    Claim claimValue = claimMap.get(headerName);
                    if (claimValue != null) {
                        XmlDisclosableClaim otherClaim = getXmlDisclosableClaim(claimValue);
                        otherClaims.add(otherClaim);
                    }
                }
            }
            return otherClaims;
        }

        return Collections.emptyList();
    }

    private Collection<String> getHeaderNames(List<XmlDisclosableClaim> claimsList) {
        Set<String> result = new HashSet<>();
        for (XmlDisclosableClaim xmlDisclosableClaim : claimsList) {
            addHeaderNameSecurely(xmlDisclosableClaim, result);
        }
        return result;
    }

    private void addHeaderNameSecurely(XmlDisclosableClaim xmlDisclosableClaim, Set<String> result) {
        if (xmlDisclosableClaim != null && xmlDisclosableClaim.getName() != null) {
            result.add(xmlDisclosableClaim.getName());
        }
    }

    private void appendGenericInfo(XmlDisclosableClaim xmlDisclosableClaim, Claim claim) {
        if (claim != null) {
            if (claim.getName() != null) {
                xmlDisclosableClaim.setName(claim.getName());
            }
            if (claim.isSelectivelyDisclosable()) {
                xmlDisclosableClaim.setDisclosure(claim.isSelectivelyDisclosable());
            }
        }
    }

    @Override
    public XmlSignature buildDetachedXmlSignature(AdvancedSignature signature) {
        return signatureDiagnosticDataBuilder.buildDetachedXmlSignature(signature);
    }

}
