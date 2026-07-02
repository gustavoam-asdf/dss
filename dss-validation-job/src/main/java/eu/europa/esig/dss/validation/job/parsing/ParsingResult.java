package eu.europa.esig.dss.validation.job.parsing;

import eu.europa.esig.dss.validation.job.cache.CachedResult;

import java.util.List;

/**
 * Provides an interface to extract information about a parsing task result
 *
 */
public interface ParsingResult extends CachedResult {

    /**
     * Gets a list of error messages when occurred during the structure validation
     *
     * @return a list of {@link String} structure validation messages, empty list if the structure validation succeeded
     */
    List<String> getStructureValidationMessages();

}
