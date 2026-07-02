package eu.europa.esig.dss.model.lote;

import eu.europa.esig.dss.model.identifier.Identifier;
import eu.europa.esig.dss.model.job.DocumentListInfo;
import eu.europa.esig.dss.model.job.DownloadInfoRecord;
import eu.europa.esig.dss.model.job.ValidationInfoRecord;
import eu.europa.esig.dss.model.lote.identifier.LoLoTEIdentifier;
import eu.europa.esig.dss.model.lote.record.LoTEParsingInfoRecord;

import java.util.List;

/**
 * Contains information about the List of TS 119 602 Lists of Trusted Entities
 *
 */
public class LoLoTEInfo extends LoTEInfo implements DocumentListInfo<LoLoTEInfo, LoTEInfo> {

    private static final long serialVersionUID = -1581874554370816691L;

    /** List of summary for Lists found inside the current LoTE */
    private List<LoTEInfo> childrenInfos;

    /**
     * The default constructor
     * @param downloadCacheInfo {@link DownloadInfoRecord} a download cache result
     * @param parsingCacheInfo {@link LoTEParsingInfoRecord} a parsing cache result
     * @param validationCacheInfo {@link ValidationInfoRecord} a validation cache result
     * @param url {@link String} address used to extract the entry
     */
    public LoLoTEInfo(final DownloadInfoRecord downloadCacheInfo, final LoTEParsingInfoRecord parsingCacheInfo,
                    final ValidationInfoRecord validationCacheInfo, final String url) {
        super(downloadCacheInfo, parsingCacheInfo, validationCacheInfo, url);
    }

    /**
     * Gets a list of processing information for other referenced LoTEs
     *
     * @return a list of {@link LoTEInfo}s
     */
    @Override
    public List<LoTEInfo> getChildrenInfos() {
        return childrenInfos;
    }

    /**
     * Sets a list of {@code ListInfo}s summary for LoTE found in the LoLoTE
     *
     * @param childrenInfos list of {@link LoTEInfo}s
     */
    public void setChildrenInfos(List<LoTEInfo> childrenInfos) {
        this.childrenInfos = childrenInfos;
    }

    @Override
    protected Identifier buildIdentifier() {
        return new LoLoTEIdentifier(this);
    }

}
