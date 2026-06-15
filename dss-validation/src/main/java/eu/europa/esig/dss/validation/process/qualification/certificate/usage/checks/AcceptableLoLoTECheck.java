package eu.europa.esig.dss.validation.process.qualification.certificate.usage.checks;

import eu.europa.esig.dss.detailedreport.jaxb.XmlConstraintsConclusion;
import eu.europa.esig.dss.detailedreport.jaxb.XmlLoTEAnalysis;
import eu.europa.esig.dss.i18n.I18nProvider;
import eu.europa.esig.dss.i18n.MessageTag;
import eu.europa.esig.dss.model.policy.LevelRule;

/**
 * Verifies if the validation of a LoLoTE was successful
 *
 * @param <T> {@link XmlConstraintsConclusion}
 */
public class AcceptableLoLoTECheck<T extends XmlConstraintsConclusion> extends AcceptableLoTECheck<T> {

    /**
     * Default constructor
     *
     * @param i18nProvider {@link I18nProvider}
     * @param result {@link XmlConstraintsConclusion}
     * @param loloteAnalysis {@link XmlLoTEAnalysis}
     * @param constraint {@link LevelRule}
     */
    public AcceptableLoLoTECheck(I18nProvider i18nProvider, T result, XmlLoTEAnalysis loloteAnalysis,
                                 LevelRule constraint) {
        super(i18nProvider, result, loloteAnalysis, constraint);
    }

    @Override
    protected MessageTag getMessageTag() {
        return MessageTag.CERT_USAGE_LOLOTE_ACCEPT;
    }

    @Override
    protected MessageTag getErrorMessageTag() {
        return MessageTag.CERT_USAGE_LOLOTE_ACCEPT_ANS;
    }

}
