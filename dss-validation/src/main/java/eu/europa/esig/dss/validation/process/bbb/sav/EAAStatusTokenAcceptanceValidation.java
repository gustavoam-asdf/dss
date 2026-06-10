package eu.europa.esig.dss.validation.process.bbb.sav;

import eu.europa.esig.dss.detailedreport.jaxb.XmlAOV;
import eu.europa.esig.dss.diagnostic.EAAStatusTokenWrapper;
import eu.europa.esig.dss.diagnostic.RevocationWrapper;
import eu.europa.esig.dss.enumerations.Context;
import eu.europa.esig.dss.i18n.I18nProvider;
import eu.europa.esig.dss.i18n.MessageTag;
import eu.europa.esig.dss.model.policy.ValidationPolicy;

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

        // TODO : add checks

        firstItem = cryptographic(firstItem);

    }

}
