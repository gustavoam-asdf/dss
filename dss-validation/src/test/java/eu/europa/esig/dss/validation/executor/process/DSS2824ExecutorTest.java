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
package eu.europa.esig.dss.validation.executor.process;

import eu.europa.esig.dss.detailedreport.DetailedReport;
import eu.europa.esig.dss.detailedreport.jaxb.XmlBasicBuildingBlocks;
import eu.europa.esig.dss.detailedreport.jaxb.XmlCRS;
import eu.europa.esig.dss.detailedreport.jaxb.XmlConstraint;
import eu.europa.esig.dss.detailedreport.jaxb.XmlRAC;
import eu.europa.esig.dss.detailedreport.jaxb.XmlSubXCV;
import eu.europa.esig.dss.detailedreport.jaxb.XmlXCV;
import eu.europa.esig.dss.diagnostic.CertificateRevocationWrapper;
import eu.europa.esig.dss.diagnostic.CertificateWrapper;
import eu.europa.esig.dss.diagnostic.DiagnosticData;
import eu.europa.esig.dss.diagnostic.DiagnosticDataFacade;
import eu.europa.esig.dss.diagnostic.SignatureWrapper;
import eu.europa.esig.dss.diagnostic.jaxb.XmlDiagnosticData;
import eu.europa.esig.dss.enumerations.Indication;
import eu.europa.esig.dss.i18n.MessageTag;
import eu.europa.esig.dss.simplereport.SimpleReport;
import eu.europa.esig.dss.validation.executor.signature.DefaultSignatureProcessExecutor;
import eu.europa.esig.dss.validation.process.ValidationProcessUtils;
import eu.europa.esig.dss.validation.reports.Reports;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class DSS2824ExecutorTest extends AbstractProcessExecutorTest {

    @Test
    void dss2824PassedValidationWithArchiveCutoffTest() throws Exception {
        XmlDiagnosticData xmlDiagnosticData = DiagnosticDataFacade.newFacade().unmarshall(
                new File("src/test/resources/diag-data/diag_data_ocsp_with_archivecutoff.xml"));
        assertNotNull(xmlDiagnosticData);

        DefaultSignatureProcessExecutor executor = new DefaultSignatureProcessExecutor();
        executor.setDiagnosticData(xmlDiagnosticData);
        executor.setValidationPolicy(loadDefaultPolicy());
        executor.setCurrentTime(xmlDiagnosticData.getValidationDate());

        Reports reports = executor.execute();

        DiagnosticData diagnosticData = reports.getDiagnosticData();
        SignatureWrapper signature = diagnosticData.getSignatureById(diagnosticData.getFirstSignatureId());
        CertificateWrapper signingCertificate = signature.getSigningCertificate();
        assertNotNull(signingCertificate);
        List<CertificateRevocationWrapper> certificateRevocationData = signingCertificate.getCertificateRevocationData();
        assertEquals(1, certificateRevocationData.size());
        CertificateRevocationWrapper certificateRevocation = certificateRevocationData.get(0);

        SimpleReport simpleReport = reports.getSimpleReport();
        assertEquals(Indication.TOTAL_PASSED, simpleReport.getIndication(simpleReport.getFirstSignatureId()));

        DetailedReport detailedReport = reports.getDetailedReport();
        XmlBasicBuildingBlocks signatureBBB = detailedReport.getBasicBuildingBlockById(detailedReport.getFirstSignatureId());
        assertNotNull(signatureBBB);

        XmlXCV xcv = signatureBBB.getXCV();
        assertNotNull(xcv);

        List<XmlSubXCV> subXCV = xcv.getSubXCV();
        assertEquals(2, subXCV.size());

        XmlSubXCV xmlSubXCV = subXCV.get(0);
        XmlCRS crs = xmlSubXCV.getCRS();
        assertNotNull(crs);

        List<XmlRAC> racs = crs.getRAC();
        assertEquals(1, racs.size());

        XmlRAC xmlRAC = racs.get(0);
        boolean consistencyCheckFound = false;
        for (XmlConstraint constraint : xmlRAC.getConstraint()) {
            if (MessageTag.BBB_XCV_REVOC_HAS_CERT_INFO.getId().equals(constraint.getName().getKey())) {
                assertEquals(i18nProvider.getMessage(MessageTag.REVOCATION_CONSISTENT_OCSP,
                        ValidationProcessUtils.getFormattedDate(certificateRevocation.getThisUpdate()),
                        ValidationProcessUtils.getFormattedDate(certificateRevocation.getArchiveCutOff()),
                        ValidationProcessUtils.getFormattedDate(signingCertificate.getNotBefore()),
                        ValidationProcessUtils.getFormattedDate(signingCertificate.getNotAfter())), constraint.getAdditionalInfo());
                consistencyCheckFound = true;
            }
        }
        assertTrue(consistencyCheckFound);

        checkReports(reports);
    }

    @Test
    void dss2824RevocationValidAtValidationTimeTest() throws Exception {
        XmlDiagnosticData xmlDiagnosticData = DiagnosticDataFacade.newFacade().unmarshall(
                new File("src/test/resources/diag-data/valid-diag-data.xml"));
        assertNotNull(xmlDiagnosticData);

        DefaultSignatureProcessExecutor executor = new DefaultSignatureProcessExecutor();
        executor.setDiagnosticData(xmlDiagnosticData);
        executor.setValidationPolicy(loadDefaultPolicy());
        executor.setCurrentTime(xmlDiagnosticData.getValidationDate());

        Reports reports = executor.execute();

        DiagnosticData diagnosticData = reports.getDiagnosticData();
        SignatureWrapper signature = diagnosticData.getSignatureById(diagnosticData.getFirstSignatureId());
        CertificateWrapper signingCertificate = signature.getSigningCertificate();
        assertNotNull(signingCertificate);
        List<CertificateRevocationWrapper> certificateRevocationData = signingCertificate.getCertificateRevocationData();
        assertEquals(1, certificateRevocationData.size());
        CertificateRevocationWrapper certificateRevocation = certificateRevocationData.get(0);

        SimpleReport simpleReport = reports.getSimpleReport();
        assertEquals(Indication.TOTAL_PASSED, simpleReport.getIndication(simpleReport.getFirstSignatureId()));

        DetailedReport detailedReport = reports.getDetailedReport();
        XmlBasicBuildingBlocks signatureBBB = detailedReport.getBasicBuildingBlockById(detailedReport.getFirstSignatureId());
        assertNotNull(signatureBBB);

        XmlXCV xcv = signatureBBB.getXCV();
        assertNotNull(xcv);

        List<XmlSubXCV> subXCV = xcv.getSubXCV();
        assertEquals(3, subXCV.size());

        XmlSubXCV xmlSubXCV = subXCV.get(0);
        XmlCRS crs = xmlSubXCV.getCRS();
        assertNotNull(crs);

        List<XmlRAC> racs = crs.getRAC();
        assertEquals(1, racs.size());

        XmlRAC xmlRAC = racs.get(0);
        boolean consistencyCheckFound = false;
        for (XmlConstraint constraint : xmlRAC.getConstraint()) {
            if (MessageTag.BBB_XCV_REVOC_HAS_CERT_INFO.getId().equals(constraint.getName().getKey())) {
                assertEquals(i18nProvider.getMessage(MessageTag.REVOCATION_CONSISTENT,
                        ValidationProcessUtils.getFormattedDate(certificateRevocation.getThisUpdate()),
                        ValidationProcessUtils.getFormattedDate(signingCertificate.getNotBefore()),
                        ValidationProcessUtils.getFormattedDate(signingCertificate.getNotAfter())), constraint.getAdditionalInfo());
                consistencyCheckFound = true;
            }
        }
        assertTrue(consistencyCheckFound);

        checkReports(reports);
    }

}
