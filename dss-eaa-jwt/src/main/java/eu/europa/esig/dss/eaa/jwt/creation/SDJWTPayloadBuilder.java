package eu.europa.esig.dss.eaa.jwt.creation;

import eu.europa.esig.dss.eaa.common.creation.AbstractEAAPayloadBuilder;
import eu.europa.esig.dss.eaa.jwt.SDJWTConstants;
import eu.europa.esig.dss.enumerations.DigestAlgorithm;
import eu.europa.esig.dss.enumerations.MimeTypeEnum;
import eu.europa.esig.dss.jades.DSSJsonUtils;
import eu.europa.esig.dss.model.DSSDocument;
import eu.europa.esig.dss.model.DSSException;
import eu.europa.esig.dss.model.Digest;
import eu.europa.esig.dss.model.InMemoryDocument;
import eu.europa.esig.dss.spi.DSSUtils;
import eu.europa.esig.dss.utils.Utils;
import org.jose4j.json.JsonUtil;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * Creates a payload for an RFC 9901 SD-JWT VC token based on the provided parameters
 *
 */
public class SDJWTPayloadBuilder extends AbstractEAAPayloadBuilder<SDJWTEAAPayloadParameters, SDJWTEAAClaim, SDJWTEAADisclosure> {

    /** Builds disclosures */
    private SDJWTDisclosureBuilder disclosureBuilder = new DefaultSDJWTDisclosureBuilder();

    /**
     * Sets a disclosure builder.
     * Default : {@code eu.europa.esig.dss.eaa.jwt.creation.DefaultSDJWTDisclosureBuilder}
     *
     * @param disclosureBuilder {@link SDJWTDisclosureBuilder}
     */
    public void setDisclosureBuilder(SDJWTDisclosureBuilder disclosureBuilder) {
        Objects.requireNonNull(disclosureBuilder, "Disclosure builder cannot be null!");
        this.disclosureBuilder = disclosureBuilder;
    }

    @Override
    public DSSDocument buildPayload(SDJWTEAAPayloadParameters payloadParameters) {
        final Map<String, Object> map = new LinkedHashMap<>();

        DigestAlgorithm digestAlgorithm = payloadParameters.getDigestAlgorithm() != null ?
                payloadParameters.getDigestAlgorithm() : DigestAlgorithm.SHA256;
        if (payloadParameters.getDigestAlgorithm() != null) {
            map.put(SDJWTConstants._SD_ALG, digestAlgorithm.getSDJWTId());
        }

        final SecureRandom secureRandom = secureRandom(payloadParameters);
        final SDJWTEAAClaimObject payload = getRootPayloadObject(payloadParameters);
        map.putAll(getEAAClaimObjectValue(payload, digestAlgorithm, secureRandom));

        InMemoryDocument result = new InMemoryDocument(JsonUtil.toJson(map).getBytes());
        result.setMimeType(MimeTypeEnum.JSON);
        return result;
    }

    private SDJWTEAAClaimObject getRootPayloadObject(SDJWTEAAPayloadParameters payloadParameters) {
        final SDJWTEAAClaimObject payload = SDJWTEAAClaimObject.create();

        if (payloadParameters.getIssuer() != null) {
            payload.addChild(SDJWTEAAClaim.create(SDJWTConstants.ISSUER, payloadParameters.getIssuer()));
        }
        if (payloadParameters.getIssuanceDate() != null) {
            payload.addChild(SDJWTEAAClaim.create(SDJWTConstants.ISSUED_AT, DSSUtils.getTimeValueInSeconds(payloadParameters.getIssuanceDate().getTime())));
        }
        if (payloadParameters.getExpirationDate() != null) {
            payload.addChild(SDJWTEAAClaim.create(SDJWTConstants.EXPIRATION_TIME, DSSUtils.getTimeValueInSeconds(payloadParameters.getExpirationDate().getTime())));
        }
        if (payloadParameters.getSubject() != null) {
            payload.addChild(SDJWTEAAClaim.create(SDJWTConstants.SUBJECT, payloadParameters.getSubject()));
        }
        if (payloadParameters.isOneTime()) {
            payload.addChild(SDJWTEAAClaim.create(SDJWTConstants.ONE_TIME, null));
        }
        if (payloadParameters.isShortLived()) {
            payload.addChild(SDJWTEAAClaim.create(SDJWTConstants.SHORT_LIVED, null));
        }

        payload.addChildren(payloadParameters.getClaims());

        if (Utils.isCollectionNotEmpty(payloadParameters.getDecoyDigests())) {
            payloadParameters.getDecoyDigests().forEach(payload::addDecoyDigest);
        }

        return payload;
    }

    private Object getClaimValue(final SDJWTEAAClaim claim, final DigestAlgorithm digestAlgorithm, final SecureRandom secureRandom) {
        if (claim instanceof SDJWTEAAClaimObject) {
            return getEAAClaimObjectValue((SDJWTEAAClaimObject) claim, digestAlgorithm, secureRandom);

        } else if (claim instanceof SDJWTEAAClaimArray) {
            return getEAAClaimArrayValue((SDJWTEAAClaimArray) claim, digestAlgorithm, secureRandom);

        } else if (claim.getValue() instanceof Map) {
            return getClaimValue(toEAAClaimObject((Map<?, ?>) claim.getValue()), digestAlgorithm, secureRandom);

        } else if (claim.getValue() instanceof Collection) {
            return getClaimValue(toEAAClaimArray((Collection<?>) claim.getValue()), digestAlgorithm, secureRandom);

        } else if (claim.getValue() instanceof Object[]) {
            return getClaimValue(toEAAClaimArray((Object[]) claim.getValue()), digestAlgorithm, secureRandom);
        }

        return claim.getValue();
    }

    private SDJWTEAAClaimObject toEAAClaimObject(Map<?, ?> map) {
        final SDJWTEAAClaimObject result = SDJWTEAAClaimObject.create();
        for (Map.Entry<?, ?> entry : map.entrySet()) {
            if (!(entry.getKey() instanceof String)) {
                throw new DSSException("Map key must be String");
            }

            String name = (String) entry.getKey();
            Object value = entry.getValue();

            if (value instanceof SDJWTEAAClaim) {
                result.addChild((SDJWTEAAClaim) value);
            } else {
                result.addChild(SDJWTEAAClaim.create(name, value));
            }
        }
        return result;
    }

    private SDJWTEAAClaimArray toEAAClaimArray(Object[] array) {
        final SDJWTEAAClaimArray result = SDJWTEAAClaimArray.create();
        for (Object item : array) {
            if (item instanceof SDJWTEAAClaim) {
                result.addElement((SDJWTEAAClaim) item);
            } else {
                result.addElement(SDJWTEAAClaim.create(item));
            }
        }
        return result;
    }

    private SDJWTEAAClaimArray toEAAClaimArray(Collection<?> collection) {
        final SDJWTEAAClaimArray result = SDJWTEAAClaimArray.create();
        for (Object item : collection) {
            if (item instanceof SDJWTEAAClaim) {
                result.addElement((SDJWTEAAClaim) item);
            } else {
                result.addElement(SDJWTEAAClaim.create(item));
            }
        }
        return result;
    }

    private Map<String, Object> getEAAClaimObjectValue(final SDJWTEAAClaimObject objectClaim, final DigestAlgorithm digestAlgorithm, SecureRandom secureRandom) {
        Map<String, Object> result = new LinkedHashMap<>();
        List<String> selectivelyDisclosableClaims = new ArrayList<>();

        objectClaim.getChildren().forEach(child -> {
            if (child.isSelectivelyDisclosable()) {
                selectivelyDisclosableClaims.add(getHashedDisclosure(child, digestAlgorithm, secureRandom));
            } else {
                result.put(child.getName(), getClaimValue(child, digestAlgorithm, secureRandom));
            }
        });

        selectivelyDisclosableClaims.addAll(objectClaim.getDecoyDigests());
        if (!selectivelyDisclosableClaims.isEmpty()) {
            Collections.shuffle(selectivelyDisclosableClaims, secureRandom);
            result.put(SDJWTConstants._SD, selectivelyDisclosableClaims);
        }

        return result;
    }

    private List<Object> getEAAClaimArrayValue(final SDJWTEAAClaimArray arrayClaim, final DigestAlgorithm digestAlgorithm, SecureRandom secureRandom) {
        List<Object> result = new ArrayList<>();

        arrayClaim.getElements().forEach(element -> {
            if (element.isSelectivelyDisclosable()) {
                Map<String, String> hashedElement = new LinkedHashMap<>();
                hashedElement.put(SDJWTConstants.HASH, getHashedDisclosure(element, digestAlgorithm, secureRandom));
                result.add(hashedElement);
            } else {
                result.add(getClaimValue(element, digestAlgorithm, secureRandom));
            }
        });

        arrayClaim.getDecoyDigests().forEach(decoyDigest -> {
            Map<String, String> decoyElement = new LinkedHashMap<>();
            decoyElement.put(SDJWTConstants.HASH, decoyDigest);
            result.add(decoyElement);
        });

        return result;
    }

    private String getHashedDisclosure(SDJWTEAAClaim claim, DigestAlgorithm digestAlgorithm, SecureRandom secureRandom) {
        SDJWTEAADisclosure disclosure = buildDisclosure(claim, digestAlgorithm, secureRandom);
        Digest digest = disclosure.computeDigest(digestAlgorithm);
        return DSSJsonUtils.toBase64Url(digest.getValue());
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
    public SDJWTEAADisclosure buildDisclosure(SDJWTEAAClaim claim, DigestAlgorithm digestAlgorithm, SecureRandom secureRandom) {
        String salt = claim.getSalt();
        if (Utils.isStringEmpty(salt)) {
            byte[] bytes = nextRandomSalt(secureRandom); // 16 * 8 = 128 bits
            salt = DSSJsonUtils.toBase64Url(bytes);
        }
        return disclosureBuilder.build(claim.getName(), getClaimValue(claim, digestAlgorithm, secureRandom), salt);
    }

    @Override
    public List<SDJWTEAADisclosure> buildDisclosures(SDJWTEAAPayloadParameters payloadParameters) {
        DigestAlgorithm digestAlgorithm = payloadParameters.getDigestAlgorithm() != null ?
                payloadParameters.getDigestAlgorithm() : DigestAlgorithm.SHA256;

        final List<SDJWTEAADisclosure> disclosures = new ArrayList<>();

        SecureRandom secureRandom = secureRandom(payloadParameters);
        SDJWTEAAClaimObject root = getRootPayloadObject(payloadParameters);
        collectDisclosures(root, digestAlgorithm, disclosures, secureRandom);

        return disclosures;
    }

    private void collectDisclosures(final SDJWTEAAClaim claim,
                                    final DigestAlgorithm digestAlgorithm,
                                    final List<SDJWTEAADisclosure> disclosures, SecureRandom secureRandom) {

        if (claim.isSelectivelyDisclosable()) {
            disclosures.add(buildDisclosure(claim, digestAlgorithm, secureRandom));
        }

        // TODO : improve ?
        if (claim instanceof SDJWTEAAClaimObject) {

            SDJWTEAAClaimObject objectClaim = (SDJWTEAAClaimObject) claim;

            for (SDJWTEAAClaim child : objectClaim.getChildren()) {
                collectDisclosures(child, digestAlgorithm, disclosures, secureRandom);
            }

        } else if (claim instanceof SDJWTEAAClaimArray) {

            SDJWTEAAClaimArray arrayClaim = (SDJWTEAAClaimArray) claim;

            for (SDJWTEAAClaim element : arrayClaim.getElements()) {
                collectDisclosures(element, digestAlgorithm, disclosures, secureRandom);
            }

        } else if (claim.getValue() instanceof Map) {
            collectDisclosures(toEAAClaimObject((Map<?, ?>) claim.getValue()), digestAlgorithm, disclosures, secureRandom);

        } else if (claim.getValue() instanceof Collection) {
            collectDisclosures(toEAAClaimArray((Collection<?>) claim.getValue()), digestAlgorithm, disclosures, secureRandom);

        } else if (claim.getValue() instanceof Object[]) {
            collectDisclosures(toEAAClaimArray((Object[]) claim.getValue()), digestAlgorithm, disclosures, secureRandom);
        }
    }

}
