package eu.europa.esig.dss.jaxb.parsers;

import eu.europa.esig.dss.enumerations.CertificateUsage;
import eu.europa.esig.dss.enumerations.LoTEServiceTypeIdentifier;

public class CertificateUsageParser {

    /**
     * Default constructor
     */
    private CertificateUsageParser() {
        // empty
    }

    /**
     * Parses the value and returns {@code CertificateUsage}
     *
     * @param v {@link String} to parse
     * @return {@link CertificateUsage}
     */
    public static CertificateUsage parse(String v) {
        return CertificateUsage.fromLabel(v);
    }

    /**
     * Gets a text name of the value
     *
     * @param v {@link LoTEServiceTypeIdentifier}
     * @return {@link String}
     */
    public static String print(CertificateUsage v) {
        return v.getLabel();
    }

}
