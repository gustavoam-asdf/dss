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
package eu.europa.esig.dss.validation.process.bbb.sav;

import eu.europa.esig.dss.detailedreport.jaxb.XmlConstraint;
import eu.europa.esig.dss.detailedreport.jaxb.XmlSAV;
import eu.europa.esig.dss.detailedreport.jaxb.XmlStatus;
import eu.europa.esig.dss.diagnostic.SignatureWrapper;
import eu.europa.esig.dss.diagnostic.jaxb.XmlFoundTimestamp;
import eu.europa.esig.dss.diagnostic.jaxb.XmlSignature;
import eu.europa.esig.dss.diagnostic.jaxb.XmlTimestamp;
import eu.europa.esig.dss.enumerations.Level;
import eu.europa.esig.dss.enumerations.TimestampType;
import eu.europa.esig.dss.policy.LevelConstraintWrapper;
import eu.europa.esig.dss.policy.jaxb.ValueConstraint;
import eu.europa.esig.dss.validation.process.bbb.AbstractTestCheck;
import eu.europa.esig.dss.validation.process.bbb.sav.checks.ArchiveTimeStampCheck;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ArchiveTimeStampCheckTest extends AbstractTestCheck {

    @Test
    void valid() {
        XmlSignature sig = new XmlSignature();
        XmlFoundTimestamp xmlFoundTimestamp = new XmlFoundTimestamp();
        XmlTimestamp xmlTimestamp = new XmlTimestamp();
        xmlTimestamp.setType(TimestampType.ARCHIVE_TIMESTAMP);
        xmlFoundTimestamp.setTimestamp(xmlTimestamp);
        sig.setFoundTimestamps(Collections.singletonList(xmlFoundTimestamp));

        ValueConstraint constraint = new ValueConstraint();
        constraint.setLevel(Level.FAIL);

        XmlSAV result = new XmlSAV();
        ArchiveTimeStampCheck atsc = new ArchiveTimeStampCheck(i18nProvider, result, new SignatureWrapper(sig), new LevelConstraintWrapper(constraint));
        atsc.execute();

        List<XmlConstraint> constraints = result.getConstraint();
        assertEquals(1, constraints.size());
        assertEquals(XmlStatus.OK, constraints.get(0).getStatus());
    }

    @Test
    void invalid() {
        XmlSignature sig = new XmlSignature();
        XmlFoundTimestamp xmlFoundTimestamp = new XmlFoundTimestamp();
        XmlTimestamp xmlTimestamp = new XmlTimestamp();
        xmlTimestamp.setType(TimestampType.CONTENT_TIMESTAMP);
        xmlFoundTimestamp.setTimestamp(xmlTimestamp);
        sig.setFoundTimestamps(Collections.singletonList(xmlFoundTimestamp));

        ValueConstraint constraint = new ValueConstraint();
        constraint.setLevel(Level.FAIL);

        XmlSAV result = new XmlSAV();
        ArchiveTimeStampCheck atsc = new ArchiveTimeStampCheck(i18nProvider, result, new SignatureWrapper(sig), new LevelConstraintWrapper(constraint));
        atsc.execute();

        List<XmlConstraint> constraints = result.getConstraint();
        assertEquals(1, constraints.size());
        assertEquals(XmlStatus.NOT_OK, constraints.get(0).getStatus());
    }

    @Test
    void multipleEntries() {
        XmlSignature sig = new XmlSignature();

        XmlFoundTimestamp xmlFoundTimestampOne = new XmlFoundTimestamp();
        XmlTimestamp xmlTimestampOne = new XmlTimestamp();
        xmlTimestampOne.setType(TimestampType.SIGNATURE_TIMESTAMP);
        xmlFoundTimestampOne.setTimestamp(xmlTimestampOne);

        XmlFoundTimestamp xmlFoundTimestampTwo = new XmlFoundTimestamp();
        XmlTimestamp xmlTimestampTwo = new XmlTimestamp();
        xmlTimestampTwo.setType(TimestampType.ARCHIVE_TIMESTAMP);
        xmlFoundTimestampTwo.setTimestamp(xmlTimestampTwo);

        sig.setFoundTimestamps(Arrays.asList(xmlFoundTimestampOne, xmlFoundTimestampTwo));

        ValueConstraint constraint = new ValueConstraint();
        constraint.setLevel(Level.FAIL);

        XmlSAV result = new XmlSAV();
        ArchiveTimeStampCheck atsc = new ArchiveTimeStampCheck(i18nProvider, result, new SignatureWrapper(sig), new LevelConstraintWrapper(constraint));
        atsc.execute();

        List<XmlConstraint> constraints = result.getConstraint();
        assertEquals(1, constraints.size());
        assertEquals(XmlStatus.OK, constraints.get(0).getStatus());
    }

    @Test
    void empty() {
        XmlSignature sig = new XmlSignature();

        ValueConstraint constraint = new ValueConstraint();
        constraint.setLevel(Level.FAIL);

        XmlSAV result = new XmlSAV();
        ArchiveTimeStampCheck atsc = new ArchiveTimeStampCheck(i18nProvider, result, new SignatureWrapper(sig), new LevelConstraintWrapper(constraint));
        atsc.execute();

        List<XmlConstraint> constraints = result.getConstraint();
        assertEquals(1, constraints.size());
        assertEquals(XmlStatus.NOT_OK, constraints.get(0).getStatus());
    }

}
