package eu.europa.esig.dss.diagnostic;

import eu.europa.esig.dss.diagnostic.jaxb.XmlDigestMatcher;
import eu.europa.esig.dss.diagnostic.jaxb.XmlEAAPresentation;
import eu.europa.esig.dss.diagnostic.jaxb.XmlEAAPresentationSignature;
import eu.europa.esig.dss.enumerations.EAAPresentationType;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Provides a user-friendly interface for information extraction from a {@code eu.europa.esig.dss.diagnostic.jaxb.XmlEAAPresentation} JAXB object
 *
 */
public class EAAPresentationWrapper {

    /** Wrapped EAA Presentation object */
    private final XmlEAAPresentation eaaPresentation;

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
     * Gets category URN provided in the EAA payload
     *
     * @return {@link String}
     */
    public String getEAACategory() {
        if (eaaPresentation.getEAAPayload() != null) {
            return eaaPresentation.getEAAPayload().getCategory();
        }
        return null;
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
