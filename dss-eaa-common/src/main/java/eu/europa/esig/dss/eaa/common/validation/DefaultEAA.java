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
package eu.europa.esig.dss.eaa.common.validation;

import eu.europa.esig.dss.eaa.common.validation.identifier.EAAIdentifierBuilder;
import eu.europa.esig.dss.enumerations.DigestAlgorithm;
import eu.europa.esig.dss.model.eaa.ValidationDisclosure;
import eu.europa.esig.dss.model.eaa.DisclosureValidation;
import eu.europa.esig.dss.model.eaa.claim.ClaimDeviceKey;
import eu.europa.esig.dss.model.identifier.Identifier;
import eu.europa.esig.dss.spi.eaa.EAAPayload;
import eu.europa.esig.dss.spi.eaa.EAA;
import eu.europa.esig.dss.spi.signature.AdvancedSignature;
import eu.europa.esig.dss.spi.x509.CertificateSource;
import eu.europa.esig.dss.spi.x509.ListCertificateSource;
import eu.europa.esig.dss.spi.x509.ProofOfPossessionCertificateSource;
import eu.europa.esig.dss.utils.Utils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * Abstract implementation of an EAA
 *
 */
public abstract class DefaultEAA implements EAA {

    /** Cached signature objects used to create the EAA */
    private List<AdvancedSignature> signatures;

    /** List of disclosures attached to the EAA Presentation */
    private List<ValidationDisclosure> disclosures;

    /** Key binding signature (optional) */
    private AdvancedSignature keyBindingSignature;

    /** The name of the EAA document */
    private String filename;

    /** Unique EAA identifier */
    private Identifier identifier;

    /** Cached instance of an EAA Payload Verifier */
    private EAAPayloadVerifier eaaPayloadVerifier;

    /**
     * Default constructor
     */
    protected DefaultEAA() {
        // empty
    }

    @Override
    public String getFilename() {
        return filename;
    }

    @Override
    public List<AdvancedSignature> getSignatures() {
        return signatures;
    }

    /**
     * Gets a list of disclosures
     *
     * @return a list of {@link ValidationDisclosure}s
     */
    public List<ValidationDisclosure> getDisclosures() {
        return disclosures;
    }

    @Override
    public List<DisclosureValidation> getDisclosureValidations() {
        return getEAAPayloadVerifier().getDisclosureValidations();
    }

    @Override
    public AdvancedSignature getKeyBindingSignature() {
        return keyBindingSignature;
    }

    @Override
    public EAAPayload getPayload() {
        return getEAAPayloadVerifier().getVerifiedPayload();
    }

    /**
     * Gets the EAA Payload Verifier, performing a verification of the attached disclosures as well as
     * building a constructed version of the EAA Payload with the discloses values attached
     *
     * @return {@link EAAPayloadVerifier}
     */
    protected EAAPayloadVerifier getEAAPayloadVerifier() {
        if (eaaPayloadVerifier == null) {
            eaaPayloadVerifier = initEAAPayloadVerifier().setDisclosures(disclosures);
            eaaPayloadVerifier.verify();
        }
        return eaaPayloadVerifier;
    }

    /**
     * Creates a new instance of {@code EAAPayloadVerifier} relatively to the current implementation
     *
     * @return {@link EAAPayloadVerifier}
     */
    protected abstract EAAPayloadVerifier initEAAPayloadVerifier();

    @Override
    public DigestAlgorithm getSelectiveDisclosuresDigestAlgorithm() {
        return getEAAPayloadVerifier().getDigestAlgorithm();
    }

    @Override
    public CertificateSource getDeviceKeyCertificateSource() {
        AdvancedSignature kbSignature = getKeyBindingSignature();
        if (kbSignature != null) {
            return getProofOfPossessionCertificateSource(kbSignature.getSigningCertificateSource());
        }
        return null;
    }

    private CertificateSource getProofOfPossessionCertificateSource(CertificateSource certificateSource) {
        if (certificateSource instanceof ProofOfPossessionCertificateSource) {
            return certificateSource;
        } else if (certificateSource instanceof ListCertificateSource) {
            for (CertificateSource embeddedCertSource : ((ListCertificateSource) certificateSource).getSources()) {
                CertificateSource popCertificateSource = getProofOfPossessionCertificateSource(embeddedCertSource);
                if (popCertificateSource != null) {
                    return popCertificateSource;
                }
            }
        }
        return null;
    }

    @Override
    public String getId() {
        return getDSSId().asXmlId();
    }

    @Override
    public Identifier getDSSId() {
        if (identifier == null) {
            identifier = new EAAIdentifierBuilder().build(this);
        }
        return identifier;
    }

    /**
     * This class is used to build a DefaultEAA
     *
     */
    protected static abstract class DefaultEAABuilder {

        private static final Logger LOG = LoggerFactory.getLogger(DefaultEAABuilder.class);

        /** Cached signature objects used to create the EAA */
        private List<AdvancedSignature> signatures;

        /** List of disclosures attached to the EAA Presentation */
        private List<ValidationDisclosure> disclosures;

        /** Key binding signature (optional) */
        private AdvancedSignature keyBindingSignature;

        /** The name of the EAA document */
        private String filename;

        /**
         * Default constructor
         */
        public DefaultEAABuilder() {
            // empty
        }

        /**
         * Sets signatures list used to create the EAA
         *
         * @param signatures a list of {@link AdvancedSignature}s
         * @return this builder
         */
        public DefaultEAABuilder setSignatures(List<AdvancedSignature> signatures) {
            this.signatures = signatures;
            return this;
        }

        /**
         * Sets a list of disclosures provided with the SD-JWT VC token
         *
         * @param disclosures a list of {@link ValidationDisclosure}s
         * @return this builder
         */
        public DefaultEAABuilder setDisclosures(List<ValidationDisclosure> disclosures) {
            this.disclosures = disclosures;
            return this;
        }

        /**
         * Sets the key binding signature, when present
         *
         * @param keyBindingSignature {@link AdvancedSignature}
         * @return this builder
         */
        public DefaultEAABuilder setKeyBindingSignature(AdvancedSignature keyBindingSignature) {
            this.keyBindingSignature = keyBindingSignature;
            return this;
        }

        /**
         * Sets the document filename
         *
         * @param filename {@link String}
         * @return this builder
         */
        public DefaultEAABuilder setFilename(String filename) {
            this.filename = filename;
            return this;
        }

        /**
         * Builds a new EAA object
         *
         * @return {@link DefaultEAA}
         */
        public DefaultEAA build() {
            if (Utils.isCollectionEmpty(signatures)) {
                throw new NullPointerException("Signatures list cannot be null or empty!");
            }
            DefaultEAA eaa = initEAA();
            eaa.signatures = signatures;
            for (AdvancedSignature signature : signatures) {
                signature.setEAA(eaa);
            }
            eaa.disclosures = disclosures;
            if (keyBindingSignature != null) {
                CertificateSource signingCertificateSource = new ListCertificateSource(
                        getHolderCertificateSource(eaa.getPayload()), getSigningCertificateSource(signatures));
                keyBindingSignature.setSigningCertificateSource(signingCertificateSource);
                eaa.keyBindingSignature = keyBindingSignature;
                keyBindingSignature.setEAA(eaa);
                keyBindingSignature.setKeyBindingSignature(true);
            }
            eaa.filename = filename;
            return eaa;
        }

        /**
         * Gets a certificate source containing a key of the EAA holder
         *
         * @param eaaPayload {@link EAAPayload}
         * @return {@link CertificateSource}
         */
        protected CertificateSource getHolderCertificateSource(EAAPayload eaaPayload) {
            ClaimDeviceKey claimDeviceKey = eaaPayload.getDeviceKey();
            if (claimDeviceKey != null) {
                try {
                    return new DeviceKeyClaimCertificateSource(claimDeviceKey);
                } catch (Exception e) {
                    LOG.warn("Unable to read the device key claim : {}", e.getMessage(), e);
                }
            }
            return null;
        }

        private CertificateSource getSigningCertificateSource(List<AdvancedSignature> signatures) {
            AdvancedSignature signature = signatures.get(0);
            return signature.getSigningCertificateSource();
        }

        /**
         * Instantiates a new {@code DefaultEAA} object
         *
         * @return {@link DefaultEAA}
         */
        protected abstract DefaultEAA initEAA();

    }

}
