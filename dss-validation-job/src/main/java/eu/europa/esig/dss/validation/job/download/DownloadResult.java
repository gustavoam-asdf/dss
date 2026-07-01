package eu.europa.esig.dss.validation.job.download;

import eu.europa.esig.dss.model.DSSDocument;
import eu.europa.esig.dss.model.Digest;
import eu.europa.esig.dss.validation.job.cache.CachedResult;

import java.util.List;

/**
 * Interface providing methods to extract information about a download job
 *
 */
public interface DownloadResult extends CachedResult {

    /**
     * Gets the downloaded document
     *
     * @return {@link DSSDocument}
     */
    DSSDocument getDSSDocument();

    /**
     * Gets digest of a canonicalized document
     *
     * @return {@link Digest}
     */
    Digest getDigest();

    /**
     * Returns error messages occurred during sha2 processing, if applicable
     *
     * @return a list of {@link String}s if errors occurred during sha2 processing, empty list otherwise
     */
    List<String> getSha2ErrorMessages();

}
