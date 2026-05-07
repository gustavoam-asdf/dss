package eu.europa.esig.dss.spi.validation.scope;

import eu.europa.esig.dss.enumerations.SignatureScopeType;
import eu.europa.esig.dss.model.DSSDocument;
import eu.europa.esig.dss.model.identifier.TokenIdentifierProvider;
import eu.europa.esig.dss.model.scope.SignatureScope;
import eu.europa.esig.dss.spi.eaa.EAA;

import java.util.Objects;

/**
 * This signature scope is used to refer a signature used to issue EAA
 *
 */
public class EAASignatureScope extends SignatureScope {

    private static final long serialVersionUID = 2439442860342669997L;

    /** The counter-signed parent signature */
    protected EAA eaa;

    /**
     * Default constructor
     *
     * @param eaa {@link EAA}
     * @param originalDocument {@link DSSDocument}
     */
    public EAASignatureScope(final EAA eaa, final DSSDocument originalDocument) {
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
        return String.format("EAA with Id : %s", getEAAPresentationId(tokenIdentifierProvider));
    }

    private String getEAAPresentationId(TokenIdentifierProvider tokenIdentifierProvider) {
        return tokenIdentifierProvider.getIdAsString(eaa);
    }

    @Override
    public SignatureScopeType getType() {
        return SignatureScopeType.EAA_SIGNATURE;
    }

}
