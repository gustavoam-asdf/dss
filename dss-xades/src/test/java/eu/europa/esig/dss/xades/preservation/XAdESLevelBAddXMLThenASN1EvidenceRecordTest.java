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
import eu.europa.esig.dss.diagnostic.TimestampWrapper;
import eu.europa.esig.dss.diagnostic.jaxb.XmlTimestampedObject;
import eu.europa.esig.dss.enumerations.EvidenceRecordTypeEnum;
import eu.europa.esig.dss.enumerations.SignatureLevel;
import eu.europa.esig.dss.model.DSSDocument;
import eu.europa.esig.dss.model.FileDocument;
import eu.europa.esig.dss.spi.exception.IllegalInputException;
import eu.europa.esig.dss.utils.Utils;
import eu.europa.esig.dss.xades.evidencerecord.XAdESEvidenceRecordIncorporationParameters;
import eu.europa.esig.dss.xades.signature.XAdESService;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class XAdESLevelBAddXMLThenASN1EvidenceRecordTest extends AbstractXAdESAddEvidenceRecordTest {

    private boolean parallelER = false;

    @Override
    protected DSSDocument getSignatureDocument() {
        return new FileDocument("src/test/resources/validation/evidence-record/incorporation/X-B-B.xml");
    }

    @Override
    protected DSSDocument getEvidenceRecordDocument() {
        return new FileDocument("src/test/resources/validation/evidence-record/incorporation/evidence-record-X-B-B.xml");
    }

    protected DSSDocument getSecondEvidenceRecordDocument() {
        return new FileDocument("src/test/resources/validation/evidence-record/incorporation/evidence-record-second-on-xml-X-B-B.ers");
    }

    @Override
    protected XAdESEvidenceRecordIncorporationParameters getEvidenceRecordIncorporationParameters() {
        XAdESEvidenceRecordIncorporationParameters parameters = super.getEvidenceRecordIncorporationParameters();
        parameters.setParallelEvidenceRecord(parallelER);
        return parameters;
    }

    @Override
    protected void checkSignatureLevel(DiagnosticData diagnosticData) {
        assertEquals(SignatureLevel.XAdES_BASELINE_B, diagnosticData.getSignatureFormat(diagnosticData.getFirstSignatureId()));
    }

    @Override
    protected EvidenceRecordTypeEnum getEvidenceRecordType() {
        return null;
    }

    @Override
    protected void checkEvidenceRecords(DiagnosticData diagnosticData) {
        super.checkEvidenceRecords(diagnosticData);

        assertEquals(2, diagnosticData.getEvidenceRecords().size());

        int xmlERs = 0;
        int asn1ERs = 0;
        for (EvidenceRecordWrapper evidenceRecord : diagnosticData.getEvidenceRecords()) {
            if (EvidenceRecordTypeEnum.XML_EVIDENCE_RECORD == evidenceRecord.getEvidenceRecordType()) {
                ++xmlERs;
            } else if (EvidenceRecordTypeEnum.ASN1_EVIDENCE_RECORD == evidenceRecord.getEvidenceRecordType()) {
                ++asn1ERs;
            }
        }
        assertEquals(1, xmlERs);
        assertEquals(1, asn1ERs);
    }

    @Override
    protected void checkEvidenceRecordType(EvidenceRecordWrapper evidenceRecord) {
        // skip
    }

    @Override
    protected void checkEvidenceRecordTimestampedReferences(DiagnosticData diagnosticData) {
        super.checkEvidenceRecordTimestampedReferences(diagnosticData);

        int ersDoNotCoverERs = 0;
        int ersCoverERs = 0;
        for (EvidenceRecordWrapper evidenceRecord : diagnosticData.getEvidenceRecords()) {
            List<XmlTimestampedObject> coveredObjects = evidenceRecord.getCoveredObjects();
            assertTrue(Utils.isCollectionNotEmpty(coveredObjects));
            assertTrue(Utils.isCollectionNotEmpty(evidenceRecord.getCoveredSignatures()));
            assertTrue(Utils.isCollectionNotEmpty(evidenceRecord.getCoveredCertificates()));
            assertFalse(Utils.isCollectionNotEmpty(evidenceRecord.getCoveredRevocations()));
            assertTrue(Utils.isCollectionNotEmpty(evidenceRecord.getCoveredSignedData()));

            if (Utils.isCollectionEmpty(evidenceRecord.getCoveredEvidenceRecords())) {
                assertFalse(Utils.isCollectionNotEmpty(evidenceRecord.getCoveredTimestamps()));
                ++ersDoNotCoverERs;
            } else {
                assertTrue(Utils.isCollectionNotEmpty(evidenceRecord.getCoveredTimestamps()));
                ++ersCoverERs;
            }
        }
        assertEquals(1, ersDoNotCoverERs);
        assertEquals(1, ersCoverERs);
    }

    @Override
    protected void checkEvidenceRecordTimestamps(DiagnosticData diagnosticData) {
        List<EvidenceRecordWrapper> evidenceRecords = diagnosticData.getEvidenceRecords();
        for (EvidenceRecordWrapper evidenceRecord : evidenceRecords) {
            List<TimestampWrapper> timestamps = evidenceRecord.getTimestampList();
            assertTrue(Utils.isCollectionNotEmpty(timestamps));
            for (TimestampWrapper timestampWrapper : timestamps) {
                assertTrue(timestampWrapper.isMessageImprintDataFound());
                assertTrue(timestampWrapper.isMessageImprintDataIntact());
                assertTrue(timestampWrapper.isSignatureIntact());
                assertTrue(timestampWrapper.isSignatureValid());
            }
        }
    }

    @Override
    protected DSSDocument getSignedDocument() {
        XAdESService service = getService();
        DSSDocument oneERDoc = service.addSignatureEvidenceRecord(getSignatureDocument(), getEvidenceRecordDocument(), getEvidenceRecordIncorporationParameters());

        parallelER = false;

        DSSDocument twoERDoc = service.addSignatureEvidenceRecord(oneERDoc, getSecondEvidenceRecordDocument(), getEvidenceRecordIncorporationParameters());

        parallelER = true;

        Exception exception = assertThrows(IllegalInputException.class, () ->
                service.addSignatureEvidenceRecord(oneERDoc, getSecondEvidenceRecordDocument(), getEvidenceRecordIncorporationParameters()));
        assertEquals("The latest signature unsigned property contains evidence records other " +
                "than EvidenceRecord type specified in IETF RFC 4998. The incorporation of different evidence record " +
                "types within the same unsigned property is not supported.", exception.getMessage());

        return twoERDoc;
    }

}
