package eu.europa.esig.dss.model.lote;

import eu.europa.esig.dss.model.identifier.Identifier;
import eu.europa.esig.dss.model.identifier.IdentifierBasedObject;
import eu.europa.esig.dss.model.lote.record.DownloadInfoRecord;
import eu.europa.esig.dss.model.lote.record.ParsingInfoRecord;
import eu.europa.esig.dss.model.lote.record.ValidationInfoRecord;

import java.util.List;

/**
 * Represents information extracted on a List processing (e.g. Trusted List or List of Trusted Entities)
 *
 */
public interface ListInfo extends IdentifierBasedObject {

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
     * Returns the {@code ListInfo} referencing the current List
     *
     * @return {@link ListInfo}
     */
    ListInfo getParent();

    /**
     * Gets information about the referenced List
     *
     * @return {@link OtherListPointer}
     */
    OtherListPointer getListPointer();

    /**
     * Returns a list of {@code ListInfo}s summary for Lists found in the List of Lists
     *
     * @return list of {@link ListInfo}s
     */
    List<ListInfo> getOtherListsInfos();

    /**
     * Returns the unique identifier
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
