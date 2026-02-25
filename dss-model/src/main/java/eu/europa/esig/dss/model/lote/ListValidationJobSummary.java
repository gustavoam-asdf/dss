package eu.europa.esig.dss.model.lote;

import eu.europa.esig.dss.model.identifier.Identifier;

import java.io.Serializable;
import java.util.List;

/**
 * Contains results of the Lists validation job
 *
 */
public interface ListValidationJobSummary extends Serializable {

    /**
     * Returns a list of ListInfo's
     *
     * @return list of {@link ListInfo}s
     */
    List<ListInfo> getListInfos();

    /**
     * Returns an amount of processed Lists during the List Validation job
     *
     * @return {@code int} number of processed Lists
     */
    int getNumberOfProcessedLists();

    /**
     * Returns a ListInfo object by Identifier
     *
     * @param identifier
     *            the Identifier of the searched ListInfo
     * @return a ListInfo or null
     */
    ListInfo getListInfoById(Identifier identifier);

}
