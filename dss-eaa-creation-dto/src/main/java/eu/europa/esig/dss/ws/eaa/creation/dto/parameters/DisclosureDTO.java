package eu.europa.esig.dss.ws.eaa.creation.dto.parameters;

import java.io.Serializable;
import java.util.Objects;

/**
 * DTO representing a selective disclosure
 *
 */
public class DisclosureDTO implements Serializable {

    private static final long serialVersionUID = -4519653477231360037L;

    /** (Mdoc) namespace of the disclosure */
    private String namespace;

    /** (Mdoc) digestId within the namespace of the disclosure */
    private Integer digestId;

    /** Value of the disclosure */
    private String value;

    /**
     * Empty constructor
     */
    public DisclosureDTO() {
        super();
    }

    /**
     * Constructor with a value (SD-JWT)
     *
     * @param value {@link String}
     */
    public DisclosureDTO(String value) {
        this.value = value;
    }

    /**
     * Constructor with a value, namespace and digestId (mdoc)
     *
     * @param namespace {@link String}
     * @param digestId {@link Integer}
     * @param value {@link String}
     */
    public DisclosureDTO(String namespace, Integer digestId, String value) {
        this.namespace = namespace;
        this.digestId = digestId;
        this.value = value;
    }

    /**
     * Gets the namespace of the selectively disclosable data item (mdoc)
     *
     * @return {@link String}
     */
    public String getNamespace() {
        return namespace;
    }

    /**
     * Sets the namespace of the selectively disclosable data item (mdoc)
     *
     * @param namespace {@link String}
     */
    public void setNamespace(String namespace) {
        this.namespace = namespace;
    }

    /**
     * Gets the digestId of the selectively disclosable data item (mdoc)
     *
     * @return {@link Integer}
     */
    public Integer getDigestId() {
        return digestId;
    }

    /**
     * Sets the digestId of the selectively disclosable data item (mdoc)
     *
     * @param digestId {@link Integer}
     */
    public void setDigestId(Integer digestId) {
        this.digestId = digestId;
    }

    /**
     * Gets the value of the selective disclosure
     *
     * @return {@link String}
     */
    public String getValue() {
        return value;
    }

    /**
     * Sets the value of the selective disclosure
     *
     * @param value {@link String}
     */
    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return "DisclosureDTO [" +
                "namespace='" + namespace + '\'' +
                ", digestId=" + digestId +
                ", value='" + value + '\'' +
                ']';
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;

        DisclosureDTO that = (DisclosureDTO) object;
        return Objects.equals(namespace, that.namespace)
                && Objects.equals(digestId, that.digestId)
                && Objects.equals(value, that.value);
    }

    @Override
    public int hashCode() {
        int result = Objects.hashCode(namespace);
        result = 31 * result + Objects.hashCode(digestId);
        result = 31 * result + Objects.hashCode(value);
        return result;
    }

}
