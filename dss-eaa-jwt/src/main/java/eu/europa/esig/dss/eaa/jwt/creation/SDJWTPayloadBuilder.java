package eu.europa.esig.dss.eaa.jwt.creation;

import eu.europa.esig.dss.eaa.common.creation.EAAPayloadBuilder;
import eu.europa.esig.dss.eaa.common.creation.claim.EAAClaim;
import eu.europa.esig.dss.eaa.common.creation.claim.EAAClaimArray;
import eu.europa.esig.dss.eaa.common.creation.claim.EAAClaimObject;
import eu.europa.esig.dss.eaa.jwt.SDJWTConstants;
import eu.europa.esig.dss.enumerations.DigestAlgorithm;
import eu.europa.esig.dss.enumerations.MimeTypeEnum;
import eu.europa.esig.dss.jades.DSSJsonUtils;
import eu.europa.esig.dss.model.DSSDocument;
import eu.europa.esig.dss.model.InMemoryDocument;
import eu.europa.esig.dss.spi.DSSUtils;
import org.jose4j.base64url.Base64Url;
import org.jose4j.json.JsonUtil;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

public class SDJWTPayloadBuilder extends EAAPayloadBuilder {

    private DigestAlgorithm digestAlgorithm = DigestAlgorithm.SHA256;
    private SDJWTSaltGenerator saltGenerator = new SDJWTDefaultSaltGenerator();
    private final Map<String, EAAClaim> claims = new LinkedHashMap<>();

    public void addClaim(final EAAClaim claim) {
        claims.put(getName(claim), claim);
    }

    public EAAClaim addClaim(final String name, final Object value) {
        return addClaim(name, value, false, null);
    }

    public EAAClaim addClaim(final String name, final Object value, final boolean isSelectivelyDisclosable) {
        String salt = isSelectivelyDisclosable ? saltGenerator.generateSalt() : null;
        return addClaim(name, value, isSelectivelyDisclosable, salt);
    }

    public EAAClaim addClaim(final String name, final Object value, final boolean isSelectivelyDisclosable, final String salt) {
        EAAClaim claim = new EAAClaim(name, value, isSelectivelyDisclosable, salt);
        claims.put(getName(claim), claim);
        return claim;
    }

    @Override
    public DSSDocument buildPayload() {
        final Map<String, Object> map = new LinkedHashMap<>();

        final List<EAAClaim> claimsSelectivelyDisclosable = claims.values()
                .stream()
                .filter(EAAClaim::isSelectivelyDisclosable)
                .collect(Collectors.toList());

        final List<EAAClaim> visibleClaims = claims.values()
                .stream()
                .filter(claim -> !claim.isSelectivelyDisclosable())
                .collect(Collectors.toList());

        if (!claimsSelectivelyDisclosable.isEmpty()) {
            List<String> hashedDisclosures = new ArrayList<>();
            for (final EAAClaim claim : claimsSelectivelyDisclosable) {
                hashedDisclosures.add(getHashedDisclosure(claim, digestAlgorithm == null ? DigestAlgorithm.SHA256 : digestAlgorithm));
            }
            map.put(SDJWTConstants._SD, hashedDisclosures);
        }

        for (final EAAClaim claim : visibleClaims) {
            map.put(getName(claim), getClaimValue(claim, digestAlgorithm == null ? DigestAlgorithm.SHA256 : digestAlgorithm, null));
        }

        if (isOneTime()) {
            map.put(SDJWTConstants.ONE_TIME, null);
        }
        if (isShortLived()) {
            map.put(SDJWTConstants.SHORT_LIVED, null);
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

    private Object getClaimValue(final EAAClaim claim, final DigestAlgorithm digestAlgorithm, EAAClaim parentClaim) {
        if (parentClaim != null && !(parentClaim instanceof EAAClaimArray)) {
            Objects.requireNonNull(claim.getName(), "The name of a claim cannot be null");
        }
        if (claim instanceof EAAClaimObject) {
            return getEAAClaimObjectValue((EAAClaimObject) claim, digestAlgorithm);
        } else if (claim instanceof EAAClaimArray) {
            return getEAAClaimArrayValue((EAAClaimArray) claim, digestAlgorithm);
        }
        return claim.getValue();
    }

    private Object getEAAClaimObjectValue(final EAAClaimObject objectClaim, final DigestAlgorithm digestAlgorithm) {
        Map<String, Object> result = new LinkedHashMap<>();
        List<String> selectivelyDisclosableClaims = new ArrayList<>();

        objectClaim.getChildren().forEach(child -> {
            if (child.isSelectivelyDisclosable()) {
                selectivelyDisclosableClaims.add(getHashedDisclosure(child, digestAlgorithm));
            } else {
                result.put(getName(child), getClaimValue(child, digestAlgorithm, objectClaim));
            }
        });

        if (!selectivelyDisclosableClaims.isEmpty()) {
            result.put(SDJWTConstants._SD, selectivelyDisclosableClaims);
        }

        return result;
    }

    private Object getEAAClaimArrayValue(final EAAClaimArray arrayClaim, final DigestAlgorithm digestAlgorithm) {
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

    private String getHashedDisclosure(EAAClaim claim, DigestAlgorithm digestAlgorithm) {
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
    public String buildDisclosure(EAAClaim claim, DigestAlgorithm digestAlgorithm) {
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

    private String getName(EAAClaim eaaClaim) {
        Object name = eaaClaim.getName();
        if (name instanceof String) {
            return (String) name;
        }
        throw new IllegalArgumentException("Name of an EAAClaim shall be of String type!");
    }

}
