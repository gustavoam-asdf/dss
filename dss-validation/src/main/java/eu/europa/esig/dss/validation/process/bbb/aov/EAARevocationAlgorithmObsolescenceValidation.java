package eu.europa.esig.dss.validation.process.bbb.aov;

import eu.europa.esig.dss.detailedreport.jaxb.XmlAOV;
import eu.europa.esig.dss.diagnostic.AbstractTokenProxy;
import eu.europa.esig.dss.diagnostic.EAARevocationTokenWrapper;
import eu.europa.esig.dss.enumerations.Context;
import eu.europa.esig.dss.i18n.I18nProvider;
import eu.europa.esig.dss.model.policy.ValidationPolicy;
import eu.europa.esig.dss.validation.process.ChainItem;

import java.util.Date;

/**
 * Performs cryptographic validation of the EAA revocation token and its certificate chain
 *
 */
public class EAARevocationAlgorithmObsolescenceValidation extends TokenAlgorithmObsolescenceValidation<EAARevocationTokenWrapper> {

    /**
     * Common constructor
     *
     * @param i18nProvider     the access to translations
     * @param token            instance of {@link AbstractTokenProxy} to be processed
     * @param validationDate   {@link Date} validation time
     * @param validationPolicy {@link ValidationPolicy} to be used during the validation
     */
    public EAARevocationAlgorithmObsolescenceValidation(I18nProvider i18nProvider, EAARevocationTokenWrapper token,
                                                        Date validationDate, ValidationPolicy validationPolicy) {
        super(i18nProvider, token, Context.EAA_REVOCATION, validationDate, validationPolicy);
    }

    @Override
    protected ChainItem<XmlAOV> buildChain() {

        ChainItem<XmlAOV> item = super.buildChain();

        item = buildCertificateChainValidationChain(item, token.getSigningCertificate(), token.getCertificateChain());

        return item;

    }

}
