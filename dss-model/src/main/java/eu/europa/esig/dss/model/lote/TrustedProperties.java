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

    /** The LoLoTE id */
    private final LoLoTEInfo loloteInfo;

    /** The LoTE id */
    private final LoTEInfo listInfo;

    /** The trustedEntity */
    private final TrustedEntity trustedEntity;

    /** The trustedServices */
    private final TimeDependentValues<ServiceStatusAndInformationExtensions> trustedServices;

    /**
     * Constructor for extracted information from an "independent" list
     *
     * @param listInfo             {@link LoTEInfo}
     * @param trustedEntity        {@link TrustedEntity}
     * @param trustedServices      the current trust service
     */
    public TrustedProperties(LoTEInfo listInfo, TrustedEntity trustedEntity,
                             TimeDependentValues<ServiceStatusAndInformationExtensions> trustedServices) {
        this(null, listInfo, trustedEntity, trustedServices);
    }

    /**
     * Constructor for extracted information with a related List of Lists
     *
     * @param loloteInfo           {@link LoLoTEInfo}
     * @param listInfo             {@link LoTEInfo}
     * @param trustedEntity        {@link TrustedEntity}
     * @param trustedServices      the current trust service
     */
    public TrustedProperties(LoLoTEInfo loloteInfo, LoTEInfo listInfo, TrustedEntity trustedEntity,
                             TimeDependentValues<ServiceStatusAndInformationExtensions> trustedServices) {
        Objects.requireNonNull(listInfo, "tlInfo cannot be null!");
        Objects.requireNonNull(trustedEntity, "trustedEntity cannot be null!");
        Objects.requireNonNull(trustedServices, "trustedServices cannot be null!");
        this.loloteInfo = loloteInfo;
        this.listInfo = listInfo;
        this.trustedEntity = trustedEntity;
        this.trustedServices = trustedServices;
    }

    /**
     * Gets LoLoTE
     *
     * @return {@link LoLoTEInfo}
     */
    public LoLoTEInfo getLoLoTEInfo() {
        return loloteInfo;
    }

    /**
     * Gets List
     *
     * @return {@link LoTEInfo}
     */
    public LoTEInfo getLoTEInfo() {
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
