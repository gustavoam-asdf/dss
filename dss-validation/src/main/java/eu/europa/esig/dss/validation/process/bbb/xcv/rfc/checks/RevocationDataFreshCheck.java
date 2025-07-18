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
package eu.europa.esig.dss.validation.process.bbb.xcv.rfc.checks;

import eu.europa.esig.dss.detailedreport.jaxb.XmlRFC;
import eu.europa.esig.dss.diagnostic.RevocationWrapper;
import eu.europa.esig.dss.i18n.I18nProvider;
import eu.europa.esig.dss.model.policy.DurationRule;

import java.util.Date;

/**
 * Checks if the revocation data is fresh
 */
public class RevocationDataFreshCheck extends AbstractRevocationFreshCheck {

	/** Defines max freshness */
	private final DurationRule durationRule;

	/**
	 * Default constructor
	 *
	 * @param i18nProvider {@link I18nProvider}
	 * @param result {@link XmlRFC}
	 * @param revocationData {@link RevocationWrapper}
	 * @param validationDate {@link Date}
	 * @param constraint {@link DurationRule}
	 */
	public RevocationDataFreshCheck(I18nProvider i18nProvider, XmlRFC result, RevocationWrapper revocationData,
									Date validationDate, DurationRule constraint) {
		super(i18nProvider, result, revocationData, validationDate, constraint);
		this.durationRule = constraint;
	}

	@Override
	protected boolean process() {
		if (revocationData != null) {
			return isThisUpdateTimeAfterValidationTime();
		}
		return false;
	}

	@Override
	protected long getMaxFreshness() {
		return durationRule.getDuration();
	}

}
