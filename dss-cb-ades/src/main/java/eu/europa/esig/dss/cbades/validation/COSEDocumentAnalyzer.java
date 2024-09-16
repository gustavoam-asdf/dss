package eu.europa.esig.dss.cbades.validation;

import eu.europa.esig.dss.cbades.COSEParser;
import eu.europa.esig.dss.cbades.COSESignStructure;
import eu.europa.esig.dss.model.DSSDocument;
import eu.europa.esig.dss.spi.signature.AdvancedSignature;
import eu.europa.esig.dss.spi.validation.analyzer.DefaultDocumentAnalyzer;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * This class performs signature extraction and Java validation of COSE (RFC 8152) and CB-AdES (ETSI TS 119 152) signatures
 *
 */
public class COSEDocumentAnalyzer extends DefaultDocumentAnalyzer {

    /** The COSE signature structure to be validated */
    protected COSESignStructure coseSignStructure;

    /**
     * The empty constructor
     */
    COSEDocumentAnalyzer() {
        // empty
    }

    /**
     * The default constructor for validation of a {@code DSSDocument} containing a COSE signature structure
     *
     * @param document
     *            {@link DSSDocument} containing COSE signature(s)
     */
    public COSEDocumentAnalyzer(final DSSDocument document) {
        super();
        Objects.requireNonNull(document, "Document to be validated cannot be null!");

        this.document = document;
        this.coseSignStructure = buildCoseSignStructure(document);
    }

    /**
     * The constructor for {@code COSEDocumentAnalyzer} to validate a provided {@code COSESignStructure}
     *
     * @param coseSignStructure
     *            {@link COSESignStructure} containing COSE signature(s)
     */
    public COSEDocumentAnalyzer(final COSESignStructure coseSignStructure) {
        this.coseSignStructure = coseSignStructure;
    }

    private COSESignStructure buildCoseSignStructure(final DSSDocument document) {
        COSEParser coseParser = new COSEParser(document);
        return coseParser.parse();
    }

    @Override
    public boolean isSupported(DSSDocument dssDocument) {
        COSEParser coseParser = new COSEParser(document);
        return coseParser.isSupported();
    }

    @Override
    public List<DSSDocument> getOriginalDocuments(AdvancedSignature advancedSignature) {
        return Collections.emptyList();
    }

}
