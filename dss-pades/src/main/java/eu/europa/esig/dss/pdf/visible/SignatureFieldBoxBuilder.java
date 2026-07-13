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
package eu.europa.esig.dss.pdf.visible;

import eu.europa.esig.dss.pades.SignatureFieldParameters;

import java.io.IOException;

/**
 * Interface to build a {@code SignatureFieldBox}
 * The interface is used for a SignatureField position validation on a signature/timestamp/empty field creation
 *
 */
public interface SignatureFieldBoxBuilder {

	/**
	 * Builds a {@code SignatureFieldBox}, defining signature field position and dimension
	 *
	 * @return {@link VisualSignatureFieldAppearance}
	 * @throws IOException if an exception occurs
	 */
	VisualSignatureFieldAppearance buildSignatureFieldBox() throws IOException;

	/**
	 * Builds a {@code SignatureFieldBox} for the given {@code fieldParameters}, defining position and dimension
	 * of a specific widget of a multi-widget signature. The visual appearance (image, text, ...) is shared with
	 * the primary field; only position, dimensions, page and rotation of {@code fieldParameters} are used.
	 * <p>
	 * The default implementation ignores {@code fieldParameters} and delegates to {@code #buildSignatureFieldBox()}.
	 *
	 * @param fieldParameters {@link SignatureFieldParameters} defining the widget position and dimensions
	 * @return {@link VisualSignatureFieldAppearance}
	 * @throws IOException if an exception occurs
	 */
	default VisualSignatureFieldAppearance buildSignatureFieldBox(SignatureFieldParameters fieldParameters) throws IOException {
		return buildSignatureFieldBox();
	}

}
