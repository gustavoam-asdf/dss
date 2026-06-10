package eu.europa.esig.dss.validation.process.bbb.sav;

import eu.europa.esig.dss.detailedreport.jaxb.XmlAOV;
import eu.europa.esig.dss.detailedreport.jaxb.XmlBasicBuildingBlocks;
import eu.europa.esig.dss.detailedreport.jaxb.XmlConclusion;
import eu.europa.esig.dss.detailedreport.jaxb.XmlConstraint;
import eu.europa.esig.dss.detailedreport.jaxb.XmlSAV;
import eu.europa.esig.dss.diagnostic.EAAStatusWrapper;
import eu.europa.esig.dss.diagnostic.EAAWrapper;
import eu.europa.esig.dss.enumerations.Context;
import eu.europa.esig.dss.enumerations.EAAType;
import eu.europa.esig.dss.i18n.I18nProvider;
import eu.europa.esig.dss.i18n.MessageTag;
import eu.europa.esig.dss.model.policy.LevelRule;
import eu.europa.esig.dss.model.policy.MultiValuesRule;
import eu.europa.esig.dss.model.policy.ValidationPolicy;
import eu.europa.esig.dss.utils.Utils;
import eu.europa.esig.dss.validation.process.ChainItem;
import eu.europa.esig.dss.validation.process.eaa.checks.AcceptableEAAStatusFoundCheck;
import eu.europa.esig.dss.validation.process.eaa.checks.EAAAdministrativeExpirationDatePresentCheck;
import eu.europa.esig.dss.validation.process.eaa.checks.EAAAdministrativeIssuanceDatePresentCheck;
import eu.europa.esig.dss.validation.process.eaa.checks.EAAAdministrativePeriodNotExpiredCheck;
import eu.europa.esig.dss.validation.process.eaa.checks.EAACategoryCheck;
import eu.europa.esig.dss.validation.process.eaa.checks.EAAClaimsCheck;
import eu.europa.esig.dss.validation.process.eaa.checks.EAAExpirationPresentCheck;
import eu.europa.esig.dss.validation.process.eaa.checks.EAAIdentifierPresentCheck;
import eu.europa.esig.dss.validation.process.eaa.checks.EAAIssuanceDatePresentCheck;
import eu.europa.esig.dss.validation.process.eaa.checks.EAAIssuingAuthorityCheck;
import eu.europa.esig.dss.validation.process.eaa.checks.EAAIssuingAuthorityRegistrationIdentifierCheck;
import eu.europa.esig.dss.validation.process.eaa.checks.EAAIssuingCountryCheck;
import eu.europa.esig.dss.validation.process.eaa.checks.EAANotBeforePresentCheck;
import eu.europa.esig.dss.validation.process.eaa.checks.EAANotExpiredCheck;
import eu.europa.esig.dss.validation.process.eaa.checks.EAAOneTimeUseCheck;
import eu.europa.esig.dss.validation.process.eaa.checks.EAAPseudonymUsageCheck;
import eu.europa.esig.dss.validation.process.eaa.checks.EAAShortLivedCheck;
import eu.europa.esig.dss.validation.process.eaa.checks.EAAStatusAcceptableCheck;
import eu.europa.esig.dss.validation.process.eaa.checks.EAAStatusAvailableCheck;
import eu.europa.esig.dss.validation.process.eaa.checks.EAAStatusPresentCheck;
import eu.europa.esig.dss.validation.process.eaa.checks.EAAStatusValidCheck;
import eu.europa.esig.dss.validation.process.eaa.checks.EAASubjectCheck;
import eu.europa.esig.dss.validation.process.eaa.checks.EAASubjectPseudonymCheck;
import eu.europa.esig.dss.validation.process.eaa.checks.EAASupportedClaimsCheck;
import eu.europa.esig.dss.validation.process.eaa.checks.EAATypeCheck;
import eu.europa.esig.dss.validation.process.eaa.checks.EAATypeIntegrityPresentCheck;
import eu.europa.esig.dss.validation.process.eaa.checks.ETSI194721ConformanceCheck;

import java.util.Date;
import java.util.Map;

/**
 * Performs verification of EAA against the validationPolicy defined acceptance criteria
 * 
 */
public class EAAAcceptanceValidation extends AbstractAcceptanceValidation<EAAWrapper> {

    /** A map of BasicBuildingBlocks */
    private final Map<String, XmlBasicBuildingBlocks> bbbs;

    /**
     * Default constructor
     *
     * @param i18nProvider {@link I18nProvider}
     * @param currentTime {@link Date} validation time
     * @param eaaWrapper {@link EAAWrapper}
     * @param aov {@link XmlAOV}
     * @param validationPolicy {@link ValidationPolicy}
     */
    public EAAAcceptanceValidation(I18nProvider i18nProvider, Date currentTime,
                                   EAAWrapper eaaWrapper, Map<String, XmlBasicBuildingBlocks> bbbs, XmlAOV aov,
                                   ValidationPolicy validationPolicy) {
        super(i18nProvider, eaaWrapper, currentTime, Context.EAA, aov, validationPolicy);
        this.bbbs = bbbs;
    }

    @Override
    protected MessageTag getTitle() {
        return MessageTag.SIGNATURE_ACCEPTANCE_VALIDATION;
    }

    @Override
    protected void initChain() {
        ChainItem<XmlSAV> item = firstItem = etsi194721Conformance();

        item = item.setNextItem(eaaType());
        if (EAAType.SD_JWT_VC == token.getEAAType()) {
            item = item.setNextItem(typeIntegrityPresent());
        }

        if (EAAType.ISO_IEC_MDOC == token.getEAAType()) {
            item = item.setNextItem(issuanceDatePresent());
        }

        item = item.setNextItem(eaaIdentifierPresent());

        item = item.setNextItem(notBeforePresent());

        item = item.setNextItem(expirationPresent());

        if (token.getEAANotBefore() != null && token.getEAAExpiration() != null) {
            item = item.setNextItem(notExpired());
        }

        item = item.setNextItem(administrativeIssuanceDatePresent());

        item = item.setNextItem(administrativeExpirationDatePresent());

        if (token.getAdministrativeIssuanceDate() != null && token.getAdministrativeExpirationDate() != null) {
            item = item.setNextItem(administrativePeriodNotExpired());
        }

        item = item.setNextItem(category());

        item = item.setNextItem(subject());

        item = item.setNextItem(subjectPseudonym());

        item = item.setNextItem(issuingCountry());

        item = item.setNextItem(issuingAuthority());

        item = item.setNextItem(issuingAuthorityRegistrationIdentifier());

        if (Utils.isTrue(token.getOneTimeUse())) {
            item = item.setNextItem(oneTimeUse());
        }

        if (Utils.isTrue(token.getShortLived())) {

            item = item.setNextItem(shortLived());

        } else {

            // TODO : make status check configurable ?

            EAAStatusPresentCheck eaaStatusPresentCheck = statusPresent();

            item = item.setNextItem(eaaStatusPresentCheck);

            if (eaaStatusPresentCheck.process()) {

                item = item.setNextItem(statusAvailable());

                EAAStatusWrapper lastAcceptableStatus = null;
                for (EAAStatusWrapper eaaStatusWrapper : token.getEAAStatuses()) {

                    XmlBasicBuildingBlocks eaaStatusBBB = bbbs.get(eaaStatusWrapper.getId());
                    if (eaaStatusBBB == null) {
                        throw new IllegalStateException(String.format("No BasicBuildingBlock found for token with Id '%s'", eaaStatusWrapper.getId()));
                    }

                    item = item.setNextItem(statusAcceptable(eaaStatusWrapper, eaaStatusBBB.getConclusion()));

                    if (isValidConclusion(eaaStatusBBB.getConclusion())) {
                        if (lastAcceptableStatus == null || lastAcceptableStatus.getIssuedAt().before(eaaStatusWrapper.getIssuedAt())) {
                            lastAcceptableStatus = eaaStatusWrapper;
                        }
                    }

                }

                item = item.setNextItem(acceptableStatusFound(lastAcceptableStatus));

                if (lastAcceptableStatus != null) {
                    item = item.setNextItem(statusValid(lastAcceptableStatus));
                }

            }

        }

        if (token.getHolderPseudonym() != null) {
            item = item.setNextItem(usePseudonym());
        }

        item = item.setNextItem(claims());

        item = item.setNextItem(supportedClaims());

        // cryptographic check
        item = cryptographic(item);

    }

    private ChainItem<XmlSAV> etsi194721Conformance() {
        LevelRule constraint = validationPolicy.getEAAETSI194721ConformanceConstraint();
        return new ETSI194721ConformanceCheck(i18nProvider, result, token, currentTime, constraint);
    }

    private ChainItem<XmlSAV> eaaType() {
        MultiValuesRule constraint = validationPolicy.getEAATypeConstraint();
        return new EAATypeCheck(i18nProvider, result, token, constraint);
    }

    private ChainItem<XmlSAV> typeIntegrityPresent() {
        LevelRule constraint = validationPolicy.getEAATypeIntegrityPresentConstraint();
        return new EAATypeIntegrityPresentCheck(i18nProvider, result, token, constraint);
    }

    private ChainItem<XmlSAV> notBeforePresent() {
        LevelRule constraint = validationPolicy.getEAANotBeforePresentConstraint();
        return new EAANotBeforePresentCheck(i18nProvider, result, token, constraint);
    }

    private ChainItem<XmlSAV> expirationPresent() {
        LevelRule constraint = validationPolicy.getEAAExpirationPresentConstraint();
        return new EAAExpirationPresentCheck(i18nProvider, result, token, constraint);
    }

    private ChainItem<XmlSAV> notExpired() {
        LevelRule constraint = validationPolicy.getEAANotExpiredConstraint();
        return new EAANotExpiredCheck(i18nProvider, result, token, currentTime, constraint);
    }

    private ChainItem<XmlSAV> administrativeIssuanceDatePresent() {
        LevelRule constraint = validationPolicy.getEAAAdministrativeIssuanceDatePresentConstraint();
        return new EAAAdministrativeIssuanceDatePresentCheck(i18nProvider, result, token, constraint);
    }

    private ChainItem<XmlSAV> administrativeExpirationDatePresent() {
        LevelRule constraint = validationPolicy.getEAAAdministrativeExpirationDatePresentConstraint();
        return new EAAAdministrativeExpirationDatePresentCheck(i18nProvider, result, token, constraint);
    }

    private ChainItem<XmlSAV> administrativePeriodNotExpired() {
        LevelRule constraint = validationPolicy.getEAAAdministrativePeriodNotExpiredConstraint();
        return new EAAAdministrativePeriodNotExpiredCheck(i18nProvider, result, token, currentTime, constraint);
    }

    private ChainItem<XmlSAV> eaaIdentifierPresent() {
        LevelRule constraint = validationPolicy.getEAAIdentifierPresentConstraint();
        return new EAAIdentifierPresentCheck(i18nProvider, result, token, constraint);
    }

    private ChainItem<XmlSAV> issuanceDatePresent() {
        LevelRule constraint = validationPolicy.getEAAIssuanceDatePresentConstraint();
        return new EAAIssuanceDatePresentCheck(i18nProvider, result, token, constraint);
    }

    private ChainItem<XmlSAV> category() {
        MultiValuesRule constraint = validationPolicy.getEAACategoryConstraint();
        return new EAACategoryCheck(i18nProvider, result, token, constraint);
    }

    private ChainItem<XmlSAV> subject() {
        MultiValuesRule constraint = validationPolicy.getEAASubjectConstraint();
        return new EAASubjectCheck(i18nProvider, result, token, constraint);
    }

    private ChainItem<XmlSAV> subjectPseudonym() {
        MultiValuesRule constraint = validationPolicy.getEAASubjectPseudonymConstraint();
        return new EAASubjectPseudonymCheck(i18nProvider, result, token, constraint);
    }

    private ChainItem<XmlSAV> issuingCountry() {
        MultiValuesRule constraint = validationPolicy.getEAAIssuingCountryConstraint();
        return new EAAIssuingCountryCheck(i18nProvider, result, token, constraint);
    }

    private ChainItem<XmlSAV> issuingAuthority() {
        MultiValuesRule constraint = validationPolicy.getEAAIssuingAuthorityConstraint();
        return new EAAIssuingAuthorityCheck(i18nProvider, result, token, constraint);
    }

    private ChainItem<XmlSAV> issuingAuthorityRegistrationIdentifier() {
        MultiValuesRule constraint = validationPolicy.getEAAIssuingAuthorityRegistrationIdentifierConstraint();
        return new EAAIssuingAuthorityRegistrationIdentifierCheck(i18nProvider, result, token, constraint);
    }

    private EAAStatusPresentCheck statusPresent() {
        LevelRule constraint = validationPolicy.getEAAStatusPresentConstraint();
        return new EAAStatusPresentCheck(i18nProvider, result, token, constraint);
    }

    private ChainItem<XmlSAV> statusAvailable() {
        LevelRule constraint = validationPolicy.getEAAStatusAvailableConstraint();
        return new EAAStatusAvailableCheck(i18nProvider, result, token, constraint);
    }

    private ChainItem<XmlSAV> statusAcceptable(EAAStatusWrapper eaaStatusWrapper, XmlConclusion xmlConclusion) {
        return new EAAStatusAcceptableCheck(i18nProvider, result, eaaStatusWrapper, xmlConclusion, getWarnLevelRule());
    }

    private ChainItem<XmlSAV> acceptableStatusFound(EAAStatusWrapper acceptableEAAStatusWrapper) {
        LevelRule constraint = validationPolicy.getEAAStatusAvailableConstraint();
        return new AcceptableEAAStatusFoundCheck(i18nProvider, result, acceptableEAAStatusWrapper, constraint);
    }

    private ChainItem<XmlSAV> statusValid(EAAStatusWrapper eaaStatusWrapper) {
        LevelRule constraint = validationPolicy.getEAAStatusValidConstraint();
        return new EAAStatusValidCheck(i18nProvider, result, eaaStatusWrapper, constraint);
    }

    private ChainItem<XmlSAV> shortLived() {
        LevelRule constraint = validationPolicy.getEAAShortLivedConstraint();
        return new EAAShortLivedCheck(i18nProvider, result, token, constraint);
    }

    private ChainItem<XmlSAV> oneTimeUse() {
        LevelRule constraint = validationPolicy.getEAAOneTimeUseConstraint();
        return new EAAOneTimeUseCheck(i18nProvider, result, token, constraint);
    }

    private ChainItem<XmlSAV> usePseudonym() {
        LevelRule constraint = validationPolicy.getEAAUsePseudonymConstraint();
        return new EAAPseudonymUsageCheck(i18nProvider, result, token, constraint);
    }

    private ChainItem<XmlSAV> claims() {
        MultiValuesRule constraint = validationPolicy.getEAAClaimsConstraint();
        return new EAAClaimsCheck(i18nProvider, result, token, constraint);
    }

    private ChainItem<XmlSAV> supportedClaims() {
        MultiValuesRule constraint = validationPolicy.getEAASupportedClaimsConstraint();
        return new EAASupportedClaimsCheck(i18nProvider, result, token, constraint);
    }

    @Override
    protected void collectMessages(XmlConclusion conclusion, XmlConstraint constraint) {
        if (!MessageTag.EAA_STATUS_ACC.getId().equals(constraint.getName().getKey())) {
            super.collectMessages(conclusion, constraint);
        }
    }
}
