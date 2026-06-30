package eu.europa.esig.dss.validation.job.parsing;

import eu.europa.esig.dss.model.job.OtherDocumentPointer;
import eu.europa.esig.dss.validation.job.cache.CachedResult;

import java.util.List;

public interface ParsingResult extends CachedResult {

    /**
     * Gets a list of pointers to other documents referenced from the current document
     *
     * @return a list of {@link OtherDocumentPointer}s
     */
    List<? extends OtherDocumentPointer> getOtherDocumentPointers();

    /**
     * Gets a list of error messages when occurred during the structure validation
     *
     * @return a list of {@link String} structure validation messages, empty list if the structure validation succeeded
     */
    List<String> getStructureValidationMessages();

}
