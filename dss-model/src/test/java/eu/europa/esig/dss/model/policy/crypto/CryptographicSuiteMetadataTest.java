package eu.europa.esig.dss.model.policy.crypto;

import org.junit.jupiter.api.Test;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class CryptographicSuiteMetadataTest {

    @Test
    void test() {
        CryptographicSuiteMetadata metadata = new CryptographicSuiteMetadata();

        String policyName = "TestPolicy";
        String policyOID = "1.2.3.4";
        String policyURI = "http://example.com/policy";
        String publisherName = "Test Publisher";
        String publisherAddress = "123 Test Street";
        String publisherURI = "http://example.com/publisher";
        Date policyIssueDate = new Date(1000L);
        Date nextUpdate = new Date(2000L);
        String usage = "Test Usage";
        String version = "1.0";
        String lang = "en";
        String id = "ID-123";

        metadata.setPolicyName(policyName);
        metadata.setPolicyOID(policyOID);
        metadata.setPolicyURI(policyURI);
        metadata.setPublisherName(publisherName);
        metadata.setPublisherAddress(publisherAddress);
        metadata.setPublisherURI(publisherURI);
        metadata.setPolicyIssueDate(policyIssueDate);
        metadata.setNextUpdate(nextUpdate);
        metadata.setUsage(usage);
        metadata.setVersion(version);
        metadata.setLang(lang);
        metadata.setId(id);

        assertEquals(policyName, metadata.getPolicyName());
        assertEquals(policyOID, metadata.getPolicyOID());
        assertEquals(policyURI, metadata.getPolicyURI());
        assertEquals(publisherName, metadata.getPublisherName());
        assertEquals(publisherAddress, metadata.getPublisherAddress());
        assertEquals(publisherURI, metadata.getPublisherURI());
        assertEquals(policyIssueDate, metadata.getPolicyIssueDate());
        assertEquals(nextUpdate, metadata.getNextUpdate());
        assertEquals(usage, metadata.getUsage());
        assertEquals(version, metadata.getVersion());
        assertEquals(lang, metadata.getLang());
        assertEquals(id, metadata.getId());
    }

    @Test
    void testNull() {
        CryptographicSuiteMetadata metadata = new CryptographicSuiteMetadata();

        assertNull(metadata.getPolicyName());
        assertNull(metadata.getPolicyOID());
        assertNull(metadata.getPolicyURI());
        assertNull(metadata.getPublisherName());
        assertNull(metadata.getPublisherAddress());
        assertNull(metadata.getPublisherURI());
        assertNull(metadata.getPolicyIssueDate());
        assertNull(metadata.getNextUpdate());
        assertNull(metadata.getUsage());
        assertNull(metadata.getVersion());
        assertNull(metadata.getLang());
        assertNull(metadata.getId());
    }

}
