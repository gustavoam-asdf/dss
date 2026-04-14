package eu.europa.esig.dss.enumerations;

import java.util.HashMap;
import java.util.Map;

/**
 * Defines possible supported qualification types for an Electronic Attestation of Attributes
 *
 */
public enum EAAQualification {

    /**
     * Qualified electronic attestation of attributes as defined in Regulation EU 2024/1183, Article 45d.
     */
    QEAA("QEAA", "Qualified Electronic Attestation of Attributes"),

    /**
     * Electronic attestation of attributes as defined in Regulation EU 2024/1183, without a qualified status.
     */
    EAA("EAA", "Electronic Attestation of Attributes"),

    /**
     * Electronic attestation of attributes issued by or on behalf of a public sector body responsible
     * for an authentic source as defined in Regulation EU 2024/1183, Article 45f.
     */
    PUBEAA("Pub-EAA", "Electronic Attestation of Attributes issued by or on behalf of a public sector body"),

    /**
     * Personal Identification Data (PID)
     */
    PID("PID", "Personal Identification Data"),

    /**
     * Electronic attestation of attributes of unknown or conflicting status.
     */
    UNKNOWN("Unknown", "Electronic Attestation of Attributes of unknown type"),

    /**
     * Indeterminate qualified electronic attestation of attributes as defined in Regulation EU 2024/1183, Article 45d.
     */
    INDETERMINATE_QEAA("Indeterminate QEAA", "Indeterminate Qualified Electronic Attestation of Attributes"),

    /**
     * Indeterminate electronic attestation of attributes as defined in Regulation EU 2024/1183, without a qualified status.
     */
    INDETERMINATE_EAA("Indeterminate EAA", "Indeterminate Electronic Attestation of Attributes"),

    /**
     * Indeterminate electronic attestation of attributes issued by or on behalf of a public sector body responsible
     * for an authentic source as defined in Regulation EU 2024/1183, Article 45f.
     */
    INDETERMINATE_PUBEAA("Indeterminate Pub-EAA", "Indeterminate Electronic Attestation of Attributes issued by or on behalf of a public sector body"),

    /**
     * Indeterminate Personal Identification Data (PID)
     */
    INDETERMINATE_PID("Indeterminate PID", "Indeterminate Personal Identification Data"),

    /**
     * Indeterminate electronic attestation of attributes of unknown or conflicting status.
     */
    INDETERMINATE_UNKNOWN("Indeterminate Unknown", "Indeterminate Electronic Attestation of Attributes of unknown type"),

    /**
     * Not electronic attestation of attributes
     */
    NOT_EAA("Not EAA", "Not Electronic Attestation of Attributes"),

    /**
     * Not Applicable
     */
    NA("N/A", "Not applicable");

    /**
     * This class is used to provide a quick mapping of the user-friendly labels to enums
     */
    private static class Registry {

        private static final Map<String, EAAQualification> QUALIFS_BY_READABLE = registerByReadable();

        private static Map<String, EAAQualification> registerByReadable() {
            final Map<String, EAAQualification> map = new HashMap<>();
            for (final EAAQualification qualification : values()) {
                map.put(qualification.readable, qualification);
            }
            return map;
        }
    }

    /** User-friendly name (abbreviation) of the qualification */
    private final String readable;

    /** Description of the enumeration */
    private final String label;

    /**
     * Default constructor
     *
     * @param readable {@link String}
     * @param label {@link String}
     */
    EAAQualification(String readable, String label) {
        this.readable = readable;
        this.label = label;
    }

    /**
     * Gets user-friendly name of the enumeration
     *
     * @return {@link String}
     */
    public String getReadable() {
        return readable;
    }

    /**
     * Gets description of the enumeration
     *
     * @return {@link String}
     */
    public String getLabel() {
        return label;
    }

    /**
     * Gets EAAQualification from an enumeration name
     * Note: EAAQualification can be null
     *
     * @param value
     *            the qualification name to be converted to the enum
     * @return the linked EAAQualification or null
     */
    public static EAAQualification forName(String value) {
        if ((value != null) && !value.isEmpty()) {
            return EAAQualification.valueOf(value);
        }
        return null;
    }

    /**
     * Gets EAAQualification from a readable user-friendly label
     * Note: EAAQualification can be null
     *
     * @param readable
     *            the readable description of the qualification to be converted to the enum
     * @return the linked EAAQualification or null
     */
    public static EAAQualification fromReadable(String readable) {
        if ((readable != null) && !readable.isEmpty()) {
            return EAAQualification.Registry.QUALIFS_BY_READABLE.get(readable);
        }
        return null;
    }
    
}
