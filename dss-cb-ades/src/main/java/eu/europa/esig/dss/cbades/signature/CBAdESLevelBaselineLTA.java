package eu.europa.esig.dss.cbades.signature;

import eu.europa.esig.dss.cbades.CBAdESUtils;
import eu.europa.esig.dss.cbades.COSEConstants;
import eu.europa.esig.dss.cbades.cbor.CBORMap;
import eu.europa.esig.dss.cbades.cbor.CBORObject;
import eu.europa.esig.dss.cbades.validation.CBAdESSignature;
import eu.europa.esig.dss.cbades.validation.CBAdESUHeaders;
import eu.europa.esig.dss.enumerations.DigestAlgorithm;
import eu.europa.esig.dss.model.DSSDocument;
import eu.europa.esig.dss.model.DSSMessageDigest;
import eu.europa.esig.dss.model.DigestDocument;
import eu.europa.esig.dss.model.TimestampBinary;
import eu.europa.esig.dss.signature.SignatureRequirementsChecker;
import eu.europa.esig.dss.spi.exception.IllegalInputException;
import eu.europa.esig.dss.spi.signature.AdvancedSignature;
import eu.europa.esig.dss.spi.validation.CertificateVerifier;
import eu.europa.esig.dss.spi.validation.ValidationData;
import eu.europa.esig.dss.spi.validation.ValidationDataContainer;
import eu.europa.esig.dss.utils.Utils;

import java.util.Collections;
import java.util.List;

/**
 * This class provides a functionality for the CB-AdES-BASELINE-LTA profile augmentation.
 *
 */
public class CBAdESLevelBaselineLTA extends CBAdESLevelBaselineLT {

    /**
     * The default constructor
     *
     * @param certificateVerifier {@link CertificateVerifier} to use
     */
    public CBAdESLevelBaselineLTA(CertificateVerifier certificateVerifier) {
        super(certificateVerifier);
    }

    @Override
    protected void extendSignatures(List<AdvancedSignature> signatures, CBAdESSignatureParameters params) {
        super.extendSignatures(signatures, params);

        final SignatureRequirementsChecker signatureRequirementsChecker = getSignatureRequirementsChecker(params);
        signatureRequirementsChecker.assertSignaturesValid(signatures);

        boolean addTimestampValidationData = false;

        for (AdvancedSignature signature : signatures) {
            CBAdESSignature jadesSignature = (CBAdESSignature) signature;
            assertExtendSignatureToLTAPossible(jadesSignature, params);

            if (jadesSignature.hasLTAProfile()) {
                addTimestampValidationData = true;
            }
        }

        // Perform signature validation
        ValidationDataContainer validationDataContainer = null;
        if (addTimestampValidationData) {
            validationDataContainer = documentAnalyzer.getValidationData(signatures);
        }

        for (AdvancedSignature signature : signatures) {
            CBAdESSignature cbadesSignature = (CBAdESSignature) signature;
            CBAdESUHeaders uHeaders = cbadesSignature.getUHeaders();

            if (cbadesSignature.hasLTAProfile() && addTimestampValidationData) {
                removeLastValidationData(cbadesSignature, uHeaders);

                final ValidationData validationDataForInclusion = validationDataContainer.getCompleteValidationDataForSignature(signature);
                if (!validationDataForInclusion.isEmpty()) {
                    CBORObject valData = getValData(validationDataForInclusion);
                    uHeaders.addComponent(COSEConstants.VAL_DATA, valData, params.isCborBtsrWrappedComponents());
                }
            }

            TimestampBinary timestampBinary = getArchiveTimestamp(cbadesSignature, params);
            CBORMap tstContainer = CBAdESUtils.getTstContainer(Collections.singletonList(timestampBinary),
                    params.getArchiveTimestampParameters().getCanonicalizationMethod());
            uHeaders.addComponent(COSEConstants.ARC_TST, tstContainer, params.isCborBtsrWrappedComponents());
        }
    }

    /**
     * Removes the last 'valData' component, when present
     *
     * @param cbadesSignature {@link CBAdESSignature} being augmented
     * @param uHeaders {@link CBAdESUHeaders}
     */
    private void removeLastValidationData(CBAdESSignature cbadesSignature, CBAdESUHeaders uHeaders) {
        uHeaders.removeLastComponent(COSEConstants.VAL_DATA);
        cbadesSignature.resetCertificateSource();
        cbadesSignature.resetRevocationSources();
    }

    private TimestampBinary getArchiveTimestamp(CBAdESSignature cbadesSignature, CBAdESSignatureParameters params) {
        CBAdESTimestampParameters archiveTimestampParameters = params.getArchiveTimestampParameters();
        DigestAlgorithm digestAlgorithmForTimestampRequest = archiveTimestampParameters.getDigestAlgorithm();
        // TODO : Support canonicalization
        String canonicalizationMethod = archiveTimestampParameters.getCanonicalizationMethod();

        final DSSMessageDigest messageDigest = cbadesSignature.getTimestampSource().getArchiveTimestampData(
                digestAlgorithmForTimestampRequest, canonicalizationMethod);
        return tspSource.getTimeStampResponse(digestAlgorithmForTimestampRequest, messageDigest.getValue());
    }

    /**
     * Checks if the extension is possible.
     */
    private void assertExtendSignatureToLTAPossible(CBAdESSignature cbadesSignature, CBAdESSignatureParameters params) {
        checkArchiveTimestampParameters(params);
        assertDetachedDocumentsContainBinaries(params);
        assertUHeadersComponentsConsistent(cbadesSignature, params.isCborBtsrWrappedComponents());
    }

    private void checkArchiveTimestampParameters(CBAdESSignatureParameters params) {
        CBAdESTimestampParameters archiveTimestampParameters = params.getArchiveTimestampParameters();
        if (!params.isCborBtsrWrappedComponents()
                && Utils.isStringEmpty(archiveTimestampParameters.getCanonicalizationMethod())) {
            throw new IllegalInputException(
                    "Unable to extend CB-AdES-BASELINE-LTA level. Clear 'uHeaders' incorporation requires a canonicalization method!");
        }
    }

    private void assertDetachedDocumentsContainBinaries(CBAdESSignatureParameters params) {
        List<DSSDocument> detachedContents = params.getDetachedContents();
        if (Utils.isCollectionNotEmpty(detachedContents)) {
            for (DSSDocument detachedDocument : detachedContents) {
                if (detachedDocument instanceof DigestDocument) {
                    throw new IllegalArgumentException("CB-AdES-BASELINE-LTA requires complete binaries of signed documents! "
                            + "Extension with a DigestDocument is not possible.");
                }
            }
        }
    }
    
}
