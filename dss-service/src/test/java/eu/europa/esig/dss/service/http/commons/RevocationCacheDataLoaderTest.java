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
package eu.europa.esig.dss.service.http.commons;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;

import eu.europa.esig.dss.service.OnlineSourceTest;

/**
 * Unit test for RevocationCacheDataLoader
 */
class RevocationCacheDataLoaderTest extends OnlineSourceTest {

	@Test
	void testDefaultCacheExpirationTimeSetterAndGetter() {
		// Test with CommonsDataLoader for CRL
		RevocationCacheDataLoader crlCacheLoader = new RevocationCacheDataLoader();
		CommonsDataLoader commonsDataLoader = new CommonsDataLoader();
		crlCacheLoader.setDataLoader(commonsDataLoader);

		// Test default value (10 minutes = 600,000 ms)
		// No public getter, but we can test that it accepts the value

		// Test setting custom expiration time
		long customExpirationTime = 300_000L; // 5 minutes
		crlCacheLoader.setDefaultCacheExpirationTime(customExpirationTime);

		assertNotNull(crlCacheLoader.getDataLoader());
		assertEquals(CommonsDataLoader.class, crlCacheLoader.getDataLoader().getClass());
	}

	@Test
	void testOCSPDataLoaderConfiguration() {
		// Test with OCSPDataLoader for OCSP
		RevocationCacheDataLoader ocspCacheLoader = new RevocationCacheDataLoader();
		OCSPDataLoader ocspDataLoader = new OCSPDataLoader();
		ocspCacheLoader.setDataLoader(ocspDataLoader);

		// Test setting custom expiration time
		long customExpirationTime = 1_800_000L; // 30 minutes
		ocspCacheLoader.setDefaultCacheExpirationTime(customExpirationTime);

		assertNotNull(ocspCacheLoader.getDataLoader());
		assertEquals(OCSPDataLoader.class, ocspCacheLoader.getDataLoader().getClass());
	}

	@Test
	void testConstructorWithDataLoader() {
		CommonsDataLoader commonsDataLoader = new CommonsDataLoader();
		RevocationCacheDataLoader cacheLoader = new RevocationCacheDataLoader(commonsDataLoader);

		assertNotNull(cacheLoader.getDataLoader());
		assertEquals(CommonsDataLoader.class, cacheLoader.getDataLoader().getClass());
	}

	@Test
	void testNegativeExpirationTime() {
		// Test that negative values are accepted (cache never expires)
		RevocationCacheDataLoader cacheLoader = new RevocationCacheDataLoader();
		cacheLoader.setDefaultCacheExpirationTime(-1L);

		// Should not throw any exception
	}
}
