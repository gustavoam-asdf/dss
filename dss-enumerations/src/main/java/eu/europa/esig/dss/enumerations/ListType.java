package eu.europa.esig.dss.enumerations;

import eu.europa.esig.dss.enumerations.loader.LoTELoader;

import java.util.Objects;
import java.util.ServiceLoader;

/**
 * Defines a List type
 *
 */
public interface ListType extends UriBasedEnum {

    /**
     * Gets label
     *
     * @return {@link String}
     */
    String getLabel();

    /**
     * This method returns a {@code ListType} for the given URI
     *
     * @param uri {@link String}
     * @return {@link ListType}
     */
    static ListType fromUri(String uri) {
        Objects.requireNonNull(uri, "URI cannot be null!");

        for (LoTELoader loader : loaders()) {
            ListType listType = loader.listTypeFromUri(uri);
            if (listType != null) {
                return listType;
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

