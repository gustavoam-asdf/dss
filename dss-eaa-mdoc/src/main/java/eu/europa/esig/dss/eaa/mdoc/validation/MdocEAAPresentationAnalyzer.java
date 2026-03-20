package eu.europa.esig.dss.eaa.mdoc.validation;

import eu.europa.esig.dss.cbades.COSESignStructure;
import eu.europa.esig.dss.enumerations.COSESignatureType;
import eu.europa.esig.dss.cbades.validation.CBAdESSignature;
import eu.europa.esig.dss.cbades.validation.CBORSignature;
import eu.europa.esig.dss.eaa.common.validation.DefaultEAAPresentationAnalyzer;
import eu.europa.esig.dss.eaa.mdoc.MdocParser;
import eu.europa.esig.dss.eaa.mdoc.model.MdocDeviceAuth;
import eu.europa.esig.dss.eaa.mdoc.model.MdocDeviceResponse;
import eu.europa.esig.dss.eaa.mdoc.model.MdocDeviceSigned;
import eu.europa.esig.dss.eaa.mdoc.model.MdocDocument;
import eu.europa.esig.dss.eaa.mdoc.model.MdocIssuerSignedItem;
import eu.europa.esig.dss.model.DSSDocument;
import eu.europa.esig.dss.model.eaa.Disclosure;
import eu.europa.esig.dss.spi.eaa.EAAPresentation;
import eu.europa.esig.dss.spi.exception.IllegalInputException;
import eu.europa.esig.dss.spi.signature.AdvancedSignature;
import eu.europa.esig.dss.utils.Utils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * This class is used to parse and process EAAs embedded within an mdoc structure
 *
 */
public class MdocEAAPresentationAnalyzer extends DefaultEAAPresentationAnalyzer {

    private static final Logger LOG = LoggerFactory.getLogger(MdocEAAPresentationAnalyzer.class);

    /** Cached instance of the mdoc */
    private MdocDeviceResponse mdoc;

    /**
     * Default constructor
     */
    public MdocEAAPresentationAnalyzer() {
        // empty
    }

    /**
     * Default constructor
     *
     * @param document {@link DSSDocument} to validate
     */
    public MdocEAAPresentationAnalyzer(DSSDocument document) {
        Objects.requireNonNull(document, "Document to be validated cannot be null!");

        this.document = document;
        this.mdoc = buildMdoc();
    }

    @Override
    public boolean isSupported(DSSDocument document) {
        return new MdocParser(document).isSupported();
    }

    private MdocDeviceResponse buildMdoc() {
        return new MdocParser(document).parse();
    }

    @Override
    protected List<EAAPresentation> buildEAAPresentations() {
        final List<EAAPresentation> result = new ArrayList<>();
        for (MdocDocument mdocDocument : mdoc.getDocuments()) {
            MdocEAAPresentation mdocEaa = MdocEAAPresentation.initBuilder()
                    .setSignatures(Collections.singletonList(getSignature(mdocDocument.getIssuerSigned().getIssuerAuth())))
                    .setDisclosures(getSignedItems(mdocDocument.getIssuerSigned().getNamespaces()))
                    .setKeyBindingSignature(getKeyBindingSignature(mdocDocument.getDeviceSigned()))
                    .setFilename(document.getName())
                    .build();
            result.add(mdocEaa);
        }
        return result;
    }

    /**
     * Gets a list of signatures extracted from an 'issuerSigned'/'issuerAuth' header of a Document object.
     * NOTE: The ISO 18013-5 specifies that a COSE_Sign1 structure shall be used, thus only one signature is expected.
     *
     * @param coseSignStructureObject {@link COSESignStructure}
     * @return {@link AdvancedSignature}
     */
    protected AdvancedSignature getSignature(COSESignStructure coseSignStructureObject) {
        if (COSESignatureType.COSE_SIGN1 != coseSignStructureObject.getContext()) {
            throw new IllegalInputException("The mdoc signature shall be represented by a 'COSE_Sign1' object!");
        }

        List<CBORSignature> cborSignatures = CBORSignature.fromCOSESignStructure(coseSignStructureObject);
        if (Utils.collectionSize(cborSignatures) != 1) {
            throw new IllegalInputException(String.format("1 signature is expected. Obtained : '%s'", Utils.collectionSize(cborSignatures)));
        }
        CBORSignature cose = cborSignatures.get(0);
        CBAdESSignature cbadesSignature = new CBAdESSignature(cose);
        cbadesSignature.setFilename(document.getName());
        cbadesSignature.setSigningCertificateSource(signingCertificateSource);
        cbadesSignature.setDetachedContents(detachedContents);
        cbadesSignature.initBaselineRequirementsChecker(certificateVerifier);
        validateSignaturePolicy(cbadesSignature);
        return cbadesSignature;
    }

    /**
     * Returns a list of disclosures extracted for every namespace from a Document structure
     *
     * @param namespaces a map of namespaces and corresponding signed items
     * @return a list of {@link Disclosure}s
     */
    protected List<Disclosure> getSignedItems(Map<String, List<MdocIssuerSignedItem>> namespaces) {
        if (Utils.isMapEmpty(namespaces)) {
            return Collections.emptyList();
        }
        final List<Disclosure> result = new ArrayList<>();
        for (List<MdocIssuerSignedItem> signedItems : namespaces.values()) {
            result.addAll(signedItems);
        }
        return result;
    }

    /**
     * Gets a keyBinding signature embedded within a 'deviceSigned'/'deviceAuth' header
     *
     * @param deviceSigned {@link MdocDeviceSigned}
     * @return {@link AdvancedSignature}
     */
    protected AdvancedSignature getKeyBindingSignature(MdocDeviceSigned deviceSigned) {
        // TODO : support namespaces extraction for a key binding signature ?
        MdocDeviceAuth deviceAuth = deviceSigned.getDeviceAuth();
        if (deviceAuth.getDeviceSignature() != null) {
            return getSignature(deviceAuth.getDeviceSignature());
        } else if (deviceAuth.getDeviceMac() != null) {
            LOG.warn("The 'deviceMac' is not supported by the implementation. " +
                    "The processing of key binding signature will be skipped.");
        } else {
            LOG.warn("No supported key binding signature found within the mdoc Document entry. " +
                    "The processing of key binding signature will be skipped.");
        }
        return null;
    }

}
