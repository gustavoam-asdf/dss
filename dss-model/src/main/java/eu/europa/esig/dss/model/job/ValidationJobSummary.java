package eu.europa.esig.dss.model.job;

import java.util.List;

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
