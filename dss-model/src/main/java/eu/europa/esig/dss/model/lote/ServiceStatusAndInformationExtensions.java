package eu.europa.esig.dss.model.lote;

import eu.europa.esig.dss.model.timedependent.TimeDependent;

import java.util.List;
import java.util.Map;

/**
 * Contains information about service status and extensions
 *
 */
public interface ServiceStatusAndInformationExtensions extends TimeDependent {

    /**
     * Gets a map of names
     *
     * @return a map of names
     */
    Map<String, List<String>> getNames();

    /**
     * Gets type
     *
     * @return {@link String}
     */
    String getType();

    /**
     * Gets status
     *
     * @return {@link String}
     */
    String getStatus();

    /**
     * Gets service supply points
     *
     * @return a list of {@link String}s
     */
    List<String> getServiceSupplyPoints();

}
