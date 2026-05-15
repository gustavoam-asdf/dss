package eu.europa.esig.dss.validation.process.eaa.checks;

import eu.europa.esig.dss.detailedreport.jaxb.XmlFC;
import eu.europa.esig.dss.diagnostic.EAAWrapper;
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
 * present within the EAA's payload
 *
 */
public class DisclosureListExhaustiveCheck extends ChainItem<XmlFC> {

    /** EAA to check */
    private final EAAWrapper eaa;

    /**
     * Default constructor
     *
     * @param i18nProvider {@link I18nProvider}
     * @param result {@link XmlFC}
     * @param eaa {@link EAAWrapper}
     * @param constraint {@link LevelRule}
     */
    public DisclosureListExhaustiveCheck(I18nProvider i18nProvider, XmlFC result,
                                         EAAWrapper eaa, LevelRule constraint) {
        super(i18nProvider, result, constraint);
        this.eaa = eaa;
    }

    @Override
    protected boolean process() {
        if (Utils.isCollectionEmpty(eaa.getDigestMatchers())) {
            return true;
        }
        return eaa.getDigestMatchers().stream().allMatch(XmlDigestMatcher::isDataFound);
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
        return Indication.FAILED;
    }

    @Override
    protected SubIndication getFailedSubIndicationForConclusion() {
        return SubIndication.FORMAT_FAILURE;
    }

}
