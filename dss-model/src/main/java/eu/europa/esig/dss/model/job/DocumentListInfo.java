package eu.europa.esig.dss.model.job;

import java.util.List;

public interface DocumentListInfo<P extends DocumentInfo<P>, C extends DocumentInfo<P>> extends DocumentInfo<P> {

    /**
     * Returns a list of {@code DocumentInfo}s summary for documents referenced from the current document
     * @return list of {@link DocumentInfo}s
     */
    List<C> getChildrenInfos();

}
