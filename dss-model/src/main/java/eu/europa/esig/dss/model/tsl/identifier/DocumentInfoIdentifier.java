package eu.europa.esig.dss.model.tsl.identifier;

import eu.europa.esig.dss.model.identifier.MultipleDigestIdentifier;
import eu.europa.esig.dss.model.job.AbstractDocumentInfo;

public class DocumentInfoIdentifier extends MultipleDigestIdentifier {

    private static final long serialVersionUID = -250692069626295484L;

    /**
     * Default constructor
     *
     * @param documentInfo {@link AbstractDocumentInfo} of the target document
     */
    public DocumentInfoIdentifier(AbstractDocumentInfo documentInfo) {
        this("DI-", documentInfo);
    }

    /**
     * Constructor with a custom prefix
     *
     * @param prefix {@link String} identifier prefix (e.g. 'TL-')
     * @param documentInfo {@link AbstractDocumentInfo} of the target document
     */
    protected DocumentInfoIdentifier(final String prefix, AbstractDocumentInfo documentInfo) {
        super(prefix, documentInfo.getUrl().getBytes());
    }

}
