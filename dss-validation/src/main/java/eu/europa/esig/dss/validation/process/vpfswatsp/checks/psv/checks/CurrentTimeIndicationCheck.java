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
package eu.europa.esig.dss.validation.process.vpfswatsp.checks.psv.checks;

import eu.europa.esig.dss.detailedreport.jaxb.XmlMessage;
import eu.europa.esig.dss.detailedreport.jaxb.XmlPSV;
import eu.europa.esig.dss.enumerations.Indication;
import eu.europa.esig.dss.enumerations.SubIndication;
import eu.europa.esig.dss.i18n.I18nProvider;
import eu.europa.esig.dss.i18n.MessageTag;
import eu.europa.esig.dss.model.policy.LevelRule;
import eu.europa.esig.dss.validation.process.ChainItem;

import java.util.List;

/**
 * Checks if the current state is PASSED
 */
public class CurrentTimeIndicationCheck extends ChainItem<XmlPSV> {

	/** Indication to check */
	private final Indication indication;

	/** Corresponding SubIndication */
	private final SubIndication subIndication;

	/** Current errors  */
	private final List<XmlMessage> errors;

	/**
	 * Default constructor
	 *
	 * @param i18nProvider {@link I18nProvider}
	 * @param result {@link XmlPSV}
	 * @param indication {@link Indication}
	 * @param subIndication {@link SubIndication}
	 * @param errors a list of {@link XmlMessage}s
	 * @param constraint {@link LevelRule}
	 */
	public CurrentTimeIndicationCheck(I18nProvider i18nProvider, XmlPSV result, Indication indication,
									  SubIndication subIndication, List<XmlMessage> errors, LevelRule constraint) {
		super(i18nProvider, result, constraint);

		this.indication = indication;
		this.subIndication = subIndication;
		this.errors = errors;
	}

	@Override
	protected boolean process() {
		return Indication.PASSED.equals(indication);
	}

	@Override
	protected MessageTag getMessageTag() {
		return MessageTag.PSV_IPCVC;
	}

	@Override
	protected MessageTag getErrorMessageTag() {
		return MessageTag.PSV_IPCVC_ANS;
	}

	@Override
	protected Indication getFailedIndicationForConclusion() {
		return indication;
	}

	@Override
	protected SubIndication getFailedSubIndicationForConclusion() {
		return subIndication;
	}

	@Override
	protected List<XmlMessage> getPreviousErrors() {
		return errors;
	}

}
