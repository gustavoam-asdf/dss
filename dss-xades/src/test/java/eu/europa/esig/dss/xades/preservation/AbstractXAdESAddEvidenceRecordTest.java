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
package eu.europa.esig.dss.xades.preservation;

import eu.europa.esig.dss.diagnostic.DiagnosticData;
import eu.europa.esig.dss.diagnostic.EvidenceRecordWrapper;
import eu.europa.esig.dss.enumerations.EvidenceRecordTypeEnum;
import eu.europa.esig.dss.model.DSSDocument;
import eu.europa.esig.dss.xades.evidencerecord.XAdESEvidenceRecordIncorporationParameters;
import eu.europa.esig.dss.xades.signature.XAdESService;
import eu.europa.esig.dss.xades.validation.evidencerecord.AbstractXAdESWithEvidenceRecordTestValidation;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public abstract class AbstractXAdESAddEvidenceRecordTest extends AbstractXAdESWithEvidenceRecordTestValidation {

    protected abstract DSSDocument getSignatureDocument();

    protected abstract DSSDocument getEvidenceRecordDocument();

    protected XAdESEvidenceRecordIncorporationParameters getEvidenceRecordIncorporationParameters() {
        XAdESEvidenceRecordIncorporationParameters parameters = new XAdESEvidenceRecordIncorporationParameters();
        parameters.setDetachedContents(getDetachedContents());
        return parameters;
    }

    protected XAdESService getService() {
        return new XAdESService(getOfflineCertificateVerifier());
    }

    @Override
    protected DSSDocument getSignedDocument() {
        XAdESService service = getService();
        return service.addSignatureEvidenceRecord(getSignatureDocument(), getEvidenceRecordDocument(), getEvidenceRecordIncorporationParameters());
    }

    @Override
    public void validate() {
        // skip
    }

    @Test
    public void addERAndValidate() {
        super.validate();
    }

    @Override
    protected void checkEvidenceRecordTimestampedReferences(DiagnosticData diagnosticData) {
        // skip (out of scope)
    }

    @Override
    protected int getNumberOfExpectedEvidenceScopes() {
        return 1 + getNumberOfCoveredDocuments(); // signature + document / detached docs
    }

    protected int getNumberOfCoveredDocuments() {
        return 1;
    }

    @Override
    protected void checkEvidenceRecordType(EvidenceRecordWrapper evidenceRecord) {
        super.checkEvidenceRecordType(evidenceRecord);

        assertEquals(getEvidenceRecordType(), evidenceRecord.getEvidenceRecordType());
    }

    protected abstract EvidenceRecordTypeEnum getEvidenceRecordType();

}
