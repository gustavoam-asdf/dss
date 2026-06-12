package eu.europa.esig.dss.eaa.revocation.validation;

import eu.europa.esig.dss.spi.eaa.EAA;
import eu.europa.esig.dss.spi.eaa.EAARevocationToken;

import java.util.List;

/**
 * This class verifies whether the provided EAA supports the given revocation status verification mechanism,
 * and performs validation on the extracted token document, if applicable.
 *
 */
public interface EAARevocationValidator {

    /**
     * Verifies whether the EAA supports the current status verification mechanism.
     * For example EAA contains the required payload claim.
     *
     * @param eaa {@link EAA} to be verified
     * @return TRUE if the EAA supports given status verification mechanism, FALSE otherwise
     */
    boolean isSupported(EAA eaa);

    /**
     * Gets a list of URIs to be used for extraction of a token containing information about the EAA revocation
     *
     * @param eaa {@link EAA} to be verified
     * @return a list of {@link String}s
     */
    List<String> getUris(EAA eaa);

    /**
     * Validates the {@code eaa} using the {@code statusDocument}
     *
     * @param eaa {@link EAA} to be verified
     * @param revocationDocument binaries of a token containing information about the EAA revocation data
     * @return {@link EAARevocationToken}
     */
    EAARevocationToken validate(EAA eaa, byte[] revocationDocument);

}
