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
package eu.europa.esig.dss.validation.job;

import eu.europa.esig.dss.model.job.OtherDocumentPointer;
import eu.europa.esig.dss.model.x509.CertificateToken;
import eu.europa.esig.dss.utils.Utils;
import eu.europa.esig.dss.validation.job.cache.CacheKey;
import eu.europa.esig.dss.validation.job.cache.access.DocumentChangesCacheAccess;
import eu.europa.esig.dss.validation.job.cache.state.CachedEntry;
import eu.europa.esig.dss.validation.job.parsing.ParsingResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;

/**
 * Applies changes in the LOTL cache
 */
public class DocumentChangeApplier {

	private static final Logger LOG = LoggerFactory.getLogger(DocumentChangeApplier.class);

	/** Access the TL caches */
	private final DocumentChangesCacheAccess cacheAccess;

	/** Old cache values */
	private final Map<CacheKey, CachedEntry<ParsingResult>> oldValues;

	/** New cache values */
	private final Map<CacheKey, CachedEntry<ParsingResult>> newValues;

	/**
	 * Default constructor
	 *
	 * @param cacheAccess {@link DocumentChangesCacheAccess} to use
	 * @param oldValues a map of old parsing values
	 * @param newValues a map of new parsing values
	 */
	public DocumentChangeApplier(final DocumentChangesCacheAccess cacheAccess,
                                 final Map<CacheKey, CachedEntry<ParsingResult>> oldValues, final Map<CacheKey, CachedEntry<ParsingResult>> newValues) {
		this.cacheAccess = cacheAccess;
		this.oldValues = oldValues;
		this.newValues = newValues;
	}

	/**
	 * Applies changes for all defined records
	 */
	public void analyzeAndApply() {
		for (Entry<CacheKey, CachedEntry<ParsingResult>> oldEntry : oldValues.entrySet()) {
			Map<String, List<CertificateToken>> oldUrlCerts = getTLPointers(oldEntry.getValue());
			Map<String, List<CertificateToken>> newUrlCerts = getTLPointers(newValues.get(oldEntry.getKey()));

			detectUrlChanges(oldUrlCerts, newUrlCerts);
			detectSigCertsChanges(oldUrlCerts, newUrlCerts);
		}
	}

	private Map<String, List<CertificateToken>> getTLPointers(CachedEntry<ParsingResult> parsingCache) {
		if (parsingCache == null || !parsingCache.isResultExist()) {
			return Collections.emptyMap();
		}
		List<? extends OtherDocumentPointer> tlOtherPointers = parsingCache.getCachedResult().getOtherDocumentPointers();
		if (Utils.isCollectionNotEmpty(tlOtherPointers)) {
			return tlOtherPointers.stream().collect(Collectors.toMap(OtherDocumentPointer::getLocation, OtherDocumentPointer::getSdiCertificates));
		}
		return Collections.emptyMap();
	}

	private void detectUrlChanges(Map<String, List<CertificateToken>> oldUrlCerts, Map<String, List<CertificateToken>> newUrlCerts) {
		for (String oldUrl : oldUrlCerts.keySet()) {
			if (!newUrlCerts.containsKey(oldUrl)) {
				LOG.info("TL with URL '{}' is not used anymore (replaced URL in the LOTL)", oldUrl);
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
				LOG.info("Signing certificates change detected for TL with URL '{}'", newUrl);
				cacheAccess.expireSignatureValidation(new CacheKey(newUrl));
			}
		}
	}

}
