package eu.europa.esig.dss.model.lote;

import eu.europa.esig.dss.model.identifier.Identifier;

import java.util.List;

/**
 * Contains summary of the validation result of a List of Trusted Entities validation job
 *
 */
public class LoTEValidationJobSummary implements ListValidationJobSummary<LoLoTEInfo, LoTEInfo> {

    /**
     * A list of LoLoTEInfos with a relationship between their LoTEs
     */
    private final List<LoLoTEInfo> listOfListsInfos;

    /**
     * List of TL infos for otherTLSources
     */
    private final List<LoTEInfo> otherListsInfos;

    /**
     * The default constructor
     *
     * @param listOfListsInfos a list of {@link LoLoTEInfo}s
     * @param otherListsInfos a list of {@link LoTEInfo}s
     */
    public LoTEValidationJobSummary(final List<LoLoTEInfo> listOfListsInfos, final List<LoTEInfo> otherListsInfos) {
        if ((listOfListsInfos == null || listOfListsInfos.isEmpty()) && (otherListsInfos == null || otherListsInfos.isEmpty())) {
            throw new IllegalArgumentException("LoLoTEInfo or LoTE Info shall be provided!");
        }
        this.listOfListsInfos = listOfListsInfos;
        this.otherListsInfos = otherListsInfos;
    }

    @Override
    public List<LoLoTEInfo> getListOfListsInfos() {
        return listOfListsInfos;
    }

    @Override
    public List<LoTEInfo> getOtherListInfos() {
        return otherListsInfos;
    }

    @Override
    public int getNumberOfProcessedLists() {
        int amount = 0;
        if (otherListsInfos != null && !otherListsInfos.isEmpty()) {
            amount += otherListsInfos.size();
        }
        if (listOfListsInfos != null && !listOfListsInfos.isEmpty()) {
            for (LoLoTEInfo loloteInfo : listOfListsInfos) {
                amount += loloteInfo.getListsInfos().size();
            }
        }
        return amount;
    }

    @Override
    public int getNumberOfProcessedListsOfLists() {
        return listOfListsInfos == null || listOfListsInfos.isEmpty() ? 0 : listOfListsInfos.size();
    }

    @Override
    public LoTEInfo getListInfoById(Identifier identifier) {
        if (otherListsInfos != null && !otherListsInfos.isEmpty()) {
            for (LoTEInfo loteInfo : otherListsInfos) {
                if (identifier.equals(loteInfo.getDSSId())) {
                    return loteInfo;
                }
            }
        }

        if (listOfListsInfos != null && !listOfListsInfos.isEmpty()) {
            for (LoLoTEInfo loloteInfo : listOfListsInfos) {
                if (loloteInfo.getListsInfos() != null && !loloteInfo.getListsInfos().isEmpty()) {
                    for (LoTEInfo loteInfo : loloteInfo.getListsInfos()) {
                        if (identifier.equals(loteInfo.getDSSId())) {
                            return loteInfo;
                        }
                    }
                }
            }
        }

        return null;
    }

    @Override
    public LoLoTEInfo getListOfListsInfoById(Identifier identifier) {
        if (listOfListsInfos != null && !listOfListsInfos.isEmpty()) {
            for (LoLoTEInfo loloteInfo : listOfListsInfos) {
                if (identifier.equals(loloteInfo.getDSSId())) {
                    return loloteInfo;
                }
            }
        }
        return null;
    }
    
}
