package eu.europa.esig.dss.eaa.jwt.creation;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.jose4j.json.JsonUtil;
import org.jose4j.lang.JoseException;
import org.junit.jupiter.api.Test;

import eu.europa.esig.dss.eaa.jwt.SDJWTConstants;
import eu.europa.esig.dss.eaa.jwt.creation.claim.SDJWTArrayPresentableClaim;
import eu.europa.esig.dss.eaa.jwt.creation.claim.SDJWTObjectPresentableClaim;
import eu.europa.esig.dss.eaa.jwt.creation.claim.SDJWTPresentableClaim;
import eu.europa.esig.dss.enumerations.DigestAlgorithm;
import eu.europa.esig.dss.jades.DSSJsonUtils;
import eu.europa.esig.dss.model.InMemoryDocument;

class SDJWTPayloadBuilderTest {

    @Test
    void buildSDJWTEAAPayload() throws JoseException {
        SDJWTSaltGenerator saltGenerator = new SDJWTDefaultSaltGenerator();
        final SDJWTPayloadBuilder builder = new SDJWTPayloadBuilder();

        final SDJWTObjectPresentableClaim addressClaim = new SDJWTObjectPresentableClaim("address");
        addressClaim.addChild(new SDJWTPresentableClaim("country", "LU"));
        addressClaim.addChild(new SDJWTPresentableClaim("street", "Test street"));
        addressClaim.addChild(new SDJWTPresentableClaim("city", "Luxembourg", true, saltGenerator.generateSalt()));
        addressClaim.addChild(new SDJWTPresentableClaim("postal-code", "4000", true, saltGenerator.generateSalt()));

        final SDJWTObjectPresentableClaim subObject = new SDJWTObjectPresentableClaim("sub-addressClaim");
        subObject.addChild(new SDJWTPresentableClaim("sub-key", "sub-value"));
        subObject.addChild(new SDJWTPresentableClaim("sub-key-hidden", "sub-value-hidden", true, saltGenerator.generateSalt()));
        addressClaim.addChild(subObject);

        final SDJWTArrayPresentableClaim pets = new SDJWTArrayPresentableClaim("pets");
        pets.addElement(new SDJWTPresentableClaim(null, "dog", true, saltGenerator.generateSalt()));
        pets.addElement(new SDJWTPresentableClaim(null, "cat", true, saltGenerator.generateSalt()));
        addressClaim.addChild(pets);

        final SDJWTArrayPresentableClaim nationalities = new SDJWTArrayPresentableClaim("nationalities");
        nationalities.addElement(new SDJWTPresentableClaim("DE"));
        nationalities.addElement(new SDJWTPresentableClaim("EN"));
        nationalities.addElement(new SDJWTPresentableClaim("FR"));
        nationalities.addElement(new SDJWTPresentableClaim(null, "LU", true, saltGenerator.generateSalt()));

        final SDJWTArrayPresentableClaim nationalities2 = new SDJWTArrayPresentableClaim("nationalities2", true, saltGenerator.generateSalt());
        nationalities2.addElement(new SDJWTPresentableClaim(null, "DE", true, saltGenerator.generateSalt()));
        nationalities2.addElement(new SDJWTPresentableClaim(null, "EN", true, saltGenerator.generateSalt()));
        nationalities2.addElement(new SDJWTPresentableClaim(null, "FR", true, saltGenerator.generateSalt()));

        builder.addClaim(addressClaim);
        builder.addClaim(nationalities);
        builder.addClaim(nationalities2);

        final SDJWTPresentableClaim nonSelectivelyDisclosableClaim = new SDJWTPresentableClaim("visible-claim", "visible-value");
        builder.addClaim(nonSelectivelyDisclosableClaim);

        final SDJWTPresentableClaim selectivelyDisclosableClaim = new SDJWTPresentableClaim("test-name", "test-value", true, saltGenerator.generateSalt());
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
