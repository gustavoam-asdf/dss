package eu.europa.esig.dss.enumerations.loader;

import eu.europa.esig.dss.enumerations.CertificateUsage;
import eu.europa.esig.dss.enumerations.ListType;
import eu.europa.esig.dss.enumerations.LoTEServiceStatus;
import eu.europa.esig.dss.enumerations.LoTEServiceTypeIdentifier;

/**
 * This class is used to load TS 119 602 LoTE related properties
 *
 */
public interface LoTELoader {

    /**
     * Gets a {@code ListType} from the given URI
     *
     * @param uri {@link String}
     * @return {@link ListType}
     */
    ListType listTypeFromUri(String uri);

    /**
     * Gets a {@code LoTEServiceTypeIdentifier} from the given URI
     *
     * @param uri {@link String}
     * @return {@link LoTEServiceTypeIdentifier}
     */
    LoTEServiceTypeIdentifier serviceTypeIdentifierFromUri(String uri);

    /**
     * Gets a {@code LoTEServiceStatus} from the given URI
     *
     * @param uri {@link String}
     * @return {@link LoTEServiceStatus}
     */
    LoTEServiceStatus serviceStatusFromUri(String uri);

    /**
     * Gets a {@code CertificateUsage} from the given label String
     *
     * @param label {@link String}
     * @return {@link CertificateUsage}
     */
    CertificateUsage certificateUsageFromLabel(String label);

    /**
     * Gets a {@code CertificateUsage} from the given label String
     *
     * @param listType {@link ListType}
     * @param sti {@link LoTEServiceTypeIdentifier}
     * @param status {@link LoTEServiceStatus}
     * @return {@link CertificateUsage}
     */
    CertificateUsage certificateUsageFromDefinition(ListType listType, LoTEServiceTypeIdentifier sti, LoTEServiceStatus status);

}
