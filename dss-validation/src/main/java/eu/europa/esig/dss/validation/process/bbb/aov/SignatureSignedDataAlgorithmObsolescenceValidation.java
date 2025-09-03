package eu.europa.esig.dss.validation.process.bbb.aov;

import eu.europa.esig.dss.detailedreport.jaxb.XmlAOV;
import eu.europa.esig.dss.diagnostic.SignatureWrapper;
import eu.europa.esig.dss.enumerations.Context;
import eu.europa.esig.dss.i18n.I18nProvider;
import eu.europa.esig.dss.model.policy.ValidationPolicy;
import eu.europa.esig.dss.validation.process.ChainItem;

import java.util.Date;

/**
 * Performs cryptographic validation of algorithms used on digest computation for the original signed data objects
 *
 */
public class SignatureSignedDataAlgorithmObsolescenceValidation extends DigestAlgorithmObsolescenceValidation<SignatureWrapper> {

    /**
     * Common constructor
     *
     * @param i18nProvider     the access to translations
     * @param token            instance of {@link SignatureWrapper} to be processed
     * @param validationDate   {@link Date} validation time
     * @param validationPolicy {@link ValidationPolicy} to be used during the validation
     */
    public SignatureSignedDataAlgorithmObsolescenceValidation(I18nProvider i18nProvider, SignatureWrapper token,
                                                              Context context, Date validationDate, ValidationPolicy validationPolicy) {
        super(i18nProvider, token, context, validationDate, validationPolicy);
    }

    @Override
    protected ChainItem<XmlAOV> buildChain() {
        return buildDigestMatchersValidationChain(firstItem, token.getDigestMatchers(), token.getId());
    }

}
