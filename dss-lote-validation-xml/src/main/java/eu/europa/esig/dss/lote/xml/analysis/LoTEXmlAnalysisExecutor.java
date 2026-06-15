package eu.europa.esig.dss.lote.xml.analysis;

import eu.europa.esig.dss.lote.analysis.ListAnalysisExecutor;
import eu.europa.esig.dss.lote.download.DownloadResult;
import eu.europa.esig.dss.lote.parsing.ParsingResult;
import eu.europa.esig.dss.lote.source.ListSource;
import eu.europa.esig.dss.lote.validation.ValidationResult;
import eu.europa.esig.dss.lote.xml.download.LoTEXmlDownloadTask;
import eu.europa.esig.dss.lote.xml.parsing.LoTEXmlParsingTask;
import eu.europa.esig.dss.lote.xml.validation.LoTEXmlValidationTask;
import eu.europa.esig.dss.model.DSSDocument;
import eu.europa.esig.dss.spi.x509.CertificateSource;
import eu.europa.esig.dss.xml.utils.DomUtils;

import java.util.function.Supplier;

/**
 * This class is used to run the analysis for the TS 119 602 XML List of Trusted Entities validation
 *
 */
public class LoTEXmlAnalysisExecutor implements ListAnalysisExecutor {

    /**
     * Default constructor
     */
    public LoTEXmlAnalysisExecutor() {
        // empty
    }

    @Override
    public boolean isSupported(DSSDocument document) {
        if (document == null) {
            return true; // accept
        }
        return DomUtils.isDOM(document);
    }

    @Override
    public Supplier<DownloadResult> getDownloadTask(DSSDocument document, String url) {
        return new LoTEXmlDownloadTask(document, url);
    }

    @Override
    public Supplier<ParsingResult> getParsingTask(DSSDocument document, ListSource source) {
        return new LoTEXmlParsingTask(document, source);
    }

    @Override
    public Supplier<ValidationResult> getValidationTask(DSSDocument document, CertificateSource signingCertificateSource) {
        return new LoTEXmlValidationTask(document, signingCertificateSource);
    }

}
