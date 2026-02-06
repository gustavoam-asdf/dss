package eu.europa.esig.dss.eaa.jwt.validation;

import eu.europa.esig.dss.eaa.jwt.SDJWTConstants;
import eu.europa.esig.dss.jades.DSSJsonUtils;
import eu.europa.esig.dss.spi.eaa.EAAPayload;
import org.jose4j.json.JsonUtil;
import org.jose4j.lang.JoseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;
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

}
