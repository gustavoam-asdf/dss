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
package eu.europa.esig.dss.validation.job.alerts;

import eu.europa.esig.dss.alert.Alert;
import eu.europa.esig.dss.model.job.DocumentInfo;
import eu.europa.esig.dss.model.job.DocumentListInfo;
import eu.europa.esig.dss.model.job.ValidationJobSummary;
import eu.europa.esig.dss.utils.Utils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * The class to process alerts on ValidationJob
 *
 */
public class ValidationJobAlerter<D extends DocumentInfo<L>, L extends DocumentListInfo<L, D>> {

	private static final Logger LOG = LoggerFactory.getLogger(ValidationJobAlerter.class);

	/** Contains a list of LOTL alerts */
	private final List<Alert<L>> lotlAlerts;

	/** Contains a list of TL alerts */
	private final List<Alert<D>> tlAlerts;

	/**
	 * The constructor to instantiate a TLValidationJobAlerter
	 * 
	 * @param lotlAlerts a list of {@link DocumentListAlert}s to be applied on LOTL changes
	 * @param tlAlerts a list of {@link DocumentAlert}s to be applied on TL changes
	 */
	public ValidationJobAlerter(final List<Alert<L>> lotlAlerts, final List<Alert<D>> tlAlerts) {
		this.lotlAlerts = lotlAlerts;
		this.tlAlerts = tlAlerts;
	}
	
	/**
	 * The method to run alerts on the given ValidationJobSummary
	 * 
	 * @param jobSummary {@link ValidationJobSummary} to execute alerts on
	 */
	public void detectChanges(final ValidationJobSummary<D, L> jobSummary) {
		for (L lotlInfo : jobSummary.getDocumentListInfos()) {
			// run LOTL alerts
			if (Utils.isCollectionNotEmpty(lotlAlerts)) {
				for (Alert<L> lotlAlert : lotlAlerts) {
					execute(lotlAlert, lotlInfo);
				}
			}
			// run TL alerts
			if (Utils.isCollectionNotEmpty(tlAlerts)) {
				for (D tlInfo : lotlInfo.getChildrenInfos()) {
					for (Alert<D> tlAlert : tlAlerts) {
						execute(tlAlert, tlInfo);
					}
				}
			}
		}
		// other TLs
		if (Utils.isCollectionNotEmpty(tlAlerts)) {
			for (D tlInfo : jobSummary.getOtherDocumentInfos()) {
				for (Alert<D> tlAlert : tlAlerts) {
					execute(tlAlert, tlInfo);
				}
			}
		}
	}
	
	private <T extends DocumentInfo> void execute(Alert<T> alert, T info) {
		try {
			alert.alert(info);
		} catch (Exception e) {
			LOG.warn("An error occurred while trying to detect changes inside '{}'. Reason : {}", 
					info.getDSSId().asXmlId(), e.getMessage());
		}
	}
	
}
