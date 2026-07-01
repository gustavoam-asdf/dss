/**
 * DSS - Digital Signature Services
 * Copyright (C) 2015 European Commission, provided under the CEF programme
 * <p>
 * This file is part of the "DSS - Digital Signature Services" project.
 * <p>
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 * <p>
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 * <p>
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301  USA
 */
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
