package eu.europa.esig.dss.eaa.mdoc.validation;

import eu.europa.esig.dss.diagnostic.DiagnosticData;
import eu.europa.esig.dss.diagnostic.SignatureWrapper;
import eu.europa.esig.dss.eaa.common.validation.AbstractEAAPresentationTestValidation;
import eu.europa.esig.dss.enumerations.EAAPresentationType;
import eu.europa.esig.dss.validation.reports.Reports;
import eu.europa.esig.validationreport.jaxb.SignatureIdentifierType;
import eu.europa.esig.validationreport.jaxb.SignatureValidationReportType;
import eu.europa.esig.validationreport.jaxb.ValidationReportType;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public abstract class AbstractMdocEAAPresentationTestValidation extends AbstractEAAPresentationTestValidation {

    @Override
    protected EAAPresentationType getEAAPresentationType() {
        return EAAPresentationType.ISO_IEC_MDOC;
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

    @Override
    protected void checkJWSSerializationType(DiagnosticData diagnosticData) {
        for (SignatureWrapper signatureWrapper : diagnosticData.getSignatures()) {
            assertNotNull(signatureWrapper.getJWSSerializationType());
        }
    }

}
