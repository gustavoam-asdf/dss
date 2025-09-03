package eu.europa.esig.dss.validation.process.bbb.xcv.sub.checks;

import eu.europa.esig.dss.detailedreport.jaxb.XmlAOV;
import eu.europa.esig.dss.detailedreport.jaxb.XmlBlockType;
import eu.europa.esig.dss.detailedreport.jaxb.XmlConstraintsConclusion;
import eu.europa.esig.dss.detailedreport.jaxb.XmlCryptographicValidation;
import eu.europa.esig.dss.i18n.I18nProvider;
import eu.europa.esig.dss.i18n.MessageTag;
import eu.europa.esig.dss.utils.Utils;
import eu.europa.esig.dss.validation.process.bbb.aov.checks.AlgorithmObsolescenceValidationCheckWithId;

import java.util.Date;

/**
 * This class checks validity of the {@code eu.europa.esig.dss.validation.process.bbb.aov.AlgorithmObsolescenceValidation}
 * process performed on a certificate token alone
 *
 * @param <T> {@link XmlConstraintsConclusion}
 */
public class CertificateAlgorithmObsolescenceValidationCheck<T extends XmlConstraintsConclusion> extends AlgorithmObsolescenceValidationCheckWithId<T> {

    /**
     * Default constructor
     *
     * @param i18nProvider   {@link I18nProvider}
     * @param result         {@link XmlConstraintsConclusion}
     * @param aovResult      {@link XmlAOV}
     * @param validationDate {@link Date}
     * @param position       {@link MessageTag}
     * @param certificateId  {@link String}
     */
    public CertificateAlgorithmObsolescenceValidationCheck(I18nProvider i18nProvider, T result, XmlAOV aovResult,
                                                           Date validationDate, MessageTag position, String certificateId) {
        super(i18nProvider, result, aovResult, validationDate, position, certificateId);
    }

    @Override
    protected XmlBlockType getBlockType() {
        return XmlBlockType.AOV_XCV;
    }

    @Override
    protected boolean process() {
        XmlCryptographicValidation cryptographicValidation = getCertificateCryptographicValidation();
        return cryptographicValidation != null && isValidConclusion(cryptographicValidation.getConclusion());
    }

    private XmlCryptographicValidation getCertificateCryptographicValidation() {
        if (aovResult != null && aovResult.getCertificateChainCryptographicValidation() != null
                && Utils.isCollectionNotEmpty(aovResult.getCertificateChainCryptographicValidation().getCertificateCryptographicValidation())) {
            for (XmlCryptographicValidation cryptographicValidation : aovResult.getCertificateChainCryptographicValidation().getCertificateCryptographicValidation()) {
                if (tokenId.equals(cryptographicValidation.getTokenId())) {
                    return cryptographicValidation;
                }
            }
        }
        return null;
    }

}
