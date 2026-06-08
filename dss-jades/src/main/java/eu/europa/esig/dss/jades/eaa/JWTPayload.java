package eu.europa.esig.dss.jades.eaa;

import eu.europa.esig.dss.jades.DSSJsonUtils;
import eu.europa.esig.dss.spi.WebTokenPayload;

import java.util.Date;
import java.util.Map;

/**
 * Represents a payload of the RFC 7519 "JSON Web Token (JWT)"
 *
 */
public class JWTPayload implements WebTokenPayload {

    /** Map representing the JSON payload */
    private final Map<String, Object> payload;

    /**
     * Default constructor
     *
     * @param payload map
     */
    public JWTPayload(final Map<String, Object> payload) {
        this.payload = payload;
    }

    /**
     * Gets the value of the 'iss' (Issuer) claim identifying the principal that issued the JWT.
     *
     * @return {@link String}
     */
    public String getIssuer() {
        return getAsString(JWTClaimNames.ISS);
    }

    /**
     * Gets the value of the 'sub' (Subject) claim identifying the principal that is the subject of the JWT.
     *
     * @return {@link String}
     */
    public String getSubject() {
        return getAsString(JWTClaimNames.SUB);
    }

    /**
     * Gets the value of the 'aud' (Audience) claim identifying the recipients that the JWT is intended for.
     *
     * @return {@link String}
     */
    public String getAudience() {
        return getAsString(JWTClaimNames.AUD);
    }

    /**
     * Gets the value of the 'exp' (Expiration Time) claim identifying the expiration time on
     * or after which the JWT MUST NOT be accepted for processing.
     *
     * @return {@link String}
     */
    public Date getExpirationTime() {
        return getAsDate(JWTClaimNames.EXP);
    }

    /**
     * Gets the value of the 'nbf' (Not Before) claim identifying the time before which the JWT
     * MUST NOT be accepted for processing.
     *
     * @return {@link String}
     */
    public Date getNotBefore() {
        return getAsDate(JWTClaimNames.NBF);
    }

    /**
     * Gets the value of the 'iat' (Issued At) claim identifying the time before which the JWT
     * MUST NOT be accepted for processing.
     *
     * @return {@link String}
     */
    public Date getIssuedAt() {
        return getAsDate(JWTClaimNames.IAT);
    }

    /**
     * Gets the value of the 'jti' (JWT ID) claim representing a unique identifier for the JWT.
     *
     * @return {@link String}
     */
    public String getJWTId() {
        return getAsString(JWTClaimNames.JTI);
    }

    /**
     * Gets a claim name as {@code String} for the given header {@code name}
     * If the header with the given {@code name} is not present or the value is not of String type, returns empty string.
     *
     * @param name {@link String}
     * @return {@link String}
     */
    public String getAsString(String name) {
        return DSSJsonUtils.getAsString(payload, name);
    }

    /**
     * Gets a claim name as {@code Number} for the given header {@code name}
     * If the header with the given {@code name} is not present or the value is not of Number type, returns null.
     *
     * @param name {@link String}
     * @return {@link Number}
     */
    public Number getAsNumber(String name) {
        return DSSJsonUtils.getAsNumber(payload, name);
    }

    /**
     * Gets a claim name as {@code Date} for the given header {@code name}
     * If the header with the given {@code name} is not present or the value is not of NumericDate type, returns null.
     *
     * @param name {@link String}
     * @return {@link Date}
     */
    public Date getAsDate(String name) {
        return DSSJsonUtils.getAsNumericDate(payload, name);
    }

    /**
     * Gets a claim name as {@code Map} for the given header {@code name}
     * If the header with the given {@code name} is not present or the value is not of JSON Object type, returns null.
     *
     * @param name {@link String}
     * @return {@link Map}
     */
    public Map<?, ?> getAsMap(String name) {
        return DSSJsonUtils.getAsMap(payload, name);
    }

}
