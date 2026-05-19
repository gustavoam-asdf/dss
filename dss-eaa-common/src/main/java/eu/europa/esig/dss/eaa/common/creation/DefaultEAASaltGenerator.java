package eu.europa.esig.dss.eaa.common.creation;

import java.security.SecureRandom;

public class DefaultEAASaltGenerator implements EAASaltGenerator {

    public static final int DEFAULT_SALT_LENGTH = 16;

    private final int saltLength;
    private final SecureRandom secureRandom = new SecureRandom();

    public DefaultEAASaltGenerator() {
        this(DEFAULT_SALT_LENGTH);
    }

    public DefaultEAASaltGenerator(int saltLength) {
        this.saltLength = saltLength;
    }

    @Override
    public byte[] generateSalt() {
        byte[] bytes = new byte[saltLength];
        secureRandom.nextBytes(bytes);
        return bytes;
    }

    @Override
    public String generateSaltString() {
        return new String(generateSalt());
    }

}
