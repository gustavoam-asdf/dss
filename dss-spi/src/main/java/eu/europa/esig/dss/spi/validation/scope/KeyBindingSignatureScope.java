package eu.europa.esig.dss.spi.validation.scope;

import eu.europa.esig.dss.enumerations.SignatureScopeType;
import eu.europa.esig.dss.model.DSSDocument;
import eu.europa.esig.dss.model.identifier.TokenIdentifierProvider;
import eu.europa.esig.dss.model.scope.SignatureScope;
import eu.europa.esig.dss.spi.eaa.EAA;

import java.util.Objects;

/**
 * This signature scope is used to refer a key binding signature's signed content
 *
 */
public class KeyBindingSignatureScope extends SignatureScope {

    private static final long serialVersionUID = -8670355199644516262L;

    /** The counter-signed parent signature */
    protected EAA eaa;

    /**
     * Default constructor
     *
     * @param eaa {@link EAA}
     * @param originalDocument {@link DSSDocument}
     */
    public KeyBindingSignatureScope(final EAA eaa, final DSSDocument originalDocument) {
        super(originalDocument);
        Objects.requireNonNull(eaa, "ElectronicAttestationOfAttributes cannot be null!");
        this.eaa = eaa;
    }

    @Override
    public String getName(TokenIdentifierProvider tokenIdentifierProvider) {
        return getEAAPresentationId(tokenIdentifierProvider);
    }

    @Override
    public String getDescription(TokenIdentifierProvider tokenIdentifierProvider) {
        return String.format("Key binding for EAA with Id : %s", getEAAPresentationId(tokenIdentifierProvider));
    }

    private String getEAAPresentationId(TokenIdentifierProvider tokenIdentifierProvider) {
        return tokenIdentifierProvider.getIdAsString(eaa);
    }

    @Override
    public SignatureScopeType getType() {
        return SignatureScopeType.KEY_BINDING_SIGNATURE;
    }

}
