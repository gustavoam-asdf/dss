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
package eu.europa.esig.dss.eaa.revocation.source;

import eu.europa.esig.dss.eaa.revocation.validation.EAARevocationValidator;
import eu.europa.esig.dss.enumerations.EAARevocationOrigin;
import eu.europa.esig.dss.model.DSSDocument;
import eu.europa.esig.dss.spi.DSSUtils;
import eu.europa.esig.dss.spi.eaa.EAA;
import eu.europa.esig.dss.spi.eaa.EAARevocationToken;
import eu.europa.esig.dss.spi.eaa.EAARevocationTokenBinary;
import eu.europa.esig.dss.spi.eaa.status.EAARevocationSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.InputStream;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.ServiceLoader;
import java.util.Set;

/**
 * This class provides an EAA revocation source based on extracted EAA revocation documents
 *
 */
public class ExternalResourcesEAARevocationSource implements EAARevocationSource {

    private static final Logger LOG = LoggerFactory.getLogger(ExternalResourcesEAARevocationSource.class);

    /** List of EAA revocation binaries */
    private final Set<EAARevocationTokenBinary> eaaRevocationBinaries = new HashSet<>();

    /** Cached map of EAAs and related revocation tokens */
    private final Map<String, EAARevocationToken> eaaRevocationTokens = new HashMap<>();

    /**
     * This constructor allows building of an EAA revocation source from an array of resource paths.
     *
     * @param paths
     *            paths to EAA revocation token documents
     */
    public ExternalResourcesEAARevocationSource(final String... paths) {
        for (final String pathItem : paths) {
            addEAARevocationDocument(getClass().getResourceAsStream(pathItem));
        }
    }

    /**
     * This constructor allows building of a CRL source from an array of <code>InputStream</code>s.
     *
     * @param inputStreams
     *            an array of <code>InputStream</code>s
     */
    public ExternalResourcesEAARevocationSource(final InputStream... inputStreams) {
        for (final InputStream inputStream : inputStreams) {
            addEAARevocationDocument(inputStream);
        }
    }

    /**
     * This constructor allows building of a CRL source from an array of <code>DSSDocument</code>s.
     *
     * @param dssDocuments
     *            an array of <code>DSSDocument</code>s
     */
    public ExternalResourcesEAARevocationSource(final DSSDocument... dssDocuments) {
        for (final DSSDocument document : dssDocuments) {
            addEAARevocationDocument(document.openStream());
        }
    }

    /**
     * This constructor allows building of a CRL source from an array of byte arrays.
     *
     * @param eaaBinaries
     *            an array of byte arrays
     */
    public ExternalResourcesEAARevocationSource(final byte[]... eaaBinaries) {
        for (final byte[] binaries : eaaBinaries) {
            addEAARevocationDocument(binaries);
        }
    }

    /**
     * Adds {@code inputStream} to the cached list of EAA revocation token binaries
     *
     * @param inputStream {@link InputStream} containing an EAA revocation token
     */
    protected void addEAARevocationDocument(InputStream inputStream) {
        addEAARevocationDocument(DSSUtils.toByteArray(inputStream));
    }

    /**
     * Adds {@code eaaBinaries} to the cached list of EAA revocation token binaries
     *
     * @param eaaBinaries byte array containing an EAA revocation token
     */
    protected void addEAARevocationDocument(byte[] eaaBinaries) {
        eaaRevocationBinaries.add(new EAARevocationTokenBinary(eaaBinaries));
    }

    @Override
    public EAARevocationToken getEAARevocation(EAA eaa) {
        Objects.requireNonNull(eaa, "EAA cannot be null!");

        EAARevocationToken eaaRevocationToken = getCachedEAARevocationToken(eaa);
        if (eaaRevocationToken != null) {
            return eaaRevocationToken;
        }

        EAARevocationValidator validator = getValidator(eaa);
        if (validator != null) {
            eaaRevocationToken = validate(eaa, validator);
        }
        return eaaRevocationToken;
    }

    /**
     * Gets a {@code EAARevocationToken} for the given {@code EAA} from a list of pre-processed tokens, when applicable
     *
     * @param eaa {@link EAA}
     * @return {@link EAARevocationToken}
     */
    protected EAARevocationToken getCachedEAARevocationToken(EAA eaa) {
        return eaaRevocationTokens.get(eaa.getId());
    }

    /**
     * Loads a relevant {@code EAARevocationValidator} for revocation status verification of the {@code eaa}
     *
     * @param eaa {@link EAA} to be verified
     * @return {@link EAARevocationValidator}
     */
    protected EAARevocationValidator getValidator(EAA eaa) {
        ServiceLoader<EAARevocationValidator> loader = ServiceLoader.load(EAARevocationValidator.class);
        Iterator<EAARevocationValidator> validatorOptions = loader.iterator();

        if (validatorOptions.hasNext()) {
            for (EAARevocationValidator validator : loader) {
                if (validator.isSupported(eaa)) {
                    return validator;
                }
            }
        }
        LOG.warn("No supported EAA revocation claim has been found. EAA revocation request won't be performed.");
        return null;
    }

    /**
     * Validates the {@code EAA} across a provided list of EAA revocation binaries.
     * This method uses a subject name to identify a matching revocation token
     *
     * @param eaa {@link EAA}
     * @param validator {@link EAARevocationValidator}
     * @return {@link EAARevocationToken}
     */
    protected EAARevocationToken validate(EAA eaa, EAARevocationValidator validator) {
        List<String> uris = validator.getUris(eaa);
        for (EAARevocationTokenBinary revocationTokenBinary : eaaRevocationBinaries) {
            try {
                EAARevocationToken eaaRevocationToken = validator.validate(eaa, revocationTokenBinary.getBinaries());
                if (eaaRevocationToken != null && uris.contains(eaaRevocationToken.getSubject())) {
                    eaaRevocationToken.setOrigin(EAARevocationOrigin.EXTERNAL);
                    eaaRevocationToken.setSourceURL(eaaRevocationToken.getSubject());
                    eaaRevocationToken.setRelatedEAA(eaa);
                    eaaRevocationTokens.put(eaa.getId(), eaaRevocationToken);
                    return eaaRevocationToken;
                }
            } catch (Exception e) {
                // skip silently
            }
        }
        return null;
    }

}
