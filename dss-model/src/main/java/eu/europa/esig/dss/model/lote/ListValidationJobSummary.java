package eu.europa.esig.dss.model.lote;

import eu.europa.esig.dss.model.identifier.Identifier;
import eu.europa.esig.dss.model.tsl.TLInfo;

import java.io.Serializable;
import java.util.List;

/**
 * Contains results of the Lists validation job
 *
 */
public interface ListValidationJobSummary<L extends ListOfListsInfo<T>, T extends ListInfo> extends Serializable {

    /**
     * Returns an information list for all processed lists of lists
     *
     * @return list of {@link ListOfListsInfo}s
     */
    List<L> getListOfListsInfos();

    /**
     * Returns a list of TLInfos for other TLs
     *
     * @return list of {@link TLInfo}s
     */
    List<T> getOtherListInfos();

    /**
     * Returns an amount of processed Lists during the List Validation job
     *
     * @return {@code int} number of processed Lists
     */
    int getNumberOfProcessedLists();

    /**
     * Returns an amount of processed Lists of Lists during the List Validation job
     *
     * @return {@code int} number of processed Lists of Lists
     */
    int getNumberOfProcessedListsOfLists();

    /**
     * Returns a ListInfo object by Identifier
     *
     * @param identifier
     *            the Identifier of the searched ListInfo
     * @return a ListInfo or null
     */
    T getListInfoById(Identifier identifier);

    /**
     * Returns a ListOfListsInfo object by Identifier
     *
     * @param identifier
     *            the Identifier of the searched ListOfListsInfo
     * @return a ListOfListsInfo or null
     */
    L getListOfListsInfoById(Identifier identifier);

}
