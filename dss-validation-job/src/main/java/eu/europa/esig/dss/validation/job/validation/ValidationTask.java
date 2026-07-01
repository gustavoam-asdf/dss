package eu.europa.esig.dss.validation.job.validation;

import java.util.function.Supplier;

/**
 * Performs a signature validation job
 *
 */
public interface ValidationTask extends Supplier<ValidationResult> {
}
