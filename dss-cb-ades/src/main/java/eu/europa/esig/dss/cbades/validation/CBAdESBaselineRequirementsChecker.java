package eu.europa.esig.dss.cbades.validation;

import eu.europa.esig.dss.cbades.COSEConstants;
import eu.europa.esig.dss.cbades.COSEProtectedHeader;
import eu.europa.esig.dss.cbades.cbor.CBORArray;
import eu.europa.esig.dss.cbades.cbor.CBORMap;
import eu.europa.esig.dss.cbades.cbor.CBORObject;
import eu.europa.esig.dss.spi.signature.BaselineRequirementsChecker;
import eu.europa.esig.dss.spi.validation.CertificateVerifier;
import eu.europa.esig.dss.utils.Utils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

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
        if (signatureProtectedHeader.getAsLong(COSEConstants.ALG) == null &&
                Utils.isStringEmpty(signatureProtectedHeader.getAsString(COSEConstants.ALG))) {
            LOG.warn("'alg' header shall be present for CB-AdES-BASELINE-B signature (cardinality == 1)!");
            return false;
        }
        // content type (Conditional presence)
        if (signature.isCounterSignature() && Utils.isStringNotEmpty(signatureProtectedHeader.getAsString(COSEConstants.CONTENT_TYPE))) {
            LOG.warn("'content type' header shall not be present for a CB-AdES-BASELINE-B counter signature!");
            return false;
        }
        // TODO : 'crit' support ?
        // iat (Cardinality == 1)
        CBORMap cwtClaims = signatureProtectedHeader.getAsMap(COSEConstants.CWT_CLAIMS);
        if (cwtClaims == null || cwtClaims.getAsLong(COSEConstants.CWT_CLAIMS_IAT) == null) {
            LOG.warn("'CWT Claims enclosing the iat' header shall be present for CB-AdES-BASELINE-B signature (cardinality == 1)!");
            return false;
        }
        // TODO : 'x5t' is not defined in the standard
        // x5chain / x5ts (Cardinality == 1)
        int certHeaders = 0;
        if (signatureProtectedHeader.getAsBinaries(COSEConstants.X5CHAIN) != null) ++certHeaders;
        if (signatureProtectedHeader.getAsArray(COSEConstants.X5CHAIN) != null) ++certHeaders;
        if (signatureProtectedHeader.getAsArray(COSEConstants.X5TS) != null) ++certHeaders;
        if (certHeaders == 0) {
            LOG.warn("At least one of x5t#x5chain, x5ts headers shall be present for CB-AdES-BASELINE-B signature (cardinality == 1)!");
            return false;
        }

        // sigPSt (Cardinality 0 or 1)
        if (uHeaders.getUnsignedPropertiesWithHeaderId(COSEConstants.SIG_PST).size() > 1) {
            LOG.warn("Only one 'sigPSt' header shall be present for CB-AdES-BASELINE-B signature (cardinality 0 or 1)!");
            return false;
        }
        // Additional requirement (b)
        if ((signatureProtectedHeader.getAsMap(COSEConstants.SIG_PID) == null ||
                !isSignaturePolicyIdentifierHashPresent()) && signature.getSignaturePolicyStore() != null) {
            LOG.warn("'sigPSt' header shall not be incorporated " +
                    "for CB-AdES-BASELINE-B signature with not defined 'sigPId/digVal' (requirement (b))!");
            return false;
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
        for (CBAdESUHeadersComponent uHeaderComponent : uHeaders.getUnsignedPropertiesWithHeaderId(COSEConstants.SIG_TST)) {
            CBORObject sigTst = uHeaderComponent.getValue();
            if (sigTst == null) {
                LOG.warn("'sigTst' shall be a type of CBOR Map for CB-AdES-BASELINE-T signature!");
                return false;
            }
            CBORMap tstContainer = (CBORMap) sigTst;
            CBORArray tstTokens = tstContainer.getAsArray(COSEConstants.TST_CONTAINER_TST_TOKENS);
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
        if (!uHeaders.getUnsignedPropertiesWithHeaderId(COSEConstants.REFS).isEmpty()) {
            LOG.warn("'refs' header shall not be present for CB-AdES-BASELINE-LT signature (cardinality == 0)!");
            return false;
        }
        // sigRTst (Cardinality == 0)
        if (!uHeaders.getUnsignedPropertiesWithHeaderId(COSEConstants.SIG_R_TST).isEmpty()) {
            LOG.warn("'sigRTst' header shall not be present for CB-AdES-BASELINE-LT signature (cardinality == 0)!");
            return false;
        }
        // rfsTst (Cardinality == 0)
        if (!uHeaders.getUnsignedPropertiesWithHeaderId(COSEConstants.RFS_TST).isEmpty()) {
            LOG.warn("'rfsTst' header shall not be present for CB-AdES-BASELINE-LT signature (cardinality == 0)!");
            return false;
        }
        return true;
    }

    @Override
    protected boolean containsLTLevelCertificates() {
        CBAdESUHeaders uHeaders = signature.getUHeaders();
        List<CBAdESUHeadersComponent> valDataEntries = uHeaders.getUnsignedPropertiesWithHeaderId(COSEConstants.VAL_DATA);
        for (CBAdESUHeadersComponent uHeadersComponent : valDataEntries) {
            CBORObject valData = uHeadersComponent.getValue();
            if (valData.isMap() && ((CBORMap) valData).getAsArray(COSEConstants.VAL_DATA_X_VALS) != null) {
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
