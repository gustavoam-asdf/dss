package eu.europa.esig.dss.validation.process.bbb.aov;

import eu.europa.esig.dss.detailedreport.jaxb.XmlAOV;
import eu.europa.esig.dss.diagnostic.EvidenceRecordWrapper;
import eu.europa.esig.dss.enumerations.Context;
import eu.europa.esig.dss.i18n.I18nProvider;
import eu.europa.esig.dss.model.policy.CryptographicSuite;
import eu.europa.esig.dss.model.policy.ValidationPolicy;
import eu.europa.esig.dss.validation.process.ChainItem;

import java.util.Date;

/**
 * Performs cryptographic validation of the algorithms used on evidence record creation
 *
 */
public class EvidenceRecordAlgorithmObsolescenceValidation extends DigestAlgorithmObsolescenceValidation<EvidenceRecordWrapper> {

    /**
     * Common constructor
     *
     * @param i18nProvider     the access to translations
     * @param token            instance of {@link EvidenceRecordWrapper} to be processed
     * @param validationDate   {@link Date} validation time
     * @param validationPolicy {@link ValidationPolicy} to be used during the validation
     */
    public EvidenceRecordAlgorithmObsolescenceValidation(I18nProvider i18nProvider, EvidenceRecordWrapper token,
                                                         Date validationDate, ValidationPolicy validationPolicy) {
        super(i18nProvider, token, Context.EVIDENCE_RECORD, validationDate, validationPolicy);
    }

    @Override
    protected ChainItem<XmlAOV> buildChain() {
        return buildDigestMatchersValidationChain(firstItem, token.getDigestMatchers(), token.getId());
    }

    @Override
    protected CryptographicSuite getCryptographicSuite() {
        return validationPolicy.getEvidenceRecordCryptographicConstraint();
    }

}
