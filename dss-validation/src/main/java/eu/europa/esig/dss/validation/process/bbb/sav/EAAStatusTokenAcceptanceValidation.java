package eu.europa.esig.dss.validation.process.bbb.sav;

import eu.europa.esig.dss.detailedreport.jaxb.XmlAOV;
import eu.europa.esig.dss.detailedreport.jaxb.XmlSAV;
import eu.europa.esig.dss.diagnostic.EAAStatusTokenWrapper;
import eu.europa.esig.dss.diagnostic.RevocationWrapper;
import eu.europa.esig.dss.enumerations.Context;
import eu.europa.esig.dss.i18n.I18nProvider;
import eu.europa.esig.dss.i18n.MessageTag;
import eu.europa.esig.dss.model.policy.LevelRule;
import eu.europa.esig.dss.model.policy.MultiValuesRule;
import eu.europa.esig.dss.model.policy.ValidationPolicy;
import eu.europa.esig.dss.validation.process.ChainItem;
import eu.europa.esig.dss.validation.process.eaa.status.EAAStatusExpirationTimeCheck;
import eu.europa.esig.dss.validation.process.eaa.status.EAAStatusIssuanceTimeCheck;
import eu.europa.esig.dss.validation.process.eaa.status.EAAStatusIssueValidAtIssuanceTimeCheck;
import eu.europa.esig.dss.validation.process.eaa.status.EAAStatusNotExpiredCheck;
import eu.europa.esig.dss.validation.process.eaa.status.EAAStatusSubjectCheck;
import eu.europa.esig.dss.validation.process.eaa.status.EAAStatusSubjectMatchCheck;

import java.util.Date;

/**
 * Performs verification of EAA status token against the validationPolicy defined acceptance criteria
 *
 */
public class EAAStatusTokenAcceptanceValidation extends AbstractAcceptanceValidation<EAAStatusTokenWrapper> {

    /**
     * Default constructor
     *
     * @param i18nProvider {@link I18nProvider}
     * @param currentTime {@link Date} validation time
     * @param eaaStatusTokenWrapper {@link RevocationWrapper}
     * @param aov {@link XmlAOV}
     * @param validationPolicy {@link ValidationPolicy}
     */
    public EAAStatusTokenAcceptanceValidation(I18nProvider i18nProvider, Date currentTime,
                                              EAAStatusTokenWrapper eaaStatusTokenWrapper, XmlAOV aov, ValidationPolicy validationPolicy) {
        super(i18nProvider, eaaStatusTokenWrapper, currentTime, Context.EAA_STATUS, aov, validationPolicy);
    }

    @Override
    protected MessageTag getTitle() {
        return MessageTag.SIGNATURE_ACCEPTANCE_VALIDATION;
    }

    @Override
    protected void initChain() {

        ChainItem<XmlSAV> item = firstItem = issuanceTime();

        item = item.setNextItem(expirationTime());

        if (token.getExpirationTime() != null) {
            item = item.setNextItem(notExpired());
        }

        item = item.setNextItem(subject());

        if (token.getSubject() != null) {
            item = item.setNextItem(subjectMatches());
        }

        if (token.getIssuedAt() != null) {
            item = item.setNextItem(issuerValidAtIssuanceTime());
        }

        item = cryptographic(item);

    }

    private ChainItem<XmlSAV> issuanceTime() {
        LevelRule constraint = validationPolicy.getEAAStatusIssuanceTimeConstraint();
        return new EAAStatusIssuanceTimeCheck(i18nProvider, result, token, constraint);
    }

    private ChainItem<XmlSAV> expirationTime() {
        LevelRule constraint = validationPolicy.getEAAStatusExpirationTimeConstraint();
        return new EAAStatusExpirationTimeCheck(i18nProvider, result, token, constraint);
    }

    private ChainItem<XmlSAV> notExpired() {
        LevelRule constraint = validationPolicy.getEAAStatusNotExpiredConstraint();
        return new EAAStatusNotExpiredCheck(i18nProvider, result, token, currentTime, constraint);
    }

    private ChainItem<XmlSAV> subject() {
        MultiValuesRule constraint = validationPolicy.getEAAStatusSubjectConstraint();
        return new EAAStatusSubjectCheck(i18nProvider, result, token, constraint);
    }

    private ChainItem<XmlSAV> subjectMatches() {
        LevelRule constraint = validationPolicy.getEAAStatusSubjectMatchConstraint();
        return new EAAStatusSubjectMatchCheck(i18nProvider, result, token, constraint);
    }

    private ChainItem<XmlSAV> issuerValidAtIssuanceTime() {
        LevelRule constraint = validationPolicy.getEAAStatusTokenIssuerValidAtIssuanceTimeConstraint();
        return new EAAStatusIssueValidAtIssuanceTimeCheck(i18nProvider, result, token, constraint);
    }

}
