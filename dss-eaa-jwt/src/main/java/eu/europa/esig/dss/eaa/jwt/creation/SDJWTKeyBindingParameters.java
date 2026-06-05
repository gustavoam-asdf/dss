package eu.europa.esig.dss.eaa.jwt.creation;

import java.util.Date;

import eu.europa.esig.dss.eaa.common.creation.KeyBindingParameters;
import eu.europa.esig.dss.enumerations.DigestAlgorithm;

/**
 * Implementation of {@link KeyBindingParameters} for SD-JWT EAA
 */
public class SDJWTKeyBindingParameters implements KeyBindingParameters {

    /** DigestAlgorithm used to compute the hash for the key binding signature, it should the same value as the digest algorithm of the EAA */
    private DigestAlgorithm digestAlgorithm = DigestAlgorithm.SHA256;

    /** Issuance time of the key binding signature */
    private Date issuanceTime;

    /** Intended receiver of the key binding */
    private String audience;

    /** Nonce of the key binding */
    private String nonce;

    public DigestAlgorithm getDigestAlgorithm() {
        return digestAlgorithm;
    }

    public void setDigestAlgorithm(final DigestAlgorithm digestAlgorithm) {
        this.digestAlgorithm = digestAlgorithm;
    }

    public Date getIssuanceTime() {
        return issuanceTime;
    }

    public void setIssuanceTime(final Date issuanceTime) {
        this.issuanceTime = issuanceTime;
    }

    public String getAudience() {
        return audience;
    }

    public void setAudience(final String audience) {
        this.audience = audience;
    }

    public String getNonce() {
        return nonce;
    }

    public void setNonce(final String nonce) {
        this.nonce = nonce;
    }
}
