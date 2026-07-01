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
package eu.europa.esig.dss.spi.validation.scope;

import eu.europa.esig.dss.enumerations.SignatureScopeType;
import eu.europa.esig.dss.model.DSSDocument;
import eu.europa.esig.dss.model.identifier.TokenIdentifierProvider;
import eu.europa.esig.dss.model.scope.SignatureScope;
import eu.europa.esig.dss.spi.eaa.EAA;

import java.util.Objects;

/**
 * This signature scope is used to refer a signature used to issue EAA
 *
 */
public class EAASignatureScope extends SignatureScope {

    private static final long serialVersionUID = 2439442860342669997L;

    /** The counter-signed parent signature */
    protected EAA eaa;

    /**
     * Default constructor
     *
     * @param eaa {@link EAA}
     * @param originalDocument {@link DSSDocument}
     */
    public EAASignatureScope(final EAA eaa, final DSSDocument originalDocument) {
        super(originalDocument);
        Objects.requireNonNull(eaa, "EAA cannot be null!");
        this.eaa = eaa;
    }

    @Override
    public String getName(TokenIdentifierProvider tokenIdentifierProvider) {
        return getEAAPresentationId(tokenIdentifierProvider);
    }

    @Override
    public String getDescription(TokenIdentifierProvider tokenIdentifierProvider) {
        return String.format("EAA with Id : %s", getEAAPresentationId(tokenIdentifierProvider));
    }

    private String getEAAPresentationId(TokenIdentifierProvider tokenIdentifierProvider) {
        return tokenIdentifierProvider.getIdAsString(eaa);
    }

    @Override
    public SignatureScopeType getType() {
        return SignatureScopeType.EAA_SIGNATURE;
    }

}
