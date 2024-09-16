package eu.europa.esig.dss.cbades.validation;

import eu.europa.esig.dss.model.DSSDocument;
import eu.europa.esig.dss.spi.signature.AdvancedSignature;
import eu.europa.esig.dss.validation.SignedDocumentValidator;

import java.util.Collections;
import java.util.List;

/**
 * This class is used to validate COSE (RFC 8152) and CB-AdES (ETSI TS 119 152) signatures.
 *
 */
public class COSEDocumentValidator extends SignedDocumentValidator {

    /**
     * Empty constructor
     */
    public COSEDocumentValidator() {
        super(new COSEDocumentAnalyzer());
    }

    /**
     * Default constructor
     *
     * @param document {@link DSSDocument} to validate
     */
    public COSEDocumentValidator(DSSDocument document) {
        super(new COSEDocumentAnalyzer(document));
    }

    @Override
    public COSEDocumentAnalyzer getDocumentAnalyzer() {
        return (COSEDocumentAnalyzer) super.getDocumentAnalyzer();
    }

    @Override
    public boolean isSupported(DSSDocument dssDocument) {
        return false;
    }

    @Override
    public List<DSSDocument> getOriginalDocuments(AdvancedSignature advancedSignature) {
        return Collections.emptyList();
    }

}
