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
package eu.europa.esig.dss.lote.job;

import eu.europa.esig.dss.lote.cache.CacheKey;
import eu.europa.esig.dss.lote.cache.access.ChangeCacheAccess;
import eu.europa.esig.dss.lote.dto.ParsingCacheDTO;
import eu.europa.esig.dss.model.lote.OtherListPointer;
import eu.europa.esig.dss.model.x509.CertificateToken;
import eu.europa.esig.dss.utils.Utils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;

/**
 * Applies changes in the Lists cache
 */
public class ListChangeApplier {

	private static final Logger LOG = LoggerFactory.getLogger(ListChangeApplier.class);

	/** Access the TL caches */
	private final ChangeCacheAccess cacheAccess;

	/** Old cache values */
	private final Map<CacheKey, ParsingCacheDTO> oldValues;

	/** New cache values */
	private final Map<CacheKey, ParsingCacheDTO> newValues;

	/**
	 * Default constructor
	 *
	 * @param cacheAccess {@link ChangeCacheAccess} to use
	 * @param oldValues a map of old parsing values
	 * @param newValues a map of new parsing values
	 */
	public ListChangeApplier(final ChangeCacheAccess cacheAccess,
                             final Map<CacheKey, ParsingCacheDTO> oldValues, final Map<CacheKey, ParsingCacheDTO> newValues) {
		this.cacheAccess = cacheAccess;
		this.oldValues = oldValues;
		this.newValues = newValues;
	}

	/**
	 * Applies changes for all defined records
	 */
	public void analyzeAndApply() {
		for (Entry<CacheKey, ParsingCacheDTO> oldEntry : oldValues.entrySet()) {
			Map<String, List<CertificateToken>> oldUrlCerts = getTLPointers(oldEntry.getValue());
			Map<String, List<CertificateToken>> newUrlCerts = getTLPointers(newValues.get(oldEntry.getKey()));

			detectUrlChanges(oldUrlCerts, newUrlCerts);
			detectSigCertsChanges(oldUrlCerts, newUrlCerts);
		}
	}

	private Map<String, List<CertificateToken>> getTLPointers(ParsingCacheDTO parsingResult) {
		List<OtherListPointer> otherListPointers = parsingResult.getOtherListPointers();
		if (Utils.isCollectionNotEmpty(otherListPointers)) {
			return otherListPointers.stream().collect(Collectors.toMap(OtherListPointer::getLocationUrl, OtherListPointer::getSdiCertificates));
		}
		return Collections.emptyMap();
	}

	private void detectUrlChanges(Map<String, List<CertificateToken>> oldUrlCerts, Map<String, List<CertificateToken>> newUrlCerts) {
		for (String oldUrl : oldUrlCerts.keySet()) {
			if (!newUrlCerts.containsKey(oldUrl)) {
				LOG.info("LoTE with URL '{}' is not used anymore (replaced URL)", oldUrl);
				cacheAccess.toBeDeleted(new CacheKey(oldUrl));
			}
		}
	}

	private void detectSigCertsChanges(Map<String, List<CertificateToken>> oldUrlCerts, Map<String, List<CertificateToken>> newUrlCerts) {
		for (Entry<String, List<CertificateToken>> newEntry : newUrlCerts.entrySet()) {
			String newUrl = newEntry.getKey();
			List<CertificateToken> oldCerts = oldUrlCerts.get(newUrl);
			List<CertificateToken> newCerts = newEntry.getValue();
			if (oldCerts != null && !oldCerts.equals(newCerts)) {
				LOG.info("Signing certificates change detected for LoTE with URL '{}'", newUrl);
				cacheAccess.expireSignatureValidation(new CacheKey(newUrl));
			}
		}
	}

}
