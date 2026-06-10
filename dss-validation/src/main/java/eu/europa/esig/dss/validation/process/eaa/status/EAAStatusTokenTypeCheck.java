package eu.europa.esig.dss.validation.process.eaa.status;

import eu.europa.esig.dss.detailedreport.jaxb.XmlFC;
import eu.europa.esig.dss.diagnostic.EAAStatusTokenWrapper;
import eu.europa.esig.dss.enumerations.Indication;
import eu.europa.esig.dss.enumerations.SubIndication;
import eu.europa.esig.dss.i18n.I18nProvider;
import eu.europa.esig.dss.i18n.MessageTag;
import eu.europa.esig.dss.model.policy.MultiValuesRule;
import eu.europa.esig.dss.spi.DSSUtils;
import eu.europa.esig.dss.validation.process.bbb.AbstractMultiValuesCheckItem;

/**
 * Verifies whether the type declared for the EAA status token is within an acceptable list of values
 *
 */
public class EAAStatusTokenTypeCheck extends AbstractMultiValuesCheckItem<XmlFC> {

    /** RFC 7519 type prefix */
    private static final String MIME_TYPE_APPLICATION_PREFIX = "application/";

    /** EAA status token to check */
    private final EAAStatusTokenWrapper eaaStatusToken;

    /**
     * Default constructor
     *
     * @param i18nProvider {@link I18nProvider}
     * @param result {@link XmlFC}
     * @param eaaStatusToken {@link EAAStatusTokenWrapper}
     * @param constraint {@link MultiValuesRule}
     */
    public EAAStatusTokenTypeCheck(I18nProvider i18nProvider, XmlFC result,
                                   EAAStatusTokenWrapper eaaStatusToken, MultiValuesRule constraint) {
        super(i18nProvider, result, constraint);
        this.eaaStatusToken = eaaStatusToken;
    }

    @Override
    protected boolean process() {
        return processValueCheck(getType());
    }

    private String getType() {
        // TODO : Separate JWT and CWT logic ?
        return getRFC7519SignatureType(eaaStatusToken.getType());
    }

    private String getRFC7519SignatureType(String mimeType) {
        if (mimeType == null) {
            return null;
        }
        String shortMimeTypeString = DSSUtils.stripFirstLeadingOccurrence(mimeType, MIME_TYPE_APPLICATION_PREFIX);
        if (!shortMimeTypeString.contains("/")) {
            return shortMimeTypeString;
        } else {
            // return original if contains other '/'
            return mimeType;
        }
    }

    @Override
    protected MessageTag getMessageTag() {
        return MessageTag.EAA_STATUS_TYPE;
    }

    @Override
    protected MessageTag getErrorMessageTag() {
        return MessageTag.EAA_STATUS_TYPE_ANS;
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
