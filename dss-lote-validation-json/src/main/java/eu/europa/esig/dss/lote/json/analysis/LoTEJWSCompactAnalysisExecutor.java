package eu.europa.esig.dss.lote.json.analysis;

import eu.europa.esig.dss.jades.JWSCompactSerializationParser;
import eu.europa.esig.dss.lote.analysis.ListAnalysisExecutor;
import eu.europa.esig.dss.lote.download.DownloadResult;
import eu.europa.esig.dss.lote.json.download.LoTEJWSCompactDownloadTask;
import eu.europa.esig.dss.lote.json.parsing.LoTEJWSCompactParsingTask;
import eu.europa.esig.dss.lote.json.validation.LoTEJWSCompactValidationTask;
import eu.europa.esig.dss.lote.parsing.ParsingResult;
import eu.europa.esig.dss.lote.source.ListSource;
import eu.europa.esig.dss.lote.validation.ValidationResult;
import eu.europa.esig.dss.model.DSSDocument;
import eu.europa.esig.dss.spi.x509.CertificateSource;

import java.util.function.Supplier;

/**
 * This class is used to run the analysis for the TS 119 602 JSON List of Trusted Entities validation,
 * signed by a Compact Serialized JWS/JAdES
 *
 */
public class LoTEJWSCompactAnalysisExecutor implements ListAnalysisExecutor {

    /**
     * Default constructor
     */
    public LoTEJWSCompactAnalysisExecutor() {
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
    public Supplier<DownloadResult> getDownloadTask(DSSDocument document, String url) {
        return new LoTEJWSCompactDownloadTask(document, url);
    }

    @Override
    public Supplier<ParsingResult> getParsingTask(DSSDocument document, ListSource source) {
        return new LoTEJWSCompactParsingTask(document, source);
    }

    @Override
    public Supplier<ValidationResult> getValidationTask(DSSDocument document, CertificateSource signingCertificateSource) {
        return new LoTEJWSCompactValidationTask(document, signingCertificateSource);
    }

}
