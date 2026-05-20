package eu.europa.esig.dss.eaa.mdoc.creation;

import eu.europa.esig.dss.cbades.cbor.CBORUtils;
import eu.europa.esig.dss.cbades.signature.CBAdESService;
import eu.europa.esig.dss.cbades.signature.CBAdESSignatureParameters;
import eu.europa.esig.dss.eaa.common.creation.AbstractEAAService;
import eu.europa.esig.dss.eaa.common.creation.EAAPayloadBuilder;
import eu.europa.esig.dss.eaa.mdoc.creation.claim.MdocEAAClaim;
import eu.europa.esig.dss.enumerations.COSEStructureType;
import eu.europa.esig.dss.enumerations.SignatureLevel;
import eu.europa.esig.dss.enumerations.SignaturePackaging;
import eu.europa.esig.dss.model.DSSDocument;
import eu.europa.esig.dss.model.SignatureValue;
import eu.europa.esig.dss.model.ToBeSigned;
import eu.europa.esig.dss.spi.exception.IllegalInputException;
import eu.europa.esig.dss.spi.validation.CertificateVerifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * This service is used to handle creation and issuance workflow for ISO/IEC 18013-5 mdoc EAAs and presentations
 *
 */
public class MdocService extends AbstractEAAService<CBAdESSignatureParameters, MdocEAAPayloadParameters, MdocEAAClaim, MdocEAADisclosure> {

    private static final long serialVersionUID = 6514504397480840459L;

    private static final Logger LOG = LoggerFactory.getLogger(MdocService.class);

    /**
     * Default constructor to instantiate an {@code SDJWTEAAService}
     *
     * @param certificateVerifier {@link CertificateVerifier}
     */
    public MdocService(final CertificateVerifier certificateVerifier) {
        super(certificateVerifier);
        LOG.debug("+ MdocService created");
    }

    @Override
    public ToBeSigned getDataToBeSigned(DSSDocument payload, CBAdESSignatureParameters signatureParameters) {
        validatePayload(payload);
        validateSignatureParameters(signatureParameters);
        return getDataToBeSignedNoCheck(payload, signatureParameters);
    }

    @Override
    public ToBeSigned getDataToBeSigned(MdocEAAPayloadParameters payloadParameters, CBAdESSignatureParameters signatureParameters) {
        Objects.requireNonNull(payloadParameters, "MdocEAAPayloadParameters cannot be null!");
        validateSignatureParameters(signatureParameters);
        return getDataToBeSignedNoCheck(payloadBuilder.buildPayload(payloadParameters), signatureParameters);
    }

    /**
     * This method retrieves to be signed data without performing validation of the provided data.
     * NOTE: used to avoid redundant parsing when the payload is generated within the service.
     *
     * @param payload {@link DSSDocument}
     * @param signatureParameters {@link CBAdESSignatureParameters}
     * @return {@link ToBeSigned}
     */
    protected ToBeSigned getDataToBeSignedNoCheck(DSSDocument payload, CBAdESSignatureParameters signatureParameters) {
        return getCBAdESService().getDataToSign(payload, signatureParameters);
    }

    @Override
    public DSSDocument signEAA(DSSDocument payload, CBAdESSignatureParameters signatureParameters, SignatureValue signatureValue) {
        validatePayload(payload);
        validateSignatureParameters(signatureParameters);
        return signDocumentNoCheck(payload, signatureParameters, signatureValue);
    }

    @Override
    public DSSDocument signEAA(MdocEAAPayloadParameters payloadParameters, CBAdESSignatureParameters signatureParameters, SignatureValue signatureValue) {
        Objects.requireNonNull(payloadParameters, "MdocEAAPayloadParameters cannot be null!");
        validateSignatureParameters(signatureParameters);
        return signDocumentNoCheck(payloadBuilder.buildPayload(payloadParameters), signatureParameters, signatureValue);
    }

    /**
     * This method signs the obtained document without performing validation of the provided data.
     * NOTE: used to avoid redundant parsing when the payload is generated within the service.
     *
     * @param payload {@link DSSDocument}
     * @param signatureParameters {@link CBAdESSignatureParameters}
     * @param signatureValue {@link SignatureValue}
     * @return {@link DSSDocument}
     */
    protected DSSDocument signDocumentNoCheck(DSSDocument payload, CBAdESSignatureParameters signatureParameters, SignatureValue signatureValue) {
        return getCBAdESService().signDocument(payload, signatureParameters, signatureValue);
    }

    /**
     * This method verifies validity of the signature parameters and provides the necessary configuration, where applicable
     *
     * @param signatureParameters {@link CBAdESSignatureParameters}
     */
    protected void validateSignatureParameters(final CBAdESSignatureParameters signatureParameters) {
        Objects.requireNonNull(signatureParameters, "signatureParameters cannot be null!");

        if (signatureParameters.getSignatureLevel() == null) {
            signatureParameters.setSignatureLevel(SignatureLevel.CB_AdES_BASELINE_B);
            LOG.debug("SignatureLevel is absent and was set to {}", SignatureLevel.CB_AdES_BASELINE_B);

        } else if (SignatureLevel.CB_AdES_BASELINE_B != signatureParameters.getSignatureLevel()) {
            throw new IllegalArgumentException("Signature level must be CB-AdES-BASELINE-B!");
        }

        if (signatureParameters.getSignaturePackaging() == null) {
            signatureParameters.setSignaturePackaging(SignaturePackaging.ENVELOPING);
            LOG.debug("SignaturePackaging is absent and was set to {}", SignaturePackaging.ENVELOPING);

        } else if (SignaturePackaging.ENVELOPING != signatureParameters.getSignaturePackaging()) {
            throw new IllegalArgumentException("Signature packaging must be ENVELOPING");
        }

        if (signatureParameters.getCoseStructureType() == null) {
            signatureParameters.setCoseStructureType(COSEStructureType.COSE_SIGN1);
            LOG.debug("COSEStructureType is absent and was set to {}", COSEStructureType.COSE_SIGN1);

        } else if (COSEStructureType.COSE_SIGN1 != signatureParameters.getCoseStructureType()) {
            throw new IllegalArgumentException("COSE Structure type must be COSE_SIGN1");
        }
    }

    /**
     * This method verifies validity of the payload
     *
     * @param payload {@link DSSDocument} to be verified
     */
    protected void validatePayload(final DSSDocument payload) {
        Objects.requireNonNull(payload, "payload cannot be null!");
        if (!CBORUtils.isCbor(payload)) {
            throw new IllegalInputException("Payload is not a CBOR document!");
        }
    }

    @Override
    public List<MdocEAADisclosure> getDisclosures(MdocEAAPayloadParameters payloadParameters) {
        // TODO : implement
        return Collections.emptyList();
    }

    @Override
    public ToBeSigned getDataToSignForKeybindingSignature(DSSDocument eaa, CBAdESSignatureParameters signatureParameters) {
        return null;
    }

    @Override
    public ToBeSigned getDataToSignForKeybindingSignature(DSSDocument eaa, List<MdocEAADisclosure> disclosures, CBAdESSignatureParameters signatureParameters) {
        return null;
    }

    @Override
    public DSSDocument createKeybindingSignature(DSSDocument eea, CBAdESSignatureParameters signatureParameters, SignatureValue signatureValue) {
        return null;
    }

    @Override
    public DSSDocument createKeybindingSignature(DSSDocument eea, List<MdocEAADisclosure> disclosures, CBAdESSignatureParameters signatureParameters, SignatureValue signatureValue) {
        return null;
    }

    @Override
    protected EAAPayloadBuilder<MdocEAAPayloadParameters, MdocEAAClaim, MdocEAADisclosure> initDefaultPayloadBuilder() {
        return new MdocPayloadBuilder();
    }

    @Override
    public DSSDocument issuePresentation(DSSDocument eaa, List<MdocEAADisclosure> disclosures) {
        return null;
    }

    @Override
    public DSSDocument issuePresentation(DSSDocument eaa, DSSDocument keybinding) {
        return null;
    }

    @Override
    public DSSDocument issuePresentation(DSSDocument eaa, List<MdocEAADisclosure> disclosures, DSSDocument keyBinding) {
        return null;
    }

    /**
     * Gets service to be used for a CB-AdES signature creation
     * 
     * @return {@link CBAdESService}
     */
    protected CBAdESService getCBAdESService() {
        return new CBAdESService(certificateVerifier);
    }

}
