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
package eu.europa.esig.dss.eaa.mdoc.validation;

import eu.europa.esig.dss.cbades.COSESignStructure;
import eu.europa.esig.dss.cbades.validation.CBAdESSignature;
import eu.europa.esig.dss.eaa.mdoc.IssuerSignedParser;
import eu.europa.esig.dss.eaa.mdoc.model.MdocIssuerSigned;
import eu.europa.esig.dss.eaa.mdoc.model.MdocIssuerSignedItem;
import eu.europa.esig.dss.enumerations.EAAPresentationType;
import eu.europa.esig.dss.model.DSSDocument;
import eu.europa.esig.dss.model.eaa.ValidationDisclosure;
import eu.europa.esig.dss.utils.Utils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * This class is used to parse and process EAA represented by an mdoc IssuerSigned object
 *
 */
public class MdocIssuerSignedEAAPresentationAnalyzer extends AbstractMdocEAAPresentationAnalyzer {

    /** Cached instance of the IssuerSigned */
    private MdocIssuerSigned issuerSigned;

    /**
     * Default constructor
     */
    public MdocIssuerSignedEAAPresentationAnalyzer() {
        // empty
    }

    /**
     * Default constructor
     *
     * @param document {@link DSSDocument} to validate
     */
    public MdocIssuerSignedEAAPresentationAnalyzer(DSSDocument document) {
        super(document);
        this.issuerSigned = buildIssuerSigned();
    }

    /**
     * Constructor with a parsed {@code MdocIssuerSigned}
     *
     * @param document {@link DSSDocument} to validate
     * @param issuerSigned {@link MdocIssuerSigned}
     */
    public MdocIssuerSignedEAAPresentationAnalyzer(DSSDocument document, MdocIssuerSigned issuerSigned) {
        super(document);
        Objects.requireNonNull(document, "MdocIssuerSigned cannot be null!");
        this.issuerSigned = issuerSigned;
    }

    @Override
    public boolean isSupported(DSSDocument document) {
        return new IssuerSignedParser(document).isSupported();
    }

    private MdocIssuerSigned buildIssuerSigned() {
        return new IssuerSignedParser(document).parse();
    }

    @Override
    protected MdocEAAPresentation buildEAAPresentation() {
        MdocEAAPresentation mdocEAAPresentation = new MdocEAAPresentation();
        mdocEAAPresentation.setEAAPresentationType(EAAPresentationType.MDOC_ISSUER_SIGNED);
        MdocEAA mdocEaa = MdocEAA.initBuilder()
                .setSignatures(Collections.singletonList(getSignature()))
                .setDisclosures(getSignedItems())
                .setFilename(document.getName())
                .build();
        mdocEAAPresentation.setElectronicAttestationsOfAttributes(Collections.singletonList(mdocEaa));
        return mdocEAAPresentation;
    }

    /**
     * Gets a list of signatures extracted from an 'issuerSigned'/'issuerAuth' header of a Document object.
     * NOTE: The ISO 18013-5 specifies that a COSE_Sign1 structure shall be used, thus only one signature is expected.
     *
     * @return {@link CBAdESSignature}
     */
    protected CBAdESSignature getSignature() {
        COSESignStructure issuerAuth = issuerSigned.getIssuerAuth();
        return getCoseSignature(issuerAuth);
    }

    /**
     * Returns a list of disclosures extracted for every namespace from a Document structure
     *
     * @return a list of {@link ValidationDisclosure}s
     */
    protected List<ValidationDisclosure> getSignedItems() {
        Map<String, List<MdocIssuerSignedItem>> namespaces = issuerSigned.getNamespaces();
        if (Utils.isMapEmpty(namespaces)) {
            return Collections.emptyList();
        }
        final List<ValidationDisclosure> result = new ArrayList<>();
        for (List<MdocIssuerSignedItem> signedItems : namespaces.values()) {
            result.addAll(signedItems);
        }
        return result;
    }

}
