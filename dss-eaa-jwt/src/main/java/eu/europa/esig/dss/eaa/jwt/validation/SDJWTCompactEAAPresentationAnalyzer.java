package eu.europa.esig.dss.eaa.jwt.validation;

import eu.europa.esig.dss.eaa.jwt.SDJWTCompactSerializationParser;
import eu.europa.esig.dss.eaa.jwt.SDJWTSerializationObject;
import eu.europa.esig.dss.model.DSSDocument;

/**
 * This class performs validation of an SD-JWT VC serialized using compact JWS serialization form
 *
 */
public class SDJWTCompactEAAPresentationAnalyzer extends AbstractSDJWTEAAPresentationAnalyzer {

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

}
