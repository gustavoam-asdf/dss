package eu.europa.esig.dss.eaa.jwt.creation;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import eu.europa.esig.dss.eaa.common.creation.claim.AbstractEAAClaim;
import eu.europa.esig.dss.eaa.common.creation.claim.EAAClaimArray;
import eu.europa.esig.dss.eaa.common.creation.claim.EAAClaimObject;

public class SDJWTEAAClaimObject extends SDJWTEAAClaim implements EAAClaimObject<SDJWTEAAClaim> {

    private final List<String> decoyDigests = new ArrayList<>();

    /**
     * Default constructor
     */
    public SDJWTEAAClaimObject() {
        this(null, false, null);
    }

    /**
     * Constructor with the claim name
     *
     * @param name {@link String} the claim name
     */
    public SDJWTEAAClaimObject(final String name) {
        this(name, false, null);
    }

    /**
     * Constructor with the claim name, selectively disclosable status and salt provided
     *
     * @param name  {@link String} the claim name
     * @param selectivelyDisclosable whether the claim is selectively disclosable
     * @param salt {@link String} the salt (mandatory if the claim is selectively disclosable)
     */
    public SDJWTEAAClaimObject(final String name, final boolean selectivelyDisclosable, final String salt) {
        super(name, new ArrayList<SDJWTEAAClaim>(), selectivelyDisclosable, salt);
    }

    @Override
    public void addChild(final SDJWTEAAClaim child) {
        getChildren().add(child);
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
