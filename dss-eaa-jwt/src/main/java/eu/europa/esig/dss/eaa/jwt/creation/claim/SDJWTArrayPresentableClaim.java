package eu.europa.esig.dss.eaa.jwt.creation.claim;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jose4j.json.JsonUtil;

import eu.europa.esig.dss.jades.DSSJsonUtils;

/**
 * Represents an array claim. The array claim itself can be disclosable and/or individual elements within the array can be independently disclosable.
 * <p>
 * Example:
 * <pre>
 *   SDJWTArrayPresentableClaim nationalities = new SDJWTArrayPresentableClaim("nationalities");
 *
 *   SDJWTPresentableClaim en = new SDJWTPresentableClaim("EN");
 *   nationalities.addElement(en);
 *
 *   SDJWTPresentableClaim fr = new SDJWTPresentableClaim("FR", true, "123456");
 *   nationalities.addElement(fr);
 * </pre>
 */
public class SDJWTArrayPresentableClaim extends SDJWTPresentableClaim {

    private static final long serialVersionUID = 1L;

    private final List<SDJWTPresentableClaim> elements = new ArrayList<>();

    /**
     * Default constructor
     */
    public SDJWTArrayPresentableClaim() {
        this(null, false, null);
    }

    /**
     * Constructor
     *
     * @param name {@link String} the claim name
     */
    public SDJWTArrayPresentableClaim(final String name) {
        this(name, false, null);
    }

    /**
     * Constructor
     *
     * @param name  {@link String} the claim name
     * @param selectivelyDisclosable whether the claim is selectively disclosable
     * @param salt {@link String} the salt (mandatory if the claim is selectively disclosable)
     */
    public SDJWTArrayPresentableClaim(final String name, final boolean selectivelyDisclosable, final String salt) {
        super(name, selectivelyDisclosable, salt);
    }

    /**
     * Adds an element to the array
     *
     * @param element {@link SDJWTPresentableClaim}
     */
    public void addElement(final SDJWTPresentableClaim element) {
        elements.add(element);
    }

    /**
     * Gets the array elements
     *
     * @return unmodifiable list of {@link SDJWTPresentableClaim}
     */
    public List<SDJWTPresentableClaim> getElements() {
        return Collections.unmodifiableList(elements);
    }

    @Override
    public String getValueAsString() {
        List<String> values = new ArrayList<>();
        for (SDJWTPresentableClaim element : elements) {
            values.add(element.getValueAsString());
        }

        final Map<String, Object> result = new HashMap<>();
        result.put(getName(), values);
        return JsonUtil.toJson(result);
    }
}

