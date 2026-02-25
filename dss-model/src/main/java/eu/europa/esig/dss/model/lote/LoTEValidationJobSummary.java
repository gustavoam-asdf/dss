package eu.europa.esig.dss.model.lote;

import eu.europa.esig.dss.model.identifier.Identifier;

import java.util.List;

/**
 * Contains summary of the validation result of a List of Trusted Entities validation job
 *
 */
public class LoTEValidationJobSummary implements ListValidationJobSummary {

    /**
     * List of infos for processed ListSource's
     */
    private final List<ListInfo> listsInfos;

    /**
     * The default constructor
     *
     * @param listsInfos a list of {@link ListInfo}s
     */
    public LoTEValidationJobSummary( final List<ListInfo> listsInfos) {
        if ((listsInfos == null || listsInfos.isEmpty())) {
            throw new IllegalArgumentException("List Info shall be provided!");
        }
        this.listsInfos = listsInfos;
    }

    @Override
    public List<ListInfo> getListInfos() {
        return listsInfos;
    }

    @Override
    public int getNumberOfProcessedLists() {
        int amount = 0;
        if (listsInfos != null && !listsInfos.isEmpty()) {
            amount += listsInfos.size();
        }
        return amount;
    }

    @Override
    public ListInfo getListInfoById(Identifier identifier) {
        if (listsInfos != null && !listsInfos.isEmpty()) {
            for (ListInfo listInfo : listsInfos) {
                if (identifier.equals(listInfo.getDSSId())) {
                    return listInfo;
                }
            }
        }
        return null;
    }
    
}
