package eu.europa.esig.dss.lote.json.download;

import eu.europa.esig.dss.enumerations.DigestAlgorithm;
import eu.europa.esig.dss.jades.JWSCompactSerializationParser;
import eu.europa.esig.dss.lote.download.DownloadResult;
import eu.europa.esig.dss.model.DSSDocument;
import eu.europa.esig.dss.model.DSSException;
import eu.europa.esig.dss.model.Digest;

import java.util.Objects;
import java.util.function.Supplier;

/**
 * This class is used to process the result of document download process for JWS compact LoTE processing
 *
 */
public class LoTEJWSCompactDownloadTask implements Supplier<DownloadResult> {

    /** Default digest algorithm used for document integrity identification */
    private static final DigestAlgorithm DEFAULT_DIGEST_ALGORITHM = DigestAlgorithm.SHA256;

    /** The document */
    private final DSSDocument document;

    /** The URL to download the document from */
    private final String url;

    /**
     * Default constructor
     *
     * @param document {@link DSSDocument} to verify
     * @param url {@link String} to download the document from
     */
    public LoTEJWSCompactDownloadTask(DSSDocument document, String url) {
        Objects.requireNonNull(url, "The url is null");
        this.document = document;
        this.url = url;
    }

    @Override
    public DownloadResult get() {
        try {
            assertDocumentIsValidJSON(document);

            final Digest digest = document.getDigest(DEFAULT_DIGEST_ALGORITHM);
            return new DownloadResult(document, digest);
        } catch (DSSException e) {
            throw e;
        } catch (Exception e) {
            throw new DSSException(String.format("Unable to retrieve the content for url '%s'. Reason : '%s'", url, e.getMessage()), e);
        }
    }

    private void assertDocumentIsValidJSON(DSSDocument document) {
        if (document == null) {
            throw new NullPointerException(String.format("No document has been retrieved from URL '%s'!", url));
        }
        JWSCompactSerializationParser parser = new JWSCompactSerializationParser(document);
        if (!parser.isSupported()) {
            throw new DSSException(String.format("The document obtained from URL '%s' is not a valid JWS Compact Serialization signature!", url));
        }
    }

}
