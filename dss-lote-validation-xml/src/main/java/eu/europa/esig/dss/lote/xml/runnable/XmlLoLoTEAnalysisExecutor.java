package eu.europa.esig.dss.lote.xml.runnable;

import eu.europa.esig.dss.lote.runnable.LoLoTEAnalysisExecutor;
import eu.europa.esig.dss.lote.source.LoLoTESource;
import eu.europa.esig.dss.lote.xml.download.LoTEXmlDownloadTask;
import eu.europa.esig.dss.lote.xml.parsing.XmlLoLoTEParsingTask;
import eu.europa.esig.dss.lote.xml.validation.LoTEXmlValidationTask;
import eu.europa.esig.dss.model.DSSDocument;
import eu.europa.esig.dss.spi.x509.CertificateSource;
import eu.europa.esig.dss.validation.job.download.DownloadTask;
import eu.europa.esig.dss.validation.job.parsing.ParsingTask;
import eu.europa.esig.dss.validation.job.validation.ValidationTask;
import eu.europa.esig.dss.xml.utils.DomUtils;

/**
 * Performs analysis of an XML List of TS 119 602 List of Trusted Entities
 */
public class XmlLoLoTEAnalysisExecutor implements LoLoTEAnalysisExecutor {

    /**
     * Default constructor
     */
    public XmlLoLoTEAnalysisExecutor() {
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
    public ParsingTask getParsingTask(DSSDocument document, LoLoTESource source) {
        return new XmlLoLoTEParsingTask(document, source);
    }

    @Override
    public ValidationTask getValidationTask(DSSDocument document, CertificateSource signingCertificateSource) {
        return new LoTEXmlValidationTask(document, signingCertificateSource);
    }

}
