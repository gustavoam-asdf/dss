package eu.europa.esig.dss.enumerations;

import java.util.Objects;

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

        for (ListType type : TSLTypeEnum.values()) {
            if (type.getUri().equals(uri)) {
                return type;
            }
        }
        for (ListType type : LoTETypeEnum.values()) {
            if (type.getUri().equals(uri)) {
                return type;
            }
        }

        return new ListType() {

            private static final long serialVersionUID = -2240326302871808895L;

            @Override
            public String getUri() { return uri; }

            @Override
            public String getLabel() { return null; }

        };
    }

}

