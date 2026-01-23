package eu.europa.esig.dss.lote.analysis;

import eu.europa.esig.dss.lote.download.DownloadResult;
import eu.europa.esig.dss.lote.parsing.ParsingResult;
import eu.europa.esig.dss.lote.source.ListSource;
import eu.europa.esig.dss.lote.validation.ValidationResult;
import eu.europa.esig.dss.model.DSSDocument;
import eu.europa.esig.dss.spi.x509.CertificateSource;

import java.util.function.Supplier;

/**
 * This class is used to perform business logic for processing and analysing a List document
 *
 */
public interface ListAnalysisExecutor {

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
     * @return {@link Supplier}
     */
    Supplier<DownloadResult> getDownloadTask(DSSDocument document, String url);

    /**
     * Gets the parsing task
     *
     * @param document {@link DSSDocument} to be processed
     * @param source {@link ListSource}
     * @return {@link Supplier}
     */
    Supplier<ParsingResult> getParsingTask(DSSDocument document, ListSource source);

    /**
     * Gets the validation task
     *
     * @param document {@link DSSDocument} to be processed
     * @param signingCertificateSource {@link CertificateSource} containing signing certificate candidates
     * @return {@link Supplier}
     */
    Supplier<ValidationResult> getValidationTask(DSSDocument document, CertificateSource signingCertificateSource);

}
