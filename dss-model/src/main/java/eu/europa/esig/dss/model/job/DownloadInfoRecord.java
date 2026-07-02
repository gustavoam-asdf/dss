package eu.europa.esig.dss.model.job;

import eu.europa.esig.dss.model.DSSDocument;

import java.util.Date;

/**
 * Defines a download result record
 */
public interface DownloadInfoRecord extends InfoRecord {

    /**
     * Returns the downloaded document
     *
     * @return {@link DSSDocument}
     */
    DSSDocument getDocument();

    /**
     * The last time when a download attempt has been proceeded.
     *
     * @return {@link Date}
     */
    Date getLastDownloadAttemptTime();

}
