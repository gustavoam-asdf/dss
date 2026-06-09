package eu.europa.esig.dss.jades.eaa.statuslist;

import eu.europa.esig.dss.enumerations.EAAStatus;
import eu.europa.esig.dss.model.DSSException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class JWTStatusListValidatorTest {

    @Test
    void jwtStatusListTest() {
        /* See draft-ietf-oauth-status-list-20 */

        String tokenB64Url =
                "eyJhbGciOiJFUzI1NiIsImtpZCI6IjEyIiwidHlwIjoic3RhdHVzbGlzdCtqd3QifQ.e" +
                "yJleHAiOjIyOTE3MjAxNzAsImlhdCI6MTY4NjkyMDE3MCwiaXNzIjoiaHR0cHM6Ly9le" +
                "GFtcGxlLmNvbSIsInN0YXR1c19saXN0Ijp7ImJpdHMiOjEsImxzdCI6ImVOcmJ1UmdBQ" +
                "WhjQlhRIn0sInN1YiI6Imh0dHBzOi8vZXhhbXBsZS5jb20vc3RhdHVzbGlzdHMvMSIsI" +
                "nR0bCI6NDMyMDB9.2lKUUNG503R9htu4aHAYi7vjmr3sgApbfoDvPrl65N3URUO1EYqq" +
                "Ql45Jfzd-Av4QzlKa3oVALpLwOEUOq-U_g";

        assertTrue(new JWTStatusListValidator().isSupported(tokenB64Url.getBytes()));

        JWTStatusListValidator validator = new JWTStatusListValidator(tokenB64Url.getBytes());

        assertEquals(EAAStatus.INVALID, validator.getStatusToken(0).getStatus());
        assertEquals(EAAStatus.VALID, validator.getStatusToken(1).getStatus());
        assertEquals(EAAStatus.VALID, validator.getStatusToken(2).getStatus());
        assertEquals(EAAStatus.INVALID, validator.getStatusToken(3).getStatus());
        assertEquals(EAAStatus.INVALID, validator.getStatusToken(4).getStatus());
        assertEquals(EAAStatus.INVALID, validator.getStatusToken(5).getStatus());
        assertEquals(EAAStatus.VALID, validator.getStatusToken(6).getStatus());
        assertEquals(EAAStatus.INVALID, validator.getStatusToken(7).getStatus());

        assertEquals(EAAStatus.INVALID, validator.getStatusToken(8).getStatus());
        assertEquals(EAAStatus.INVALID, validator.getStatusToken(9).getStatus());
        assertEquals(EAAStatus.VALID, validator.getStatusToken(10).getStatus());
        assertEquals(EAAStatus.VALID, validator.getStatusToken(11).getStatus());
        assertEquals(EAAStatus.VALID, validator.getStatusToken(12).getStatus());
        assertEquals(EAAStatus.INVALID, validator.getStatusToken(13).getStatus());
        assertEquals(EAAStatus.VALID, validator.getStatusToken(14).getStatus());
        assertEquals(EAAStatus.INVALID, validator.getStatusToken(15).getStatus());

        Exception exception = assertThrows(DSSException.class, () -> validator.getStatusToken(16));
        assertEquals("The position of the index '16' is out of bounds of " +
                "the obtained status list array with size '2' bytes (16 bits)!", exception.getMessage());
    }

}
