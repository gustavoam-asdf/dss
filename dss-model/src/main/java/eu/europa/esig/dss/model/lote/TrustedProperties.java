package eu.europa.esig.dss.model.lote;

import eu.europa.esig.dss.model.timedependent.TimeDependentValues;

import java.io.Serializable;
import java.util.Objects;

/**
 * Contains a list of trusted certificates and their properties
 *
 */
public class TrustedProperties implements Serializable {

    private static final long serialVersionUID = -3151960723009323199L;

    /** The LOTL id */
    private final ListOfListsInfo listOfListsInfo;

    /** The TL id */
    private final ListInfo listInfo;

    /** The trustServiceProvider */
    private final TrustedEntity trustedEntity;

    /** The trustServices */
    private final TimeDependentValues<ServiceStatusAndInformationExtensions> trustServices;

    /**
     * Constructor for extracted information from an "independent" trusted list
     *
     * @param listInfo               {@link ListInfo}
     * @param trustedEntity        {@link TrustedEntity}
     * @param trustedServices         the current trust service
     */
    public TrustedProperties(ListInfo listInfo, TrustedEntity trustedEntity,
                             TimeDependentValues<ServiceStatusAndInformationExtensions> trustedServices) {
        this(null, listInfo, trustedEntity, trustedServices);
    }

    /**
     * Constructor to create a TrustedProperties object linked to a LoLoTE
     *
     * @param listOfListsInfo             {@link ListOfListsInfo}
     * @param listInfo               {@link ListInfo}
     * @param trustedEntity        {@link TrustedEntity}
     * @param trustServices         the current trust service
     */
    public TrustedProperties(ListOfListsInfo listOfListsInfo, ListInfo listInfo, TrustedEntity trustedEntity,
                             TimeDependentValues<ServiceStatusAndInformationExtensions> trustServices) {
        Objects.requireNonNull(listInfo, "tlInfo cannot be null!");
        Objects.requireNonNull(trustedEntity, "trustedEntity cannot be null!");
        Objects.requireNonNull(trustServices, "trustService cannot be null!");
        this.listOfListsInfo = listOfListsInfo;
        this.listInfo = listInfo;
        this.trustedEntity = trustedEntity;
        this.trustServices = trustServices;
    }

    /**
     * Gets List Of Lists
     *
     * @return {@link ListOfListsInfo}
     */
    public ListOfListsInfo getListOfListsInfo() {
        return listOfListsInfo;
    }

    /**
     * Gets List
     *
     * @return {@link ListInfo}
     */
    public ListInfo getTLInfo() {
        return listInfo;
    }

    /**
     * Gets trusted entity
     *
     * @return {@link TrustedEntity}
     */
    public TrustedEntity getTrustedEntity() {
        return trustedEntity;
    }

    /**
     * Gets trust service
     *
     * @return {@link TimeDependentValues}
     */
    public TimeDependentValues<ServiceStatusAndInformationExtensions> getTrustServices() {
        return trustServices;
    }

}
