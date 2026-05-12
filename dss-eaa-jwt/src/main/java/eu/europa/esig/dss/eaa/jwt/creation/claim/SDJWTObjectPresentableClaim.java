package eu.europa.esig.dss.eaa.jwt.creation.claim;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.jose4j.json.JsonUtil;

/**
 * Represents an object claim. The object itself can be disclosable and/or individual child claims within it can be independently disclosable.
 * <p>
 * Example:
 * <pre>
 *   SDJWTObjectPresentableClaim address = new SDJWTObjectPresentableClaim("address"); // Always visible, but the children are disclosable
 *
 *   SDJWTPresentableClaim street = new SDJWTPresentableClaim("street_address", "123 Main St", true, "1234");
 *   address.addChild(street);
 *
 *   SDJWTPresentableClaim city = new SDJWTPresentableClaim("locality", "Luxembourg", true, "4567");
 *   address.addChild(city);
 * </pre>
 */
public class SDJWTObjectPresentableClaim extends SDJWTPresentableClaim {

    private static final long serialVersionUID = 1L;

    /**
     * Default constructor
     */
    public SDJWTObjectPresentableClaim() {
        this(null);
    }

    /**
     * Constructor
     *
     * @param name {@link String} the claim name
     */
    public SDJWTObjectPresentableClaim(final String name) {
        this(name, false, null);
    }

    /**
     * Constructor
     *
     * @param name  {@link String} the claim name
     * @param selectivelyDisclosable whether the claim is selectively disclosable
     * @param salt {@link String} the salt (mandatory if the claim is selectively disclosable)
     */
    public SDJWTObjectPresentableClaim(final String name, final boolean selectivelyDisclosable, final String salt) {
        super(name, new ArrayList<SDJWTPresentableClaim>(), selectivelyDisclosable, salt);
    }

    /**
     * Adds a child claim
     *
     * @param child {@link SDJWTPresentableClaim}
     */
    public void addChild(final SDJWTPresentableClaim child) {
        getChildren().add(child);
    }

    public List<SDJWTPresentableClaim> getChildren() {
        return (List<SDJWTPresentableClaim>) getValue();
    }

}

