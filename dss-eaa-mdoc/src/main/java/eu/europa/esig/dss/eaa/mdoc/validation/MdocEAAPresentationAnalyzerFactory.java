package eu.europa.esig.dss.eaa.mdoc.validation;

import eu.europa.esig.dss.model.DSSDocument;
import eu.europa.esig.dss.spi.validation.analyzer.eaa.EAAPresentationAnalyzerFactory;

/**
 * This class is used to parse and process Electronic Attestation of Attributes (EAAs) embedded
 * within an mdoc document structure as defined in ISO 18013-5.
 *
 */
public class MdocEAAPresentationAnalyzerFactory implements EAAPresentationAnalyzerFactory {

    /**
     * Default constructor
     */
    public MdocEAAPresentationAnalyzerFactory() {
        // empty
    }

    @Override
    public boolean isSupported(DSSDocument document) {
        MdocEAAPresentationAnalyzer analyzer = new MdocEAAPresentationAnalyzer();
        return analyzer.isSupported(document);
    }

    @Override
    public MdocEAAPresentationAnalyzer create(DSSDocument document) {
        return new MdocEAAPresentationAnalyzer(document);
    }

}
