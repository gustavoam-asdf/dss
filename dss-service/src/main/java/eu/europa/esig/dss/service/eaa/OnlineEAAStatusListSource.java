package eu.europa.esig.dss.service.eaa;

import eu.europa.esig.dss.model.eaa.claim.ClaimStatus;
import eu.europa.esig.dss.service.http.commons.CommonsDataLoader;
import eu.europa.esig.dss.spi.client.http.DataLoader;
import eu.europa.esig.dss.spi.eaa.EAA;
import eu.europa.esig.dss.spi.eaa.EAAStatusToken;
import eu.europa.esig.dss.spi.eaa.statuslist.EAAStatusListSource;
import eu.europa.esig.dss.utils.Utils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Objects;

/**
 * Requests Token Status List (TSL) for an EAA status verification using the online data loader.
 *
 */
public class OnlineEAAStatusListSource implements EAAStatusListSource {

    private static final Logger LOG = LoggerFactory.getLogger(OnlineEAAStatusListSource.class);

    /**
     * The component that allows to retrieve the data.
     */
    private DataLoader dataLoader;

    /**
     * The default constructor. A {@code CommonsDataLoader is created}.
     */
    public OnlineEAAStatusListSource() {
        dataLoader = new CommonsDataLoader();
        LOG.trace("+OnlineEAAStatusListSource with the default data loader.");
    }

    /**
     * This constructor allows to set a specific {@code DataLoader}.
     *
     * @param dataLoader
     *            the component that allows to retrieve the data.
     */
    public OnlineEAAStatusListSource(final DataLoader dataLoader) {
        this.dataLoader = dataLoader;
        LOG.trace("+OnlineEAAStatusListSource with the specific data loader.");
    }

    /**
     * Set the DataLoader to use for querying a revocation server.
     *
     * @param dataLoader
     *            the component that allows to retrieve a EAA Token Status List (TSL) response using HTTP.
     */
    public void setDataLoader(final DataLoader dataLoader) {
        this.dataLoader = dataLoader;
    }

    @Override
    public EAAStatusToken getEAAStatus(EAA eaa) {
        Objects.requireNonNull(eaa, "EAA cannot be null!");
        Objects.requireNonNull(dataLoader, "DataLoader is not provided!");
        LOG.trace("--> OnlineEAAStatusListSource queried for {}", eaa.getId());

        final String statusListUrl = getStatusListUrl(eaa);
        if (Utils.isStringBlank(statusListUrl)) {
            LOG.debug("No status_list location found within EAA with Id '{}'", eaa.getId());
            return null;
        }
        byte[] statusListResponse = dataLoader.get(statusListUrl);
        if (Utils.isArrayEmpty(statusListResponse)) {
            LOG.warn("The server URL '{}' replied with en empty byte array!", statusListUrl);
        }


        return null;
    }

    private String getStatusListUrl(EAA eaa) {
        ClaimStatus status = eaa.getPayload().getStatus();
        if (status != null && status.getStatusList() != null && status.getStatusList().getUri() != null) {
            return status.getStatusList().getUri().getValueAsString();
        }
        return null;
    }

}
