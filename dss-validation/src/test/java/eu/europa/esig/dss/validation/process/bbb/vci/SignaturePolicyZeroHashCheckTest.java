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
package eu.europa.esig.dss.validation.process.bbb.vci;

import eu.europa.esig.dss.detailedreport.jaxb.XmlConstraint;
import eu.europa.esig.dss.detailedreport.jaxb.XmlStatus;
import eu.europa.esig.dss.detailedreport.jaxb.XmlVCI;
import eu.europa.esig.dss.diagnostic.SignatureWrapper;
import eu.europa.esig.dss.diagnostic.jaxb.XmlPolicy;
import eu.europa.esig.dss.diagnostic.jaxb.XmlPolicyDigestAlgoAndValue;
import eu.europa.esig.dss.diagnostic.jaxb.XmlSignature;
import eu.europa.esig.dss.enumerations.Level;
import eu.europa.esig.dss.policy.LevelConstraintWrapper;
import eu.europa.esig.dss.policy.jaxb.LevelConstraint;
import eu.europa.esig.dss.validation.process.bbb.AbstractTestCheck;
import eu.europa.esig.dss.validation.process.bbb.vci.checks.SignaturePolicyZeroHashCheck;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class SignaturePolicyZeroHashCheckTest extends AbstractTestCheck {

	@Test
	void signaturePolicyIdentifiedCheck() {
		XmlPolicy xmlPolicy = new XmlPolicy();
		XmlPolicyDigestAlgoAndValue xmlPolicyDigestAlgoAndValue = new XmlPolicyDigestAlgoAndValue();
		xmlPolicyDigestAlgoAndValue.setZeroHash(true);
		xmlPolicy.setDigestAlgoAndValue(xmlPolicyDigestAlgoAndValue);

		XmlSignature sig = new XmlSignature();
		sig.setPolicy(xmlPolicy);

		LevelConstraint constraint = new LevelConstraint();
		constraint.setLevel(Level.FAIL);

		XmlVCI result = new XmlVCI();
		SignaturePolicyZeroHashCheck spzhc = new SignaturePolicyZeroHashCheck(i18nProvider, result, new SignatureWrapper(sig),
				new LevelConstraintWrapper(constraint));
		spzhc.execute();

		List<XmlConstraint> constraints = result.getConstraint();
		assertEquals(1, constraints.size());
		assertEquals(XmlStatus.OK, constraints.get(0).getStatus());
	}

	@Test
	void signaturePolicyNotIdentifiedCheck() {
		XmlPolicy xmlPolicy = new XmlPolicy();
		XmlPolicyDigestAlgoAndValue xmlPolicyDigestAlgoAndValue = new XmlPolicyDigestAlgoAndValue();
		xmlPolicyDigestAlgoAndValue.setZeroHash(false);
		xmlPolicy.setDigestAlgoAndValue(xmlPolicyDigestAlgoAndValue);

		XmlSignature sig = new XmlSignature();
		sig.setPolicy(xmlPolicy);

		LevelConstraint constraint = new LevelConstraint();
		constraint.setLevel(Level.FAIL);

		XmlVCI result = new XmlVCI();
		SignaturePolicyZeroHashCheck spzhc = new SignaturePolicyZeroHashCheck(i18nProvider, result, new SignatureWrapper(sig),
				new LevelConstraintWrapper(constraint));
		spzhc.execute();

		List<XmlConstraint> constraints = result.getConstraint();
		assertEquals(1, constraints.size());
		assertEquals(XmlStatus.NOT_OK, constraints.get(0).getStatus());
	}

}
