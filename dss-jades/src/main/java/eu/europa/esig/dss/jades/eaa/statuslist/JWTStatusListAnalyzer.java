package eu.europa.esig.dss.jades.eaa.statuslist;

import eu.europa.esig.dss.jades.validation.JWSCompactDocumentAnalyzer;
import eu.europa.esig.dss.model.DSSDocument;
import eu.europa.esig.dss.spi.eaa.EAAStatusToken;
import eu.europa.esig.dss.spi.eaa.statuslist.StatusListAnalyzer;
import eu.europa.esig.dss.spi.signature.AdvancedSignature;
import eu.europa.esig.dss.utils.Utils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Objects;

public class JWTStatusListAnalyzer implements StatusListAnalyzer {

    private static final Logger LOG = LoggerFactory.getLogger(JWTStatusListAnalyzer.class);

    /**
     * The document to be validated
     */
    protected DSSDocument eaaDocument;

    /**
     * Empty constructor
     */
    protected JWTStatusListAnalyzer() {
        // empty
    }

    /**
     * Default constructor
     *
     * @param eaaDocument {@link DSSDocument} to validate
     */
    public JWTStatusListAnalyzer(DSSDocument eaaDocument) {
        this.eaaDocument = eaaDocument;
    }

    @Override
    public boolean isSupported(DSSDocument eaaDocument) {
        return new JWSCompactDocumentAnalyzer().isSupported(eaaDocument);
    }

    @Override
    public EAAStatusToken getStatusToken() {
        Objects.requireNonNull(eaaDocument, "EAA Document cannot be null!");

        /*
         * 8.2. Status List Response
         *
         * The body of such an HTTP response contains the raw Status List Token,
         * that means the binary encoding as defined in Section 9.2.1 of [RFC8392] for
         * a Status List Token in CWT format and the JWS Compact Serialization form for
         * a Status List Token in JWT format.
         */
        JWSCompactDocumentAnalyzer documentAnalyzer = new JWSCompactDocumentAnalyzer(eaaDocument);
        List<AdvancedSignature> signatures = documentAnalyzer.getSignatures();
        if (Utils.collectionSize(signatures) == 1) {


        } else {
            LOG.warn("One and only one signature shall be present within JWT Status List body! " +
                    "Found : {} signatures", Utils.collectionSize(signatures));
        }
        return null;
    }

}
