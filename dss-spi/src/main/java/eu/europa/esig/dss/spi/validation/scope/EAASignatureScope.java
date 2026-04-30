package eu.europa.esig.dss.spi.validation.scope;

import eu.europa.esig.dss.enumerations.SignatureScopeType;
import eu.europa.esig.dss.model.DSSDocument;
import eu.europa.esig.dss.model.identifier.TokenIdentifierProvider;
import eu.europa.esig.dss.model.scope.SignatureScope;
import eu.europa.esig.dss.spi.eaa.EAAPresentation;

import java.util.Objects;

/**
 * This signature scope is used to refer a signature used to issue EAA
 *
 */
public class EAASignatureScope extends SignatureScope {

    private static final long serialVersionUID = 2439442860342669997L;

    /** The counter-signed parent signature */
    protected EAAPresentation eaaPresentation;

    /**
     * Default constructor
     *
     * @param eaaPresentation {@link EAAPresentation}
     * @param originalDocument {@link DSSDocument}
     */
    public EAASignatureScope(final EAAPresentation eaaPresentation, final DSSDocument originalDocument) {
        super(originalDocument);
        Objects.requireNonNull(eaaPresentation, "EAA Presentation cannot be null!");
        this.eaaPresentation = eaaPresentation;
    }

    @Override
    public String getName(TokenIdentifierProvider tokenIdentifierProvider) {
        return getEAAPresentationId(tokenIdentifierProvider);
    }

    @Override
    public String getDescription(TokenIdentifierProvider tokenIdentifierProvider) {
        return String.format("EAA Presentation with Id : %s", getEAAPresentationId(tokenIdentifierProvider));
    }

    private String getEAAPresentationId(TokenIdentifierProvider tokenIdentifierProvider) {
        return tokenIdentifierProvider.getIdAsString(eaaPresentation);
    }

    @Override
    public SignatureScopeType getType() {
        return SignatureScopeType.EAA_SIGNATURE;
    }

}
