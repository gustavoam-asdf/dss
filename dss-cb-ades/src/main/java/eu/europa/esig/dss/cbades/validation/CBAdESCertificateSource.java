package eu.europa.esig.dss.cbades.validation;

import eu.europa.esig.dss.cbades.COSEConstants;
import eu.europa.esig.dss.cbades.cbor.CBORArray;
import eu.europa.esig.dss.cbades.cbor.CBORByteString;
import eu.europa.esig.dss.cbades.cbor.CBORObject;
import eu.europa.esig.dss.cbades.cbor.CBORUtils;
import eu.europa.esig.dss.enumerations.CertificateOrigin;
import eu.europa.esig.dss.enumerations.CertificateRefOrigin;
import eu.europa.esig.dss.enumerations.DigestAlgorithm;
import eu.europa.esig.dss.model.Digest;
import eu.europa.esig.dss.model.x509.CertificateToken;
import eu.europa.esig.dss.spi.DSSASN1Utils;
import eu.europa.esig.dss.spi.DSSUtils;
import eu.europa.esig.dss.spi.SignatureCertificateSource;
import eu.europa.esig.dss.spi.x509.CandidatesForSigningCertificate;
import eu.europa.esig.dss.spi.x509.CertificateRef;
import eu.europa.esig.dss.spi.x509.CertificateSource;
import eu.europa.esig.dss.spi.x509.CertificateValidity;
import eu.europa.esig.dss.spi.x509.KidCertificateSource;
import eu.europa.esig.dss.spi.x509.X509URLCertificateSource;
import eu.europa.esig.dss.utils.Utils;
import org.bouncycastle.asn1.x509.IssuerSerial;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

/**
 * Extracts and stores certificates from a CB-AdES signature
 *
 */
public class CBAdESCertificateSource extends SignatureCertificateSource {

    private static final long serialVersionUID = -8170607661341382049L;

    private static final Logger LOG = LoggerFactory.getLogger(CBAdESCertificateSource.class);

    /** The COSE Signature to extract certificates from */
    private final transient CBORSignature cose;

    /** Map of 'x5u' certificates, when present */
    private final Map<String, Collection<CertificateToken>> x509UrlMap = new HashMap<>();

    /**
     * Default constructor
     *
     * @param cose {@link CBORSignature} signature to extract certificate values from
     */
    public CBAdESCertificateSource(CBORSignature cose) {
        Objects.requireNonNull(cose, "CBOR signature cannot be null");

        this.cose = cose;

        // signing certificate
        extractX5T();
        extractX5Ts();
        extractKid();
        extractX509Url();

        // certificate chain
        extractX5Bag();
        extractX5Chain();

        // TODO : unsigned properties
        // extractUHeaders();
    }

    /**
     * Retrieves the list of {@link CertificateRef}s referenced within a 'kid' (key identifier) header
     *
     * @return the list of references to the signing certificate (from key identifier)
     */
    public List<CertificateRef> getKeyIdentifierCertificateRefs() {
        return getCertificateRefsByOrigin(CertificateRefOrigin.KEY_IDENTIFIER);
    }

    /**
     * Retrieves the Set of {@link CertificateToken}s according to a reference present
     * within a 'kid' (key identifier) header
     *
     * @return Set of {@link CertificateToken}s
     */
    public Set<CertificateToken> getKeyIdentifierCertificates() {
        return findTokensFromRefs(getKeyIdentifierCertificateRefs());
    }

    private void extractX5T() {
        /*
         * x5t: This header parameter identifies the end-entity X.509
         * certificate by a hash value (a thumbprint). The 'x5t' header
         * parameter is represented as an array of two elements.  The first
         * element is an algorithm identifier that is an integer or a string
         * containing the hash algorithm identifier corresponding to the
         * Value column (integer or text string) of the algorithm registered
         * in the "COSE Algorithms" registry (see
         * <https://www.iana.org/assignments/cose/>). The second element is
         * a binary string containing the hash value computed over the DER-
         * encoded certificate.
         */
        CBORArray x5t = cose.getProtectedHeaderValueAsArray(COSEConstants.X5T);
        extractX5T(x5t);
    }

    private void extractX5T(CBORArray x5t) {
        if (x5t != null) {
            if (x5t.getSize() == 2) {
                Long hashAlgId = x5t.getAsLongOrString(0);
                DigestAlgorithm hashAlg = CBORUtils.getDigestAlgorithmForCoseId(hashAlgId);
                byte[] hashValue = x5t.getAsBinaries(1);
                if (hashAlg != null && hashValue != null) {
                    CertificateRef certRef = new CertificateRef();
                    certRef.setCertDigest(new Digest(hashAlg, hashValue));
                    addCertificateRef(certRef, CertificateRefOrigin.SIGNING_CERTIFICATE);

                } else {
                    LOG.warn("'x5t' header array members have invalid structure!");
                }

            } else {
                LOG.warn("'x5t' header array shall have two entries!");
            }
        }
    }

    private void extractX509Url() {
        /*
         * x5u: This header parameter provides the ability to identify an X.509
         * certificate by a URI [RFC3986]. It contains a CBOR text string.
         */
        String x5u = getX5uValue();
        if (Utils.isStringNotEmpty(x5u)) {
            CertificateRef certificateRef = new CertificateRef();
            certificateRef.setX509Url(x5u);
            addCertificateRef(certificateRef, CertificateRefOrigin.X509_URL);
        }
    }

    private String getX5uValue() {
        return cose.getProtectedHeaderValueAsString(COSEConstants.X5U);
    }

    private void extractX5Bag() {
        byte[] x5bagEntry = cose.getProtectedHeaderValueAsBinaries(COSEConstants.X5BAG);
        CBORArray x5bagArray = cose.getProtectedHeaderValueAsArray(COSEConstants.X5BAG);
        if (x5bagEntry != null) {
            CertificateToken certificate = loadCertificate(x5bagEntry);
            if (certificate != null) {
                addCertificate(certificate, CertificateOrigin.KEY_INFO);
            }

        } else if (x5bagArray != null) {
            for (CBORObject cborObject : x5bagArray.getItems()) {
                if (cborObject.isByteString()) {
                    CertificateToken certificate = loadCertificate(((CBORByteString) cborObject).getBytes());
                    if (certificate != null) {
                        addCertificate(certificate, CertificateOrigin.KEY_INFO);
                    }
                } else {
                    LOG.warn("The item of 'x5bag' CBOR array shall be a byte string!");
                }
            }

        }
    }

    private void extractX5Chain() {
        byte[] x5chainEntry = cose.getProtectedHeaderValueAsBinaries(COSEConstants.X5CHAIN);
        CBORArray x5chainArray = cose.getProtectedHeaderValueAsArray(COSEConstants.X5CHAIN);
        if (x5chainEntry != null) {
            CertificateToken certificate = loadCertificate(x5chainEntry);
            if (certificate != null) {
                addCertificate(certificate, CertificateOrigin.KEY_INFO);
            }

        } else if (x5chainArray != null) {
            for (CBORObject cborObject : x5chainArray.getItems()) {
                if (cborObject.isByteString()) {
                    CertificateToken certificate = loadCertificate(((CBORByteString) cborObject).getBytes());
                    if (certificate != null) {
                        addCertificate(certificate, CertificateOrigin.KEY_INFO);
                    }
                } else {
                    LOG.warn("The item of 'x5chain' CBOR array shall be a byte string!");
                }
            }

        }
    }

    private void extractKid() {
        byte[] kid = getKidValue();
        if (kid != null) {
            IssuerSerial kidIssuerSerial = CBORUtils.getIssuerSerial(kid);
            if (kidIssuerSerial != null) {
                CertificateRef certificateRef = new CertificateRef();
                certificateRef.setCertificateIdentifier(DSSASN1Utils.toSignerIdentifier(kidIssuerSerial));
                addCertificateRef(certificateRef, CertificateRefOrigin.KEY_IDENTIFIER);
            }
        }
    }

    private byte[] getKidValue() {
        return cose.getProtectedHeaderValueAsBinaries(COSEConstants.KID);
    }

    private void extractX5Ts() {
        CBORArray x5ts = cose.getProtectedHeaderValueAsArray(COSEConstants.X5TS);
        if (x5ts != null && !x5ts.isEmpty()) {
            for (CBORObject item : x5ts.getItems()) {
                if (item.isArray()) {
                    extractX5T((CBORArray) item);
                } else {
                    LOG.warn("The entry of 'x5ts' CBOR array shall be a CBOR array!");
                }
            }
        }
    }

    private CertificateToken loadCertificate(byte[] binaries) {
        try {
            return DSSUtils.loadCertificate(binaries);
        } catch (Exception e) {
            LOG.warn("Unable to decode a certificate from binaries! Reason : {}", e.getMessage(), e);
            return null;
        }
    }

    @Override
    protected CandidatesForSigningCertificate extractCandidatesForSigningCertificate(
            CertificateSource signingCertificateSource) {
        CandidatesForSigningCertificate candidatesForSigningCertificate = new CandidatesForSigningCertificate();

        for (final CertificateToken certificateToken : getKeyInfoCertificates()) {
            candidatesForSigningCertificate.add(new CertificateValidity(certificateToken));
        }

        if (signingCertificateSource != null) {
            resolveFromSource(signingCertificateSource, candidatesForSigningCertificate);
        }

        checkSigningCertificateRef(candidatesForSigningCertificate);

        return candidatesForSigningCertificate;
    }

    private void resolveFromSource(CertificateSource signingCertificateSource, CandidatesForSigningCertificate candidatesForSigningCertificate) {
        CertificateToken kidCandidate = resolveByKid(signingCertificateSource);
        if (kidCandidate != null) {
            LOG.debug("Resolved certificate by kid");
            super.addCertificate(kidCandidate);
            candidatesForSigningCertificate.add(new CertificateValidity(kidCandidate));
            return;
        }

        Collection<CertificateToken> uriCandidates = resolveByUri(signingCertificateSource);
        if (Utils.isCollectionNotEmpty(uriCandidates)) {
            LOG.debug("Resolved certificates by x5u");
            for (CertificateToken externalCandidate : uriCandidates) {
                super.addCertificate(externalCandidate);
                candidatesForSigningCertificate.add(new CertificateValidity(externalCandidate));
            }
            return;
        }

        Digest certificateDigest = getSigningCertificateDigest();
        if (certificateDigest != null) {
            Set<CertificateToken> certificatesByDigest = signingCertificateSource.getByCertificateDigest(certificateDigest);
            if (Utils.isCollectionNotEmpty(certificatesByDigest)) {
                LOG.debug("Resolved certificate by digest");
                for (CertificateToken certificateToken : certificatesByDigest) {
                    candidatesForSigningCertificate.add(new CertificateValidity(certificateToken));
                }
            }

        } else {
            List<CertificateToken> certificates = signingCertificateSource.getCertificates();
            LOG.debug("No signing certificate reference found. " +
                    "Resolve all {} certificates from the provided certificate source as signing candidates.", certificates.size());
            for (CertificateToken certCandidate : certificates) {
                candidatesForSigningCertificate.add(new CertificateValidity(certCandidate));
            }
        }
    }

    private CertificateToken resolveByKid(CertificateSource signingCertificateSource) {
        byte[] kid = getKidValue();
        if (kid != null) {
            if (signingCertificateSource instanceof KidCertificateSource) {
                KidCertificateSource kidCertificateSource = (KidCertificateSource) signingCertificateSource;
                return kidCertificateSource.getCertificateByKid(kid);
            } else {
                LOG.info("COSE/CB-AdES contains a 'kid' header (provide a KidCertificateSource to resolve it)");
            }
        }
        return null;
    }

    private Collection<CertificateToken> resolveByUri(CertificateSource signingCertificateSource) {
        String x5uHeader = getX5uValue();
        if (Utils.isStringNotEmpty(x5uHeader)) {
            if (signingCertificateSource instanceof X509URLCertificateSource) {
                X509URLCertificateSource x509URLCertificateSource = (X509URLCertificateSource) signingCertificateSource;
                Collection<CertificateToken> certificatesByUri = x509URLCertificateSource.getCertificatesByUrl(x5uHeader);
                if (Utils.isCollectionNotEmpty(certificatesByUri)) {
                    x509UrlMap.put(x5uHeader, certificatesByUri);
                }
                return certificatesByUri;
            } else {
                LOG.info("COSE/CB-AdES contains a 'x5u' header (provide a X509URLCertificateSource to resolve it)");
            }
        }
        return Collections.emptyList();
    }

    private Digest getSigningCertificateDigest() {
        List<CertificateRef> signingCertificateRefs = getSigningCertificateRefs();
        if (Utils.isCollectionNotEmpty(signingCertificateRefs)) {
            // must contain only one reference
            final CertificateRef signingCert = signingCertificateRefs.get(0);
            return signingCert.getCertDigest();
        }
        return null;
    }

    private void checkSigningCertificateRef(CandidatesForSigningCertificate candidates) {
        CertificateRef signingCertRef = null;
        final List<CertificateRef> potentialSigningCertificates = getSigningCertificateRefs();
        if (Utils.isCollectionNotEmpty(potentialSigningCertificates)) {
            // first reference shall be a reference to a signing certificate
            signingCertRef = potentialSigningCertificates.get(0);
        }

        CertificateRef kidCertRef = null;
        final List<CertificateRef> keyIdentifierCertificateRefs = getKeyIdentifierCertificateRefs();
        if (Utils.isCollectionNotEmpty(keyIdentifierCertificateRefs)) {
            kidCertRef = keyIdentifierCertificateRefs.get(0);
        }

        if (signingCertRef != null) {
            CertificateValidity bestCertificateValidity = null;
            // check all certificates against the signingCert ref and find the best one
            final List<CertificateValidity> certificateValidityList = candidates.getCertificateValidityList();
            for (final CertificateValidity certificateValidity : certificateValidityList) {
                if (isValid(certificateValidity, signingCertRef, kidCertRef)) {
                    bestCertificateValidity = certificateValidity;
                }
            }
            if (bestCertificateValidity != null) {
                candidates.setTheCertificateValidity(bestCertificateValidity);
            }
        }
    }

    private boolean isValid(CertificateValidity certificateValidity,
                            CertificateRef signingCertRef, CertificateRef kidCertRef) {
        certificateValidity.setDigestPresent(signingCertRef != null && signingCertRef.getCertDigest() != null);
        certificateValidity.setIssuerSerialPresent(kidCertRef != null && kidCertRef.getCertificateIdentifier() != null);

        CertificateToken certificateToken = certificateValidity.getCertificateToken();
        if (certificateToken != null) {
            if (signingCertRef != null) {
                certificateValidity.setDigestEqual(certificateMatcher.matchByDigest(certificateToken, signingCertRef));
            }
            if (kidCertRef != null) {
                certificateValidity.setSerialNumberEqual(certificateMatcher.matchBySerialNumber(certificateToken, kidCertRef));
                certificateValidity.setDistinguishedNameEqual(certificateMatcher.matchByIssuerName(certificateToken, kidCertRef));
            }
        }
        return certificateValidity.isValid();
    }

}
