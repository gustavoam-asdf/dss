package eu.europa.esig.dss.eaa.jwt.creation;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import eu.europa.esig.dss.eaa.common.creation.EAAParameters;
import eu.europa.esig.dss.eaa.jwt.creation.claim.SDJWTPresentableClaim;
import eu.europa.esig.dss.enumerations.DigestAlgorithm;

public class SDJWTEAAParameters extends EAAParameters {

    private final Map<String, SDJWTPresentableClaim> claims = new LinkedHashMap<>();
    private DigestAlgorithm digestAlgorithm = DigestAlgorithm.SHA256;
    private String issuer;
    private String subject;

    public void addClaim(final SDJWTPresentableClaim claim) {
        claims.put(claim.getName(), claim);
    }

    public SDJWTPresentableClaim getClaim(final String name) {
        return claims.get(name);
    }

    public void deleteClaim(final String name) {
        claims.remove(name);
    }

    public List<SDJWTPresentableClaim> getClaims() {
        return new ArrayList<>(claims.values());
    }

    public void setDigestAlgorithm(final DigestAlgorithm digestAlgorithm) {
        this.digestAlgorithm = digestAlgorithm;
    }

    public DigestAlgorithm getDigestAlgorithm() {
        return digestAlgorithm;
    }

    public String getIssuer() {
        return issuer;
    }

    public void setIssuer(final String issuer) {
        this.issuer = issuer;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(final String subject) {
        this.subject = subject;
    }
}
