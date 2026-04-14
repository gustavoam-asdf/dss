package eu.europa.esig.dss.validation.process.qualification.certificate.usage.checks;

import eu.europa.esig.dss.detailedreport.jaxb.XmlValidationCertificateUsage;
import eu.europa.esig.dss.diagnostic.TrustServiceWrapper;
import eu.europa.esig.dss.diagnostic.TrustedEntityServiceWrapper;
import eu.europa.esig.dss.enumerations.Indication;
import eu.europa.esig.dss.enumerations.SubIndication;
import eu.europa.esig.dss.i18n.I18nProvider;
import eu.europa.esig.dss.i18n.MessageTag;
import eu.europa.esig.dss.model.policy.LevelRule;
import eu.europa.esig.dss.utils.Utils;
import eu.europa.esig.dss.validation.process.ChainItem;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Verifies whether the Trusted Entity Service statuses are consistent
 *
 */
public class TrustedEntityServiceStatusConsistencyCheck extends ChainItem<XmlValidationCertificateUsage> {

    /** List of {@code TrustedEntityServiceWrapper}s at control time */
    private final List<TrustedEntityServiceWrapper> trustedServicesWithSti;

    /**
     * Default constructor
     *
     * @param i18nProvider {@link I18nProvider}
     * @param result {@link XmlValidationCertificateUsage}
     * @param trustedServicesWithSti list of {@link TrustServiceWrapper}s
     * @param constraint {@link LevelRule}
     */
    public TrustedEntityServiceStatusConsistencyCheck(I18nProvider i18nProvider, XmlValidationCertificateUsage result,
                                                    List<TrustedEntityServiceWrapper> trustedServicesWithSti, LevelRule constraint) {
        super(i18nProvider, result, constraint);
        this.trustedServicesWithSti = trustedServicesWithSti;
    }

    @Override
    protected boolean process() {
        Set<String> statusesSet = getApplicableStatusesSet();
        return Utils.collectionSize(statusesSet) <= 1;
    }

    private Set<String> getApplicableStatusesSet() {
        return trustedServicesWithSti.stream().map(TrustedEntityServiceWrapper::getStatus).collect(Collectors.toSet());
    }

    @Override
    protected MessageTag getMessageTag() {
        return MessageTag.CERT_USAGE_STATUS_CONS;
    }

    @Override
    protected MessageTag getErrorMessageTag() {
        return MessageTag.CERT_USAGE_STATUS_CONS_ANS;
    }

    @Override
    protected String buildAdditionalInfo() {
        Set<String> applicableStatusesSet = getApplicableStatusesSet();
        if (Utils.collectionSize(applicableStatusesSet) == 1) {
            return i18nProvider.getMessage(MessageTag.CERTIFICATE_USAGE_STATUS, applicableStatusesSet.iterator().next());
        }
        return i18nProvider.getMessage(MessageTag.CERTIFICATE_USAGE_STATUSES, applicableStatusesSet);
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
