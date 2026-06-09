package eu.europa.esig.dss.service.eaa;

import eu.europa.esig.dss.enumerations.EAAStatusOrigin;
import eu.europa.esig.dss.service.http.commons.CommonsDataLoader;
import eu.europa.esig.dss.spi.client.http.DataLoader;
import eu.europa.esig.dss.spi.eaa.EAA;
import eu.europa.esig.dss.spi.eaa.EAAStatusToken;
import eu.europa.esig.dss.spi.eaa.EAAStatusValidator;
import eu.europa.esig.dss.spi.eaa.statuslist.EAAStatusSource;
import eu.europa.esig.dss.spi.exception.DSSExternalResourceException;
import eu.europa.esig.dss.utils.Utils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.ServiceLoader;

/**
 * Requests Token Status List (TSL) for an EAA status verification using the online data loader.
 *
 */
public class OnlineEAAStatusSource implements EAAStatusSource {

    private static final Logger LOG = LoggerFactory.getLogger(OnlineEAAStatusSource.class);

    /**
     * The component that allows to retrieve the data.
     */
    private DataLoader dataLoader;

    /**
     * The default constructor. A {@code CommonsDataLoader is created}.
     */
    public OnlineEAAStatusSource() {
        dataLoader = new CommonsDataLoader();
        LOG.trace("+OnlineEAAStatusListSource with the default data loader.");
    }

    /**
     * This constructor allows to set a specific {@code DataLoader}.
     *
     * @param dataLoader
     *            the component that allows to retrieve the data.
     */
    public OnlineEAAStatusSource(final DataLoader dataLoader) {
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

        ServiceLoader<EAAStatusValidator> loader = ServiceLoader.load(EAAStatusValidator.class);
        Iterator<EAAStatusValidator> validatorOptions = loader.iterator();

        if (validatorOptions.hasNext()) {
            for (EAAStatusValidator validator : loader) {
                if (validator.isSupported(eaa)) {
                    try {
                        EAAStatusToken eaaStatusToken = validate(eaa, validator);
                        if (eaaStatusToken != null) {
                            return eaaStatusToken;
                        }

                    } catch (Exception e) {
                        LOG.warn("An error occurred on EAA status validation using the {}", validator.getClass().getSimpleName());
                    }
                }
            }
        }

        return null;
    }

    /**
     * Performs validation of the {@code EAA} using the {@code validator}
     *
     * @param eaa {@link EAA} to validate
     * @param validator {@link EAAStatusValidator} to perform validation process
     * @return {@link EAAStatusToken}
     */
    protected EAAStatusToken validate(EAA eaa, EAAStatusValidator validator) {
        List<String> statusUris = validator.getStatusUris(eaa);
        if (Utils.isCollectionEmpty(statusUris)) {
            return null;
        }

        int nbTries = statusUris.size();
        for (String uriLocation : statusUris) {
            if (LOG.isDebugEnabled()) {
                LOG.debug("Trying to retrieve an EAA status from URL '{}'...", uriLocation);
            }
            nbTries--;

            try {
                byte[] bytes = dataLoader.get(uriLocation);
                if (Utils.isArrayEmpty(bytes)) {
                    LOG.warn("The server URL '{}' replied with en empty byte array!", bytes);
                    continue;
                }

                EAAStatusToken eaaStatusToken = validator.validate(eaa, bytes);
                if (eaaStatusToken == null) {
                    LOG.warn("The {} returned an empty token for the response retrieved from '{}' location",
                            validator.getClass().getSimpleName(), uriLocation);
                    continue;
                }
                eaaStatusToken.setOrigin(EAAStatusOrigin.EXTERNAL);
                eaaStatusToken.setSourceURL(uriLocation);
                eaaStatusToken.setRelatedEAA(eaa);
                return eaaStatusToken;

            } catch (Exception e) {
                if (nbTries == 0) {
                    throw new DSSExternalResourceException(String.format(
                            "Unable to retrieve EAA status response for EAA with Id '%s' from URL '%s'. Reason : %s",
                            eaa.getId(), uriLocation, e.getMessage()), e);
                } else {
                    LOG.warn("Unable to retrieve EAA status response with URL '{}' : {}", uriLocation, e.getMessage());
                }
            }
        }

        throw new IllegalStateException(String.format("Invalid state within OnlineEAAStatusSource " +
                "for a EAA status call with id '%s'", eaa.getId()));
    }

}
