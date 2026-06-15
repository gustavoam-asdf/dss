package eu.europa.esig.dss.eaa.common.key;

import java.security.PublicKey;

/**
 * Factory for creating a provider-independent {@link PublicKeyInfo}
 * representation from a {@link PublicKey}.
 * <p>
 * Implementations are responsible for extracting the relevant public key
 * parameters from provider-specific key implementations and converting them
 * into a generic {@link PublicKeyInfo} model.
 * <p>
 * The resulting {@link PublicKeyInfo} can subsequently be transformed into
 * different representations, such as COSE_Key or JWK.
 */
public interface PublicKeyInfoFactory {

    /**
     * Creates a provider-independent representation of the given public key.
     *
     * @param publicKey {@link PublicKey} to convert
     * @return {@link PublicKeyInfo}
     */
    PublicKeyInfo create(PublicKey publicKey);

}
