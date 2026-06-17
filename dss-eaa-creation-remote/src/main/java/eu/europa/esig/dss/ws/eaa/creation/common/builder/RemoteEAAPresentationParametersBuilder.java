package eu.europa.esig.dss.ws.eaa.creation.common.builder;

import eu.europa.esig.dss.eaa.mdoc.creation.MdocEAADeviceSignedParameters;
import eu.europa.esig.dss.eaa.mdoc.creation.MdocKeyBindingParameters;
import eu.europa.esig.dss.ws.eaa.creation.common.converter.MdocEAAClaimFromDTOConverter;
import eu.europa.esig.dss.ws.eaa.creation.dto.parameters.RemoteEAAPresentationParameters;
import eu.europa.esig.dss.ws.eaa.creation.dto.parameters.RemoteKeyBindingParameters;

import java.util.Objects;

/**
 * Creates parameters for EAA Presentation issuance
 *
 */
public class RemoteEAAPresentationParametersBuilder {

    /** DTO representing the EAA Presentation parameters */
    private final RemoteEAAPresentationParameters remoteEAAPresentationParameters;

    /**
     * Default constructor
     *
     * @param remoteEAAPresentationParameters {@link RemoteKeyBindingParameters}
     */
    public RemoteEAAPresentationParametersBuilder(final RemoteEAAPresentationParameters remoteEAAPresentationParameters) {
        Objects.requireNonNull(remoteEAAPresentationParameters, "RemoteEAAPresentationParameters must be defined!");
        Objects.requireNonNull(remoteEAAPresentationParameters.getEaaType(), "EAA type must be definedy!");
        this.remoteEAAPresentationParameters = remoteEAAPresentationParameters;
    }

    /**
     * Creates {@code MdocEAADeviceSignedParameters}
     *
     * @return {@link MdocEAADeviceSignedParameters}
     */
    public MdocEAADeviceSignedParameters buildMdocEAADeviceSignedParameters() {
        final MdocKeyBindingParameters mdocKeyBindingParameters = new MdocKeyBindingParameters();
        if (remoteEAAPresentationParameters.getDeviceSignedDataElements() != null &&
                !remoteEAAPresentationParameters.getDeviceSignedDataElements().isEmpty()) {
            final MdocEAAClaimFromDTOConverter converter = new MdocEAAClaimFromDTOConverter();
            remoteEAAPresentationParameters.getDeviceSignedDataElements().forEach(c ->
                    mdocKeyBindingParameters.addDeviceSignedDataElement(converter.apply(c)));
        }
        return mdocKeyBindingParameters;
    }

}
