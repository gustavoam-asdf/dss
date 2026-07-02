package eu.europa.esig.dss.lote.runnable;

import eu.europa.esig.dss.lote.source.LoTESource;
import eu.europa.esig.dss.model.DSSDocument;
import eu.europa.esig.dss.spi.x509.CertificateSource;
import eu.europa.esig.dss.validation.job.download.DownloadTask;
import eu.europa.esig.dss.validation.job.parsing.ParsingTask;
import eu.europa.esig.dss.validation.job.validation.ValidationTask;

/**
 * Common interface to perform LoTE analysis
 *
 * @param <S> {@link LoTESource}
 */
public interface ILoTEAnalysisExecutor<S extends LoTESource> {

    /**
     * Verifies if the List document is supported by the current executor
     *
     * @param document {@link DSSDocument} to be analyzed
     * @return TRUE if the document is supported, FALSE otherwise
     */
    boolean isSupported(DSSDocument document);

    /**
     * Gets the download task
     *
     * @param document {@link DSSDocument} to be processed
     * @param url {@link String}
     * @return {@link DownloadTask}
     */
    DownloadTask getDownloadTask(DSSDocument document, String url);

    /**
     * Gets the parsing task
     *
     * @param document {@link DSSDocument} to be processed
     * @param source {@link LoTESource}
     * @return {@link ParsingTask}
     */
    ParsingTask getParsingTask(DSSDocument document, S source);

    /**
     * Gets the validation task
     *
     * @param document {@link DSSDocument} to be processed
     * @param signingCertificateSource {@link CertificateSource} containing signing certificate candidates
     * @return {@link ValidationTask}
     */
    ValidationTask getValidationTask(DSSDocument document, CertificateSource signingCertificateSource);

}
