package eu.europa.esig.dss.eaa.common.validation.identifier;

import eu.europa.esig.dss.eaa.common.validation.DefaultEAA;
import eu.europa.esig.dss.model.DSSException;
import eu.europa.esig.dss.model.eaa.Disclosure;
import eu.europa.esig.dss.spi.eaa.EAA;
import eu.europa.esig.dss.spi.signature.AdvancedSignature;
import eu.europa.esig.dss.utils.Utils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * This class is used to build an identifier for EAA object
 */
public class EAAIdentifierBuilder {

    /**
     * Default constructor
     */
    public EAAIdentifierBuilder() {
        // empty
    }

    /**
     * Builds an {@code EAAIdentifier} for the given {@code eaa}
     *
     * @param eaa {@link EAAIdentifier} to build identifier for
     * @return {@link EAAIdentifier}
     */
    public EAAIdentifier build(DefaultEAA eaa) {
        return new EAAIdentifier(buildBinaries(eaa));
    }

    /**
     * Builds unique binary data describing the object
     *
     * @param eaa {@link EAA} to build binaries for identifier on
     * @return a byte array
     */
    protected byte[] buildBinaries(DefaultEAA eaa) {
        try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
            for (AdvancedSignature signature : eaa.getSignatures()) {
                baos.write(signature.getId().getBytes());
            }
            if (Utils.isCollectionNotEmpty(eaa.getDisclosures())) {
                for (Disclosure disclosure : eaa.getDisclosures()) {
                    baos.write(disclosure.getSalt());
                    if (disclosure.getName() != null) {
                        baos.write(disclosure.getName().getBytes());
                    }
                    // claim value is not used to avoid unnecessary information disclosure
                }
            }
            if (eaa.getKeyBindingSignature() != null) {
                baos.write(eaa.getKeyBindingSignature().getId().getBytes());
            }
            return baos.toByteArray();

        } catch (IOException e) {
            throw new DSSException(String.format("An error occurred while building an Identifier : %s", e.getMessage()), e);
        }
    }

}
