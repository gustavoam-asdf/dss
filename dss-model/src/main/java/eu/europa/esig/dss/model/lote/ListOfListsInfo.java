package eu.europa.esig.dss.model.lote;

import java.util.List;

/**
 * Contains information about List of Lists (e.g. EU List of Trusted Lists, List of Lists of Trusted Entities, etc.)
 *
 * @param <L> implementation of {@link ListInfo}
 */
public interface ListOfListsInfo<L extends ListInfo> extends ListInfo {

    /**
     * Returns a list of {@code ListInfo}s summary for Lists found in the List of Lists
     *
     * @return list of {@link ListInfo}s
     */
    List<L> getListsInfos();

}
