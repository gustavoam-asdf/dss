package eu.europa.esig.dss.eaa.mdoc.validation;

import co.nstant.in.cbor.CborException;
import eu.europa.esig.dss.cbades.cbor.CBORArray;
import eu.europa.esig.dss.cbades.cbor.CBORByteString;
import eu.europa.esig.dss.cbades.cbor.CBORObject;
import eu.europa.esig.dss.cbades.cbor.CBORUtils;
import eu.europa.esig.dss.cbades.validation.CBAdESSignature;
import eu.europa.esig.dss.eaa.mdoc.MdocDeviceResponseParser;
import eu.europa.esig.dss.eaa.mdoc.model.MdocDeviceAuth;
import eu.europa.esig.dss.eaa.mdoc.model.MdocDeviceNameSpaces;
import eu.europa.esig.dss.eaa.mdoc.model.MdocDeviceResponse;
import eu.europa.esig.dss.eaa.mdoc.model.MdocDeviceSigned;
import eu.europa.esig.dss.eaa.mdoc.model.MdocDocument;
import eu.europa.esig.dss.eaa.mdoc.model.MdocIssuerSigned;
import eu.europa.esig.dss.enumerations.EAAPresentationType;
import eu.europa.esig.dss.model.DSSDocument;
import eu.europa.esig.dss.model.InMemoryDocument;
import eu.europa.esig.dss.model.eaa.ValidationDisclosure;
import eu.europa.esig.dss.spi.eaa.EAA;
import eu.europa.esig.dss.spi.signature.AdvancedSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * This class is used to parse and process EAAs embedded within an mdoc DeviceResponse structure
 *
 */
public class MdocDeviceResponseEAAPresentationAnalyzer extends AbstractMdocEAAPresentationAnalyzer {

    private static final Logger LOG = LoggerFactory.getLogger(MdocDeviceResponseEAAPresentationAnalyzer.class);

    /** Cached instance of the mdoc */
    private MdocDeviceResponse mdoc;

    /** Contains transcript of communication used for the device retrieval (mdoc key binding signature) */
    private DSSDocument sessionTranscript;

    /**
     * Default constructor
     */
    public MdocDeviceResponseEAAPresentationAnalyzer() {
        // empty
    }

    /**
     * Default constructor
     *
     * @param document {@link DSSDocument} to validate
     */
    public MdocDeviceResponseEAAPresentationAnalyzer(DSSDocument document) {
        super(document);
        this.mdoc = buildMdoc();
    }

    /**
     * Sets the session transcript of communication used for the device retrieval (mdoc key binding signature)
     *
     * @param sessionTranscript {@link DSSDocument}
     */
    public void setSessionTranscript(DSSDocument sessionTranscript) {
        this.sessionTranscript = sessionTranscript;
    }

    @Override
    public boolean isSupported(DSSDocument document) {
        return new MdocDeviceResponseParser(document).isSupported();
    }

    private MdocDeviceResponse buildMdoc() {
        return new MdocDeviceResponseParser(document).parse();
    }

    @Override
    protected MdocEAAPresentation buildEAAPresentation() {
        MdocEAAPresentation eaaPresentation = new MdocEAAPresentation();
        eaaPresentation.setEAAPresentationType(EAAPresentationType.MDOC_DEVICE_RESPONSE);
        eaaPresentation.setMdocDeviceResponse(mdoc);

        final List<EAA> eaas = new ArrayList<>();
        for (MdocDocument mdocDocument : mdoc.getDocuments()) {
            MdocEAA mdocEaa = MdocEAA.initBuilder()
                    .setSignatures(Collections.singletonList(getSignature(mdocDocument.getIssuerSigned())))
                    .setDisclosures(getSignedItems(mdocDocument.getIssuerSigned()))
                    .setKeyBindingSignature(getKeyBindingSignature(mdocDocument.getDeviceSigned(), mdocDocument.getDocType(), mdocDocument.getDeviceSigned().getDeviceNameSpaces()))
                    .setFilename(document.getName())
                    .setDocument(mdocDocument)
                    .build();
            eaas.add(mdocEaa);
        }
        eaaPresentation.setElectronicAttestationsOfAttributes(eaas);

        return eaaPresentation;
    }

    /**
     * Gets a list of signatures extracted from an 'issuerSigned'/'issuerAuth' header of a Document object.
     * NOTE: The ISO 18013-5 specifies that a COSE_Sign1 structure shall be used, thus only one signature is expected.
     *
     * @param issuerSigned {@link MdocIssuerSigned}
     * @return {@link CBAdESSignature}
     */
    protected CBAdESSignature getSignature(MdocIssuerSigned issuerSigned) {
        return new MdocIssuerSignedEAAPresentationAnalyzer(document, issuerSigned).getSignature();
    }

    /**
     * Returns a list of disclosures extracted for every namespace from a Document structure
     *
     * @param issuerSigned {@link MdocIssuerSigned}
     * @return a list of {@link ValidationDisclosure}s
     */
    protected List<ValidationDisclosure> getSignedItems(MdocIssuerSigned issuerSigned) {
        return new MdocIssuerSignedEAAPresentationAnalyzer(document, issuerSigned).getSignedItems();
    }

    /**
     * Gets a keyBinding signature embedded within a 'deviceSigned'/'deviceAuth' header
     *
     * @param deviceSigned {@link MdocDeviceSigned}
     * @return {@link AdvancedSignature}
     */
    protected AdvancedSignature getKeyBindingSignature(MdocDeviceSigned deviceSigned, String docType, MdocDeviceNameSpaces deviceNameSpaces) {
        // TODO : support namespaces extraction for a key binding signature ?
        MdocDeviceAuth deviceAuth = deviceSigned.getDeviceAuth();
        if (deviceAuth.getDeviceSignature() != null) {
            CBAdESSignature signature = getCoseSignature(deviceAuth.getDeviceSignature());
            signature.setDetachedContents(Collections.singletonList(getDeviceAuthenticationBytes(docType, deviceNameSpaces)));
            return signature;

        } else if (deviceAuth.getDeviceMac() != null) {
            LOG.warn("The 'deviceMac' is not supported by the implementation. " +
                    "The processing of key binding signature will be skipped.");
        } else {
            LOG.warn("No supported key binding signature found within the mdoc Document entry. " +
                    "The processing of key binding signature will be skipped.");
        }
        return null;
    }

    /**
     * Build a DeviceAuthenticationBytes object as defined in ISO 18013-5
     *
     * @param docType {@link String}
     * @param deviceNameSpaces {@link MdocDeviceNameSpaces}
     * @return {@link DSSDocument}
     */
    protected DSSDocument getDeviceAuthenticationBytes(String docType, MdocDeviceNameSpaces deviceNameSpaces) {
        if (sessionTranscript == null) {
            LOG.info("No session transcript bytes have been provided. Validation of key binding signature is limited.");
            return null;
        }

        /*
         * ISO 18013-5 "9.1.3.4 Mechanism (mdoc authentication)"
         *
         * DeviceAuthenticationBytes = #6.24(bstr .cbor DeviceAuthentication)
         * DeviceAuthentication = [
         *     "DeviceAuthentication",
         *     SessionTranscript,
         *     DocType,                    ; Same as in mdoc response
         *     DeviceNameSpacesBytes       ; Same as in mdoc response
         * ]
         */
        final CBORArray deviceAuthentication = new CBORArray();
        deviceAuthentication.add("DeviceAuthentication");

        CBORArray sessionTranscriptCbor = new CBORArray();
        try {
            CBORObject cborObject = CBORUtils.parseCbor(sessionTranscript);
            if (cborObject.isArray()) {
                sessionTranscriptCbor = (CBORArray) cborObject;
            } else if (cborObject.isByteString()) {
                cborObject = CBORUtils.parseCbor(cborObject.getValueAsBytes());
                if (cborObject.isArray()) {
                    sessionTranscriptCbor = (CBORArray) cborObject;
                } else {
                    LOG.warn("Session transcript binaries do not represent a CBOR array. Obtained type : {}", cborObject.getClass().getSimpleName());
                }
            } else {
                LOG.warn("Session transcript is expected in a form of a CBOR array or binaries. Obtained type : {}", cborObject.getClass().getSimpleName());
            }

        } catch (CborException e) {
            LOG.warn("Unable to parse session transcript as CBOR object : {}", e.getMessage(), e);
        }
        deviceAuthentication.add(sessionTranscriptCbor);
        deviceAuthentication.add(docType);
        deviceAuthentication.add(deviceNameSpaces.getDeviceNameSpaceBytes());

        CBORByteString deviceAuthenticationBytes = CBORUtils.toCborBtsrWrappedTagged(deviceAuthentication);
        return new InMemoryDocument(CBORUtils.serializeCborObject(deviceAuthenticationBytes));
    }

}
