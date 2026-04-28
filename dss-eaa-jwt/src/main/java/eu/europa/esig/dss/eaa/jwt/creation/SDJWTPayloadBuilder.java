package eu.europa.esig.dss.eaa.jwt.creation;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import org.jose4j.base64url.Base64Url;
import org.jose4j.json.JsonUtil;

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

public class SDJWTPayloadBuilder {

    public DSSDocument buildPayload(SDJWTEAAParameters parameters) {
        final Map<String, Object> map = new LinkedHashMap<>();

        DigestAlgorithm digestAlgorithm = DigestAlgorithm.SHA256; // Default digest algorithm
        if (parameters.getDigestAlgorithm() != null) {
            digestAlgorithm = parameters.getDigestAlgorithm();
        }

        final List<SDJWTPresentableClaim> claimsSelectivelyDisclosable = parameters.getClaims()
                .stream()
                .filter(SDJWTPresentableClaim::isSelectivelyDisclosable)
                .collect(Collectors.toList());

        final List<SDJWTPresentableClaim> visibleClaims = parameters.getClaims()
                .stream()
                .filter(claim -> !claim.isSelectivelyDisclosable())
                .collect(Collectors.toList());

        if (!claimsSelectivelyDisclosable.isEmpty()) {
            List<String> hashedDisclosures = new ArrayList<>();
            for (final SDJWTPresentableClaim claim : claimsSelectivelyDisclosable) {
                hashedDisclosures.add(getHashedDisclosure(claim, digestAlgorithm));
            }
            map.put(SDJWTConstants._SD, hashedDisclosures);
        }

        for (final SDJWTPresentableClaim claim : visibleClaims) {
            map.put(claim.getName(), getClaimValue(claim, digestAlgorithm, null));
        }

        if (parameters.getIssuer() != null) {
            map.put(SDJWTConstants.ISSUER, parameters.getIssuer());
        }
        if (parameters.getIssuanceDate() != null) {
            map.put(SDJWTConstants.ISSUED_AT, parameters.getIssuanceDate().toInstant().getEpochSecond());
        }
        if (parameters.getExpirationDate() != null) {
            map.put(SDJWTConstants.EXPIRATION_TIME, parameters.getExpirationDate().toInstant().getEpochSecond());
        }
        if (parameters.getSubject() != null) {
            map.put(SDJWTConstants.SUBJECT, parameters.getSubject());
        }
        if (parameters.getDigestAlgorithm() != null) {
            map.put(SDJWTConstants._SD_ALG, digestAlgorithm.getSDJWTId());
        }

        InMemoryDocument result = new InMemoryDocument(JsonUtil.toJson(map).getBytes());
        result.setMimeType(MimeTypeEnum.JSON);
        return result;
    }

    private Object getClaimValue(final SDJWTPresentableClaim claim, final DigestAlgorithm digestAlgorithm, SDJWTPresentableClaim parentClaim) {
        if (!(parentClaim instanceof SDJWTArrayPresentableClaim)) {
            Objects.requireNonNull(claim.getName(), "The name of a claim cannot be null");
        }
        if (claim instanceof SDJWTObjectPresentableClaim) {
            SDJWTObjectPresentableClaim objectClaim = (SDJWTObjectPresentableClaim) claim;
            Map<String, Object> result = new LinkedHashMap<>();
            List<String> selectivelyDisclosableClaims = new ArrayList<>();

            objectClaim.getChildren().forEach(child -> {
                if (child.isSelectivelyDisclosable()) {
                    selectivelyDisclosableClaims.add(getHashedDisclosure(child, digestAlgorithm));
                } else {
                    result.put(child.getName(), getClaimValue(child, digestAlgorithm, claim));
                }
            });

            if (!selectivelyDisclosableClaims.isEmpty()) {
                result.put(SDJWTConstants._SD, selectivelyDisclosableClaims);
            }

            return result;
        } else if (claim instanceof SDJWTArrayPresentableClaim) {
            SDJWTArrayPresentableClaim arrayClaim = (SDJWTArrayPresentableClaim) claim;
            List<Object> result = new ArrayList<>();

            arrayClaim.getElements().forEach(element -> {
                if (element.isSelectivelyDisclosable()) {
                    Map<String, String> hashedElement = new LinkedHashMap<>();
                    hashedElement.put(SDJWTConstants.HASH, getHashedDisclosure(element, digestAlgorithm));
                    result.add(hashedElement);
                } else {
                    result.add(getClaimValue(element, digestAlgorithm, claim));
                }
            });

            return result;
        } else {
            return claim.getValueAsString();
        }
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

}
