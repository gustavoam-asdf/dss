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
package eu.europa.esig.dss.tsl.summary;

import eu.europa.esig.dss.model.job.DownloadInfoRecord;
import eu.europa.esig.dss.model.job.ValidationInfoRecord;
import eu.europa.esig.dss.model.tsl.CertificatePivotStatus;
import eu.europa.esig.dss.model.tsl.LOTLInfo;
import eu.europa.esig.dss.model.tsl.OtherTSLPointer;
import eu.europa.esig.dss.model.tsl.PivotInfo;
import eu.europa.esig.dss.model.tsl.TLInfo;
import eu.europa.esig.dss.model.tsl.TLValidationJobSummary;
import eu.europa.esig.dss.model.x509.CertificateToken;
import eu.europa.esig.dss.spi.x509.CertificateSource;
import eu.europa.esig.dss.tsl.dto.TLParsingCacheDTO;
import eu.europa.esig.dss.tsl.dto.builder.TLParsingCacheDTOBuilder;
import eu.europa.esig.dss.tsl.parsing.ParsingUtils;
import eu.europa.esig.dss.tsl.source.LOTLSource;
import eu.europa.esig.dss.tsl.source.TLSource;
import eu.europa.esig.dss.utils.Utils;
import eu.europa.esig.dss.validation.job.cache.CacheKey;
import eu.europa.esig.dss.validation.job.cache.access.ReadOnlyCacheAccess;
import eu.europa.esig.dss.validation.job.cache.state.CachedEntry;
import eu.europa.esig.dss.validation.job.download.DownloadResult;
import eu.europa.esig.dss.validation.job.dto.builder.DownloadCacheDTOBuilder;
import eu.europa.esig.dss.validation.job.dto.builder.ValidationCacheDTOBuilder;
import eu.europa.esig.dss.validation.job.parsing.ParsingResult;
import eu.europa.esig.dss.validation.job.summary.ValidationJobSummaryBuilder;
import eu.europa.esig.dss.validation.job.validation.ValidationResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Builds a {@code TLValidationJobSummary}
 */
public class TLValidationJobSummaryBuilder implements ValidationJobSummaryBuilder<TLInfo, LOTLInfo> {

	private static final Logger LOG = LoggerFactory.getLogger(TLValidationJobSummaryBuilder.class);

	/**
	 * A read-only access for the cache of the current Validation Job
	 */
	private final ReadOnlyCacheAccess readOnlyCacheAccess;

	/**
	 * List of TLSources to extract summary for
	 */
	private final TLSource[] tlSources;

	/**
	 * List of LOTLSource to extract summary for
	 */
	private final LOTLSource[] lotlSources;

	/**
	 * Default constructor
	 *
	 * @param readOnlyCacheAccess {@link ReadOnlyCacheAccess}
	 * @param tlSources {@link TLSource}s
	 * @param lotlSources {@link LOTLSource}s
	 */
	public TLValidationJobSummaryBuilder(final ReadOnlyCacheAccess readOnlyCacheAccess, final TLSource[] tlSources,
	                                     final LOTLSource[] lotlSources) {
		this.readOnlyCacheAccess = readOnlyCacheAccess;
		this.tlSources = tlSources;
		this.lotlSources = lotlSources;
	}

	/**
	 * Builds the {@code TLValidationJobSummary}
	 *
	 * @return {@link TLValidationJobSummary}
	 */
	public TLValidationJobSummary build() {

		final List<TLInfo> otherTLInfos = new ArrayList<>();
		if (Utils.isArrayNotEmpty(tlSources)) {
			for (TLSource tlSource : tlSources) {
				otherTLInfos.add(buildTLInfo(tlSource));
			}
		}

		final List<LOTLInfo> lotlList = new ArrayList<>();
		if (Utils.isArrayNotEmpty(lotlSources)) {

			for (LOTLSource lotlSource : lotlSources) {
				TLParsingCacheDTO lotlParsingResult = toParsingDTO(readOnlyCacheAccess.getParsingCacheEntry(lotlSource.getCacheKey()));

				LOTLInfo lotlInfo = buildLOTLInfo(lotlSource);

				List<TLInfo> tlInfos = new ArrayList<>();
				List<TLSource> currentTLSources = extractTLSources(lotlParsingResult);
				for (TLSource tlSource : currentTLSources) {
					OtherTSLPointer otherTSLPointer = getOtherTSLPointer(lotlParsingResult.getTlOtherPointers(), tlSource.getUrl());
					TLInfo tlInfo = buildTLInfo(tlSource, lotlInfo, otherTSLPointer);
					tlInfos.add(tlInfo);
				}
				lotlInfo.setTlInfos(tlInfos);

				if (lotlSource.isPivotSupport()) {
					List<PivotInfo> pivotInfos = new LinkedList<>();

					List<CertificateToken> currentCertificates = getLOTLKeystoreCertificates(lotlSource);
					if (LOG.isDebugEnabled()) {
						LOG.debug("LOTL original keystore certs [Amount : {}] : {}", currentCertificates.size(), currentCertificates);
					}
					
					List<LOTLSource> pivotSources = extractPivotSources(lotlParsingResult);
					for (LOTLSource pivotSource : pivotSources) {
						TLParsingCacheDTO pivotParsingCacheDTO = toParsingDTO(readOnlyCacheAccess.getParsingCacheEntry(pivotSource.getCacheKey()));
						List<CertificateToken> pivotCertificateTokens = getPivotCertificateTokens(pivotParsingCacheDTO);
						Map<CertificateToken, CertificatePivotStatus> certificateChangesMap = getCertificateChangesMap(
								pivotCertificateTokens, currentCertificates);
						String associatedLOTLLocation = getAssociatedLOTLLocation(pivotParsingCacheDTO);
						pivotInfos.add(buildPivotInfo(pivotSource, certificateChangesMap, associatedLOTLLocation));
						
						currentCertificates = pivotCertificateTokens;
						if (LOG.isDebugEnabled()) {
							LOG.debug("Pivot [{}] certificate source [Amount : {}] : {}", pivotSource.getUrl(), currentCertificates.size(), currentCertificates);
						}
					}
					lotlInfo.setPivotInfos(pivotInfos);
					
				} else {
					lotlInfo.setPivotInfos(Collections.emptyList());
				}

				lotlList.add(lotlInfo);
			}
		}

		return new TLValidationJobSummary(lotlList, otherTLInfos);
	}

	private LOTLInfo buildLOTLInfo(LOTLSource lotlSource) {
		CacheKey cacheKey = lotlSource.getCacheKey();
		return new LOTLInfo(
				toDownloadDTO(readOnlyCacheAccess.getDownloadCacheEntry(cacheKey)),
				toParsingDTO(readOnlyCacheAccess.getParsingCacheEntry(cacheKey)),
				toValidationDTO(readOnlyCacheAccess.getValidationCacheEntry(cacheKey)), lotlSource.getUrl()
		);
	}

	private TLInfo buildTLInfo(TLSource tlSource) {
		CacheKey cacheKey = tlSource.getCacheKey();
		return new TLInfo(
				toDownloadDTO(readOnlyCacheAccess.getDownloadCacheEntry(cacheKey)),
				toParsingDTO(readOnlyCacheAccess.getParsingCacheEntry(cacheKey)),
				toValidationDTO(readOnlyCacheAccess.getValidationCacheEntry(cacheKey)), tlSource.getUrl()
		);
	}

	private TLInfo buildTLInfo(TLSource tlSource, LOTLInfo lotlInfo, OtherTSLPointer otherTSLPointer) {
		CacheKey cacheKey = tlSource.getCacheKey();
		return new TLInfo(
				toDownloadDTO(readOnlyCacheAccess.getDownloadCacheEntry(cacheKey)),
				toParsingDTO(readOnlyCacheAccess.getParsingCacheEntry(cacheKey)),
				toValidationDTO(readOnlyCacheAccess.getValidationCacheEntry(cacheKey)), tlSource.getUrl(), lotlInfo, otherTSLPointer
		);
	}

	private PivotInfo buildPivotInfo(LOTLSource pivotSource, Map<CertificateToken, CertificatePivotStatus> certificateChangesMap, 
			String associatedLOTLLocation) {
		CacheKey cacheKey = pivotSource.getCacheKey();
		return new PivotInfo(toDownloadDTO(readOnlyCacheAccess.getDownloadCacheEntry(cacheKey)),
				toParsingDTO(readOnlyCacheAccess.getParsingCacheEntry(cacheKey)),
				toValidationDTO(readOnlyCacheAccess.getValidationCacheEntry(cacheKey)), pivotSource.getUrl(),
				certificateChangesMap, associatedLOTLLocation);
	}

	private DownloadInfoRecord toDownloadDTO(CachedEntry<DownloadResult> downloadCacheEntry) {
		return new DownloadCacheDTOBuilder(downloadCacheEntry).build();
	}

	private TLParsingCacheDTO toParsingDTO(CachedEntry<ParsingResult> parsingCacheEntry) {
		return new TLParsingCacheDTOBuilder(parsingCacheEntry).build();
	}

	private ValidationInfoRecord toValidationDTO(CachedEntry<ValidationResult> validationCacheEntry) {
		return new ValidationCacheDTOBuilder(validationCacheEntry).build();
	}

	private OtherTSLPointer getOtherTSLPointer(List<OtherTSLPointer> tlOtherPointers, String tslPointerLocation) {
		for (OtherTSLPointer otherTSLPointer : tlOtherPointers) {
			if (Utils.areStringsEqual(tslPointerLocation, otherTSLPointer.getTSLLocation())) {
				return otherTSLPointer;
			}
		}
		return null;
	}

	private List<TLSource> extractTLSources(TLParsingCacheDTO lotlParsingResult) {
		List<TLSource> result = new ArrayList<>();
		if (lotlParsingResult != null && lotlParsingResult.isResultExist()) {
			List<OtherTSLPointer> tlPointers = lotlParsingResult.getTlOtherPointers();
			for (OtherTSLPointer otherTSLPointerDTO : tlPointers) {
				TLSource tlSource = new TLSource();
				tlSource.setUrl(otherTSLPointerDTO.getTSLLocation());
				result.add(tlSource);
			}
		}
		return result;
	}
	
	private List<CertificateToken> getLOTLKeystoreCertificates(LOTLSource lotlSource) {
		CertificateSource certificateSource = lotlSource.getCertificateSource();
		if (certificateSource != null) {
			return certificateSource.getCertificates();
		} else {
			LOG.warn("Certificate source is not defined for the LOTL with URL [{}]", lotlSource.getUrl());
			return Collections.emptyList();
		}
	}
	
	private List<LOTLSource> extractPivotSources(TLParsingCacheDTO lotlParsingResult) {
		List<LOTLSource> result = new LinkedList<>();
		if (lotlParsingResult != null && lotlParsingResult.isResultExist()) {
			List<String> pivotUrls = lotlParsingResult.getPivotUrls();
			for (String pivotUrl : pivotUrls) {
				LOTLSource pivotSource = new LOTLSource();
				pivotSource.setUrl(pivotUrl);
				result.add(pivotSource);
			}
		}
		return Utils.reverseList(result);
	}
	
	private List<CertificateToken> getPivotCertificateTokens(TLParsingCacheDTO parsingCacheDTO) {
		List<OtherTSLPointer> lotlOtherPointers = parsingCacheDTO.getLotlOtherPointers();
		int lotlOtherPointersAmount = Utils.isCollectionNotEmpty(lotlOtherPointers) ? lotlOtherPointers.size() : 0;
		if (lotlOtherPointersAmount == 1) {
			return lotlOtherPointers.get(0).getSdiCertificates();
		} else {
			LOG.debug("Pivot certificates were not extracted. Nb of OtherTSLPointers is [{}]", lotlOtherPointersAmount);
			return Collections.emptyList();
		}
	}
	
	private Map<CertificateToken, CertificatePivotStatus> getCertificateChangesMap(List<CertificateToken> pivotSourceCertificates, 
			List<CertificateToken> currentCertificates) {
		Map<CertificateToken, CertificatePivotStatus> certificateChangesMap = new LinkedHashMap<>();
		
		List<CertificateToken> commonCertificates = pivotSourceCertificates.stream().filter(currentCertificates::contains).collect(Collectors.toList());
		
		// added certificates
		for (CertificateToken certificateToken : pivotSourceCertificates) {
			if (!commonCertificates.contains(certificateToken)) {
				certificateChangesMap.put(certificateToken, CertificatePivotStatus.ADDED);
			}
		}
		
		// common certificates
		for (CertificateToken certificateToken : commonCertificates) {
			certificateChangesMap.put(certificateToken, CertificatePivotStatus.NOT_CHANGED);
		}
		
		// removed certificates
		for (CertificateToken certificateToken : currentCertificates) {
			if (!commonCertificates.contains(certificateToken)) {
				certificateChangesMap.put(certificateToken, CertificatePivotStatus.REMOVED);
			}
		}
		
		return certificateChangesMap;
	}
	
	private String getAssociatedLOTLLocation(final TLParsingCacheDTO parsingCacheDTO) {
		OtherTSLPointer xmllotlPointer = ParsingUtils.getXMLLOTLPointer(parsingCacheDTO);
		if (xmllotlPointer != null) {
			return xmllotlPointer.getTSLLocation();
		}
		return null;
	}

}
