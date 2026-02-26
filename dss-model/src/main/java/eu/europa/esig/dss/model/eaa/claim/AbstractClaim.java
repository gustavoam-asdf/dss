package eu.europa.esig.dss.model.eaa.claim;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Abstract implementation of a disclosable claim, contains common information for the (selectively) disclosable claims
 *
 */
public abstract class AbstractClaim implements Claim {

    private static final long serialVersionUID = -6060146078508116153L;

    /** Name of the claim */
    private String name;

    /** Whether the claim is selectively disclosable */
    private boolean selectivelyDisclosable;

    /** Parent claim, containing the current claim in its body */
    private Claim parent;

    /**
     * Default constructor
     */
    protected AbstractClaim() {
        // empty
    }

    /**
     * Constructor with claim name provided
     *
     * @param name {@link String}
     */
    protected AbstractClaim(String name) {
        this.name = name;
    }

    /**
     * Constructor with claim name and selectively disclosable status provided
     *
     * @param name {@link String}
     * @param selectivelyDisclosable whether the claim is selectively disclosable
     *                               (can be TRUE only when the value of claim is provided in a form of disclosure)
     */
    protected AbstractClaim(String name, boolean selectivelyDisclosable) {
        this.name = name;
        this.selectivelyDisclosable = selectivelyDisclosable;
    }

    /**
     * Constructor with claim name and selectively disclosable status and parent claim provided
     *
     * @param name {@link String}
     * @param selectivelyDisclosable whether the claim is selectively disclosable
     *                               (can be TRUE only when the value of claim is provided in a form of disclosure)
     * @param parent {@link Claim} representing the parent claim, when applicable
     */
    protected AbstractClaim(String name, boolean selectivelyDisclosable, Claim parent) {
        this.name = name;
        this.selectivelyDisclosable = selectivelyDisclosable;
        this.parent = parent;
    }

    @Override
    public String getName() {
        return name;
    }

    /**
     * Sets the claim name
     *
     * @param name {@link String}
     */
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public boolean isSelectivelyDisclosable() {
        return selectivelyDisclosable;
    }

    @Override
    public Claim getParent() {
        return parent;
    }

    @Override
    public String getStringValue() {
        return null;
    }

    @Override
    public Number getNumberValue() {
        return null;
    }

    @Override
    public Map<String, Claim> getMapValue() {
        return null;
    }

    @Override
    public Date getDateValue() {
        return null;
    }

    @Override
    public Boolean getBooleanValue() {
        return null;
    }

    @Override
    public byte[] getBinariesValue() {
        return null;
    }

    @Override
    public List<Claim> getListValue() {
        return null;
    }

    @Override
    public boolean isStringValueType() {
        return false;
    }

    @Override
    public boolean isBinaryValueType() {
        return false;
    }

    @Override
    public boolean isBooleanValueType() {
        return false;
    }

    @Override
    public boolean isNumberValueType() {
        return false;
    }

    @Override
    public boolean isDateValueType() {
        return false;
    }

    @Override
    public boolean isArrayValueType() {
        return false;
    }

    @Override
    public boolean isMapValueType() {
        return false;
    }

    @Override
    public boolean isSubresourceIntegrityType() {
        return false;
    }

    @Override
    public boolean isNullValueType() {
        return false;
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + " {" +
                "'" + name + "'" + (selectivelyDisclosable ? " (disclosure)" : "") + ": " + getValueAsString() +
                '}';
    }

}
