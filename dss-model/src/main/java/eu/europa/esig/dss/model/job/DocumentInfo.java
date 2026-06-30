package eu.europa.esig.dss.model.job;

import eu.europa.esig.dss.model.identifier.Identifier;
import eu.europa.esig.dss.model.identifier.IdentifierBasedObject;

public interface DocumentInfo<P extends DocumentInfo> extends IdentifierBasedObject {

    /**
     * Returns Download Cache Info
     *
     * @return {@link DownloadInfoRecord}
     */
    DownloadInfoRecord getDownloadCacheInfo();

    /**
     * Returns Parsing Cache Info
     *
     * @return {@link ParsingInfoRecord}
     */
    ParsingInfoRecord getParsingCacheInfo();

    /**
     * Returns Validation Cache Info
     *
     * @return {@link ValidationInfoRecord}
     */
    ValidationInfoRecord getValidationCacheInfo();

    /**
     * Returns a URL that was used to download the remote file
     *
     * @return {@link String} url
     */
    String getUrl();

    /**
     * Returns the {@code DocumentInfo} referencing the current Trusted List
     *
     * @return {@link DocumentInfo}
     */
    P getParent();

    /**
     * Returns the TL id
     *
     * @return {@link String} id
     */
    Identifier getDSSId();

    /**
     * Returns the String representation of the identifier
     *
     * @return {@link String}
     */
    String getDSSIdAsString();


}
