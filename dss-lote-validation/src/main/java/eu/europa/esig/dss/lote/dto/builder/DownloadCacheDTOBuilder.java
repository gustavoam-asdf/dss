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

import eu.europa.esig.dss.lote.cache.state.CachedEntry;
import eu.europa.esig.dss.lote.download.DownloadResult;
import eu.europa.esig.dss.lote.dto.DownloadCacheDTO;
import eu.europa.esig.dss.model.DSSDocument;

/**
 * Builds {@code DownloadCacheDTO}
 */
public class DownloadCacheDTOBuilder extends AbstractCacheDTOBuilder<DownloadResult> {

	/**
	 * Default constructor
	 *
	 * @param cachedEntry download cache entry
	 */
	public DownloadCacheDTOBuilder(final CachedEntry<DownloadResult> cachedEntry) {
		super(cachedEntry);
	}
	
	@Override
	public DownloadCacheDTO build() {
		DownloadCacheDTO downloadCacheDTO = new DownloadCacheDTO(super.build());
		if (isResultExist()) {
			downloadCacheDTO.setDocument(getDocument());
		}
		return downloadCacheDTO;
	}

	private DSSDocument getDocument() {
		return getResult().getDSSDocument();
	}

}
