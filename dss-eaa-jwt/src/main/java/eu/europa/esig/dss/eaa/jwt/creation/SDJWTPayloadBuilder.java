package eu.europa.esig.dss.eaa.jwt.creation;

import eu.europa.esig.dss.eaa.common.creation.AbstractEAAPayloadBuilder;
import eu.europa.esig.dss.eaa.jwt.SDJWTConstants;
import eu.europa.esig.dss.enumerations.DigestAlgorithm;
import eu.europa.esig.dss.enumerations.MimeTypeEnum;
import eu.europa.esig.dss.jades.DSSJsonUtils;
import eu.europa.esig.dss.model.DSSDocument;
import eu.europa.esig.dss.model.DSSException;
import eu.europa.esig.dss.model.InMemoryDocument;
import eu.europa.esig.dss.spi.DSSUtils;
import eu.europa.esig.dss.utils.Utils;
import org.jose4j.json.JsonUtil;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

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
        map.put(SDJWTConstants._SD_ALG, digestAlgorithm.getSDJWTId()); // TODO : ignore if no selectively disclosable claims present ?

        final SDJWTEAAClaimObject payload = getRootPayloadObject(payloadParameters);
        map.putAll(getEAAClaimObjectValue(payload, digestAlgorithm));

        InMemoryDocument result = new InMemoryDocument(JsonUtil.toJson(map).getBytes());
        result.setMimeType(MimeTypeEnum.JSON);
        return result;
    }

    private SDJWTEAAClaimObject getRootPayloadObject(SDJWTEAAPayloadParameters payloadParameters) {
        final SDJWTEAAClaimObject payload = new SDJWTEAAClaimObject();

        if (payloadParameters.getIssuer() != null) {
            payload.addChild(new SDJWTEAAClaim(SDJWTConstants.ISSUER, payloadParameters.getIssuer()));
        }
        if (payloadParameters.getIssuanceDate() != null) {
            payload.addChild(new SDJWTEAAClaim(SDJWTConstants.ISSUED_AT, DSSUtils.getTimeValueInSeconds(payloadParameters.getIssuanceDate().getTime())));
        }
        if (payloadParameters.getExpirationDate() != null) {
            payload.addChild(new SDJWTEAAClaim(SDJWTConstants.EXPIRATION_TIME, DSSUtils.getTimeValueInSeconds(payloadParameters.getExpirationDate().getTime())));
        }
        if (payloadParameters.getSubject() != null) {
            payload.addChild(new SDJWTEAAClaim(SDJWTConstants.SUBJECT, payloadParameters.getSubject()));
        }
        if (payloadParameters.isOneTime()) {
            payload.addChild(new SDJWTEAAClaim(SDJWTConstants.ONE_TIME, null));
        }
        if (payloadParameters.isShortLived()) {
            payload.addChild(new SDJWTEAAClaim(SDJWTConstants.SHORT_LIVED, null));
        }

        payload.addChildren(payloadParameters.getClaims());

        if (Utils.isCollectionNotEmpty(payloadParameters.getDecoyDigests())) {
            payloadParameters.getDecoyDigests().forEach(payload::addDecoyDigest);
        }

        return payload;
    }

    private Object getClaimValue(final SDJWTEAAClaim claim, final DigestAlgorithm digestAlgorithm) {
        if (claim instanceof SDJWTEAAClaimObject) {
            return getEAAClaimObjectValue((SDJWTEAAClaimObject) claim, digestAlgorithm);

        } else if (claim instanceof SDJWTEAAClaimArray) {
            return getEAAClaimArrayValue((SDJWTEAAClaimArray) claim, digestAlgorithm);

        } else if (claim.getValue() instanceof Map) {
            return getClaimValue(toEAAClaimObject((Map<?, ?>) claim.getValue()), digestAlgorithm);

        } else if (claim.getValue() instanceof Collection) {
            return getClaimValue(toEAAClaimArray((Collection<?>) claim.getValue()), digestAlgorithm);

        } else if (claim.getValue() instanceof Object[]) {
            return getClaimValue(toEAAClaimArray((Object[]) claim.getValue()), digestAlgorithm);
        }

        return claim.getValue();
    }

    private SDJWTEAAClaimObject toEAAClaimObject(Map<?, ?> map) {
        final SDJWTEAAClaimObject result = new SDJWTEAAClaimObject();
        for (Map.Entry<?, ?> entry : map.entrySet()) {
            if (!(entry.getKey() instanceof String)) {
                throw new DSSException("Map key must be String");
            }

            String name = (String) entry.getKey();
            Object value = entry.getValue();

            if (value instanceof SDJWTEAAClaim) {
                result.addChild((SDJWTEAAClaim) value);
            } else {
                result.addChild(new SDJWTEAAClaim(name, value, false, null));
            }
        }
        return result;
    }

    private SDJWTEAAClaimArray toEAAClaimArray(Object[] array) {
        final SDJWTEAAClaimArray result = new SDJWTEAAClaimArray();
        for (Object item : array) {
            if (item instanceof SDJWTEAAClaim) {
                result.addElement((SDJWTEAAClaim) item);
            } else {
                result.addElement(new SDJWTEAAClaim(item));
            }
        }
        return result;
    }

    private SDJWTEAAClaimArray toEAAClaimArray(Collection<?> collection) {
        final SDJWTEAAClaimArray result = new SDJWTEAAClaimArray();
        for (Object item : collection) {
            if (item instanceof SDJWTEAAClaim) {
                result.addElement((SDJWTEAAClaim) item);
            } else {
                result.addElement(new SDJWTEAAClaim(item));
            }
        }
        return result;
    }

    private Map<String, Object> getEAAClaimObjectValue(final SDJWTEAAClaimObject objectClaim, final DigestAlgorithm digestAlgorithm) {
        Map<String, Object> result = new LinkedHashMap<>();
        List<String> selectivelyDisclosableClaims = new ArrayList<>();

        objectClaim.getChildren().forEach(child -> {
            if (child.isSelectivelyDisclosable()) {
                selectivelyDisclosableClaims.add(getHashedDisclosure(child, digestAlgorithm));
            } else {
                result.put(child.getName(), getClaimValue(child, digestAlgorithm));
            }
        });

        selectivelyDisclosableClaims.addAll(objectClaim.getDecoyDigests());
        if (!selectivelyDisclosableClaims.isEmpty()) {
            Collections.shuffle(selectivelyDisclosableClaims);
            result.put(SDJWTConstants._SD, selectivelyDisclosableClaims);
        }

        return result;
    }

    private List<Object> getEAAClaimArrayValue(final SDJWTEAAClaimArray arrayClaim, final DigestAlgorithm digestAlgorithm) {
        List<Object> result = new ArrayList<>();

        arrayClaim.getElements().forEach(element -> {
            if (element.isSelectivelyDisclosable()) {
                Map<String, String> hashedElement = new LinkedHashMap<>();
                hashedElement.put(SDJWTConstants.HASH, getHashedDisclosure(element, digestAlgorithm));
                result.add(hashedElement);
            } else {
                result.add(getClaimValue(element, digestAlgorithm));
            }
        });

        arrayClaim.getDecoyDigests().forEach(decoyDigest -> {
            Map<String, String> decoyElement = new LinkedHashMap<>();
            decoyElement.put(SDJWTConstants.HASH, decoyDigest);
            result.add(decoyElement);
        });

        return result;
    }

    private String getHashedDisclosure(SDJWTEAAClaim claim, DigestAlgorithm digestAlgorithm) {
        byte[] digest = DSSUtils.digest(digestAlgorithm, buildDisclosure(claim, digestAlgorithm).getBytesToBeSigned());
        return DSSJsonUtils.toBase64Url(digest);
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
    public SDJWTEAADisclosure buildDisclosure(SDJWTEAAClaim claim, DigestAlgorithm digestAlgorithm) {
        return disclosureBuilder.build(claim.getName(), getClaimValue(claim, digestAlgorithm), claim.getSalt());
    }

    @Override
    public List<SDJWTEAADisclosure> buildDisclosures(SDJWTEAAPayloadParameters payloadParameters) {
        DigestAlgorithm digestAlgorithm = payloadParameters.getDigestAlgorithm() != null ?
                payloadParameters.getDigestAlgorithm() : DigestAlgorithm.SHA256;

        SDJWTEAAClaimObject root = getRootPayloadObject(payloadParameters);

        List<SDJWTEAADisclosure> disclosures = new ArrayList<>();
        collectDisclosures(root, digestAlgorithm, disclosures);

        return disclosures;
    }

    private void collectDisclosures(final SDJWTEAAClaim claim,
                                    final DigestAlgorithm digestAlgorithm,
                                    final List<SDJWTEAADisclosure> disclosures) {

        if (claim.isSelectivelyDisclosable()) {
            disclosures.add(buildDisclosure(claim, digestAlgorithm));
        }

        // TODO : improve ?
        if (claim instanceof SDJWTEAAClaimObject) {

            SDJWTEAAClaimObject objectClaim = (SDJWTEAAClaimObject) claim;

            for (SDJWTEAAClaim child : objectClaim.getChildren()) {
                collectDisclosures(child, digestAlgorithm, disclosures);
            }

        } else if (claim instanceof SDJWTEAAClaimArray) {

            SDJWTEAAClaimArray arrayClaim = (SDJWTEAAClaimArray) claim;

            for (SDJWTEAAClaim element : arrayClaim.getElements()) {
                collectDisclosures(element, digestAlgorithm, disclosures);
            }

        } else if (claim.getValue() instanceof Map) {
            collectDisclosures(toEAAClaimObject((Map<?, ?>) claim.getValue()), digestAlgorithm, disclosures);

        } else if (claim.getValue() instanceof Collection) {
            collectDisclosures(toEAAClaimArray((Collection<?>) claim.getValue()), digestAlgorithm, disclosures);

        } else if (claim.getValue() instanceof Object[]) {
            collectDisclosures(toEAAClaimArray((Object[]) claim.getValue()), digestAlgorithm, disclosures);
        }
    }

}
