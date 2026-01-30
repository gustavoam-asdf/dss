package eu.europa.esig.dss.enumerations;

import eu.europa.esig.dss.enumerations.loader.LoTELoader;

import java.util.Objects;
import java.util.ServiceLoader;

public interface LoTEServiceStatus extends UriBasedEnum {

    /**
     * Gets user-friendly label
     *
     * @return {@link String}
     */
    String getLabel();

    /**
     * This method returns a {@code LoTEServiceStatus} for the given URI
     *
     * @param uri {@link String}
     * @return {@link LoTEServiceStatus}
     */
    static LoTEServiceStatus fromUri(String uri) {
        Objects.requireNonNull(uri, "URI cannot be null!");

        for (LoTELoader loader : loaders()) {
            LoTEServiceStatus status = loader.serviceStatusFromUri(uri);
            if (status != null) {
                return status;
            }
        }
        return null;
    }

    /**
     * This method loads available {@code LoTELoader}s using a ServiceLoader
     *
     * @return iterable of {@link LoTELoader}
     */
    static Iterable<LoTELoader> loaders() {
        return ServiceLoader.load(LoTELoader.class);
    }

}
