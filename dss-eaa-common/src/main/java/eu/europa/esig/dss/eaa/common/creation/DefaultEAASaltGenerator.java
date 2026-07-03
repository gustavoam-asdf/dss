/**
 * DSS - Digital Signature Services
 * Copyright (C) 2015 European Commission, provided under the CEF programme
 * <p>
 * This file is part of the "DSS - Digital Signature Services" project.
 * <p>
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 * <p>
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 * <p>
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301  USA
 */
package eu.europa.esig.dss.eaa.common.creation;

import java.security.SecureRandom;

/**
 * Default implementation to build a salt for EAA creation
 *
 */
public class DefaultEAASaltGenerator implements EAASaltGenerator {

    /** Salt length used by default (16) */
    public static final int DEFAULT_SALT_LENGTH = 16;

    /** Length of the salt */
    private final int saltLength;

    /** SecureRandom to be used on salt generation */
    private final SecureRandom secureRandom = new SecureRandom();

    /**
     * Constructor to instantiate DefaultEAASaltGenerator with default salt length (16)
     */
    public DefaultEAASaltGenerator() {
        this(DEFAULT_SALT_LENGTH);
    }

    /**
     * Constructor to instantiate DefaultEAASaltGenerator with custom salt length
     *
     * @param saltLength length of the salt
     */
    public DefaultEAASaltGenerator(int saltLength) {
        this.saltLength = saltLength;
    }

    @Override
    public byte[] generateSalt() {
        byte[] bytes = new byte[saltLength];
        secureRandom.nextBytes(bytes);
        return bytes;
    }

}
