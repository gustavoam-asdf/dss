package eu.europa.esig.dss.eaa.jwt.validation;

import eu.europa.esig.dss.eaa.common.EAAUtils;
import eu.europa.esig.dss.eaa.jwt.SDJWTUtils;
import eu.europa.esig.dss.model.eaa.DisclosureValidation;
import eu.europa.esig.dss.model.eaa.claim.Claim;
import eu.europa.esig.dss.utils.Utils;
import org.jose4j.json.JsonUtil;
import org.jose4j.lang.JoseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Builds {@code eu.europa.esig.dss.eaa.jwt.validation.SDJWTPayload} from the provided payload
 *
 */
public class SDJWTPayloadBuilder {

    private static final Logger LOG = LoggerFactory.getLogger(SDJWTPayloadBuilder.class);

    /** Payload to parse */
    private final String payload;

    /** List of disclosure validations */
    private List<DisclosureValidation> disclosureValidations;

    /**
     * Default constructor
     *
     * @param payload {@link String}
     */
    public SDJWTPayloadBuilder(final String payload) {
        this.payload = payload;
    }

    /**
     * Sets disclosure validations
     *
     * @param disclosureValidations a list of {@link DisclosureValidation}s
     * @return this {@link SDJWTPayloadBuilder}
     */
    public SDJWTPayloadBuilder setDisclosureValidations(List<DisclosureValidation> disclosureValidations) {
        this.disclosureValidations = disclosureValidations;
        return this;
    }

    /**
     * Builds payload
     *
     * @return {@link SDJWTPayload}
     */
    public SDJWTPayload build() {
        Map<String, Object> payloadMap = parseJsonString(payload);
        Map<String, Claim> claimMap = toClaimMap(payloadMap);
        return new SDJWTPayload(claimMap, disclosureValidations);
    }

    private Map<String, Claim> toClaimMap(Map<String, Object> map) {
        if (Utils.isMapEmpty(map)) {
            return Collections.emptyMap();
        }
        final Map<String, Claim> claimMap = new HashMap<>();
        for (Map.Entry<String, Object> payloadEntry : map.entrySet()) {
            String claimName = payloadEntry.getKey();
            Claim claim = Claim.create(claimName, payloadEntry.getValue());
            claimMap.put(claimName, claim);
        }
        return claimMap;
    }

    private static Map<String, Object> parseJsonString(String payload) {
        try {
            return JsonUtil.parseJson(payload);
        } catch (JoseException e) {
            LOG.warn("Unable to parse EAA payload : {}", e.getMessage(), e);
            return Collections.emptyMap();
        }
    }

}
