package eu.europa.esig.dss.cbades.validation;

import eu.europa.esig.dss.cbades.COSEHeaderParameters;
import eu.europa.esig.dss.cbades.COSEProtectedHeader;
import eu.europa.esig.dss.cbades.cbor.CBORArray;
import eu.europa.esig.dss.cbades.cbor.CBORMap;
import eu.europa.esig.dss.cbades.cbor.CBORObject;
import eu.europa.esig.dss.cbades.cbor.CBORUtils;
import eu.europa.esig.dss.spi.signature.BaselineRequirementsChecker;
import eu.europa.esig.dss.spi.validation.CertificateVerifier;
import eu.europa.esig.dss.utils.Utils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Performs checks according to TS 119 152-1
 * "6.3 Requirements on CB-AdES components and services"
 *
 */
public class CBAdESBaselineRequirementsChecker extends BaselineRequirementsChecker<CBAdESSignature> {

    private static final Logger LOG = LoggerFactory.getLogger(CBAdESBaselineRequirementsChecker.class);

    /**
     * Default constructor
     *
     * @param signature                  {@link CBAdESSignature} to validate
     * @param offlineCertificateVerifier {@link CertificateVerifier} offline copy of a used CertificateVerifier
     */
    public CBAdESBaselineRequirementsChecker(CBAdESSignature signature, CertificateVerifier offlineCertificateVerifier) {
        super(signature, offlineCertificateVerifier);
    }

    @Override
    public boolean hasBaselineBProfile() {
        CBORSignature cose = signature.getCoseSignature();
        CBAdESUHeaders uHeaders = signature.getUHeaders();

        COSEProtectedHeader signatureProtectedHeader = cose.getSignatureProtectedHeader();
        if (signatureProtectedHeader == null) {
            LOG.warn("Signature protected header shall be present for CB-AdES-BASELINE-B signature!");
            return false;
        }

        // alg (Cardinality == 1)
        if (signatureProtectedHeader.getAsLong(COSEHeaderParameters.ALG.cbor()) == null &&
                Utils.isStringEmpty(signatureProtectedHeader.getAsString(COSEHeaderParameters.ALG.cbor()))) {
            LOG.warn("'alg' header shall be present for CB-AdES-BASELINE-B signature (cardinality == 1)!");
            return false;
        }
        // content type (Conditional presence)
        if (signature.isCounterSignature() && Utils.isStringNotEmpty(signatureProtectedHeader.getAsString(COSEHeaderParameters.CONTENT_TYPE.cbor()))) {
            LOG.warn("'content type' header shall not be present for a CB-AdES-BASELINE-B counter signature!");
            return false;
        }
        // verify 'crit' as of RFC 9052 and ETSI TS 119 152-1
        if (!critRequirements(signatureProtectedHeader)) {
            // validation errors returned inside
            return false;
        }
        // iat (Cardinality == 1)
        CBORMap cwtClaims = signatureProtectedHeader.getAsMap(COSEHeaderParameters.CWT_CLAIMS.cbor());
        if (cwtClaims == null || cwtClaims.getAsLong(COSEHeaderParameters.CWT_CLAIMS_IAT.cbor()) == null) {
            LOG.warn("'CWT Claims enclosing the iat' header shall be present for CB-AdES-BASELINE-B signature (cardinality == 1)!");
            return false;
        }
        // x5t / x5ts / x5chain (Cardinality == 1)
        int certHeaders = 0;
        if (signatureProtectedHeader.getAsArray(COSEHeaderParameters.X5T.cbor()) != null) ++certHeaders;
        if (signatureProtectedHeader.getAsArray(COSEHeaderParameters.X5TS.cbor()) != null) ++certHeaders;
        if (signatureProtectedHeader.getAsArray(COSEHeaderParameters.X5CHAIN.cbor()) != null) ++certHeaders;
        if (signatureProtectedHeader.getAsBinaries(COSEHeaderParameters.X5CHAIN.cbor()) != null) ++certHeaders;
        if (certHeaders == 0) {
            LOG.warn("At least one of 'x5t', 'x5ts' or 'x5chain' headers shall be present for CB-AdES-BASELINE-B signature (cardinality == 1)!");
            return false;
        }

        // sigPSt (Cardinality 0 or 1)
        if (uHeaders.getUnsignedPropertiesWithHeaderId(COSEHeaderParameters.SIG_PST.cbor()).size() > 1) {
            LOG.warn("Only one 'sigPSt' header shall be present for CB-AdES-BASELINE-B signature (cardinality 0 or 1)!");
            return false;
        }
        // Additional requirement (b)
        if ((signatureProtectedHeader.getAsMap(COSEHeaderParameters.SIG_PID.cbor()) == null ||
                !isSignaturePolicyIdentifierHashPresent()) && signature.getSignaturePolicyStore() != null) {
            LOG.warn("'sigPSt' header shall not be incorporated " +
                    "for CB-AdES-BASELINE-B signature with not defined 'sigPId/digVal' (requirement (b))!");
            return false;
        }
        return true;
    }

    private boolean critRequirements(COSEProtectedHeader protectedHeader) {
        // NOTE: RFC 9052 requirements are more lax than RFC 7515 for JWS
        List<CBORObject> critList = new ArrayList<>();

        // crit (conditional presence, required only for some elements)
        CBORArray crit = protectedHeader.getAsArray(COSEHeaderParameters.CRIT.cbor());
        if (crit != null) {
            // crit cannot be empty
            critList.addAll(crit.getValueAsList());
            if (crit.isEmpty()) {
                LOG.warn("'crit' header shall not be empty for a CB-AdES-BASELINE-B signature (see RFC 9052)!");
                return false;
            }
        }

        Set<CBORObject> keySet = protectedHeader.getKeys();
        for (CBORObject key : keySet) {
            if (CBORUtils.isRequiredCriticalHeader(key)) {
                if (crit == null) {
                    LOG.warn("'crit' header shall be present when '{}' header is present in a signature for CB-AdES-BASELINE-B signature!", key);
                    return false;
                } else if (!critList.contains(key)) {
                    LOG.warn("'crit' header shall contain '{}' header when present in a signature for CB-AdES-BASELINE-B signature!", key);
                    return false;
                }
            }
        }
        for (CBORObject critEntry : critList) {
            // crit shall not contain not-used entries
            if (!keySet.contains(critEntry)) {
                LOG.warn("'crit' header can contain only entries used within a protected header " +
                        "for CB-AdES-BASELINE-B signature (see RFC 9052)! Found header : '{}'", critEntry);
                return false;
            }
        }
        return true;
    }

    @Override
    public boolean hasBaselineTProfile() {
        if (!minimalTRequirement()) {
            return false;
        }
        CBAdESUHeaders uHeaders = signature.getUHeaders();
        // Additional requirement (c)
        for (CBAdESUHeadersComponent uHeaderComponent : uHeaders.getUnsignedPropertiesWithHeaderId(COSEHeaderParameters.SIG_TST.cbor())) {
            CBORObject sigTst = uHeaderComponent.getValue();
            if (sigTst == null) {
                LOG.warn("'sigTst' shall be a type of CBOR Map for CB-AdES-BASELINE-T signature!");
                return false;
            }
            CBORMap tstContainer = (CBORMap) sigTst;
            CBORArray tstTokens = tstContainer.getAsArray(COSEHeaderParameters.TST_CONTAINER_TST_TOKENS.cbor());
            if (tstTokens.getSize() != 1) {
                LOG.warn("'sigTst' shall contain only one electronic timestamp for CB-AdES-BASELINE-T signature (requirement (c))!");
                return false;
            }
        }
        // Additional requirement (d)
        if (!signatureTimestampsCreatedBeforeSignCertExpiration()) {
            LOG.warn("sigTst shall be created before expiration of the signing-certificate " +
                    "for CB-AdES-BASELINE-T signature (requirement (d))!");
            return false;
        }
        return true;
    }

    @Override
    public boolean hasBaselineLTProfile() {
        if (!minimalLTRequirement()) {
            return false;
        }
        CBAdESUHeaders uHeaders = signature.getUHeaders();
        // refs (Cardinality == 0)
        if (!uHeaders.getUnsignedPropertiesWithHeaderId(COSEHeaderParameters.REFS.cbor()).isEmpty()) {
            LOG.warn("'refs' header shall not be present for CB-AdES-BASELINE-LT signature (cardinality == 0)!");
            return false;
        }
        // sigRTst (Cardinality == 0)
        if (!uHeaders.getUnsignedPropertiesWithHeaderId(COSEHeaderParameters.SIG_R_TST.cbor()).isEmpty()) {
            LOG.warn("'sigRTst' header shall not be present for CB-AdES-BASELINE-LT signature (cardinality == 0)!");
            return false;
        }
        // rfsTst (Cardinality == 0)
        if (!uHeaders.getUnsignedPropertiesWithHeaderId(COSEHeaderParameters.RFS_TST.cbor()).isEmpty()) {
            LOG.warn("'rfsTst' header shall not be present for CB-AdES-BASELINE-LT signature (cardinality == 0)!");
            return false;
        }
        return true;
    }

    @Override
    protected boolean containsLTLevelCertificates() {
        CBAdESUHeaders uHeaders = signature.getUHeaders();
        List<CBAdESUHeadersComponent> valDataEntries = uHeaders.getUnsignedPropertiesWithHeaderId(COSEHeaderParameters.VAL_DATA.cbor());
        for (CBAdESUHeadersComponent uHeadersComponent : valDataEntries) {
            CBORObject valData = uHeadersComponent.getValue();
            if (valData.isMap() && ((CBORMap) valData).getAsArray(COSEHeaderParameters.VAL_DATA_X_VALS.cbor()) != null) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean hasBaselineLTAProfile() {
        return minimalLTARequirement();
    }

}
