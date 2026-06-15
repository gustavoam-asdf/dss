package eu.europa.esig.dss.eaa.jwt.validation;

import eu.europa.esig.dss.eaa.jwt.SDJWTCompactSerializationParser;
import eu.europa.esig.dss.eaa.jwt.SDJWTSerializationObject;
import eu.europa.esig.dss.model.DSSDocument;
import eu.europa.esig.dss.model.InMemoryDocument;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * This class performs validation of an SD-JWT VC serialized using compact JWS serialization form
 *
 */
public class SDJWTCompactEAAPresentationAnalyzer extends AbstractSDJWTEAAPresentationAnalyzer {

    private static final Logger LOG = LoggerFactory.getLogger(SDJWTCompactEAAPresentationAnalyzer.class);

    /**
     * Empty constructor
     */
    public SDJWTCompactEAAPresentationAnalyzer() {
        // empty
    }

    /**
     * Default constructor
     *
     * @param document {@link DSSDocument} to validate
     */
    public SDJWTCompactEAAPresentationAnalyzer(DSSDocument document) {
        super(document);
    }

    @Override
    public boolean isSupported(DSSDocument document) {
        SDJWTCompactSerializationParser parser = new SDJWTCompactSerializationParser(document);
        return parser.isSupported();
    }

    @Override
    protected SDJWTSerializationObject buildSDJWTSerializationObject() {
        SDJWTCompactSerializationParser parser = new SDJWTCompactSerializationParser(document);
        return parser.parse();
    }

    @Override
    protected DSSDocument getKeyBindingDetachedContent(SDJWTSerializationObject sdJwtSerializationObject) {
        try (InputStream is = document.openStream(); ByteArrayOutputStream baos = new ByteArrayOutputStream();
             ByteArrayOutputStream tail = new ByteArrayOutputStream()) {

            boolean tildeReached = false;
            byte[] buffer = new byte[8192];
            int len;

            while ((len = is.read(buffer)) != -1) {
                for (int i = 0; i < len; i++) {
                    byte b = buffer[i];

                    if (tildeReached) {
                        tail.write(b);
                        if (b == '~') {
                            tail.writeTo(baos);
                            tail.reset();
                        }

                    } else {
                        baos.write(b);
                        if (b == '~') {
                            tildeReached = true;
                        }
                    }
                }
            }

            return new InMemoryDocument(baos.toByteArray());

        } catch (IOException e) {
            LOG.warn("Unable to compute input for the key binding signature verification : {}", e.getMessage(), e);
            return null;
        }
    }

}
