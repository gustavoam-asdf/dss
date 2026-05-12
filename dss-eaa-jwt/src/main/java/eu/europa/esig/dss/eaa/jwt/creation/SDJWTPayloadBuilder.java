package eu.europa.esig.dss.eaa.jwt.creation;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import org.jose4j.base64url.Base64Url;
import org.jose4j.json.JsonUtil;
import org.jose4j.json.internal.json_simple.JSONAware;
import org.jose4j.json.internal.json_simple.JSONStreamAware;
import org.jose4j.json.internal.json_simple.JSONValue;

import eu.europa.esig.dss.eaa.common.creation.EAAPayloadBuilder;
import eu.europa.esig.dss.eaa.jwt.SDJWTConstants;
import eu.europa.esig.dss.eaa.jwt.creation.claim.SDJWTArrayPresentableClaim;
import eu.europa.esig.dss.eaa.jwt.creation.claim.SDJWTObjectPresentableClaim;
import eu.europa.esig.dss.eaa.jwt.creation.claim.SDJWTPresentableClaim;
import eu.europa.esig.dss.enumerations.DigestAlgorithm;
import eu.europa.esig.dss.enumerations.MimeTypeEnum;
import eu.europa.esig.dss.jades.DSSJsonUtils;
import eu.europa.esig.dss.model.DSSDocument;
import eu.europa.esig.dss.model.InMemoryDocument;
import eu.europa.esig.dss.spi.DSSUtils;

public class SDJWTPayloadBuilder extends EAAPayloadBuilder {

    private DigestAlgorithm digestAlgorithm = DigestAlgorithm.SHA256;
    private SDJWTSaltGenerator saltGenerator = new SDJWTDefaultSaltGenerator();
    private final Map<String, SDJWTPresentableClaim> claims = new LinkedHashMap<>();

    public void addClaim(final SDJWTPresentableClaim claim) {
        claims.put(claim.getName(), claim);
    }

    public SDJWTPresentableClaim addClaim(final String key, final Object value, final boolean isSelectivelyDisclosable) {
        return this.addClaim(key, value, isSelectivelyDisclosable, saltGenerator.generateSalt());
    }

    public SDJWTPresentableClaim addClaim(final String key, final Object value, final boolean isSelectivelyDisclosable, final String salt) {
        SDJWTPresentableClaim claim = new SDJWTPresentableClaim(key, value, isSelectivelyDisclosable, salt);
        claims.put(claim.getName(), claim);
        return claim;
    }

    @Override
    public DSSDocument buildPayload() {
        final Map<String, Object> map = new LinkedHashMap<>();

        final List<SDJWTPresentableClaim> claimsSelectivelyDisclosable = claims.values()
                .stream()
                .filter(SDJWTPresentableClaim::isSelectivelyDisclosable)
                .collect(Collectors.toList());

        final List<SDJWTPresentableClaim> visibleClaims = claims.values()
                .stream()
                .filter(claim -> !claim.isSelectivelyDisclosable())
                .collect(Collectors.toList());

        if (!claimsSelectivelyDisclosable.isEmpty()) {
            List<String> hashedDisclosures = new ArrayList<>();
            for (final SDJWTPresentableClaim claim : claimsSelectivelyDisclosable) {
                hashedDisclosures.add(getHashedDisclosure(claim, digestAlgorithm == null ? DigestAlgorithm.SHA256 : digestAlgorithm));
            }
            map.put(SDJWTConstants._SD, hashedDisclosures);
        }

        for (final SDJWTPresentableClaim claim : visibleClaims) {
            map.put(claim.getName(), getClaimValue(claim, digestAlgorithm == null ? DigestAlgorithm.SHA256 : digestAlgorithm, null));
        }

        if (getIssuer() != null) {
            map.put(SDJWTConstants.ISSUER, getIssuer());
        }
        if (getIssuanceDate() != null) {
            map.put(SDJWTConstants.ISSUED_AT, getIssuanceDate().toInstant().getEpochSecond());
        }
        if (getExpirationDate() != null) {
            map.put(SDJWTConstants.EXPIRATION_TIME, getExpirationDate().toInstant().getEpochSecond());
        }
        if (getSubject() != null) {
            map.put(SDJWTConstants.SUBJECT, getSubject());
        }
        if (digestAlgorithm != null) {
            map.put(SDJWTConstants._SD_ALG, digestAlgorithm.getSDJWTId());
        }

        InMemoryDocument result = new InMemoryDocument(JsonUtil.toJson(map).getBytes());
        result.setMimeType(MimeTypeEnum.JSON);
        return result;
    }

    private Object getClaimValue(final SDJWTPresentableClaim claim, final DigestAlgorithm digestAlgorithm, SDJWTPresentableClaim parentClaim) {
        if (parentClaim != null && !(parentClaim instanceof SDJWTArrayPresentableClaim)) {
            Objects.requireNonNull(claim.getName(), "The name of a claim cannot be null");
        }
        if (claim instanceof SDJWTObjectPresentableClaim) {
            return this.getSDJWTObjectPresentableClaimValue((SDJWTObjectPresentableClaim) claim, digestAlgorithm);
        } else if (claim instanceof SDJWTArrayPresentableClaim) {
            return this.getSDJWTArrayPresentableClaimValue((SDJWTArrayPresentableClaim) claim, digestAlgorithm);
        } else if (claim.getValue() == null) {
            return null;
        } else if (claim.getValue() instanceof String
                || claim.getValue() instanceof Number
                || claim.getValue() instanceof Map
                || claim.getValue() instanceof Collection
                || claim.getValue() instanceof JSONAware
                || claim.getValue() instanceof JSONStreamAware
                || claim.getValue().getClass().isArray()) {
            return claim.getValue();
        }
        return JSONValue.toJSONString(claim.getValue());
    }

    private Object getSDJWTObjectPresentableClaimValue(final SDJWTObjectPresentableClaim objectClaim, final DigestAlgorithm digestAlgorithm) {
        Map<String, Object> result = new LinkedHashMap<>();
        List<String> selectivelyDisclosableClaims = new ArrayList<>();

        objectClaim.getChildren().forEach(child -> {
            if (child.isSelectivelyDisclosable()) {
                selectivelyDisclosableClaims.add(getHashedDisclosure(child, digestAlgorithm));
            } else {
                result.put(child.getName(), getClaimValue(child, digestAlgorithm, objectClaim));
            }
        });

        if (!selectivelyDisclosableClaims.isEmpty()) {
            result.put(SDJWTConstants._SD, selectivelyDisclosableClaims);
        }

        return result;
    }

    private Object getSDJWTArrayPresentableClaimValue(final SDJWTArrayPresentableClaim arrayClaim, final DigestAlgorithm digestAlgorithm) {
        List<Object> result = new ArrayList<>();

        arrayClaim.getElements().forEach(element -> {
            if (element.isSelectivelyDisclosable()) {
                Map<String, String> hashedElement = new LinkedHashMap<>();
                hashedElement.put(SDJWTConstants.HASH, getHashedDisclosure(element, digestAlgorithm));
                result.add(hashedElement);
            } else {
                result.add(getClaimValue(element, digestAlgorithm, arrayClaim));
            }
        });

        return result;
    }

    private String getHashedDisclosure(SDJWTPresentableClaim claim, DigestAlgorithm digestAlgorithm) {
        byte[] digest = DSSUtils.digest(digestAlgorithm, buildDisclosure(claim, digestAlgorithm).getBytes());
        return Base64Url.encode(digest);
    }

    /**
     * Build the disclosure for the given claim
     *
     * @param claim
     *         the claim
     * @param digestAlgorithm
     *         the digest algorithm
     * @return {@link String}
     */
    public String buildDisclosure(SDJWTPresentableClaim claim, DigestAlgorithm digestAlgorithm) {
        List<Object> data = new ArrayList<>();
        data.add(claim.getSalt());
        if (claim.getName() != null) {
            data.add(claim.getName());
        }
        data.add(getClaimValue(claim, digestAlgorithm, null));

        return DSSJsonUtils.toBase64Url(data);
    }

    public void setSaltGenerator(final SDJWTSaltGenerator saltGenerator) {
        this.saltGenerator = saltGenerator;
    }

    public void setDigestAlgorithm(final DigestAlgorithm digestAlgorithm) {
        this.digestAlgorithm = digestAlgorithm;
    }

    public DigestAlgorithm getDigestAlgorithm() {
        return digestAlgorithm;
    }
}
