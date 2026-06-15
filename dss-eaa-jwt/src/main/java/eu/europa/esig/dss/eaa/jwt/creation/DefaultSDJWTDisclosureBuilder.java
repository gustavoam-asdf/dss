package eu.europa.esig.dss.eaa.jwt.creation;

import eu.europa.esig.dss.jades.DSSJsonUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Default implementation of an {@code SDJWTDisclosureBuilder}.
 * Creates a base64url disclosure String for a given claim, e.g. for a disclosure:
 * {@code ["_26bc4LT-ac6q2KI6cBW5es","family_name","Möbius"]}
 * The returned base64url encoded String is:
 * {@code WyJfMjZiYzRMVC1hYzZxMktJNmNCVzVlcyIsICJmYW1pbHlfbmFtZSIsICJNw7ZiaXVzIl0}
 *
 */
public class DefaultSDJWTDisclosureBuilder implements SDJWTDisclosureBuilder {

    /**
     * Default constructor
     *
     */
    public DefaultSDJWTDisclosureBuilder() {
        // empty
    }

    @Override
    public SDJWTEAADisclosure build(String name, Object value, String salt) {
        Objects.requireNonNull(value, "Value cannot be null!");
        Objects.requireNonNull(salt, "Salt cannot be null!");
        List<Object> data = new ArrayList<>();
        data.add(salt);
        if (name != null) {
            data.add(name);
        }
        data.add(value);
        return new SDJWTEAADisclosure(DSSJsonUtils.toBase64Url(data));
    }

}
