package eu.europa.esig.dss.model.job;

import java.util.List;

/**
 * Contains a summary of the validation job, with validation information for every document or/and a document list
 *
 * @param <D> {@link DocumentInfo}
 * @param <L> {@link DocumentListInfo}
 */
public interface ValidationJobSummary<D extends DocumentInfo<L>, L extends DocumentListInfo<L, D>> {

    /**
     * Returns a list of document list infos
     *
     * @return list of {@link DocumentListInfo}s
     */
    List<L> getDocumentListInfos();

    /**
     * Returns a list of other document infos
     *
     * @return list of {@link DocumentInfo}s
     */
    List<D> getOtherDocumentInfos();

}
