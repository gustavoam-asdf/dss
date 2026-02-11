package eu.europa.esig.dss.eaa.jwt.validation;

import eu.europa.esig.dss.eaa.jwt.SDJWTConstants;
import eu.europa.esig.dss.eaa.jwt.SDJWTUtils;
import eu.europa.esig.dss.enumerations.DigestAlgorithm;
import eu.europa.esig.dss.jades.DSSJsonUtils;
import eu.europa.esig.dss.model.eaa.SelectivelyDisclosableClaim;
import eu.europa.esig.dss.spi.eaa.EAAPayload;
import eu.europa.esig.dss.utils.Utils;
import org.jose4j.json.JsonUtil;
import org.jose4j.lang.JoseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * This class implements a user-friendly access to the EAA payload elements of the SD-JWT token
 *
 */
public class SDJWTPayload implements EAAPayload {

    private static final long serialVersionUID = -4552799683587409954L;

    private static final Logger LOG = LoggerFactory.getLogger(SDJWTPayload.class);

    /** Represents a map of objects defined within EAA payload */
    private final Map<String, Object> payloadMap;

    /**
     * Default constructor
     *
     * @param payload {@link String}
     */
    public SDJWTPayload(String payload) {
        this.payloadMap = parseJsonString(payload);
    }

    private static Map<String, Object> parseJsonString(String payload) {
        try {
            return JsonUtil.parseJson(payload);
        } catch (JoseException e) {
            LOG.warn("Unable to parse EAA payload : {}", e.getMessage(), e);
            return Collections.emptyMap();
        }
    }

    @Override
    public String getCategory() {
        return DSSJsonUtils.getAsString(payloadMap, SDJWTConstants.CATEGORY);
    }

    @Override
    public List<SelectivelyDisclosableClaim> getSelectiveDisclosableClaims() {
        final List<SelectivelyDisclosableClaim> result = new ArrayList<>();

        // 1. Extract _sd header hashes
        List<?> sdHashes = DSSJsonUtils.getAsList(payloadMap, SDJWTConstants.SD);
        if (Utils.isCollectionNotEmpty(sdHashes)) {
            for (Object sdHashValue : sdHashes) {
                if (!(sdHashValue instanceof String)) {
                    LOG.warn("Value of the '{}' shall be represented by a String!", SDJWTConstants.SD);
                    continue;
                }
                String sdHash = (String) sdHashValue;
                if (!DSSJsonUtils.isBase64UrlEncoded(sdHash)) {
                    LOG.warn("Value of the '{}' shall be base64url encoded!", SDJWTConstants.SD);
                    continue;
                }
                SelectivelyDisclosableClaim sdClaim = new SelectivelyDisclosableClaim();
                sdClaim.setDigestValue(DSSJsonUtils.fromBase64Url(sdHash));
                result.add(sdClaim);
            }
        }
        // 2. Look for selectively disclosable array items
        for (Map.Entry<String, Object> entry : payloadMap.entrySet()) {
            String headerName = entry.getKey();
            /*
             * 4.2.1. Disclosures for Object Properties (draft-ietf-oauth-selective-disclosure-jwt-22)
             *
             * 2. The claim name, or key, as it would be used in a regular JWT payload.
             * It MUST be a string and MUST NOT be _sd, ..., or a claim name existing in
             * the object as a permanently disclosed claim.
             */
            if (SDJWTConstants.SD.equals(headerName) || SDJWTConstants.HASH.equals(headerName)) {
                continue;
            }

            // Currently only selectively disclosable array entries are supported.
            // It is not very clear if other options are possible too.
            Object value = entry.getValue();
            result.addAll(SDJWTUtils.getNestedSelectivelyDisclosableClaims(headerName, value));
        }

        return result;
    }

    @Override
    public DigestAlgorithm getSelectiveDisclosableClaimDigestAlgorithm() {
        String digestAlgoName = DSSJsonUtils.getAsString(payloadMap, SDJWTConstants.SD_ALG);
        if (digestAlgoName != null) {
            try {
                return DigestAlgorithm.forSdJwtId(digestAlgoName);
            } catch (IllegalArgumentException e) {
                LOG.warn("The value '{}' of '{}' is not supported!", digestAlgoName, SDJWTConstants.SD_ALG);
                return null;
            }
        }
        /*
         * 4.2.3. Hashing Disclosures (draft-ietf-oauth-selective-disclosure-jwt-22)
         *
         * For embedding references to the Disclosures in the SD-JWT, each Disclosure is hashed
         * using the hash algorithm specified in the _sd_alg claim described in Section 4.1.1,
         * or SHA-256 if no algorithm is specified.
         */
        return DigestAlgorithm.SHA256;
    }

}
