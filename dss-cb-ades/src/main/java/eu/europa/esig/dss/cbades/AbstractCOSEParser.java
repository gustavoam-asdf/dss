package eu.europa.esig.dss.cbades;

import co.nstant.in.cbor.CborException;
import co.nstant.in.cbor.decoder.ArrayDecoder;
import eu.europa.esig.dss.cbades.cbor.CBORObject;
import eu.europa.esig.dss.cbades.cbor.CBORUtils;
import eu.europa.esig.dss.model.DSSDocument;
import eu.europa.esig.dss.model.DSSException;

import java.io.InputStream;
import java.util.Objects;

/**
 * Abstract class containing common methods for parsing COSE structures
 *
 */
public abstract class AbstractCOSEParser {

    /** The object to be parsed */
    protected final CBORObject cborObject;

    /**
     * The constructor to parse a CBORObject
     *
     * @param cborObject {@link CBORObject} to parse
     */
    protected AbstractCOSEParser(CBORObject cborObject) {
        Objects.requireNonNull(cborObject, "CBORObject cannot be null!");
        this.cborObject = cborObject;
    }

    /**
     * Parses CBOR {@code DSSDocument}
     *
     * @param document {@link DSSDocument}
     * @return {@link CBORObject}
     */
    protected static CBORObject parseCbor(DSSDocument document) {
        try {
            return CBORUtils.parseCbor(document);
        } catch (CborException e) {
            throw new DSSException(String.format("A parsing error of CBOR content occurred : %s", e.getMessage()), e);
        }
    }

    /**
     * Extended implementation of {@code ArrayDecoder}
     */
    protected static class DSSArrayDecoder extends ArrayDecoder {

        public DSSArrayDecoder(InputStream inputStream) {
            super(null, inputStream);
        }

        @Override
        protected long getLength(int initialByte) throws CborException {
            return super.getLength(initialByte);
        }

    }

}
