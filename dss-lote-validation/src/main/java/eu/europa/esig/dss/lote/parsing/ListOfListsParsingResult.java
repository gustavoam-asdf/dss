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
package eu.europa.esig.dss.lote.parsing;

import eu.europa.esig.dss.model.lote.OtherListPointer;

import java.util.List;

/**
 * Parsed List Of Lists result
 *
 */
public class ListOfListsParsingResult extends ParsingResult {

	/** List of Lists of Lists pointers */
	private List<OtherListPointer> listOfListsPointers;

	/** List of List pointers */
	private List<OtherListPointer> listPointers;

	/** Signing certificate announcement URL */
	private String signingCertificateAnnouncementURL;

	/** List of pivot URLs */
	private List<String> pivotURLs;

	/**
	 * Default constructor
	 */
	public ListOfListsParsingResult() {
		super();
	}

	/**
	 * Gets List of Lists of Lists other pointers
	 *
	 * @return a list of {@link OtherListPointer}s
	 */
	public List<OtherListPointer> getListOfListsPointers() {
		return listOfListsPointers;
	}

	/**
	 * Sets List of List other pointers
	 *
	 * @param listOfListsPointers a list of {@link OtherListPointer}s
	 */
	public void setListOfListsPointers(List<OtherListPointer> listOfListsPointers) {
		this.listOfListsPointers = listOfListsPointers;
	}

	/**
	 * Gets List to other TSL pointers
	 *
	 * @return a list of {@link OtherListPointer}s
	 */
	public List<OtherListPointer> getListPointers() {
		return listPointers;
	}

	/**
	 * Sets List to other pointers
	 *
	 * @param listPointers a list of {@link OtherListPointer}s
	 */
	public void setListPointers(List<OtherListPointer> listPointers) {
		this.listPointers = listPointers;
	}

	/**
	 * Gets signing certificate announcement URL
	 *
	 * @return {@link String}
	 */
	public String getSigningCertificateAnnouncementURL() {
		return signingCertificateAnnouncementURL;
	}

	/**
	 * Sets the signing certificate announcement URL
	 *
	 * @param signingCertificateAnnouncementURL {@link String}
	 */
	public void setSigningCertificateAnnouncementURL(String signingCertificateAnnouncementURL) {
		this.signingCertificateAnnouncementURL = signingCertificateAnnouncementURL;
	}

	/**
	 * Gets pivot URLs
	 *
	 * @return a list of {@link String}s
	 */
	public List<String> getPivotURLs() {
		return pivotURLs;
	}

	/**
	 * Sets pivot URLs
	 *
	 * @param pivotURLs a list of {@link String}s
	 */
	public void setPivotURLs(List<String> pivotURLs) {
		this.pivotURLs = pivotURLs;
	}

}
