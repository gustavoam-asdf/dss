package eu.europa.esig.dss.model.lote;

import eu.europa.esig.dss.model.identifier.Identifier;
import eu.europa.esig.dss.model.lote.identifier.LoLoTEIdentifier;
import eu.europa.esig.dss.model.lote.record.DownloadInfoRecord;
import eu.europa.esig.dss.model.lote.record.ParsingInfoRecord;
import eu.europa.esig.dss.model.lote.record.ValidationInfoRecord;

import java.util.List;

/**
 * Contains information about the List of TS 119 602 Lists of Trusted Entities
 *
 */
public class LoLoTEInfo extends LoTEInfo implements ListOfListsInfo<LoTEInfo> {

    /**
     * List of summary for TLs found inside the current LOTL
     */
    private List<LoTEInfo> loteInfos;

    /**
     * The default constructor
     *
     * @param downloadCacheInfo {@link DownloadInfoRecord} a download cache result
     * @param parsingCacheInfo {@link ParsingInfoRecord} a parsing cache result
     * @param validationCacheInfo {@link ValidationInfoRecord} a validation cache result
     * @param url {@link String} address used to extract the entry
     */
    public LoLoTEInfo(final DownloadInfoRecord downloadCacheInfo, final ParsingInfoRecord parsingCacheInfo,
                      final ValidationInfoRecord validationCacheInfo, final String url) {
        super(downloadCacheInfo, parsingCacheInfo, validationCacheInfo, url);
    }

    @Override
    public List<LoTEInfo> getListsInfos() {
        return loteInfos;
    }

    /**
     * Sets a list of {@code LoTEInfo}s summary for LoTE found in the LoLoTE
     *
     * @param loteInfos list of {@link LoTEInfo}s
     */
    public void setLoteInfos(List<LoTEInfo> loteInfos) {
        this.loteInfos = loteInfos;
    }

    @Override
    protected Identifier buildIdentifier() {
        return new LoLoTEIdentifier(this);
    }

}
