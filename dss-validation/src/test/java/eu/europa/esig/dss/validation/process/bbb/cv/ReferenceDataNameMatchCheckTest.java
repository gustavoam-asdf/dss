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
package eu.europa.esig.dss.validation.process.bbb.cv;

import eu.europa.esig.dss.detailedreport.jaxb.XmlCV;
import eu.europa.esig.dss.detailedreport.jaxb.XmlConstraint;
import eu.europa.esig.dss.detailedreport.jaxb.XmlStatus;
import eu.europa.esig.dss.diagnostic.jaxb.XmlDigestMatcher;
import eu.europa.esig.dss.enumerations.DigestMatcherType;
import eu.europa.esig.dss.enumerations.Level;
import eu.europa.esig.dss.policy.LevelConstraintWrapper;
import eu.europa.esig.dss.policy.jaxb.LevelConstraint;
import eu.europa.esig.dss.validation.process.bbb.AbstractTestCheck;
import eu.europa.esig.dss.validation.process.bbb.cv.checks.ReferenceDataNameMatchCheck;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ReferenceDataNameMatchCheckTest extends AbstractTestCheck {

    @Test
    void referenceDataIntactCheck() {
        XmlDigestMatcher digestMatcher = new XmlDigestMatcher();
        digestMatcher.setDataIntact(true);
        digestMatcher.setType(DigestMatcherType.MESSAGE_DIGEST);
        digestMatcher.setUri("sample.xml");
        digestMatcher.setDocumentName("sample.xml");

        LevelConstraint constraint = new LevelConstraint();
        constraint.setLevel(Level.FAIL);

        XmlCV result = new XmlCV();
        ReferenceDataNameMatchCheck<XmlCV> rdnmc = new ReferenceDataNameMatchCheck<>(i18nProvider, result, digestMatcher, new LevelConstraintWrapper(constraint));
        rdnmc.execute();

        List<XmlConstraint> constraints = result.getConstraint();
        assertEquals(1, constraints.size());
        assertEquals(XmlStatus.OK, constraints.get(0).getStatus());
    }

    @Test
    void referenceDataNotIntactCheck() {
        XmlDigestMatcher digestMatcher = new XmlDigestMatcher();
        digestMatcher.setDataIntact(false);
        digestMatcher.setType(DigestMatcherType.MESSAGE_DIGEST);
        digestMatcher.setUri("sample.xml");
        digestMatcher.setDocumentName("sample.png");

        LevelConstraint constraint = new LevelConstraint();
        constraint.setLevel(Level.FAIL);

        XmlCV result = new XmlCV();
        ReferenceDataNameMatchCheck<XmlCV> rdnmc = new ReferenceDataNameMatchCheck<>(i18nProvider, result, digestMatcher, new LevelConstraintWrapper(constraint));
        rdnmc.execute();

        List<XmlConstraint> constraints = result.getConstraint();
        assertEquals(1, constraints.size());
        assertEquals(XmlStatus.NOT_OK, constraints.get(0).getStatus());
    }

}
