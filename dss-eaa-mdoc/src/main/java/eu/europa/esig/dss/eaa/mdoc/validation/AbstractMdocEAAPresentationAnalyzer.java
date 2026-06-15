package eu.europa.esig.dss.eaa.mdoc.validation;

import eu.europa.esig.dss.cbades.COSESignStructure;
import eu.europa.esig.dss.cbades.validation.CBAdESSignature;
import eu.europa.esig.dss.cbades.validation.CBORSignature;
import eu.europa.esig.dss.eaa.common.validation.DefaultEAAPresentationAnalyzer;
import eu.europa.esig.dss.enumerations.COSESignatureType;
import eu.europa.esig.dss.model.DSSDocument;
import eu.europa.esig.dss.spi.exception.IllegalInputException;
import eu.europa.esig.dss.utils.Utils;

import java.util.List;
import java.util.Objects;

/**
 * Abstract implementation for analyzing an ISO/IEC 18013-5 mdoc document instance
 *
 */
public abstract class AbstractMdocEAAPresentationAnalyzer extends DefaultEAAPresentationAnalyzer {

    /**
     * Default constructor
     */
    protected AbstractMdocEAAPresentationAnalyzer() {
        // empty
    }

    /**
     * Default constructor
     *
     * @param document {@link DSSDocument} to validate
     */
    protected AbstractMdocEAAPresentationAnalyzer(DSSDocument document) {
        Objects.requireNonNull(document, "Document to be validated cannot be null!");
        this.document = document;
    }

    /**
     * Builds a COSE signature instance from a {@code COSESignStructure}
     *
     * @param coseSignStructure {@link COSESignStructure}
     * @return {@link CBAdESSignature}
     */
    protected CBAdESSignature getCoseSignature(COSESignStructure coseSignStructure) {
        if (COSESignatureType.COSE_SIGN1 != coseSignStructure.getContext()) {
            throw new IllegalInputException("The mdoc signature shall be represented by a 'COSE_Sign1' object!");
        }

        List<CBORSignature> cborSignatures = CBORSignature.fromCOSESignStructure(coseSignStructure);
        if (Utils.collectionSize(cborSignatures) != 1) {
            throw new IllegalInputException(String.format("1 signature is expected. Obtained : '%s'", Utils.collectionSize(cborSignatures)));
        }
        CBORSignature cose = cborSignatures.get(0);
        CBAdESSignature cbadesSignature = new CBAdESSignature(cose);
        cbadesSignature.setFilename(document.getName());
        cbadesSignature.setSigningCertificateSource(signingCertificateSource);
        cbadesSignature.setDetachedContents(detachedContents);
        cbadesSignature.initBaselineRequirementsChecker(certificateVerifier);
        validateSignaturePolicy(cbadesSignature);
        return cbadesSignature;
    }

}
