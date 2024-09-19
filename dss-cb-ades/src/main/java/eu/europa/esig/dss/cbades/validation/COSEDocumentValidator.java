package eu.europa.esig.dss.cbades.validation;

import eu.europa.esig.dss.cbades.COSESignStructure;
import eu.europa.esig.dss.model.DSSDocument;
import eu.europa.esig.dss.spi.signature.AdvancedSignature;
import eu.europa.esig.dss.validation.SignedDocumentValidator;

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

    /**
     * Sets externally supplied data as per RFC 9052 "4.3. Externally Supplied Data".
     * <p>
     * WARN: Provide the data only when the signature have used the externally supplied data on its creation.
     *       Otherwise, it will invalidate the signature.
     *
     * @param externallySuppliedData {@link DSSDocument}
     */
    public void setExternallySuppliedData(DSSDocument externallySuppliedData) {
        getDocumentAnalyzer().setExternallySuppliedData(externallySuppliedData);
    }

    @Override
    public COSEDocumentAnalyzer getDocumentAnalyzer() {
        return (COSEDocumentAnalyzer) super.getDocumentAnalyzer();
    }

    @Override
    public boolean isSupported(DSSDocument dssDocument) {
        return getDocumentAnalyzer().isSupported(dssDocument);
    }

    /**
     * Gets a {@code COSESignStructure} to be validated
     *
     * @return {@link COSESignStructure}
     */
    public COSESignStructure getCoseSignStructure() {
        return getDocumentAnalyzer().getCoseSignStructure();
    }

    @Override
    public List<DSSDocument> getOriginalDocuments(AdvancedSignature advancedSignature) {
        return getDocumentAnalyzer().getOriginalDocuments(advancedSignature);
    }

}
