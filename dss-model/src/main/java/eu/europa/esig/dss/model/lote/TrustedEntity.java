package eu.europa.esig.dss.model.lote;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * This class is a DTO containing information extracted for a trusted entity (TS 119 602)
 *
 */
public class TrustedEntity<S extends EntityService> implements Serializable {

    private static final long serialVersionUID = 8226814037598715986L;

    /*
     * Key = lang
     *
     * List = values / lang
     */

    /** The map of names */
    private Map<String, List<String>> names;

    /** The map of trade names */
    private Map<String, List<String>> tradeNames;

    /** The list of registration identifiers */
    private List<String> registrationIdentifiers;

    /** The map of postal addresses */
    private Map<String, String> postalAddresses;

    /** The map of electronic addresses */
    private Map<String, List<String>> electronicAddresses;

    /** The map of information */
    private Map<String, String> information;

    /** The list of trusted entity services */
    private List<S> services;

    /** The territory (country) */
    private String territory;

    /**
     * Default constructor
     *
     */
    public TrustedEntity() {
        // empty
    }

    /**
     * Gets a map of names
     *
     * @return a map of names
     */
    public Map<String, List<String>> getNames() {
        return names;
    }

    /**
     * Sets a map of names
     *
     * @param names a map of names
     */
    public void setNames(Map<String, List<String>> names) {
        this.names = names;
    }

    /**
     * Gets a map of trade names
     *
     * @return a map of trade names
     */
    public Map<String, List<String>> getTradeNames() {
        return tradeNames;
    }

    /**
     * Sets a map of trade names
     *
     * @param tradeNames a map of trade names
     */
    public void setTradeNames(Map<String, List<String>> tradeNames) {
        this.tradeNames = tradeNames;
    }

    /**
     * Gets a list of registration identifiers
     *
     * @return a list of {@link String}s
     */
    public List<String> getRegistrationIdentifiers() {
        return registrationIdentifiers;
    }

    /**
     * Sets a list of registration identifiers
     *
     * @param registrationIdentifiers a list of registration identifiers
     */
    public void setRegistrationIdentifiers(List<String> registrationIdentifiers) {
        this.registrationIdentifiers = registrationIdentifiers;
    }

    /**
     * Gets a map of postal addresses
     *
     * @return a map of postal addresses
     */
    public Map<String, String> getPostalAddresses() {
        return postalAddresses;
    }

    /**
     * Sets a map of postal addresses
     *
     * @param postalAddresses a map of postal addresses
     */
    public void setPostalAddresses(Map<String, String> postalAddresses) {
        this.postalAddresses = postalAddresses;
    }

    /**
     * Gets a map of electronic addresses
     *
     * @return a map of electronic addresses
     */
    public Map<String, List<String>> getElectronicAddresses() {
        return electronicAddresses;
    }

    /**
     * Sets a map of electronic addresses
     *
     * @param electronicAddresses a map of electronic addresses
     */
    public void setElectronicAddresses(Map<String, List<String>> electronicAddresses) {
        this.electronicAddresses = electronicAddresses;
    }

    /**
     * Gets a map of information
     *
     * @return a map of information
     */
    public Map<String, String> getInformation() {
        return information;
    }

    /**
     * Sets a map of information
     *
     * @param information a map of information
     */
    public void setInformation(Map<String, String> information) {
        this.information = information;
    }

    /**
     * Gets a list of trusted entity services
     *
     * @return a list of {@link S}s
     */
    public List<S> getServices() {
        return services;
    }

    /**
     * Sets a list of trusted entity services
     *
     * @param services a list of trusted entity services
     */
    public void setServices(List<S> services) {
        this.services = services;
    }

    /**
     * Gets territory (country)
     *
     * @return {@link String}
     */
    public String getTerritory() {
        return territory;
    }

    /**
     * Sets territory (country)
     *
     * @param territory {@link String}
     */
    public void setTerritory(String territory) {
        this.territory = territory;
    }

}
