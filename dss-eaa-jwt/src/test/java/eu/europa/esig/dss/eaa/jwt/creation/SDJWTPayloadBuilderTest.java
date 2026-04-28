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
import eu.europa.esig.dss.eaa.jwt.creation.claim.SDJWTStringPresentableClaim;
import eu.europa.esig.dss.enumerations.DigestAlgorithm;
import eu.europa.esig.dss.jades.DSSJsonUtils;
import eu.europa.esig.dss.model.InMemoryDocument;

class SDJWTPayloadBuilderTest {

    @Test
    void buildSDJWTEAAPayload() throws JoseException {
        SDJWTSaltGenerator saltGenerator = new SDJWTDefaultSaltGenerator();
        SDJWTEAAParameters parameters = new SDJWTEAAParameters();

        final SDJWTObjectPresentableClaim addressClaim = new SDJWTObjectPresentableClaim("address");
        addressClaim.addChild(new SDJWTStringPresentableClaim("country", "LU"));
        addressClaim.addChild(new SDJWTStringPresentableClaim("street", "Test street"));
        addressClaim.addChild(new SDJWTStringPresentableClaim("city", "Luxembourg", true, saltGenerator.generateSalt()));
        addressClaim.addChild(new SDJWTStringPresentableClaim("postal-code", "4000", true, saltGenerator.generateSalt()));

        final SDJWTObjectPresentableClaim subObject = new SDJWTObjectPresentableClaim("sub-addressClaim");
        subObject.addChild(new SDJWTStringPresentableClaim("sub-key", "sub-value"));
        subObject.addChild(new SDJWTStringPresentableClaim("sub-key-hidden", "sub-value-hidden", true, saltGenerator.generateSalt()));
        addressClaim.addChild(subObject);

        final SDJWTArrayPresentableClaim pets = new SDJWTArrayPresentableClaim("pets");
        pets.addElement(new SDJWTStringPresentableClaim(null, "dog", true, saltGenerator.generateSalt()));
        pets.addElement(new SDJWTStringPresentableClaim(null, "cat", true, saltGenerator.generateSalt()));
        addressClaim.addChild(pets);

        final SDJWTArrayPresentableClaim nationalities = new SDJWTArrayPresentableClaim("nationalities");
        nationalities.addElement(new SDJWTStringPresentableClaim("DE"));
        nationalities.addElement(new SDJWTStringPresentableClaim("EN"));
        nationalities.addElement(new SDJWTStringPresentableClaim("FR"));
        nationalities.addElement(new SDJWTStringPresentableClaim(null, "LU", true, saltGenerator.generateSalt()));

        final SDJWTArrayPresentableClaim nationalities2 = new SDJWTArrayPresentableClaim("nationalities2", true, saltGenerator.generateSalt());
        nationalities2.addElement(new SDJWTStringPresentableClaim(null, "DE", true, saltGenerator.generateSalt()));
        nationalities2.addElement(new SDJWTStringPresentableClaim(null, "EN", true, saltGenerator.generateSalt()));
        nationalities2.addElement(new SDJWTStringPresentableClaim(null, "FR", true, saltGenerator.generateSalt()));

        parameters.addClaim(addressClaim);
        parameters.addClaim(nationalities);
        parameters.addClaim(nationalities2);

        final SDJWTPresentableClaim nonSelectivelyDisclosableClaim = new SDJWTStringPresentableClaim("visible-claim", "visible-value");
        parameters.addClaim(nonSelectivelyDisclosableClaim);

        final SDJWTPresentableClaim selectivelyDisclosableClaim = new SDJWTStringPresentableClaim("test-name", "test-value", true, saltGenerator.generateSalt());
        parameters.addClaim(selectivelyDisclosableClaim);

        final Date now = new Date();
        final Date expiration = new Date(now.getTime() + 3600 * 1000);
        parameters.setIssuanceDate(now);
        parameters.setExpirationDate(expiration);
        parameters.setSubject("test-subject");
        parameters.setIssuer("test-issuer");

        final SDJWTPayloadBuilder builder = new SDJWTPayloadBuilder();
        final InMemoryDocument payload = (InMemoryDocument) builder.buildPayload(parameters);

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
        assertEquals(nonSelectivelyDisclosableClaim.getValueAsString(), payloadMap.get(nonSelectivelyDisclosableClaim.getName()));

        final Map<String, Object> addressMap = (Map<String, Object>) payloadMap.get(addressClaim.getName());
        assertEquals(5, addressMap.size());

    }
}
