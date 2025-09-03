package eu.europa.esig.dss.validation.process.vpfbs.checks;

import eu.europa.esig.dss.detailedreport.jaxb.XmlConstraintsConclusion;
import eu.europa.esig.dss.detailedreport.jaxb.XmlSAV;
import eu.europa.esig.dss.diagnostic.TokenProxy;
import eu.europa.esig.dss.enumerations.Indication;
import eu.europa.esig.dss.enumerations.SubIndication;
import eu.europa.esig.dss.i18n.I18nProvider;
import eu.europa.esig.dss.model.policy.LevelRule;

/**
 * Verifies if the format Signature Acceptance Validation process as per clause 5.2.8 succeeded,
 * but skips the cryptographic check failures, if any.
 *
 * @param <T> {@code XmlConstraintsConclusion}
 */
public class SignatureAcceptanceValidationNoCryptoResultCheck<T extends XmlConstraintsConclusion> extends SignatureAcceptanceValidationResultCheck<T> {

    /**
     * Default constructor
     *
     * @param i18nProvider {@link I18nProvider}
     * @param result {@link XmlConstraintsConclusion}
     * @param xmlSAV {@link XmlSAV}
     * @param token {@link TokenProxy}
     * @param constraint {@link LevelRule}
     */
    public SignatureAcceptanceValidationNoCryptoResultCheck(I18nProvider i18nProvider, T result,
                                                    XmlSAV xmlSAV, TokenProxy token, LevelRule constraint) {
        super(i18nProvider, result, xmlSAV, token, constraint);
    }

    @Override
    protected boolean process() {
        return xmlSAV != null && (isValid(xmlSAV) || isCryptoFailure(xmlSAV));
    }

    private boolean isCryptoFailure(XmlSAV xmlSAV) {
        return Indication.INDETERMINATE == xmlSAV.getConclusion().getIndication()
                && SubIndication.CRYPTO_CONSTRAINTS_FAILURE_NO_POE == xmlSAV.getConclusion().getSubIndication();
    }

}
