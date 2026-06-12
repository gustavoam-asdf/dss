package eu.europa.esig.dss.eaa.revocation.cwt.validation.statuslist;

import eu.europa.esig.dss.enumerations.EAAStatus;
import eu.europa.esig.dss.model.DSSException;
import eu.europa.esig.dss.utils.Utils;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class CWTStatusListValidatorTest {

    @Test
    void cwtStatusListTest() {
        /* See draft-ietf-oauth-status-list-20 */

        String tokenHex =
                "d2845820a2012610781a6170706c69636174696f6e2f7374617475736c6973742b63" +
                        "7774a1044231325850a502782168747470733a2f2f6578616d706c652e636f6d2f73" +
                        "74617475736c697374732f31061a648c5bea041a8898dfea19fffe19a8c019fffda2" +
                        "646269747301636c73744a78dadbb918000217015d584093fa4d01032b18c35e2fe1" +
                        "101b77fd6cc9440022caa4694450c4e4e9feab4e99d1fa6d9772ce2bf3a12e0323de" +
                        "d7c982c5e101a5e67f0cbc1e2b6f57ce99c279";

        byte[] tokenBytes = Utils.fromHex(tokenHex);

        assertTrue(new CWTStatusListValidator().isSupported(tokenBytes));

        CWTStatusListValidator validator = new CWTStatusListValidator(tokenBytes);

        assertEquals(EAAStatus.INVALID, validator.getRevocationToken(0).getStatus());
        assertEquals(EAAStatus.VALID, validator.getRevocationToken(1).getStatus());
        assertEquals(EAAStatus.VALID, validator.getRevocationToken(2).getStatus());
        assertEquals(EAAStatus.INVALID, validator.getRevocationToken(3).getStatus());
        assertEquals(EAAStatus.INVALID, validator.getRevocationToken(4).getStatus());
        assertEquals(EAAStatus.INVALID, validator.getRevocationToken(5).getStatus());
        assertEquals(EAAStatus.VALID, validator.getRevocationToken(6).getStatus());
        assertEquals(EAAStatus.INVALID, validator.getRevocationToken(7).getStatus());

        assertEquals(EAAStatus.INVALID, validator.getRevocationToken(8).getStatus());
        assertEquals(EAAStatus.INVALID, validator.getRevocationToken(9).getStatus());
        assertEquals(EAAStatus.VALID, validator.getRevocationToken(10).getStatus());
        assertEquals(EAAStatus.VALID, validator.getRevocationToken(11).getStatus());
        assertEquals(EAAStatus.VALID, validator.getRevocationToken(12).getStatus());
        assertEquals(EAAStatus.INVALID, validator.getRevocationToken(13).getStatus());
        assertEquals(EAAStatus.VALID, validator.getRevocationToken(14).getStatus());
        assertEquals(EAAStatus.INVALID, validator.getRevocationToken(15).getStatus());

        Exception exception = assertThrows(DSSException.class, () -> validator.getRevocationToken(16));
        assertEquals("The position of the index '16' is out of bounds of " +
                "the obtained status list array with size '2' bytes (16 bits)!", exception.getMessage());
    }

}
