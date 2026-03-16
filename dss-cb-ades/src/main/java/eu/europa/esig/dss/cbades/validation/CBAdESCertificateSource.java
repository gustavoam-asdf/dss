package eu.europa.esig.dss.cbades.validation;

import eu.europa.esig.dss.cbades.CBAdESUtils;
import eu.europa.esig.dss.cbades.COSEConstants;
import eu.europa.esig.dss.cbades.cbor.CBORArray;
import eu.europa.esig.dss.cbades.cbor.CBORByteString;
import eu.europa.esig.dss.cbades.cbor.CBORMap;
import eu.europa.esig.dss.cbades.cbor.CBORObject;
import eu.europa.esig.dss.cbades.cbor.CBORUtils;
import eu.europa.esig.dss.enumerations.CertificateOrigin;
import eu.europa.esig.dss.enumerations.CertificateRefOrigin;
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

    /** Represents the unsigned 'uHeaders' header */
    private final transient CBAdESUHeaders uHeaders;

    /** Map of 'x5u' certificates, when present */
    private final Map<String, Collection<CertificateToken>> x509UrlMap = new HashMap<>();

    /**
     * Default constructor
     *
     * @param cose {@link CBORSignature} signature to extract certificate values from
     * @param uHeaders {@link CBAdESUHeaders} containing the unsigned properties of the signature
     */
    public CBAdESCertificateSource(final CBORSignature cose, final CBAdESUHeaders uHeaders) {
        Objects.requireNonNull(cose, "CBOR signature cannot be null");

        this.cose = cose;
        this.uHeaders = uHeaders;

        // signing certificate
        extractX5T();
        extractX5Ts();
        extractKid();
        extractX509Url();

        // certificate chain
        extractX5Bag();
        extractX5Chain();

        extractUHeaders();
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
        Digest digest = CBAdESUtils.extractX5TDigest(x5t);
        if (digest != null) {
            CertificateRef certRef = new CertificateRef();
            certRef.setCertDigest(digest);
            addCertificateRef(certRef, CertificateRefOrigin.SIGNING_CERTIFICATE);
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

    private void extractUHeaders() {
        if (uHeaders == null || !uHeaders.isExist()) {
            return;
        }

        for (CBAdESAttribute attribute : uHeaders.getAttributes()) {
            extractValidationData(attribute);
            extractCompleteCertificateRefs(attribute);
        }
    }

    private void extractValidationData(CBAdESAttribute attribute) {
        if (COSEConstants.VAL_DATA == attribute.getHeaderId()) {
            CBORObject valData = attribute.getValue();
            if (valData.isMap()) {
                CBORMap valDataMap = (CBORMap) valData;
                CBORArray xVals = valDataMap.getAsArray(COSEConstants.VAL_DATA_X_VALS);
                if (xVals != null && !xVals.isEmpty()) {
                    extractCertificateValues(xVals, CertificateOrigin.ANY_VALIDATION_DATA);
                }
            } else {
                LOG.warn("The value of header 'valData' shall be represented by a CBOR Map! Entry is skilled.");
            }
        }
    }

    private void extractCertificateValues(CBORArray xVals, CertificateOrigin origin) {
        for (CBORObject item : xVals.getItems()) {
            if (item.isMap()) {
                CBORMap x509OrOther = (CBORMap) item;

                CBORMap pkiOb = x509OrOther.getAsMap(COSEConstants.X509_OR_OTHER_X509_CERT);
                extractX509Cert(pkiOb, origin);

                CBORMap otherCert = x509OrOther.getAsMap(COSEConstants.X509_OR_OTHER_OTHER_CERT);
                if (otherCert != null) {
                    LOG.warn("The header 'otherCert' is not supported! The entry is skipped.");
                }
            } else {
                LOG.warn("The value of 'x509OrOther' shall be represented by a CBOR Map! Entry is skilled.");
            }
        }
    }

    private void extractX509Cert(CBORMap pkiOb, CertificateOrigin origin) {
        byte[] val = CBAdESUtils.extractDerEncodedPkiObject(pkiOb);
        if (Utils.isArrayNotEmpty(val)) {
            CertificateToken certificateToken = loadCertificate(val);
            addCertificate(certificateToken, origin);
        }
    }

    private void extractCompleteCertificateRefs(CBAdESAttribute attribute) {
        if (COSEConstants.REFS == attribute.getHeaderId()) {
            CBORObject refs = attribute.getValue();
            if (refs.isMap()) {
                CBORMap refsMap = (CBORMap) refs;
                CBORArray xRefs = refsMap.getAsArray(COSEConstants.REFS_X_REFS);
                if (xRefs != null && !xRefs.isEmpty()) {
                    extractCertificateRefs(xRefs, CertificateRefOrigin.COMPLETE_CERTIFICATE_REFS);
                }
            } else {
                LOG.warn("The value of header 'refs' shall be represented by a CBOR Map! Entry is skilled.");
            }
        }
    }

    private void extractCertificateRefs(CBORArray xRefs, CertificateRefOrigin origin) {
        for (CBORObject item : xRefs.getItems()) {
            if (item.isMap()) {
                CBORMap certId = (CBORMap) item;
                CertificateRef certificateRef = CBAdESUtils.fromCertId(certId);
                if (certificateRef != null) {
                    addCertificateRef(certificateRef, origin);
                }
            } else {
                LOG.warn("The value of 'CertId' shall be represented by a CBOR Map! Entry is skilled.");
            }
        }
    }

    @Override
    protected CandidatesForSigningCertificate extractCandidatesForSigningCertificate(
            CertificateSource signingCertificateSource) {
        CandidatesForSigningCertificate candidatesForSigningCertificate = new CandidatesForSigningCertificate();

        for (final CertificateToken certificateToken : getKeyInfoCertificates()) {
            candidatesForSigningCertificate.add(new CertificateValidity(certificateToken));
        }

        // if x5chain does not contain certificates,
        // check other certificates embedded into the signature
        if (candidatesForSigningCertificate.isEmpty()) {

            // Add all found certificates
            for (final CertificateToken certificateToken : getCertificates()) {
                candidatesForSigningCertificate.add(new CertificateValidity(certificateToken));
            }

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

    @Override
    public List<CertificateRef> getReferencesForCertificateToken(CertificateToken certificateToken) {
        final List<CertificateRef> result = super.getReferencesForCertificateToken(certificateToken);
        for (Map.Entry<String, Collection<CertificateToken>> x5uEntry : x509UrlMap.entrySet()) {
            if (x5uEntry.getValue().contains(certificateToken)) {
                for (CertificateRef certificateRef : getCertificateRefsByOrigin(CertificateRefOrigin.X509_URL)) {
                    if (x5uEntry.getKey().equals(certificateRef.getX509Url())) {
                        result.add(certificateRef);
                    }
                }
            }
        }
        return result;
    }

    @Override
    public Set<CertificateToken> findTokensFromCertRef(CertificateRef certificateRef) {
        final Set<CertificateToken> certificates = super.findTokensFromCertRef(certificateRef);
        if (Utils.isStringNotEmpty(certificateRef.getX509Url())) {
            Collection<CertificateToken> x509UrlCertificates = x509UrlMap.get(certificateRef.getX509Url());
            if (Utils.isCollectionNotEmpty(x509UrlCertificates)) {
                certificates.addAll(x509UrlCertificates);
            }
        }
        return certificates;
    }

}
