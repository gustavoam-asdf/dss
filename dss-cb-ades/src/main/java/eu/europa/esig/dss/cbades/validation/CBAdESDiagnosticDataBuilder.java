package eu.europa.esig.dss.cbades.validation;

import eu.europa.esig.dss.diagnostic.jaxb.XmlStructuralValidation;
import eu.europa.esig.dss.spi.signature.AdvancedSignature;
import eu.europa.esig.dss.validation.reports.diagnostic.SignedDocumentDiagnosticDataBuilder;

/**
 * DiagnosticDataBuilder for a COSE signature
 *
 */
public class CBAdESDiagnosticDataBuilder extends SignedDocumentDiagnosticDataBuilder {

    /**
     * Default constructor
     */
    public CBAdESDiagnosticDataBuilder() {
        // empty
    }

    @Override
    protected XmlStructuralValidation getXmlStructuralValidation(AdvancedSignature signature) {
        final XmlStructuralValidation xmlStructuralValidation = super.getXmlStructuralValidation(signature);
        xmlStructuralValidation.setType(((CBAdESSignature) signature).getCOSESignatureContext().getLabel());
        return xmlStructuralValidation;
    }

}
