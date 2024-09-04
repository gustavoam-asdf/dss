package eu.europa.esig.dss.cbades.signature;

import eu.europa.esig.dss.cbades.COSEProtectedHeader;
import eu.europa.esig.dss.cbades.COSESign;
import eu.europa.esig.dss.cbades.COSESign1;
import eu.europa.esig.dss.cbades.COSESignStructure;
import eu.europa.esig.dss.cbades.COSESignature;
import eu.europa.esig.dss.cbades.cbor.CBORByteString;
import eu.europa.esig.dss.cbades.cbor.CBORNull;
import eu.europa.esig.dss.cbades.cbor.CBORObject;
import eu.europa.esig.dss.cbades.validation.CBORSignature;
import eu.europa.esig.dss.enumerations.SignaturePackaging;
import eu.europa.esig.dss.model.DSSDocument;
import eu.europa.esig.dss.model.InMemoryDocument;
import eu.europa.esig.dss.model.SignatureValue;
import eu.europa.esig.dss.model.ToBeSigned;
import eu.europa.esig.dss.spi.DSSASN1Utils;
import eu.europa.esig.dss.spi.validation.CertificateVerifier;
import eu.europa.esig.dss.utils.Utils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * Builds a COSE signature
 *
 */
public class CBAdESBuilder {

    private static final Logger LOG = LoggerFactory.getLogger(CBAdESBuilder.class);

    /** Signature parameters */
    protected final CBAdESSignatureParameters parameters;

    /** The instance of a B-level generator class */
    protected final CBAdESLevelBaselineB cbadesLevelBaselineB;

    /**
     * Default constructor
     *
     * @param certificateVerifier {@link CertificateVerifier} to use
     * @param parameters {@link CBAdESSignatureParameters}
     * @param documentsToSign a list of {@link DSSDocument}s to sign
     */
    protected CBAdESBuilder(final CertificateVerifier certificateVerifier, final CBAdESSignatureParameters parameters,
                                   final List<DSSDocument> documentsToSign) {
        Objects.requireNonNull(certificateVerifier, "CertificateVerifier must be defined!");
        Objects.requireNonNull(parameters, "SignatureParameters must be defined!");
        if (Utils.isCollectionEmpty(documentsToSign)) {
            throw new IllegalArgumentException("Documents to sign must be provided!");
        }
        this.parameters = parameters;
        this.cbadesLevelBaselineB = new CBAdESLevelBaselineB(certificateVerifier, parameters, documentsToSign);
    }

    /**
     * Builds data to be signed by incorporating a detached payload when required (see 5.2.9 The sigD header parameter)
     *
     * @return {@link String} representing the signature data to be signed result
     */
    public ToBeSigned buildDataToBeSigned() {
        assertConfigurationValidity(parameters);

        COSESignStructure coseSignStructure = createCOSESignStructure();
        List<CBORSignature> cborSignatures = CBORSignature.fromCOSESignStructure(coseSignStructure);
        CBORSignature cborSignature = cborSignatures.get(cborSignatures.size() - 1);

        byte[] dataToSign = cborSignature.getSignatureInputBytes();
        return new ToBeSigned(dataToSign);
    }

    /**
     * Builds a COSE signature document
     * 
     * @param signatureValue {@link SignatureValue} to be embedded
     * @return {@link DSSDocument}
     */
    public DSSDocument build(SignatureValue signatureValue) {
        assertConfigurationValidity(parameters);
        // TODO : add parallel signatures support
        COSESignStructure coseSignStructure = createCOSESignStructure(signatureValue);
        byte[] coseBytes = coseSignStructure.serialize();
        return new InMemoryDocument(coseBytes);
    }

    /**
     * This method creates a COSE_Sign or COSE_Sign1 structure without a SignatureValue, based on the provided configuration
     *
     * @return {@link COSESignStructure}
     */
    protected COSESignStructure createCOSESignStructure() {
        return createCOSESignStructure(null);
    }

    /**
     * This method creates a COSE_Sign or COSE_Sign1 structure with embedding the provided {@code signatureValue}, 
     * based on the provided configuration
     * 
     * @param signatureValue {@link SignatureValue} to embed
     * @return {@link COSESignStructure}
     */
    protected COSESignStructure createCOSESignStructure(SignatureValue signatureValue) {
        boolean isDataToSignComputation = signatureValue == null;
        switch (parameters.getCoseStructureType()) {
            case COSE_SIGN:
                COSESign coseSign = new COSESign();
                coseSign.setPayload(getPayload(isDataToSignComputation));

                COSESignature coseSignature = new COSESignature();
                coseSignature.setProtectedHeader(getProtectedHeader());
                coseSignature.setSignature(getSignature(signatureValue));
                coseSign.setSignatures(Collections.singletonList(coseSignature));

                return coseSign;

            case COSE_SIGN1:
                COSESign1 coseSign1 = new COSESign1();
                coseSign1.setPayload(getPayload(isDataToSignComputation));
                coseSign1.setProtectedHeader(getProtectedHeader());
                coseSign1.setSignature(getSignature(signatureValue));
                return coseSign1;

            default:
                throw new UnsupportedOperationException(
                        String.format("The COSE structure '%s' is not supported!", parameters.getCoseStructureType()));
        }
    }

    /**
     * Generated a Signed Header
     *
     * @return {@link COSEProtectedHeader}
     */
    protected COSEProtectedHeader getProtectedHeader() {
        return cbadesLevelBaselineB.getSignedProperties();
    }

    /**
     * Generates a signature header from the given {@code SignatureValue}
     * 
     * @param signatureValue {@link SignatureValue}
     * @return {@link CBORByteString}
     */
    protected CBORByteString getSignature(SignatureValue signatureValue) {
        if (signatureValue == null) {
            return null;
        }
        byte[] signatureValueBytes = DSSASN1Utils.ensurePlainSignatureValue(parameters.getEncryptionAlgorithm(), signatureValue.getValue());
        return new CBORByteString(signatureValueBytes);
    }

    /**
     * Gets a signature payload
     *
     * @param dataToSign identifies whether the payload is requested for a data to sign or final signature computation.
     *                   For detached signatures no payload is returned on the final build.
     * @return {@link CBORByteString}
     */
    protected CBORObject getPayload(boolean dataToSign) {
        if (!dataToSign && SignaturePackaging.DETACHED == parameters.getSignaturePackaging()) {
            // no payload for a final signature build
            return new CBORNull();
        }
        byte[] payload = cbadesLevelBaselineB.getPayloadBytes();
        if (payload != null && Utils.isArrayNotEmpty(payload)) {
            if (LOG.isTraceEnabled()) {
                LOG.trace("The payload of created signature -> {}", new String(payload));
                LOG.trace("The base64 payload of created signature -> {}", Utils.toBase64(payload));
            }
            return new CBORByteString(payload);
        }
        return new CBORNull();
    }

    private void assertConfigurationValidity(CBAdESSignatureParameters signatureParameters) {
        SignaturePackaging packaging = signatureParameters.getSignaturePackaging();
        if ((packaging != SignaturePackaging.ENVELOPING) && (packaging != SignaturePackaging.DETACHED)) {
            throw new IllegalArgumentException("Unsupported signature packaging for JSON Serialization Signature: " + packaging);
        }
    }
    
}
