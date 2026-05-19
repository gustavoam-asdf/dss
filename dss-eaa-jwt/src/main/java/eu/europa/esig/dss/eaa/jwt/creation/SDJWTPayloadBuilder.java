package eu.europa.esig.dss.eaa.jwt.creation;

import eu.europa.esig.dss.eaa.common.creation.EAAPayloadBuilder;
import eu.europa.esig.dss.eaa.common.creation.claim.AbstractEAAClaim;
import eu.europa.esig.dss.eaa.common.creation.claim.EAAClaimArray;
import eu.europa.esig.dss.eaa.jwt.SDJWTConstants;
import eu.europa.esig.dss.enumerations.DigestAlgorithm;
import eu.europa.esig.dss.enumerations.MimeTypeEnum;
import eu.europa.esig.dss.jades.DSSJsonUtils;
import eu.europa.esig.dss.model.DSSDocument;
import eu.europa.esig.dss.model.DSSException;
import eu.europa.esig.dss.model.InMemoryDocument;
import eu.europa.esig.dss.spi.DSSUtils;
import eu.europa.esig.dss.utils.Utils;

import org.jose4j.base64url.Base64Url;
import org.jose4j.json.JsonUtil;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

public class SDJWTPayloadBuilder extends EAAPayloadBuilder {

    private DigestAlgorithm digestAlgorithm = DigestAlgorithm.SHA256;
    private SDJWTSaltGenerator saltGenerator = new SDJWTDefaultSaltGenerator();
    private final Map<String, SDJWTEAAClaim> claims = new LinkedHashMap<>();

    public void addClaim(final SDJWTEAAClaim claim) {
        claims.put(getName(claim), claim);
    }

    public AbstractEAAClaim addClaim(final String name, final Object value) {
        return addClaim(name, value, false, null);
    }

    public AbstractEAAClaim addClaim(final String name, final Object value, final boolean isSelectivelyDisclosable) {
        String salt = isSelectivelyDisclosable ? saltGenerator.generateSalt() : null;
        return addClaim(name, value, isSelectivelyDisclosable, salt);
    }

    public AbstractEAAClaim addClaim(final String name, final Object value, final boolean isSelectivelyDisclosable, final String salt) {
        SDJWTEAAClaim claim = new SDJWTEAAClaim(name, value, isSelectivelyDisclosable, salt);
        claims.put(getName(claim), claim);
        return claim;
    }

    @Override
    public DSSDocument buildPayload() {
        final Map<String, Object> map = new LinkedHashMap<>();

        final List<SDJWTEAAClaim> claimsSelectivelyDisclosable = claims.values()
                .stream()
                .filter(SDJWTEAAClaim::isSelectivelyDisclosable)
                .collect(Collectors.toList());

        final List<SDJWTEAAClaim> visibleClaims = claims.values()
                .stream()
                .filter(claim -> !claim.isSelectivelyDisclosable())
                .collect(Collectors.toList());

        if (!claimsSelectivelyDisclosable.isEmpty()) {
            List<String> hashedDisclosures = new ArrayList<>();
            for (final SDJWTEAAClaim claim : claimsSelectivelyDisclosable) {
                hashedDisclosures.add(getHashedDisclosure(claim, digestAlgorithm == null ? DigestAlgorithm.SHA256 : digestAlgorithm));
            }
            map.put(SDJWTConstants._SD, hashedDisclosures);
        }

        for (final SDJWTEAAClaim claim : visibleClaims) {
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

    private Object getClaimValue(final SDJWTEAAClaim claim, final DigestAlgorithm digestAlgorithm, SDJWTEAAClaim parentClaim) {
        if (parentClaim != null && !(parentClaim instanceof EAAClaimArray)) {
            Objects.requireNonNull(claim.getName(), "The name of a claim cannot be null");
        }
        if (claim.getName() != null && !(claim.getName() instanceof String)) {
            throw new DSSException("The name of an SD-JWT claim must be of type String");
        }

        if (claim instanceof SDJWTEAAClaimObject) {
            return getEAAClaimObjectValue((SDJWTEAAClaimObject) claim, digestAlgorithm);
        } else if (claim instanceof SDJWTEAAClaimArray) {
            return getEAAClaimArrayValue((SDJWTEAAClaimArray) claim, digestAlgorithm);
        } else if (claim.getValue() instanceof Map) {
            return getMapClaimValue(claim, (Map<?, ?>) claim.getValue(), digestAlgorithm, parentClaim);
        } else if (claim.getValue() != null
                && (claim.getValue().getClass().isArray() || claim.getValue() instanceof Collection)) {
            // TODO
            return null;
        }

        return claim.getValue();
    }

    private Object getMapClaimValue(final SDJWTEAAClaim claim, final Map<?, ?> map, final DigestAlgorithm digestAlgorithm, final SDJWTEAAClaim parentClaim) {
        String claimName = (String) claim.getName();
        SDJWTEAAClaimObject claimObject = new SDJWTEAAClaimObject(claimName, claim.isSelectivelyDisclosable(), claim.getSalt());
        for (Map.Entry<?, ?> entry : map.entrySet()) {
            if (!(entry.getKey() instanceof String)) {
                throw new DSSException("The name of an SD-JWT claim must be of type String");
            }

            String entryName = (String) entry.getKey();
            Object value = entry.getValue();

            if (value instanceof AbstractEAAClaim) {
                AbstractEAAClaim eaaClaim = (AbstractEAAClaim) value;
                if (!(eaaClaim.getName() instanceof String)) {
                    throw new DSSException("The name of an SD-JWT claim must be of type String");
                }
                if (Utils.areStringsEqual(entryName, (String) eaaClaim.getName())) {
                    throw new DSSException("For a Map, the key of the entry is expected to have the same value as the name of the claim");
                }
                claimObject.addChild((SDJWTEAAClaim) value);
            } else {
                claimObject.addChild(new SDJWTEAAClaim(entryName, value, false, null));
            }
        }

        return getClaimValue(claimObject, digestAlgorithm, parentClaim);
    }

    private Object getEAAClaimObjectValue(final SDJWTEAAClaimObject objectClaim, final DigestAlgorithm digestAlgorithm) {
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

    private Object getEAAClaimArrayValue(final SDJWTEAAClaimArray arrayClaim, final DigestAlgorithm digestAlgorithm) {
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

    private String getHashedDisclosure(SDJWTEAAClaim claim, DigestAlgorithm digestAlgorithm) {
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
    public String buildDisclosure(SDJWTEAAClaim claim, DigestAlgorithm digestAlgorithm) {
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

    private String getName(SDJWTEAAClaim eaaClaim) {
        Object name = eaaClaim.getName();
        if (name instanceof String) {
            return (String) name;
        }
        throw new IllegalArgumentException("Name of an EAAClaim shall be of String type!");
    }

}
