package eu.europa.esig.dss.validation.process.bbb.aov;

import eu.europa.esig.dss.detailedreport.jaxb.XmlAOV;
import eu.europa.esig.dss.diagnostic.AbstractTokenProxy;
import eu.europa.esig.dss.diagnostic.CertificateWrapper;
import eu.europa.esig.dss.enumerations.Context;
import eu.europa.esig.dss.i18n.I18nProvider;
import eu.europa.esig.dss.model.policy.ValidationPolicy;
import eu.europa.esig.dss.utils.Utils;
import eu.europa.esig.dss.validation.process.ChainItem;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Performs cryptographic validation of the certificate and its chain
 *
 */
public class CertificateAndChainAlgorithmObsolescenceValidation extends TokenAlgorithmObsolescenceValidation<CertificateWrapper> {

    /**
     * Common constructor
     *
     * @param i18nProvider     the access to translations
     * @param token            instance of {@link AbstractTokenProxy} to be processed
     * @param context          {@link Context} validation context
     * @param validationDate   {@link Date} validation time
     * @param validationPolicy {@link ValidationPolicy} to be used during the validation
     */
    public CertificateAndChainAlgorithmObsolescenceValidation(I18nProvider i18nProvider, CertificateWrapper token, Context context,
                                                              Date validationDate, ValidationPolicy validationPolicy) {
        super(i18nProvider, token, context, validationDate, validationPolicy);
    }

    @Override
    protected ChainItem<XmlAOV> buildChain() {

        return buildCertificateChainValidationChain(firstItem, token, getFullCertificateChain());

    }

    private List<CertificateWrapper> getFullCertificateChain() {
        List<CertificateWrapper> certificateChain = new ArrayList<>();
        if (token != null) {
            certificateChain.add(token);
            if (Utils.isCollectionNotEmpty(token.getCertificateChain())) {
                certificateChain.addAll(token.getCertificateChain());
            }
        }
        return certificateChain;
    }

}
