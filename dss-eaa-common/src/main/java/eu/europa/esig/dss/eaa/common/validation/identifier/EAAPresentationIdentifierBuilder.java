package eu.europa.esig.dss.eaa.common.validation.identifier;

import eu.europa.esig.dss.eaa.common.validation.DefaultEAAPresentation;
import eu.europa.esig.dss.model.DSSException;
import eu.europa.esig.dss.model.eaa.Disclosure;
import eu.europa.esig.dss.spi.eaa.EAAPresentation;
import eu.europa.esig.dss.spi.signature.AdvancedSignature;
import eu.europa.esig.dss.utils.Utils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * This class is used to build an identifier for EAA Presentation object
 */
public class EAAPresentationIdentifierBuilder {

    /**
     * Default constructor
     */
    public EAAPresentationIdentifierBuilder() {
        // empty
    }

    /**
     * Builds an {@code EAAPresentationIdentifier} for the given {@code eaaPresentation}
     *
     * @param eaaPresentation {@link EAAPresentationIdentifier} to build identifier for
     * @return {@link EAAPresentationIdentifier}
     */
    public EAAPresentationIdentifier build(DefaultEAAPresentation eaaPresentation) {
        return new EAAPresentationIdentifier(buildBinaries(eaaPresentation));
    }

    /**
     * Builds unique binary data describing the object
     *
     * @param eaaPresentation {@link EAAPresentation} to build binaries for identifier on
     * @return a byte array
     */
    protected byte[] buildBinaries(DefaultEAAPresentation eaaPresentation) {
        try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
            for (AdvancedSignature signature : eaaPresentation.getSignatures()) {
                baos.write(signature.getId().getBytes());
            }
            if (Utils.isCollectionNotEmpty(eaaPresentation.getDisclosures())) {
                for (Disclosure disclosure : eaaPresentation.getDisclosures()) {
                    baos.write(disclosure.getSalt());
                    if (disclosure.getClaimName() != null) {
                        baos.write(disclosure.getClaimName().getBytes());
                    }
                    // claim value is not used to avoid unnecessary information disclosure
                }
            }
            if (eaaPresentation.getKeyBindingSignature() != null) {
                baos.write(eaaPresentation.getKeyBindingSignature().getId().getBytes());
            }
            return baos.toByteArray();

        } catch (IOException e) {
            throw new DSSException(String.format("An error occurred while building an Identifier : %s", e.getMessage()), e);
        }
    }

}
