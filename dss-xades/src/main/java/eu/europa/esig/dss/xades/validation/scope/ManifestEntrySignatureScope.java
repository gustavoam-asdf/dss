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
package eu.europa.esig.dss.xades.validation.scope;

import eu.europa.esig.dss.enumerations.SignatureScopeType;
import eu.europa.esig.dss.model.DSSDocument;
import eu.europa.esig.dss.model.identifier.TokenIdentifierProvider;
import eu.europa.esig.dss.spi.validation.scope.SignatureScopeWithTransformations;
import eu.europa.esig.dss.xml.utils.DomUtils;

import java.util.List;

/**
 * This SignatureScope represents a Manifest entry document
 *
 */
public class ManifestEntrySignatureScope extends SignatureScopeWithTransformations {
	
	private static final long serialVersionUID = 8764813918985821868L;

	/** Manifest filename */
	private final String manifestName;

	/**
	 * Constructor with transformations (Used in XAdES)
	 * @param entryName {@link String} name of the manifest entry
	 * @param document {@link DSSDocument} manifest entry content
	 * @param manifestName {@link String} name of the manifest containing the entry
	 * @param transformations list of {@link String}s transformations
	 */
	public ManifestEntrySignatureScope(final String entryName, final DSSDocument document, final String manifestName,
									   final List<String> transformations) {
		super(entryName, document, transformations);
		this.manifestName = manifestName;
	}

	@Override
	public String getDescription(TokenIdentifierProvider tokenIdentifierProvider) {
		String description;
		if (DomUtils.isElementReference(getDocumentName())) {
			description = String.format("The XML Manifest Entry with ID '%s' from a Manifest with name '%s'", getDocumentName(), manifestName);
		} else {
			description = String.format("The File Manifest Entry with name '%s' from a Manifest with name '%s'", getDocumentName(), manifestName);
		}
		return addTransformationIfNeeded(description);
	}

	@Override
	public SignatureScopeType getType() {
		return SignatureScopeType.FULL;
	}

	@Override
	public String toString() {
		return "ManifestEntrySignatureScope{" +
				"manifestName='" + manifestName + '\'' +
				"} " + super.toString();
	}

}
