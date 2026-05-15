package eu.europa.esig.dss.validation.process.eaa.checks;

import eu.europa.esig.dss.detailedreport.jaxb.XmlSAV;
import eu.europa.esig.dss.diagnostic.EAAWrapper;
import eu.europa.esig.dss.enumerations.Indication;
import eu.europa.esig.dss.enumerations.SubIndication;
import eu.europa.esig.dss.i18n.I18nProvider;
import eu.europa.esig.dss.i18n.MessageTag;
import eu.europa.esig.dss.model.policy.MultiValuesRule;
import eu.europa.esig.dss.utils.Utils;
import eu.europa.esig.dss.validation.process.bbb.AbstractMultiValuesCheckItem;

import java.util.List;
import java.util.stream.Collectors;

/**
 * This class verifies whether the EAA contains only supported claims
 *
 */
public class EAASupportedClaimsCheck extends AbstractMultiValuesCheckItem<XmlSAV> {

    /** EAA to check */
    private final EAAWrapper eaa;

    /**
     * Default constructor
     *
     * @param i18nProvider {@link I18nProvider}
     * @param result {@link XmlSAV}
     * @param eaa {@link EAAWrapper}
     * @param constraint {@link MultiValuesRule}
     */
    public EAASupportedClaimsCheck(I18nProvider i18nProvider, XmlSAV result,
                                   EAAWrapper eaa, MultiValuesRule constraint) {
        super(i18nProvider, result, constraint);
        this.eaa = eaa;
    }

    @Override
    protected boolean process() {
        List<String> claimNames = eaa.getAllEAAPayloadClaimNames();
        return processAllValuesCheck(claimNames);
    }

    @Override
    protected String buildAdditionalInfo() {
        List<String> unsupportedClaims = eaa.getAllEAAPayloadClaimNames().stream()
                .filter(c -> !processValueCheck(c))
                .collect(Collectors.toList());
        return i18nProvider.getMessage(MessageTag.EAA_UNSUPPORTED_CLAIMS, Utils.joinStrings(unsupportedClaims, ", "));
    }

    @Override
    protected MessageTag getMessageTag() {
        return MessageTag.EAA_SUPPORTED_CLAIMS;
    }

    @Override
    protected MessageTag getErrorMessageTag() {
        return MessageTag.EAA_SUPPORTED_CLAIMS_ANS;
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