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
package eu.europa.esig.dss.lote.dto.builder;

import eu.europa.esig.dss.enumerations.ListType;
import eu.europa.esig.dss.lote.cache.state.CachedEntry;
import eu.europa.esig.dss.lote.dto.ParsingCacheDTO;
import eu.europa.esig.dss.lote.parsing.ParsingResult;
import eu.europa.esig.dss.model.lote.OtherListPointer;
import eu.europa.esig.dss.model.lote.TrustedEntity;

import java.util.Date;
import java.util.List;

/**
 * Builds {@code ParsingCacheDTO}
 *
 */
public class ParsingCacheDTOBuilder extends AbstractCacheDTOBuilder<ParsingResult> {

	/**
	 * Default constructor
	 *
	 * @param cachedEntry parsing cache entry
	 */
	public ParsingCacheDTOBuilder(final CachedEntry<ParsingResult> cachedEntry) {
		super(cachedEntry);
	}
	
	@Override
	public ParsingCacheDTO build() {
		ParsingCacheDTO parsingCacheDTO = new ParsingCacheDTO(super.build());
		if (isResultExist()) {
			parsingCacheDTO.setType(getType());
			parsingCacheDTO.setSequenceNumber(getSequenceNumber());
			parsingCacheDTO.setVersion(getVersion());
			parsingCacheDTO.setTerritory(getTerritory());
			parsingCacheDTO.setIssueDate(getIssueDate());
			parsingCacheDTO.setNextUpdateDate(getNextUpdateDate());
			parsingCacheDTO.setDistributionPoints(getDistributionPoints());
			parsingCacheDTO.setStructureValidationMessages(getStructureValidationMessages());
			parsingCacheDTO.setCurrentListPointers(getCurrentListPointers());
			parsingCacheDTO.setOtherListPointers(getOtherListPointers());
			parsingCacheDTO.setTrustedEntities(getTrustedEntities());
		}
		return parsingCacheDTO;
	}

	private ListType getType() {
		return getResult().getType();
	}
	
	private Integer getSequenceNumber() {
		return getResult().getSequenceNumber();
	}
	
	private Integer getVersion() {
		return getResult().getVersion();
	}
	
	private String getTerritory() {
		return getResult().getTerritory();
	}
	
	private Date getIssueDate() {
		return getResult().getIssueDate();
	}
	
	private Date getNextUpdateDate() {
		return getResult().getNextUpdateDate();
	}
	
	private List<String> getDistributionPoints() {
		return getResult().getDistributionPoints();
	}

	private List<String> getStructureValidationMessages() {
		return getResult().getStructureValidationMessages();
	}
	
	private List<TrustedEntity> getTrustedEntities() {
		ParsingResult result = getResult();
		return result.getTrustedEntities();
	}
	
	private List<OtherListPointer> getCurrentListPointers() {
		ParsingResult result = getResult();
		return result.getCurrentListPointers();
	}
	
	private List<OtherListPointer> getOtherListPointers() {
		ParsingResult result = getResult();
		return result.getOtherListPointers();
	}

}
