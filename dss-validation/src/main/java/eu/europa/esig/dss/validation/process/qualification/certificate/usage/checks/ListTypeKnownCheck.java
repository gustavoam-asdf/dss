package eu.europa.esig.dss.validation.process.qualification.certificate.usage.checks;

import eu.europa.esig.dss.detailedreport.jaxb.XmlCertificateApprovalStatusProcess;
import eu.europa.esig.dss.enumerations.Indication;
import eu.europa.esig.dss.enumerations.ListType;
import eu.europa.esig.dss.enumerations.SubIndication;
import eu.europa.esig.dss.i18n.I18nProvider;
import eu.europa.esig.dss.i18n.MessageTag;
import eu.europa.esig.dss.model.policy.LevelRule;
import eu.europa.esig.dss.validation.process.ChainItem;

/**
 * Verifies whether the list type is known
 *
 */
public class ListTypeKnownCheck extends ChainItem<XmlCertificateApprovalStatusProcess> {

    /** List Type URI */
    private final String listTypeUri;

    /**
     * Default constructor
     *
     * @param i18nProvider {@link I18nProvider}
     * @param result {@link XmlCertificateApprovalStatusProcess}
     * @param listTypeUri {@link String}
     * @param constraint {@link LevelRule}
     */
    public ListTypeKnownCheck(I18nProvider i18nProvider, XmlCertificateApprovalStatusProcess result,
                              String listTypeUri, LevelRule constraint) {
        super(i18nProvider, result, constraint);
        this.listTypeUri = listTypeUri;
    }

    @Override
    protected boolean process() {
        ListType listType = ListType.fromUri(listTypeUri);
        return listType != null && listType.getLabel() != null; // Label is present -> defined
    }

    @Override
    protected String buildAdditionalInfo() {
        return i18nProvider.getMessage(MessageTag.CERTIFICATE_USAGE_LIST_TYPE, listTypeUri);
    }

    @Override
    protected MessageTag getMessageTag() {
        return MessageTag.CERT_USAGE_LIST_TYPE_KNOWN;
    }

    @Override
    protected MessageTag getErrorMessageTag() {
        return MessageTag.CERT_USAGE_LIST_TYPE_KNOWN_ANS;
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