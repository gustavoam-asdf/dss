package eu.europa.esig.dss.lote.json.runnable;

import eu.europa.esig.dss.jades.JWSCompactSerializationParser;
import eu.europa.esig.dss.lote.json.download.JsonLoTEDownloadTask;
import eu.europa.esig.dss.lote.json.parsing.JsonLoLoTEParsingTask;
import eu.europa.esig.dss.lote.json.validation.JsonLoTEValidationTask;
import eu.europa.esig.dss.lote.runnable.LoLoTEAnalysisExecutor;
import eu.europa.esig.dss.lote.source.LoLoTESource;
import eu.europa.esig.dss.model.DSSDocument;
import eu.europa.esig.dss.spi.x509.CertificateSource;
import eu.europa.esig.dss.validation.job.download.DownloadTask;
import eu.europa.esig.dss.validation.job.parsing.ParsingTask;
import eu.europa.esig.dss.validation.job.validation.ValidationTask;

/**
 * Performs analysis for a JWS signed List of TS 119 602 Lists of Trusted Entities
 *
 */
public class JsonLoLoTEAnalysisExecutor implements LoLoTEAnalysisExecutor {

    /**
     * Default constructor
     */
    public JsonLoLoTEAnalysisExecutor() {
        // empty
    }

    @Override
    public boolean isSupported(DSSDocument document) {
        if (document == null) {
            return true; // accept
        }
        return new JWSCompactSerializationParser(document).isSupported();
    }

    @Override
    public DownloadTask getDownloadTask(DSSDocument document, String url) {
        return new JsonLoTEDownloadTask(document, url);
    }

    @Override
    public ParsingTask getParsingTask(DSSDocument document, LoLoTESource source) {
        return new JsonLoLoTEParsingTask(document, source);
    }

    @Override
    public ValidationTask getValidationTask(DSSDocument document, CertificateSource signingCertificateSource) {
        return new JsonLoTEValidationTask(document, signingCertificateSource);
    }

}
