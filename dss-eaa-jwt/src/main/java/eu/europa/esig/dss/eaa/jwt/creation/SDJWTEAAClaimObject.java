package eu.europa.esig.dss.eaa.jwt.creation;

import eu.europa.esig.dss.eaa.common.creation.claim.EAAClaimObject;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class SDJWTEAAClaimObject extends SDJWTEAAClaim implements EAAClaimObject<SDJWTEAAClaim> {

    private static final long serialVersionUID = 3602569321684484970L;

    private final List<String> decoyDigests = new ArrayList<>();

    /**
     * Create a {@link SDJWTEAAClaimObject}. The name of the claim will be null.
     *
     * @return the created {@link SDJWTEAAClaim}
     */
    public static SDJWTEAAClaimObject create() {
        return new SDJWTEAAClaimObject(null, false);
    }

    /**
     * Create a {@link SDJWTEAAClaim} with the provided name
     *
     * @param name {@link String} the name of the claim
     * @return the created {@link SDJWTEAAClaim}
     */
    public static SDJWTEAAClaimObject create(final String name) {
        return new SDJWTEAAClaimObject(name, false);
    }

    /**
     * Create a selectively disclosable {@link SDJWTEAAClaimObject}. The name of the claim will be null.
     *
     * @return the created {@link SDJWTEAAClaim}
     */
    public static SDJWTEAAClaimObject createSelectivelyDisclosable() {
        return new SDJWTEAAClaimObject(null, true);
    }

    /**
     * Create a selectively disclosable {@link SDJWTEAAClaimObject} with the provided salt. The name of the claim will be null.
     *
     * @param salt {@link String} the salt value
     * @return the created {@link SDJWTEAAClaim}
     */
    public static SDJWTEAAClaimObject createSelectivelyDisclosableWithSalt(final String salt) {
        return new SDJWTEAAClaimObject(null, true, salt);
    }

    /**
     * Create a selectively disclosable {@link SDJWTEAAClaim} with the provided name
     *
     * @param name {@link String} the name of the claim
     * @return the created {@link SDJWTEAAClaim}
     */
    public static SDJWTEAAClaimObject createSelectivelyDisclosable(final String name) {
        return new SDJWTEAAClaimObject(name, true);
    }

    /**
     * Create a selectively disclosable {@link SDJWTEAAClaim} with the provided name and salt
     *
     * @param name {@link String} the name of the claim
     * @param salt {@link String} the salt value
     * @return the created {@link SDJWTEAAClaim}
     */
    public static SDJWTEAAClaimObject createSelectivelyDisclosableWithSalt(final String name, final String salt) {
        return new SDJWTEAAClaimObject(name, true, salt);
    }

    /**
     * Constructor with the claim name and selectively disclosable status.
     * When the selectivelyDisclosable status is enabled but no salt is provided,
     * the salt will be generated during the EAA Payload computation.
     *
     * @param name  {@link String} the claim name
     * @param selectivelyDisclosable whether the claim is selectively disclosable
     */
    protected SDJWTEAAClaimObject(final String name, final boolean selectivelyDisclosable) {
        super(name, new ArrayList<SDJWTEAAClaim>(), selectivelyDisclosable, null);
    }

    /**
     * Constructor with the claim name, selectively disclosable status and salt provided
     *
     * @param name  {@link String} the claim name
     * @param selectivelyDisclosable whether the claim is selectively disclosable
     * @param salt {@link String} the salt (mandatory if the claim is selectively disclosable)
     */
    protected SDJWTEAAClaimObject(final String name, final boolean selectivelyDisclosable, final String salt) {
        super(name, new ArrayList<SDJWTEAAClaim>(), selectivelyDisclosable, salt);
    }

    @Override
    public void addChild(final SDJWTEAAClaim child) {
        getChildren().add(child);
    }

    public void addChildren(final Collection<SDJWTEAAClaim> children) {
        getChildren().addAll(children);
    }

    @Override
    public List<SDJWTEAAClaim> getChildren() {
        return (List<SDJWTEAAClaim>) getValue();
    }

    public void addDecoyDigest(String digest) {
        decoyDigests.add(digest);
    }

    public List<String> getDecoyDigests() {
        return Collections.unmodifiableList(decoyDigests);
    }

}
