package eu.europa.esig.dss.lote.json.runnable;

import eu.europa.esig.dss.jades.JWSCompactSerializationParser;
import eu.europa.esig.dss.lote.json.download.JsonLoTEDownloadTask;
import eu.europa.esig.dss.lote.json.parsing.JsonLoTEParsingTask;
import eu.europa.esig.dss.lote.json.validation.JsonLoTEValidationTask;
import eu.europa.esig.dss.lote.runnable.LoTEAnalysisExecutor;
import eu.europa.esig.dss.lote.source.LoTESource;
import eu.europa.esig.dss.model.DSSDocument;
import eu.europa.esig.dss.spi.x509.CertificateSource;
import eu.europa.esig.dss.validation.job.download.DownloadTask;
import eu.europa.esig.dss.validation.job.parsing.ParsingTask;
import eu.europa.esig.dss.validation.job.validation.ValidationTask;

/**
 * This class is used to run the analysis for the TS 119 602 JSON List of Trusted Entities validation,
 * signed by a Compact Serialized JWS/JAdES
 *
 */
public class JsonLoTEAnalysisExecutor implements LoTEAnalysisExecutor {

    /**
     * Default constructor
     */
    public JsonLoTEAnalysisExecutor() {
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
    public ParsingTask getParsingTask(DSSDocument document, LoTESource source) {
        return new JsonLoTEParsingTask(document, source);
    }

    @Override
    public ValidationTask getValidationTask(DSSDocument document, CertificateSource signingCertificateSource) {
        return new JsonLoTEValidationTask(document, signingCertificateSource);
    }

}
