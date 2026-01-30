package eu.europa.esig.dss.enumerations;

import eu.europa.esig.dss.enumerations.loader.LoTELoader;

import java.util.Objects;
import java.util.ServiceLoader;

public interface LoTEServiceTypeIdentifier extends UriBasedEnum {

    /**
     * Gets user-friendly identifier
     *
     * @return {@link String}
     */
    String getLabel();

    /**
     * This method returns a {@code LoTEServiceTypeIdentifier} for the given URI
     *
     * @param uri {@link String}
     * @return {@link LoTEServiceTypeIdentifier}
     */
    static LoTEServiceTypeIdentifier fromUri(String uri) {
        Objects.requireNonNull(uri, "URI cannot be null!");

        for (LoTELoader loader : loaders()) {
            LoTEServiceTypeIdentifier identifier = loader.serviceTypeIdentifierFromUri(uri);
            if (identifier != null) {
                return identifier;
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
