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

import eu.europa.esig.dss.cbades.validation.CBAdESDiagnosticDataBuilder;
import eu.europa.esig.dss.eaa.common.validation.DefaultEAAPresentationAnalyzer;
import eu.europa.esig.dss.eaa.common.validation.DefaultEAAPresentationValidator;
import eu.europa.esig.dss.eaa.common.validation.EAAPresentationDiagnosticDataBuilder;
import eu.europa.esig.dss.validation.reports.diagnostic.SignedDocumentDiagnosticDataBuilder;

/**
 * Abstract implementation of an mdoc document validator
 *
 */
public abstract class AbstractMdocEAAPresentationValidator extends DefaultEAAPresentationValidator {

    /**
     * Empty constructor
     *
     * @param eaaPresentationAnalyzer {@link DefaultEAAPresentationAnalyzer}
     */
    protected AbstractMdocEAAPresentationValidator(final DefaultEAAPresentationAnalyzer eaaPresentationAnalyzer) {
        super(eaaPresentationAnalyzer);
    }

    @Override
    protected SignedDocumentDiagnosticDataBuilder getSignatureDiagnosticDataBuilder() {
        return new CBAdESDiagnosticDataBuilder();
    }

    @Override
    public EAAPresentationDiagnosticDataBuilder initializeDiagnosticDataBuilder() {
        return new MdocPresentationDiagnosticDataBuilder()
                .foundEAAPresentation(getEAAPresentation())
                .setSignatureDiagnosticDataBuilder(getSignatureDiagnosticDataBuilder());
    }

}