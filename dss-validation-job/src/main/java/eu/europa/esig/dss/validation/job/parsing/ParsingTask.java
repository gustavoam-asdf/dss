package eu.europa.esig.dss.validation.job.parsing;

import java.util.function.Supplier;

/**
 * Performs a parsing job
 *
 */
public interface ParsingTask extends Supplier<ParsingResult> {
}
