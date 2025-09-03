package eu.europa.esig.dss.validation.process.bbb.aov;

import eu.europa.esig.dss.detailedreport.jaxb.XmlAOV;
import eu.europa.esig.dss.diagnostic.TokenProxy;
import eu.europa.esig.dss.enumerations.Context;
import eu.europa.esig.dss.i18n.I18nProvider;
import eu.europa.esig.dss.model.policy.ValidationPolicy;
import eu.europa.esig.dss.validation.process.ChainItem;

import java.util.Date;

/**
 * Performs validation of the signature token (or timestamp), including validation of the signature value,
 * signed attributes and certificate chain
 *
 * @param <T> {@link TokenProxy}
 */
public class SignatureAlgorithmObsolescenceValidation<T extends TokenProxy> extends SignatureValueAndSignedAttributesAlgorithmObsolescenceValidation<T> {

    /**
     * Common constructor
     *
     * @param i18nProvider     the access to translations
     * @param token            instance of {@link TokenProxy} to be processed
     * @param context          {@link Context} validation context
     * @param validationDate   {@link Date} validation time
     * @param validationPolicy {@link ValidationPolicy} to be used during the validation
     */
    public SignatureAlgorithmObsolescenceValidation(I18nProvider i18nProvider, T token, Context context,
                                                    Date validationDate, ValidationPolicy validationPolicy) {
        super(i18nProvider, token, context, validationDate, validationPolicy);
    }

    @Override
    protected ChainItem<XmlAOV> buildChain() {

        ChainItem<XmlAOV> item = super.buildChain();

        item = buildDigestMatchersValidationChain(item, token.getDigestMatchers(), token.getId());

        item = buildCertificateChainValidationChain(item, token.getSigningCertificate(), token.getCertificateChain());

        return item;

    }

}
