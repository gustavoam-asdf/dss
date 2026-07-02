package eu.europa.esig.dss.lote.xml.runnable;

import eu.europa.esig.dss.lote.runnable.LoTEAnalysisExecutor;
import eu.europa.esig.dss.lote.source.LoTESource;
import eu.europa.esig.dss.lote.xml.download.LoTEXmlDownloadTask;
import eu.europa.esig.dss.lote.xml.parsing.XmlLoTEParsingTask;
import eu.europa.esig.dss.lote.xml.validation.LoTEXmlValidationTask;
import eu.europa.esig.dss.model.DSSDocument;
import eu.europa.esig.dss.spi.x509.CertificateSource;
import eu.europa.esig.dss.validation.job.download.DownloadTask;
import eu.europa.esig.dss.validation.job.parsing.ParsingTask;
import eu.europa.esig.dss.validation.job.validation.ValidationTask;
import eu.europa.esig.dss.xml.utils.DomUtils;

/**
 * This class is used to run the analysis for the TS 119 602 XML List of Trusted Entities validation
 *
 */
public class XmlLoTEAnalysisExecutor implements LoTEAnalysisExecutor {

    /**
     * Default constructor
     */
    public XmlLoTEAnalysisExecutor() {
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
    public DownloadTask getDownloadTask(DSSDocument document, String url) {
        return new LoTEXmlDownloadTask(document, url);
    }

    @Override
    public ParsingTask getParsingTask(DSSDocument document, LoTESource source) {
        return new XmlLoTEParsingTask(document, source);
    }

    @Override
    public ValidationTask getValidationTask(DSSDocument document, CertificateSource signingCertificateSource) {
        return new LoTEXmlValidationTask(document, signingCertificateSource);
    }

}
