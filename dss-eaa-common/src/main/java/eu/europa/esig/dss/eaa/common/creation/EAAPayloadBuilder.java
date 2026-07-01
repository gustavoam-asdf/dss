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
package eu.europa.esig.dss.eaa.common.creation;

import eu.europa.esig.dss.model.DSSDocument;

import java.util.List;

/**
 * Builds the EAA payload according to the provided configuration
 *
 * @param <P> implementation of {@link EAAPayloadParameters} for the EAA format
 * @param <D> implementation of {@link EAADisclosure} for the EAA format
 */
public interface EAAPayloadBuilder<P extends EAAPayloadParameters, D extends EAADisclosure> {

    /**
     * Builds the EAA payload to be signed
     *
     * @param payloadParameters {@link EAAPayloadParameters}
     * @return {@link DSSDocument}
     */
    DSSDocument buildPayload(P payloadParameters);

    /**
     * Builds a list of selectively disclosable EAA claims to be used for Digest computation, format specific
     *
     * @param payloadParameters {@link EAAPayloadParameters}
     * @return {@link EAADisclosure} representing the disclosure structure
     */
    List<D> buildDisclosures(P payloadParameters);

}
