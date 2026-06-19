package eu.europa.esig.dss.cookbook.example.snippets.ws.rest;

// tag::demo[]
import eu.europa.esig.dss.cookbook.example.CookbookTools;
import eu.europa.esig.dss.model.FileDocument;
import eu.europa.esig.dss.token.DSSPrivateKeyEntry;
import eu.europa.esig.dss.token.SignatureTokenConnection;
import eu.europa.esig.dss.ws.converter.RemoteDocumentConverter;
import eu.europa.esig.dss.ws.dto.RemoteDocument;
import eu.europa.esig.dss.ws.eaa.validation.dto.EAAToValidateDTO;
import eu.europa.esig.dss.ws.eaa.validation.dto.EAAValidationParametersDTO;
import eu.europa.esig.dss.ws.eaa.validation.rest.RestEAAValidationServiceImpl;
import eu.europa.esig.dss.ws.eaa.validation.rest.client.RestEAAValidationService;
import eu.europa.esig.dss.ws.validation.dto.WSReportsDTO;

public class RestEAAValidationServiceSnippet extends CookbookTools {

    @SuppressWarnings("unused")
    public void demo() throws Exception {

        try (SignatureTokenConnection signingToken = getPkcs12Token()) {

            DSSPrivateKeyEntry privateKey = signingToken.getKeys().get(0);

            // Instantiate the REST client
            RestEAAValidationService restClient = new RestEAAValidationServiceImpl();

            // Initialize EAA document to be validated
            FileDocument signatureToValidate = new FileDocument("src/test/resources/mdoc-eaa.cbor");
            RemoteDocument signedDocument = RemoteDocumentConverter.toRemoteDocument(signatureToValidate);

            // Initialize validation parameters, when needed
            EAAValidationParametersDTO validationParameters = new EAAValidationParametersDTO();

            // E.g. provide SessionTranscript, required for mdoc's key binding signature validation
            FileDocument sessionTranscript = new FileDocument("src/test/resources/sessionTranscript.cbor");
            validationParameters.setSessionTranscript(RemoteDocumentConverter.toRemoteDocument(sessionTranscript));

            // Initialize XML validation policy to be used (optional, if not provided the default policy will be used)
            FileDocument policyFile = new FileDocument("src/test/resources/policy.xml");
            RemoteDocument policy = RemoteDocumentConverter.toRemoteDocument(policyFile);

            // Create the object containing data to be validated
            EAAToValidateDTO toValidate = new EAAToValidateDTO(signedDocument, validationParameters, policy);

            // Validate the EAA Presentation
            WSReportsDTO result = restClient.validateEAA(toValidate);
        }

    }

}
// end::demo[]