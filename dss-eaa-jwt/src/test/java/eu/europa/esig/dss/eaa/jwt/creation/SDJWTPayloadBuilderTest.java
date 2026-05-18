package eu.europa.esig.dss.eaa.jwt.creation;

import eu.europa.esig.dss.eaa.common.creation.claim.EAAClaim;
import eu.europa.esig.dss.eaa.common.creation.claim.EAAClaimArray;
import eu.europa.esig.dss.eaa.common.creation.claim.EAAClaimObject;
import eu.europa.esig.dss.eaa.jwt.SDJWTConstants;
import eu.europa.esig.dss.enumerations.DigestAlgorithm;
import eu.europa.esig.dss.jades.DSSJsonUtils;
import eu.europa.esig.dss.model.InMemoryDocument;
import org.jose4j.json.JsonUtil;
import org.jose4j.lang.JoseException;
import org.junit.jupiter.api.Test;

import java.util.Date;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class SDJWTPayloadBuilderTest {

    @Test
    void buildSDJWTEAAPayload() throws JoseException {
        SDJWTSaltGenerator saltGenerator = new SDJWTDefaultSaltGenerator();
        final SDJWTPayloadBuilder builder = new SDJWTPayloadBuilder();

        final EAAClaimObject addressClaim = new EAAClaimObject("address");
        addressClaim.addChild(new EAAClaim("country", "LU"));
        addressClaim.addChild(new EAAClaim("street", "Test street"));
        addressClaim.addChild(new EAAClaim("city", "Luxembourg", true, saltGenerator.generateSalt()));
        addressClaim.addChild(new EAAClaim("postal-code", "4000", true, saltGenerator.generateSalt()));

        final EAAClaimObject subObject = new EAAClaimObject("sub-addressClaim");
        subObject.addChild(new EAAClaim("sub-key", "sub-value"));
        subObject.addChild(new EAAClaim("sub-key-hidden", "sub-value-hidden", true, saltGenerator.generateSalt()));
        addressClaim.addChild(subObject);

        final EAAClaimArray pets = new EAAClaimArray("pets");
        pets.addElement(new EAAClaim(null, "dog", true, saltGenerator.generateSalt()));
        pets.addElement(new EAAClaim(null, "cat", true, saltGenerator.generateSalt()));
        addressClaim.addChild(pets);

        final EAAClaimArray nationalities = new EAAClaimArray("nationalities");
        nationalities.addElement(new EAAClaim("DE"));
        nationalities.addElement(new EAAClaim("EN"));
        nationalities.addElement(new EAAClaim("FR"));
        nationalities.addElement(new EAAClaim(null, "LU", true, saltGenerator.generateSalt()));

        final EAAClaimArray nationalities2 = new EAAClaimArray("nationalities2", true, saltGenerator.generateSalt());
        nationalities2.addElement(new EAAClaim(null, "DE", true, saltGenerator.generateSalt()));
        nationalities2.addElement(new EAAClaim(null, "EN", true, saltGenerator.generateSalt()));
        nationalities2.addElement(new EAAClaim(null, "FR", true, saltGenerator.generateSalt()));

        builder.addClaim(addressClaim);
        builder.addClaim(nationalities);
        builder.addClaim(nationalities2);

        final EAAClaim nonSelectivelyDisclosableClaim = new EAAClaim("visible-claim", "visible-value");
        builder.addClaim(nonSelectivelyDisclosableClaim);

        final EAAClaim selectivelyDisclosableClaim = new EAAClaim("test-name", "test-value", true, saltGenerator.generateSalt());
        builder.addClaim(selectivelyDisclosableClaim);

        final Date now = new Date();
        final Date expiration = new Date(now.getTime() + 3600 * 1000);
        builder.setIssuanceDate(now);
        builder.setExpirationDate(expiration);
        builder.setSubject("test-subject");
        builder.setIssuer("test-issuer");

        final InMemoryDocument payload = (InMemoryDocument) builder.buildPayload();

        assertTrue(DSSJsonUtils.isJsonDocument(payload));

        final Map<String, Object> payloadMap = JsonUtil.parseJson(new String(payload.getBytes()));
        assertEquals(9, payloadMap.size());
        assertEquals("test-subject", payloadMap.get(SDJWTConstants.SUBJECT));
        assertEquals("test-issuer", payloadMap.get(SDJWTConstants.ISSUER));
        assertEquals(DigestAlgorithm.SHA256.getSDJWTId(), payloadMap.get(SDJWTConstants._SD_ALG));
        assertEquals(now.toInstant().getEpochSecond(), payloadMap.get(SDJWTConstants.ISSUED_AT));
        assertEquals(expiration.toInstant().getEpochSecond(), payloadMap.get(SDJWTConstants.EXPIRATION_TIME));

        List<String> digests = (List<String>) payloadMap.get(SDJWTConstants._SD);
        assertEquals(2, digests.size());

        assertNull(payloadMap.get(selectivelyDisclosableClaim.getName()));
        assertEquals(nonSelectivelyDisclosableClaim.getValue(), payloadMap.get(nonSelectivelyDisclosableClaim.getName()));

        final Map<String, Object> addressMap = (Map<String, Object>) payloadMap.get(addressClaim.getName());
        assertEquals(5, addressMap.size());

    }
}
