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
package eu.europa.esig.dss.lote.source;

import eu.europa.esig.dss.lote.cache.CacheKey;
import eu.europa.esig.dss.model.lote.OtherListPointer;
import eu.europa.esig.dss.model.lote.ServiceStatusAndInformationExtensions;
import eu.europa.esig.dss.model.lote.TrustedEntity;
import eu.europa.esig.dss.model.lote.TrustedEntityService;
import eu.europa.esig.dss.spi.x509.CertificateSource;

import java.util.Objects;
import java.util.function.Predicate;

/**
 * Represent a List source definition
 *
 */
public class ListSource {

	/**
	 * URL
	 */
	private String url;

	/**
	 * Signing certificates for the current TL
	 */
	private CertificateSource certificateSource;

	/**
	 * Allow to filter the collected trust entities with a predicate
	 * <p>
	 * Default : all trust entities are selected
	 */
	private Predicate<TrustedEntity> trustedEntityPredicate;

	/**
	 * Allow to filter the collected trusted service(s) with a predicate
	 * <p>
	 * Default : all trusted services are selected
	 */
	private Predicate<TrustedEntityService> trustedServicePredicate;

	/**
	 * Defines whether an SDI can be considered as a trust anchor during the given period of time
	 */
	private Predicate<ServiceStatusAndInformationExtensions> trustAnchorValidityPredicate;

	/**
	 * Allows specifying pointers to other lists to be extracted during the parsing process
	 */
	private Predicate<OtherListPointer> otherListPointerPredicate;
	
	/**
	 * The cached CacheKey value (the key is computed from url parameter)
	 */
	private CacheKey cacheKey;

	/**
	 * Default constructor instantiating object with null values
	 */
	public ListSource() {
		// empty
	}

	/**
	 * Gets the TL URL
	 *
	 * @return {@link String}
	 */
	public String getUrl() {
		return url;
	}

	/**
	 * Sets the TL access URL
	 *
	 * @param url {@link String}
	 */
	public void setUrl(String url) {
		Objects.requireNonNull(url, "URL cannot be null.");
		this.url = url;
	}

	/**
	 * Gets the certificate source to be used for TL validation
	 *
	 * @return {@link CertificateSource}
	 */
	public CertificateSource getCertificateSource() {
		return certificateSource;
	}

	/**
	 * Sets the certificate source to be used for TL validation
	 *
	 * @param certificateSource {@link CertificateSource}
	 */
	public void setCertificateSource(CertificateSource certificateSource) {
		Objects.requireNonNull(certificateSource);
		this.certificateSource = certificateSource;
	}

	/**
	 * Gets a predicate to filter Trusted Entities
	 *
	 * @return {@link Predicate}
	 */
	public Predicate<TrustedEntity> getTrustedEntityPredicate() {
		return trustedEntityPredicate;
	}

	/**
	 * Sets a  predicate to filter Trusted Entities
	 *
	 * @param trustedEntityPredicate {@link Predicate}
	 */
	public void setTrustedEntityPredicate(Predicate<TrustedEntity> trustedEntityPredicate) {
		this.trustedEntityPredicate = trustedEntityPredicate;
	}

	/**
	 * Gets a predicate to filter TrustedEntityServices
	 *
	 * @return {@link Predicate}
	 */
	public Predicate<TrustedEntityService> getTrustedServicePredicate() {
		return trustedServicePredicate;
	}

	/**
	 * Sets a  predicate to filter TrustedEntityServices
	 *
	 * @param trustedServicePredicate {@link Predicate}
	 */
	public void setTrustedServicePredicate(Predicate<TrustedEntityService> trustedServicePredicate) {
		this.trustedServicePredicate = trustedServicePredicate;
	}

	/**
	 * Gets a predicate for filtering {@code TrustServiceStatusAndInformationExtensions} in order to define
	 * an acceptability period of a corresponding SDI as a trust anchor.
	 *
	 * @return trust anchor validity predicate
	 */
	public Predicate<ServiceStatusAndInformationExtensions> getTrustAnchorValidityPredicate() {
		return trustAnchorValidityPredicate;
	}

	/**
	 * Sets a predicate allowing to filter {@code ServiceStatusAndInformationExtensions} in order to define
	 * an acceptability period of a corresponding SDI as a trust anchor.
	 * If the predicate is defined and condition fails, the SDI will not be treated as a trust anchor
	 * during the validation process.
	 *
	 * @param trustAnchorValidityPredicate trust anchor validity predicate
	 */
	public void setTrustAnchorValidityPredicate(Predicate<ServiceStatusAndInformationExtensions> trustAnchorValidityPredicate) {
		this.trustAnchorValidityPredicate = trustAnchorValidityPredicate;
	}

	/**
	 * Gets a predicate to filter {@code OtherListPointer} in order to extract pointers to other Lists
	 *
	 * @return other lists pointer predicate
	 */
	public Predicate<OtherListPointer> getOtherListPointerPredicate() {
		return otherListPointerPredicate;
	}

	/**
	 * Sets a predicate allowing to filter {@code OtherListPointer} in order to extract pointers to other Lists,
	 * to be used for further processing (for instance, pointers to other LoTEs from a LoLoTE).
	 *
	 * @param otherListPointerPredicate other lists pointer predicate
	 */
	public void setOtherListPointerPredicate(Predicate<OtherListPointer> otherListPointerPredicate) {
		this.otherListPointerPredicate = otherListPointerPredicate;
	}

	/**
	 * Gets the TL cache key
	 *
	 * @return {@link CacheKey}
	 */
	public CacheKey getCacheKey() {
		if (cacheKey == null) {
			cacheKey = new CacheKey(url);
		}
		return cacheKey;
	}

}
