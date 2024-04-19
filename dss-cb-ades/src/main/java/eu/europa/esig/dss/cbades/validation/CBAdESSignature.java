package eu.europa.esig.dss.cbades.validation;

import eu.europa.esig.dss.enumerations.DigestAlgorithm;
import eu.europa.esig.dss.enumerations.EncryptionAlgorithm;
import eu.europa.esig.dss.enumerations.SignatureAlgorithm;
import eu.europa.esig.dss.enumerations.SignatureForm;
import eu.europa.esig.dss.enumerations.SignatureLevel;
import eu.europa.esig.dss.model.Digest;
import eu.europa.esig.dss.model.ReferenceValidation;
import eu.europa.esig.dss.model.SignaturePolicyStore;
import eu.europa.esig.dss.model.scope.SignatureScope;
import eu.europa.esig.dss.model.signature.CommitmentTypeIndication;
import eu.europa.esig.dss.model.signature.SignatureDigestReference;
import eu.europa.esig.dss.model.signature.SignaturePolicy;
import eu.europa.esig.dss.model.signature.SignatureProductionPlace;
import eu.europa.esig.dss.model.signature.SignerRole;
import eu.europa.esig.dss.model.x509.revocation.crl.CRL;
import eu.europa.esig.dss.model.x509.revocation.ocsp.OCSP;
import eu.europa.esig.dss.spi.SignatureCertificateSource;
import eu.europa.esig.dss.spi.signature.AdvancedSignature;
import eu.europa.esig.dss.spi.signature.BaselineRequirementsChecker;
import eu.europa.esig.dss.spi.signature.DefaultAdvancedSignature;
import eu.europa.esig.dss.spi.signature.identifier.SignatureIdentifierBuilder;
import eu.europa.esig.dss.spi.validation.CertificateVerifier;
import eu.europa.esig.dss.spi.x509.revocation.OfflineRevocationSource;
import eu.europa.esig.dss.spi.x509.tsp.TimestampSource;
import eu.europa.esig.dss.spi.x509.tsp.TimestampToken;

import java.util.Collections;
import java.util.Date;
import java.util.List;

public class CBAdESSignature extends DefaultAdvancedSignature {

    @Override
    protected SignatureIdentifierBuilder getSignatureIdentifierBuilder() {
        return null;
    }

    @Override
    protected List<SignatureScope> findSignatureScopes() {
        return Collections.emptyList();
    }

    @Override
    protected SignaturePolicy buildSignaturePolicy() {
        return null;
    }

    @Override
    protected BaselineRequirementsChecker createBaselineRequirementsChecker(CertificateVerifier certificateVerifier) {
        return null;
    }

    @Override
    public SignatureForm getSignatureForm() {
        return null;
    }

    @Override
    public SignatureAlgorithm getSignatureAlgorithm() {
        return null;
    }

    @Override
    public EncryptionAlgorithm getEncryptionAlgorithm() {
        return null;
    }

    @Override
    public DigestAlgorithm getDigestAlgorithm() {
        return null;
    }

    @Override
    public Date getSigningTime() {
        return null;
    }

    @Override
    public SignatureCertificateSource getCertificateSource() {
        return null;
    }

    @Override
    public OfflineRevocationSource<CRL> getCRLSource() {
        return null;
    }

    @Override
    public OfflineRevocationSource<OCSP> getOCSPSource() {
        return null;
    }

    @Override
    public TimestampSource getTimestampSource() {
        return null;
    }

    @Override
    public void checkSignatureIntegrity() {

    }

    @Override
    public SignaturePolicyStore getSignaturePolicyStore() {
        return null;
    }

    @Override
    public SignatureProductionPlace getSignatureProductionPlace() {
        return null;
    }

    @Override
    public List<CommitmentTypeIndication> getCommitmentTypeIndications() {
        return Collections.emptyList();
    }

    @Override
    public String getContentType() {
        return "";
    }

    @Override
    public String getMimeType() {
        return "";
    }

    @Override
    public List<SignerRole> getSignedAssertions() {
        return Collections.emptyList();
    }

    @Override
    public List<SignerRole> getClaimedSignerRoles() {
        return Collections.emptyList();
    }

    @Override
    public List<SignerRole> getCertifiedSignerRoles() {
        return Collections.emptyList();
    }

    @Override
    public void addExternalTimestamp(TimestampToken timestamp) {

    }

    @Override
    public List<AdvancedSignature> getCounterSignatures() {
        return Collections.emptyList();
    }

    @Override
    public String getDAIdentifier() {
        return "";
    }

    @Override
    public SignatureLevel getDataFoundUpToLevel() {
        return null;
    }

    @Override
    public byte[] getSignatureValue() {
        return new byte[0];
    }

    @Override
    public List<ReferenceValidation> getReferenceValidations() {
        return Collections.emptyList();
    }

    @Override
    public SignatureDigestReference getSignatureDigestReference(DigestAlgorithm digestAlgorithm) {
        return null;
    }

    @Override
    public Digest getDataToBeSignedRepresentation() {
        return null;
    }
}
