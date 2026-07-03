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
package eu.europa.esig.dss.model.tsl.identifier;

import eu.europa.esig.dss.model.identifier.MultipleDigestIdentifier;
import eu.europa.esig.dss.model.job.AbstractDocumentInfo;

public class DocumentInfoIdentifier extends MultipleDigestIdentifier {

    private static final long serialVersionUID = -250692069626295484L;

    /**
     * Default constructor
     *
     * @param documentInfo {@link AbstractDocumentInfo} of the target document
     */
    public DocumentInfoIdentifier(AbstractDocumentInfo documentInfo) {
        this("DI-", documentInfo);
    }

    /**
     * Constructor with a custom prefix
     *
     * @param prefix {@link String} identifier prefix (e.g. 'TL-')
     * @param documentInfo {@link AbstractDocumentInfo} of the target document
     */
    protected DocumentInfoIdentifier(final String prefix, AbstractDocumentInfo documentInfo) {
        super(prefix, documentInfo.getUrl().getBytes());
    }

}
