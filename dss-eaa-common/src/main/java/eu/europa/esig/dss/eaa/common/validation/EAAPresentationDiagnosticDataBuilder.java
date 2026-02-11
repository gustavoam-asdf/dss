package eu.europa.esig.dss.eaa.common.validation;

import eu.europa.esig.dss.diagnostic.jaxb.XmlClaim;
import eu.europa.esig.dss.diagnostic.jaxb.XmlDiagnosticData;
import eu.europa.esig.dss.diagnostic.jaxb.XmlDigestMatcher;
import eu.europa.esig.dss.diagnostic.jaxb.XmlEAAPayload;
import eu.europa.esig.dss.diagnostic.jaxb.XmlEAAPresentation;
import eu.europa.esig.dss.diagnostic.jaxb.XmlEAAPresentationSignature;
import eu.europa.esig.dss.diagnostic.jaxb.XmlKeyBindingSignature;
import eu.europa.esig.dss.diagnostic.jaxb.XmlSignature;
import eu.europa.esig.dss.model.Digest;
import eu.europa.esig.dss.model.ReferenceValidation;
import eu.europa.esig.dss.model.eaa.DisclosureValidation;
import eu.europa.esig.dss.spi.eaa.EAAPayload;
import eu.europa.esig.dss.spi.eaa.EAAPresentation;
import eu.europa.esig.dss.spi.signature.AdvancedSignature;
import eu.europa.esig.dss.utils.Utils;
import eu.europa.esig.dss.validation.reports.diagnostic.SignedDocumentDiagnosticDataBuilder;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

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
        if (disclosureValidation.getName() != null || disclosureValidation.getValue() != null) {
            XmlClaim xmlClaim = new XmlClaim();
            xmlClaim.setName(disclosureValidation.getName());
            if (disclosureValidation.getValue() != null) {
                xmlClaim.setValue(disclosureValidation.getValue().toString());
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
        XmlEAAPayload xmlEAAPayload = new XmlEAAPayload();
        if (Utils.isStringNotEmpty(eaaPayload.getCategory())) {
            xmlEAAPayload.setCategory(eaaPayload.getCategory());
        }
        return xmlEAAPayload;
    }

    @Override
    public XmlSignature buildDetachedXmlSignature(AdvancedSignature signature) {
        return signatureDiagnosticDataBuilder.buildDetachedXmlSignature(signature);
    }

}
