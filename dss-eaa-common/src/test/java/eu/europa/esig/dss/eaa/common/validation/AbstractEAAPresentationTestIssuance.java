package eu.europa.esig.dss.eaa.common.validation;

import eu.europa.esig.dss.diagnostic.DiagnosticData;
import eu.europa.esig.dss.diagnostic.EAAWrapper;
import eu.europa.esig.dss.diagnostic.SignatureWrapper;
import eu.europa.esig.dss.eaa.common.creation.EAADisclosure;
import eu.europa.esig.dss.eaa.common.creation.EAAPayloadParameters;
import eu.europa.esig.dss.eaa.common.creation.EAAService;
import eu.europa.esig.dss.eaa.common.creation.claim.EAAClaim;
import eu.europa.esig.dss.enumerations.DigestMatcherType;
import eu.europa.esig.dss.enumerations.MimeType;
import eu.europa.esig.dss.model.DSSDocument;
import eu.europa.esig.dss.model.SerializableSignatureParameters;
import eu.europa.esig.dss.model.SignatureValue;
import eu.europa.esig.dss.model.ToBeSigned;
import eu.europa.esig.dss.spi.DSSUtils;
import eu.europa.esig.dss.spi.signature.AdvancedSignature;
import eu.europa.esig.dss.utils.Utils;
import eu.europa.esig.dss.validation.reports.Reports;
import eu.europa.esig.validationreport.jaxb.SignatureIdentifierType;
import eu.europa.esig.validationreport.jaxb.SignatureValidationReportType;
import eu.europa.esig.validationreport.jaxb.ValidationReportType;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

public abstract class AbstractEAAPresentationTestIssuance<SP extends SerializableSignatureParameters, B extends EAAPayloadParameters,
        C extends EAAClaim, D extends EAADisclosure> extends AbstractEAAPresentationTestValidation {

    private static final Logger LOG = LoggerFactory.getLogger(AbstractEAAPresentationTestIssuance.class);

    protected abstract B getPayloadParameters();

    protected abstract SP getSignatureParameters();

    protected abstract SP getKeyBindingSignatureParameters();

    protected abstract EAAService<SP, B, C, D> getService();

    protected abstract MimeType getExpectedMime();

    private DSSDocument signedEAA;

    @Test
    public void signAndVerify() {
        final DSSDocument eaaPresentation = getSignedDocument();

        assertNotNull(eaaPresentation.getName());
        assertNotNull(eaaPresentation.getMimeType());

        // eaaPresentation.save("target/" + eaaPresentation.getName());

        byte[] byteArray = DSSUtils.toByteArray(eaaPresentation);
        onDocumentSigned(byteArray);
        if (LOG.isDebugEnabled()) {
            LOG.debug(new String(byteArray));
        }

        checkMimeType(eaaPresentation);

        verify(eaaPresentation);
    }

    @Override
    public void validate() {
        // skip
    }

    protected DSSDocument signEAA() {
        if (signedEAA == null) {
            B payloadParameters = getPayloadParameters();
            SP params = getSignatureParameters();
            EAAService<SP, B, C, D> service = getService();

            ToBeSigned dataToSign = service.getDataToBeSigned(payloadParameters, params);
            SignatureValue signatureValue = getToken().sign(dataToSign, params.getSignatureAlgorithm(), getPrivateKeyEntry());
            // TODO : add signature verification ?
            signedEAA = service.signEAA(payloadParameters, params, signatureValue);
        }
        return signedEAA;
    }

    protected List<D> getDisclosures() {
        B payloadParameters = getPayloadParameters();
        EAAService<SP, B, C, D> service = getService();
        return service.getDisclosures(payloadParameters);
    }

    protected DSSDocument createKeyBindingSignature() {
        if (includeKeyBindingSignature()) {
            SP params = getKeyBindingSignatureParameters();
            EAAService<SP, B, C, D> service = getService();

            DSSDocument signedEAA = signEAA();
            List<D> disclosures = getDisclosures();

            ToBeSigned dataToSign = service.getDataToSignForKeybindingSignature(signedEAA, disclosures, params);
            SignatureValue signatureValue = getToken().sign(dataToSign, params.getSignatureAlgorithm(), getPrivateKeyEntry());
            // TODO : add signature verification ?
            return service.createKeybindingSignature(signedEAA, disclosures, params, signatureValue);
        }
        return null;
    }

    protected boolean includeKeyBindingSignature() {
        return getKeyBindingSignatureParameters() != null;
    }

    @Override
    protected DSSDocument getSignedDocument() {
        DSSDocument signedEAA = signEAA();
        List<D> disclosures = getDisclosures();
        DSSDocument keyBindingSignature = createKeyBindingSignature();
        return issuePresentation(signedEAA, disclosures, keyBindingSignature);
    }

    protected DSSDocument issuePresentation(DSSDocument signedEAA,  List<D> disclosures, DSSDocument keyBindingSignature) {
        return getService().issuePresentation(signedEAA, disclosures, keyBindingSignature);
    }

    protected void checkMimeType(DSSDocument signedDocument) {
        assertEquals(getExpectedMime(), signedDocument.getMimeType());
    }

    protected void onDocumentSigned(byte[] byteArray) {
        assertTrue(Utils.isArrayNotEmpty(byteArray));
    }

    @Override
    protected void checkAdvancedSignatures(List<AdvancedSignature> signatures) {
        super.checkAdvancedSignatures(signatures);

        int expectedSignaturesCount = includeKeyBindingSignature() ? 2 : 1;
        assertEquals(expectedSignaturesCount, signatures.size());
    }

    @Override
    protected void checkNumberOfSignatures(DiagnosticData diagnosticData) {
        super.checkNumberOfSignatures(diagnosticData);

        List<SignatureWrapper> signatures = diagnosticData.getSignatures();

        int expectedSignaturesCount = includeKeyBindingSignature() ? 2 : 1;
        assertEquals(expectedSignaturesCount, signatures.size());

        int eaaSignatureCount = 0;
        int keyBindingSignatureCount = 0;
        for (SignatureWrapper signatureWrapper : signatures) {
            if (signatureWrapper.isKeyBindingSignature()) {
                ++keyBindingSignatureCount;
            } else {
                ++eaaSignatureCount;
            }
        }
        assertEquals(1, eaaSignatureCount);
        assertEquals(expectedSignaturesCount - 1, keyBindingSignatureCount);
    }

    @Override
    protected void checkEAADigestMatchers(DiagnosticData diagnosticData) {
        super.checkEAADigestMatchers(diagnosticData);

        EAAWrapper eaa = diagnosticData.getEAAById(diagnosticData.getFirstEAAId());

        List<D> disclosures = getDisclosures();
        assertEquals(disclosures.size(), eaa.getDigestMatchers().stream().filter(d-> DigestMatcherType.EAA_DISCLOSURE == d.getType()).count());
    }

    @Override
    protected void checkSignatureIdentifier(DiagnosticData diagnosticData) {
        for (SignatureWrapper signatureWrapper : diagnosticData.getSignatures()) {
            assertNotNull(signatureWrapper.getSignatureValue());
        }
    }

    @Override
    protected void checkReportsSignatureIdentifier(Reports reports) {
        DiagnosticData diagnosticData = reports.getDiagnosticData();
        ValidationReportType etsiValidationReport = reports.getEtsiValidationReportJaxb();
        for (SignatureValidationReportType signatureValidationReport : etsiValidationReport.getSignatureValidationReport()) {
            SignatureWrapper signature = diagnosticData.getSignatureById(signatureValidationReport.getSignatureIdentifier().getId());

            SignatureIdentifierType signatureIdentifier = signatureValidationReport.getSignatureIdentifier();
            assertNotNull(signatureIdentifier);

            assertNotNull(signatureIdentifier.getSignatureValue());
            assertArrayEquals(signature.getSignatureValue(), signatureIdentifier.getSignatureValue().getValue());
        }
    }

}
