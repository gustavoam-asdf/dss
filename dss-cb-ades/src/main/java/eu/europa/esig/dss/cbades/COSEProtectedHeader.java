package eu.europa.esig.dss.cbades;

import co.nstant.in.cbor.CborDecoder;
import co.nstant.in.cbor.CborException;
import co.nstant.in.cbor.model.ByteString;
import co.nstant.in.cbor.model.DataItem;
import co.nstant.in.cbor.model.MajorType;
import eu.europa.esig.dss.spi.exception.IllegalInputException;
import eu.europa.esig.dss.utils.Utils;

import java.util.List;

public class COSEProtectedHeader extends COSEMap {

    private ByteString header;

    public COSEProtectedHeader(final ByteString header) {
        super(parseProtectedHeader(header));
        this.header = header;
    }

    private static co.nstant.in.cbor.model.Map parseProtectedHeader(ByteString header) {
        try {
            List<DataItem> dataItems = CborDecoder.decode(header.getBytes());
            if (Utils.collectionSize(dataItems) == 0) {
                return CBORUtils.EMPTY_MAP;
            } else if (Utils.collectionSize(dataItems) > 1) {
                throw new IllegalInputException("Protected header root shall consist of one data object!");
            }
            DataItem dataItem = dataItems.iterator().next();
            if (MajorType.MAP != dataItem.getMajorType()) {
                throw new IllegalInputException("Protected header shall be of Map type!");
            }
            return (co.nstant.in.cbor.model.Map) dataItem;

        } catch (CborException e) {
            throw new IllegalInputException(String.format("Unable to parse protected header: %s", e.getMessage()), e);
        }
    }

    public ByteString getByteString() {
        return header;
    }

    public byte[] getBytes() {
        return header.getBytes();
    }

}
