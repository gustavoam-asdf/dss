package eu.europa.esig.dss.validation.process.eaa.checks;

import eu.europa.esig.dss.detailedreport.jaxb.XmlValidationProcessEAAPresentation;
import eu.europa.esig.dss.diagnostic.EAAPresentationWrapper;
import eu.europa.esig.dss.diagnostic.jaxb.XmlDigestMatcher;
import eu.europa.esig.dss.enumerations.Indication;
import eu.europa.esig.dss.enumerations.SubIndication;
import eu.europa.esig.dss.i18n.I18nProvider;
import eu.europa.esig.dss.i18n.MessageTag;
import eu.europa.esig.dss.model.policy.LevelRule;
import eu.europa.esig.dss.utils.Utils;
import eu.europa.esig.dss.validation.process.ChainItem;

/**
 * This method verifies whether disclosures have been provided for all selectively disclosable claim hashes
 * present within the EAA Presentation's payload
 *
 */
public class DisclosureListExhaustiveCheck extends ChainItem<XmlValidationProcessEAAPresentation> {

    /** EAA Presentation to check */
    private final EAAPresentationWrapper eaaPresentation;

    /**
     * Default constructor
     *
     * @param i18nProvider {@link I18nProvider}
     * @param result {@link XmlValidationProcessEAAPresentation}
     * @param eaaPresentation {@link EAAPresentationWrapper}
     * @param constraint {@link LevelRule}
     */
    public DisclosureListExhaustiveCheck(I18nProvider i18nProvider, XmlValidationProcessEAAPresentation result,
                                  EAAPresentationWrapper eaaPresentation, LevelRule constraint) {
        super(i18nProvider, result, constraint);
        this.eaaPresentation = eaaPresentation;
    }

    @Override
    protected boolean process() {
        if (Utils.isCollectionEmpty(eaaPresentation.getDigestMatchers())) {
            return true;
        }
        return eaaPresentation.getDigestMatchers().stream().allMatch(XmlDigestMatcher::isDataFound);
    }

    @Override
    protected MessageTag getMessageTag() {
        return MessageTag.EAA_DLEEAAP;
    }

    @Override
    protected MessageTag getErrorMessageTag() {
        return MessageTag.EAA_DLEEAAP_ANS;
    }

    @Override
    protected Indication getFailedIndicationForConclusion() {
        return Indication.INDETERMINATE;
    }

    @Override
    protected SubIndication getFailedSubIndicationForConclusion() {
        return SubIndication.SIGNED_DATA_NOT_FOUND;
    }

}
