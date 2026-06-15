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

    /** The TL id */
    private final ListInfo listInfo;

    /** The trustedEntity */
    private final TrustedEntity trustedEntity;

    /** The trustedServices */
    private final TimeDependentValues<ServiceStatusAndInformationExtensions> trustedServices;

    /**
     * Constructor for extracted information from an "independent" trusted list
     *
     * @param listInfo               {@link ListInfo}
     * @param trustedEntity        {@link TrustedEntity}
     * @param trustedServices         the current trust service
     */
    public TrustedProperties(ListInfo listInfo, TrustedEntity trustedEntity,
                             TimeDependentValues<ServiceStatusAndInformationExtensions> trustedServices) {
        Objects.requireNonNull(listInfo, "tlInfo cannot be null!");
        Objects.requireNonNull(trustedEntity, "trustedEntity cannot be null!");
        Objects.requireNonNull(trustedServices, "trustedServices cannot be null!");
        this.listInfo = listInfo;
        this.trustedEntity = trustedEntity;
        this.trustedServices = trustedServices;
    }

    /**
     * Gets List
     *
     * @return {@link ListInfo}
     */
    public ListInfo getListInfo() {
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
    public TimeDependentValues<ServiceStatusAndInformationExtensions> getTrustedServices() {
        return trustedServices;
    }

}
