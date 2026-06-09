package eu.europa.esig.dss.spi.eaa;

import java.util.List;

/**
 * This class verifies whether the provided EAA supports the given status verification mechanism,
 * and performs validation on the extracted token document, if applicable.
 *
 */
public interface EAAStatusValidator {

    /**
     * Verifies whether the EAA supports the current status verification mechanism.
     * For example EAA contains the required payload claim.
     *
     * @param eaa {@link EAA} to be verified
     * @return TRUE if the EAA supports given status verification mechanism, FALSE otherwise
     */
    boolean isSupported(EAA eaa);

    /**
     * Gets a list of URIs to be used for extraction of a token containing information about the EAA status
     *
     * @param eaa {@link EAA} to be verified
     * @return a list of {@link String}s
     */
    List<String> getStatusUris(EAA eaa);

    /**
     * Validates the {@code eaa} using the {@code statusDocument}
     *
     * @param eaa {@link EAA} to be verified
     * @param statusDocument binaries of a token containing information about the EAA status
     * @return {@link EAAStatusToken}
     */
    EAAStatusToken validate(EAA eaa, byte[] statusDocument);

}
