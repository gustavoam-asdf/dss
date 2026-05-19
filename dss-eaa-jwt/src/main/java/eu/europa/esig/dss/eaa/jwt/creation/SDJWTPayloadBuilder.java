package eu.europa.esig.dss.eaa.jwt.creation;

import eu.europa.esig.dss.eaa.common.creation.AbstractEAAPayloadBuilder;
import eu.europa.esig.dss.eaa.common.creation.EAASaltGenerator;
import eu.europa.esig.dss.eaa.common.creation.claim.EAAClaim;
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
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

public class SDJWTPayloadBuilder extends AbstractEAAPayloadBuilder {

    private final Map<String, SDJWTEAAClaim> claims = new LinkedHashMap<>();
    private final List<String> decoyDigests = new ArrayList<>();

    /** Builds disclosures */
    private SDJWTDisclosureBuilder disclosureBuilder = new DefaultSDJWTDisclosureBuilder();

    public void addClaim(final SDJWTEAAClaim claim) {
        claims.put(getName(claim), claim);
    }

    public SDJWTEAAClaim addClaim(final String name, final Object value) {
        return addClaim(name, value, false, null);
    }

    public SDJWTEAAClaim addClaim(final String name, final Object value, final boolean isSelectivelyDisclosable) {
        String salt = isSelectivelyDisclosable ? DSSJsonUtils.toBase64Url(saltGenerator.generateSalt()) : null;
        return addClaim(name, value, isSelectivelyDisclosable, salt);
    }

    public SDJWTEAAClaim addClaim(final String name, final Object value, final boolean isSelectivelyDisclosable, final String salt) {
        SDJWTEAAClaim claim = new SDJWTEAAClaim(name, value, isSelectivelyDisclosable, salt);
        claims.put(getName(claim), claim);
        return claim;
    }

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

        List<String> hashedDisclosures = new ArrayList<>();
        for (final SDJWTEAAClaim claim : claimsSelectivelyDisclosable) {
            hashedDisclosures.add(getHashedDisclosure(claim, digestAlgorithm == null ? DigestAlgorithm.SHA256 : digestAlgorithm));
        }
        hashedDisclosures.addAll(decoyDigests);
        if (!hashedDisclosures.isEmpty()) {
            Collections.shuffle(hashedDisclosures);
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

        if (claim instanceof SDJWTEAAClaimObject) {
            return getEAAClaimObjectValue((SDJWTEAAClaimObject) claim, digestAlgorithm);
        } else if (claim instanceof SDJWTEAAClaimArray) {
            return getEAAClaimArrayValue((SDJWTEAAClaimArray) claim, digestAlgorithm);
        } else if (claim.getValue() instanceof Map) {
            return getMapClaimValue(claim, (Map<?, ?>) claim.getValue(), digestAlgorithm, parentClaim);
        } else if (claim.getValue() instanceof Object[]) {
            return this.getArrayClaimValue(claim, (Object[]) claim.getValue(), digestAlgorithm, parentClaim);
        }

        return claim.getValue();
    }

    private Object getArrayClaimValue(final SDJWTEAAClaim claim, final Object[] array, final DigestAlgorithm digestAlgorithm, final SDJWTEAAClaim parentClaim) {
        SDJWTEAAClaimArray claimArray = new SDJWTEAAClaimArray(claim.getName(), claim.isSelectivelyDisclosable(), claim.getSalt());

        for (final Object item : array) {
            if (item instanceof SDJWTEAAClaim) {
                SDJWTEAAClaim sdjwteaaClaim = (SDJWTEAAClaim) item;
                if (sdjwteaaClaim.getName() != null) {
                    throw new DSSException("The name of an SD-JWT claim must be null in an array");
                }
                claimArray.addElement(sdjwteaaClaim);
            } else if (item instanceof EAAClaim) {
                EAAClaim eaaClaim = (EAAClaim) item;
                if (eaaClaim.getName() != null) {
                    throw new DSSException("The name of an SD-JWT claim must be null in an array");
                }
                claimArray.addElement(new SDJWTEAAClaim(eaaClaim.getValue()));
            } else {
                claimArray.addElement(new SDJWTEAAClaim(item));
            }
        }

        return getClaimValue(claimArray, digestAlgorithm, parentClaim);
    }

    private Object getMapClaimValue(final SDJWTEAAClaim claim, final Map<?, ?> map, final DigestAlgorithm digestAlgorithm, final SDJWTEAAClaim parentClaim) {
        SDJWTEAAClaimObject claimObject = new SDJWTEAAClaimObject(claim.getName(), claim.isSelectivelyDisclosable(), claim.getSalt());
        for (Map.Entry<?, ?> entry : map.entrySet()) {
            if (!(entry.getKey() instanceof String)) {
                throw new DSSException("The name of an SD-JWT claim must be of type String");
            }

            String entryName = (String) entry.getKey();
            Object value = entry.getValue();

            if (value instanceof SDJWTEAAClaim) {
                SDJWTEAAClaim sdjwteaaClaim = (SDJWTEAAClaim) value;
                if (Utils.areStringsEqual(entryName, claim.getName())) {
                    throw new DSSException("For a Map, the key of the entry is expected to have the same value as the name of the claim");
                }
                claimObject.addChild(sdjwteaaClaim);
            } else if (value instanceof EAAClaim) {
                EAAClaim eaaClaim = (EAAClaim) value;
                if (Utils.areStringsEqual(entryName, getName(eaaClaim))) {
                    throw new DSSException("For a Map, the key of the entry is expected to have the same value as the name of the claim");
                }
                claimObject.addChild(new SDJWTEAAClaim(entryName, value, false, null));
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

        selectivelyDisclosableClaims.addAll(objectClaim.getDecoyDigests());
        if (!selectivelyDisclosableClaims.isEmpty()) {
            Collections.shuffle(selectivelyDisclosableClaims);
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

        arrayClaim.getDecoyDigests().forEach(decoyDigest -> {
            Map<String, String> decoyElement = new LinkedHashMap<>();
            decoyElement.put(SDJWTConstants.HASH, decoyDigest);
            result.add(decoyElement);
        });

        return result;
    }

    private String getHashedDisclosure(SDJWTEAAClaim claim, DigestAlgorithm digestAlgorithm) {
        byte[] digest = DSSUtils.digest(digestAlgorithm, buildDisclosure(claim, digestAlgorithm).getBytesToBeSigned());
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
    public SDJWTEAADisclosure buildDisclosure(SDJWTEAAClaim claim, DigestAlgorithm digestAlgorithm) {
        return disclosureBuilder.build(claim.getName(), getClaimValue(claim, digestAlgorithm, null), claim.getSalt());
    }

    public void setSaltGenerator(final EAASaltGenerator saltGenerator) {
        this.saltGenerator = saltGenerator;
    }

    public void setDigestAlgorithm(final DigestAlgorithm digestAlgorithm) {
        this.digestAlgorithm = digestAlgorithm;
    }

    public DigestAlgorithm getDigestAlgorithm() {
        return digestAlgorithm;
    }

    public void addDecoyDigest(String digest) {
        decoyDigests.add(digest);
    }

    public void addDecoyDigests(Collection<String> digests) {
        decoyDigests.addAll(digests);
    }

    private String getName(EAAClaim eaaClaim) {
        Object name = eaaClaim.getName();
        if (name instanceof String) {
            return (String) name;
        }
        throw new IllegalArgumentException("Name of an SD-JWT EAAClaim shall be of String type!");
    }

}
