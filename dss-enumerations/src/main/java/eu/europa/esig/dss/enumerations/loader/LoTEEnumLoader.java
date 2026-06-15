package eu.europa.esig.dss.enumerations.loader;

import eu.europa.esig.dss.enumerations.CertificateUsage;
import eu.europa.esig.dss.enumerations.CertificateUsageEnum;
import eu.europa.esig.dss.enumerations.ListType;
import eu.europa.esig.dss.enumerations.LoTEServiceStatus;
import eu.europa.esig.dss.enumerations.LoTEServiceStatusEnum;
import eu.europa.esig.dss.enumerations.LoTEServiceTypeIdentifier;
import eu.europa.esig.dss.enumerations.LoTEServiceTypeIdentifierEnum;
import eu.europa.esig.dss.enumerations.LoTETypeEnum;

public class LoTEEnumLoader implements LoTELoader {

    /**
     * Default constructor
     */
    public LoTEEnumLoader() {
        // empty
    }

    @Override
    public ListType listTypeFromUri(String uri) {
        for (ListType type : LoTETypeEnum.values()) {
            if (uri.equalsIgnoreCase(type.getUri())) {
                return type;
            }
        }
        return null;
    }

    @Override
    public LoTEServiceTypeIdentifier serviceTypeIdentifierFromUri(String uri) {
        for (LoTEServiceTypeIdentifier sti : LoTEServiceTypeIdentifierEnum.values()) {
            if (uri.equalsIgnoreCase(sti.getUri())) {
                return sti;
            }
        }
        return null;
    }

    @Override
    public LoTEServiceStatus serviceStatusFromUri(String uri) {
        for (LoTEServiceStatus status : LoTEServiceStatusEnum.values()) {
            if (uri.equalsIgnoreCase(status.getUri())) {
                return status;
            }
        }
        return null;
    }

    @Override
    public CertificateUsage certificateUsageFromLabel(String label) {
        for (CertificateUsage certUsage : CertificateUsageEnum.values()) {
            if (label.equalsIgnoreCase(certUsage.getLabel())) {
                return certUsage;
            }
        }
        return null;
    }

    @Override
    public CertificateUsage certificateUsageFromDefinition(ListType listType, LoTEServiceTypeIdentifier sti, LoTEServiceStatus status) {
        for (CertificateUsage certUsage : CertificateUsageEnum.values()) {
            if (((listType == null && certUsage.getListType() == null) || (listType != null && listType.equals(certUsage.getListType())) &&
                    (sti == null && certUsage.getServiceTypeIdentifier() == null) || (sti != null && sti.equals(certUsage.getServiceTypeIdentifier())) &&
                    (status == null && certUsage.getServiceStatus() == null) || (status != null && status.equals(certUsage.getServiceStatus())))) {
                return certUsage;
            }
        }
        return CertificateUsageEnum.CERT_FOR_UNKNOWN;
    }

}
