package eu.europa.esig.dss.model.lote;

import eu.europa.esig.dss.model.identifier.Identifier;
import eu.europa.esig.dss.model.identifier.IdentifierBasedObject;
import eu.europa.esig.dss.model.lote.identifier.LoTEIdentifier;
import eu.europa.esig.dss.model.lote.record.DownloadInfoRecord;
import eu.europa.esig.dss.model.lote.record.ParsingInfoRecord;
import eu.europa.esig.dss.model.lote.record.ValidationInfoRecord;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;

/**
 * Contains information about the TS 119 602 List of Trusted Entities
 *
 */
public class LoTEInfo implements ListInfo, IdentifierBasedObject, Serializable {

    private static final long serialVersionUID = -1505115221927652721L;

    /** Address of the source */
    private final String url;

    /** The parent LoLoTE/LoTE referencing the current List of Trusted Entities */
    private final ListInfo parent;

    /** The download result record */
    private final DownloadInfoRecord downloadCacheInfo;

    /** The parsing result record */
    private final ParsingInfoRecord parsingCacheInfo;

    /** The validation result record */
    private final ValidationInfoRecord validationCacheInfo;

    /** OtherListPointer element extracted from the pointing LoLoTE/LoTE */
    private final OtherListPointer otherListPointer;

    /**
     * List of summary for Lists found inside the current LoTE
     */
    private List<ListInfo> otherLoTEInfos;

    /** Cached Identifier instance */
    private Identifier identifier;

    /**
     * The default constructor
     *
     * @param downloadCacheInfo {@link DownloadInfoRecord} a download cache result
     * @param parsingCacheInfo {@link ParsingInfoRecord} a parsing cache result
     * @param validationCacheInfo {@link ValidationInfoRecord} a validation cache result
     * @param url {@link String} address used to extract the entry
     */
    public LoTEInfo(final DownloadInfoRecord downloadCacheInfo, final ParsingInfoRecord parsingCacheInfo,
                  final ValidationInfoRecord validationCacheInfo, final String url) {
        this(downloadCacheInfo, parsingCacheInfo, validationCacheInfo, url, null);
    }

    /**
     * The default constructor with parent LoTEInfo
     *
     * @param downloadCacheInfo {@link DownloadInfoRecord} a download cache result
     * @param parsingCacheInfo {@link ParsingInfoRecord} a parsing cache result
     * @param validationCacheInfo {@link ValidationInfoRecord} a validation cache result
     * @param url {@link String} address used to extract the entry
     * @param parent {@link ListInfo} referencing the current Trusted List
     */
    public LoTEInfo(final DownloadInfoRecord downloadCacheInfo, final ParsingInfoRecord parsingCacheInfo,
                    final ValidationInfoRecord validationCacheInfo, final String url, final ListInfo parent) {
        this(downloadCacheInfo, parsingCacheInfo, validationCacheInfo, url, parent, null);
    }

    /**
     * The constructor with parent LOLoTEInfo and Mutual Recognition Agreement
     *
     * @param downloadCacheInfo {@link DownloadInfoRecord} a download cache result
     * @param parsingCacheInfo {@link ParsingInfoRecord} a parsing cache result
     * @param validationCacheInfo {@link ValidationInfoRecord} a validation cache result
     * @param url {@link String} address used to extract the entry
     * @param parent {@link ListInfo} referencing the current Trusted List
     * @param otherListPointer {@link OtherListPointer} element from the pointing TL/LOTL
     */
    public LoTEInfo(final DownloadInfoRecord downloadCacheInfo, final ParsingInfoRecord parsingCacheInfo,
                    final ValidationInfoRecord validationCacheInfo, final String url, final ListInfo parent,
                    final OtherListPointer otherListPointer) {
        Objects.requireNonNull(url, "URL String shall be provided!");

        this.downloadCacheInfo = downloadCacheInfo;
        this.parsingCacheInfo = parsingCacheInfo;
        this.validationCacheInfo = validationCacheInfo;
        this.url = url;
        this.parent = parent;
        this.otherListPointer = otherListPointer;
    }

    /**
     * Returns Download Cache Info
     *
     * @return {@link DownloadInfoRecord}
     */
    public DownloadInfoRecord getDownloadCacheInfo() {
        return downloadCacheInfo;
    }

    /**
     * Returns Parsing Cache Info
     *
     * @return {@link ParsingInfoRecord}
     */
    public ParsingInfoRecord getParsingCacheInfo() {
        return parsingCacheInfo;
    }

    /**
     * Returns Validation Cache Info
     *
     * @return {@link ValidationInfoRecord}
     */
    public ValidationInfoRecord getValidationCacheInfo() {
        return validationCacheInfo;
    }

    /**
     * Returns a URL that was used to download the remote file
     *
     * @return {@link String} url
     */
    public String getUrl() {
        return url;
    }

    /**
     * Returns the {@code ListInfo} referencing the current Trusted List
     *
     * @return {@link ListInfo}
     */
    public ListInfo getParent() {
        return parent;
    }

    @Override
    public OtherListPointer getListPointer() {
        return otherListPointer;
    }

    @Override
    public List<ListInfo> getOtherListsInfos() {
        return otherLoTEInfos;
    }

    /**
     * Sets a list of {@code ListInfo}s summary for LoTE found in the LoLoTE
     *
     * @param otherLoTEInfos list of {@link ListInfo}s
     */
    public void setOtherLoTEInfos(List<ListInfo> otherLoTEInfos) {
        this.otherLoTEInfos = otherLoTEInfos;
    }

    /**
     * Returns the TL id
     *
     * @return {@link String} id
     */
    public Identifier getDSSId() {
        if (identifier == null) {
            identifier = buildIdentifier();
        }
        return identifier;
    }

    /**
     * Builds the identifier
     *
     * @return {@link Identifier}
     */
    protected Identifier buildIdentifier() {
        return new LoTEIdentifier(this);
    }

    /**
     * Returns the String representation of the identifier
     *
     * @return {@link String}
     */
    public String getDSSIdAsString() {
        return getDSSId().asXmlId();
    }
    
}
