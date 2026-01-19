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

import eu.europa.esig.dss.model.http.ResponseEnvelope;
import eu.europa.esig.dss.model.x509.CertificateToken;
import eu.europa.esig.dss.spi.DSSUtils;
import eu.europa.esig.dss.spi.client.http.Protocol;
import eu.europa.esig.dss.utils.Utils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;
import java.security.cert.Certificate;
import java.security.cert.CertificateEncodingException;
import java.util.ArrayList;
import java.util.List;

/**
 * The data loader which includes server webpage certificates to the response context
 * Use the method getCertificates(url) to extract the data
 *
 * @deprecated since DSS 6.4. Please see {@code QWACValidator} class instead.
 */
@Deprecated
public class SSLCertificateLoader implements Serializable {

    private static final long serialVersionUID = -2560386894555266018L;

    private static final Logger LOG = LoggerFactory.getLogger(SSLCertificateLoader.class);

    /** A proxied CommonsDataLoader to be used */
    private CommonsDataLoader commonsDataLoader;

    /**
     * Default constructor with null CommonsDataLoader
     *
     * @deprecated since DSS 6.4
     */
    @Deprecated
    public SSLCertificateLoader() {
        // empty
    }

    /**
     * Allows to set a pre-configured CommonsDataLoader
     *
     * @param commonsDataLoader {@link CommonsDataLoader} to use
     */
    public void setCommonsDataLoader(CommonsDataLoader commonsDataLoader) {
        this.commonsDataLoader = commonsDataLoader;
    }

    /**
     * Gets the {@code CommonsDataLoader} implementation
     *
     * @return {@link CommonsDataLoader}
     */
    protected CommonsDataLoader getCommonsDataLoader() {
        if (commonsDataLoader == null) {
            commonsDataLoader = new CommonsDataLoader();
        }
        return commonsDataLoader;
    }

    /**
     * The method to extract SSL-certificates from the given web page
     *
     * @param urlString {@link String} representing a URL of a webpage with a secure connection (HTTPS)
     * @return a {@link CertificateToken} chain of the secure webpage
     * @deprecated since DSS 6.4. Please see {@code AdvancedDataLoader#requestGet(urlString, false).getTLSCertificates()}.
     */
    @Deprecated
    public List<CertificateToken> getCertificates(final String urlString) {
        final String trimmedUrl = Utils.trim(urlString);
        if (Protocol.isHttpUrl(trimmedUrl)) {
            ResponseEnvelope responseEnvelope = getCommonsDataLoader().requestGet(trimmedUrl, true, false);
            Certificate[] tlsCertificates = responseEnvelope.getTLSCertificates();
            return toCertificateTokens(tlsCertificates);
        }
        throw new UnsupportedOperationException(String.format(
                "DSS framework supports only HTTP(S) certificate extraction. Obtained URL : '%s'", urlString));
    }

    /**
     * This method converts an array of {@code java.security.cert.Certificate}s to
     * a list of {@code eu.europa.esig.dss.model.x509.CertificateToken}s
     *
     * @param certificates an array of {@code Certificate} to convert
     * @return a list of {@link CertificateToken}s
     */
    protected List<CertificateToken> toCertificateTokens(Certificate[] certificates) {
        List<CertificateToken> certificateTokens = new ArrayList<>();
        for (Certificate certificate : certificates) {
            try {
                certificateTokens.add(DSSUtils.loadCertificate(certificate.getEncoded()));
            } catch (CertificateEncodingException e) {
                LOG.warn("Cannot read and/or create an instance of a CertificateToken for a certificate : '{}'. "
                        + "The entry is skipped. Reason : {}", certificate, e.getMessage());
            }
        }
        return certificateTokens;
    }


}
