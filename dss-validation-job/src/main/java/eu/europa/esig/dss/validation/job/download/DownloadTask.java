package eu.europa.esig.dss.validation.job.download;

import java.util.function.Supplier;

/**
 * Performs a download job
 *
 */
public interface DownloadTask extends Supplier<DownloadResult> {
}
