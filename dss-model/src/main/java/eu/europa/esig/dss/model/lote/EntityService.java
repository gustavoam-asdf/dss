package eu.europa.esig.dss.model.lote;

import eu.europa.esig.dss.model.timedependent.TimeDependentValues;
import eu.europa.esig.dss.model.x509.CertificateToken;

import java.io.Serializable;
import java.util.List;

/**
 * Contains information about an entity service
 *
 * @param <S> implementation of {@link ServiceStatusAndInformationExtensions}
 */
public interface EntityService<S extends ServiceStatusAndInformationExtensions> extends Serializable {

    /**
     * Gets a list of certificates
     *
     * @return a list of {@link CertificateToken}s
     */
    List<CertificateToken> getCertificates();

    /**
     * Gets status based on time
     *
     * @return {@link TimeDependentValues}
     */
    TimeDependentValues<S> getStatusAndInformationExtensions();

}
