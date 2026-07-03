package eu.europa.esig.dss.validation.process.qualification.trust.filter;

import java.util.Collection;
import java.util.Collections;
import java.util.Date;

/**
 * Creates filters for trusted entity services filtering
 *
 */
public final class TrustedEntitiesFilterFactory {

    /**
     * Default constructor
     */
    private TrustedEntitiesFilterFactory() {
        // empty
    }

    /**
     * Creates a TrustedEntityService filter by list Url
     *
     * @param url {@link String} URL to filter trusted services by
     * @return {@link TrustedEntityServiceFilter}
     */
    public static TrustedEntityServiceFilter createFilterByListUrl(String url) {
        return new ServiceByTrustedEntityServiceUrlFilter(Collections.singleton(url));
    }

    /**
     * Creates a TrustedEntityService filter by list urls
     *
     * @param urls a collection of {@link String}s to filter trusted services by
     * @return {@link TrustedEntityServiceFilter}
     */
    public static TrustedEntityServiceFilter createFilterByListUrls(Collection<String> urls) {
        return new ServiceByTrustedEntityServiceUrlFilter(urls);
    }

    /**
     * Creates a TrustedEntityService filter by date
     *
     * @param date {@link Date} to filter trusted services by
     * @return {@link TrustedEntityServiceFilter}
     */
    public static TrustedEntityServiceFilter createFilterByDate(Date date) {
        return new TrustedEntityServiceByDateFilter(date);
    }

    /**
     * Creates a TrustedEntityService filter by STI URI
     *
     * @param stiUri {@link String} to filter trusted services by STI URI
     * @return {@link TrustedEntityServiceFilter}
     */
    public static TrustedEntityServiceFilter createFilterByServiceTypeIdentifierUri(String stiUri) {
        return new TrustedEntityServiceByStiFilter(stiUri);
    }

    /**
     * Creates a TrustedEntityService filter by service status URI
     *
     * @param statusUri {@link String} to filter trusted services by service status URI
     * @return {@link TrustedEntityServiceFilter}
     */
    public static TrustedEntityServiceFilter createFilterByServiceStatusUri(String statusUri) {
        return new TrustedEntityServiceByStatusFilter(statusUri);
    }

}
