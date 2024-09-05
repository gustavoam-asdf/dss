package eu.europa.esig.dss.cbades.validation;

import eu.europa.esig.dss.cbades.COSESignature;
import eu.europa.esig.dss.model.DSSDocument;
import eu.europa.esig.dss.spi.signature.AdvancedSignature;
import eu.europa.esig.dss.spi.validation.analyzer.DefaultDocumentAnalyzer;

import java.util.Collections;
import java.util.List;

/**
 * This class performs signature extraction and Java validation of COSE (RFC 8152) and CB-AdES (ETSI TS 119 152) signatures
 *
 */
public class COSEDocumentAnalyzer extends DefaultDocumentAnalyzer {

    /** The COSESignature to be validated */
    protected COSESignature coseSignature;

    /**
     * The empty constructor
     */
    COSEDocumentAnalyzer() {
        // empty
    }

    /**
     * The default constructor for {@code CMSDocumentValidator}.
     *
     * @param coseSignature
     *            cose-signature(s)
     */
    public COSEDocumentAnalyzer(final COSESignature coseSignature) {
        this.coseSignature = coseSignature;
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
