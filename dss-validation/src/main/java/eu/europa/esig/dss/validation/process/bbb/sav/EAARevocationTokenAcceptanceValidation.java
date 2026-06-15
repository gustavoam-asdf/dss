package eu.europa.esig.dss.validation.process.bbb.sav;

import eu.europa.esig.dss.detailedreport.jaxb.XmlAOV;
import eu.europa.esig.dss.detailedreport.jaxb.XmlSAV;
import eu.europa.esig.dss.diagnostic.EAARevocationTokenWrapper;
import eu.europa.esig.dss.diagnostic.RevocationWrapper;
import eu.europa.esig.dss.enumerations.Context;
import eu.europa.esig.dss.i18n.I18nProvider;
import eu.europa.esig.dss.i18n.MessageTag;
import eu.europa.esig.dss.model.policy.LevelRule;
import eu.europa.esig.dss.model.policy.MultiValuesRule;
import eu.europa.esig.dss.model.policy.ValidationPolicy;
import eu.europa.esig.dss.validation.process.ChainItem;
import eu.europa.esig.dss.validation.process.eaa.status.EAARevocationExpirationTimeCheck;
import eu.europa.esig.dss.validation.process.eaa.status.EAARevocationIssuanceTimeCheck;
import eu.europa.esig.dss.validation.process.eaa.status.EAARevocationIssuerValidAtIssuanceTimeCheck;
import eu.europa.esig.dss.validation.process.eaa.status.EAARevocationNotExpiredCheck;
import eu.europa.esig.dss.validation.process.eaa.status.EAARevocationSubjectCheck;
import eu.europa.esig.dss.validation.process.eaa.status.EAARevocationSubjectMatchCheck;

import java.util.Date;

/**
 * Performs verification of EAA revocation token against the validationPolicy defined acceptance criteria
 *
 */
public class EAARevocationTokenAcceptanceValidation extends AbstractAcceptanceValidation<EAARevocationTokenWrapper> {

    /**
     * Default constructor
     *
     * @param i18nProvider {@link I18nProvider}
     * @param currentTime {@link Date} validation time
     * @param eaaRevocationTokenWrapper {@link RevocationWrapper}
     * @param aov {@link XmlAOV}
     * @param validationPolicy {@link ValidationPolicy}
     */
    public EAARevocationTokenAcceptanceValidation(I18nProvider i18nProvider, Date currentTime,
                                              EAARevocationTokenWrapper eaaRevocationTokenWrapper, XmlAOV aov, ValidationPolicy validationPolicy) {
        super(i18nProvider, eaaRevocationTokenWrapper, currentTime, Context.EAA_REVOCATION, aov, validationPolicy);
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
        LevelRule constraint = validationPolicy.getEAARevocationIssuanceTimeConstraint();
        return new EAARevocationIssuanceTimeCheck(i18nProvider, result, token, constraint);
    }

    private ChainItem<XmlSAV> expirationTime() {
        LevelRule constraint = validationPolicy.getEAARevocationExpirationTimeConstraint();
        return new EAARevocationExpirationTimeCheck(i18nProvider, result, token, constraint);
    }

    private ChainItem<XmlSAV> notExpired() {
        LevelRule constraint = validationPolicy.getEAARevocationNotExpiredConstraint();
        return new EAARevocationNotExpiredCheck(i18nProvider, result, token, currentTime, constraint);
    }

    private ChainItem<XmlSAV> subject() {
        MultiValuesRule constraint = validationPolicy.getEAARevocationSubjectConstraint();
        return new EAARevocationSubjectCheck(i18nProvider, result, token, constraint);
    }

    private ChainItem<XmlSAV> subjectMatches() {
        LevelRule constraint = validationPolicy.getEAARevocationSubjectMatchConstraint();
        return new EAARevocationSubjectMatchCheck(i18nProvider, result, token, constraint);
    }

    private ChainItem<XmlSAV> issuerValidAtIssuanceTime() {
        LevelRule constraint = validationPolicy.getEAARevocationIssuerValidAtIssuanceTimeConstraint();
        return new EAARevocationIssuerValidAtIssuanceTimeCheck(i18nProvider, result, token, constraint);
    }

}
