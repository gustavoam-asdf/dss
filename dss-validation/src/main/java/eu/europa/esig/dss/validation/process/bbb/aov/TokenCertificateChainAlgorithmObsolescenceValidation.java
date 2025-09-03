package eu.europa.esig.dss.validation.process.bbb.aov;

import eu.europa.esig.dss.detailedreport.jaxb.XmlAOV;
import eu.europa.esig.dss.diagnostic.AbstractTokenProxy;
import eu.europa.esig.dss.diagnostic.TokenProxy;
import eu.europa.esig.dss.enumerations.Context;
import eu.europa.esig.dss.i18n.I18nProvider;
import eu.europa.esig.dss.model.policy.ValidationPolicy;
import eu.europa.esig.dss.validation.process.ChainItem;

import java.util.Date;

/**
 * Performs cryptographic validation of the token's certificate chain only
 *
 * @param <T> {@link TokenProxy}
 */
public class TokenCertificateChainAlgorithmObsolescenceValidation<T extends TokenProxy> extends TokenAlgorithmObsolescenceValidation<T> {

    /**
     * Common constructor
     *
     * @param i18nProvider     the access to translations
     * @param token            instance of {@link AbstractTokenProxy} to be processed
     * @param context          {@link Context} validation context
     * @param validationDate   {@link Date} validation time
     * @param validationPolicy {@link ValidationPolicy} to be used during the validation
     */
    public TokenCertificateChainAlgorithmObsolescenceValidation(I18nProvider i18nProvider, T token,
            Context context, Date validationDate, ValidationPolicy validationPolicy) {
        super(i18nProvider, token, context, validationDate, validationPolicy);
    }

    @Override
    protected ChainItem<XmlAOV> buildChain() {

        return buildCertificateChainValidationChain(firstItem, token.getSigningCertificate(), token.getCertificateChain());

    }

}
