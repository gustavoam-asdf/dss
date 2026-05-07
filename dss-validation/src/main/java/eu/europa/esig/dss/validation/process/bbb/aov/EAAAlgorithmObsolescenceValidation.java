package eu.europa.esig.dss.validation.process.bbb.aov;

import eu.europa.esig.dss.detailedreport.jaxb.XmlAOV;
import eu.europa.esig.dss.diagnostic.EAAWrapper;
import eu.europa.esig.dss.enumerations.Context;
import eu.europa.esig.dss.i18n.I18nProvider;
import eu.europa.esig.dss.model.policy.CryptographicSuite;
import eu.europa.esig.dss.model.policy.ValidationPolicy;
import eu.europa.esig.dss.validation.process.ChainItem;

import java.util.Date;

/**
 * Performs algorithm obsolescence validation for cryptographic algorithms used within an EAA
 *
 */
public class EAAAlgorithmObsolescenceValidation extends DigestAlgorithmObsolescenceValidation<EAAWrapper> {

    /**
     * Default constructor
     *
     * @param i18nProvider     the access to translations
     * @param token            instance of {@link EAAWrapper} to be processed
     * @param validationDate   {@link Date} validation time
     * @param validationPolicy {@link ValidationPolicy} to be used during the validation
     */
    public EAAAlgorithmObsolescenceValidation(I18nProvider i18nProvider, EAAWrapper token,
                                              Date validationDate, ValidationPolicy validationPolicy) {
        super(i18nProvider, token, Context.EAA_PRESENTATION, validationDate, validationPolicy);
    }

    @Override
    protected ChainItem<XmlAOV> buildChain() {
        return buildDigestMatchersValidationChain(firstItem, token.getDigestMatchers(), token.getId());
    }

    @Override
    protected CryptographicSuite getCryptographicSuite() {
        return validationPolicy.getEAACryptographicConstraint();
    }

}
