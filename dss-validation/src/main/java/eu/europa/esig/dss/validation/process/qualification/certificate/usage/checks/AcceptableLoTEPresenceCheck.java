package eu.europa.esig.dss.validation.process.qualification.certificate.usage.checks;

import eu.europa.esig.dss.detailedreport.jaxb.XmlConstraintsConclusion;
import eu.europa.esig.dss.diagnostic.jaxb.XmlTrustSourceList;
import eu.europa.esig.dss.enumerations.Indication;
import eu.europa.esig.dss.enumerations.SubIndication;
import eu.europa.esig.dss.i18n.I18nProvider;
import eu.europa.esig.dss.i18n.MessageTag;
import eu.europa.esig.dss.model.policy.LevelRule;
import eu.europa.esig.dss.utils.Utils;
import eu.europa.esig.dss.validation.process.ChainItem;

import java.util.Set;

/**
 * Checks if an acceptable LoTE has been found
 *
 * @param <T> {@link XmlConstraintsConclusion}
 */
public class AcceptableLoTEPresenceCheck<T extends XmlConstraintsConclusion> extends ChainItem<T> {

    /** Set of acceptable Lists of Trusted Entities */
    private final Set<XmlTrustSourceList> validLoTEss;

    /**
     * Default constructor
     *
     * @param i18nProvider {@link I18nProvider}
     * @param result {@link XmlConstraintsConclusion}
     * @param validLoTEUrls a set of {@link XmlTrustSourceList}s
     * @param constraint {@link LevelRule}
     */
    public AcceptableLoTEPresenceCheck(I18nProvider i18nProvider, T result, Set<XmlTrustSourceList> validLoTEUrls,
                                       LevelRule constraint) {
        super(i18nProvider, result, constraint);

        this.validLoTEss = validLoTEUrls;
    }

    @Override
    public boolean process() {
        return Utils.isCollectionNotEmpty(validLoTEss);
    }

    @Override
    protected MessageTag getMessageTag() {
        return MessageTag.CERT_USAGE_VALID_LOTE_PRESENT;
    }

    @Override
    protected MessageTag getErrorMessageTag() {
        return MessageTag.CERT_USAGE_VALID_LOTE_PRESENT_ANS;
    }

    @Override
    protected Indication getFailedIndicationForConclusion() {
        return Indication.FAILED;
    }

    @Override
    protected SubIndication getFailedSubIndicationForConclusion() {
        return null;
    }

}