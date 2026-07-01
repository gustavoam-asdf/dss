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
package eu.europa.esig.dss.eaa.mdoc.creation;

import eu.europa.esig.dss.cbades.cbor.CBORArray;
import eu.europa.esig.dss.cbades.cbor.CBORByteString;
import eu.europa.esig.dss.cbades.cbor.CBORMap;
import eu.europa.esig.dss.cbades.cbor.CBORObject;
import eu.europa.esig.dss.cbades.cbor.CBORUtils;
import eu.europa.esig.dss.eaa.mdoc.MdocConstants;
import eu.europa.esig.dss.eaa.mdoc.MdocHeaderParameter;
import eu.europa.esig.dss.model.DSSDocument;
import eu.europa.esig.dss.model.DSSException;
import eu.europa.esig.dss.model.InMemoryDocument;
import eu.europa.esig.dss.spi.exception.IllegalInputException;
import eu.europa.esig.dss.utils.Utils;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Builds an EAA Presentation based on the ISO/IEC 18013-5 mdoc format
 */
public class MdocEAAPresentationBuilder {

    /**
     * Empty constructor
     */
    public MdocEAAPresentationBuilder() {
        //empty
    }

    /**
     * Builds a DSSDocument representing the IssuerSigned structure as defined in "8.3.2.1.2.2 Device retrieval mdoc response"
     *
     * @param eaa {@link DSSDocument} containing the signed EAA
     * @param disclosures a list of {@link MdocEAADisclosure}s to be included
     * @return {@link DSSDocument}
     */
    public DSSDocument buildIssuerSignedDocument(DSSDocument eaa, List<MdocEAADisclosure> disclosures) {
        CBORMap issuerSigned = buildIssuerSigned(eaa, disclosures);
        return new InMemoryDocument(CBORUtils.serializeCborObject(issuerSigned));
    }

    /**
     * Builds a CBORMap representing the IssuerSigned structure as defined in "8.3.2.1.2.2 Device retrieval mdoc response".
     * {@code
     *   IssuerSigned = {
     *     ? "nameSpaces" : IssuerNameSpaces,  ; Returned data elements
     *     "issuerAuth" : IssuerAuth           ; Contains the mobile security object (MSO)
     *                                         ; for issuer data authentication
     *   }
     * }
     *
     * @param eaa {@link DSSDocument} containing the EAA signature (IssuerAuth)
     * @param disclosures a list of {@link MdocEAADisclosure}s to be included
     * @return {@link CBORMap}
     */
    protected CBORMap buildIssuerSigned(DSSDocument eaa, List<MdocEAADisclosure> disclosures) {
        Objects.requireNonNull(eaa, "EAA cannot be null!");
        if (!CBORUtils.isCbor(eaa)) {
            throw new IllegalInputException("EAA document shall represent a CBOR encoded object!");
        }

        try {
            // TODO : do verification in another separate MdocService method ?
            CBORObject issuerAuth = CBORUtils.parseCbor(eaa);

            final CBORMap issuerSigned = new CBORMap();
            if (Utils.isCollectionNotEmpty(disclosures)) {
                issuerSigned.put(MdocConstants.NAMESPACES, buildIssuerNameSpaces(disclosures));
            }
            issuerSigned.put(MdocHeaderParameter.ISSUER_AUTH.toString(), issuerAuth);
            return issuerSigned;

        } catch (Exception e) {
            throw new DSSException(String.format("Unable to build IssuerSigned. Reason : %s", e.getMessage()), e);
        }

    }

    /**
     * Builds a CBORMap representing the IssuerNameSpaces structure as defined in "8.3.2.1.2.2 Device retrieval mdoc response".
     * {@code
     *   IssuerNameSpaces = {                  ; Returned data elements for each namespace
     *     + NameSpace => [ + IssuerSignedItemBytes ]
     *   }
     * }
     *
     * @param disclosures a list of {@link MdocEAADisclosure}s to be included
     * @return {@link CBORMap}
     */
    protected CBORMap buildIssuerNameSpaces(List<MdocEAADisclosure> disclosures) {
        final CBORMap issuerNameSpaces = new CBORMap();
        Map<String, List<CBORByteString>> issuerSignedBytesByNamespace = disclosures.stream().collect(
                Collectors.groupingBy(MdocEAADisclosure::getNamespace, LinkedHashMap::new,
                        Collectors.mapping(MdocEAADisclosure::getIssuerSignedItemBytes, Collectors.toList())));
        issuerSignedBytesByNamespace.forEach((k, v) -> issuerNameSpaces.put(k, new CBORArray(v)));
        return issuerNameSpaces;
    }

}
