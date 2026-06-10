package eu.europa.esig.dss.validation.process.bbb.aov;

import eu.europa.esig.dss.detailedreport.jaxb.XmlAOV;
import eu.europa.esig.dss.diagnostic.AbstractTokenProxy;
import eu.europa.esig.dss.diagnostic.EAAStatusTokenWrapper;
import eu.europa.esig.dss.enumerations.Context;
import eu.europa.esig.dss.i18n.I18nProvider;
import eu.europa.esig.dss.model.policy.ValidationPolicy;
import eu.europa.esig.dss.validation.process.ChainItem;

import java.util.Date;

/**
 * Performs cryptographic validation of the EAA status token and its certificate chain
 *
 */
public class EAAStatusAlgorithmObsolescenceValidation extends TokenAlgorithmObsolescenceValidation<EAAStatusTokenWrapper> {

    /**
     * Common constructor
     *
     * @param i18nProvider     the access to translations
     * @param token            instance of {@link AbstractTokenProxy} to be processed
     * @param validationDate   {@link Date} validation time
     * @param validationPolicy {@link ValidationPolicy} to be used during the validation
     */
    public EAAStatusAlgorithmObsolescenceValidation(I18nProvider i18nProvider, EAAStatusTokenWrapper token,
                                                         Date validationDate, ValidationPolicy validationPolicy) {
        super(i18nProvider, token, Context.EAA_STATUS, validationDate, validationPolicy);
    }

    @Override
    protected ChainItem<XmlAOV> buildChain() {

        ChainItem<XmlAOV> item = super.buildChain();

        // TODO : add processing of CertID algorithm

        item = buildCertificateChainValidationChain(item, token.getSigningCertificate(), token.getCertificateChain());

        return item;

    }

}
