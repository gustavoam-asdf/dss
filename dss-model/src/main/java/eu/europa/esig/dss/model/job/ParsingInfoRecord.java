package eu.europa.esig.dss.model.job;

import java.util.List;

/**
 * Defines a parsing result record
 *
 */
public interface ParsingInfoRecord extends InfoRecord {

    /**
     * Gets a list of error messages when occurred during the structure validation
     *
     * @return a list of {@link String} structure validation messages, empty list if the structure validation succeeded
     */
    List<String> getStructureValidationMessages();

}
