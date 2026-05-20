package eu.europa.esig.dss.eaa.jwt.creation;

import eu.europa.esig.dss.eaa.common.creation.DefaultEAASaltGenerator;
import eu.europa.esig.dss.eaa.common.creation.EAASaltGenerator;
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
        EAASaltGenerator saltGenerator = new DefaultEAASaltGenerator();
        final SDJWTEAAPayloadParameters parameters = new SDJWTEAAPayloadParameters();

        final SDJWTEAAClaimObject addressClaim = new SDJWTEAAClaimObject("address");
        addressClaim.addChild(new SDJWTEAAClaim("country", "LU"));
        addressClaim.addChild(new SDJWTEAAClaim("street", "Test street"));
        addressClaim.addChild(new SDJWTEAAClaim("city", "Luxembourg", true, saltGenerator.generateSaltString()));
        addressClaim.addChild(new SDJWTEAAClaim("postal-code", "4000", true, saltGenerator.generateSaltString()));

        final SDJWTEAAClaimObject subObject = new SDJWTEAAClaimObject("sub-addressClaim");
        subObject.addChild(new SDJWTEAAClaim("sub-key", "sub-value"));
        subObject.addChild(new SDJWTEAAClaim("sub-key-hidden", "sub-value-hidden", true, saltGenerator.generateSaltString()));
        addressClaim.addChild(subObject);

        final SDJWTEAAClaimArray pets = new SDJWTEAAClaimArray("pets");
        pets.addElement(new SDJWTEAAClaim(null, "dog", true, saltGenerator.generateSaltString()));
        pets.addElement(new SDJWTEAAClaim(null, "cat", true, saltGenerator.generateSaltString()));
        addressClaim.addChild(pets);

        final SDJWTEAAClaimArray nationalities = new SDJWTEAAClaimArray("nationalities");
        nationalities.addElement(new SDJWTEAAClaim("DE"));
        nationalities.addElement(new SDJWTEAAClaim("EN"));
        nationalities.addElement(new SDJWTEAAClaim("FR"));
        nationalities.addElement(new SDJWTEAAClaim(null, "LU", true, saltGenerator.generateSaltString()));

        final SDJWTEAAClaimArray nationalities2 = new SDJWTEAAClaimArray("nationalities2", true, saltGenerator.generateSaltString());
        nationalities2.addElement(new SDJWTEAAClaim(null, "DE", true, saltGenerator.generateSaltString()));
        nationalities2.addElement(new SDJWTEAAClaim(null, "EN", true, saltGenerator.generateSaltString()));
        nationalities2.addElement(new SDJWTEAAClaim(null, "FR", true, saltGenerator.generateSaltString()));

        parameters.addClaim(addressClaim);
        parameters.addClaim(nationalities);
        parameters.addClaim(nationalities2);

        final SDJWTEAAClaim nonSelectivelyDisclosableClaim = new SDJWTEAAClaim("visible-claim", "visible-value");
        parameters.addClaim(nonSelectivelyDisclosableClaim);

        final SDJWTEAAClaim selectivelyDisclosableClaim = new SDJWTEAAClaim("test-name", "test-value", true, saltGenerator.generateSaltString());
        parameters.addClaim(selectivelyDisclosableClaim);

        final Date now = new Date();
        final Date expiration = new Date(now.getTime() + 3600 * 1000);
        parameters.setIssuanceDate(now);
        parameters.setExpirationDate(expiration);
        parameters.setSubject("test-subject");
        parameters.setIssuer("test-issuer");

        SDJWTPayloadBuilder payloadBuilder = new SDJWTPayloadBuilder();
        final InMemoryDocument payload = (InMemoryDocument) payloadBuilder.buildPayload(parameters);

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
