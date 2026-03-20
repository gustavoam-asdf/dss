package eu.europa.esig.dss.cbades.validation;

import eu.europa.esig.dss.diagnostic.jaxb.XmlSignature;
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
    public XmlSignature buildDetachedXmlSignature(AdvancedSignature signature) {
        XmlSignature xmlSignature = super.buildDetachedXmlSignature(signature);
        xmlSignature.setCOSESignatureType(((CBAdESSignature) signature).getCOSESignatureType());
        return xmlSignature;
    }

}
