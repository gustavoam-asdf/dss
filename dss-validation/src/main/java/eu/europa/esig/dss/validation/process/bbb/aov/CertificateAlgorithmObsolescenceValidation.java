package eu.europa.esig.dss.validation.process.bbb.aov;

import eu.europa.esig.dss.diagnostic.AbstractTokenProxy;
import eu.europa.esig.dss.diagnostic.CertificateWrapper;
import eu.europa.esig.dss.enumerations.Context;
import eu.europa.esig.dss.enumerations.SubContext;
import eu.europa.esig.dss.i18n.I18nProvider;
import eu.europa.esig.dss.i18n.MessageTag;
import eu.europa.esig.dss.model.policy.CryptographicSuite;
import eu.europa.esig.dss.model.policy.ValidationPolicy;
import eu.europa.esig.dss.validation.process.ValidationProcessUtils;

import java.util.Date;

/**
 * Performs cryptographic validation of the certificate
 *
 */
public class CertificateAlgorithmObsolescenceValidation extends TokenAlgorithmObsolescenceValidation<CertificateWrapper> {

    /** SubContext of the validating certificate */
    private final SubContext subContext;

    /**
     * Common constructor
     *
     * @param i18nProvider     the access to translations
     * @param token            instance of {@link AbstractTokenProxy} to be processed
     * @param context          {@link Context} validation context
     * @param subContext       {@link SubContext} validation subContext
     * @param validationDate   {@link Date} validation time
     * @param validationPolicy {@link ValidationPolicy} to be used during the validation
     */
    public CertificateAlgorithmObsolescenceValidation(I18nProvider i18nProvider, CertificateWrapper token, Context context,
                                                      SubContext subContext, Date validationDate, ValidationPolicy validationPolicy) {
        super(i18nProvider, token, context, validationDate, validationPolicy);

        this.subContext = subContext;
    }

    @Override
    protected MessageTag getPosition() {
        return ValidationProcessUtils.getSubContextPosition(context, subContext);
    }

    @Override
    protected CryptographicSuite getCryptographicSuite() {
        return validationPolicy.getCertificateCryptographicConstraint(context, subContext);
    }

}
