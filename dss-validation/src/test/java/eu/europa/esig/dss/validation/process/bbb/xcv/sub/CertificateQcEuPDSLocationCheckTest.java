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
package eu.europa.esig.dss.validation.process.bbb.xcv.sub;

import eu.europa.esig.dss.detailedreport.jaxb.XmlConstraint;
import eu.europa.esig.dss.detailedreport.jaxb.XmlStatus;
import eu.europa.esig.dss.detailedreport.jaxb.XmlSubXCV;
import eu.europa.esig.dss.diagnostic.CertificateWrapper;
import eu.europa.esig.dss.diagnostic.jaxb.XmlCertificate;
import eu.europa.esig.dss.diagnostic.jaxb.XmlLangAndValue;
import eu.europa.esig.dss.diagnostic.jaxb.XmlQcStatements;
import eu.europa.esig.dss.enumerations.CertificateExtensionEnum;
import eu.europa.esig.dss.enumerations.Level;
import eu.europa.esig.dss.policy.MultiValuesConstraintWrapper;
import eu.europa.esig.dss.policy.jaxb.MultiValuesConstraint;
import eu.europa.esig.dss.validation.process.bbb.AbstractTestCheck;
import eu.europa.esig.dss.validation.process.bbb.xcv.sub.checks.CertificateQcEuPDSLocationCheck;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class CertificateQcEuPDSLocationCheckTest extends AbstractTestCheck {

    @Test
    void validTest() {
        XmlQcStatements xmlQcStatements = new XmlQcStatements();
        xmlQcStatements.setOID(CertificateExtensionEnum.QC_STATEMENTS.getOid());

        XmlLangAndValue langAndValueEn = new XmlLangAndValue();
        langAndValueEn.setLang("en");
        langAndValueEn.setValue("https://repository.eid.belgium.be");
        XmlLangAndValue langAndValueFr = new XmlLangAndValue();
        langAndValueFr.setLang("fr");
        langAndValueFr.setValue("https://repository.eid.belgium.be/fr/");
        xmlQcStatements.setQcEuPDS(Arrays.asList(langAndValueEn, langAndValueFr));

        MultiValuesConstraint constraint = new MultiValuesConstraint();
        constraint.setLevel(Level.FAIL);
        constraint.getId().add("https://repository.eid.belgium.be");

        XmlCertificate xc = new XmlCertificate();
        xc.getCertificateExtensions().add(xmlQcStatements);

        XmlSubXCV result = new XmlSubXCV();
        CertificateQcEuPDSLocationCheck cqcpdslc = new CertificateQcEuPDSLocationCheck(i18nProvider, result,
                new CertificateWrapper(xc), new MultiValuesConstraintWrapper(constraint));
        cqcpdslc.execute();

        List<XmlConstraint> constraints = result.getConstraint();
        assertEquals(1, constraints.size());
        assertEquals(XmlStatus.OK, constraints.get(0).getStatus());
    }

    @Test
    void invalidTest() {
        XmlQcStatements xmlQcStatements = new XmlQcStatements();
        xmlQcStatements.setOID(CertificateExtensionEnum.QC_STATEMENTS.getOid());

        XmlLangAndValue langAndValueEn = new XmlLangAndValue();
        langAndValueEn.setLang("en");
        langAndValueEn.setValue("https://repository.eid.lux.lu");
        XmlLangAndValue langAndValueFr = new XmlLangAndValue();
        langAndValueFr.setLang("fr");
        langAndValueFr.setValue("https://repository.eid.lux.lu/fr/");
        xmlQcStatements.setQcEuPDS(Arrays.asList(langAndValueEn, langAndValueFr));

        MultiValuesConstraint constraint = new MultiValuesConstraint();
        constraint.setLevel(Level.FAIL);
        constraint.getId().add("https://repository.eid.belgium.be");

        XmlCertificate xc = new XmlCertificate();
        xc.getCertificateExtensions().add(xmlQcStatements);

        XmlSubXCV result = new XmlSubXCV();
        CertificateQcEuPDSLocationCheck cqcpdslc = new CertificateQcEuPDSLocationCheck(i18nProvider, result,
                new CertificateWrapper(xc), new MultiValuesConstraintWrapper(constraint));
        cqcpdslc.execute();

        List<XmlConstraint> constraints = result.getConstraint();
        assertEquals(1, constraints.size());
        assertEquals(XmlStatus.NOT_OK, constraints.get(0).getStatus());
    }

    @Test
    void qcPDSNotPresentTest() {
        XmlQcStatements xmlQcStatements = new XmlQcStatements();
        xmlQcStatements.setOID(CertificateExtensionEnum.QC_STATEMENTS.getOid());

        MultiValuesConstraint constraint = new MultiValuesConstraint();
        constraint.setLevel(Level.FAIL);
        constraint.getId().add("https://repository.eid.belgium.be");

        XmlCertificate xc = new XmlCertificate();
        xc.getCertificateExtensions().add(xmlQcStatements);

        XmlSubXCV result = new XmlSubXCV();
        CertificateQcEuPDSLocationCheck cqcpdslc = new CertificateQcEuPDSLocationCheck(i18nProvider, result,
                new CertificateWrapper(xc), new MultiValuesConstraintWrapper(constraint));
        cqcpdslc.execute();

        List<XmlConstraint> constraints = result.getConstraint();
        assertEquals(1, constraints.size());
        assertEquals(XmlStatus.NOT_OK, constraints.get(0).getStatus());
    }

    @Test
    void qcStatementsNotPresentTest() {
        MultiValuesConstraint constraint = new MultiValuesConstraint();
        constraint.setLevel(Level.FAIL);
        constraint.getId().add("https://repository.eid.belgium.be");

        XmlCertificate xc = new XmlCertificate();

        XmlSubXCV result = new XmlSubXCV();
        CertificateQcEuPDSLocationCheck cqcpdslc = new CertificateQcEuPDSLocationCheck(i18nProvider, result,
                new CertificateWrapper(xc), new MultiValuesConstraintWrapper(constraint));
        cqcpdslc.execute();

        List<XmlConstraint> constraints = result.getConstraint();
        assertEquals(1, constraints.size());
        assertEquals(XmlStatus.NOT_OK, constraints.get(0).getStatus());
    }

}
