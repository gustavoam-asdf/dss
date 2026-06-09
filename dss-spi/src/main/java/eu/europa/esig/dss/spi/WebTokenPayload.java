package eu.europa.esig.dss.spi;

import java.util.Date;

/**
 * Represents a payload of a web token (e.g. RFC 7519 token or RFC 8392 CWT)
 *
 */
public interface WebTokenPayload {

    /**
     * Gets the value of the Issuer claim identifying the principal that issued the token.
     *
     * @return {@link String}
     */
    String getIssuer();

    /**
     * Gets the value of the Subject claim identifying the principal that is the subject of the token.
     *
     * @return {@link String}
     */
    String getSubject();

    /**
     * Gets the value of the Audience claim identifying the recipients that the token is intended for.
     *
     * @return {@link String}
     */
    String getAudience();

    /**
     * Gets the value of the Expiration Time claim identifying the expiration time on
     * or after which the token MUST NOT be accepted for processing.
     *
     * @return {@link String}
     */
    Date getExpirationTime();

    /**
     * Gets the value of the Not Before claim identifying the time before which the token
     * MUST NOT be accepted for processing.
     *
     * @return {@link String}
     */
    Date getNotBefore();

    /**
     * Gets the value of the Issued At claim identifying the time before which the token
     * MUST NOT be accepted for processing.
     *
     * @return {@link String}
     */
     Date getIssuedAt();

    /**
     * Gets the value of the token ID claim representing a unique identifier for the token.
     *
     * @return {@link String}
     */
    String getTokenId();
    
}
