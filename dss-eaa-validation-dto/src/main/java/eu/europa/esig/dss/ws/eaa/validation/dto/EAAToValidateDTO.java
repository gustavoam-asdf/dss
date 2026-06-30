package eu.europa.esig.dss.ws.eaa.validation.dto;

import eu.europa.esig.dss.enumerations.TokenExtractionStrategy;
import eu.europa.esig.dss.ws.dto.RemoteDocument;

import java.util.Date;

/**
 * Represents a request DTO for validation of an EAA Presentation
 * 
 */
public class EAAToValidateDTO {

    /**
     * The document that contains an EAA Presentation to be validated
     */
    private RemoteDocument eaaPresentation;

    /**
     * Supplementary data for EAA Presentation validation
     */
    private EAAValidationParametersDTO eaaValidationParameters;
    
    /**
     * The custom validation policy to use
     * <p>
     * OPTIONAL.
     */
    private RemoteDocument policy;

    /**
     * The custom cryptographic suite to use
     * <p>
     * OPTIONAL.
     */
    private RemoteDocument cryptographicSuite;

    /**
     * Allows to specify a validation time different from the current time.
     * <p>
     * OPTIONAL.
     */
    private Date validationTime;

    /**
     * The strategy for the token (certificate/timestamp/revocation data) extraction
     * <p>
     * OPTIONAL.
     */
    private TokenExtractionStrategy tokenExtractionStrategy = TokenExtractionStrategy.NONE;

    /**
     * Empty constructor
     */
    public EAAToValidateDTO() {
        // empty
    }

    /**
     * Constructor to validate an EAA Presentation
     *
     * @param eaaPresentation {@link RemoteDocument} to validate
     */
    public EAAToValidateDTO(RemoteDocument eaaPresentation) {
        this(eaaPresentation, (EAAValidationParametersDTO) null);
    }

    /**
     * Constructor to validate an EAA Presentation with supplementary validation parameters
     *
     * @param eaaPresentation {@link RemoteDocument} to validate
     * @param eaaValidationParameters {@link EAAValidationParametersDTO}
     */
    public EAAToValidateDTO(RemoteDocument eaaPresentation, EAAValidationParametersDTO eaaValidationParameters) {
        this(eaaPresentation, eaaValidationParameters, null);
    }

    /**
     * Constructor to validate a document with a validation policy provided
     *
     * @param eaaPresentation {@link RemoteDocument} to validate
     * @param policy {@link RemoteDocument} validation policy
     */
    public EAAToValidateDTO(RemoteDocument eaaPresentation, RemoteDocument policy) {
        this(eaaPresentation, policy, null);
    }

    /**
     * Constructor to validate a document with supplementary validation parameters and with a validation policy provided
     *
     * @param eaaPresentation {@link RemoteDocument} to validate
     * @param eaaValidationParameters {@link EAAValidationParametersDTO}
     * @param policy {@link RemoteDocument} validation policy
     */
    public EAAToValidateDTO(RemoteDocument eaaPresentation, EAAValidationParametersDTO eaaValidationParameters, RemoteDocument policy) {
        this(eaaPresentation, eaaValidationParameters, policy, null);
    }

    /**
     * Constructor to validate a document with a validation policy and cryptographic suite provided
     *
     * @param eaaPresentation {@link RemoteDocument} to validate
     * @param policy {@link RemoteDocument} validation policy
     * @param cryptographicSuite {@link RemoteDocument} cryptographic suite
     */
    public EAAToValidateDTO(RemoteDocument eaaPresentation, RemoteDocument policy, RemoteDocument cryptographicSuite) {
        this(eaaPresentation, null, null, policy, cryptographicSuite);
    }

    /**
     * Constructor to validate a document with supplementary validation parameters,
     * a validation policy and cryptographic suite provided
     *
     * @param eaaPresentation {@link RemoteDocument} to validate
     * @param eaaValidationParameters {@link EAAValidationParametersDTO}
     * @param policy {@link RemoteDocument} validation policy
     * @param cryptographicSuite {@link RemoteDocument} cryptographic suite
     */
    public EAAToValidateDTO(RemoteDocument eaaPresentation, EAAValidationParametersDTO eaaValidationParameters,
                            RemoteDocument policy, RemoteDocument cryptographicSuite) {
        this(eaaPresentation, eaaValidationParameters, null, policy, cryptographicSuite);
    }

    /**
     * Constructor to validate a document with validation time and with a validation policy provided
     *
     * @param eaaPresentation {@link RemoteDocument} to validate
     * @param validationTime {@link Date}
     * @param policy {@link RemoteDocument} validation policy
     */
    public EAAToValidateDTO(RemoteDocument eaaPresentation, Date validationTime, RemoteDocument policy) {
        this(eaaPresentation, validationTime, policy, null);
    }

    /**
     * Constructor to validate a document with supplementary validation parameters,
     * validation time and with a validation policy provided
     *
     * @param eaaPresentation {@link RemoteDocument} to validate
     * @param eaaValidationParameters {@link EAAValidationParametersDTO}
     * @param validationTime {@link Date}
     * @param policy {@link RemoteDocument} validation policy
     */
    public EAAToValidateDTO(RemoteDocument eaaPresentation, EAAValidationParametersDTO eaaValidationParameters,
                            Date validationTime, RemoteDocument policy) {
        this(eaaPresentation, eaaValidationParameters, validationTime, policy, null);
    }

    /**
     * Constructor to validate a document with validation time and 
     * with a validation policy and cryptographic suite provided
     *
     * @param eaaPresentation {@link RemoteDocument} to validate
     * @param validationTime {@link Date}
     * @param policy {@link RemoteDocument} validation policy
     * @param cryptographicSuite {@link RemoteDocument} cryptographic suite
     */
    public EAAToValidateDTO(RemoteDocument eaaPresentation, Date validationTime, RemoteDocument policy,
                            RemoteDocument cryptographicSuite) {
        this(eaaPresentation, null, validationTime, policy, cryptographicSuite);
    }

    /**
     * Constructor to validate a document with supplementary validation parameters, validation time and
     * with a validation policy and cryptographic suite provided
     *
     * @param eaaPresentation {@link RemoteDocument} to validate
     * @param eaaValidationParameters {@link EAAValidationParametersDTO}
     * @param validationTime {@link Date}
     * @param policy {@link RemoteDocument} validation policy
     * @param cryptographicSuite {@link RemoteDocument} cryptographic suite
     */
    public EAAToValidateDTO(RemoteDocument eaaPresentation, EAAValidationParametersDTO eaaValidationParameters,
                            Date validationTime, RemoteDocument policy, RemoteDocument cryptographicSuite) {
        this.eaaPresentation = eaaPresentation;
        this.eaaValidationParameters = eaaValidationParameters;
        this.validationTime = validationTime;
        this.policy = policy;
        this.cryptographicSuite = cryptographicSuite;
    }

    /**
     * Gets the EAA Presentation document
     * 
     * @return {@link RemoteDocument}
     */
    public RemoteDocument getEaaPresentation() {
        return eaaPresentation;
    }

    /**
     * Sets the EAA Presentation document to be validated
     * 
     * @param eaaPresentation {@link RemoteDocument}
     */
    public void setEaaPresentation(RemoteDocument eaaPresentation) {
        this.eaaPresentation = eaaPresentation;
    }

    /**
     * Gets supplementary input data parameters for EAA Presentation's validation
     *
     * @return {@link EAAValidationParametersDTO}
     */
    public EAAValidationParametersDTO getEaaValidationParameters() {
        return eaaValidationParameters;
    }

    /**
     * Sets supplementary input data for performing an EAA Presentation's validation, if required
     *
     * @param eaaValidationParameters {@link EAAValidationParametersDTO}
     */
    public void setEaaValidationParameters(EAAValidationParametersDTO eaaValidationParameters) {
        this.eaaValidationParameters = eaaValidationParameters;
    }

    /**
     * Gets the validation policy
     *
     * @return {@link RemoteDocument}
     */
    public RemoteDocument getPolicy() {
        return policy;
    }

    /**
     * Sets the validation policy
     *
     * @param policy {@link RemoteDocument}
     */
    public void setPolicy(RemoteDocument policy) {
        this.policy = policy;
    }

    /**
     * Gets a cryptographic suite document (to be applied globally)
     *
     * @return {@link RemoteDocument}
     */
    public RemoteDocument getCryptographicSuite() {
        return cryptographicSuite;
    }

    /**
     * Sets a cryptographic suite document (to be applied globally)
     *
     * @param cryptographicSuite {@link RemoteDocument}
     */
    public void setCryptographicSuite(RemoteDocument cryptographicSuite) {
        this.cryptographicSuite = cryptographicSuite;
    }

    /**
     * Gets the validation time
     *
     * @return {@link Date}
     */
    public Date getValidationTime() {
        return validationTime;
    }

    /**
     * Sets the validation time
     * NOTE: if not defined, the current time is used
     *
     * @param validationTime {@link Date}
     */
    public void setValidationTime(Date validationTime) {
        this.validationTime = validationTime;
    }
    
    /**
     * Gets a token extraction strategy
     *
     * @return {@link TokenExtractionStrategy}
     */
    public TokenExtractionStrategy getTokenExtractionStrategy() {
        return tokenExtractionStrategy;
    }

    /**
     * Sets a token extraction strategy
     *
     * @param tokenExtractionStrategy {@link TokenExtractionStrategy}
     */
    public void setTokenExtractionStrategy(TokenExtractionStrategy tokenExtractionStrategy) {
        this.tokenExtractionStrategy = tokenExtractionStrategy;
    }
    
}
