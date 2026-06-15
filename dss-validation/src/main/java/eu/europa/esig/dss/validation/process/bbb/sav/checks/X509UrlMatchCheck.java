package eu.europa.esig.dss.validation.process.bbb.sav.checks;

import eu.europa.esig.dss.detailedreport.jaxb.XmlSAV;
import eu.europa.esig.dss.diagnostic.CertificateWrapper;
import eu.europa.esig.dss.diagnostic.RelatedCertificateWrapper;
import eu.europa.esig.dss.diagnostic.SignatureWrapper;
import eu.europa.esig.dss.enumerations.CertificateRefOrigin;
import eu.europa.esig.dss.enumerations.Indication;
import eu.europa.esig.dss.enumerations.SubIndication;
import eu.europa.esig.dss.i18n.I18nProvider;
import eu.europa.esig.dss.i18n.MessageTag;
import eu.europa.esig.dss.model.policy.LevelRule;
import eu.europa.esig.dss.validation.process.ChainItem;

import java.util.List;

/**
 * Verifies if the application was able to derive signing-certificate using the value of the 'x5u' (X.509 URL)
 * header parameter of the protected header of the signature
 *
 */
public class X509UrlMatchCheck extends ChainItem<XmlSAV> {

    /** The signature to verify */
    private final SignatureWrapper signature;

    /**
     * Default constructor
     *
     * @param i18nProvider {@link I18nProvider}
     * @param result {@link XmlSAV}
     * @param signature {@link SignatureWrapper}
     * @param constraint {@link LevelRule}
     */
    public X509UrlMatchCheck(I18nProvider i18nProvider, XmlSAV result, SignatureWrapper signature,
                             LevelRule constraint) {
        super(i18nProvider, result, constraint);
        this.signature = signature;
    }

    @Override
    protected boolean process() {
        CertificateWrapper signingCertificate = signature.getSigningCertificate();
        if (signingCertificate == null) {
            return false;
        }
        List<RelatedCertificateWrapper> x509UrlCertificates = signature.foundCertificates().getRelatedCertificatesByRefOrigin(CertificateRefOrigin.X509_URL);
        return x509UrlCertificates.stream().anyMatch(r -> signingCertificate.getId().equals(r.getId()));
    }

    @Override
    protected MessageTag getMessageTag() {
        return MessageTag.BBB_ICS_ISAX509UA;
    }

    @Override
    protected MessageTag getErrorMessageTag() {
        return MessageTag.BBB_ICS_ISAX509UA_ANS;
    }

    @Override
    protected Indication getFailedIndicationForConclusion() {
        return Indication.INDETERMINATE;
    }

    @Override
    protected SubIndication getFailedSubIndicationForConclusion() {
        return SubIndication.SIG_CONSTRAINTS_FAILURE;
    }

}
