package eu.europa.esig.dss.eaa.mdoc.validation;

import eu.europa.esig.dss.eaa.common.validation.DefaultEAAPresentationAnalyzer;
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
        MdocDeviceResponseEAAPresentationAnalyzer mdocDeviceResponseAnalyzer = new MdocDeviceResponseEAAPresentationAnalyzer();
        if (mdocDeviceResponseAnalyzer.isSupported(document)) {
            return true;
        }

        MdocIssuerSignedEAAPresentationAnalyzer mdocIssuerSignedAnalyzer = new MdocIssuerSignedEAAPresentationAnalyzer();
        if (mdocIssuerSignedAnalyzer.isSupported(document)) {
            return true;
        }

        return false;
    }

    @Override
    public DefaultEAAPresentationAnalyzer create(DSSDocument document) {
        MdocDeviceResponseEAAPresentationAnalyzer mdocDeviceResponseAnalyzer = new MdocDeviceResponseEAAPresentationAnalyzer();
        if (mdocDeviceResponseAnalyzer.isSupported(document)) {
            return new MdocDeviceResponseEAAPresentationAnalyzer(document);
        }

        MdocIssuerSignedEAAPresentationAnalyzer mdocIssuerSignedAnalyzer = new MdocIssuerSignedEAAPresentationAnalyzer();
        if (mdocIssuerSignedAnalyzer.isSupported(document)) {
            return new MdocIssuerSignedEAAPresentationAnalyzer(document);
        }

        throw new IllegalArgumentException("Not supported document");
    }

}
