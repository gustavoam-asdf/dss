package eu.europa.esig.dss.ws.eaa.validation.common;

import eu.europa.esig.dss.eaa.common.validation.DefaultEAAPresentationValidator;
import eu.europa.esig.dss.model.DSSDocument;
import eu.europa.esig.dss.model.policy.ValidationPolicy;
import eu.europa.esig.dss.spi.eaa.EAAValidationParameters;
import eu.europa.esig.dss.spi.eaa.status.EAARevocationSource;
import eu.europa.esig.dss.spi.validation.CertificateVerifier;
import eu.europa.esig.dss.validation.eaa.EAAPresentationValidator;
import eu.europa.esig.dss.validation.policy.ValidationPolicyLoader;
import eu.europa.esig.dss.validation.reports.Reports;
import eu.europa.esig.dss.ws.converter.RemoteDocumentConverter;
import eu.europa.esig.dss.ws.dto.RemoteDocument;
import eu.europa.esig.dss.ws.dto.exception.DSSRemoteServiceException;
import eu.europa.esig.dss.ws.eaa.validation.dto.EAAToValidateDTO;
import eu.europa.esig.dss.ws.validation.dto.WSReportsDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.InputStream;

/**
 * Remote service to perform validation of an Electronic Attestation of Attributes
 *
 */
public class RemoteEAAValidationService {

    private static final Logger LOG = LoggerFactory.getLogger(RemoteEAAValidationService.class);

    /** The path for default EAA Presentation policy */
    private static final String EAA_PRESENTATION_POLICY_LOCATION = "/policy/eaa-constraint.xml";

    /** The certificate verifier to use */
    private CertificateVerifier verifier;

    /** The validation policy to be used by default */
    private ValidationPolicy defaultValidationPolicy;

    /** EAA revocation source */
    private EAARevocationSource eaaRevocationSource;

    /**
     * Default construction instantiating object with null certificate verifier
     */
    public RemoteEAAValidationService() {
        // empty
    }

    /**
     * Sets the certificate verifier
     *
     * @param verifier {@link CertificateVerifier}
     */
    public void setVerifier(CertificateVerifier verifier) {
        this.verifier = verifier;
    }

    /**
     * Sets the validation policy to be used by default, when no policy provided within the request
     *
     * @param validationPolicy {@link InputStream}
     */
    public void setDefaultValidationPolicy(InputStream validationPolicy) {
        setDefaultValidationPolicy(validationPolicy, null);
    }

    /**
     * Sets the validation policy with a custom cryptographic suite to be used by default,
     * when no policy provided within the request.
     * If cryptographic suite is set, the constraints from validation policy will be overwritten
     * by the constraints retrieved from the cryptographic suite.
     * When set, the cryptographic suite constraints are applied with the default behavior, using FAIL level.
     * For a customizable cryptographic suite and its applicability context,
     * please use {@code eu.europa.esig.dss.validation.policy.ValidationPolicyLoader}.
     * <p>
     * The format of validation policy should correspond to the DSS XML Validation policy
     * (please include 'dss-policy-jaxb' module in your classpath), unless a custom validation policy has been implemented.
     * The format of cryptographic suite should correspond to XML or JSON schema as defined in ETSI TS 119 322
     * (please include 'dss-policy-crypto-xml' or 'dss-policy-crypto-json' to the classpath), unless a custom
     * cryptographic suite has been implemented.
     * <p>
     * The {@code InputStream} parameters contains the constraint files. If null the default file is used.
     *
     * @param validationPolicy {@link InputStream}
     * @param cryptographicSuite {@link InputStream}
     */
    public void setDefaultValidationPolicy(InputStream validationPolicy, InputStream cryptographicSuite) {
        ValidationPolicyLoader validationPolicyLoader;
        try {
            if (validationPolicy != null) {
                validationPolicyLoader = ValidationPolicyLoader.fromValidationPolicy(validationPolicy);
            } else {
                validationPolicyLoader = ValidationPolicyLoader.fromValidationPolicy(EAA_PRESENTATION_POLICY_LOCATION);
            }
        } catch (Exception e) {
            throw new DSSRemoteServiceException(String.format("Unable to instantiate validation policy: %s", e.getMessage()), e);
        }
        try {
            if (cryptographicSuite != null) {
                validationPolicyLoader = validationPolicyLoader.withCryptographicSuite(cryptographicSuite);
            }
        } catch (Exception e) {
            throw new DSSRemoteServiceException(String.format("Unable to instantiate cryptographic suite: %s", e.getMessage()), e);
        }
        this.defaultValidationPolicy = validationPolicyLoader.create();
    }

    /**
     * Sets the validation policy to be used by default, when no policy provided within the request
     *
     * @param validationPolicy {@link ValidationPolicy}
     */
    public void setDefaultValidationPolicy(ValidationPolicy validationPolicy) {
        this.defaultValidationPolicy = validationPolicy;
    }

    /**
     * Sets a source to request and verify EAA revocation
     *
     * @param eaaRevocationSource {@link EAARevocationSource}
     */
    public void setEAARevocationSource(EAARevocationSource eaaRevocationSource) {
        this.eaaRevocationSource = eaaRevocationSource;
    }

    /**
     * Validates the EAA Presentation document
     *
     * @param dataToValidate {@link EAAToValidateDTO} the request
     * @return {@link WSReportsDTO} response
     */
    public WSReportsDTO validateEAA(EAAToValidateDTO dataToValidate) {
        LOG.info("ValidateEAA in process...");
        EAAPresentationValidator validator = initValidator(dataToValidate);

        Reports reports;
        ValidationPolicyLoader validationPolicyLoader;
        RemoteDocument policy = dataToValidate.getPolicy();
        if (policy != null) {
            validationPolicyLoader = ValidationPolicyLoader.fromValidationPolicy(RemoteDocumentConverter.toDSSDocument(policy));
        } else if (defaultValidationPolicy != null) {
            validationPolicyLoader = ValidationPolicyLoader.fromValidationPolicy(defaultValidationPolicy);
        } else {
            validationPolicyLoader = ValidationPolicyLoader.fromValidationPolicy(EAA_PRESENTATION_POLICY_LOCATION);
        }
        RemoteDocument cryptographicSuite = dataToValidate.getCryptographicSuite();
        if (cryptographicSuite != null) {
            validationPolicyLoader.withCryptographicSuite(RemoteDocumentConverter.toDSSDocument(cryptographicSuite));
        }

        ValidationPolicy validationPolicy = validationPolicyLoader.create();
        reports = validator.validateDocument(validationPolicy);

        WSReportsDTO reportsDTO = new WSReportsDTO(reports.getDiagnosticDataJaxb(), reports.getSimpleReportJaxb(),
                reports.getDetailedReportJaxb(), reports.getEtsiValidationReportJaxb());
        LOG.info("ValidateEAA is finished");
        return reportsDTO;
    }

    /**
     * Instantiates a {@code EAAPresentationValidator} based on the request data DTO
     *
     * @param dataToValidate {@link EAAToValidateDTO} representing the request data
     * @return {@link EAAPresentationValidator}
     */
    protected EAAPresentationValidator initValidator(EAAToValidateDTO dataToValidate) {
        DSSDocument eaaPresentation = RemoteDocumentConverter.toDSSDocument(dataToValidate.getEaaPresentation());
        EAAPresentationValidator validator = DefaultEAAPresentationValidator.fromDocument(eaaPresentation);
        if (eaaRevocationSource != null) {
            validator.setEAARevocationSource(eaaRevocationSource);
        }
        if (dataToValidate.getEaaValidationParameters() != null) {
            EAAValidationParameters eaaValidationParameters =
                    new RemoteEAAValidationParametersBuilder(dataToValidate.getEaaValidationParameters()).build();
            validator.setEAAValidationParameters(eaaValidationParameters);
        }
        if (dataToValidate.getValidationTime() != null) {
            validator.setValidationTime(dataToValidate.getValidationTime());
        }
        validator.setCertificateVerifier(verifier);
        // If null, uses default (NONE)
        if (dataToValidate.getTokenExtractionStrategy() != null) {
            validator.setTokenExtractionStrategy(dataToValidate.getTokenExtractionStrategy());
        }
        return validator;
    }

}
