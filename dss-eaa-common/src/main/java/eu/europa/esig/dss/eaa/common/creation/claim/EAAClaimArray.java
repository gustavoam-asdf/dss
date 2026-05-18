package eu.europa.esig.dss.eaa.common.creation.claim;

import java.util.ArrayList;
import java.util.List;

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
public class EAAClaimArray extends EAAClaim {

    private static final long serialVersionUID = 1L;

    /**
     * Default constructor
     */
    public EAAClaimArray() {
        this(null, false, null);
    }

    /**
     * Constructor
     *
     * @param name {@link String} the claim name
     */
    public EAAClaimArray(final String name) {
        this(name, false, null);
    }

    /**
     * Constructor
     *
     * @param name  {@link String} the claim name
     * @param selectivelyDisclosable whether the claim is selectively disclosable
     * @param salt {@link String} the salt (mandatory if the claim is selectively disclosable)
     */
    public EAAClaimArray(final String name, final boolean selectivelyDisclosable, final String salt) {
        super(name, new ArrayList<EAAClaim>(), selectivelyDisclosable, salt);
    }

    /**
     * Adds an element to the array
     *
     * @param element {@link EAAClaim}
     */
    public void addElement(final EAAClaim element) {
        getElements().add(element);
    }

    public List<EAAClaim> getElements() {
        return (List<EAAClaim>) getValue();
    }
}

