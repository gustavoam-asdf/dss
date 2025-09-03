package eu.europa.esig.dss.validation.process.bbb.aov;

import eu.europa.esig.dss.diagnostic.TimestampWrapper;
import eu.europa.esig.dss.enumerations.Context;
import eu.europa.esig.dss.i18n.I18nProvider;
import eu.europa.esig.dss.model.policy.ValidationPolicy;

import java.util.Date;

/**
 * Performs cryptographic validation of a timestamp token
 *
 */
public class TimestampAlgorithmObsolescenceValidation extends SignatureAlgorithmObsolescenceValidation<TimestampWrapper> {

    /**
     * Common constructor
     *
     * @param i18nProvider     the access to translations
     * @param token            instance of {@link TimestampWrapper} to be processed
     * @param validationDate   {@link Date} validation time
     * @param validationPolicy {@link ValidationPolicy} to be used during the validation
     */
    public TimestampAlgorithmObsolescenceValidation(I18nProvider i18nProvider, TimestampWrapper token,
                                                    Date validationDate, ValidationPolicy validationPolicy) {
        super(i18nProvider, token, Context.TIMESTAMP, validationDate, validationPolicy);
    }

}
