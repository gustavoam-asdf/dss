package eu.europa.esig.dss.eaa.jwt.creation;

import java.security.SecureRandom;

import org.jose4j.base64url.Base64Url;

public class SDJWTDefaultSaltGenerator implements SDJWTSaltGenerator {

    public static final int DEFAULT_SALT_LENGTH = 16;

    private final int saltLength;
    private final SecureRandom secureRandom = new SecureRandom();

    public SDJWTDefaultSaltGenerator() {
        this(DEFAULT_SALT_LENGTH);
    }

    public SDJWTDefaultSaltGenerator(int saltLength) {
        this.saltLength = saltLength;
    }

    @Override
    public String generateSalt() {
        byte[] bytes = new byte[saltLength];
        secureRandom.nextBytes(bytes);
        return Base64Url.encode(bytes);
    }
}
