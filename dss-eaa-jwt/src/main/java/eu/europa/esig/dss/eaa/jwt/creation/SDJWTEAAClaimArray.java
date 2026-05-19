package eu.europa.esig.dss.eaa.jwt.creation;

import eu.europa.esig.dss.eaa.common.creation.claim.EAAClaimArray;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SDJWTEAAClaimArray extends SDJWTEAAClaim implements EAAClaimArray<SDJWTEAAClaim> {

    private static final long serialVersionUID = -8747676551662684772L;

    private final List<String> decoyDigests = new ArrayList<>();

    /**
     * Default constructor
     */
    public SDJWTEAAClaimArray() {
        this(null, false, null);
    }

    /**
     * Constructor with the claim name
     *
     * @param name {@link String} the claim name
     */
    public SDJWTEAAClaimArray(final String name) {
        this(name, false, null);
    }

    /**
     * Constructor with the claim name, selectively disclosable status and salt provided
     *
     * @param name  {@link String} the claim name
     * @param selectivelyDisclosable whether the claim is selectively disclosable
     * @param salt {@link String} the salt (mandatory if the claim is selectively disclosable)
     */
    public SDJWTEAAClaimArray(final String name, final boolean selectivelyDisclosable, final String salt) {
        super(name, new ArrayList<SDJWTEAAClaim>(), selectivelyDisclosable, salt);
    }

    @Override
    public void addElement(final SDJWTEAAClaim element) {
        getElements().add(element);
    }

    @Override
    public List<SDJWTEAAClaim> getElements() {
        return (List<SDJWTEAAClaim>) getValue();
    }

    public void addDecoyDigest(String digest) {
        decoyDigests.add(digest);
    }

    public List<String> getDecoyDigests() {
        return Collections.unmodifiableList(decoyDigests);
    }
}
