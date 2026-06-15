package eu.europa.esig.dss.validation.process.eaa.status;

import eu.europa.esig.dss.detailedreport.jaxb.XmlSAV;
import eu.europa.esig.dss.diagnostic.CertificateWrapper;
import eu.europa.esig.dss.diagnostic.EAARevocationTokenWrapper;
import eu.europa.esig.dss.diagnostic.EAARevocationWrapper;
import eu.europa.esig.dss.enumerations.Indication;
import eu.europa.esig.dss.enumerations.SubIndication;
import eu.europa.esig.dss.i18n.I18nProvider;
import eu.europa.esig.dss.i18n.MessageTag;
import eu.europa.esig.dss.model.policy.LevelRule;
import eu.europa.esig.dss.validation.process.ChainItem;
import eu.europa.esig.dss.validation.process.ValidationProcessUtils;

import java.util.Date;

/**
 * Checks whether the issuer certificate of the EAA revocation token was valid at the EAA revocation token issuance time
 *
 */
public class EAARevocationIssuerValidAtIssuanceTimeCheck extends ChainItem<XmlSAV> {

    /** EAA revocation token to check */
    private final EAARevocationTokenWrapper eaaStatusToken;

    /**
     * Default constructor
     *
     * @param i18nProvider {@link I18nProvider}
     * @param result {@link XmlSAV}
     * @param eaaStatusToken {@link EAARevocationWrapper}
     * @param constraint {@link LevelRule}
     */
    public EAARevocationIssuerValidAtIssuanceTimeCheck(I18nProvider i18nProvider, XmlSAV result,
                                                       EAARevocationTokenWrapper eaaStatusToken, LevelRule constraint) {
        super(i18nProvider, result, constraint);
        this.eaaStatusToken = eaaStatusToken;
    }

    @Override
    protected boolean process() {
        Date issuedAt = eaaStatusToken.getIssuedAt();
        CertificateWrapper signingCertificate = eaaStatusToken.getSigningCertificate();
        return issuedAt != null && signingCertificate != null
                && !issuedAt.before(signingCertificate.getNotBefore())
                && !issuedAt.after(signingCertificate.getNotAfter());
    }

    @Override
    protected MessageTag getMessageTag() {
        return MessageTag.EAA_REV_ISS_VALID;
    }

    @Override
    protected MessageTag getErrorMessageTag() {
        return MessageTag.EAA_REV_ISS_VALID_ANS;
    }

    @Override
    protected String buildAdditionalInfo() {
        if (eaaStatusToken.getSigningCertificate() != null) {
            return i18nProvider.getMessage(MessageTag.EAA_REV_ISS_CERT, ValidationProcessUtils.getFormattedDate(eaaStatusToken.getIssuedAt()),
                    ValidationProcessUtils.getFormattedDate(eaaStatusToken.getSigningCertificate().getNotBefore()),
                    ValidationProcessUtils.getFormattedDate(eaaStatusToken.getSigningCertificate().getNotAfter()));
        }
        return null;
    }


    @Override
    protected Indication getFailedIndicationForConclusion() {
        return Indication.INDETERMINATE;
    }

    @Override
    protected SubIndication getFailedSubIndicationForConclusion() {
        return SubIndication.EAA_CONSTRAINTS_FAILURE;
    }

}
