package eu.europa.esig.dss.cbades;

import co.nstant.in.cbor.CborEncoder;
import co.nstant.in.cbor.CborException;
import co.nstant.in.cbor.model.ByteString;
import eu.europa.esig.dss.model.DSSException;
import eu.europa.esig.dss.spi.DSSUtils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public final class CBORUtils {

    public static final ByteString EMPTY_BYTE_STRING = new ByteString(DSSUtils.EMPTY_BYTE_ARRAY);

    public static final co.nstant.in.cbor.model.Map EMPTY_MAP = new co.nstant.in.cbor.model.Map();

    public static final ByteString EMPTY_SERIALIZED_MAP;

    static {
        try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
            co.nstant.in.cbor.model.Map map = new co.nstant.in.cbor.model.Map();
            CborEncoder cborEncoder = new CborEncoder(baos);
            cborEncoder.encode(map);
            EMPTY_SERIALIZED_MAP = new ByteString(baos.toByteArray());
        } catch (IOException | CborException e) {
            throw new DSSException(String.format("An error occurred : %s", e.getMessage()), e);
        }
    }

    private CBORUtils() {
    }
}
