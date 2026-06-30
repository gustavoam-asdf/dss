package eu.europa.esig.dss.model.job;

import eu.europa.esig.dss.model.identifier.Identifier;

import java.io.Serializable;
import java.util.Objects;

public abstract class AbstractDocumentInfo<P extends DocumentInfo<P>> implements DocumentInfo<P>, Serializable {

    private static final long serialVersionUID = -5439324653080984894L;

    /** Address of the source */
    private final String url;

    /** The parent LOTL/TL referencing the current Trusted List */
    private final P parent;

    /** The download result record */
    private final DownloadInfoRecord downloadCacheInfo;

    /** The parsing result record */
    private final ParsingInfoRecord parsingCacheInfo;

    /** The validation result record */
    private final ValidationInfoRecord validationCacheInfo;

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
    public AbstractDocumentInfo(final DownloadInfoRecord downloadCacheInfo, final ParsingInfoRecord parsingCacheInfo,
                                final ValidationInfoRecord validationCacheInfo, final String url) {
        this(downloadCacheInfo, parsingCacheInfo, validationCacheInfo, url, null);
    }

    /**
     * The default constructor with parent TLInfo
     *
     * @param downloadCacheInfo {@link DownloadInfoRecord} a download cache result
     * @param parsingCacheInfo {@link ParsingInfoRecord} a parsing cache result
     * @param validationCacheInfo {@link ValidationInfoRecord} a validation cache result
     * @param url {@link String} address used to extract the entry
     * @param parent {@link DocumentInfo} referencing the current Trusted List
     */
    public AbstractDocumentInfo(final DownloadInfoRecord downloadCacheInfo, final ParsingInfoRecord parsingCacheInfo,
                                final ValidationInfoRecord validationCacheInfo, final String url, final P parent) {
        Objects.requireNonNull(url, "URL String shall be provided!");

        this.downloadCacheInfo = downloadCacheInfo;
        this.parsingCacheInfo = parsingCacheInfo;
        this.validationCacheInfo = validationCacheInfo;
        this.url = url;
        this.parent = parent;
    }

    @Override
    public DownloadInfoRecord getDownloadCacheInfo() {
        return downloadCacheInfo;
    }

    @Override
    public ParsingInfoRecord getParsingCacheInfo() {
        return parsingCacheInfo;
    }

    @Override
    public ValidationInfoRecord getValidationCacheInfo() {
        return validationCacheInfo;
    }

    @Override
    public String getUrl() {
        return url;
    }

    @Override
    public P getParent() {
        return parent;
    }

    @Override
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
    protected abstract Identifier buildIdentifier();

    @Override
    public String getDSSIdAsString() {
        return getDSSId().asXmlId();
    }

}
